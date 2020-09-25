package br.com.zupfilms.server.repositories;

import androidx.paging.PageKeyedDataSource;
import androidx.annotation.NonNull;

import br.com.zupfilms.server.response.FilmResponse;


public class FilmDataSource extends PageKeyedDataSource<Integer, FilmResponse> {

    private final int PAGE_SIZE;
    private static final int FIRST_PAGE = 1;
    private final String genreID;
    private final FilmRepository filmRepository = new FilmRepository();
    private final Thread requestDelay = new Thread();
    private final String FILTER;

    public FilmDataSource(int pageSize, String genreID, String filter) {
        this.PAGE_SIZE = pageSize;
        this.genreID = genreID;
        this.FILTER = filter;
    }

    @Override
    public void loadInitial(@NonNull LoadInitialParams<Integer> params, @NonNull final LoadInitialCallback<Integer, FilmResponse> callback) {
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        filmRepository.getFilmsResultsLoadInitial(callback, String.valueOf(FIRST_PAGE), genreID);

    }

    @Override
    public void loadBefore(@NonNull final LoadParams<Integer> params, @NonNull final LoadCallback<Integer, FilmResponse> callback) {
        try {
            Thread.sleep(1500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        filmRepository.getFilmsResultsLoadBefore(params, callback, genreID);
    }

    @Override
    public void loadAfter(@NonNull final LoadParams<Integer> params, @NonNull final LoadCallback<Integer, FilmResponse> callback) {
        try {
            Thread.sleep(1500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        filmRepository.getFilmsResultsLoadAfter(PAGE_SIZE, params, callback, genreID);
    }
}
