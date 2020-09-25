package br.com.zupfilms.ui.home.adapters;

import androidx.lifecycle.MutableLiveData;
import androidx.paging.DataSource;
import androidx.paging.PageKeyedDataSource;

import br.com.zupfilms.server.repositories.SearchDataSource;
import br.com.zupfilms.server.response.FilmResponse;

public class SearchDataSourceFactory extends DataSource.Factory {

    private final MutableLiveData<PageKeyedDataSource<Integer, FilmResponse>> itemLiveDataSource = new MutableLiveData<>();
    private final int pageSize;
    private final String ID;

    public SearchDataSourceFactory(Integer pageSize, String ID) {
        this.pageSize = pageSize;
        this.ID = ID;
    }

    @Override
    public DataSource create() {
        SearchDataSource searchDataSource = new SearchDataSource(pageSize, ID);
        itemLiveDataSource.postValue(searchDataSource);
        return searchDataSource;
    }

    public MutableLiveData<PageKeyedDataSource<Integer, FilmResponse>> getItemLiveDataSource() {
        return itemLiveDataSource;
    }
}