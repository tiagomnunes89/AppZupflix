/*
package br.com.zupfilms.ui.home.adapters;

import android.arch.lifecycle.MutableLiveData;
import android.arch.paging.DataSource;
import android.arch.paging.PageKeyedDataSource;

import br.com.zupfilms.server.repositories.FavoritesDataSource;
import br.com.zupfilms.server.response.FilmResponse;

public class FavoriteDataSourceFactory extends DataSource.Factory {

    private MutableLiveData<PageKeyedDataSource<Integer, FilmResponse>> itemLiveDataSource = new MutableLiveData<>();
    private int pageSize;
    private String email;

    public FavoriteDataSourceFactory(Integer pageSize, String email) {
        this.pageSize = pageSize;
        this.email = email;
    }

    public FavoriteDataSourceFactory() {
    }

    @Override
    public DataSource create() {
        FavoritesDataSource favoritesDataSource = new FavoritesDataSource(pageSize, email);
        itemLiveDataSource.postValue(favoritesDataSource);
        return favoritesDataSource;
    }

    public MutableLiveData<PageKeyedDataSource<Integer, FilmResponse>> getItemLiveDataSource() {
        return itemLiveDataSource;
    }
}*/
