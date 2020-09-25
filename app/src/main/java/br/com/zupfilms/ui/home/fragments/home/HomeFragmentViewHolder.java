package br.com.zupfilms.ui.home.fragments.home;

import com.google.android.material.tabs.TabLayout;
import androidx.viewpager.widget.ViewPager;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import br.com.zupfilms.R;

class HomeFragmentViewHolder {

    final TabLayout tabLayout;
    final ViewPager viewPager;
    final ProgressBar progressBar;
    final FrameLayout frameLayout;
    final TextView textViewServiceDisable;

    public HomeFragmentViewHolder(View view) {
        tabLayout = view.findViewById(R.id.tabLayout);
        viewPager = view.findViewById(R.id.viewPager);
        progressBar = view.findViewById(R.id.progress_bar);
        frameLayout = view.findViewById(R.id.loading_layout);
        textViewServiceDisable = view.findViewById(R.id.textViewServiceDisable);
    }
}
