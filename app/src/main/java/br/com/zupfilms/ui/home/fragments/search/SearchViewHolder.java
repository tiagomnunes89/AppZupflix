package br.com.zupfilms.ui.home.fragments.search;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.TextView;

import br.com.zupfilms.R;

class SearchViewHolder {

    final ProgressBar progressBar;
    final FrameLayout frameLayout;
    final RecyclerView recyclerView;
    final SearchView searchView;
    final TextView textViewFilmNotFound;
    private final BottomNavigationView bottomNavigationView;
    final TextView textViewServiceDisable;


    public SearchViewHolder(View view) {
        progressBar = view.findViewById(R.id.progress_bar);
        frameLayout = view.findViewById(R.id.loading_layout);
        recyclerView = view.findViewById(R.id.recycler_films);
        searchView = view.findViewById(R.id.search_view);
        textViewFilmNotFound = view.findViewById(R.id.textViewFilmNotFound);
        bottomNavigationView = view.findViewById(R.id.botton_nav_view);
        textViewServiceDisable = view.findViewById(R.id.textViewServiceDisable);
    }
}
