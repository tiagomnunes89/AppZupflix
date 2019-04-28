package br.com.zupfilms.ui.home.fragments.movieList;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Observer;
import android.arch.paging.LivePagedListBuilder;
import android.arch.paging.PageKeyedDataSource;
import android.arch.paging.PagedList;
import android.support.annotation.Nullable;

import br.com.zupfilms.model.ErrorMessage;
import br.com.zupfilms.model.FilterIDAndPageSize;
import br.com.zupfilms.model.ResponseModel;
import br.com.zupfilms.server.repositories.FilmRepository;
import br.com.zupfilms.server.response.FilmResponse;
import br.com.zupfilms.server.response.FilmsResults;
import br.com.zupfilms.ui.BaseViewModel;
import br.com.zupfilms.ui.home.adapters.FilmDataSourceFactory;

//import br.com.zupfilms.server.repositories.FavoriteListRepository;

public class MovieListViewModel extends BaseViewModel {

    private Integer INITIAL_LOAD_SIZE_HINT = 10;
    private Integer PREFETCH_DISTANCE_VALUE = 10;
    private Integer PAGE_SIZE = 10;
    private String MESSAGE_ERROR_RECURRENT = "Erro inesperado ao receber filmes. Feche o aplicativo e tente novamente mais tarde";
    private FilmRepository filmRepository = new FilmRepository();
    //private FavoriteListRepository favoriteListRepository = new FavoriteListRepository();
    private String SERVICE_OR_CONNECTION_ERROR = "Falha ao receber filmes. Verifique a conexão e tente novamente.";
    private LiveData<PagedList<FilmResponse>> itemPagedList;
    private LiveData<PageKeyedDataSource<Integer, FilmResponse>> liveDataSource;
    private LiveData<ResponseModel<FilmsResults>> filmsResults;
    private MutableLiveData<FilterIDAndPageSize> receiverAPageSizeAndGenreIDService = new MutableLiveData<>();
    private MutableLiveData<FilmsResults> fragmentTellerThereIsFilmResults = new MutableLiveData<>();
    private String genreID;

    public LiveData<PagedList<FilmResponse>> getItemPagedList() {
        return itemPagedList;
    }

    public MutableLiveData<FilmsResults> getFragmentTellerThereIsFilmResults() {
        return fragmentTellerThereIsFilmResults;
    }

    private Observer<FilterIDAndPageSize> receiverAPageSizeAndGenreIDServiceObserver = new Observer<FilterIDAndPageSize>() {
        @Override
        public void onChanged(FilterIDAndPageSize filterIDAndPageSize) {
            FilmDataSourceFactory itemDataSourceFactory =
                    new FilmDataSourceFactory(filterIDAndPageSize.getPageSize(),
                            filterIDAndPageSize.getFilterID());
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
                if (responseModel.getCode() == SUCCESS_CODE && responseModel.getResponse().getTotal_pages() != null && MovieListViewModel.this.genreID != null) {
                    FilterIDAndPageSize filterIDAndPageSize = new FilterIDAndPageSize(responseModel.getResponse().getTotal_pages(),
                            MovieListViewModel.this.genreID);
                    receiverAPageSizeAndGenreIDService.setValue(filterIDAndPageSize);
                    fragmentTellerThereIsFilmResults.setValue(responseModel.getResponse());
                }
            } else {
                isErrorMessageForToast.setValue(SERVICE_OR_CONNECTION_ERROR);
            }
        }
    };

    private void setupObserversForever() {
        filmRepository.getThereIsPaginationError().observeForever(thereIsPaginationErrorObserve);
        receiverAPageSizeAndGenreIDService.observeForever(receiverAPageSizeAndGenreIDServiceObserver);
    }


    public void executeServiceGetFilmResults(String page, String genreID) {
        isLoading.setValue(true);
        this.genreID = genreID;
        setupObserversForever();
        filmsResults = filmRepository.getFilmsResults(page,genreID);
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
        if (filmsResults != null && filmRepository.getThereIsPaginationError() != null
                && receiverAPageSizeAndGenreIDService != null
                && getAddFavoriteFilm() != null
                && getRemoveFavoriteFilm() != null) {
            filmsResults.removeObserver(filmsResultsObserver);
            filmRepository.getThereIsPaginationError().removeObserver(thereIsPaginationErrorObserve);
            receiverAPageSizeAndGenreIDService.removeObserver(receiverAPageSizeAndGenreIDServiceObserver);
            getAddFavoriteFilm().removeObserver(addFavoriteFilmObserver);
            getRemoveFavoriteFilm().removeObserver(removeFavoriteFilmObserver);
        }
    }
}