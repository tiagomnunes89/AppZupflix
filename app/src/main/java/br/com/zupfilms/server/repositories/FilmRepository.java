package br.com.zupfilms.server.repositories;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.paging.PageKeyedDataSource;
import android.support.annotation.NonNull;

import br.com.zupfilms.model.ErrorMessage;
import br.com.zupfilms.model.MovieDetailsModel;
import br.com.zupfilms.model.ResponseModel;
import br.com.zupfilms.server.remote.FilmService;
import br.com.zupfilms.server.remote.RetrofitServiceBuilder;
import br.com.zupfilms.server.response.FilmGenres;
import br.com.zupfilms.server.response.FilmResponse;
import br.com.zupfilms.server.response.FilmsResults;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FilmRepository extends BaseRepository {

    private final FilmService filmService;
    private final int SUCCESS_CODE = 200;
    private final String UNEXPECTED_ERROR_KEY = "erro.inesperado";
    private final String UNEXPECTED_ERROR_MESSAGE = "Erro inesperado, tente novamente mais tarde!";
    private static final int FIRST_PAGE = 1;

    private final MutableLiveData<ErrorMessage> thereIsPaginationError;


    public MutableLiveData<ErrorMessage> getThereIsPaginationError() {
        return thereIsPaginationError;
    }

    public FilmRepository(){
        filmService = RetrofitServiceBuilder.buildService(FilmService.class);
        thereIsPaginationError = new MutableLiveData<>();
    }

    public LiveData<ResponseModel<FilmGenres>> getGenreList() {
        final MutableLiveData<ResponseModel<FilmGenres>> data = new MutableLiveData<>();
        filmService.getGenres("pt-BR")
                .enqueue(new Callback<FilmGenres>() {
                    @Override
                    public void onResponse(@NonNull Call<FilmGenres> call, @NonNull Response<FilmGenres> response) {

                        ResponseModel<FilmGenres> responseModel = new ResponseModel<>();
                        if(response.code() == SUCCESS_CODE && response.body() != null){
                            responseModel.setCode(SUCCESS_CODE);
                            responseModel.setResponse(response.body());
                        } else {
                            if(response.errorBody() != null){
                                responseModel.setErrorMessage(serializeErrorBody(response.errorBody()));
                            } else {
                                ErrorMessage errorMessage = new ErrorMessage();
                                errorMessage.setKey(UNEXPECTED_ERROR_KEY);
                                errorMessage.setMessage(UNEXPECTED_ERROR_MESSAGE);
                                responseModel.setErrorMessage(errorMessage);
                            }
                        }
                        data.setValue(responseModel);
                    }

                    @Override
                    public void onFailure(@NonNull Call<FilmGenres> call, @NonNull Throwable t) {
                        data.setValue(null);
                    }
                });
        return data;
    }

    public LiveData<ResponseModel<MovieDetailsModel>> getMovieDetails(int id) {
        final MutableLiveData<ResponseModel<MovieDetailsModel>> data = new MutableLiveData<>();
        filmService.getMovieDetails(id,"pt-BR")
                .enqueue(new Callback<MovieDetailsModel>() {
                    @Override
                    public void onResponse(@NonNull Call<MovieDetailsModel> call, @NonNull Response<MovieDetailsModel> response) {

                        ResponseModel<MovieDetailsModel> responseModel = new ResponseModel<>();
                        if(response.code() == SUCCESS_CODE && response.body() != null){
                            responseModel.setCode(SUCCESS_CODE);
                            responseModel.setResponse(response.body());
                        } else {
                            if(response.errorBody() != null){
                                responseModel.setErrorMessage(serializeErrorBody(response.errorBody()));
                            } else {
                                ErrorMessage errorMessage = new ErrorMessage();
                                errorMessage.setKey(UNEXPECTED_ERROR_KEY);
                                errorMessage.setMessage(UNEXPECTED_ERROR_MESSAGE);
                                responseModel.setErrorMessage(errorMessage);
                            }
                        }
                        data.setValue(responseModel);
                    }

                    @Override
                    public void onFailure(@NonNull Call<MovieDetailsModel> call, @NonNull Throwable t) {
                        data.setValue(null);
                    }
                });
        return data;
    }

    public LiveData<ResponseModel<FilmsResults>> getFilmsResults(String page, String GenreID) {
        final MutableLiveData<ResponseModel<FilmsResults>> data = new MutableLiveData<>();
        filmService.getMovieGenre("pt-BR","popularity.desc",page,true,GenreID)
                .enqueue(new Callback<FilmsResults>() {
                    @Override
                    public void onResponse(@NonNull Call<FilmsResults> call, @NonNull Response<FilmsResults> response) {

                        ResponseModel<FilmsResults> responseModel = new ResponseModel<>();
                        if(response.code() == SUCCESS_CODE && response.body() != null){
                            responseModel.setCode(SUCCESS_CODE);
                            responseModel.setResponse(response.body());
                        } else {
                            if(response.errorBody() != null){
                                responseModel.setErrorMessage(serializeErrorBody(response.errorBody()));
                            } else {
                                ErrorMessage errorMessage = new ErrorMessage();
                                errorMessage.setKey(UNEXPECTED_ERROR_KEY);
                                errorMessage.setMessage(UNEXPECTED_ERROR_MESSAGE);
                                responseModel.setErrorMessage(errorMessage);
                            }
                        }
                        data.setValue(responseModel);
                    }

                    @Override
                    public void onFailure(@NonNull Call<FilmsResults> call, @NonNull Throwable t) {
                        data.setValue(null);
                    }
                });
        return data;
    }

    public void getFilmsResultsLoadInitial (
            final PageKeyedDataSource.LoadInitialCallback<Integer, FilmResponse> callback,
            String firstPage, String genreID) {
        filmService.getMovieGenre("pt-BR","popularity.desc",firstPage,true,genreID)
                .enqueue(new Callback<FilmsResults>() {
                    @Override
                    public void onResponse(@NonNull Call<FilmsResults> call, @NonNull Response<FilmsResults> response) {

                        if(response.code() == SUCCESS_CODE && response.body() != null){
                            callback.onResult(response.body().getResults(), null, FIRST_PAGE + 1);
                        } else {
                            if(response.errorBody() != null){
                                ErrorMessage errorMessage = serializeErrorBody(response.errorBody());
                                thereIsPaginationError.setValue(errorMessage);
                            } else {
                                ErrorMessage errorMessage = new ErrorMessage();
                                errorMessage.setKey(UNEXPECTED_ERROR_KEY);
                                errorMessage.setMessage(UNEXPECTED_ERROR_MESSAGE);
                                thereIsPaginationError.setValue(errorMessage);
                            }
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<FilmsResults> call, @NonNull Throwable t) {
                        ErrorMessage errorMessage = new ErrorMessage();
                        errorMessage.setMessage(t.getMessage());
                        thereIsPaginationError.setValue(errorMessage);
                    }
                });
    }

    public void getFilmsResultsLoadBefore (
            final PageKeyedDataSource.LoadParams<Integer> params, final PageKeyedDataSource.LoadCallback<Integer,
            FilmResponse> callback, String genreID) {
        filmService.getMovieGenre("pt-BR","popularity.desc",String.valueOf(params.key),true,genreID)
                .enqueue(new Callback<FilmsResults>() {
                    @Override
                    public void onResponse(@NonNull Call<FilmsResults> call, @NonNull Response<FilmsResults> response) {

                        if(response.code() == SUCCESS_CODE && response.body() != null){
                            Integer key = (params.key > 1) ? params.key - 1 : null;
                            callback.onResult(response.body().getResults(),key);
                        } else {
                            if(response.errorBody() != null){
                                ErrorMessage errorMessage = serializeErrorBody(response.errorBody());
                                thereIsPaginationError.setValue(errorMessage);
                            } else {
                                ErrorMessage errorMessage = new ErrorMessage();
                                errorMessage.setKey(UNEXPECTED_ERROR_KEY);
                                errorMessage.setMessage(UNEXPECTED_ERROR_MESSAGE);
                                thereIsPaginationError.setValue(errorMessage);
                            }
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<FilmsResults> call, @NonNull Throwable t) {
                        ErrorMessage errorMessage = new ErrorMessage();
                        errorMessage.setMessage(t.getMessage());
                        thereIsPaginationError.setValue(errorMessage);
                    }
                });
    }

    public void getFilmsResultsLoadAfter(
            final Integer PAGE_SIZE, final PageKeyedDataSource.LoadParams<Integer> params,
            final PageKeyedDataSource.LoadCallback<Integer, FilmResponse> callback, String genreID) {
        filmService.getMovieGenre("pt-BR","popularity.desc",String.valueOf(params.key),true,genreID)
                .enqueue(new Callback<FilmsResults>() {
                    @Override
                    public void onResponse(@NonNull Call<FilmsResults> call, @NonNull Response<FilmsResults> response) {

                        if(response.code() == SUCCESS_CODE && response.body() != null){
                            Integer key = (params.key < PAGE_SIZE)? params.key + 1 : null;
                            callback.onResult(response.body().getResults(), key);
                        } else {
                            if(response.errorBody() != null){
                                ErrorMessage errorMessage = serializeErrorBody(response.errorBody());
                                thereIsPaginationError.setValue(errorMessage);
                            } else {
                                ErrorMessage errorMessage = new ErrorMessage();
                                errorMessage.setKey(UNEXPECTED_ERROR_KEY);
                                errorMessage.setMessage(UNEXPECTED_ERROR_MESSAGE);
                                thereIsPaginationError.setValue(errorMessage);
                            }
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<FilmsResults> call, @NonNull Throwable t) {
                        ErrorMessage errorMessage = new ErrorMessage();
                        errorMessage.setMessage(t.getMessage());
                        thereIsPaginationError.setValue(errorMessage);
                    }
                });
    }
}
