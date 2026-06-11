package com.movie.service;

import com.movie.dto.CommentByTmdbRequest;
import com.movie.dto.CommentRequest;
import com.movie.entity.Comment;
import com.movie.entity.MovieCollection;
import com.movie.entity.MoviePublic;
import com.movie.mapper.CommentMapper;
import com.movie.mapper.MovieMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class CommentService {

    @Autowired
    private CommentMapper commentMapper;

    @Autowired
    private MovieMapper movieMapper;

    // 获取电影评论（根据movieId）
    public List<Comment> getCommentsByMovie(Integer movieId) {
        return commentMapper.findCommentsByMovieId(movieId);
    }

    // 获取用户自己的所有评论
    public List<Comment> getCommentsByUserId(Integer userId) {
        return commentMapper.findCommentsByUserId(userId);
    }

    // 根据TMDB ID获取评论
    public List<Comment> getCommentsByTmdbId(Integer tmdbId) {
        return commentMapper.findCommentsByTmdbId(tmdbId);
    }

    // 获取电影评分统计（根据movieId）- 从收藏表统计
    public Map<String, Object> getMovieRating(Integer movieId) {
        Map<String, Object> result = new HashMap<>();
        // 从收藏表统计评分
        Double avgRating = movieMapper.getAveragePersonalRatingByMovie(movieId);
        Integer count = movieMapper.getRatingCountFromCollections(movieId);

        result.put("avgRating", avgRating != null ? avgRating : 0.0);
        result.put("count", count != null ? count : 0);
        result.put("avgRatingFormatted", String.format("%.1f", avgRating != null ? avgRating : 0.0));
        result.put("hasRating", count != null && count > 0);

        return result;
    }

    // 获取电影评分统计（根据TMDB ID）- 从收藏表统计
    public Map<String, Object> getMovieRatingByTmdbId(Integer tmdbId) {
        Map<String, Object> result = new HashMap<>();
        Double avgRating = movieMapper.getAveragePersonalRatingByTmdbId(tmdbId);
        Integer count = movieMapper.getRatingCountFromCollectionsByTmdbId(tmdbId);

        result.put("avgRating", avgRating != null ? avgRating : 0.0);
        result.put("count", count != null ? count : 0);
        result.put("avgRatingFormatted", String.format("%.1f", avgRating != null ? avgRating : 0.0));
        result.put("hasRating", count != null && count > 0);

        return result;
    }

    /**
     * 发布评论 - 不存储评分，评分从收藏表获取
     */
    @Transactional
    public Map<String, Object> addComment(Integer userId, CommentRequest request) {
        Map<String, Object> result = new HashMap<>();

        // 1. 检查用户是否收藏了该电影，获取评分
        MovieCollection collection = movieMapper.findCollectionByUserAndMovie(userId, request.getMovieId());

        // 2. 检查用户是否已经有公开评价
        Comment existingComment = commentMapper.findCommentByUserAndMovie(userId, request.getMovieId());
        if (existingComment != null) {
            result.put("success", false);
            result.put("message", "你已经评价过这部电影了");
            return result;
        }

        // 3. 获取评分（用于返回给前端显示，不存入评论表）
        Double rating = 0.0;
        boolean isRated = false;
        if (collection != null && collection.getPersonalRating() != null && collection.getPersonalRating() > 0) {
            rating = collection.getPersonalRating();
            isRated = true;
        }

        // 4. 创建公开评论（不存储评分）
        Comment comment = new Comment();
        comment.setMovieId(request.getMovieId());
        comment.setUserId(userId);
        comment.setContent(request.getContent());

        commentMapper.insertComment(comment);

        // 5. 注意：电影综合评分从收藏表计算，不在评论时更新
        // 因为评分来自收藏表，不是来自评论表

        result.put("success", true);
        result.put("message", "发布成功");
        result.put("commentId", comment.getCommentId());
        result.put("rating", rating);
        result.put("isRated", isRated);
        if (!isRated) {
            result.put("warning", "您尚未收藏该电影或未评分");
        }
        return result;
    }

    /**
     * 通过TMDB发布评论 - 不存储评分
     */
    @Transactional
    public Map<String, Object> addCommentByTmdb(Integer userId, CommentByTmdbRequest request) {
        Map<String, Object> result = new HashMap<>();

        // 1. 查找或创建电影
        MoviePublic movie = movieMapper.findMovieByTmdbId(request.getTmdbId());
        if (movie == null) {
            movie = new MoviePublic();
            movie.setTmdbId(request.getTmdbId());
            movie.setMovieName(request.getMovieName());
            movie.setPosterUrl(request.getPosterUrl());
            movie.setYear(request.getYear());
            movie.setDirector(request.getDirector());
            movie.setGenre(request.getGenre());
            movie.setRegion(request.getRegion());
            movieMapper.insertMovie(movie);
        }

        // 2. 检查用户是否收藏了该电影
        MovieCollection collection = movieMapper.findCollectionByUserAndMovie(userId, movie.getMovieId());

        // 3. 检查用户是否已经有公开评价
        Comment existingComment = commentMapper.findCommentByUserAndMovie(userId, movie.getMovieId());
        if (existingComment != null) {
            result.put("success", false);
            result.put("message", "你已经评价过这部电影了");
            return result;
        }

        // 4. 获取评分（用于返回给前端显示）
        Double rating = 0.0;
        boolean isRated = false;
        if (collection != null && collection.getPersonalRating() != null && collection.getPersonalRating() > 0) {
            rating = collection.getPersonalRating();
            isRated = true;
        }

        // 5. 创建公开评论（不存储评分）
        Comment comment = new Comment();
        comment.setMovieId(movie.getMovieId());
        comment.setUserId(userId);
        comment.setContent(request.getContent());
        commentMapper.insertComment(comment);

        result.put("success", true);
        result.put("message", "评价发布成功");
        result.put("rating", rating);
        result.put("isRated", isRated);
        if (!isRated) {
            result.put("warning", "您尚未收藏该电影或未评分");
        }
        return result;
    }

    // 更新评论
    @Transactional
    public Map<String, Object> updateComment(Integer commentId, CommentRequest request) {
        Map<String, Object> result = new HashMap<>();

        Comment comment = commentMapper.findCommentById(commentId);
        if (comment == null) {
            result.put("success", false);
            result.put("message", "评论不存在");
            return result;
        }

        // 更新评论内容
        comment.setContent(request.getContent());
        comment.setIsEdited(true);

        commentMapper.updateComment(comment);

        result.put("success", true);
        result.put("message", "更新成功");
        return result;
    }

    // 删除评论
    @Transactional
    public Map<String, Object> deleteComment(Integer commentId) {
        Map<String, Object> result = new HashMap<>();

        Comment comment = commentMapper.findCommentById(commentId);
        if (comment != null) {
            commentMapper.deleteComment(commentId);
        }

        result.put("success", true);
        result.put("message", "删除成功");
        return result;
    }
}