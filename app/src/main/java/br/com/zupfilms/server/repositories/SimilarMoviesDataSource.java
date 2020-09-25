package br.com.zupfilms.server.repositories;

import androidx.paging.PageKeyedDataSource;
import androidx.annotation.NonNull;

import br.com.zupfilms.server.response.FilmResponse;

public class SimilarMoviesDataSource extends PageKeyedDataSource<Integer, FilmResponse> {

    private final int PAGE_SIZE;
    private static final int FIRST_PAGE = 1;
    private final String movieID;
    private final SimilarMoviesRepository similarMoviesRepository = new SimilarMoviesRepository();

    public SimilarMoviesDataSource(int pageSize, String movieID) {
        this.PAGE_SIZE = pageSize;
        this.movieID = movieID;
    }

    @Override
    public void loadInitial(@NonNull PageKeyedDataSource.LoadInitialParams<Integer> params, @NonNull final PageKeyedDataSource.LoadInitialCallback<Integer, FilmResponse> callback) {
        similarMoviesRepository.getSimilarMoviesLoadInitial(callback, movieID,String.valueOf(FIRST_PAGE));
    }

    @Override
    public void loadBefore(@NonNull final PageKeyedDataSource.LoadParams<Integer> params, @NonNull final PageKeyedDataSource.LoadCallback<Integer, FilmResponse> callback) {
        try {
            Thread.sleep(1500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        similarMoviesRepository.getSimilarMoviesLoadBefore(params,callback, movieID);
    }

    @Override
    public void loadAfter(@NonNull final PageKeyedDataSource.LoadParams<Integer> params, @NonNull final PageKeyedDataSource.LoadCallback<Integer, FilmResponse> callback) {
        try {
            Thread.sleep(1500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        similarMoviesRepository.getSimilarMoviesLoadAfter(PAGE_SIZE,params,callback, movieID);
    }
}
