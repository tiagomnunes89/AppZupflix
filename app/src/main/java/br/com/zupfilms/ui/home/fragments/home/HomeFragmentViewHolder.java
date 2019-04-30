package br.com.zupfilms.ui.home.fragments.home;

import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import br.com.zupfilms.R;

public class HomeFragmentViewHolder {

    TabLayout tabLayout;
    ViewPager viewPager;
    Toolbar toolbar;
    ProgressBar progressBar;
    FrameLayout frameLayout;
    TextView textViewServiceDisable;

    public HomeFragmentViewHolder(View view) {
        tabLayout = view.findViewById(R.id.tabLayout);
        viewPager = view.findViewById(R.id.viewPager);
        progressBar = view.findViewById(R.id.progress_bar);
        frameLayout = view.findViewById(R.id.loading_layout);
        textViewServiceDisable = view.findViewById(R.id.textViewServiceDisable);
    }
}
