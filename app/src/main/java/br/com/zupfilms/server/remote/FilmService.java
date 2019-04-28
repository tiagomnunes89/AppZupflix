package br.com.zupfilms.server.remote;

import br.com.zupfilms.model.MovieDetailsModel;
import br.com.zupfilms.server.response.FilmGenres;
import br.com.zupfilms.server.response.FilmsResults;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface FilmService {

    @GET("discover/movie")
    Call<FilmsResults> getMovieGenre(@Query("language") String language,
                                     @Query("sort_by") String sortBy,
                                     @Query("page") String page,
                                     @Query("include_adult") Boolean includeAdult,
                                     @Query("with_genres") String genreID);

    @GET("search/movie")
    Call<FilmsResults> getMovieSearch(@Query("language") String language,
                                     @Query("query") String query,
                                     @Query("page") String page,
                                     @Query("include_adult") Boolean includeAdult);

    @GET("genre/movie/list")
    Call<FilmGenres> getGenres(@Query("language") String language);

    @GET("movie/{movie_id}")
    Call<MovieDetailsModel> getMovieDetails(@Path("movie_id") int id, @Query("language") String language);

    @GET("movie/{movie_id}/similar")
    Call<FilmsResults> getSimilarMovies (@Path("movie_id") String movieID,
                                         @Query("language") String language,
                                                  @Query("page") String page);
}
