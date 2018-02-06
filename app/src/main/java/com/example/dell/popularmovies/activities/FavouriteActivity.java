package com.example.dell.popularmovies.activities;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.res.Configuration;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Build;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.ShareActionProvider;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.dell.popularmovies.R;
import com.example.dell.popularmovies.adapters.RecyclerFavouriteAdapter;
import com.example.dell.popularmovies.adapters.RecyclerMovieAdapter;
import com.example.dell.popularmovies.data.MovieContract;
import com.example.dell.popularmovies.model.Movie;
import com.example.dell.popularmovies.utils.ShareMovie;

import java.util.ArrayList;
import java.util.List;

public class FavouriteActivity extends AppCompatActivity  {

    private RecyclerView mrecyclerView;
    private RecyclerFavouriteAdapter madapter;
    private List<Movie> movieList;
    private ProgressBar pb;
    private static final String LOG_TAG = RecyclerMovieAdapter.class.getName();
    private Toast mToast;

    private Toolbar toolbar;
    private CollapsingToolbarLayout toolbarLayout;
    private CoordinatorLayout coordinatorLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favourite);

        favInitView();
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
    @SuppressLint("RestrictedApi")
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void setUpActionBar() {
        if (toolbar != null) {

            ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
            ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayShowHomeEnabled(false);
            ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(false);
            ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayShowTitleEnabled(false);
            ((AppCompatActivity) getActivity()).getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(true);

            ((AppCompatActivity) getActivity()).getSupportActionBar().setElevation(7);


        }
    }
    private void createViews() {
        mrecyclerView = findViewById(R.id.rv_movies);
        mrecyclerView.setHasFixedSize(true);
        movieList = new ArrayList<Movie>();
        madapter = new RecyclerFavouriteAdapter(this, movieList);

//        coordinatorLayout = findViewById(R.id.coordinator_layout);
        toolbar = findViewById(R.id.toolbar); // Attaching the layout to the toolbar object
        setUpActionBar();
        toolbarLayout = findViewById(R.id.collapsingToolbarLayout);
        toolbarLayout.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));


        if (getActivity().getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            mrecyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        } else {
            mrecyclerView.setLayoutManager(new GridLayoutManager(this, 4));
        }

        mrecyclerView.setItemAnimator(new DefaultItemAnimator());
        mrecyclerView.setAdapter(madapter);
        madapter.notifyDataSetChanged();
    }
    private void favInitView() {
        createViews();
        getAllFavMovies();


    }

    public List<Movie> getAllMovies() {
        List<Movie> favList = new ArrayList<>();

        Cursor mCursor = getContentResolver().query(
                MovieContract.MovieEntry.CONTENT_URI,
                null,
                null,
                null,
                null
        );
        if (mCursor.moveToFirst()){
            do {
                Movie movie ;
                String movie_title = mCursor.getString(mCursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_TITLE));
                String movie_overview = mCursor.getString(mCursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_OVERVIEW));
                String movie_release_date = mCursor.getString(mCursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_DATE));
                String movie_posterPath = mCursor.getString(mCursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_POSTER_PATH));
                Double movie_rate = Double.parseDouble (mCursor.getString(mCursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_RATING)));
                String movie_backdropPath= mCursor.getString(mCursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_BACKDROP_PATH));
                int movie_id= Integer.valueOf(mCursor.getString(mCursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_MOVIE_ID)));

                movie = new Movie(movie_id,movie_rate, movie_title,movie_posterPath,
                        movie_title,movie_backdropPath,movie_overview,movie_release_date);
                favList.add(movie);

            }while(mCursor.moveToNext());
        }
        mCursor.close();
        return favList;


    }

    public void getAllFavMovies() {

        new AsyncTask<Void, Void, Void>() {

            @Override
            protected Void doInBackground(Void... voids) {
                try {
                    movieList.clear();
                    movieList.addAll(getAllMovies());
                } catch (Exception e) {
                }


                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                madapter.notifyDataSetChanged();
            }
        }.execute();

    }
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.favourite_menu, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        Intent intent;
        //noinspection SimplifiableIfStatement
        switch (id) {

            case R.id.homepage:
                intent  = new Intent(this, MainActivity.class);
                startActivity(intent);
                break;

                case R.id.homeAsUp :
                onBackPressed();
                break;


            default:


        }
        return super.onOptionsItemSelected(item);
    }
}
