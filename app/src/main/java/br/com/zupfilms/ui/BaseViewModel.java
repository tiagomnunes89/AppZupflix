package br.com.zupfilms.ui;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModel;
import android.support.annotation.Nullable;

import br.com.zupfilms.model.ResponseModel;
//import br.com.zupfilms.server.repositories.FavoriteListRepository;


public abstract class BaseViewModel extends ViewModel {

    protected int SUCCESS_CODE = 200;
    private String SUCCESS_MESSAGE_ADD = "Filme adicionado aos favoritos com sucesso";
    private String SUCCESS_MESSAGE_DELETE = "Filme removido dos favoritos com sucesso";
    private String SERVICE_OR_CONNECTION_ERROR_ADD = "Falha ao adicionar aos favoritos. Verifique a conexão e tente novamente.";
    private String MESSAGE_OTHER_ERROR_REMOVE = "Não foi possível remover esse filme. Verifique se já foi removido!";
    private String MESSAGE_OTHER_ERROR_ADD = "Não foi possível adicionar esse filme. Verifique se já foi adicionado!";
    private String SERVICE_OR_CONNECTION_ERROR_DELETE = "Falha ao remover dos favoritos. Verifique a conexão e tente novamente.";
    //protected FavoriteListRepository favoriteListRepository = new FavoriteListRepository();

    private LiveData<ResponseModel<Void>> addFavoriteFilm;

    private LiveData<ResponseModel<Void>> removeFavoriteFilm;

    public LiveData<ResponseModel<Void>> getAddFavoriteFilm() {
        return addFavoriteFilm;
    }

    public LiveData<ResponseModel<Void>> getRemoveFavoriteFilm() {
        return removeFavoriteFilm;
    }

    protected MutableLiveData<Boolean> isLoading = new MutableLiveData<>();

    protected MutableLiveData<String> isErrorMessageForToast = new MutableLiveData<>();

    protected MutableLiveData<String> forwardedToken = new MutableLiveData<>();

    protected MutableLiveData<String> isMessageSuccessForToast = new MutableLiveData<>();

    public MutableLiveData<String> getIsMessageSuccessForToast() {
        return isMessageSuccessForToast;
    }

    public MutableLiveData<String> getIsErrorMessageForToast() {
        return isErrorMessageForToast;
    }

    public MutableLiveData<Boolean> getIsLoading() {
        return isLoading;
    }

    public MutableLiveData<String> getForwardedToken() {
        return forwardedToken;
    }

/*    public void executeAddFavoriteFilm(String email, String movieID) {
        addFavoriteFilm = favoriteListRepository.addFavoriteFilm(email, movieID);
        addFavoriteFilm.observeForever(addFavoriteFilmObserver);
    }*/

    protected Observer<ResponseModel<Void>> addFavoriteFilmObserver = new Observer<ResponseModel<Void>>() {
        @Override
        public void onChanged(ResponseModel<Void> responseModel) {
            if (responseModel.getCode() == SUCCESS_CODE) {
                isMessageSuccessForToast.setValue(SUCCESS_MESSAGE_ADD);
            } else {
                isErrorMessageForToast.setValue(SERVICE_OR_CONNECTION_ERROR_ADD);
            }
        }
    };

/*    public void executeRemoveFavoriteFilm(String email, String movieID) {
        removeFavoriteFilm = favoriteListRepository.removeFavoriteFilm(email, movieID);
        removeFavoriteFilm.observeForever(removeFavoriteFilmObserver);
    }*/

    protected Observer<ResponseModel<Void>> removeFavoriteFilmObserver = new Observer<ResponseModel<Void>>() {
        @Override
        public void onChanged(@Nullable ResponseModel<Void> responseModel) {
            if (responseModel.getCode() == SUCCESS_CODE) {
                isMessageSuccessForToast.setValue(SUCCESS_MESSAGE_DELETE);
            } else {
                    isErrorMessageForToast.setValue(SERVICE_OR_CONNECTION_ERROR_DELETE);
                }
            }
    };

    public void removeObserver() {

    }
}