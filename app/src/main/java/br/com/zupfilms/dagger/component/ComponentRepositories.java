package br.com.zupfilms.dagger.component;

import br.com.zupfilms.dagger.provide.RepositoriesModule;
import br.com.zupfilms.ui.BaseViewModel;
import br.com.zupfilms.ui.home.fragments.search.SearchViewModel;
import br.com.zupfilms.ui.home.movieDetailsActivity.MovieDetailsViewModel;
import dagger.Component;

@Component(modules = RepositoriesModule.class)
public interface ComponentRepositories {
    
    void inject(BaseViewModel baseViewModel);

    void inject(MovieDetailsViewModel movieDetailsViewModel);

    void inject(SearchViewModel searchViewModel);
}
