/*
 Navicat Premium Dump SQL

 Source Server         : localhost_3306
 Source Server Type    : MySQL
 Source Server Version : 80042 (8.0.42)
 Source Host           : localhost:3306
 Source Schema         : movie_system

 Target Server Type    : MySQL
 Target Server Version : 80042 (8.0.42)
 File Encoding         : 65001

 Date: 12/04/2026 15:50:35
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for movie_collection
-- ----------------------------
DROP TABLE IF EXISTS `movie_collection`;
CREATE TABLE `movie_collection`  (
  `collection_id` int NOT NULL AUTO_INCREMENT COMMENT '收藏ID',
  `user_id` int NOT NULL COMMENT '用户ID',
  `movie_id` int NOT NULL COMMENT '电影ID',
  `personal_rating` decimal(2, 1) NULL DEFAULT NULL COMMENT '个人评分(1-5)',
  `watch_status` enum('想看','已看','不感兴趣') CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '想看' COMMENT '观影状态',
  `private_review` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL COMMENT '私人影评(仅自己可见)',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '添加时间',
  `update_time` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  PRIMARY KEY (`collection_id`) USING BTREE,
  UNIQUE INDEX `uk_user_movie`(`user_id` ASC, `movie_id` ASC) USING BTREE,
  INDEX `movie_id`(`movie_id` ASC) USING BTREE,
  INDEX `idx_user_id`(`user_id` ASC) USING BTREE,
  INDEX `idx_watch_status`(`watch_status` ASC) USING BTREE,
  CONSTRAINT `movie_collection_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `user` (`user_id`) ON DELETE CASCADE ON UPDATE RESTRICT,
  CONSTRAINT `movie_collection_ibfk_2` FOREIGN KEY (`movie_id`) REFERENCES `movie_public` (`movie_id`) ON DELETE CASCADE ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '个人收藏表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of movie_collection
-- ----------------------------

-- ----------------------------
-- Table structure for movie_comment
-- ----------------------------
DROP TABLE IF EXISTS `movie_comment`;
CREATE TABLE `movie_comment`  (
  `comment_id` int NOT NULL AUTO_INCREMENT COMMENT '评价ID',
  `movie_id` int NOT NULL COMMENT '电影ID',
  `user_id` int NOT NULL COMMENT '用户ID',
  `rating` decimal(2, 1) NOT NULL COMMENT '评分(1-5)',
  `content` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '评价内容',
  `like_count` int NULL DEFAULT 0 COMMENT '点赞数',
  `reply_to` int NULL DEFAULT NULL COMMENT '回复的评价ID',
  `is_edited` tinyint(1) NULL DEFAULT 0 COMMENT '是否已编辑',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '发布时间',
  `update_time` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  PRIMARY KEY (`comment_id`) USING BTREE,
  INDEX `idx_movie_id`(`movie_id` ASC) USING BTREE,
  INDEX `idx_user_id`(`user_id` ASC) USING BTREE,
  INDEX `idx_create_time`(`create_time` DESC) USING BTREE,
  CONSTRAINT `movie_comment_ibfk_1` FOREIGN KEY (`movie_id`) REFERENCES `movie_public` (`movie_id`) ON DELETE CASCADE ON UPDATE RESTRICT,
  CONSTRAINT `movie_comment_ibfk_2` FOREIGN KEY (`user_id`) REFERENCES `user` (`user_id`) ON DELETE CASCADE ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '公开评价表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of movie_comment
-- ----------------------------

-- ----------------------------
-- Table structure for movie_public
-- ----------------------------
DROP TABLE IF EXISTS `movie_public`;
CREATE TABLE `movie_public`  (
  `movie_id` int NOT NULL AUTO_INCREMENT COMMENT '电影ID',
  `tmdb_id` int NULL DEFAULT NULL COMMENT 'TMDB数据库中的电影ID',
  `movie_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '电影名称',
  `director` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '导演/演员',
  `year` int NULL DEFAULT NULL COMMENT '上映年份',
  `poster_url` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '海报图片URL',
  `genre` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '电影类型',
  `region` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '国家/地区',
  `avg_rating` decimal(2, 1) NULL DEFAULT 0.0 COMMENT '综合评分',
  `rating_count` int NULL DEFAULT 0 COMMENT '评价人数',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`movie_id`) USING BTREE,
  INDEX `idx_movie_name`(`movie_name` ASC) USING BTREE,
  INDEX `idx_year`(`year` ASC) USING BTREE,
  INDEX `idx_genre`(`genre` ASC) USING BTREE,
  INDEX `idx_region`(`region` ASC) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '公共电影信息表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of movie_public
-- ----------------------------

-- ----------------------------
-- Table structure for user
-- ----------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user`  (
  `user_id` int NOT NULL AUTO_INCREMENT COMMENT '用户ID',
  `username` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '用户名',
  `password` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '密码(MD5加密)',
  `email` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '邮箱',
  `avatar` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '头像URL',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '注册时间',
  `update_time` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`user_id`) USING BTREE,
  UNIQUE INDEX `username`(`username` ASC) USING BTREE,
  UNIQUE INDEX `email`(`email` ASC) USING BTREE,
  INDEX `idx_username`(`username` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 3 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '用户表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of user
-- ----------------------------
INSERT INTO `user` VALUES (1, 'admin', '123456', 'admin@example.com', NULL, '2026-04-12 15:21:29', '2026-04-12 15:23:10');
INSERT INTO `user` VALUES (2, 'testuser', '123456', 'test@example.com', NULL, '2026-04-12 15:21:29', '2026-04-12 15:23:17');

SET FOREIGN_KEY_CHECKS = 1;
