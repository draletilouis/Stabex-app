package com.demo.banking_app.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import java.util.Base64;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;

@Component
public class DeterministicHasher {

    private final Mac mac;

    public DeterministicHasher(@Value("${crypto.salt}") String saltB64) {
        try {
            byte[] key = Base64.getDecoder().decode(saltB64);
            mac = Mac.getInstance("HmacSHA256");
            mac.init(new SecretKeySpec(key, "HmacSHA256"));
        } catch (Exception e) {
            throw new IllegalStateException("Failed to initialize hasher", e);
        }
    }

    public String hash(String value) {
        if (value == null) return null;
        byte[] out = mac.doFinal(value.getBytes(StandardCharsets.UTF_8));
        return Base64.getEncoder().encodeToString(out);
    }
}


