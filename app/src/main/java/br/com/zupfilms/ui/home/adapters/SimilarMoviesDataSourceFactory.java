package br.com.zupfilms.ui.home.adapters;

import androidx.lifecycle.MutableLiveData;
import androidx.paging.DataSource;
import androidx.paging.PageKeyedDataSource;

import br.com.zupfilms.server.repositories.SimilarMoviesDataSource;
import br.com.zupfilms.server.response.FilmResponse;

public class SimilarMoviesDataSourceFactory extends DataSource.Factory {

    private final MutableLiveData<PageKeyedDataSource<Integer, FilmResponse>> itemLiveDataSource = new MutableLiveData<>();
    private final int pageSize;
    private final String movieID;

    public SimilarMoviesDataSourceFactory(Integer pageSize, String movieID) {
        this.pageSize = pageSize;
        this.movieID = movieID;
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