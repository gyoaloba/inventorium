package dev.gracco.inventorium.utils;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.security.SecureRandom;
import java.util.Base64;

public class Security {
    public static String encrypt(String password) throws Exception {
        SecureRandom random = new SecureRandom();
        byte[] salt = new byte[16];
        random.nextBytes(salt);

        int iterations = 10000;
        int keyLength = 256;

        PBEKeySpec spec = new PBEKeySpec(password.toCharArray(), salt, iterations, keyLength);
        SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");

        byte[] hash = factory.generateSecret(spec).getEncoded();

        String saltBase64 = Base64.getEncoder().encodeToString(salt);
        String hashBase64 = Base64.getEncoder().encodeToString(hash);
        return saltBase64 + "$" + hashBase64;
    }

    public static boolean decrypt(String password, String storedHash) throws Exception {
        String[] parts = storedHash.split("\\$");
        if (parts.length != 2) throw new IllegalArgumentException("Invalid stored hash format.");

        String saltBase64 = parts[0];
        String storedPasswordHashBase64 = parts[1];

        byte[] salt = Base64.getDecoder().decode(saltBase64);
        byte[] storedPasswordHash = Base64.getDecoder().decode(storedPasswordHashBase64);

        int iterations = 10000;
        int keyLength = 256;

        PBEKeySpec spec = new PBEKeySpec(password.toCharArray(), salt, iterations, keyLength);
        SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");

        byte[] enteredPasswordHash = factory.generateSecret(spec).getEncoded();

        return java.util.Arrays.equals(storedPasswordHash, enteredPasswordHash);
    }
}
