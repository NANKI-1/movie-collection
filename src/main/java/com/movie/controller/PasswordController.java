package com.movie.controller;

import com.movie.dto.ApiResponse;
import com.movie.dto.ResetPasswordRequest;
import com.movie.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/password")
public class PasswordController {

    @Autowired
    private UserService userService;

    @PostMapping("/reset")
    public ApiResponse<?> resetPassword(@RequestBody ResetPasswordRequest request) {
        if (request.getEmail() == null || request.getEmail().isEmpty() ||
                request.getCode() == null || request.getCode().isEmpty() ||
                request.getNewPassword() == null || request.getNewPassword().isEmpty()) {
            return ApiResponse.error(400, "所有字段都不能为空");
        }

        if (request.getNewPassword().length() < 6) {
            return ApiResponse.error(400, "密码长度至少6位");
        }

        Map<String, Object> result = userService.resetPassword(request);
        boolean success = (Boolean) result.get("success");

        if (success) {
            return ApiResponse.success((String) result.get("message"));
        }

        return ApiResponse.error(400, (String) result.get("message"));
    }
}