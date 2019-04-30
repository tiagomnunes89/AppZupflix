package br.com.zupfilms.ui.home.fragments.search;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.arch.paging.PagedList;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.SearchView;

import com.github.ybq.android.spinkit.sprite.Sprite;
import com.github.ybq.android.spinkit.style.ThreeBounce;
import com.sdsmdg.tastytoast.TastyToast;

import br.com.zupfilms.R;
import br.com.zupfilms.data.DB;
import br.com.zupfilms.model.MovieDetailsModel;
import br.com.zupfilms.server.response.FilmResponse;
import br.com.zupfilms.server.response.FilmsResults;
import br.com.zupfilms.ui.BaseFragment;
import br.com.zupfilms.ui.home.adapters.FilmAdapter;
import br.com.zupfilms.ui.home.movieDetailsActivity.MovieDetailsActivity;
import br.com.zupfilms.ui.singleton.SingletonFilmGenres;
import br.com.zupfilms.ui.singleton.SingletonFilmID;
import br.com.zupfilms.ui.singleton.SingletonTotalResults;

public class SearchFragment extends BaseFragment {

    private SearchViewHolder searchViewHolder;
    private SearchViewModel searchViewModel;
    private FilmAdapter adapter;
    private LinearLayoutManager linearLayoutManager;
    private DB db;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search, container, false);
        this.searchViewHolder = new SearchViewHolder(view);

        db = new DB(getActivity());

        linearLayoutManager = new LinearLayoutManager(getActivity());

        searchViewModel = ViewModelProviders.of(SearchFragment.this).get(SearchViewModel.class);

        searchViewHolder.searchView.setQueryHint("Buscar filmes...");

        searchViewHolder.searchView.setOnQueryTextListener(searchViewListener);

        searchViewHolder.searchView.setIconified(false);

        return view;
    }

    private View.OnClickListener textServiceDisable = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            searchViewHolder.textViewServiceDisable.setVisibility(View.GONE);
            if(verifyConection()){
                searchViewHolder.searchView.setQuery("",false);
                searchViewHolder.searchView.setQueryHint("Conectado. Busque seu filme aqui!");
            } else {
                searchViewHolder.textViewServiceDisable.setVisibility(View.VISIBLE);
            }
        }
    };

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden) {
            searchViewHolder.searchView.setIconified(false);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        searchViewModel.getIsLoading().setValue(false);
        Log.d("TAG", "onPause");
    }

    public void onResume() {
        super.onResume();
        if (adapter == null && SingletonFilmGenres.INSTANCE.getFilmGenres() != null) {
            adapter = new FilmAdapter(getActivity(), SingletonFilmGenres.INSTANCE.getFilmGenres());
        }
        setupObserversAndListeners();
        setupLayoutManager();
        Log.d("TAG", "onResume");
    }

    private void setupLayoutManager() {
        searchViewHolder.recyclerView.setLayoutManager(linearLayoutManager);
        searchViewHolder.recyclerView.setHasFixedSize(true);
    }

    private void setupObserversAndListeners() {
        searchViewModel.getIsMessageSuccessForToast().observe(this, isSuccessMessageForToastObserver);
        searchViewModel.getIsLoading().observe(this, progressBarObserver);
        searchViewModel.getFragmentTellerThereIsFilmResults().observe(this, homeTellerThereIsFilmResultsObserver);
        searchViewModel.getIsErrorMessageForToast().observe(this, isErrorMessageForToastObserver);
        searchViewModel.getIsSearchEmpty().observe(this, isSearchEmptyObserver);
        searchViewModel.getThereIsMovieDetailsToSaveOffiline().observe(this, thereIsMovieDetailsObserver);
        searchViewHolder.textViewServiceDisable.setOnClickListener(textServiceDisable);
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

    private Observer<Boolean> isSearchEmptyObserver = new Observer<Boolean>() {
        @Override
        public void onChanged(Boolean isSearchEmpty) {
            if (isSearchEmpty) {
                searchViewModel.getIsLoading().setValue(false);
                searchViewHolder.textViewFilmNotFound.setVisibility(View.VISIBLE);
                searchViewHolder.recyclerView.setVisibility(View.GONE);
            } else {
                searchViewHolder.textViewFilmNotFound.setVisibility(View.GONE);
                searchViewHolder.recyclerView.setVisibility(View.VISIBLE);
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
        public void onChanged(@Nullable PagedList<FilmResponse> filmResponses) {
            adapter.submitList(filmResponses);
            searchViewModel.getIsLoading().setValue(false);
        }
    };

    private Observer<FilmsResults> homeTellerThereIsFilmResultsObserver = new Observer<FilmsResults>() {
        @Override
        public void onChanged(final FilmsResults filmsResults) {
            searchViewModel.getItemPagedList().observe(SearchFragment.this, pagedListObserver);
            searchViewHolder.recyclerView.setAdapter(adapter);
            SingletonTotalResults.setTotalResultsEntered(filmsResults.getTotal_results());
            adapter.setOnCheckBoxClickListener(new FilmAdapter.OnCheckBoxClickListener() {
                @Override
                public void OnCheckBoxClick(int position, PagedList<FilmResponse> currentList, Boolean isChecked) {
                    SingletonFilmID.setIDEntered(currentList.get(position).getId());
                    if (db != null) {
                        if (isChecked) {
                            searchViewModel.executeServiceGetMovieDetailsToSaveOffiline(currentList.get(position).getId());
                        } else {
                            db.delete(currentList.get(position).getId());
                            adapter.remove(position);
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
                    searchViewHolder.progressBar,
                    searchViewHolder.frameLayout);
        }
    };

    private SearchView.OnQueryTextListener searchViewListener = new SearchView.OnQueryTextListener() {
        @Override
        public boolean onQueryTextSubmit(String query) {
            return false;
        }


        @Override
        public boolean onQueryTextChange(String newText) {
            searchViewHolder.recyclerView.setVisibility(View.GONE);
            if (verifyConection()) {
                if (!newText.isEmpty()) {
                    if(SingletonFilmID.INSTANCE.getID() != null && adapter != null){
                        SingletonFilmID.setIDEntered(null);
                        adapter.submitList(null);
                    }
                    searchViewModel.executeServiceGetFilmResultsSearch(newText);
                    searchViewHolder.searchView.setQueryHint("Buscar filmes...");
                } else {
                    if(adapter!= null){
                        adapter.submitList(null);
                    }
                }
            } else {
                searchViewHolder.recyclerView.setVisibility(View.GONE);
                TastyToast.makeText(getActivity(), getString(R.string.NO_CONNECTION_MESSAGE), TastyToast.LENGTH_LONG, TastyToast.ERROR)
                        .setGravity(Gravity.CENTER, 0, 700);
                searchViewHolder.textViewServiceDisable.setVisibility(View.VISIBLE);
            }
            return false;
        }
    };

    public void loadingExecutor(Boolean isLoading, ProgressBar progressBar, FrameLayout frameLayout) {
        if (isLoading != null && getActivity() != null) {
            if (isLoading) {
                Sprite threeBounce = new ThreeBounce();
                progressBar.setIndeterminateDrawable(threeBounce);
                searchViewHolder.textViewFilmNotFound.setVisibility(View.GONE);
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
        searchViewModel.removeObserver();
    }
}
