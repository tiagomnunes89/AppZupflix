package br.com.zupfilms.server.repositories;

import androidx.paging.PageKeyedDataSource;
import androidx.annotation.NonNull;

import br.com.zupfilms.server.response.FilmResponse;


public class SearchDataSource extends PageKeyedDataSource<Integer, FilmResponse> {

    private final int PAGE_SIZE;
    private static final int FIRST_PAGE = 1;
    private final String query;
    private final SearchRepository searchRepository = new SearchRepository();

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
            Thread.sleep(1500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        searchRepository.getMovieSearchLoadBefore(params,callback,query);
    }

    @Override
    public void loadAfter(@NonNull final LoadParams<Integer> params, @NonNull final LoadCallback<Integer, FilmResponse> callback) {
        try {
            Thread.sleep(1500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        searchRepository.getMovieSearchLoadAfter(PAGE_SIZE,params,callback,query);
    }
}
