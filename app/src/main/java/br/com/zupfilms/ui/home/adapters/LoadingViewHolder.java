package br.com.zupfilms.ui.home.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.FrameLayout;

import br.com.zupfilms.R;

class LoadingViewHolder extends RecyclerView.ViewHolder{

    private FrameLayout frameLayoutLoad;

    public LoadingViewHolder(View view) {
        super(view);
        frameLayoutLoad = view.findViewById(R.id.loading_layout_list);
    }
}
