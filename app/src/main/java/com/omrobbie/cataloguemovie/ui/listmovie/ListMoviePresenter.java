package com.omrobbie.cataloguemovie.ui.listmovie;

import android.util.Log;

import com.omrobbie.cataloguemovie.data.DataManager;
import com.omrobbie.cataloguemovie.data.model.search.ResultsItem;
import com.omrobbie.cataloguemovie.data.model.search.SearchModel;
import com.omrobbie.cataloguemovie.injection.annotation.ConfigPersistent;
import com.omrobbie.cataloguemovie.ui.base.BasePresenter;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;

@ConfigPersistent
public class ListMoviePresenter extends BasePresenter<ListMovieContract.View>
        implements ListMovieContract.Presenter {

    private static final String TAG = ListMoviePresenter.class.getSimpleName();
    private final DataManager mDataManager;
    private int mPageNumber = 1;
    private int mTotalPage;
    private List<ResultsItem> mListMovie;
    boolean isConfigChange = false;
    private int mTotalAvailableMovies;

    @Inject
    public ListMoviePresenter(DataManager mDataManager) {
        this.mDataManager = mDataManager;
        mListMovie = new ArrayList<>();
    }

    @Override
    public void getPopularMovie() {

        if (isConfigChange) {
            Log.d(TAG, "getPopularMovie: config change, restore " +
                     mListMovie.size() + " movies from configPersistent component...");

            getMvpView().showPopularMovie(mListMovie, mTotalAvailableMovies);
            isConfigChange = false;
            return;
        }

        if (mPageNumber < mTotalPage) {
            mPageNumber++;
        }

        Log.d(TAG, "getPopularMovie: page number " + String.valueOf(mPageNumber));
        Log.d(TAG, "getPopularMovie: field movies size: " + String.valueOf(mListMovie.size()));

        getMvpView().showLoading(true);
        Disposable disposable = mDataManager.getPopularMovie(mPageNumber)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doFinally(new Action() {
                    @Override
                    public void run() throws Exception {
                        getMvpView().showLoading(false);
                    }
                })
                .subscribeWith(new DisposableSingleObserver<SearchModel>() {
                    @Override
                    public void onSuccess(@NonNull SearchModel searchModel) {
                        List<ResultsItem> retrievedMovies = searchModel.getResults();
                        mListMovie.addAll(retrievedMovies);

                        mTotalAvailableMovies = searchModel.getTotalResults();
                        mTotalPage = searchModel.getTotalPages();
                        Log.d(TAG, "onSuccess: new retrievedMovies size= " + retrievedMovies.size());
                        Log.d(TAG, "onSuccess: member list size: " + String.valueOf(mListMovie.size()));
                        if (!retrievedMovies.isEmpty()) {
                            getMvpView().showPopularMovie(retrievedMovies, mTotalAvailableMovies);
                        } else {
                            Log.d(TAG, "onSuccess: no movie found");
                            getMvpView().showNoDataFound();
                        }
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        getMvpView().showError(e);
                    }
                });

        mDisposables.add(disposable);
    }

    @Override
    public void onPageRefresh() {
        mPageNumber = 1;
        mTotalPage = 0;
        mListMovie.clear();
        getPopularMovie();
    }

    @Override
    public void onConfigurationChange() {
        isConfigChange = true;
    }

}
