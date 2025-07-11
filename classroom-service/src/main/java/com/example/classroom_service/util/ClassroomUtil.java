package com.example.classroom_service.util;

import java.security.SecureRandom;

public class ClassroomUtil {
    private static final String CHARACTERS = "ABCDEFGHJKLMNPQRSTUVWXYZ23456789"; // tránh 0, O, I, 1
    private static final int DEFAULT_LENGTH = 6;
    private static final SecureRandom random = new SecureRandom();

    public static String generateCode(int length) {
        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            sb.append(CHARACTERS.charAt(random.nextInt(CHARACTERS.length())));
        }
        return sb.toString();
    }

    public static String generateCode() {
        return generateCode(DEFAULT_LENGTH);
    }
}
