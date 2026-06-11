package com.movie.dto;

import lombok.Data;

@Data
public class ReplyRequest {
    private Integer parentCommentId;  // 被回复的评论ID
    private String content;           // 回复内容
}