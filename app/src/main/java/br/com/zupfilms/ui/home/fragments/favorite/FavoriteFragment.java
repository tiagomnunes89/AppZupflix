package br.com.zupfilms.ui.home.fragments.favorite;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

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
    private List<MovieDetailsModelDB> modelDBList;
    private DB db;


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
        favoriteViewModel.getThereAreMovieDetailsToSaveOffline().observe(this.getViewLifecycleOwner(), thereIsMovieDetailsObserver);
        adapter.setOnCheckBoxClickListener((position, currentList, isChecked) -> {
            SingletonFilmID.setIDEntered(currentList.get(position).getId());
            if (isChecked) {
                favoriteViewModel.executeServiceGetMovieDetailsToSaveOffline(currentList.get(position).getId());
            } else {
                db.delete(currentList.get(position).getId());
                adapter.notifyDataSetChanged();
            }
        });
        adapter.setOnItemClickListener((position, currentList) -> {
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
