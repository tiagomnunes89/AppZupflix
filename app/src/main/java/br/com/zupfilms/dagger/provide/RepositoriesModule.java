package br.com.zupfilms.dagger.provide;

import android.content.Context;

import br.com.zupfilms.data.DB;
import br.com.zupfilms.server.repositories.FilmRepository;
import br.com.zupfilms.server.repositories.SearchRepository;
import br.com.zupfilms.server.repositories.SimilarMoviesRepository;
import dagger.Module;
import dagger.Provides;

@Module
public class RepositoriesModule {

    public RepositoriesModule() {
    }

    @Provides
    FilmRepository provideFilmRepository() {
        return new FilmRepository();
    }

    @Provides
    SearchRepository provideSearchRepository() {
        return new SearchRepository();
    }

    @Provides
    SimilarMoviesRepository provideSimilarMoviesRepository() {
        return new SimilarMoviesRepository();
    }

    @Provides
    DB provideDataBase(Context context) {
        return new DB(context);
    }
}