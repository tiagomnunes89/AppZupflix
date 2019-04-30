package br.com.zupfilms.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import br.com.zupfilms.model.MovieDetailsModel;
import br.com.zupfilms.model.MovieDetailsModelDB;
import br.com.zupfilms.server.response.CountriesResponse;
import br.com.zupfilms.server.response.GenresResponse;

public class DB {

    private SQLiteDatabase db;

    final static String TABLE_NAME = "favorites";
    final static String COLUMN_MOVIEID = "movie_id";
    final static String COLUMN_POSTER_PATH = "poster_path";
    final static String COLUMN_BACKDROP_PATH = "backdrop_path";
    final static String COLUMN_VOTE_AVERAGE = "vote_average";
    final static String COLUMN_TITLE = "title";
    final static String COLUMN_RELEASE_DATE = "release_date";
    final static String COLUMN_GENRES = "genres";
    final static String COLUMN_RUNTIME = "runtime";
    final static String COLUMN_OVERVIEW = "overview";
    final static String COLUMN_COUNTRIES = "production_countries";
    final static String COLUMN_TAGLINE = "tagline";
    final static String COLUMN_VOTE_COUNT = "vote_count";

    public DB(Context context) {
        DBCore auxBd = new DBCore(context);
        db = auxBd.getWritableDatabase();
    }

    public void insert(MovieDetailsModel movieDetailsModel) {

        ContentValues values = new ContentValues();
        values.put(COLUMN_MOVIEID, movieDetailsModel.getId());
        values.put(COLUMN_POSTER_PATH, movieDetailsModel.getPoster_path());
        values.put(COLUMN_BACKDROP_PATH, movieDetailsModel.getBackdrop_path());
        values.put(COLUMN_VOTE_AVERAGE, movieDetailsModel.getVote_average());
        values.put(COLUMN_TITLE, movieDetailsModel.getTitle());
        values.put(COLUMN_RELEASE_DATE, movieDetailsModel.getRelease_date());
        List<GenresResponse> genresKeywords = movieDetailsModel.getGenres();
        List<String> listGenres = new ArrayList<>();
        for (GenresResponse genre : genresKeywords) {
            listGenres.add(genre.getName());
        }
        values.put(COLUMN_GENRES, sentenceBuilder(listGenres));
        values.put(COLUMN_RUNTIME, movieDetailsModel.getRuntime());
        values.put(COLUMN_OVERVIEW, movieDetailsModel.getOverview());
        List<CountriesResponse> countriesResponses = movieDetailsModel.getProduction_countries();
        List<String> listCountries = new ArrayList<>();
        for (CountriesResponse country : countriesResponses) {
            listCountries.add(country.getName());
        }
        values.put(COLUMN_COUNTRIES, sentenceBuilder(listCountries));
        values.put(COLUMN_TAGLINE, movieDetailsModel.getTagline());
        values.put(COLUMN_VOTE_COUNT, movieDetailsModel.getVote_count());

        Log.d("bd", "add film " + movieDetailsModel.getTitle());
        db.insert(TABLE_NAME, null, values);
    }

    public void delete(int movieID) {
        db.delete(TABLE_NAME, " movie_id = " + movieID, null);
        Log.d("delete", "delete film " + movieID);
    }

    public List<MovieDetailsModelDB> getAllFavoritesFilms() {
        List<MovieDetailsModelDB> list = new ArrayList<>();
        String[] column = new String[]{
                COLUMN_MOVIEID,
                COLUMN_POSTER_PATH,
                COLUMN_BACKDROP_PATH,
                COLUMN_VOTE_AVERAGE,
                COLUMN_TITLE,
                COLUMN_RELEASE_DATE,
                COLUMN_GENRES,
                COLUMN_RUNTIME,
                COLUMN_OVERVIEW,
                COLUMN_COUNTRIES,
                COLUMN_TAGLINE,
                COLUMN_VOTE_COUNT
        };

        Cursor cursor = db.query(TABLE_NAME, column, null, null, null, null, "title ASC");

        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            do {

                MovieDetailsModelDB movie = new MovieDetailsModelDB();
                movie.setId(Integer.parseInt(cursor.getString(cursor.getColumnIndex(COLUMN_MOVIEID))));
                movie.setPosterPath(cursor.getString(cursor.getColumnIndex(COLUMN_POSTER_PATH)));
                movie.setBackdropPath(cursor.getString(cursor.getColumnIndex(COLUMN_BACKDROP_PATH)));
                movie.setVote_average(Float.parseFloat(cursor.getString(cursor.getColumnIndex(COLUMN_VOTE_AVERAGE))));
                movie.setTitle(cursor.getString(cursor.getColumnIndex(COLUMN_TITLE)));
                movie.setReleaseDate(cursor.getString(cursor.getColumnIndex(COLUMN_RELEASE_DATE)));
                movie.setGenres(cursor.getString(cursor.getColumnIndex(COLUMN_GENRES)));
                movie.setRuntime(Integer.parseInt(cursor.getString(cursor.getColumnIndex(COLUMN_RUNTIME))));
                movie.setOverview(cursor.getString(cursor.getColumnIndex(COLUMN_OVERVIEW)));
                movie.setCountries(cursor.getString(cursor.getColumnIndex(COLUMN_COUNTRIES)));
                movie.setTagline(cursor.getString(cursor.getColumnIndex(COLUMN_TAGLINE)));
                movie.setVoteCount(Integer.parseInt(cursor.getString(cursor.getColumnIndex(COLUMN_VOTE_COUNT))));
                list.add(movie);
            } while (cursor.moveToNext());
        }
        return (list);
    }

    public MovieDetailsModelDB getOneFavoritesFilms(Integer movieID) {

        MovieDetailsModelDB movie = new MovieDetailsModelDB();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_NAME + " WHERE " + COLUMN_MOVIEID + " = " + movieID, null);

        if (cursor.moveToFirst()){
            movie.setId(Integer.parseInt(cursor.getString(cursor.getColumnIndex(COLUMN_MOVIEID))));
            movie.setPosterPath(cursor.getString(cursor.getColumnIndex(COLUMN_POSTER_PATH)));
            movie.setBackdropPath(cursor.getString(cursor.getColumnIndex(COLUMN_BACKDROP_PATH)));
            movie.setVote_average(Float.parseFloat(cursor.getString(cursor.getColumnIndex(COLUMN_VOTE_AVERAGE))));
            movie.setTitle(cursor.getString(cursor.getColumnIndex(COLUMN_TITLE)));
            movie.setReleaseDate(cursor.getString(cursor.getColumnIndex(COLUMN_RELEASE_DATE)));
            movie.setGenres(cursor.getString(cursor.getColumnIndex(COLUMN_GENRES)));
            movie.setRuntime(Integer.parseInt(cursor.getString(cursor.getColumnIndex(COLUMN_RUNTIME))));
            movie.setOverview(cursor.getString(cursor.getColumnIndex(COLUMN_OVERVIEW)));
            movie.setCountries(cursor.getString(cursor.getColumnIndex(COLUMN_COUNTRIES)));
            movie.setTagline(cursor.getString(cursor.getColumnIndex(COLUMN_TAGLINE)));
            movie.setVoteCount(Integer.parseInt(cursor.getString(cursor.getColumnIndex(COLUMN_VOTE_COUNT))));

        }
        return movie;
    }


    private String sentenceBuilder(List<String> listString) {
        StringBuilder keywordList = new StringBuilder();
        if (listString != null) {
            for (int i = 0; i < listString.size(); i++) {
                keywordList.append(listString.get(i));
                if (i < listString.size() - 1) {
                    keywordList.append(", ");
                }
            }
        }
        Log.d("KEYWORDS", keywordList.toString());
        return keywordList.toString();
    }
}
