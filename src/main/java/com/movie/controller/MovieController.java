package com.movie.controller;

import com.movie.dto.ApiResponse;
import com.movie.dto.MovieRequest;
import com.movie.entity.MovieCollection;
import com.movie.service.MovieService;
import com.movie.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/movie")
public class MovieController {

    @Autowired
    private MovieService movieService;

    @Autowired
    private JwtUtil jwtUtil;

    private Integer getUserIdFromToken(HttpServletRequest request) {
        String token = request.getHeader("Authorization");
        if (token != null && token.startsWith("Bearer ")) {
            token = token.substring(7);
            return jwtUtil.getUserIdFromToken(token);
        }
        return null;
    }

    @GetMapping("/list")
    public ApiResponse<?> getMovieList(HttpServletRequest request) {
        Integer userId = getUserIdFromToken(request);
        if (userId == null) {
            return ApiResponse.error(401, "未登录");
        }
        List<MovieCollection> movies = movieService.getUserCollections(userId);
        return ApiResponse.success(movies);
    }

    @PostMapping("/add")
    public ApiResponse<?> addMovie(@RequestBody MovieRequest request, HttpServletRequest req) {
        Integer userId = getUserIdFromToken(req);
        if (userId == null) {
            return ApiResponse.error(401, "未登录");
        }
        Map<String, Object> result = movieService.addCollection(userId, request);
        boolean success = (Boolean) result.get("success");
        if (success) {
            String message = (String) result.get("message");
            Object collectionId = result.get("collectionId");
            // 包装返回值
            Map<String, Object> data = new HashMap<>();
            data.put("collectionId", collectionId);
            return ApiResponse.success(message, data);
        }
        return ApiResponse.error(400, (String) result.get("message"));
    }

    @PutMapping("/update/{collectionId}")
    public ApiResponse<?> updateMovie(@PathVariable Integer collectionId,
                                      @RequestBody MovieRequest request) {
        Map<String, Object> result = movieService.updateCollection(collectionId, request);
        boolean success = (Boolean) result.get("success");
        if (success) {
            String message = (String) result.get("message");
            return ApiResponse.success(message);
        }
        return ApiResponse.error(400, (String) result.get("message"));
    }

    @DeleteMapping("/delete/{collectionId}")
    public ApiResponse<?> deleteMovie(@PathVariable Integer collectionId) {
        Map<String, Object> result = movieService.deleteCollection(collectionId);
        String message = (String) result.get("message");
        return ApiResponse.success(message);
    }

    @GetMapping("/search")
    public ApiResponse<?> searchMovies(@RequestParam(required = false) String keyword,
                                       @RequestParam(required = false) String director,
                                       @RequestParam(required = false) Double minRating,
                                       @RequestParam(required = false) String region,
                                       @RequestParam(required = false) String genre,
                                       HttpServletRequest request) {
        Integer userId = getUserIdFromToken(request);
        if (userId == null) {
            return ApiResponse.error(401, "未登录");
        }
        List<MovieCollection> movies = movieService.searchMovies(userId, keyword, director, minRating, region, genre);
        return ApiResponse.success(movies);
    }
    /**
     * 获取电影排行榜
     * @param type 排行榜类型: rating(按评分排行), popular(按评论数排行)
     */
    @GetMapping("/rankings")
    public ApiResponse<?> getMovieRankings(@RequestParam String type) {
        List<Map<String, Object>> rankings;

        if ("popular".equals(type)) {
            // 按评论数排行
            rankings = movieService.getMoviesOrderByCommentCount();
        } else {
            // 按评分排行（默认）
            rankings = movieService.getMoviesOrderByRating();
        }

        return ApiResponse.success(rankings);
    }
}