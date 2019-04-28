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

public class SearchRepository extends BaseRepository{

    private FilmService filmService;
    private int SUCCESS_CODE = 200;
    private String UNEXPECTED_ERROR_KEY = "erro.inesperado";
    private String UNEXPECTED_ERROR_MESSAGE = "Erro inesperado, tente novamente mais tarde!";
    private static final int FIRST_PAGE = 1;

    private MutableLiveData<ErrorMessage> thereIsPaginationError;


    public MutableLiveData<ErrorMessage> getThereIsPaginationError() {
        return thereIsPaginationError;
    }

    public SearchRepository(){
        filmService = RetrofitServiceBuilder.buildService(FilmService.class);
        thereIsPaginationError = new MutableLiveData<>();
    }

    public LiveData<ResponseModel<FilmsResults>> getMovieSearch(String page, String query) {
        final MutableLiveData<ResponseModel<FilmsResults>> data = new MutableLiveData<>();
        filmService.getMovieSearch("pt-BR",query,page,true)
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

    public void getMovieSearchLoadInitial(
            final PageKeyedDataSource.LoadInitialCallback<Integer, FilmResponse> callback, String query,
            String firstPage) {
        filmService.getMovieSearch("pt-BR",query,firstPage,true)
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

    public void getMovieSearchLoadBefore(
            final PageKeyedDataSource.LoadParams<Integer> params, final PageKeyedDataSource.LoadCallback<Integer,
            FilmResponse> callback, String query) {
        filmService.getMovieSearch("pt-BR",query,String.valueOf(params.key),true)
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

    public void getMovieSearchLoadAfter(
            final Integer PAGE_SIZE, final PageKeyedDataSource.LoadParams<Integer> params,
            final PageKeyedDataSource.LoadCallback<Integer, FilmResponse> callback, String query) {
        filmService.getMovieSearch("pt-BR",query,String.valueOf(params.key),true)
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
