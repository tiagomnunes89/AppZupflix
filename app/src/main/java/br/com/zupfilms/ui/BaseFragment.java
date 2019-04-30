package br.com.zupfilms.ui;

import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ProgressBar;

import com.github.ybq.android.spinkit.sprite.Sprite;
import com.github.ybq.android.spinkit.style.ThreeBounce;

import java.util.ArrayList;
import java.util.List;

import br.com.zupfilms.model.MovieDetailsModel;
import br.com.zupfilms.model.MovieDetailsModelDB;
import br.com.zupfilms.server.response.CountriesResponse;
import br.com.zupfilms.server.response.GenresResponse;


public abstract class BaseFragment extends Fragment {

    public void loadingExecutor(Boolean isLoading, ProgressBar progressBar, FrameLayout frameLayout) {
        if (isLoading != null && getActivity() != null) {
            if (isLoading) {
                Sprite threeBounce = new ThreeBounce();
                progressBar.setIndeterminateDrawable(threeBounce);
                frameLayout.setVisibility(View.VISIBLE);
            } else {
                Sprite threeBounce = new ThreeBounce();
                progressBar.setIndeterminateDrawable(threeBounce);
                frameLayout.setVisibility(View.INVISIBLE);
            }
        }
    }

    public MovieDetailsModelDB convertMovieFavoriteModelForDataBase (MovieDetailsModel movieDetailsModel){
        MovieDetailsModelDB movieDB = new MovieDetailsModelDB();
        movieDB.setId(movieDetailsModel.getId());
        movieDB.setPosterPath(movieDetailsModel.getPoster_path());
        movieDB.setBackdropPath(movieDetailsModel.getBackdrop_path());
        movieDB.setVote_average(movieDetailsModel.getVote_average());
        movieDB.setOverview(movieDetailsModel.getTitle());
        movieDB.setReleaseDate(movieDetailsModel.getRelease_date());
        List<GenresResponse> genresKeywords = movieDetailsModel.getGenres();
        List<String> listGenres = new ArrayList<>();
        for (GenresResponse genre : genresKeywords) {
            listGenres.add(genre.getName());
        }
        movieDB.setGenres(sentenceBuilder(listGenres));
        movieDB.setRuntime(movieDetailsModel.getRuntime());
        movieDB.setOverview(movieDetailsModel.getOverview());
        List<CountriesResponse> countriesResponses = movieDetailsModel.getProduction_countries();
        List<String> listCountries = new ArrayList<>();
        for (CountriesResponse country : countriesResponses) {
            listCountries.add(country.getName());
        }
        movieDB.setCountries(sentenceBuilder(listCountries));
        movieDB.setTagline(movieDetailsModel.getTagline());
        movieDB.setVoteCount(movieDetailsModel.getVote_count());

        return movieDB;
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
