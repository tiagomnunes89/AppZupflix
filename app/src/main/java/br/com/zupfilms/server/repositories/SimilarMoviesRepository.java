package br.com.zupfilms.server.repositories;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.paging.PageKeyedDataSource;

import br.com.zupfilms.model.ErrorMessage;
import br.com.zupfilms.model.ResponseModel;
import br.com.zupfilms.server.remote.FilmService;
import br.com.zupfilms.server.remote.RetrofitServiceBuilder;
import br.com.zupfilms.server.response.FilmResponse;
import br.com.zupfilms.server.response.FilmsResults;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SimilarMoviesRepository extends BaseRepository {

    private FilmService filmService;
    private int SUCCESS_CODE = 200;
    private String UNEXPECTED_ERROR_KEY = "erro.inesperado";
    private String UNEXPECTED_ERROR_MESSAGE = "Erro inesperado, tente novamente mais tarde!";
    private static final int FIRST_PAGE = 1;

    private MutableLiveData<ErrorMessage> thereIsPaginationError;


    public MutableLiveData<ErrorMessage> getThereIsPaginationError() {
        return thereIsPaginationError;
    }

    public SimilarMoviesRepository(){
        filmService = RetrofitServiceBuilder.buildService(FilmService.class);
        thereIsPaginationError = new MutableLiveData<>();
    }

    public LiveData<ResponseModel<FilmsResults>> getSimilarMovies(String page, String movieID) {
        final MutableLiveData<ResponseModel<FilmsResults>> data = new MutableLiveData<>();
        filmService.getSimilarMovies(movieID,"pt-BR",page)
                .enqueue(new Callback<FilmsResults>() {
                    @Override
                    public void onResponse(Call<FilmsResults> call, Response<FilmsResults> response) {
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
                    public void onFailure(Call<FilmsResults> call, Throwable t) {
                        data.setValue(null);
                    }
                });
        return data;
    }

    public void getSimilarMoviesLoadInitial (
            final PageKeyedDataSource.LoadInitialCallback<Integer, FilmResponse> callback, String movieID,
            String firstPage) {
        filmService.getSimilarMovies(movieID,"pt-BR",firstPage)
                .enqueue(new Callback<FilmsResults>() {
                    @Override
                    public void onResponse(Call<FilmsResults> call, Response<FilmsResults> response) {

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
                    public void onFailure(Call<FilmsResults> call, Throwable t) {
                        ErrorMessage errorMessage = new ErrorMessage();
                        errorMessage.setMessage(t.getMessage());
                        thereIsPaginationError.setValue(errorMessage);
                    }
                });
    }

    public void getSimilarMoviesLoadBefore (
            final PageKeyedDataSource.LoadParams<Integer> params, final PageKeyedDataSource.LoadCallback<Integer,
            FilmResponse> callback, String movieID) {
        filmService.getSimilarMovies(movieID, "pt-BR",String.valueOf(params.key))
                .enqueue(new Callback<FilmsResults>() {
                    @Override
                    public void onResponse(Call<FilmsResults> call, Response<FilmsResults> response) {

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
                    public void onFailure(Call<FilmsResults> call, Throwable t) {
                        ErrorMessage errorMessage = new ErrorMessage();
                        errorMessage.setMessage(t.getMessage());
                        thereIsPaginationError.setValue(errorMessage);
                    }
                });
    }

    public void getSimilarMoviesLoadAfter(
            final Integer PAGE_SIZE, final PageKeyedDataSource.LoadParams<Integer> params,
            final PageKeyedDataSource.LoadCallback<Integer, FilmResponse> callback, String movieID) {
        filmService.getSimilarMovies(movieID,"pt-BR",String.valueOf(params.key))
                .enqueue(new Callback<FilmsResults>() {
                    @Override
                    public void onResponse(Call<FilmsResults> call, Response<FilmsResults> response) {

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
                    public void onFailure(Call<FilmsResults> call, Throwable t) {
                        ErrorMessage errorMessage = new ErrorMessage();
                        errorMessage.setMessage(t.getMessage());
                        thereIsPaginationError.setValue(errorMessage);
                    }
                });
    }
}
