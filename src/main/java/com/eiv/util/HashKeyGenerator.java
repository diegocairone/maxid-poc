package com.eiv.util;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Properties;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import javax.persistence.Column;
import javax.persistence.Table;
import javax.transaction.Status;
import javax.transaction.Synchronization;

import org.hibernate.HibernateException;
import org.hibernate.MappingException;
import org.hibernate.Transaction;
import org.hibernate.engine.jdbc.connections.spi.JdbcConnectionAccess;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.id.Configurable;
import org.hibernate.id.IdentifierGenerator;
import org.hibernate.jdbc.AbstractReturningWork;
import org.hibernate.jdbc.WorkExecutor;
import org.hibernate.jdbc.WorkExecutorVisitable;
import org.hibernate.service.ServiceRegistry;
import org.hibernate.type.Type;

public class HashKeyGenerator implements IdentifierGenerator, Configurable {

    public static final String COMPOSITE_KEY = "compositeKey";
    public static final String ID_FIELD = "idField";
    public static final String USE_HASH = "useHash";
    
    private boolean isCompositeKey;
    private String idField;
    private String capitalizedIdField;
    private Type identifierType;
    private boolean useHash;
    
    @Override
    public void configure(
            Type type, Properties params, ServiceRegistry serviceRegistry) throws MappingException {
        
        this.isCompositeKey = "true".equals(params.getProperty(COMPOSITE_KEY));
        this.useHash = "true".equals(params.getProperty(USE_HASH));
        this.idField = params.getProperty(ID_FIELD);
        this.identifierType = type;
        this.capitalizedIdField = idField == null || idField.isEmpty() ? idField 
                : idField.substring(0, 1).toUpperCase() + idField.substring(1);
    }

    @Override
    public Serializable generate(
            SharedSessionContractImplementor session, Object object) throws HibernateException {
        
        WorkExecutorVisitable<Serializable> work = new AbstractReturningWork<Serializable>() {

            @Override
            public Serializable execute(Connection connection) throws SQLException {
                if (isCompositeKey) {
                    return compositeKey(connection, object);
                } else {
                    return simpleKey(connection, object);
                }
            }
        };
        
        Transaction transaction = session.accessTransaction();
        
        try {
            JdbcConnectionAccess connectionAccess = session.getJdbcCoordinator()
                    .getJdbcSessionOwner()
                    .getJdbcConnectionAccess();
            
            Connection connection = connectionAccess.obtainConnection();
            connection.setAutoCommit(false);
            
            Serializable result = work.accept(new WorkExecutor<>(), connection);
            
            transaction.registerSynchronization(new Synchronization() {
                
                @Override
                public void beforeCompletion() {
                    
                }
                
                @Override
                public void afterCompletion(int status) {
                    try {
                        if (Status.STATUS_COMMITTED == status) {
                            connection.commit();
                                
                        } else {
                            connection.rollback();
                        }
                        connection.setAutoCommit(true);
                        connectionAccess.releaseConnection(connection);
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }
                }
            });
            
            return result;
            
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    
    private Serializable simpleKey(
            Connection connection, Object object) throws HibernateException {
        
        Table table = object.getClass().getAnnotation(Table.class);
        String tableName = table.name();
        
        try {
            Long ultValor = ultValor(tableName, connection);
            return ultValor;
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }
    
    private Serializable compositeKey(
            Connection connection, Object object) throws HibernateException {

        Class<?> clazz = object.getClass();
        Table table = clazz.getAnnotation(Table.class);
        String tableName = table.name();

        String keyName = compositeKeyName(tableName, object);
        
        try {
            Long ultValor = ultValor(keyName, connection);
            
            Serializable pk = extractPkField(object);
            Class<?> type = pk.getClass().getDeclaredField(idField).getType();
            pk.getClass().getMethod("set" + capitalizedIdField, type).invoke(pk, ultValor);
        
            updateIdField(object, pk, ultValor);
            
            return pk;
            
        } catch (IllegalArgumentException | IllegalAccessException | InvocationTargetException 
                | NoSuchMethodException | SecurityException | NoSuchFieldException 
                | UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }
    
    private static boolean delay = true;
    
    private Long ultValor(String keyName, Connection conn) 
            throws UnsupportedEncodingException {

        String key = useHash 
                ? SerializationUtils.calcularHash(keyName.getBytes("UTF-8")) : keyName;
        
        String query = "SELECT ult_valor FROM sequence_table WHERE query_id = ? FOR UPDATE";
        
        try {
            conn.setAutoCommit(false);
            
            PreparedStatement stmtSelect = conn.prepareStatement(query);
            stmtSelect.setString(1, key);
            
            ResultSet rs = stmtSelect.executeQuery();
            long ultValor = 0;
            
            if (rs.next()) {
                ultValor = rs.getLong(1);
            }
            
            try {
                if (delay) {
                    delay = !delay;
                    TimeUnit.SECONDS.sleep(5);
                }
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            
            stmtSelect.close();
            
            if (ultValor == 0) {
                
                ultValor++;
                String insert = "INSERT INTO sequence_table (query_id, ult_valor) VALUES (?, ?)";
                
                PreparedStatement stmtInsert = conn.prepareStatement(insert);
                stmtInsert.setString(1, key);
                stmtInsert.setLong(2, ultValor);
                
                stmtInsert.executeUpdate();
                stmtInsert.close();
            
            } else {
                
                ultValor++;
                String update = "UPDATE sequence_table SET ult_valor = ? WHERE query_id = ?";
                
                PreparedStatement stmtUpdate = conn.prepareStatement(update);
                stmtUpdate.setLong(1, ultValor);
                stmtUpdate.setString(2, key);
                
                stmtUpdate.executeUpdate();
                stmtUpdate.close();
            }
            
            conn.commit();
            
            return ultValor;
            
        } catch (SQLException e) {
            //try {
            //    if (!conn.isClosed()) {
            //        conn.rollback();
            //    }
            //} catch (SQLException e2) {
            //    throw new RuntimeException(e2);
            //}
            throw new RuntimeException(e);
        }
    }
    
    private String compositeKeyName(String tableName, Object object) throws HibernateException {
        
        try {
            
            final Serializable pk = extractPkField(object);
                    
            String condition = Arrays.stream(pk.getClass().getMethods())
                    .filter(m -> m.getName().startsWith("get") 
                            && !m.getName().equals("get" + capitalizedIdField)
                            && !m.getName().equals("getClass"))
                    .map(m -> {
                        try {
                            return String.format("%s=%s", 
                                    m.getName().substring(3), m.invoke(pk).toString());
                        } catch (IllegalAccessException | IllegalArgumentException 
                                | InvocationTargetException e) {
                            throw new RuntimeException(e);
                        }
                    })
                    .collect(Collectors.joining(","));
            
            String keyName = String.format("%s,%s", tableName, condition);
            
            return keyName;
            
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    
    private Serializable extractPkField(Object object) 
            throws IllegalArgumentException, IllegalAccessException {
        
        Serializable pk = null;
        Field[] fields = object.getClass().getDeclaredFields();
        Class<?> type = identifierType.getReturnedClass();
        
        for (Field field : fields) {
            field.setAccessible(true);
            if (field.getType().equals(type)) {
                
                pk = (Serializable) field.get(object);
                field.setAccessible(false);
                break;
            }
        }
        
        return pk;
    }

    private void updateIdField(Object object, Serializable pk, Long ultValor) 
            throws IllegalArgumentException, IllegalAccessException, InvocationTargetException, NoSuchMethodException, SecurityException {
        
        Field[] pkFields = pk.getClass().getDeclaredFields();
        String dbColName = null;
        
        for (Field field : pkFields) {
            Column column = field.getAnnotation(Column.class);
            if (column != null && field.getName().equals(idField)) {
                dbColName = column.name(); 
            }
        }
        
        Field[] fields = object.getClass().getDeclaredFields();
        
        for (Field field : fields) {
            Column column = field.getAnnotation(Column.class);
            if (column != null && column.name().equals(dbColName)) {
            
                Class<?> type = field.getType();
                String setterName = "set" + field.getName().substring(0, 1).toUpperCase() + field.getName().substring(1);
                
                object.getClass().getMethod(setterName, type).invoke(object, ultValor);
                
                break;
            }
        }
    }
}
