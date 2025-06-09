package ir.ac.ut.ece.ie.miobook.utils;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;


public class PasswordHasher {

    public static String generateSalt() {
        byte[] salt = new byte[16];
        new SecureRandom().nextBytes(salt);
        return Base64.getEncoder().encodeToString(salt);
    }

    public static String hashPassword(String password, String salt) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] saltedPassword = password.getBytes(StandardCharsets.UTF_8);
            byte[] saltBytes = Base64.getDecoder().decode(salt);
            byte[] combined = new byte[saltedPassword.length + saltBytes.length];

            System.arraycopy(saltedPassword, 0, combined, 0, saltedPassword.length);
            System.arraycopy(saltBytes, 0, combined, saltedPassword.length, saltBytes.length);

            byte[] hash = digest.digest(combined);
            return Base64.getEncoder().encodeToString(hash);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("SHA-256 algorithm not available.", e);
        }
    }
}