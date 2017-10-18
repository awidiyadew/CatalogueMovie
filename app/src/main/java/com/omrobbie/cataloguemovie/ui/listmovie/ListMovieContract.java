package com.omrobbie.cataloguemovie.ui.listmovie;

import com.omrobbie.cataloguemovie.data.model.search.ResultsItem;
import com.omrobbie.cataloguemovie.ui.base.BaseContract;

import java.util.List;

public interface ListMovieContract {

    interface View extends BaseContract.View {

        void showPopularMovie(List<ResultsItem> resultsItems, int totalResult);

        void showNoDataFound();

    }

    interface Presenter {

        void getPopularMovie();

        void onPageRefresh();

        void onConfigurationChange();

    }

}
