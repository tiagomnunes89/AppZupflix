package br.com.zupfilms.ui.home.fragments.home;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sdsmdg.tastytoast.TastyToast;

import br.com.zupfilms.R;
import br.com.zupfilms.server.response.FilmGenres;
import br.com.zupfilms.ui.BaseFragment;
import br.com.zupfilms.ui.home.adapters.FragmentStateAdapter;
import br.com.zupfilms.ui.singleton.SingletonFilmGenres;

public class HomeFragment extends BaseFragment {

    private HomeFragmentViewHolder viewHolder;
    private HomeFragmentViewModel viewModelHome;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = this.getLayoutInflater().inflate(R.layout.fragment_home, container, false);
        this.viewHolder = new HomeFragmentViewHolder(view);

        viewModelHome = ViewModelProviders.of(this).get(HomeFragmentViewModel.class);

        if(verifyConnection()){
            viewHolder.textViewServiceDisable.setVisibility(View.GONE);
            viewModelHome.executeServiceGetGenreList();
            viewHolder.tabLayout.setVisibility(View.VISIBLE);
        } else {
            viewHolder.tabLayout.setVisibility(View.GONE);
            viewHolder.textViewServiceDisable.setVisibility(View.VISIBLE);
        }

        setupObserversAndListeners();

        return view;
    }

    private final View.OnClickListener textServiceDisable = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            viewHolder.textViewServiceDisable.setVisibility(View.GONE);
            if(verifyConnection()){
                viewHolder.textViewServiceDisable.setVisibility(View.GONE);
                viewModelHome.executeServiceGetGenreList();
                viewHolder.tabLayout.setVisibility(View.VISIBLE);
            } else {
                viewHolder.tabLayout.setVisibility(View.GONE);
                viewHolder.textViewServiceDisable.setVisibility(View.VISIBLE);
            }
        }
    };

    private void setupObserversAndListeners() {
        viewModelHome.getThereIsAGenreList().observe(this, genresObserver);
        viewModelHome.getIsLoading().observe(this,progressBarObserver);
        viewModelHome.getIsErrorMessageForToast().observe(this,isErrorMessageForToastObserver);
        viewHolder.textViewServiceDisable.setOnClickListener(textServiceDisable);
    }

    private final Observer<String> isErrorMessageForToastObserver = new Observer<String>() {
        @Override
        public void onChanged(String message) {
            TastyToast.makeText(getActivity(),message, TastyToast.LENGTH_LONG, TastyToast.ERROR)
                    .setGravity(Gravity.CENTER,0,700);
        }
    };

    private final Observer<FilmGenres> genresObserver = new Observer<FilmGenres>() {
        @Override
        public void onChanged(@Nullable FilmGenres filmGenres) {
            FragmentStatePagerAdapter fragmentStatePagerAdapter =
                    new FragmentStateAdapter(getFragmentManager(), filmGenres);

            SingletonFilmGenres.createFilmGenres(filmGenres);

            viewHolder.viewPager.setAdapter(fragmentStatePagerAdapter);

            viewHolder.tabLayout.setupWithViewPager(viewHolder.viewPager);
        }
    };

    private final Observer<Boolean> progressBarObserver = new Observer<Boolean>() {
        @Override
        public void onChanged(Boolean isLoading) {
            loadingExecutor(isLoading,
                    viewHolder.progressBar,
                    viewHolder.frameLayout);
        }
    };

    public static HomeFragment newInstance() {
        return new HomeFragment();
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        viewModelHome.removeObserver();
    }
}
