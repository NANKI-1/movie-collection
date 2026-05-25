package com.movie.dto;

import lombok.Data;

@Data
public class UpdateEmailRequest {
    private String newEmail;
    private String code;
}