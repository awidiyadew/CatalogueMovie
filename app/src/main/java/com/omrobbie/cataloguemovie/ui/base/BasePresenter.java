package com.omrobbie.cataloguemovie.ui.base;

import com.omrobbie.cataloguemovie.utils.RxUtil;

import io.reactivex.disposables.CompositeDisposable;

public class BasePresenter<T extends BaseContract.View> implements BaseContract.Presenter<T> {

    private T mMvpView;
    protected CompositeDisposable mDisposables;

    public BasePresenter() {
        mDisposables = new CompositeDisposable();
    }

    @Override
    public void attachView(T mvpView) {
        mMvpView = mvpView;
    }

    @Override
    public void detachView() {
        RxUtil.dispose(mDisposables);
        mMvpView = null;
    }

    public boolean isViewAttached() {
        return mMvpView != null;
    }

    public void checkViewAttached(){
        if(!isViewAttached()) throw new MvpViewNotAttachedException();
    }

    public static class MvpViewNotAttachedException extends RuntimeException {
        public MvpViewNotAttachedException() {
            super("Please call Presenter.attachView(MvpView) before" +
                    " requesting data to the Presenter");
        }
    }

    public T getMvpView() {
        return mMvpView;
    }

}
