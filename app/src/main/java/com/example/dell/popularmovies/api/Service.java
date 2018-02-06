package com.example.dell.popularmovies.api;

import com.example.dell.popularmovies.model.MovieResponse;
import com.example.dell.popularmovies.model.ReviewResponse;
import com.example.dell.popularmovies.model.TrailerResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface Service {

    @GET("movie/popular")
    Call<MovieResponse> getPopularMovies(@Query("api_key") String apiKey,@Query("page") int page);

    @GET("movie/top_rated")
    Call<MovieResponse> getTopRatedMovies(@Query("api_key") String apiKey,@Query("page") int page);

    @GET("movie/{movie_id}/videos")
    Call<TrailerResponse> getMovieVideos(@Path("movie_id") int id, @Query("api_key") String apiKey);

    @GET("movie/{movie_id}/reviews")
    Call<ReviewResponse> getMovieReviews(@Path("movie_id") int id, @Query("api_key") String apiKey);

}