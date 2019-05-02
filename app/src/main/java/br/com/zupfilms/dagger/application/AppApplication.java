package br.com.zupfilms.dagger.application;

import android.app.Application;

import br.com.zupfilms.dagger.component.ComponentRepositories;
import br.com.zupfilms.dagger.component.DaggerComponentRepositories;
import br.com.zupfilms.dagger.provide.RepositoriesModule;

public class AppApplication extends Application {

    private static ComponentRepositories componentRepositories;

    @Override
    public void onCreate() {
        super.onCreate();
        initDagger();
    }

    private void initDagger() {
        componentRepositories = DaggerComponentRepositories.builder()
                .repositoriesModule(new RepositoriesModule())
                .build();
    }

    public static ComponentRepositories getComponentRepositories() {
        return componentRepositories;
    }
}
