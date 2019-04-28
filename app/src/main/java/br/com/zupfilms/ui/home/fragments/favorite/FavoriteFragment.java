/*
package br.com.zupfilms.ui.home.fragments.favorite;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.arch.paging.PagedList;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ProgressBar;

import com.github.ybq.android.spinkit.sprite.Sprite;
import com.github.ybq.android.spinkit.style.ThreeBounce;
import com.sdsmdg.tastytoast.TastyToast;

import br.com.zupfilms.R;
import br.com.zupfilms.server.response.FilmResponse;
import br.com.zupfilms.server.response.FilmsResults;
import br.com.zupfilms.ui.BaseFragment;
import br.com.zupfilms.ui.home.adapters.FilmAdapter;
import br.com.zupfilms.ui.home.movieDetailsActivity.MovieDetailsActivity;
import br.com.zupfilms.ui.singleton.SingletonFilmID;
import br.com.zupfilms.ui.singleton.SingletonTotalResults;

public class FavoriteFragment extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener {

    private FavoriteViewModel favoriteViewModel;
    private FavoriteViewHolder favoriteViewHolder;
    private FilmAdapter adapter;
    private LinearLayoutManager linearLayoutManager;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_favorite, container,false);
        this.favoriteViewHolder = new FavoriteViewHolder(view);

        linearLayoutManager = new LinearLayoutManager(getActivity());

        favoriteViewModel = ViewModelProviders.of(FavoriteFragment.this).get(FavoriteViewModel.class);

        return view;
    }

    @Override
    public void onPause() {
        super.onPause();
        favoriteViewModel.getIsLoading().setValue(false);
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if(!hidden){
            onRefresh();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (adapter == null) {
            adapter = new FilmAdapter(getActivity());
        }
        setupObserversAndListeners();
        setupLayoutManager();
    }

    private void setupLayoutManager() {
        favoriteViewHolder.recyclerView.setLayoutManager(linearLayoutManager);
        favoriteViewHolder.recyclerView.setHasFixedSize(true);
    }

    private void setupObserversAndListeners() {
        favoriteViewModel.getIsMessageSuccessForToast().observe(this, isSuccessMessageForToastObserver);
        favoriteViewModel.getIsLoading().observe(this, progressBarObserver);
        favoriteViewModel.getFragmentTellerThereIsFilmResults().observe(this, homeTellerThereIsFilmResultsObserver);
        favoriteViewModel.getIsErrorMessageForToast().observe(this, isErrorMessageForToastObserver);
        favoriteViewHolder.swipeRefreshLayout.setOnRefreshListener(this);
        favoriteViewModel.getIsFavoriteListEmpty().observe(this,isFavoriteListEmptyObserver);
    }

    private Observer<Boolean> isFavoriteListEmptyObserver = new Observer<Boolean>() {
        @Override
        public void onChanged(Boolean isFavoriteListEmpty) {
            if(isFavoriteListEmpty){
                favoriteViewModel.getIsLoading().setValue(false);
                favoriteViewHolder.textViewFavoriteListNotFound.setVisibility(View.VISIBLE);
                favoriteViewHolder.recyclerView.setVisibility(View.GONE);
            } else {
                favoriteViewHolder.textViewFavoriteListNotFound.setVisibility(View.GONE);
                favoriteViewHolder.recyclerView.setVisibility(View.VISIBLE);
            }
        }
    };

    @Override
    public void onRefresh() {
        favoriteViewHolder.recyclerView.setVisibility(View.GONE);
            new Handler().postDelayed(new Runnable() {
                @Override public void run() {
                    if (favoriteViewHolder.swipeRefreshLayout.isRefreshing()) {
                        favoriteViewHolder.swipeRefreshLayout.setRefreshing(false);
                    }
                }
            }, 3000);
            adapter.submitList(null);
            favoriteViewModel.executeServiceGetSimilarMovies("email");
        }


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
        public void onChanged(@Nullable PagedList<FilmResponse> filmResponses) {
            adapter.submitList(filmResponses);
            favoriteViewModel.getIsLoading().setValue(false);
            favoriteViewHolder.recyclerView.setVisibility(View.VISIBLE);
        }
    };

    private Observer<FilmsResults> homeTellerThereIsFilmResultsObserver = new Observer<FilmsResults>() {
        @Override
        public void onChanged(final FilmsResults filmsResults) {
            favoriteViewModel.getItemPagedList().observe(FavoriteFragment.this, pagedListObserver);
            favoriteViewHolder.recyclerView.setAdapter(adapter);
            SingletonTotalResults.setTotalResultsEntered(filmsResults.getTotal_results());
            adapter.setOnCheckBoxClickListener(new FilmAdapter.OnCheckBoxClickListener() {
                @Override
                public void OnCheckBoxClick(int position, PagedList<FilmResponse> currentList, Boolean isChecked) {
                    SingletonFilmID.setIDEntered(currentList.get(position).getId());
                    if(isChecked){
                        favoriteViewModel.executeAddFavoriteFilm("email",
                                String.valueOf(SingletonFilmID.INSTANCE.getID()));
                    } else {
                        favoriteViewModel.executeRemoveFavoriteFilm("email",
                                String.valueOf(SingletonFilmID.INSTANCE.getID()));
                    }
                }
            });
            adapter.setOnItemClickListener(new FilmAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(int position, PagedList<FilmResponse> currentList) {
                    Log.d("position",String.valueOf(position));
                        favoriteViewModel.getIsLoading().setValue(true);
                        SingletonFilmID.setIDEntered(currentList.get(position).getId());
                        if(SingletonFilmID.INSTANCE.getID() != null){
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
                    favoriteViewHolder.progressBar,
                    favoriteViewHolder.frameLayout);
        }
    };

    public void loadingExecutor(Boolean isLoading, ProgressBar progressBar, FrameLayout frameLayout) {
        if (isLoading != null && getActivity() != null) {
            if (isLoading) {
                Sprite threeBounce = new ThreeBounce();
                progressBar.setIndeterminateDrawable(threeBounce);
                favoriteViewHolder.textViewFavoriteListNotFound.setVisibility(View.GONE);
                frameLayout.setVisibility(View.VISIBLE);

            } else {
                Sprite threeBounce = new ThreeBounce();
                progressBar.setIndeterminateDrawable(threeBounce);
                frameLayout.setVisibility(View.INVISIBLE);
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        favoriteViewModel.removeObserver();
        SingletonFilmID.setIDEntered(null);
    }
}
*/
