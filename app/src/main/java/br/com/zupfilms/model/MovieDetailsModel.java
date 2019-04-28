package br.com.zupfilms.model;

import java.util.List;

import br.com.zupfilms.server.response.CountriesResponse;
import br.com.zupfilms.server.response.GenresResponse;

public class MovieDetailsModel {

    private Integer id;
    private String poster_path;
    private String backdrop_path;
    private float vote_average;
    private String title;
    private String release_date;
    private List<GenresResponse> genres;
    private Integer runtime;
    private String overview;
    private List<CountriesResponse> production_countries;

    public MovieDetailsModel(Integer id, String poster_path, String backdrop_path, float vote_average, String title, String release_date,
                             List<GenresResponse> genres, Integer runtime, String overview, List<CountriesResponse> production_countries) {
        this.id = id;
        this.poster_path = poster_path;
        this.backdrop_path = backdrop_path;
        this.vote_average = vote_average;
        this.title = title;
        this.release_date = release_date;
        this.genres = genres;
        this.runtime = runtime;
        this.overview = overview;
        this.production_countries = production_countries;
    }

    public Integer getId() {
        return id;
    }

    public String getPoster_path() {
        return poster_path;
    }

    public String getBackdrop_path() {
        return backdrop_path;
    }

    public float getVote_average() {
        return vote_average;
    }

    public String getTitle() {
        return title;
    }

    public String getRelease_date() {
        return release_date;
    }

    public List<GenresResponse> getGenres() {
        return genres;
    }

    public Integer getRuntime() {
        return runtime;
    }

    public String getOverview() {
        return overview;
    }

    public List<CountriesResponse> getProduction_countries() {
        return production_countries;
    }
}
