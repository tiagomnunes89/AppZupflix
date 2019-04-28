/*
package br.com.zupfilms.server.repositories;

import android.arch.paging.PageKeyedDataSource;
import android.support.annotation.NonNull;

import br.com.zupfilms.server.response.FilmResponse;

public class FavoritesDataSource extends PageKeyedDataSource<Integer, FilmResponse> {

    private int PAGE_SIZE;
    private FavoriteListRepository favoriteListRepository = new FavoriteListRepository();
    private Thread requestDelay = new Thread();
    private String email;

    public FavoritesDataSource(int pageSize, String email) {
        this.PAGE_SIZE = pageSize;
        this.email = email;
    }

    @Override
    public void loadInitial(@NonNull LoadInitialParams<Integer> params, @NonNull final LoadInitialCallback<Integer, FilmResponse> callback) {
        favoriteListRepository.getFavoritesFilmsResultsLoadInitial(callback,email);
    }

    @Override
    public void loadBefore(@NonNull final LoadParams<Integer> params, @NonNull final LoadCallback<Integer, FilmResponse> callback) {
        try {
            requestDelay.sleep(1500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        favoriteListRepository.getMovieSearchLoadBefore(params,callback,email);
    }

    @Override
    public void loadAfter(@NonNull final LoadParams<Integer> params, @NonNull final LoadCallback<Integer, FilmResponse> callback) {
        try {
            requestDelay.sleep(1500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        favoriteListRepository.getMovieSearchLoadAfter(PAGE_SIZE,params,callback,email);
    }
}
*/
