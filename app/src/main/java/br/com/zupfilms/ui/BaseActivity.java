package br.com.zupfilms.ui;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ProgressBar;

import com.github.ybq.android.spinkit.sprite.Sprite;
import com.github.ybq.android.spinkit.style.ThreeBounce;

import br.com.zupfilms.R;

public abstract class BaseActivity extends AppCompatActivity {

    protected static final String TAG_FRAGMENT_HOME = "fragment_home";
    protected static final String TAG_FRAGMENT_FAVORITE = "fragment_favorite";
    protected static final String TAG_FRAGMENT_SEARCH = "fragment_search";


    public void hideKeyword(Window window){
        window.setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    }

    public void colorStatusBar(Window window, int color, Boolean isClearColor) {
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        View view = window.getDecorView();
        if(isClearColor){
            view.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        } else {
            view.setSystemUiVisibility(View.GONE);
        }
        window.setStatusBarColor(getColor(color));
    }

    public void hideKeyboardFrom(Context context, View view) {
        InputMethodManager inputMethodManager = (InputMethodManager) context.getSystemService(this.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    public void loadingExecutor(Boolean isLoading, ProgressBar progressBar, FrameLayout frameLayout, Button button) {
        if (isLoading != null) {
            if (isLoading) {
                getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                        WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                Sprite threeBounce = new ThreeBounce();
                progressBar.setIndeterminateDrawable(threeBounce);
                button.setVisibility(View.INVISIBLE);
                frameLayout.setVisibility(View.VISIBLE);
            } else {
                Sprite threeBounce = new ThreeBounce();
                progressBar.setIndeterminateDrawable(threeBounce);
                button.setVisibility(View.VISIBLE);
                frameLayout.setVisibility(View.INVISIBLE);
                getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
            }
        }
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

        if (tag == TAG_FRAGMENT_HOME) {
            if (fragmentHome != null) {
                ft.show(fragmentHome);
            }
        }
        if (tag == TAG_FRAGMENT_FAVORITE) {
            if (fragmentFavorite != null) {
                ft.show(fragmentFavorite);
            }
        }

        if (tag == TAG_FRAGMENT_SEARCH) {
            if (fragmentSearch != null) {
                ft.show(fragmentSearch);
            }
        }
        ft.commitAllowingStateLoss();
    }
}
