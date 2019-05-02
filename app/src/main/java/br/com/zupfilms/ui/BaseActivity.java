package br.com.zupfilms.ui;

import android.content.Context;
import android.net.ConnectivityManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ProgressBar;

import com.github.ybq.android.spinkit.sprite.Sprite;
import com.github.ybq.android.spinkit.style.ThreeBounce;

import java.util.Objects;

import br.com.zupfilms.R;

public abstract class BaseActivity extends AppCompatActivity {

    protected static final String TAG_FRAGMENT_HOME = "fragment_home";
    protected static final String TAG_FRAGMENT_FAVORITE = "fragment_favorite";
    protected static final String TAG_FRAGMENT_SEARCH = "fragment_search";

    protected void colorStatusBar(Window window) {
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        View view = window.getDecorView();
        view.setSystemUiVisibility(View.GONE);
        window.setStatusBarColor(getColor(R.color.colorBlack));
    }

    protected void pushFragments(String tag, Fragment fragment) {

        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();

        if (getSupportFragmentManager().findFragmentByTag(tag) == null) {
            ft.add(R.id.content_home_drawer, fragment, tag);
        }

        Fragment fragmentHome = getSupportFragmentManager().findFragmentByTag(TAG_FRAGMENT_HOME);
        Fragment fragmentFavorite = getSupportFragmentManager().findFragmentByTag(TAG_FRAGMENT_FAVORITE);
        Fragment fragmentSearch = getSupportFragmentManager().findFragmentByTag(TAG_FRAGMENT_SEARCH);

        if (fragmentHome != null) {
            ft.hide(fragmentHome);
        }
        if (fragmentFavorite != null) {
            ft.hide(fragmentFavorite);
        }
        if (fragmentSearch != null) {
            ft.hide(fragmentSearch);
        }

        if (Objects.equals(tag, TAG_FRAGMENT_HOME)) {
            if (fragmentHome != null) {
                ft.show(fragmentHome);
            }
        }
        if (Objects.equals(tag, TAG_FRAGMENT_FAVORITE)) {
            if (fragmentFavorite != null) {
                ft.show(fragmentFavorite);
            }
        }

        if (Objects.equals(tag, TAG_FRAGMENT_SEARCH)) {
            if (fragmentSearch != null) {
                ft.show(fragmentSearch);
            }
        }
        ft.commitAllowingStateLoss();
    }

    protected boolean connectionVerifier() {
        boolean connected;
        ConnectivityManager connectivityManager = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        connected = connectivityManager.getActiveNetworkInfo() != null
                && connectivityManager.getActiveNetworkInfo().isAvailable()
                && connectivityManager.getActiveNetworkInfo().isConnected();
        return connected;
    }

    protected void loadingExecutor(Boolean isLoading, ProgressBar progressBar, FrameLayout frameLayout) {
        if (isLoading != null) {
            if (isLoading) {
                Sprite threeBounce = new ThreeBounce();
                progressBar.setIndeterminateDrawable(threeBounce);
                frameLayout.setVisibility(View.VISIBLE);
            } else {
                Sprite threeBounce = new ThreeBounce();
                progressBar.setIndeterminateDrawable(threeBounce);
                frameLayout.setVisibility(View.INVISIBLE);
            }
        }
    }
}
