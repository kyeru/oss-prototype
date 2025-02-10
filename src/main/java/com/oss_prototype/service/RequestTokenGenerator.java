package com.oss_prototype.service;

import com.oss_prototype.request.TaskRequest;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

public class RequestTokenGenerator {
    public static String generate(final TaskRequest request) throws NoSuchAlgorithmException {
        final MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] hash = digest.digest(request.toString().getBytes(StandardCharsets.UTF_8));
        String token = Base64.getEncoder().encodeToString(hash);
        return token;
    }
}
