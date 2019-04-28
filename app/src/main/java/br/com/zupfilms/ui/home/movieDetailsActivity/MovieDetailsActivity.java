package br.com.zupfilms.ui.home.movieDetailsActivity;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.arch.paging.PagedList;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.text.SpannableString;
import android.text.style.StyleSpan;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.ProgressBar;

import com.github.ybq.android.spinkit.sprite.Sprite;
import com.github.ybq.android.spinkit.style.ThreeBounce;
import com.sdsmdg.tastytoast.TastyToast;

import java.util.List;

import br.com.zupfilms.R;
import br.com.zupfilms.model.MovieDetailsModel;
import br.com.zupfilms.server.response.FilmResponse;
import br.com.zupfilms.server.response.FilmsResults;
import br.com.zupfilms.ui.BaseActivity;
import br.com.zupfilms.ui.home.adapters.FilmAdapterDetailsList;
import br.com.zupfilms.ui.home.homeActivity.HomeActivity;
import br.com.zupfilms.ui.singleton.SingletonFilmGenres;
import br.com.zupfilms.ui.singleton.SingletonFilmID;
import br.com.zupfilms.ui.singleton.SingletonTotalResults;

public class MovieDetailsActivity extends BaseActivity {

    private MovieDetailsViewHolder movieDetailsViewHolder;
    private MovieDetailsViewModel movieDetailsViewModel;
    private FilmAdapterDetailsList adapter;
    private LinearLayoutManager linearLayoutManager;
    private MovieDetailsModel mMovieDetailsModel;
    private List<FilmResponse> listfilmResponses;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        View view = this.getLayoutInflater().inflate(R.layout.activity_movie_details, null);
        this.movieDetailsViewHolder = new MovieDetailsViewHolder(view);

        setContentView(view);

        movieDetailsViewModel = ViewModelProviders.of(this).get(MovieDetailsViewModel.class);

        SpannableString spannableString = new SpannableString("OT" + "MOVIES");
        spannableString.setSpan(new StyleSpan(Typeface.BOLD), 0, 2, 0);
        movieDetailsViewHolder.textViewToobar.setText(spannableString);

        setupListeners();

        setupObservers();

        linearLayoutManager = new LinearLayoutManager(this);

        setupLayoutManager();

        if(SingletonFilmID.INSTANCE.getID() != null){
            Integer filmID = SingletonFilmID.INSTANCE.getID();
            movieDetailsViewModel.executeServiceGetMovieDetails(filmID);
            movieDetailsViewModel.executeServiceGetSimilarMovies("1",filmID);
        } else {
            Intent intent = new Intent(MovieDetailsActivity.this, HomeActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        movieDetailsViewModel.getIsLoading().setValue(false);
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        colorStatusBar(this.getWindow(), R.color.colorPrimary, false);
    }

    private void setupLayoutManager() {
        movieDetailsViewHolder.recyclerViewDetails.setLayoutManager(linearLayoutManager);
        movieDetailsViewHolder.recyclerViewDetails.setHasFixedSize(true);
    }

    private void setupListeners(){
        movieDetailsViewHolder.textViewToobar.setOnClickListener(titleToolbarOnClickListener);
        movieDetailsViewHolder.backArrow.setOnClickListener(backArrowListener);
        movieDetailsViewHolder.checkBox.setOnCheckedChangeListener(onCheckedChangeDetailsListener);
    }

    private CompoundButton.OnCheckedChangeListener onCheckedChangeDetailsListener = new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
/*            if(isChecked){
                movieDetailsViewModel.executeAddFavoriteFilm("email",
                        String.valueOf(SingletonFilmID.INSTANCE.getID()));
            } else {
                movieDetailsViewModel.executeRemoveFavoriteFilm("email",
                        String.valueOf(SingletonFilmID.INSTANCE.getID()));
            }*/
        }
    };

    private void setupObservers(){
        movieDetailsViewModel.getIsMessageSuccessForToast().observe(this,isSuccessMessageForToastObserver);
        movieDetailsViewModel.getIsLoading().observe(this,progressBarObserver);
        movieDetailsViewModel.getThereIsMovieDetails().observe(this,thereIsMovieDetailsObserver);
        movieDetailsViewModel.getActivityTellerThereIsFilmResults().observe(this, homeTellerThereIsFilmResultsObserver);
        movieDetailsViewModel.getIsErrorMessageForToast().observe(this, isErrorMessageForToastObserver);
    }

    private View.OnClickListener backArrowListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            onBackPressed();
            SingletonFilmID.setIDEntered(null);

        }
    };

    private View.OnClickListener titleToolbarOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(MovieDetailsActivity.this, HomeActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            SingletonFilmID.setIDEntered(null);
        }
    };

    private Observer<String> isSuccessMessageForToastObserver = new Observer<String>() {
        @Override
        public void onChanged(String message) {
            TastyToast.makeText(MovieDetailsActivity.this, message, TastyToast.LENGTH_LONG, TastyToast.SUCCESS)
                    .setGravity(Gravity.CENTER, 0, 700);
        }
    };

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        SingletonFilmID.setIDEntered(null);
    }

    private final Observer<PagedList<FilmResponse>> pagedListObserver = new Observer<PagedList<FilmResponse>>() {
        @Override
        public void onChanged(PagedList<FilmResponse> filmResponses) {
            listfilmResponses = filmResponses.snapshot();
            if (adapter == null && SingletonFilmGenres.INSTANCE.getFilmGenres() != null) {
                if(mMovieDetailsModel != null){
                    adapter = new FilmAdapterDetailsList(MovieDetailsActivity.this,mMovieDetailsModel,SingletonFilmGenres.INSTANCE.getFilmGenres());
                } else {
                    if(SingletonFilmID.INSTANCE.getID() != null){
                        Integer filmID = SingletonFilmID.INSTANCE.getID();
                        movieDetailsViewModel.executeServiceGetMovieDetails(filmID);
                        movieDetailsViewModel.executeServiceGetSimilarMovies("1",filmID);
                    } else {
                        Intent intent = new Intent(MovieDetailsActivity.this, HomeActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                    }
                }
            } else {
                adapter.submitList(filmResponses);
                movieDetailsViewModel.getIsLoading().setValue(false);
            }
        }
    };

    private Observer<String> isErrorMessageForToastObserver = new Observer<String>() {
        @Override
        public void onChanged(String message) {
            TastyToast.makeText(MovieDetailsActivity.this, message, TastyToast.LENGTH_LONG, TastyToast.ERROR)
                    .setGravity(Gravity.CENTER, 0, 700);
        }
    };

    private Observer<FilmsResults> homeTellerThereIsFilmResultsObserver = new Observer<FilmsResults>() {
        @Override
        public void onChanged(final FilmsResults filmsResults) {
            movieDetailsViewModel.getItemPagedList().observe(MovieDetailsActivity.this, pagedListObserver);
            SingletonTotalResults.setTotalResultsEntered(filmsResults.getTotal_results());

        }
    };

    private Observer<Boolean> progressBarObserver = new Observer<Boolean>() {
        @Override
        public void onChanged(Boolean isLoading) {
            loadingExecutor(isLoading,
                    movieDetailsViewHolder.progressBarFragment,
                    movieDetailsViewHolder.frameLayout);
        }
    };

    private Observer<MovieDetailsModel> thereIsMovieDetailsObserver = new Observer<MovieDetailsModel>() {
        @Override
        public void onChanged(final MovieDetailsModel movieDetailsModel) {
            mMovieDetailsModel = movieDetailsModel;
            movieDetailsViewModel.getSimilarMoviesListEmpty().observe(MovieDetailsActivity.this, new Observer<Boolean>() {
                @Override
                public void onChanged(Boolean isSimilarMoviesListEmpty) {
                    if(isSimilarMoviesListEmpty && mMovieDetailsModel != null){
                        movieDetailsViewHolder.setMovieDetailsInformation(mMovieDetailsModel);
                        movieDetailsViewHolder.layoutItemDetails.setVisibility(View.VISIBLE);
                    } else {
                        movieDetailsViewHolder.layoutItemDetails.setVisibility(View.GONE);
                    }
                }
            });
            if (adapter == null && SingletonFilmGenres.INSTANCE.getFilmGenres() != null) {
                adapter = new FilmAdapterDetailsList(MovieDetailsActivity.this,mMovieDetailsModel,SingletonFilmGenres.INSTANCE.getFilmGenres());
            }
            movieDetailsViewHolder.recyclerViewDetails.setAdapter(adapter);
            adapter.setOnCheckBoxClickListener(new FilmAdapterDetailsList.OnCheckBoxClickListener() {
                @Override
                public void OnCheckBoxClick(int position, PagedList<FilmResponse> currentList, Boolean isChecked) {
                    Log.d("positionOnCheck",String.valueOf(position));
                    if(isChecked){
/*                        if(position == 0){
                            movieDetailsViewModel.executeAddFavoriteFilm("email",
                                    String.valueOf(movieDetailsModel.getId()));
                        } else {
                            movieDetailsViewModel.executeAddFavoriteFilm("email",
                                    String.valueOf(currentList.get(position-1).getId()));
                        }*/
                    } else {
/*                        if(position == 0){
                            movieDetailsViewModel.executeRemoveFavoriteFilm("email",
                                    String.valueOf(movieDetailsModel.getId()));
                        } else {
                            movieDetailsViewModel.executeRemoveFavoriteFilm("email",
                                    String.valueOf(currentList.get(position-1).getId()));
                        }*/

                    }
                }
            });
            adapter.setOnItemClickListener(new FilmAdapterDetailsList.OnItemClickListener() {
                @Override
                public void onItemClick(int position, PagedList<FilmResponse> currentList) {
                    if (movieDetailsModel != null) {
                        movieDetailsViewModel.getIsLoading().setValue(true);
                        SingletonFilmID.setIDEntered(currentList.get(position-1).getId());
                        if(SingletonFilmID.INSTANCE.getID() != null){
                            Intent intent = new Intent(MovieDetailsActivity.this, MovieDetailsActivity.class);
                            startActivity(intent);
                        }
                    }
                }
            });
        }
    };

    public void loadingExecutor(Boolean isLoading, ProgressBar progressBar, FrameLayout frameLayout) {
        if (isLoading != null) {
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
}
