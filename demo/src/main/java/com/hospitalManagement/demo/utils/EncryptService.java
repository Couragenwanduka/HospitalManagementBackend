package com.hospitalManagement.demo.utils;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;

@Service
public class EncryptService {

    private static final String AES = "AES/GCM/NoPadding";
    private static final int TAG_LENGTH_BIT = 128;
    public static final int IV_LENGTH_BYTE = 12;

    private final SecretKey secretKey;

    // Load key from application.properties
    public EncryptService(@Value("${encryption.secret-key}") String key) {
        byte[] decodedKey = Base64.getDecoder().decode(key);
        this.secretKey = new SecretKeySpec(decodedKey, "AES");
    }

    public String encrypt(String plainText, byte[] iv) throws Exception {
        Cipher cipher = Cipher.getInstance(AES);
        GCMParameterSpec spec = new GCMParameterSpec(TAG_LENGTH_BIT, iv);
        cipher.init(Cipher.ENCRYPT_MODE, secretKey, spec);
        byte[] encrypted = cipher.doFinal(plainText.getBytes(StandardCharsets.UTF_8));
        return Base64.getEncoder().encodeToString(encrypted);
    }

    public String decrypt(String cipherText, byte[] iv) throws Exception {
        Cipher cipher = Cipher.getInstance(AES);
        GCMParameterSpec spec = new GCMParameterSpec(TAG_LENGTH_BIT, iv);
        cipher.init(Cipher.DECRYPT_MODE, secretKey, spec);
        byte[] decoded = Base64.getDecoder().decode(cipherText);
        return new String(cipher.doFinal(decoded), StandardCharsets.UTF_8);
    }

    public byte[] generateIV() {
        byte[] iv = new byte[IV_LENGTH_BYTE];
        new SecureRandom().nextBytes(iv);
        return iv;
    }
    public String hash(String value) throws NoSuchAlgorithmException {
    MessageDigest digest = MessageDigest.getInstance("SHA-256");
    byte[] hashBytes = digest.digest(value.getBytes(StandardCharsets.UTF_8));
    return Base64.getEncoder().encodeToString(hashBytes);
}
}