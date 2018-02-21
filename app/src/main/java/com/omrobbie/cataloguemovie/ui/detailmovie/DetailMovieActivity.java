package com.omrobbie.cataloguemovie.ui.detailmovie;

import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.omrobbie.cataloguemovie.BuildConfig;
import com.omrobbie.cataloguemovie.R;
import com.omrobbie.cataloguemovie.data.model.detail.DetailModel;
import com.omrobbie.cataloguemovie.data.model.search.ResultsItem;
import com.omrobbie.cataloguemovie.ui.base.BaseActivity;
import com.omrobbie.cataloguemovie.utils.DateTime;

import java.text.NumberFormat;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.BindViews;
import butterknife.ButterKnife;

public class DetailMovieActivity extends BaseActivity
        implements DetailMovieContract.View {

    public static final String MOVIE_ITEM = "movie_item";

    @BindView(R.id.collapsing_toolbar)
    CollapsingToolbarLayout collapsing_toolbar;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.tv_title)
    TextView tv_title;

    @BindView(R.id.img_backdrop)
    ImageView img_backdrop;

    @BindView(R.id.img_poster)
    ImageView img_poster;

    @BindView(R.id.tv_release_date)
    TextView tv_release_date;

    @BindView(R.id.tv_vote)
    TextView tv_vote;

    @BindViews({
            R.id.img_star1,
            R.id.img_star2,
            R.id.img_star3,
            R.id.img_star4,
            R.id.img_star5
    })
    List<ImageView> img_vote;

    @BindView(R.id.tv_genres)
    TextView tv_genres;

    @BindView(R.id.tv_overview)
    TextView tv_overview;

    @BindView(R.id.img_poster_belongs)
    ImageView img_poster_belongs;

    @BindView(R.id.tv_title_belongs)
    TextView tv_title_belongs;

    @BindView(R.id.tv_budget)
    TextView tv_budget;

    @BindView(R.id.tv_revenue)
    TextView tv_revenue;

    @BindView(R.id.tv_companies)
    TextView tv_companies;

    @BindView(R.id.tv_countries)
    TextView tv_countries;

    private Gson gson = new Gson();

    @Inject DetailMoviePresenter mPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        collapsing_toolbar.setExpandedTitleColor(getResources().getColor(android.R.color.transparent));

        activityComponent().inject(this);
        mPresenter.attachView(this);

        String movieItemJson = getIntent().getStringExtra(MOVIE_ITEM);
        ResultsItem movieItem = gson.fromJson(movieItemJson, ResultsItem.class);
        bindBasicDesc(movieItem);

        String movieId = String.valueOf(movieItem.getId());
        mPresenter.getDetailMovie(movieId);
    }

    @Override
    protected void onDestroy() {
        mPresenter.detachView();
        super.onDestroy();
    }

    private void bindBasicDesc(ResultsItem item) {
        getSupportActionBar().setTitle(item.getTitle());
        tv_title.setText(item.getTitle());

        Glide.with(DetailMovieActivity.this)
                .load(BuildConfig.BASE_URL_IMG + "w185" + item.getBackdropPath())
                .into(img_backdrop);

        Glide.with(DetailMovieActivity.this)
                .load(BuildConfig.BASE_URL_IMG + "w154" + item.getPosterPath())
                .into(img_poster);

        tv_release_date.setText(DateTime.getLongDate(item.getReleaseDate()));
        tv_vote.setText(String.valueOf(item.getVoteAverage()));
        tv_overview.setText(item.getOverview());

        double userRating = item.getVoteAverage() / 2;
        int integerPart = (int) userRating;

        // Fill stars
        for (int i = 0; i < integerPart; i++) {
            img_vote.get(i).setImageResource(R.drawable.ic_star_black_24dp);
        }

        // Fill half star
        if (Math.round(userRating) > integerPart) {
            img_vote.get(integerPart).setImageResource(R.drawable.ic_star_half_black_24dp);
        }
    }

    // ------------------------------------------------------------------------------------------------------------
    // MVP View Implementation ------------------------------------------------------------------------------------
    @Override
    public void showLoading(boolean isShow) {
        // do something when loading data
    }

    @Override
    public void showError(Throwable error) {
        super.showError(error);
        Toast.makeText(this, "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showDetailMovie(DetailModel item) {
        String genres = "";
        int size = item.getGenres().size();
        for (int i = 0; i < size; i++) {
            genres += "√ " + item.getGenres().get(i).getName() + (i + 1 < size ? "\n" : "");
        }
        tv_genres.setText(genres);

        if (item.getBelongsToCollection() != null) {
            Glide.with(DetailMovieActivity.this)
                    .load(BuildConfig.BASE_URL_IMG + "w92" + item.getBelongsToCollection().getPosterPath())
                    .into(img_poster_belongs);

            tv_title_belongs.setText(item.getBelongsToCollection().getName());
        }

        tv_budget.setText("$ " + NumberFormat.getIntegerInstance().format(item.getBudget()));
        tv_revenue.setText("$ " + NumberFormat.getIntegerInstance().format(item.getRevenue()));

        String companies = "";
        size = item.getProductionCompanies().size();
        for (int i = 0; i < size; i++) {
            companies += "√ " + item.getProductionCompanies().get(i).getName() + (i + 1 < size ? "\n" : "");
        }
        tv_companies.setText(companies);

        String countries = "";
        size = item.getProductionCountries().size();
        for (int i = 0; i < size; i++) {
            countries += "√ " + item.getProductionCountries().get(i).getName() + (i + 1 < size ? "\n" : "");
        }
        tv_countries.setText(countries);
    }

}
