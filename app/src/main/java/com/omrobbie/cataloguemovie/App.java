package com.omrobbie.cataloguemovie;

import android.app.Application;
import android.content.Context;

import com.omrobbie.cataloguemovie.injection.componen.ApplicationComponent;
import com.omrobbie.cataloguemovie.injection.componen.DaggerApplicationComponent;
import com.omrobbie.cataloguemovie.injection.module.ApplicationModule;

public class App extends Application {

    private ApplicationComponent mAppComponent;

    @Override
    public void onCreate() {
        super.onCreate();

        // create application component
        if (mAppComponent == null) {
            mAppComponent = DaggerApplicationComponent.builder()
                    .applicationModule(new ApplicationModule(App.get(this)))
                    .build();
        }
    }

    public static App get(Context context){
        return (App) context.getApplicationContext();
    }

    public ApplicationComponent getComponent(){
        return mAppComponent;
    }

}
