package com.craft.assignment.carrental.utils;

import org.mindrot.jbcrypt.BCrypt;

public class HashUtils {

    public static String hashPassword(String password) {
        // Generate a salt
        String salt = BCrypt.gensalt();
        // Hash the password with the salt
        return BCrypt.hashpw(password, salt);
    }

    public static  boolean verifyPassword(String password, String hashedPassword) {
        return BCrypt.checkpw(password, hashedPassword);
    }
}
