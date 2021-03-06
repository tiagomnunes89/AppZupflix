package br.com.zupfilms.ui.home.fragments.movieList;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.paging.LivePagedListBuilder;
import androidx.paging.PageKeyedDataSource;
import androidx.paging.PagedList;
import androidx.annotation.Nullable;

import br.com.zupfilms.model.ErrorMessage;
import br.com.zupfilms.model.FilterIDAndPageSize;
import br.com.zupfilms.model.ResponseModel;
import br.com.zupfilms.server.response.FilmResponse;
import br.com.zupfilms.server.response.FilmsResults;
import br.com.zupfilms.ui.BaseViewModel;
import br.com.zupfilms.ui.home.adapters.FilmDataSourceFactory;

public class MovieListViewModel extends BaseViewModel {

    private final Integer INITIAL_LOAD_SIZE_HINT = 10;
    private final Integer PREFETCH_DISTANCE_VALUE = 10;
    private final Integer PAGE_SIZE = 10;
    private final String SERVICE_OR_CONNECTION_ERROR = "Falha ao receber filmes. Verifique a conexão e tente novamente.";
    private LiveData<PagedList<FilmResponse>> itemPagedList;
    private LiveData<PageKeyedDataSource<Integer, FilmResponse>> liveDataSource;
    private LiveData<ResponseModel<FilmsResults>> filmsResults;
    private final MutableLiveData<FilterIDAndPageSize> receiverAPageSizeAndGenreIDService = new MutableLiveData<>();
    private final MutableLiveData<FilmsResults> fragmentTellerThereIsFilmResults = new MutableLiveData<>();
    private String genreID;


    public LiveData<PagedList<FilmResponse>> getItemPagedList() {
        return itemPagedList;
    }

    public MutableLiveData<FilmsResults> getFragmentTellerThereIsFilmResults() {
        return fragmentTellerThereIsFilmResults;
    }

    private final Observer<FilterIDAndPageSize> receiverAPageSizeAndGenreIDServiceObserver = new Observer<FilterIDAndPageSize>() {
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

    private final Observer<ResponseModel<FilmsResults>> filmsResultsObserver = responseModel -> {
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

    private final Observer<ErrorMessage> thereIsPaginationErrorObserve = errorMessage -> {
        if (errorMessage != null) {
            isErrorMessageForToast.setValue(errorMessage.getMessage());
        }
    };

    @Override
    public void removeObserver() {
        super.removeObserver();
        if (filmsResults != null && filmRepository.getThereIsPaginationError() != null
                && receiverAPageSizeAndGenreIDService != null) {
            filmsResults.removeObserver(filmsResultsObserver);
            filmRepository.getThereIsPaginationError().removeObserver(thereIsPaginationErrorObserve);
            receiverAPageSizeAndGenreIDService.removeObserver(receiverAPageSizeAndGenreIDServiceObserver);
        }
    }
}