package com.omrobbie.cataloguemovie.utils;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

public class RxUtil {

    public static void dispose(Disposable disposable) {
        if (disposable != null && !disposable.isDisposed()) {
            disposable.dispose();
        }
    }

    public static void dispose(CompositeDisposable compositeDisposable){
        if (compositeDisposable != null && !compositeDisposable.isDisposed()){
            compositeDisposable.clear();
        }
    }
}
