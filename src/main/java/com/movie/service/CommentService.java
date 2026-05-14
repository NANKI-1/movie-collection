package com.movie.service;

import com.movie.dto.CommentByTmdbRequest;
import com.movie.dto.CommentRequest;
import com.movie.entity.Comment;
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

    // 获取电影评分统计（根据movieId）
    public Map<String, Object> getMovieRating(Integer movieId) {
        Map<String, Object> result = new HashMap<>();
        Double avgRating = commentMapper.getAverageRating(movieId);
        Integer count = commentMapper.getRatingCount(movieId);
        result.put("avgRating", avgRating != null ? avgRating : 0);
        result.put("count", count != null ? count : 0);
        return result;
    }

    // 获取电影评分统计（根据TMDB ID）
    public Map<String, Object> getMovieRatingByTmdbId(Integer tmdbId) {
        Map<String, Object> result = new HashMap<>();
        Double avgRating = commentMapper.getAverageRatingByTmdbId(tmdbId);
        Integer count = commentMapper.getRatingCountByTmdbId(tmdbId);
        result.put("avgRating", avgRating != null ? avgRating : 0);
        result.put("count", count != null ? count : 0);
        return result;
    }

    // 发布评论
    @Transactional
    public Map<String, Object> addComment(Integer userId, CommentRequest request) {
        Map<String, Object> result = new HashMap<>();

        Comment comment = new Comment();
        comment.setMovieId(request.getMovieId());
        comment.setUserId(userId);
        comment.setRating(request.getRating());
        comment.setContent(request.getContent());

        commentMapper.insertComment(comment);

        // 更新电影综合评分
        updateMovieRating(request.getMovieId());

        result.put("success", true);
        result.put("message", "发布成功");
        result.put("commentId", comment.getCommentId());
        return result;
    }

    // 通过TMDB发布评论
    @Transactional
    public Map<String, Object> addCommentByTmdb(Integer userId, CommentByTmdbRequest request) {
        Map<String, Object> result = new HashMap<>();

        // 查找或创建电影
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

        Comment comment = new Comment();
        comment.setMovieId(movie.getMovieId());
        comment.setUserId(userId);
        comment.setRating(request.getRating());
        comment.setContent(request.getContent());
        commentMapper.insertComment(comment);

        // 更新电影综合评分
        updateMovieRating(movie.getMovieId());

        result.put("success", true);
        result.put("message", "评价发布成功");
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

        comment.setRating(request.getRating());
        comment.setContent(request.getContent());
        comment.setIsEdited(true);

        commentMapper.updateComment(comment);

        // 更新电影综合评分
        updateMovieRating(comment.getMovieId());

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
            updateMovieRating(comment.getMovieId());
        }

        result.put("success", true);
        result.put("message", "删除成功");
        return result;
    }

    // 更新电影综合评分
    private void updateMovieRating(Integer movieId) {
        Double avgRating = commentMapper.getAverageRating(movieId);
        Integer count = commentMapper.getRatingCount(movieId);

        if (avgRating != null && count != null) {
            movieMapper.updateMovieRating(movieId, avgRating, count);
        }
    }
}