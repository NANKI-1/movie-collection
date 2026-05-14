package com.movie.controller;

import com.movie.dto.ApiResponse;
import com.movie.dto.LoginRequest;
import com.movie.dto.ProfileUpdateRequest;
import com.movie.dto.RegisterRequest;
import com.movie.service.UserService;
import com.movie.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private JwtUtil jwtUtil;

    private Integer getUserIdFromToken(HttpServletRequest request) {
        String token = request.getHeader("Authorization");
        if (token != null && token.startsWith("Bearer ")) {
            token = token.substring(7);
            return jwtUtil.getUserIdFromToken(token);
        }
        return null;
    }
    @PostMapping("/register")
    public ApiResponse<?> register(@RequestBody RegisterRequest request) {
        Map<String, Object> result = userService.register(request);
        boolean success = (Boolean) result.get("success");
        if (success) {
            String message = (String) result.get("message");
            Object userId = result.get("userId");
            // 使用 HashMap 包装返回值，避免泛型问题
            Map<String, Object> data = new HashMap<>();
            data.put("userId", userId);
            return ApiResponse.success(message, data);
        }
        return ApiResponse.error(400, (String) result.get("message"));
    }

    @PostMapping("/login")
    public ApiResponse<?> login(@RequestBody LoginRequest request) {
        Map<String, Object> result = userService.login(request);
        boolean success = (Boolean) result.get("success");
        if (success) {
            Object data = result.get("data");
            return ApiResponse.success(data);
        }
        return ApiResponse.error(401, (String) result.get("message"));
    }

    @PutMapping("/update")
    public ApiResponse<?> updateProfile(@RequestBody ProfileUpdateRequest request, HttpServletRequest req) {
        Integer userId = getUserIdFromToken(req);
        if (userId == null) {
            return ApiResponse.error(401, "未登录");
        }
        Map<String, Object> result = userService.updateProfile(userId, request);
        boolean success = (Boolean) result.get("success");
        if (success) {
            return ApiResponse.success((String) result.get("message"));
        }
        return ApiResponse.error(400, (String) result.get("message"));
    }

}