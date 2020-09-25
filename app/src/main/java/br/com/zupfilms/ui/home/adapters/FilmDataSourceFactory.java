package br.com.zupfilms.ui.home.adapters;

import androidx.lifecycle.MutableLiveData;
import androidx.paging.DataSource;
import androidx.paging.PageKeyedDataSource;

import br.com.zupfilms.server.repositories.FilmDataSource;
import br.com.zupfilms.server.response.FilmResponse;

public class FilmDataSourceFactory extends DataSource.Factory {

    private final MutableLiveData<PageKeyedDataSource<Integer, FilmResponse>> itemLiveDataSource = new MutableLiveData<>();
    private int pageSize;
    private String ID;
    private String FILTER;

    public FilmDataSourceFactory(Integer pageSize, String ID) {
        this.pageSize = pageSize;
        this.ID = ID;
    }

    @Override
    public DataSource create() {
        FilmDataSource filmDataSource = new FilmDataSource(pageSize, ID,FILTER);
        itemLiveDataSource.postValue(filmDataSource);
        return filmDataSource;
    }

    public MutableLiveData<PageKeyedDataSource<Integer, FilmResponse>> getItemLiveDataSource() {
        return itemLiveDataSource;
    }
}