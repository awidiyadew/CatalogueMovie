package com.omrobbie.cataloguemovie.injection.module;

import android.app.Application;
import android.content.Context;

import com.omrobbie.cataloguemovie.injection.annotation.ApplicationContext;

import dagger.Module;
import dagger.Provides;

@Module
public class ApplicationModule {

    protected Application application;

    public ApplicationModule(Application application) {
        this.application = application;
    }

    @Provides
    Application provideApplication() {
        return application;
    }

    @Provides
    @ApplicationContext
    Context provideAppContext() {
        return application;
    }

}
