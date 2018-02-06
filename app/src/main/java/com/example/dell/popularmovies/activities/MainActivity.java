package com.example.dell.popularmovies.activities;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.PersistableBundle;
import android.preference.PreferenceManager;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.dell.popularmovies.BuildConfig;
import com.example.dell.popularmovies.R;
import com.example.dell.popularmovies.adapters.RecyclerMovieAdapter;
import com.example.dell.popularmovies.api.Client;
import com.example.dell.popularmovies.api.Service;
import com.example.dell.popularmovies.model.Movie;
import com.example.dell.popularmovies.model.MovieResponse;
import com.example.dell.popularmovies.utils.EndlessScrollListener;
import com.example.dell.popularmovies.utils.NetworkCheck;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements SharedPreferences.OnSharedPreferenceChangeListener {

    private RecyclerView mrecyclerView;
    private RecyclerMovieAdapter madapter;
    private List<Movie> movieList;
    private ProgressBar pb;
    private SwipeRefreshLayout mswipeRefreshLayout;
    private static final String LOG_TAG = RecyclerMovieAdapter.class.getName();
    private Toast mToast;
    int mScrollPosition;


    private Toolbar toolbar;
    private CollapsingToolbarLayout toolbarLayout;
    private CoordinatorLayout coordinatorLayout;
    Client client;
    Service apiService;
    NetworkCheck networkCheck;

    private static final int MAX_ITEMS_PER_REQUEST = 20;
    int page = 1;
    private StaggeredGridLayoutManager layoutManager;
    private EndlessScrollListener scrollListener;

    private static final String LIFECYCLE_CALLBACKS_MOVIES = "movies";
    private static final String LIST_STATE_KEY = "state";
    private List<Movie> movies;
    private Parcelable mListState;
    private FloatingActionButton nextPage;
    private FloatingActionButton lastPage;
    private static final String EXTRA_MOVIES = "EXTRA_MOVIES";
    private  Bundle getSavedInstance;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getSavedInstance = savedInstanceState;
        movieList = new ArrayList<Movie>();
        networkCheck = new NetworkCheck();

        initView();
        swipeRefresh();

        nextPage = findViewById(R.id.nextpage);
        nextPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                page = page + 1;
                if (page >= 993) {
                    page = 993;
                    checkSort(page);
                } else
                    checkSort(page);

            }
        });

        lastPage = findViewById(R.id.lastpage);
        lastPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                page = page - 1;
                if (page <= 0) {
                    page = 1;
                    checkSort(page);
                }
            }
        });

    }

    private void swipeRefresh() {
        mswipeRefreshLayout = findViewById(R.id.main_content);
        mswipeRefreshLayout.setColorSchemeResources(R.color.yellow);
        mswipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                initView();

                if (mToast != null) {
                    mToast.cancel();
                }
                mToast = Toast.makeText(MainActivity.this, "Movies Refreshed", Toast.LENGTH_LONG);
                mToast.show();
                pb.setVisibility(View.GONE);

            }
        });
    }



    @Override
    public void onSaveInstanceState(Bundle outState) {

        mListState = mrecyclerView.getLayoutManager().onSaveInstanceState();
        outState.putParcelable(LIST_STATE_KEY, mListState);
        Log.e("saveList", outState.toString());
        super.onSaveInstanceState(outState);

    }

    @Override
    protected void onPause() {
        mListState = mrecyclerView.getLayoutManager().onSaveInstanceState();
        super.onPause();
    }
    @Override
    protected void onRestoreInstanceState(Bundle state) {
        super.onRestoreInstanceState(state);
        if (state != null) {
            mListState = state.getParcelable(LIST_STATE_KEY);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        PreferenceManager.getDefaultSharedPreferences(this)
                .unregisterOnSharedPreferenceChangeListener(this);
    }

    public Activity getActivity() {
        Context context = this;
        while (context instanceof ContextWrapper) {
            if (context instanceof Activity) {
                return (Activity) context;
            }
            context = ((ContextWrapper) context).getBaseContext();

        }
        return null;

    }

    private void setupRecyclerView() {

        mrecyclerView = findViewById(R.id.rv_movies);
        mrecyclerView.setHasFixedSize(true);

        madapter = new RecyclerMovieAdapter(this, movieList);
        mrecyclerView.setAdapter(madapter);
        layoutManager = new StaggeredGridLayoutManager(2, LinearLayoutManager.VERTICAL);

        if (mListState != null) {
           mrecyclerView.getLayoutManager().onRestoreInstanceState(mListState);
       }

           else {

                   if (getActivity().getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
                          mrecyclerView.setLayoutManager(layoutManager);
                       mrecyclerView.getLayoutManager().onRestoreInstanceState(mListState);

                   }       else {
                       mrecyclerView.setLayoutManager(new StaggeredGridLayoutManager(3, LinearLayoutManager.VERTICAL));
                       mrecyclerView.getLayoutManager().onRestoreInstanceState(mListState);

                   }
       }
        mrecyclerView.setItemAnimator(new DefaultItemAnimator());

        madapter.notifyDataSetChanged();
    }

    private void initView() {

        pb = findViewById(R.id.progressBar);
        pb.setVisibility(View.VISIBLE);
        client = new Client();
        apiService = client.getClient().create(Service.class);

        setupRecyclerView();
        setupToolbar();

            if (networkCheck.isNetworkAvailable(this) == true) {
                checkSort(page);
                pb.setVisibility(View.GONE);


            } else {
                onFailureConnect();
            }
    }

    private void setupToolbar() {
        coordinatorLayout = findViewById(R.id.coordinator_layout);
        toolbar = findViewById(R.id.toolbar);
        setUpActionBar();
        toolbarLayout = findViewById(R.id.collapsingToolbarLayout);
        toolbarLayout.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
        toolbarLayout.setTitle("Pop Movies");
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void setUpActionBar() {
        if (toolbar != null) {

            ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
            ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayShowHomeEnabled(false);
            ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(false);
            ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayShowTitleEnabled(true);

            ((AppCompatActivity) getActivity()).getSupportActionBar().setElevation(7);


        }
    }

    private void fetchJson(Call<MovieResponse> call) {


        try {
            if (BuildConfig.THE_MOVIE_DB_API_TOKEN.isEmpty()) {

                if (mToast != null) {
                    mToast.cancel();
                }
                mToast = Toast.makeText(this, R.string.insert_api_key, Toast.LENGTH_LONG);
                mToast.show();
                return;
            }


            Log.e(LOG_TAG, "url is " + call.request().url()
            );

            call.enqueue(new Callback<MovieResponse>() {
                @Override
                public void onResponse(Call<MovieResponse> call, Response<MovieResponse> response) {
                    movies = new ArrayList<Movie>();
                    try {
                        if (response.body() != null) {
                            movies = response.body().getResults();
                        }

                        Log.e(LOG_TAG, "size is " + movies.size());

                    } catch (Exception e) {
                        Log.e(LOG_TAG, e.getMessage());
                    }

                    movieList = movies ;
                    mrecyclerView.setAdapter(new RecyclerMovieAdapter(getApplicationContext(), movieList));

                    if (mListState != null)
                        mrecyclerView.getLayoutManager().onRestoreInstanceState(mListState);


                    pb.setVisibility(View.GONE);
                    if (mswipeRefreshLayout.isRefreshing()) {
                        mswipeRefreshLayout.setRefreshing(false);
                    }
                }

                @Override
                public void onFailure(Call<MovieResponse> call, Throwable t) {
                    Log.e(LOG_TAG, t.getMessage());

                    onFailureConnect();

                }
            });

        } catch (Exception e) {
            Log.e(LOG_TAG, e.getMessage());

            if (mToast != null) {
                mToast.cancel();
            }
            mToast = Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_LONG);
            mToast.show();
        }

    }


    private void onFailureConnect() {
        Snackbar snackbar = Snackbar.make(coordinatorLayout, R.string.check_connection, Snackbar.LENGTH_INDEFINITE);
        pb.setVisibility(View.GONE);
        snackbar.setAction("Retry", new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                initView();

            }
        });
        snackbar.show();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        Intent intent;
        //noinspection SimplifiableIfStatement
        switch (id) {

            case R.id.favorite:
                Toast.makeText(this, "FavouriteActivity", Toast.LENGTH_SHORT).show();
                intent = new Intent(this, FavouriteActivity.class);
                startActivity(intent);
                break;

            case R.id.action_settings:
                intent = new Intent(this, SettingsActivity.class);
                startActivity(intent);
                break;

            default:


        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String s) {
        Log.d(LOG_TAG, "prefrences Updated");
        checkSort(page);
    }

    private void checkSort(final int page) {
        pb.setVisibility(View.VISIBLE);

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        String sortOrder = preferences.getString(
                this.getString(R.string.pref_sort_order_key),
                this.getString(R.string.pref_most_popular));

        if (networkCheck.isNetworkAvailable(this) == true) {


            if (sortOrder.equals(this.getString(R.string.pref_most_popular))) {
                Log.d(LOG_TAG, " sort by most popular");
                fetchJson(apiService.getPopularMovies(BuildConfig.THE_MOVIE_DB_API_TOKEN, page));

            } else {
                Log.d(LOG_TAG, "sort by highest reated");
                fetchJson(apiService.getTopRatedMovies(BuildConfig.THE_MOVIE_DB_API_TOKEN, page));

            }


        } else {
            onFailureConnect();
        }
        preferences.registerOnSharedPreferenceChangeListener(this);

    }
}

