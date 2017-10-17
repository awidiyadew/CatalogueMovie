package com.omrobbie.cataloguemovie.injection.componen;

import android.app.Activity;
import android.content.Context;

import com.omrobbie.cataloguemovie.injection.annotation.ActivityContext;
import com.omrobbie.cataloguemovie.injection.annotation.PerActivity;
import com.omrobbie.cataloguemovie.injection.module.ActivityModule;

import dagger.Subcomponent;

/**
 * This component inject dependencies to all Activities across the application
 */
@PerActivity
@Subcomponent(modules = ActivityModule.class)
public interface ActivityComponent {

    Activity getActivity();

    @ActivityContext
    Context getContext();

}
