package br.com.zupfilms.ui.home.fragments.search;

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
import br.com.zupfilms.server.repositories.SearchRepository;
import br.com.zupfilms.server.response.FilmResponse;
import br.com.zupfilms.server.response.FilmsResults;
import br.com.zupfilms.ui.BaseViewModel;
import br.com.zupfilms.ui.home.adapters.SearchDataSourceFactory;

public class SearchViewModel extends BaseViewModel {

    private final static String FIRST_PAGE = "1";
    private final Integer PAGE_SIZE = 10;
    private final Integer INITIAL_LOAD_SIZE_HINT = 5;
    private final Integer PREFETCH_DISTANCE_VALUE = 5;
    private final SearchRepository searchRepository = new SearchRepository();
    private final String SERVICE_OR_CONNECTION_ERROR = "Falha ao receber filmes. Verifique a conexão e tente novamente.";
    private LiveData<PagedList<FilmResponse>> itemPagedList;
    private LiveData<PageKeyedDataSource<Integer, FilmResponse>> liveDataSource;
    private LiveData<ResponseModel<FilmsResults>> filmsResults;
    private final MutableLiveData<FilterIDAndPageSize> receiverAPageSizeAndGenreIDService = new MutableLiveData<>();
    private final MutableLiveData<FilmsResults> fragmentTellerThereIsFilmResults = new MutableLiveData<>();
    private final MutableLiveData<Boolean> isSearchEmpty = new MutableLiveData<>();
    private String queryMovies;

    public MutableLiveData<Boolean> getIsSearchEmpty() {
        return isSearchEmpty;
    }

    public LiveData<PagedList<FilmResponse>> getItemPagedList() {
        return itemPagedList;
    }

    public MutableLiveData<FilmsResults> getFragmentTellerThereIsFilmResults() {
        return fragmentTellerThereIsFilmResults;
    }

    private final Observer<FilterIDAndPageSize> receiverAPageSizeAndGenreIDServiceObserver = new Observer<FilterIDAndPageSize>() {
        @Override
        public void onChanged(FilterIDAndPageSize filterIDAndPageSize) {
            SearchDataSourceFactory itemDataSourceFactory =
                    new SearchDataSourceFactory(filterIDAndPageSize.getPageSize(),
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

    public void executeServiceGetFilmResultsSearch(String queryMovies) {
        this.queryMovies = queryMovies;
        isLoading.setValue(true);

        setupObserversForever();

        if(queryMovies != null){
            filmsResults = searchRepository.getMovieSearch(FIRST_PAGE,queryMovies);
            filmsResults.observeForever(filmsResultsObserverSearch);
        }
    }

    private final Observer<ResponseModel<FilmsResults>> filmsResultsObserverSearch = new Observer<ResponseModel<FilmsResults>>() {
        @Override
        public void onChanged(@Nullable ResponseModel<FilmsResults> responseModel) {
            if (responseModel != null) {
                if (responseModel.getCode() == SUCCESS_CODE) {
                    if(responseModel.getResponse().getTotal_results() !=0 && SearchViewModel.this.queryMovies != null){
                        FilterIDAndPageSize filterIDAndPageSize = new FilterIDAndPageSize(responseModel.getResponse().getTotal_pages(),
                                SearchViewModel.this.queryMovies);
                        receiverAPageSizeAndGenreIDService.setValue(filterIDAndPageSize);
                        fragmentTellerThereIsFilmResults.setValue(responseModel.getResponse());
                        isSearchEmpty.setValue(false);
                    } else {
                        isSearchEmpty.setValue(true);
                    }
                }
            } else {
                isErrorMessageForToast.setValue(SERVICE_OR_CONNECTION_ERROR);
            }
        }
    };


    private void setupObserversForever(){
        searchRepository.getThereIsPaginationError().observeForever(thereIsPaginationErrorObserve);
        receiverAPageSizeAndGenreIDService.observeForever(receiverAPageSizeAndGenreIDServiceObserver);
    }
    
    private final Observer<ErrorMessage> thereIsPaginationErrorObserve = new Observer<ErrorMessage>() {
        @Override
        public void onChanged(@Nullable ErrorMessage errorMessage) {
            if(errorMessage != null){
                isErrorMessageForToast.setValue(errorMessage.getMessage());
            }
        }
    };

    @Override
    public void removeObserver() {
        super.removeObserver();
        if (filmsResults != null && searchRepository.getThereIsPaginationError() != null)  {
            filmsResults.removeObserver(filmsResultsObserverSearch);
            searchRepository.getThereIsPaginationError().removeObserver(thereIsPaginationErrorObserve);
            receiverAPageSizeAndGenreIDService.removeObserver(receiverAPageSizeAndGenreIDServiceObserver);
        }
    }
}
