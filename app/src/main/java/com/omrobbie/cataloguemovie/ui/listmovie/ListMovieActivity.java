package com.omrobbie.cataloguemovie.ui.listmovie;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import com.mancj.materialsearchbar.MaterialSearchBar;
import com.omrobbie.cataloguemovie.R;
import com.omrobbie.cataloguemovie.adapter.SearchAdapter;
import com.omrobbie.cataloguemovie.data.model.search.ResultsItem;
import com.omrobbie.cataloguemovie.ui.base.BaseActivity;
import com.omrobbie.cataloguemovie.utils.AlarmReceiver;
import com.omrobbie.cataloguemovie.utils.upcoming.SchedulerTask;

import java.text.NumberFormat;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

import static android.support.v7.widget.DividerItemDecoration.VERTICAL;

public class ListMovieActivity extends BaseActivity
        implements MaterialSearchBar.OnSearchActionListener,
        SwipeRefreshLayout.OnRefreshListener,
        PopupMenu.OnMenuItemClickListener,
        ListMovieContract.View {

    private static final String TAG = ListMovieActivity.class.getSimpleName();
    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.swipe_refresh)
    SwipeRefreshLayout swipe_refresh;

    @BindView(R.id.search_bar)
    MaterialSearchBar search_bar;

    @BindView(R.id.rv_movielist)
    RecyclerView rv_movielist;

    private SearchAdapter mAdapter;

    private AlarmReceiver alarmReceiver = new AlarmReceiver();
    private SchedulerTask schedulerTask;

    @Inject ListMoviePresenter mPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        // inject dependency to this activity
        activityComponent().inject(this);
        mPresenter.attachView(this);

        alarmReceiver.setRepeatingAlarm(this, alarmReceiver.TYPE_REPEATING, "07:00", "Good morning! Ready to pick your new movies today?");

        schedulerTask = new SchedulerTask(this);
        schedulerTask.createPeriodicTask();

        setSupportActionBar(toolbar);
        search_bar.setOnSearchActionListener(this);
        swipe_refresh.setOnRefreshListener(this);

        search_bar.inflateMenu(R.menu.main);
        search_bar.getMenu().setOnMenuItemClickListener(this);

        setupList();
        setupListScrollListener();
        mPresenter.getPopularMovie();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPresenter.detachView();
        mPresenter.setConfigurationChange(isChangingConfigurations());
    }

    /**
     * Invoked when SearchBar opened or closed
     *
     * @param enabled
     */
    @Override
    public void onSearchStateChanged(boolean enabled) {

    }

    /**
     * Invoked when search confirmed and "search" button is clicked on the soft keyboard
     *
     * @param text search input
     */
    @Override
    public void onSearchConfirmed(CharSequence text) {
        Log.d(TAG, "onSearchConfirmed: text " + text.toString());
        Toast.makeText(this, "Feature disabled!", Toast.LENGTH_SHORT).show();
    }

    /**
     * Invoked when "speech" or "navigation" buttons clicked.
     *
     * @param buttonCode
     */
    @Override
    public void onButtonClicked(int buttonCode) {

    }

    /**
     * Called when a swipe gesture triggers a refresh.
     */
    @Override
    public void onRefresh() {
        mAdapter.clearAll();
        mPresenter.onPageRefresh();
    }

    /**
     * This method will be invoked when a menu item is clicked if the item
     * itself did not already handle the event.
     *
     * @param item the menu item that was clicked
     * @return {@code true} if the event was handled, {@code false}
     * otherwise
     */
    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.mn_refresh:
                onRefresh();
                break;
        }

        return false;
    }

    private void setupList() {
        mAdapter = new SearchAdapter();
        rv_movielist.addItemDecoration(new DividerItemDecoration(this, VERTICAL));
        rv_movielist.setLayoutManager(new LinearLayoutManager(this));
        rv_movielist.setAdapter(mAdapter);
    }

    private void setupListScrollListener() {
        rv_movielist.addOnScrollListener(new RecyclerView.OnScrollListener() {
            /**
             * Callback method to be invoked when the RecyclerView has been scrolled. This will be
             * called after the scroll has completed.
             * <p>
             * This callback will also be called if visible item range changes after a layout
             * calculation. In that case, dx and dy will be 0.
             *
             * @param recyclerView The RecyclerView which scrolled.
             * @param dx           The amount of horizontal scroll.
             * @param dy           The amount of vertical scroll.
             */
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();

                int totalItems = layoutManager.getItemCount();
                int visibleItems = layoutManager.getChildCount();
                int pastVisibleItems = layoutManager.findFirstCompletelyVisibleItemPosition();

                if (pastVisibleItems + visibleItems >= totalItems) {
                    if (!swipe_refresh.isRefreshing()) {
                        Log.d(TAG, "onScrolled: load more movie");
                        mPresenter.getPopularMovie();
                    }
                }
            }
        });
    }

    private void showResults(int totalResults) {
        String results;

        String formatResults = NumberFormat.getIntegerInstance().format(totalResults);

        if (totalResults > 0) {
            results = "I found " + formatResults + " movie" + (totalResults > 1 ? "s" : "") + " for you :)";
        } else results = "Sorry! I can't find your movie :(";

        getSupportActionBar().setSubtitle(results);
    }

    // ------------------------------------------------------------------------------------------------------------
    // MVP View Implementation ------------------------------------------------------------------------------------
    @Override
    public void showLoading(boolean isShow) {
        super.showLoading(isShow);
        swipe_refresh.setRefreshing(isShow);
    }

    @Override
    public void showError(Throwable error) {
        super.showError(error);
        Toast.makeText(this, "Error " + error.getMessage(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showPopularMovie(List<ResultsItem> resultsItems, int totalResult) {
        mAdapter.updateData(resultsItems);
        showResults(totalResult);
    }

    @Override
    public void showNoDataFound() {
        Toast.makeText(this, "No data found", Toast.LENGTH_SHORT).show();
    }

}
