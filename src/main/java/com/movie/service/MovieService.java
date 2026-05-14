package com.movie.service;

import com.movie.dto.MovieRequest;
import com.movie.entity.MovieCollection;
import com.movie.entity.MoviePublic;
import com.movie.mapper.MovieMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class MovieService {

    @Autowired
    private MovieMapper movieMapper;

    // 获取用户所有收藏
    public List<MovieCollection> getUserCollections(Integer userId) {
        return movieMapper.findCollectionsByUserId(userId);
    }

    // 添加电影收藏
    @Transactional
    public Map<String, Object> addCollection(Integer userId, MovieRequest request) {
        Map<String, Object> result = new HashMap<>();

        // 查找或创建公共电影
        MoviePublic movie = movieMapper.findMovieByName(request.getMovieName());
        if (movie == null) {
            movie = new MoviePublic();
            movie.setTmdbId(request.getTmdbId());
            movie.setMovieName(request.getMovieName());
            movie.setDirector(request.getDirector());
            movie.setYear(request.getYear());
            movie.setRegion(request.getRegion());
            movie.setGenre(request.getGenre());
            movie.setPosterUrl(request.getPosterUrl());
            movieMapper.insertMovie(movie);
        }

        // 检查是否已收藏
        MovieCollection existing = movieMapper.findCollectionByUserAndMovie(userId, movie.getMovieId());
        if (existing != null) {
            result.put("success", false);
            result.put("message", "该电影已在收藏列表中");
            return result;
        }

        // 添加收藏
        MovieCollection collection = new MovieCollection();
        collection.setUserId(userId);
        collection.setMovieId(movie.getMovieId());
        collection.setPersonalRating(request.getPersonalRating());
        collection.setWatchStatus(request.getWatchStatus());
        collection.setPrivateReview(request.getPrivateReview());

        movieMapper.insertCollection(collection);

        result.put("success", true);
        result.put("message", "添加成功");
        result.put("collectionId", collection.getCollectionId());
        return result;
    }

    // 更新电影收藏
    public Map<String, Object> updateCollection(Integer collectionId, MovieRequest request) {
        Map<String, Object> result = new HashMap<>();

        MovieCollection collection = movieMapper.findCollectionById(collectionId);
        if (collection == null) {
            result.put("success", false);
            result.put("message", "收藏记录不存在");
            return result;
        }

        collection.setPersonalRating(request.getPersonalRating());
        collection.setWatchStatus(request.getWatchStatus());
        collection.setPrivateReview(request.getPrivateReview());

        movieMapper.updateCollection(collection);

        result.put("success", true);
        result.put("message", "更新成功");
        return result;
    }

    // 删除电影收藏
    public Map<String, Object> deleteCollection(Integer collectionId) {
        Map<String, Object> result = new HashMap<>();
        movieMapper.deleteCollection(collectionId);
        result.put("success", true);
        result.put("message", "删除成功");
        return result;
    }

    // 搜索电影
    public List<MovieCollection> searchMovies(Integer userId, String keyword, String director,
                                              Double minRating, String region, String genre) {
        return movieMapper.searchCollections(userId, keyword, director, minRating, region, genre);
    }
    /**
     * 获取按评分排序的电影排行榜
     */
    public List<Map<String, Object>> getMoviesOrderByRating() {
        return movieMapper.selectMoviesOrderByRating();
    }

    /**
     * 获取按评论数排序的电影排行榜
     */
    public List<Map<String, Object>> getMoviesOrderByCommentCount() {
        return movieMapper.selectMoviesOrderByCommentCount();
    }
}