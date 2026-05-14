package com.movie.mapper;

import com.movie.entity.MovieCollection;
import com.movie.entity.MoviePublic;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;
import java.util.Map;

@Mapper
public interface MovieMapper {
    // 公共电影操作
    MoviePublic findMovieByName(@Param("movieName") String movieName);
    int insertMovie(MoviePublic movie);
    int updateMovieRating(@Param("movieId") Integer movieId,
                          @Param("avgRating") Double avgRating,
                          @Param("ratingCount") Integer ratingCount);

    // 个人收藏操作
    List<MovieCollection> findCollectionsByUserId(@Param("userId") Integer userId);
    MovieCollection findCollectionById(@Param("collectionId") Integer collectionId);
    MovieCollection findCollectionByUserAndMovie(@Param("userId") Integer userId,
                                                 @Param("movieId") Integer movieId);
    int insertCollection(MovieCollection collection);
    int updateCollection(MovieCollection collection);
    int deleteCollection(@Param("collectionId") Integer collectionId);

    // 搜索筛选
    List<MovieCollection> searchCollections(@Param("userId") Integer userId,
                                            @Param("keyword") String keyword,
                                            @Param("director") String director,
                                            @Param("minRating") Double minRating,
                                            @Param("region") String region,
                                            @Param("genre") String genre);
    MoviePublic findMovieByTmdbId(@Param("tmdbId") Integer tmdbId);
    // 获取按评分排序的电影（综合评分最高的电影）
    List<Map<String, Object>> selectMoviesOrderByRating();
    // 获取按评论数排序的电影
    List<Map<String, Object>> selectMoviesOrderByCommentCount();

}
