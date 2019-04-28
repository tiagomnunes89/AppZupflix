/*
package br.com.zupfilms.ui.home.fragments.favorite;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Observer;
import android.arch.paging.LivePagedListBuilder;
import android.arch.paging.PageKeyedDataSource;
import android.arch.paging.PagedList;
import android.support.annotation.Nullable;

import br.com.zupfilms.model.ErrorMessage;
import br.com.zupfilms.model.ResponseModel;
import br.com.zupfilms.server.repositories.FavoriteListRepository;
import br.com.zupfilms.server.response.FilmResponse;
import br.com.zupfilms.server.response.FilmsResults;
import br.com.zupfilms.ui.BaseViewModel;
import br.com.zupfilms.ui.home.adapters.FavoriteDataSourceFactory;

public class FavoriteViewModel extends BaseViewModel {

    private final static Integer INITIAL_LOAD_SIZE_HINT = 10;
    private final static Integer PREFETCH_DISTANCE_VALUE = 10;
    private final static Integer PAGE_SIZE = 5;
    private int ERROR_UNEXPECTED_CODE = 500;
    private String MESSAGE_ERROR_RECURRENT = "Erro inesperado ao receber filmes. Feche o aplicativo e tente novamente mais tarde";
    private FavoriteListRepository favoriteListRepository = new FavoriteListRepository();
    private final static String SERVICE_OR_CONNECTION_ERROR = "Falha ao receber filmes. Verifique a conexão e tente novamente.";
    private LiveData<PagedList<FilmResponse>> itemPagedList;
    private LiveData<PageKeyedDataSource<Integer, FilmResponse>> liveDataSource;
    private LiveData<ResponseModel<FilmsResults>> filmsResults;
    private MutableLiveData<Integer> receiverAPageSize = new MutableLiveData<>();
    private MutableLiveData<FilmsResults> fragmentTellerThereIsFilmResults = new MutableLiveData<>();
    private MutableLiveData<Boolean> isFavoriteListEmpty = new MutableLiveData<>();

    public MutableLiveData<Boolean> getIsFavoriteListEmpty() {
        return isFavoriteListEmpty;
    }

    public LiveData<PagedList<FilmResponse>> getItemPagedList() {
        return itemPagedList;
    }

    public MutableLiveData<FilmsResults> getFragmentTellerThereIsFilmResults() {
        return fragmentTellerThereIsFilmResults;
    }

    private Observer<Integer> receiverAPageSizeServiceObserver = new Observer<Integer>() {
        @Override
        public void onChanged(Integer pageSize) {

                FavoriteDataSourceFactory itemDataSourceFactory =
                        new FavoriteDataSourceFactory(pageSize,"email");
                liveDataSource = itemDataSourceFactory.getItemLiveDataSource();
                PagedList.Config config =
                        (new PagedList.Config.Builder())
                                .setEnablePlaceholders(false)
                                .setInitialLoadSizeHint(INITIAL_LOAD_SIZE_HINT)
                                .setPrefetchDistance(PREFETCH_DISTANCE_VALUE)
                                .setPageSize(PAGE_SIZE)
                                .build();

                itemPagedList = (new LivePagedListBuilder(itemDataSourceFactory, config)).build();
            }

    };

    private Observer<ResponseModel<FilmsResults>> filmsResultsObserver = new Observer<ResponseModel<FilmsResults>>() {
        @Override
        public void onChanged(@Nullable ResponseModel<FilmsResults> responseModel) {
            if (responseModel != null) {
                if (responseModel.getCode() == SUCCESS_CODE) {
                    if(responseModel.getResponse().getTotal_results() !=0) {
                    receiverAPageSize.setValue(responseModel.getResponse().getTotal_pages());
                    fragmentTellerThereIsFilmResults.setValue(responseModel.getResponse());
                    isFavoriteListEmpty.setValue(false);
                    } else {
                        isFavoriteListEmpty.setValue(true);
                    }
                }
            } else {
                isErrorMessageForToast.setValue(SERVICE_OR_CONNECTION_ERROR);
            }
        }
    };

    private void setupObserversForever() {
        favoriteListRepository.getThereIsPaginationError().observeForever(thereIsPaginationErrorObserve);
        receiverAPageSize.observeForever(receiverAPageSizeServiceObserver);
        favoriteListRepository.getThereIsError().observeForever(thereIsPaginationErrorObserve);
    }


    public void executeServiceGetSimilarMovies(String email) {
        isLoading.setValue(true);
        setupObserversForever();
        filmsResults = favoriteListRepository.getFavoriteList(email);
        filmsResults.observeForever(filmsResultsObserver);
    }

    private Observer<ErrorMessage> thereIsPaginationErrorObserve = new Observer<ErrorMessage>() {
        @Override
        public void onChanged(@Nullable ErrorMessage errorMessage) {
            if (errorMessage != null) {
                isErrorMessageForToast.setValue(errorMessage.getMessage());
            }
        }
    };

    @Override
    public void removeObserver() {
        super.removeObserver();
        if (filmsResults != null && favoriteListRepository.getThereIsPaginationError() != null
                && receiverAPageSize != null
                && favoriteListRepository.getViewModelTellerIsSessionExpiredPagination() != null
                && favoriteListRepository.getViewModelTellerIsSessionExpired() != null
        && favoriteListRepository.getThereIsError() !=null
                && getAddFavoriteFilm() != null
                && getRemoveFavoriteFilm() != null) {
            filmsResults.removeObserver(filmsResultsObserver);
            favoriteListRepository.getThereIsPaginationError().removeObserver(thereIsPaginationErrorObserve);
            receiverAPageSize.removeObserver(receiverAPageSizeServiceObserver);
            favoriteListRepository.getThereIsError().removeObserver(thereIsPaginationErrorObserve);
            getAddFavoriteFilm().removeObserver(addFavoriteFilmObserver);
            getRemoveFavoriteFilm().removeObserver(removeFavoriteFilmObserver);
        }
    }

}
*/
