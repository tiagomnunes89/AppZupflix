package br.com.zupfilms.ui.home.fragments.movieList;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.arch.paging.PagedList;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sdsmdg.tastytoast.TastyToast;

import br.com.zupfilms.R;
import br.com.zupfilms.data.DB;
import br.com.zupfilms.model.MovieDetailsModel;
import br.com.zupfilms.server.response.FilmResponse;
import br.com.zupfilms.server.response.FilmsResults;
import br.com.zupfilms.ui.BaseFragment;
import br.com.zupfilms.ui.home.adapters.FilmAdapter;
import br.com.zupfilms.ui.home.homeActivity.HomeActivity;
import br.com.zupfilms.ui.home.movieDetailsActivity.MovieDetailsActivity;
import br.com.zupfilms.ui.singleton.SingletonFilmGenres;
import br.com.zupfilms.ui.singleton.SingletonFilmID;
import br.com.zupfilms.ui.singleton.SingletonGenreID;
import br.com.zupfilms.ui.singleton.SingletonTotalResults;

public class MovieListFragment extends BaseFragment {

    private MovieListViewModel movieListViewModel;
    private MovieListViewHolder movieListViewHolder;
    private FilmAdapter adapter;
    private LinearLayoutManager linearLayoutManager;
    private String FIRST_PAGE = "1";
    private DB db;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_movie_list, container, false);
        this.movieListViewHolder = new MovieListViewHolder(view);

        linearLayoutManager = new LinearLayoutManager(getActivity());

        db = new DB(getActivity());

        movieListViewModel = ViewModelProviders.of(MovieListFragment.this).get(MovieListViewModel.class);

        if (verifyConnection()) {
            if (SingletonGenreID.INSTANCE.getGenreID() != null) {
                movieListViewModel.executeServiceGetFilmResults(FIRST_PAGE, SingletonGenreID.INSTANCE.getGenreID());
                if (adapter == null) {
                    adapter = new FilmAdapter(getActivity(), SingletonFilmGenres.INSTANCE.getFilmGenres());
                }
                movieListViewHolder.recyclerView.setVisibility(View.VISIBLE);
            } else {
                Intent intent = new Intent(getActivity(), HomeActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        } else {
            Intent intent = new Intent(getActivity(), HomeActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }

        return view;
    }

    @Override
    public void onPause() {
        super.onPause();
        movieListViewModel.getIsLoading().setValue(false);
    }

    @Override
    public void onResume() {
        super.onResume();
        setupObserversAndListeners();
        setupLayoutManager();
    }

    private void setupLayoutManager() {
        movieListViewHolder.recyclerView.setLayoutManager(linearLayoutManager);
        movieListViewHolder.recyclerView.setHasFixedSize(true);
    }

    private void setupObserversAndListeners() {
        movieListViewModel.getIsMessageSuccessForToast().observe(this, isSuccessMessageForToastObserver);
        movieListViewModel.getIsLoading().observe(this, progressBarObserver);
        movieListViewModel.getFragmentTellerThereIsFilmResults().observe(this, homeTellerThereIsFilmResultsObserver);
        movieListViewModel.getIsErrorMessageForToast().observe(this, isErrorMessageForToastObserver);
        movieListViewModel.getThereAreMovieDetailsToSaveOffline().observe(this, thereIsMovieDetailsObserver);
    }

    private Observer<MovieDetailsModel> thereIsMovieDetailsObserver = new Observer<MovieDetailsModel>() {
        @Override
        public void onChanged(MovieDetailsModel movieDetailsModel) {
            if (movieDetailsModel != null && db != null) {
                db.insert(movieDetailsModel);
                adapter.notifyDataSetChanged();
            }
        }
    };

    private Observer<String> isSuccessMessageForToastObserver = new Observer<String>() {
        @Override
        public void onChanged(String message) {
            TastyToast.makeText(getActivity(), message, TastyToast.LENGTH_LONG, TastyToast.SUCCESS)
                    .setGravity(Gravity.CENTER, 0, 700);
        }
    };

    private Observer<String> isErrorMessageForToastObserver = new Observer<String>() {
        @Override
        public void onChanged(String message) {
            TastyToast.makeText(getActivity(), message, TastyToast.LENGTH_LONG, TastyToast.ERROR)
                    .setGravity(Gravity.CENTER, 0, 700);
        }
    };

    private final Observer<PagedList<FilmResponse>> pagedListObserver = new Observer<PagedList<FilmResponse>>() {
        @Override
        public void onChanged(PagedList<FilmResponse> filmResponses) {
            adapter.submitList(filmResponses);
            movieListViewModel.getIsLoading().setValue(false);
        }
    };

    private Observer<FilmsResults> homeTellerThereIsFilmResultsObserver = new Observer<FilmsResults>() {
        @Override
        public void onChanged(final FilmsResults filmsResults) {
            movieListViewModel.getItemPagedList().observe(MovieListFragment.this, pagedListObserver);
            movieListViewHolder.recyclerView.setAdapter(adapter);
            SingletonTotalResults.setTotalResultsEntered(filmsResults.getTotal_results());
            adapter.setOnCheckBoxClickListener(new FilmAdapter.OnCheckBoxClickListener() {
                @Override
                public void OnCheckBoxClick(int position, PagedList<FilmResponse> currentList, Boolean isChecked) {
                    SingletonFilmID.setIDEntered(currentList.get(position).getId());
                    if (db != null) {
                        if (isChecked) {
                            movieListViewModel.executeServiceGetMovieDetailsToSaveOffline(currentList.get(position).getId());
                        } else {
                            db.delete(currentList.get(position).getId());
                            adapter.notifyDataSetChanged();
                        }
                    }
                }
            });
            adapter.setOnItemClickListener(new FilmAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(int position, PagedList<FilmResponse> currentList) {
                    SingletonFilmID.setIDEntered(currentList.get(position).getId());
                    if (SingletonFilmID.INSTANCE.getID() != null) {
                        Intent intent = new Intent(getActivity(), MovieDetailsActivity.class);
                        startActivity(intent);
                    }
                }
            });
        }
    };

    private Observer<Boolean> progressBarObserver = new Observer<Boolean>() {
        @Override
        public void onChanged(Boolean isLoading) {
            loadingExecutor(isLoading,
                    movieListViewHolder.progressBar,
                    movieListViewHolder.frameLayout);
        }
    };

    public static MovieListFragment newInstance() {
        return new MovieListFragment();
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        movieListViewModel.removeObserver();
        SingletonFilmID.setIDEntered(null);
    }
}