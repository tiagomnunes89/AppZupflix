package br.com.zupfilms.ui.home.fragments.favorite;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;
import java.util.Objects;

import br.com.zupfilms.R;
import br.com.zupfilms.data.DB;
import br.com.zupfilms.model.MovieDetailsModel;
import br.com.zupfilms.model.MovieDetailsModelDB;
import br.com.zupfilms.ui.BaseFragment;
import br.com.zupfilms.ui.home.adapters.MoviesAdapter;
import br.com.zupfilms.ui.home.movieDetailsActivity.MovieDetailsActivity;
import br.com.zupfilms.ui.singleton.SingletonFilmID;


public class FavoriteFragment extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener {

    private FavoriteViewHolder favoriteViewHolder;
    private FavoriteViewModel favoriteViewModel;
    private MoviesAdapter adapter;
    private DB db;
    private List<MovieDetailsModelDB> modelDBList;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_favorite, container, false);
        this.favoriteViewHolder = new FavoriteViewHolder(view);

        favoriteViewModel = ViewModelProviders.of(FavoriteFragment.this).get(FavoriteViewModel.class);

        db = new DB(getActivity());

        modelDBList = db.getAllFavoritesFilms();

        adapter = new MoviesAdapter(getActivity(), modelDBList);

        setupListenersAndObservers();

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());

        RecyclerView recyclerView = SetupsLayout(view);

        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        if (modelDBList.isEmpty()) {
            recyclerView.setVisibility(View.GONE);
            favoriteViewHolder.textViewFavoriteListNotFound.setVisibility(View.VISIBLE);
        } else {
            favoriteViewHolder.textViewFavoriteListNotFound.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
        }
        return view;
    }

    private RecyclerView SetupsLayout(View view) {
        return view.findViewById(R.id.recycler_films);
    }

    private void setupListenersAndObservers() {
        favoriteViewHolder.swipeRefreshLayout.setOnRefreshListener(this);
        favoriteViewModel.getThereAreMovieDetailsToSaveOffline().observe(this, thereIsMovieDetailsObserver);
        adapter.setOnCheckBoxClickListener(new MoviesAdapter.OnCheckBoxClickListener() {
            @Override
            public void OnCheckBoxClick(int position, List<MovieDetailsModelDB> currentList, Boolean isChecked) {
                SingletonFilmID.setIDEntered(currentList.get(position).getId());
                if (isChecked) {
                    favoriteViewModel.executeServiceGetMovieDetailsToSaveOffline(currentList.get(position).getId());
                } else {
                    db.delete(currentList.get(position).getId());
                    adapter.notifyDataSetChanged();
                }
            }
        });
        adapter.setOnItemClickListener(new MoviesAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position, List<MovieDetailsModelDB> currentList) {
                if (verifyConnection()) {
                    SingletonFilmID.setIDEntered(currentList.get(position).getId());
                    if (SingletonFilmID.INSTANCE.getID() != null) {
                        Intent intent = new Intent(getActivity(), MovieDetailsActivity.class);
                        startActivity(intent);
                    }
                } else {
                    SingletonFilmID.setIDEntered(modelDBList.get(position).getId());
                    if (SingletonFilmID.INSTANCE.getID() != null) {
                        Intent intent = new Intent(getActivity(), MovieDetailsActivity.class);
                        startActivity(intent);
                    }
                }

            }
        });
    }

    private final Observer<MovieDetailsModel> thereIsMovieDetailsObserver = new Observer<MovieDetailsModel>() {
        @Override
        public void onChanged(MovieDetailsModel movieDetailsModel) {
            if (movieDetailsModel != null && db != null) {
                db.insert(movieDetailsModel);
                adapter.notifyDataSetChanged();
            }
        }
    };

    @Override
    public void onRefresh() {
        RecyclerView recyclerView = SetupsLayout(Objects.requireNonNull(getView()));
        recyclerView.setVisibility(View.GONE);
        if (adapter != null) {
            DB db = new DB(getActivity());
            modelDBList.clear();
            modelDBList.addAll(db.getAllFavoritesFilms());
            adapter.setMovieList(modelDBList);

            recyclerView.setAdapter(adapter);

            if (favoriteViewHolder.swipeRefreshLayout.isRefreshing()) {
                favoriteViewHolder.swipeRefreshLayout.setRefreshing(false);
            }
            if (modelDBList.isEmpty()) {
                recyclerView.setVisibility(View.GONE);
                favoriteViewHolder.textViewFavoriteListNotFound.setVisibility(View.VISIBLE);
            } else {
                favoriteViewHolder.textViewFavoriteListNotFound.setVisibility(View.GONE);
                recyclerView.setVisibility(View.VISIBLE);
            }
        } else {
            recyclerView.setVisibility(View.GONE);
            favoriteViewHolder.textViewFavoriteListNotFound.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden) {
            onRefresh();
        }
    }
}
