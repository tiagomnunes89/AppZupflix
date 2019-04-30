package br.com.zupfilms.ui;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModel;
import android.support.annotation.Nullable;

import br.com.zupfilms.model.MovieDetailsModel;
import br.com.zupfilms.model.ResponseModel;
import br.com.zupfilms.server.repositories.FilmRepository;


public abstract class BaseViewModel extends ViewModel {

    protected final int SUCCESS_CODE = 200;
    private final FilmRepository filmRepository = new FilmRepository();

    protected final MutableLiveData<Boolean> isLoading = new MutableLiveData<>();

    protected final MutableLiveData<String> isErrorMessageForToast = new MutableLiveData<>();

    private final MutableLiveData<String> isMessageSuccessForToast = new MutableLiveData<>();

    public MutableLiveData<String> getIsMessageSuccessForToast() {
        return isMessageSuccessForToast;
    }

    public MutableLiveData<String> getIsErrorMessageForToast() {
        return isErrorMessageForToast;
    }

    public MutableLiveData<Boolean> getIsLoading() {
        return isLoading;
    }

    private final MutableLiveData<MovieDetailsModel> thereAreMovieDetailsToSaveOffline = new MutableLiveData<>();

    public MutableLiveData<MovieDetailsModel> getThereAreMovieDetailsToSaveOffline() {
        return thereAreMovieDetailsToSaveOffline;
    }

    public void executeServiceGetMovieDetailsToSaveOffline(int movieID) {
        LiveData<ResponseModel<MovieDetailsModel>> getMovieDetails = filmRepository.getMovieDetails(movieID);
        getMovieDetails.observeForever(getMovieDetailsToSaveOfflineObserver);
    }

    private final Observer<ResponseModel<MovieDetailsModel>> getMovieDetailsToSaveOfflineObserver = new Observer<ResponseModel<MovieDetailsModel>>() {
        @Override
        public void onChanged(@Nullable ResponseModel<MovieDetailsModel> movieDetails) {
            if (movieDetails != null) {
                if (movieDetails.getCode() == SUCCESS_CODE) {
                    thereAreMovieDetailsToSaveOffline.setValue(movieDetails.getResponse());
                }
            } else {
                String SERVICE_OR_CONNECTION_ERROR_MOVIE_DETAILS = "Falha ao receber detalhes do filme. Verifique a conexão e tente novamente.";
                isErrorMessageForToast.setValue(SERVICE_OR_CONNECTION_ERROR_MOVIE_DETAILS);
            }
        }
    };

    public void removeObserver() {

    }
}