package com.movie.entity;

import lombok.Data;
import java.util.Date;

@Data
public class User {
    private Integer userId;
    private String username;
    private String password;
    private String email;
    private String avatar;
    private Integer status;
    private Date createTime;
    private Date updateTime;
}