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
import android.view.Gravity;
import android.view.View;
import android.widget.CompoundButton;

import com.sdsmdg.tastytoast.TastyToast;

import java.util.Objects;

import br.com.zupfilms.R;
import br.com.zupfilms.data.DB;
import br.com.zupfilms.model.MovieDetailsModel;
import br.com.zupfilms.model.MovieDetailsModelDB;
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

    private DB db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        View view = this.getLayoutInflater().inflate(R.layout.activity_movie_details, null);
        this.movieDetailsViewHolder = new MovieDetailsViewHolder(view);

        setContentView(view);

        db = new DB(this);

        movieDetailsViewModel = ViewModelProviders.of(this).get(MovieDetailsViewModel.class);

        SpannableString spannableString = new SpannableString("ZUP" + "FLIX");
        spannableString.setSpan(new StyleSpan(Typeface.BOLD), 0, 2, 0);
        movieDetailsViewHolder.textViewToobar.setText(spannableString);

        setupListeners();

        setupObservers();

        linearLayoutManager = new LinearLayoutManager(this);

        setupLayoutManager();

        if(SingletonFilmID.INSTANCE.getID() != null){
            if(connectionVerifier()){
                Integer filmID = SingletonFilmID.INSTANCE.getID();
                movieDetailsViewModel.executeServiceGetMovieDetails(filmID);
                movieDetailsViewModel.executeServiceGetSimilarMovies("1",filmID);
            } else {
                Integer filmID = SingletonFilmID.INSTANCE.getID();
                MovieDetailsModelDB movieDetailsModelDB = db.getOneFavoritesFilms(filmID);
                if(movieDetailsModelDB != null){
                    movieDetailsViewHolder.setMovieDetailsDBInformation(movieDetailsModelDB);
                    movieDetailsViewHolder.layoutItemDetails.setVisibility(View.VISIBLE);
                } else {
                    TastyToast.makeText(MovieDetailsActivity.this, getString(R.string.NO_SERVICE_OFFILINE_DETAILS), TastyToast.LENGTH_LONG, TastyToast.ERROR)
                            .setGravity(Gravity.CENTER, 0, 700);
                }
            }
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
        colorStatusBar(this.getWindow());
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

    private final CompoundButton.OnCheckedChangeListener onCheckedChangeDetailsListener = new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            if(db != null){
                if(isChecked){
                    if(movieDetailsViewModel != null){
                        db.insert(mMovieDetailsModel);
                        adapter.notifyDataSetChanged();
                    }
                } else {
                    if(SingletonFilmID.INSTANCE.getID() != null){
                        db.delete(SingletonFilmID.INSTANCE.getID());
                        adapter.notifyDataSetChanged();
                    }

                }
            }
        }
    };

    private void setupObservers(){
        movieDetailsViewModel.getIsMessageSuccessForToast().observe(this,isSuccessMessageForToastObserver);
        movieDetailsViewModel.getIsLoading().observe(this,progressBarObserver);
        movieDetailsViewModel.getThereIsMovieDetails().observe(this,thereIsMovieDetailsObserver);
        movieDetailsViewModel.getActivityTellerThereIsFilmResults().observe(this, homeTellerThereIsFilmResultsObserver);
        movieDetailsViewModel.getIsErrorMessageForToast().observe(this, isErrorMessageForToastObserver);
        movieDetailsViewModel.getThereAreMovieDetailsToSaveOffline().observe(this, thereAreMovieDetailsToSaveOfflineObserver);
    }

    private final Observer<MovieDetailsModel> thereAreMovieDetailsToSaveOfflineObserver = new Observer<MovieDetailsModel>() {
        @Override
        public void onChanged(MovieDetailsModel movieDetailsModel) {
            if (movieDetailsModel != null && db != null) {
                db.insert(movieDetailsModel);
                adapter.notifyDataSetChanged();
            }

        }
    };

    private final View.OnClickListener backArrowListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            onBackPressed();
            SingletonFilmID.setIDEntered(null);

        }
    };

    private final View.OnClickListener titleToolbarOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(MovieDetailsActivity.this, HomeActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            SingletonFilmID.setIDEntered(null);
        }
    };

    private final Observer<String> isSuccessMessageForToastObserver = new Observer<String>() {
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
            if (adapter == null && SingletonFilmGenres.INSTANCE.getFilmGenres() != null) {
                if(mMovieDetailsModel != null){
                    adapter = new FilmAdapterDetailsList(MovieDetailsActivity.this,mMovieDetailsModel,SingletonFilmGenres.INSTANCE.getFilmGenres());
                } else {
                    if(SingletonFilmID.INSTANCE.getID() != null){
                        Integer filmID = SingletonFilmID.INSTANCE.getID();
                        movieDetailsViewModel.executeServiceGetMovieDetailsToSaveOffline(filmID);
                        movieDetailsViewModel.executeServiceGetSimilarMovies("1",filmID);
                    } else {
                        Intent intent = new Intent(MovieDetailsActivity.this, HomeActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                    }
                }
            } else {
                Objects.requireNonNull(adapter).submitList(filmResponses);
                movieDetailsViewModel.getIsLoading().setValue(false);
            }
        }
    };

    private final Observer<String> isErrorMessageForToastObserver = new Observer<String>() {
        @Override
        public void onChanged(String message) {
            TastyToast.makeText(MovieDetailsActivity.this, message, TastyToast.LENGTH_LONG, TastyToast.ERROR)
                    .setGravity(Gravity.CENTER, 0, 700);
        }
    };

    private final Observer<FilmsResults> homeTellerThereIsFilmResultsObserver = new Observer<FilmsResults>() {
        @Override
        public void onChanged(final FilmsResults filmsResults) {
            movieDetailsViewModel.getItemPagedList().observe(MovieDetailsActivity.this, pagedListObserver);
            SingletonTotalResults.setTotalResultsEntered(filmsResults.getTotal_results());

        }
    };

    private final Observer<Boolean> progressBarObserver = new Observer<Boolean>() {
        @Override
        public void onChanged(Boolean isLoading) {
            loadingExecutor(isLoading,
                    movieDetailsViewHolder.progressBarFragment,
                    movieDetailsViewHolder.frameLayout);
        }
    };

    private final Observer<MovieDetailsModel> thereIsMovieDetailsObserver = new Observer<MovieDetailsModel>() {
        @Override
        public void onChanged(final MovieDetailsModel movieDetailsModel) {
            mMovieDetailsModel = movieDetailsModel;
            movieDetailsViewModel.getSimilarMoviesListEmpty().observe(MovieDetailsActivity.this, isSimilarMoviesListEmpty -> {
                if(isSimilarMoviesListEmpty && mMovieDetailsModel != null){
                    movieDetailsViewHolder.setMovieDetailsInformation(mMovieDetailsModel);
                    movieDetailsViewHolder.layoutItemDetails.setVisibility(View.VISIBLE);
                } else {
                    movieDetailsViewHolder.layoutItemDetails.setVisibility(View.GONE);
                }
            });
            if (adapter == null && SingletonFilmGenres.INSTANCE.getFilmGenres() != null) {
                adapter = new FilmAdapterDetailsList(MovieDetailsActivity.this,mMovieDetailsModel,SingletonFilmGenres.INSTANCE.getFilmGenres());
            }
            movieDetailsViewHolder.recyclerViewDetails.setAdapter(adapter);
            adapter.setOnCheckBoxClickListener((position, currentList, isChecked) -> {
                SingletonFilmID.setIDEntered(Objects.requireNonNull(currentList.get(position)).getId());
                if (db != null) {
                    if (isChecked) {
                        if(position == 0){
                            movieDetailsViewModel.executeServiceGetMovieDetailsToSaveOffline(movieDetailsModel.getId());
                        } else {
                            movieDetailsViewModel.executeServiceGetMovieDetailsToSaveOffline(Objects.requireNonNull(currentList.get(position - 1)).getId());
                        }
                    } else {
                        if(position == 0) {
                            db.delete(movieDetailsModel.getId());
                            adapter.notifyDataSetChanged();
                        } else {
                            db.delete(Objects.requireNonNull(currentList.get(position - 1)).getId());
                            adapter.notifyDataSetChanged();
                        }
                    }
                }
            });
            adapter.setOnItemClickListener((position, currentList) -> {
                if (movieDetailsModel != null) {
                    movieDetailsViewModel.getIsLoading().setValue(true);
                    SingletonFilmID.setIDEntered(Objects.requireNonNull(currentList.get(position - 1)).getId());
                    if(SingletonFilmID.INSTANCE.getID() != null){
                        Intent intent = new Intent(MovieDetailsActivity.this, MovieDetailsActivity.class);
                        startActivity(intent);
                    }
                }
            });
        }
    };
}
