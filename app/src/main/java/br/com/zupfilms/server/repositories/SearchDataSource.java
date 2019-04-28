package br.com.zupfilms.server.repositories;

import android.arch.paging.PageKeyedDataSource;
import android.support.annotation.NonNull;

import br.com.zupfilms.server.response.FilmResponse;


public class SearchDataSource extends PageKeyedDataSource<Integer, FilmResponse> {

    private int PAGE_SIZE;
    private static final int FIRST_PAGE = 1;
    private String query;
    private SearchRepository searchRepository = new SearchRepository();
    private Thread requestDelay = new Thread();

    public SearchDataSource(int pageSize, String query) {
        this.PAGE_SIZE = pageSize;
        this.query = query;
    }

    @Override
    public void loadInitial(@NonNull LoadInitialParams<Integer> params, @NonNull final LoadInitialCallback<Integer, FilmResponse> callback) {
        searchRepository.getMovieSearchLoadInitial(callback,query,String.valueOf(FIRST_PAGE));
    }

    @Override
    public void loadBefore(@NonNull final LoadParams<Integer> params, @NonNull final LoadCallback<Integer, FilmResponse> callback) {
        try {
            requestDelay.sleep(1500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        searchRepository.getMovieSearchLoadBefore(params,callback,query);
    }

    @Override
    public void loadAfter(@NonNull final LoadParams<Integer> params, @NonNull final LoadCallback<Integer, FilmResponse> callback) {
        try {
            requestDelay.sleep(1500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        searchRepository.getMovieSearchLoadAfter(PAGE_SIZE,params,callback,query);
    }
}
