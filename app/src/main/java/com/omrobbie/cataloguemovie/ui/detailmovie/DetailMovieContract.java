package com.omrobbie.cataloguemovie.ui.detailmovie;

import com.omrobbie.cataloguemovie.data.model.detail.DetailModel;
import com.omrobbie.cataloguemovie.ui.base.BaseContract;

public interface DetailMovieContract {

    interface View extends BaseContract.View {

        void showDetailMovie(DetailModel detailMovie);

    }

    interface Presenter {

        void getDetailMovie(String movieId);

    }

}
