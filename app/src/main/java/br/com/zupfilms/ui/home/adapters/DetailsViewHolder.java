package br.com.zupfilms.ui.home.adapters;

import android.arch.paging.PagedList;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.CheckBox;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import br.com.zupfilms.R;
import br.com.zupfilms.model.MovieDetailsModel;
import br.com.zupfilms.server.response.CountriesResponse;
import br.com.zupfilms.server.response.FilmResponse;
import br.com.zupfilms.server.response.GenresResponse;

class DetailsViewHolder extends RecyclerView.ViewHolder{

    private final TextView textViewTitle;
    private final TextView textViewKeywords;
    private final TextView textViewYear;
    private final TextView textViewCountries;
    private final TextView textViewRuntime;
    private final TextView textViewPoints;
    private final TextView textViewOverview;
    private final RecyclerView recyclerViewDetails;
    private final ProgressBar progressBar;
    private final ImageView imageViewBanner;
    private final ImageView imageViewPoster;
    private final FrameLayout frameLayout;
    private final CardView cardViewPoster;
    private final TextView textViewTagline;
    private final TextView textViewVoteCount;
    private final CardView cardViewBanner;
    private final CheckBox checkBox;


    public DetailsViewHolder(View view, final FilmAdapterDetailsList.OnCheckBoxClickListener onCheckBoxClickListener,
    final PagedList<FilmResponse> currentList) {
        super(view);
        frameLayout = view.findViewById(R.id.loading_layout);
        textViewTitle = view.findViewById(R.id.text_title_details);
        textViewKeywords = view.findViewById(R.id.textView_keywords_details);
        textViewYear = view.findViewById(R.id.textView_year_details);
        textViewCountries = view.findViewById(R.id.textView_countries_details);
        textViewRuntime = view.findViewById(R.id.textView_runtime_details);
        textViewPoints = view.findViewById(R.id.textView_points_details);
        textViewOverview = view.findViewById(R.id.textView_overview_details);
        recyclerViewDetails = view.findViewById(R.id.recycler_films);
        progressBar = view.findViewById(R.id.progress_bar);
        textViewTagline = view.findViewById(R.id.textView_tagline);
        textViewVoteCount = view.findViewById(R.id.textView_voteCountDetails);
        cardViewBanner = view.findViewById(R.id.cardview_banner_details);
        cardViewPoster = view.findViewById(R.id.cardview_poster_details);
        imageViewBanner = view.findViewById(R.id.imageView_banner_details);
        imageViewPoster = view.findViewById(R.id.imageView_poster_details);
        checkBox = view.findViewById(R.id.checkbox_favorite_details);
        checkBox.setOnClickListener(v -> {
            if (onCheckBoxClickListener != null) {
                int position = getAdapterPosition();
                if(checkBox.isChecked()){
                    onCheckBoxClickListener.OnCheckBoxClick(position,currentList,true);
                } else {
                    onCheckBoxClickListener.OnCheckBoxClick(position, currentList,false);
                }
            }
        });
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
        if(movieDetailsModel.getRuntime() != 0){
            String runtime = movieDetailsModel.getRuntime() + "min";
            this.textViewRuntime.setText(runtime);
        }
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
}
