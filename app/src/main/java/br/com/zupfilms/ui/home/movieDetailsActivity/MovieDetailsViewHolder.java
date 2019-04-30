package br.com.zupfilms.ui.home.movieDetailsActivity;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import br.com.zupfilms.R;
import br.com.zupfilms.model.MovieDetailsModel;
import br.com.zupfilms.model.MovieDetailsModelDB;
import br.com.zupfilms.server.response.CountriesResponse;
import br.com.zupfilms.server.response.GenresResponse;

public class MovieDetailsViewHolder {

    RecyclerView recyclerViewDetails;
    ProgressBar progressBarFragment;
    FrameLayout frameLayout;
    TextView textViewToobar;
    ImageView backArrow;
    ViewGroup layoutItemDetails;
    TextView textViewTitle;
    TextView textViewKeywords;
    TextView textViewYear;
    TextView textViewCountries;
    TextView textViewRuntime;
    TextView textViewPoints;
    TextView textViewOverview;
    ImageView imageViewBanner;
    ImageView imageViewPoster;
    CardView cardViewPoster;
    TextView textViewTagline;
    TextView textViewVoteCount;
    CardView cardViewBanner;
    CheckBox checkBox;
    LinearLayout linearLayoutVote;

    public MovieDetailsViewHolder(View view) {
        frameLayout = view.findViewById(R.id.loading_layout);
        recyclerViewDetails = view.findViewById(R.id.recycler_films);
        progressBarFragment = view.findViewById(R.id.progress_bar);
        textViewToobar = view.findViewById(R.id.textview_toobar_details);
        backArrow = view.findViewById(R.id.imageView_iconBackArrow);
        layoutItemDetails = view.findViewById(R.id.layout_item_details);
        frameLayout = view.findViewById(R.id.loading_layout);
        textViewTitle = view.findViewById(R.id.text_title_details);
        textViewKeywords = view.findViewById(R.id.textView_keywords_details);
        textViewYear = view.findViewById(R.id.textView_year_details);
        textViewTagline = view.findViewById(R.id.textView_tagline);
        linearLayoutVote = view.findViewById(R.id.linearLayout_vote_count);
        textViewVoteCount = linearLayoutVote.findViewById(R.id.textView_voteCountDetails);
        textViewCountries = view.findViewById(R.id.textView_countries_details);
        textViewRuntime = view.findViewById(R.id.textView_runtime_details);
        textViewPoints = view.findViewById(R.id.textView_points_details);
        textViewOverview = view.findViewById(R.id.textView_overview_details);
        recyclerViewDetails = view.findViewById(R.id.recycler_films);
        cardViewBanner = view.findViewById(R.id.cardview_banner_details);
        cardViewPoster = view.findViewById(R.id.cardview_poster_details);
        imageViewBanner = view.findViewById(R.id.imageView_banner_details);
        imageViewPoster = view.findViewById(R.id.imageView_poster_details);
        checkBox = view.findViewById(R.id.checkbox_favorite_details);
    }

    private String sentenceBuilder(List<String> listString) {
        StringBuilder keywordList = new StringBuilder();
        if(listString != null) {
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

    public void setMovieDetailsInformation(MovieDetailsModel movieDetailsModel){
        this.textViewOverview.setText(movieDetailsModel.getOverview());
        List<GenresResponse> genresKeywords = movieDetailsModel.getGenres();
        List<String> listGenres = new ArrayList<>();
        for(GenresResponse genre : genresKeywords){
            listGenres.add(genre.getName());
        }
        this.textViewKeywords.setText(sentenceBuilder(listGenres));
        List<CountriesResponse> countriesResponses = movieDetailsModel.getProduction_countries();
        List<String> listCountries = new ArrayList<>();
        for(CountriesResponse country : countriesResponses){
            listCountries.add(country.getName());
        }
        this.textViewCountries.setText(sentenceBuilder(listCountries));
        this.textViewPoints.setText(String.valueOf(movieDetailsModel.getVote_average()));
        String runtime = movieDetailsModel.getRuntime() + "min";
        this.textViewRuntime.setText(runtime);
        this.textViewTagline.setText(movieDetailsModel.getTagline());
        this.textViewVoteCount.setText(String.valueOf(movieDetailsModel.getVote_count()));
        if(!movieDetailsModel.getRelease_date().isEmpty() && movieDetailsModel.getRelease_date().length()>=4){
            textViewYear.setText(movieDetailsModel.getRelease_date().substring(0, 4));
        }
        this.textViewTitle.setText(movieDetailsModel.getTitle());
        if(movieDetailsModel.getPoster_path() == null || movieDetailsModel.getPoster_path().isEmpty()){
            this.cardViewPoster.setVisibility(View.INVISIBLE);
        } else {
            this.cardViewPoster.setVisibility(View.VISIBLE);
            Picasso.get()
                    .load("https://image.tmdb.org/t/p/w342/"+movieDetailsModel.getPoster_path())
                    .into(this.imageViewPoster);
        }
        if(movieDetailsModel.getBackdrop_path() == null || movieDetailsModel.getBackdrop_path().isEmpty()){
            this.cardViewBanner.setVisibility(View.INVISIBLE);
        } else {
            this.cardViewBanner.setVisibility(View.VISIBLE);
            Picasso.get()
                    .load("https://image.tmdb.org/t/p/w1280/"+movieDetailsModel.getBackdrop_path())
                    .into(this.imageViewBanner);
        }
    }

    public void setMovieDetailsDBInformation(MovieDetailsModelDB movieDetailsModel){
        this.textViewOverview.setText(movieDetailsModel.getOverview());
        this.textViewKeywords.setText(movieDetailsModel.getGenres());
        this.textViewCountries.setText(movieDetailsModel.getCountries());
        this.textViewPoints.setText(String.valueOf(movieDetailsModel.getVote_average()));
        String runtime = movieDetailsModel.getRuntime() + "min";
        this.textViewRuntime.setText(runtime);
        this.textViewTagline.setText(movieDetailsModel.getTagline());
        this.textViewVoteCount.setText(String.valueOf(movieDetailsModel.getVoteCount()));
        if(!movieDetailsModel.getReleaseDate().isEmpty() && movieDetailsModel.getPosterPath().length()>=4){
            textViewYear.setText(movieDetailsModel.getReleaseDate().substring(0, 4));
        }
        this.textViewTitle.setText(movieDetailsModel.getTitle());
        if(movieDetailsModel.getPosterPath() == null || movieDetailsModel.getPosterPath().isEmpty()){
            this.cardViewPoster.setVisibility(View.INVISIBLE);
        } else {
            this.cardViewPoster.setVisibility(View.VISIBLE);
            Picasso.get()
                    .load("https://image.tmdb.org/t/p/w342/"+movieDetailsModel.getPosterPath())
                    .into(this.imageViewPoster);
        }
        if(movieDetailsModel.getBackdropPath() == null || movieDetailsModel.getBackdropPath().isEmpty()){
            this.cardViewBanner.setVisibility(View.INVISIBLE);
        } else {
            this.cardViewBanner.setVisibility(View.VISIBLE);
            Picasso.get()
                    .load("https://image.tmdb.org/t/p/w1280/"+movieDetailsModel.getBackdropPath())
                    .into(this.imageViewBanner);
        }
    }
}
