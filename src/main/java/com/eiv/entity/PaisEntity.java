package com.eiv.entity;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import com.eiv.util.HashKeyGenerator;

@Entity
@Table(name = "paises")
public class PaisEntity implements Serializable {

    private static final long serialVersionUID = -8555442095663748853L;
    
    @Id
    @GeneratedValue(generator = "pais-generator")
    @GenericGenerator(
            name = "pais-generator", 
            strategy = "com.eiv.util.HashKeyGenerator",
            parameters = { 
                    @Parameter(name = HashKeyGenerator.COMPOSITE_KEY, value = "false"),
                    @Parameter(name = HashKeyGenerator.ID_FIELD, value = "id")})
    private long id;
    
    private String nombre;
    
    public PaisEntity() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + (int) (id ^ (id >>> 32));
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        PaisEntity other = (PaisEntity) obj;
        if (id != other.id) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "PaisEntity [id=" + id + ", nombre=" + nombre + "]";
    }
}
