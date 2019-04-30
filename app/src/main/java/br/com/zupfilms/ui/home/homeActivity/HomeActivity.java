package br.com.zupfilms.ui.home.homeActivity;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.text.SpannableString;
import android.text.style.StyleSpan;
import android.view.MenuItem;
import android.view.View;

import br.com.zupfilms.R;
import br.com.zupfilms.ui.BaseActivity;
//import br.com.zupfilms.ui.home.fragments.favorite.FavoriteFragment;
import br.com.zupfilms.ui.home.fragments.favorite.FavoriteFragment;
import br.com.zupfilms.ui.home.fragments.home.HomeFragment;
import br.com.zupfilms.ui.home.fragments.search.SearchFragment;


public class HomeActivity extends BaseActivity
        implements BottomNavigationView.OnNavigationItemSelectedListener {

    private HomeActivityViewHolder homeActivityViewHolder;


    private final Fragment home = HomeFragment.newInstance();
    private final Fragment favorite = new FavoriteFragment();
    private final Fragment search = new SearchFragment();

    private Fragment getHome() {
        return home;
    }

    private Fragment getFavorite() {
        return favorite;
    }

    private Fragment getSearch() {
        return search;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        View view = this.getLayoutInflater().inflate(R.layout.activity_home, null);
        this.homeActivityViewHolder = new HomeActivityViewHolder(view);
        setContentView(view);

        setupListener();

        setSupportActionBar(homeActivityViewHolder.toolbar);

        SpannableString spannableString = new SpannableString("ZUP" + "FLIX");
        spannableString.setSpan(new StyleSpan(Typeface.BOLD), 0, 3, 0);
        homeActivityViewHolder.titleToolBar.setText(spannableString);

        if (savedInstanceState == null) {
            homeActivityViewHolder.bottomNavigationView.setSelectedItemId(R.id.navigation_home);
        }
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        colorStatusBar(this.getWindow());
    }

    private void setupListener() {
        homeActivityViewHolder.titleToolBar.setOnClickListener(backArrowListener);
        homeActivityViewHolder.bottomNavigationView
                .setOnNavigationItemSelectedListener(this);
    }

    private final View.OnClickListener backArrowListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if(homeActivityViewHolder.bottomNavigationView.getSelectedItemId() != R.id.navigation_home){
                homeActivityViewHolder.bottomNavigationView.setSelectedItemId(R.id.navigation_home);
            }
        }
    };

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.navigation_home:
                pushFragments(TAG_FRAGMENT_HOME, getHome());
                break;
            case R.id.navigation_favorite:
                pushFragments(TAG_FRAGMENT_FAVORITE, getFavorite());
                break;
            case R.id.navigation_search:
                pushFragments(TAG_FRAGMENT_SEARCH, getSearch());
                break;
        }
        return true;
    }
}
