package com.omrobbie.cataloguemovie.injection.module;

import android.app.Activity;
import android.content.Context;

import com.omrobbie.cataloguemovie.injection.annotation.ActivityContext;

import dagger.Module;
import dagger.Provides;

@Module
public class ActivityModule {

    protected Activity activity;

    public ActivityModule(Activity activity) {
        this.activity = activity;
    }

    @Provides
    Activity provideActivity() {
        return activity;
    }

    @Provides
    @ActivityContext
    Context proviceActivityContext() {
        return activity;
    }

}
