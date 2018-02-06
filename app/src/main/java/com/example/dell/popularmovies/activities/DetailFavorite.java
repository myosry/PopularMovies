package com.example.dell.popularmovies.activities;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Parcelable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.ShareActionProvider;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dell.popularmovies.R;
import com.example.dell.popularmovies.data.MovieContract;
import com.example.dell.popularmovies.model.Movie;
import com.example.dell.popularmovies.utils.ShareMovie;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class DetailFavorite extends AppCompatActivity {

    private FloatingActionButton fab;
    Intent intent ;
    String title,_id ,posterPath;
    Movie movie;
    private ImageView poster_movie_img;
    private TextView overview_movie ,release_date_movie,rate_movie,title_movie;
      Toolbar toolbar;
    private Parcelable mListState;
    private static final String LIFECYCLE_CALLBACKS_MOVIES = "movies";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_favorite);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setUpActionBar();

        initviews();
         intent = getIntent();

        overview_movie.setText(intent.getStringExtra("overview"));
        release_date_movie.setText(intent.getStringExtra("release"));
        rate_movie.setText(intent.getStringExtra("vote"));
        title =intent.getStringExtra("title");
        _id =intent.getStringExtra("_id");

        posterPath = Environment.getExternalStorageDirectory().getPath() + "/" +_id;

        Log.e("_id","fav is "+ posterPath);

            Picasso.with(this)
                    .load("file://"+posterPath)
                    .placeholder(R.drawable.loading)
                    .into(poster_movie_img);




        fab = (FloatingActionButton) findViewById(R.id.delete_fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteMovie();
            }
        });
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
            ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayShowHomeEnabled(true);
            ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayShowTitleEnabled(true);
            ((AppCompatActivity) getActivity()).getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(true);

            ((AppCompatActivity) getActivity()).getSupportActionBar().setElevation(7);


        }
    }


    private void initviews() {
        poster_movie_img = findViewById(R.id.poster_movie);
        overview_movie = findViewById(R.id.overview_movie);
        release_date_movie = findViewById(R.id.release_date_text);
        rate_movie = findViewById(R.id.rate_movie);
        title_movie = findViewById(R.id.title_movie);

    }

    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.detail_fav_menu, menu);
        MenuItem menuItem = menu.findItem(R.id.share);
        ShareActionProvider mShareActionProvider = (ShareActionProvider) MenuItemCompat.getActionProvider(menuItem);
        // Set share Intent.
        // Note: You can set the share Intent afterwords if you don't want to set it right now.
        mShareActionProvider.setShareIntent(ShareMovie.createShareIntent(title));
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


    private void deleteMovie() {
        String[] mSelectionArgs = new String[]{title};
        int row = getContentResolver().delete(MovieContract.MovieEntry.CONTENT_URI, "title=?", mSelectionArgs);


        Toast.makeText(DetailFavorite.this, "movie deleted", Toast.LENGTH_SHORT).show();
       Intent intent = new Intent(this,FavouriteActivity.class);
       startActivity(intent);


    }

}
