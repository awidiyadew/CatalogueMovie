package com.omrobbie.cataloguemovie.ui.detailmovie;

import android.util.Log;

import com.omrobbie.cataloguemovie.data.DataManager;
import com.omrobbie.cataloguemovie.data.model.detail.DetailModel;
import com.omrobbie.cataloguemovie.ui.base.BasePresenter;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;

public class DetailMoviePresenter extends BasePresenter<DetailMovieContract.View>
        implements DetailMovieContract.Presenter {

    private static final String TAG = DetailMoviePresenter.class.getSimpleName();
    private final DataManager mDataManager;

    @Inject
    public DetailMoviePresenter(DataManager mDataManager) {
        this.mDataManager = mDataManager;
    }

    @Override
    public void getDetailMovie(String movieId) {
        Log.d(TAG, "getDetailMovie: movie id: " + movieId);

        getMvpView().showLoading(true);
        Disposable disposable = mDataManager.getDetailMovie(movieId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doFinally(new Action() {
                    @Override
                    public void run() throws Exception {
                        getMvpView().showLoading(false);
                    }
                })
                .subscribeWith(new DisposableSingleObserver<DetailModel>() {
                    @Override
                    public void onSuccess(@NonNull DetailModel detailModel) {
                        Log.d(TAG, "onSuccess: detail movie load success ");
                        getMvpView().showDetailMovie(detailModel);
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        getMvpView().showError(e);
                    }
                });

        mDisposables.add(disposable);
    }

}
