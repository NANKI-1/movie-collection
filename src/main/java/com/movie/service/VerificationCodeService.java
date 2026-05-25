package com.movie.service;

import org.springframework.stereotype.Service;
import java.util.HashMap;
import java.util.Map;

@Service
public class VerificationCodeService {

    private static final long CODE_EXPIRE_MINUTES = 5;  // 5分钟过期
    private static final long SEND_INTERVAL_SECONDS = 60;  // 发送间隔60秒

    // 使用内存存储（临时方案，重启后会丢失）
    private final Map<String, String> codeMap = new HashMap<>();
    private final Map<String, Long> expireMap = new HashMap<>();
    private final Map<String, Long> sendTimeMap = new HashMap<>();

    /**
     * 保存验证码
     */
    public void saveCode(String email, String code) {
        codeMap.put(email, code);
        expireMap.put(email, System.currentTimeMillis() + CODE_EXPIRE_MINUTES * 60 * 1000);
        System.out.println("保存验证码 - 邮箱: " + email + ", 验证码: " + code);
    }

    /**
     * 验证验证码是否正确
     */
    public boolean verifyCode(String email, String code) {
        Long expireTime = expireMap.get(email);
        if (expireTime == null || System.currentTimeMillis() > expireTime) {
            System.out.println("验证码已过期 - 邮箱: " + email);
            return false;
        }
        String savedCode = codeMap.get(email);
        boolean isValid = savedCode != null && savedCode.equals(code);
        System.out.println("验证验证码 - 邮箱: " + email + ", 结果: " + isValid);
        return isValid;
    }

    /**
     * 删除验证码
     */
    public void deleteCode(String email) {
        codeMap.remove(email);
        expireMap.remove(email);
        System.out.println("删除验证码 - 邮箱: " + email);
    }

    /**
     * 检查是否可以发送验证码（防止频繁发送）
     */
    public boolean canSendCode(String email) {
        Long lastSendTime = sendTimeMap.get(email);
        if (lastSendTime == null) {
            return true;
        }
        boolean canSend = System.currentTimeMillis() - lastSendTime > SEND_INTERVAL_SECONDS * 1000;
        System.out.println("检查发送限制 - 邮箱: " + email + ", 可以发送: " + canSend);
        return canSend;
    }

    /**
     * 标记验证码已发送
     */
    public void markCodeSent(String email) {
        sendTimeMap.put(email, System.currentTimeMillis());
        System.out.println("标记发送时间 - 邮箱: " + email);
    }
}