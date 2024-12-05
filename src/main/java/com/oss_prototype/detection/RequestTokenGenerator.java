package com.oss_prototype.detection;

import lombok.extern.slf4j.Slf4j;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

@Slf4j
public class RequestTokenGenerator {
    public static String generate(final String input) throws NoSuchAlgorithmException {
        final MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] hash = digest.digest(input.getBytes(StandardCharsets.UTF_8));
        String token = Base64.getEncoder().encodeToString(hash);
        log.info("generated token: {}", token);
        return token;
    }
}
