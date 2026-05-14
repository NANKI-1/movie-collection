package com.movie.dto;

import lombok.Data;

@Data
public class CommentRequest {
    private Integer movieId;
    private Double rating;
    private String content;
}