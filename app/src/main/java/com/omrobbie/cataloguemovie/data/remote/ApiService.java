package com.omrobbie.cataloguemovie.data.remote;

import com.omrobbie.cataloguemovie.data.model.detail.DetailModel;
import com.omrobbie.cataloguemovie.data.model.search.SearchModel;
import com.omrobbie.cataloguemovie.data.model.upcoming.UpcomingModel;

import javax.inject.Singleton;

import io.reactivex.Single;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

@Singleton
public interface ApiService {

    @GET("movie/popular?")
    Single<SearchModel> getPopularMovie(@Query("page") int page);

    @GET("search/movie")
    Single<SearchModel> getSearchMovie(@Query("page") int page, @Query("query") String query);

    @GET("movie/{movie_id}")
    Single<DetailModel> getDetailMovie(@Path("movie_id") String movie_id);

    @GET("movie/upcoming")
    Single<UpcomingModel> getUpcomingMovie();

}
