package br.com.zupfilms.model;

import java.util.List;

public class Film {

    private final String title;
    private final String posterPath;
    private final Double voteAverage;
    private final String overview;
    private final String releaseDate;
    private final List<Integer> genre_ids;

    public Film(String title, String posterPath, Double voteAverage, String overview, String releaseDate, List<Integer> genre_ids) {
        this.title = title;
        this.posterPath = posterPath;
        this.voteAverage = voteAverage;
        this.overview = overview;
        this.releaseDate = releaseDate;
        this.genre_ids = genre_ids;
    }

    public String getTitle() {
        return title;
    }

    public String getPosterPath() {
        return posterPath;
    }

    public Double getVoteAverage() {
        return voteAverage;
    }

    public String getOverview() {
        return overview;
    }

    public String getReleaseDate() {
        if (this.releaseDate != null) {
            return releaseDate.substring(0, 4);
        }
        return releaseDate;
    }

    public List<Integer> getGenre_ids() {
        return genre_ids;
    }
}
