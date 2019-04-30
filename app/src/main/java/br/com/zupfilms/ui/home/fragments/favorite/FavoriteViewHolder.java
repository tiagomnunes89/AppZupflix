package br.com.zupfilms.ui.home.fragments.favorite;

import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import br.com.zupfilms.R;

class FavoriteViewHolder {

    private ProgressBar progressBar;
    private FrameLayout frameLayout;
    final SwipeRefreshLayout swipeRefreshLayout;
    final TextView textViewFavoriteListNotFound;

    public FavoriteViewHolder(View view) {
        progressBar = view.findViewById(R.id.progress_bar);
        frameLayout = view.findViewById(R.id.loading_layout);
        swipeRefreshLayout = view.findViewById(R.id.swiperefresh);
        textViewFavoriteListNotFound = view.findViewById(R.id.textView_FavoriteListNotFound);
    }
}
