package com.movie.service;

import com.movie.dto.LoginRequest;
import com.movie.dto.ProfileUpdateRequest;
import com.movie.dto.RegisterRequest;
import com.movie.entity.User;
import com.movie.mapper.UserMapper;
import com.movie.util.JwtUtil;
import com.movie.util.MD5Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class UserService {

    @Autowired
    private UserMapper userMapper;

    public Map<String, Object> register(RegisterRequest request) {
        Map<String, Object> result = new HashMap<>();

        if (userMapper.findByUsername(request.getUsername()) != null) {
            result.put("success", false);
            result.put("message", "用户名已存在");
            return result;
        }

        if (userMapper.findByEmail(request.getEmail()) != null) {
            result.put("success", false);
            result.put("message", "邮箱已被注册");
            return result;
        }

        User user = new User();
        user.setUsername(request.getUsername());
        user.setPassword(MD5Util.md5(request.getPassword()));
        user.setEmail(request.getEmail());

        userMapper.insert(user);

        result.put("success", true);
        result.put("message", "注册成功");
        result.put("userId", user.getUserId());
        return result;
    }

    public Map<String, Object> login(LoginRequest request) {
        Map<String, Object> result = new HashMap<>();

        User user = userMapper.findByUsername(request.getUsername());
        if (user == null) {
            result.put("success", false);
            result.put("message", "用户名不存在");
            return result;
        }

        if (!user.getPassword().equals(MD5Util.md5(request.getPassword()))) {
            result.put("success", false);
            result.put("message", "密码错误");
            return result;
        }

        String token = JwtUtil.generateToken(user.getUserId(), user.getUsername());

        // Java 9+ 可以使用 Map.of()
        Map<String, Object> userInfo = Map.of(
                "userId", user.getUserId(),
                "username", user.getUsername(),
                "email", user.getEmail()
        );

        Map<String, Object> data = Map.of(
                "token", token,
                "user", userInfo
        );

        result.put("success", true);
        result.put("message", "登录成功");
        result.put("data", data);
        return result;
    }

    public User getUserById(Integer userId) {
        return userMapper.findByUserId(userId);
    }

    public Map<String, Object> updateProfile(Integer userId, ProfileUpdateRequest request) {
        Map<String, Object> result = new HashMap<>();

        User user = userMapper.findByUserId(userId);
        if (user == null) {
            result.put("success", false);
            result.put("message", "用户不存在");
            return result;
        }

        // 更新邮箱
        if (request.getEmail() != null && !request.getEmail().isEmpty()) {
            // 检查邮箱是否已被其他用户使用
            User existingUser = userMapper.findByEmail(request.getEmail());
            if (existingUser != null && !existingUser.getUserId().equals(userId)) {
                result.put("success", false);
                result.put("message", "邮箱已被其他用户使用");
                return result;
            }
            user.setEmail(request.getEmail());
        }

        // 更新密码
        if (request.getPassword() != null && !request.getPassword().isEmpty()) {
            user.setPassword(MD5Util.md5(request.getPassword()));
        }

        userMapper.updateUser(user);

        result.put("success", true);
        result.put("message", "资料更新成功");
        return result;
    }
}