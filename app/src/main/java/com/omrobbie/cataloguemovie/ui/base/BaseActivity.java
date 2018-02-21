package com.omrobbie.cataloguemovie.ui.base;

import android.os.Bundle;
import android.support.annotation.StringRes;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.omrobbie.cataloguemovie.App;
import com.omrobbie.cataloguemovie.injection.componen.ActivityComponent;
import com.omrobbie.cataloguemovie.injection.componen.ConfigPersistentComponent;
import com.omrobbie.cataloguemovie.injection.componen.DaggerConfigPersistentComponent;
import com.omrobbie.cataloguemovie.injection.module.ActivityModule;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

public class BaseActivity extends AppCompatActivity implements BaseContract.View {

    private static final String TAG = BaseActivity.class.getSimpleName();
    private static final String KEY_ACTIVITY_ID = "KEY_ACTIVITY_ID";
    private static final AtomicLong NEXT_ID = new AtomicLong(0);
    private static final Map<Long, ConfigPersistentComponent> sComponentsMap = new HashMap<>();

    private ActivityComponent mActivityComponent;
    private long mActivityId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Create the ActivityComponent and reuses cached ConfigPersistentComponent if this is
        // being called after a configuration change.
        mActivityId = savedInstanceState != null ?
                savedInstanceState.getLong(KEY_ACTIVITY_ID) : NEXT_ID.getAndIncrement();
        ConfigPersistentComponent configPersistentComponent;
        if (!sComponentsMap.containsKey(mActivityId)) {
            Log.i(TAG, "Creating new ConfigPersistentComponent id= " + String.valueOf(mActivityId));
            configPersistentComponent = DaggerConfigPersistentComponent.builder()
                    .applicationComponent(App.get(this).getComponent())
                    .build();
            sComponentsMap.put(mActivityId, configPersistentComponent);
        } else {
            Log.i(TAG, "Reusing ConfigPersistentComponent id= " + String.valueOf(mActivityId));
            configPersistentComponent = sComponentsMap.get(mActivityId);
        }
        mActivityComponent = configPersistentComponent.activityComponent(new ActivityModule(this));
    }

    public ActivityComponent activityComponent() {
        return mActivityComponent;
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putLong(KEY_ACTIVITY_ID, mActivityId);
    }

    @Override
    protected void onDestroy() {
        if (!isChangingConfigurations()) {
            Log.i(TAG, "Clearing ConfigPersistentComponent id= " + String.valueOf(mActivityId));
            sComponentsMap.remove(mActivityId);
        }

        super.onDestroy();
    }

    @Override
    public void showLoading(boolean isShow) {

    }

    @Override
    public void showError(Throwable error) {
        Log.e(TAG, "showError: %s", error);
    }

    @Override
    public void showToast(String message) {

    }

    @Override
    public void showToast(@StringRes int messageRes) {

    }
}
