package br.com.zupfilms.ui.home.fragments.home;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Observer;
import android.support.annotation.Nullable;

import br.com.zupfilms.model.ResponseModel;
import br.com.zupfilms.server.repositories.FilmRepository;
import br.com.zupfilms.server.response.FilmGenres;
import br.com.zupfilms.server.response.GenresResponse;
import br.com.zupfilms.ui.BaseViewModel;

public class HomeFragmentViewModel extends BaseViewModel {

    private String SERVICE_OR_CONNECTION_ERROR = "Falha ao receber lista de gêneros. Verifique a conexão e tente novamente.";

    private FilmRepository filmRepository = new FilmRepository();

    private LiveData<ResponseModel<FilmGenres>> getGenreList;

    private MutableLiveData<FilmGenres> thereIsAGenreList = new MutableLiveData<>();


    public MutableLiveData<FilmGenres> getThereIsAGenreList() {
        return thereIsAGenreList;
    }

    public void executeServiceGetGenreList() {
        isLoading.setValue(true);
        getGenreList = filmRepository.getGenreList();
        getGenreList.observeForever(filmGenresObserver);
    }

    private Observer<ResponseModel<FilmGenres>> filmGenresObserver = new Observer<ResponseModel<FilmGenres>>() {
        @Override
        public void onChanged(@Nullable ResponseModel<FilmGenres> responseFilmGenres) {
            isLoading.setValue(false);
            if (responseFilmGenres != null) {
                if (responseFilmGenres.getCode() == SUCCESS_CODE) {
                    thereIsAGenreList.setValue(responseFilmGenres.getResponse());
                }
            } else {
                isErrorMessageForToast.setValue(SERVICE_OR_CONNECTION_ERROR);
            }
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
