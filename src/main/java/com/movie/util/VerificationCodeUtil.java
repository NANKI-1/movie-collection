package com.movie.util;

import java.security.SecureRandom;

public class VerificationCodeUtil {

    private static final String CHARACTERS = "0123456789";
    private static final int CODE_LENGTH = 6;
    private static final SecureRandom random = new SecureRandom();

    /**
     * 生成6位数字验证码
     */
    public static String generateCode() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < CODE_LENGTH; i++) {
            sb.append(CHARACTERS.charAt(random.nextInt(CHARACTERS.length())));
        }
        return sb.toString();
    }
}