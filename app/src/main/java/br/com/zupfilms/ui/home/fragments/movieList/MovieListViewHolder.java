package br.com.zupfilms.ui.home.fragments.movieList;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ProgressBar;

import br.com.zupfilms.R;

public class MovieListViewHolder {

    ProgressBar progressBar;
    FrameLayout frameLayout;
    RecyclerView recyclerView;


    public MovieListViewHolder(View view) {
        progressBar = view.findViewById(R.id.progress_bar);
        frameLayout = view.findViewById(R.id.loading_layout);
        recyclerView = view.findViewById(R.id.recycler_films);
    }
}
