package br.com.zupfilms.ui.home.fragments.home;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.annotation.Nullable;

import br.com.zupfilms.model.ResponseModel;
import br.com.zupfilms.server.response.FilmGenres;
import br.com.zupfilms.ui.BaseViewModel;

public class HomeFragmentViewModel extends BaseViewModel {

    private LiveData<ResponseModel<FilmGenres>> getGenreList;

    private final MutableLiveData<FilmGenres> thereIsAGenreList = new MutableLiveData<>();


    public MutableLiveData<FilmGenres> getThereIsAGenreList() {
        return thereIsAGenreList;
    }

    public void executeServiceGetGenreList() {
        isLoading.setValue(true);
        getGenreList = filmRepository.getGenreList();
        getGenreList.observeForever(filmGenresObserver);
    }

    private final Observer<ResponseModel<FilmGenres>> filmGenresObserver = responseFilmGenres -> {
        isLoading.setValue(false);
        if (responseFilmGenres != null) {
            if (responseFilmGenres.getCode() == SUCCESS_CODE) {
                thereIsAGenreList.setValue(responseFilmGenres.getResponse());
            }
        } else {
            String SERVICE_OR_CONNECTION_ERROR = "Falha ao receber lista de gêneros. Verifique a conexão e tente novamente.";
            isErrorMessageForToast.setValue(SERVICE_OR_CONNECTION_ERROR);
        }
    };

    @Override
    public void removeObserver() {
        super.removeObserver();
        if (getGenreList != null && filmRepository.getThereIsPaginationError() != null)  {
            getGenreList.removeObserver(filmGenresObserver);
        }
    }
}
