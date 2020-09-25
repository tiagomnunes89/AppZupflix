package br.com.zupfilms.ui.home.fragments.movieList;

import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ProgressBar;

import br.com.zupfilms.R;

class MovieListViewHolder {

    final ProgressBar progressBar;
    final FrameLayout frameLayout;
    final RecyclerView recyclerView;

    public MovieListViewHolder(View view) {
        progressBar = view.findViewById(R.id.progress_bar);
        frameLayout = view.findViewById(R.id.loading_layout);
        recyclerView = view.findViewById(R.id.recycler_films);
    }
}
