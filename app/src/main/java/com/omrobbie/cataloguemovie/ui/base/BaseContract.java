package com.omrobbie.cataloguemovie.ui.base;

import android.support.annotation.StringRes;

public interface BaseContract {

    interface Presenter<V extends View> {

        void attachView(V mvpView);

        void detachView();

    }

    interface View {

        void showLoading(boolean isShow);

        void showToast(String message);

        void showToast(@StringRes int messageRes);

        void showError(Throwable error);

    }

}
