package br.com.zupfilms.ui.home.adapters;

import android.arch.lifecycle.MutableLiveData;
import android.arch.paging.DataSource;
import android.arch.paging.PageKeyedDataSource;

import br.com.zupfilms.server.repositories.SimilarMoviesDataSource;
import br.com.zupfilms.server.response.FilmResponse;

public class SimilarMoviesDataSourceFactory extends DataSource.Factory {

    private MutableLiveData<PageKeyedDataSource<Integer, FilmResponse>> itemLiveDataSource = new MutableLiveData<>();
    private int pageSize;
    private String movieID;

    public SimilarMoviesDataSourceFactory(Integer pageSize, String movieID) {
        this.pageSize = pageSize;
        this.movieID = movieID;
    }

    public SimilarMoviesDataSourceFactory() {
    }

    @Override
    public DataSource create() {
        SimilarMoviesDataSource similarMoviesDataSource = new SimilarMoviesDataSource(pageSize, movieID);
        itemLiveDataSource.postValue(similarMoviesDataSource);
        return similarMoviesDataSource;
    }

    public MutableLiveData<PageKeyedDataSource<Integer, FilmResponse>> getItemLiveDataSource() {
        return itemLiveDataSource;
    }
}