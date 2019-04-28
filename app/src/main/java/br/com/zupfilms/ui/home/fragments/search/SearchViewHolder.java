package br.com.zupfilms.ui.home.fragments.search;

import android.support.design.widget.BottomNavigationView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.TextView;

import br.com.zupfilms.R;

public class SearchViewHolder {

    ProgressBar progressBar;
    FrameLayout frameLayout;
    RecyclerView recyclerView;
    SearchView searchView;
    TextView textViewFilmNotFound;
    BottomNavigationView bottomNavigationView;


    public SearchViewHolder(View view) {
        progressBar = view.findViewById(R.id.progress_bar);
        frameLayout = view.findViewById(R.id.loading_layout);
        recyclerView = view.findViewById(R.id.recycler_films);
        searchView = view.findViewById(R.id.search_view);
        textViewFilmNotFound = view.findViewById(R.id.textViewFilmNotFound);
        bottomNavigationView = view.findViewById(R.id.bottom_navigation);
    }
}
