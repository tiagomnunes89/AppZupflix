package br.com.zupfilms.server.repositories;

import android.arch.paging.PageKeyedDataSource;
import android.support.annotation.NonNull;

import br.com.zupfilms.server.response.FilmResponse;


public class FilmDataSource extends PageKeyedDataSource<Integer, FilmResponse> {

    private int PAGE_SIZE;
    private String FILTER;
    private static final int FIRST_PAGE = 1;
    private String genreID;
    private FilmRepository filmRepository = new FilmRepository();
    private Thread requestDelay = new Thread();

    public FilmDataSource(int pageSize, String genreID, String filter) {
        this.PAGE_SIZE = pageSize;
        this.genreID = genreID;
        this.FILTER = filter;
    }

    @Override
    public void loadInitial(@NonNull LoadInitialParams<Integer> params, @NonNull final LoadInitialCallback<Integer, FilmResponse> callback) {
        try {
            requestDelay.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        filmRepository.getFilmsResultsLoadInitial(callback, String.valueOf(FIRST_PAGE), genreID);

    }

    @Override
    public void loadBefore(@NonNull final LoadParams<Integer> params, @NonNull final LoadCallback<Integer, FilmResponse> callback) {
        try {
            requestDelay.sleep(1500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        filmRepository.getFilmsResultsLoadBefore(params, callback, genreID);
    }

    @Override
    public void loadAfter(@NonNull final LoadParams<Integer> params, @NonNull final LoadCallback<Integer, FilmResponse> callback) {
        try {
            requestDelay.sleep(1500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        filmRepository.getFilmsResultsLoadAfter(PAGE_SIZE, params, callback, genreID);
    }
}
