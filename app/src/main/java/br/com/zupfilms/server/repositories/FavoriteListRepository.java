/*
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

public class FavoriteListRepository extends BaseRepository {

    private static final Integer FIRST_PAGE = 1;
    private static final String AMOUNT = "5";
    private FilmService filmService;
    private int OTHER_ERROR_CODE = 500;
    private MutableLiveData<ErrorMessage> thereIsError;
    private MutableLiveData<Boolean> viewModelTellerIsSessionExpired;
    private MutableLiveData<ErrorMessage> thereIsPaginationError;
    private MutableLiveData<Boolean> viewModelTellerIsSessionExpiredPagination;

    public MutableLiveData<Boolean> getViewModelTellerIsSessionExpiredPagination() {
        return viewModelTellerIsSessionExpiredPagination;
    }

    public MutableLiveData<ErrorMessage> getThereIsPaginationError() {
        return thereIsPaginationError;
    }


    public FavoriteListRepository() {
        filmService = RetrofitServiceBuilder.buildService(FilmService.class);
        thereIsError = new MutableLiveData<>();
        viewModelTellerIsSessionExpired = new MutableLiveData<>();
        thereIsPaginationError = new MutableLiveData<>();
        viewModelTellerIsSessionExpiredPagination = new MutableLiveData<>();
    }

    public MutableLiveData<Boolean> getViewModelTellerIsSessionExpired() {
        return viewModelTellerIsSessionExpired;
    }

    public MutableLiveData<ErrorMessage> getThereIsError() {
        return thereIsError;
    }

    public LiveData<ResponseModel<FilmsResults>> getFavoriteList(String email) {
        final MutableLiveData<ResponseModel<FilmsResults>> data = new MutableLiveData<>();
        filmService.getFavoriteList(email, FIRST_PAGE, AMOUNT)
                .enqueue(new Callback<FilmsResults>() {
                    @Override
                    public void onResponse(Call<FilmsResults> call, Response<FilmsResults> response) {

                        ResponseModel<FilmsResults> responseModel = new ResponseModel<>();
                        if (response.code() == SUCCESS_CODE && response.body() != null) {
                            responseModel.setCode(SUCCESS_CODE);
                            responseModel.setResponse(response.body());
                        } else if (response.code() == SESSION_EXPIRED_CODE) {
                            viewModelTellerIsSessionExpired.postValue(true);
                        } else {
                            if (response.errorBody() != null) {
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

    public LiveData<ResponseModel<Void>> addFavoriteFilm(String email, String movieID) {
        final MutableLiveData<ResponseModel<Void>> data = new MutableLiveData<>();
        filmService.addFavotiteFilm(email, movieID)
                .enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {
                        ResponseModel<Void> responseModel = new ResponseModel<>();
                        if (response.code() == SUCCESS_CODE) {
                            responseModel.setCode(SUCCESS_CODE);
                        } else if (response.code() == OTHER_ERROR_CODE) {
                            responseModel.setCode(OTHER_ERROR_CODE);
                        } else if (response.code() == SESSION_EXPIRED_CODE) {
                            viewModelTellerIsSessionExpired.postValue(true);
                        } else {
                            if (response.errorBody() != null) {
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
                    public void onFailure(Call<Void> call, Throwable t) {
                        data.setValue(null);
                    }
                });
        return data;
    }

    public LiveData<ResponseModel<Void>> removeFavoriteFilm(String email, String movieID) {
        final MutableLiveData<ResponseModel<Void>> data = new MutableLiveData<>();
        filmService.removeFavotiteFilm(email, movieID)
                .enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {

                        ResponseModel<Void> responseModel = new ResponseModel<>();
                        if (response.code() == SUCCESS_CODE) {
                            responseModel.setCode(SUCCESS_CODE);
                        } else if (response.code() == OTHER_ERROR_CODE) {
                            responseModel.setCode(OTHER_ERROR_CODE);
                        } else if (response.code() == SESSION_EXPIRED_CODE) {
                            viewModelTellerIsSessionExpired.postValue(true);
                        } else {
                            if (response.errorBody() != null) {
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
                    public void onFailure(Call<Void> call, Throwable t) {
                        data.setValue(null);
                    }
                });
        return data;
    }

    public void getFavoritesFilmsResultsLoadInitial(
            final PageKeyedDataSource.LoadInitialCallback<Integer, FilmResponse> callback,
            String email) {
        filmService.getFavoriteList(email, FIRST_PAGE, AMOUNT)
                .enqueue(new Callback<FilmsResults>() {
                    @Override
                    public void onResponse(Call<FilmsResults> call, Response<FilmsResults> response) {
                        if (response.code() == SUCCESS_CODE && response.body() != null) {
                            callback.onResult(response.body().getResults(), null, FIRST_PAGE + 1);
                        } else if (response.code() == SESSION_EXPIRED_CODE) {
                            viewModelTellerIsSessionExpiredPagination.postValue(true);
                        } else {
                            if (response.errorBody() != null) {
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
            FilmResponse> callback, String email) {
        filmService.getFavoriteList(email, params.key, AMOUNT)
                .enqueue(new Callback<FilmsResults>() {
                    @Override
                    public void onResponse(Call<FilmsResults> call, Response<FilmsResults> response) {
                        if (response.code() == SUCCESS_CODE && response.body() != null) {
                            Integer key = (params.key > 1) ? params.key - 1 : null;
                            callback.onResult(response.body().getResults(), key);
                        } else if (response.code() == SESSION_EXPIRED_CODE) {
                            viewModelTellerIsSessionExpiredPagination.postValue(true);
                        } else {
                            if (response.errorBody() != null) {
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
            final PageKeyedDataSource.LoadCallback<Integer, FilmResponse> callback, String email) {
        filmService.getFavoriteList(email, params.key, AMOUNT)
                .enqueue(new Callback<FilmsResults>() {
                    @Override
                    public void onResponse(Call<FilmsResults> call, Response<FilmsResults> response) {
                        if (response.code() == SUCCESS_CODE && response.body() != null) {
                            Integer key = (params.key < PAGE_SIZE) ? params.key + 1 : null;
                            callback.onResult(response.body().getResults(), key);
                        } else if (response.code() == SESSION_EXPIRED_CODE) {
                            viewModelTellerIsSessionExpiredPagination.postValue(true);
                        } else {
                            if (response.errorBody() != null) {
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
*/
