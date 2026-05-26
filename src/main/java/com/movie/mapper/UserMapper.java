package com.movie.mapper;

import com.movie.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface UserMapper {
    User findByUsername(@Param("username") String username);
    User findByEmail(@Param("email") String email);
    User findByUserId(@Param("userId") Integer userId);
    int insert(User user);
    int updatePassword(@Param("userId") Integer userId, @Param("password") String password);
    int updateUser(User user);
    int updateEmail(@Param("userId") Integer userId, @Param("email") String email);
    int updateUsername(@Param("userId") Integer userId, @Param("username") String username);

}
