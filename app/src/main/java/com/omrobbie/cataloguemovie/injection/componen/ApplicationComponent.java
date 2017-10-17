package com.omrobbie.cataloguemovie.injection.componen;

import android.app.Application;
import android.content.Context;

import com.omrobbie.cataloguemovie.data.DataManager;
import com.omrobbie.cataloguemovie.injection.annotation.ApplicationContext;
import com.omrobbie.cataloguemovie.injection.module.ApplicationModule;
import com.omrobbie.cataloguemovie.injection.module.NetworkModule;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = {ApplicationModule.class, NetworkModule.class})
public interface ApplicationComponent {

    @ApplicationContext
    Context getContext(); // return context of application

    Application getApplication();

    DataManager getDataManager();

}
