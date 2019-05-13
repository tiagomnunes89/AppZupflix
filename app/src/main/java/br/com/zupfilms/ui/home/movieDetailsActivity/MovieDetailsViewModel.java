package br.com.zupfilms.ui.home.movieDetailsActivity;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Observer;
import android.arch.paging.LivePagedListBuilder;
import android.arch.paging.PageKeyedDataSource;
import android.arch.paging.PagedList;
import android.support.annotation.Nullable;

import javax.inject.Inject;

import br.com.zupfilms.dagger.application.AppApplication;
import br.com.zupfilms.model.ErrorMessage;
import br.com.zupfilms.model.MovieDetailsModel;
import br.com.zupfilms.model.ResponseModel;
import br.com.zupfilms.server.repositories.SimilarMoviesRepository;
import br.com.zupfilms.server.response.FilmResponse;
import br.com.zupfilms.server.response.FilmsResults;
import br.com.zupfilms.ui.BaseViewModel;
import br.com.zupfilms.ui.home.adapters.SimilarMoviesDataSourceFactory;
import br.com.zupfilms.ui.singleton.SingletonFilmID;

public class MovieDetailsViewModel extends BaseViewModel {

    @Inject
    SimilarMoviesRepository similarMoviesRepository;

    public MovieDetailsViewModel() {
        AppApplication.getComponentRepositories().inject(this);
    }

    private LiveData<ResponseModel<MovieDetailsModel>> getMovieDetails;

    private final MutableLiveData<MovieDetailsModel> thereIsMovieDetails = new MutableLiveData<>();

    private final String SERVICE_OR_CONNECTION_ERROR = "Falha ao receber detalhes do filme. Verifique a conexão e tente novamente.";
    private final Integer INITIAL_LOAD_SIZE_HINT = 10;
    private final Integer PREFETCH_DISTANCE_VALUE = 10;
    private final Integer PAGE_SIZE = 10;

    private LiveData<PagedList<FilmResponse>> itemPagedList;
    private LiveData<PageKeyedDataSource<Integer, FilmResponse>> liveDataSource;
    private final MutableLiveData<Integer> receiverPageSizeService = new MutableLiveData<>();
    private final MutableLiveData<FilmsResults> activityTellerThereIsFilmResults = new MutableLiveData<>();
    private LiveData<ResponseModel<FilmsResults>> filmsResults;
    private final MutableLiveData<Boolean> similarMoviesListEmpty = new MutableLiveData<>();

    public MutableLiveData<Boolean> getSimilarMoviesListEmpty() {
        return similarMoviesListEmpty;
    }

    public LiveData<PagedList<FilmResponse>> getItemPagedList() {
        return itemPagedList;
    }

    public MutableLiveData<FilmsResults> getActivityTellerThereIsFilmResults() {
        return activityTellerThereIsFilmResults;
    }

    public MutableLiveData<MovieDetailsModel> getThereIsMovieDetails() {
        return thereIsMovieDetails;
    }


    public void executeServiceGetMovieDetails(int movieID) {
        getMovieDetails = filmRepository.getMovieDetails(movieID);
        getMovieDetails.observeForever(getMovieDetailsObserver);
    }

    private final Observer<ResponseModel<MovieDetailsModel>> getMovieDetailsObserver = movieDetails -> {
        if (movieDetails != null) {
            if (movieDetails.getCode() == SUCCESS_CODE) {
                thereIsMovieDetails.setValue(movieDetails.getResponse());
            }
        } else {
            isErrorMessageForToast.setValue(SERVICE_OR_CONNECTION_ERROR);
        }
    };

    private final Observer<Integer> receiverPageSizeServiceObserver = new Observer<Integer>() {
        @Override
        public void onChanged(Integer pageSize) {
            SimilarMoviesDataSourceFactory itemDataSourceFactory =
                    new SimilarMoviesDataSourceFactory(pageSize,
                            String.valueOf(SingletonFilmID.INSTANCE.getID()));
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
            if (responseModel.getCode() == SUCCESS_CODE) {
                receiverPageSizeService.setValue(responseModel.getResponse().getTotal_pages());
                activityTellerThereIsFilmResults.setValue(responseModel.getResponse());
                if (responseModel.getResponse().getTotal_results() == 0) {
                    similarMoviesListEmpty.setValue(true);
                } else {
                    similarMoviesListEmpty.setValue(false);
                }
            }
        } else {
            isErrorMessageForToast.setValue(SERVICE_OR_CONNECTION_ERROR);
        }
    };

    private void setupObserversForever() {
        similarMoviesRepository.getThereIsPaginationError().observeForever(thereIsPaginationErrorObserve);
        receiverPageSizeService.observeForever(receiverPageSizeServiceObserver);
    }

    public void executeServiceGetSimilarMovies(String page, Integer filmID) {
        isLoading.setValue(true);
        setupObserversForever();
        filmsResults = similarMoviesRepository.getSimilarMovies(page, String.valueOf(filmID));
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
        if (filmsResults != null && filmRepository.getThereIsPaginationError() != null) {
            filmsResults.removeObserver(filmsResultsObserver);
            filmRepository.getThereIsPaginationError().removeObserver(thereIsPaginationErrorObserve);
            receiverPageSizeService.removeObserver(receiverPageSizeServiceObserver);
        }
    }
}
