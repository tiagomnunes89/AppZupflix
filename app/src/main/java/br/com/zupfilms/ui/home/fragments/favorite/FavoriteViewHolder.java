package br.com.zupfilms.ui.home.fragments.favorite;

import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import br.com.zupfilms.R;

public class FavoriteViewHolder {

    ProgressBar progressBar;
    FrameLayout frameLayout;
    RecyclerView recyclerView;
    SwipeRefreshLayout swipeRefreshLayout;
    TextView textViewFavoriteListNotFound;

    public FavoriteViewHolder(View view) {
        progressBar = view.findViewById(R.id.progress_bar);
        frameLayout = view.findViewById(R.id.loading_layout);
        recyclerView = view.findViewById(R.id.recycler_films);
        swipeRefreshLayout = view.findViewById(R.id.swiperefresh);
        textViewFavoriteListNotFound = view.findViewById(R.id.textView_FavoriteListNotFound);
    }
}
