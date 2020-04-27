package com.eiv.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class SerializationUtils {

    public static byte[] serialize(Object state) {
        ObjectOutputStream oos = null;
        try {
            ByteArrayOutputStream bos = new ByteArrayOutputStream(512);
            oos = new ObjectOutputStream(bos);
            oos.writeObject(state);
            oos.flush();
            return bos.toByteArray();
        }
        catch (IOException e) {
            throw new IllegalArgumentException(e);
        }
        finally {
            if (oos != null) {
                try {
                    oos.close();
                }
                catch (IOException e) {
                    // eat it
                }
            }
        }
    }
    
    public static String calcularHash(byte[] value) {
        if (value == null) {
            return null;
        }
        MessageDigest digest;
        try {
            digest = MessageDigest.getInstance("MD5");
        }
        catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException("MD5 algorithm not available.  Fatal (should be in the JDK).");
        }

        byte[] bytes = digest.digest(value);
        return String.format("%032x", new BigInteger(1, bytes));
    }
    
    public static String genKey(Object state) {
        byte[] serialized = serialize(state);
        String hashed = calcularHash(serialized);
        return hashed;
    }
}
