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
    private String tagline;
    private Integer vote_count;

    public MovieDetailsModel(Integer id, String poster_path, String backdrop_path, float vote_average, String title, String release_date, List<GenresResponse> genres, Integer runtime, String overview,
                             List<CountriesResponse> production_countries, String tagline, Integer vote_count) {
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
        this.tagline = tagline;
        this.vote_count = vote_count;
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

    public String getTagline() {
        return tagline;
    }

    public Integer getVote_count() {
        return vote_count;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setPoster_path(String poster_path) {
        this.poster_path = poster_path;
    }

    public void setBackdrop_path(String backdrop_path) {
        this.backdrop_path = backdrop_path;
    }

    public void setVote_average(float vote_average) {
        this.vote_average = vote_average;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setRelease_date(String release_date) {
        this.release_date = release_date;
    }

    public void setGenres(List<GenresResponse> genres) {
        this.genres = genres;
    }

    public void setRuntime(Integer runtime) {
        this.runtime = runtime;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public void setProduction_countries(List<CountriesResponse> production_countries) {
        this.production_countries = production_countries;
    }

    public void setTagline(String tagline) {
        this.tagline = tagline;
    }

    public void setVote_count(Integer vote_count) {
        this.vote_count = vote_count;
    }
}
