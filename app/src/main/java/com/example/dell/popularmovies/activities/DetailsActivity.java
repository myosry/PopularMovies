package com.example.dell.popularmovies.activities;

import android.Manifest;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.ShareActionProvider;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import com.example.dell.popularmovies.api.Client;
import com.example.dell.popularmovies.api.Service;

import com.example.dell.popularmovies.BuildConfig;
import com.example.dell.popularmovies.R;
import com.example.dell.popularmovies.adapters.RecyclerMovieReviewsAdapter;
import com.example.dell.popularmovies.adapters.RecyclerMovieTrailerAdapter;

import com.example.dell.popularmovies.data.MovieContract;
import com.example.dell.popularmovies.data.MyMovieProvider;
import com.example.dell.popularmovies.model.Movie;
import com.example.dell.popularmovies.model.ReviewResponse;
import com.example.dell.popularmovies.model.ReviewResult;
import com.example.dell.popularmovies.model.TrailerResponse;
import com.example.dell.popularmovies.model.TralierResult;
import com.example.dell.popularmovies.utils.NetworkCheck;
import com.example.dell.popularmovies.utils.ShareMovie;
import com.github.florent37.picassopalette.PicassoPalette;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetailsActivity extends AppCompatActivity {

    ImageView backdrop_movie_img ;
    ImageView poster_movie_img ;
    TextView overview_movie ;
    TextView release_date_movie ;
    TextView title_movie ;
    TextView rate_movie ;
    Intent intent;
    Movie movie ;

    String uri_backdrop_movie_img ;
    String uri_poster_movie_img ;
    String overview_movie_text ;
    String release_date_movie_text ;
    String vote_average_text;
    String title;

    private CollapsingToolbarLayout toolbarLayout;
    private Toolbar toolbar;
    private int movieId =346364;

    private RecyclerMovieTrailerAdapter movieTrailerAdapter ;
    private List<TralierResult> trailer;
    private RecyclerView movieTrailerRecycler ;

    private RecyclerMovieReviewsAdapter movieReviewsAdapter;
    private RecyclerView movieReviewsRecycler ;
    private List<ReviewResult> reviews;

    private ProgressBar pb;
    private Toast mToast;
    private static final String LOG_TAG = DetailsActivity.class.getName();
    NetworkCheck networkCheck ;
    TextView reviews_header;
    TextView trailers_header;
    FloatingActionButton favoriteButton;

    String photo_path ="path";
    String photo_backdrop ="bacdrop";
    Client client;
    Service apiService;
    Cursor mCursor ;
    SharedPreferences pref;
    SharedPreferences.Editor editor;
    Boolean fav;
    FloatingActionButton fav_delete_movie;
    private ShareActionProvider mShareActionProvider;
    private static final int MY_PERMISSION_WRITE_IN_EXSTORAGEREQUEST_CODE = 88;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null){
            fav =savedInstanceState.getBoolean("fav");
        }

        setContentView(R.layout.activity_details);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Context context = getBaseContext();

         pref = getApplicationContext().getSharedPreferences("MyPref", 0); // 0 - for private mode
        editor = pref.edit();

         fav = pref.getBoolean("fav",false);

        pb = findViewById(R.id.progressBar2);
        pb.setVisibility(View.VISIBLE);

        client  = new Client();
        apiService = client.getClient().create(Service.class);
        networkCheck = new NetworkCheck();

        initView();

        favoriteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CheckMovie();


            }
        });

        fav_delete_movie.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteMovie();

            }
        });

    }

    public  boolean queryCheckMovieInDataBase(){

        final String args[]= {title} ;
        final String[] CHECK_TITLE = {
                MovieContract.MovieEntry.COLUMN_TITLE        };

        mCursor= getContentResolver().query(MovieContract.MovieEntry.CONTENT_URI ,CHECK_TITLE,"title=?",args ,null);
        if (mCursor != null && mCursor.getCount() > 0) {
        return true;
        }
        else
            return  false;
        }



    @Override
    protected void onStart() {
        CheckFavouriteResource();
        super.onStart();
    }

    @Override
    protected void onResume() {
        CheckFavouriteResource();
        super.onResume();
    }




    private void CheckFavouriteResource( ) {
        if (queryCheckMovieInDataBase() == true) {
            fav_delete_movie.setVisibility(View.VISIBLE);
            favoriteButton.setVisibility(View.GONE);

        }
        else {
            fav_delete_movie.setVisibility(View.GONE);
            favoriteButton.setVisibility(View.VISIBLE);
        }

        }
    private void setupPermissions() {
        // If we don't have the record audio permission...
        if ((ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)||
                (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)) {
            // And if we're on SDK M or later...
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                // Ask again, nicely, for the permissions.
                String[] permissionsWeNeed = new String[]{ Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE };
                requestPermissions(permissionsWeNeed, MY_PERMISSION_WRITE_IN_EXSTORAGEREQUEST_CODE);
            }
        } else {
            // Otherwise, permissions were granted and we are ready to go!
               saveinDataBase();
            }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String permissions[], @NonNull int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSION_WRITE_IN_EXSTORAGEREQUEST_CODE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && (grantResults[0] == PackageManager.PERMISSION_GRANTED
                        || grantResults[1] != PackageManager.PERMISSION_GRANTED))
                {
                    // The permission was granted! Start up the visualizer!
                           saveinDataBase();
                } else {
                    Toast.makeText(this, "Permission for  save Movie not granted. Movie can't saved.", Toast.LENGTH_LONG).show();
                    finish();
                    // The permission was denied, so we can show a message why we can't run the app
                    // and then close the app.
                }
            }
            // Other permissions could go down here

        }
    }

        private void CheckMovie() {

        if (queryCheckMovieInDataBase() == true) {
            Toast.makeText(this,"Tihs movie already in your favourite ", Toast.LENGTH_LONG).show();
        }
        else {
            setupPermissions();

        }
    }

    private void deleteMovie() {
        String[] mSelectionArgs = new String[]{title};
        int row = getContentResolver().delete(MovieContract.MovieEntry.CONTENT_URI, "title=?", mSelectionArgs);


        fav_delete_movie.setVisibility(View.GONE);
        favoriteButton.setVisibility(View.VISIBLE);
        Toast.makeText(DetailsActivity.this, "row delete" + row, Toast.LENGTH_SHORT).show();
        Intent i = new Intent(DetailsActivity.this,MainActivity.class);
        startActivity(i);
    }

    private void saveinDataBase() {
        if (queryCheckMovieInDataBase() == false) {

            ContentValues cv = new ContentValues();
            cv.put(MovieContract.MovieEntry.COLUMN_OVERVIEW, overview_movie_text);
            cv.put(MovieContract.MovieEntry.COLUMN_MOVIE_ID, movieId);
            cv.put(MovieContract.MovieEntry.COLUMN_BACKDROP_PATH, uri_backdrop_movie_img);
            cv.put(MovieContract.MovieEntry.COLUMN_DATE, release_date_movie_text);
            cv.put(MovieContract.MovieEntry.COLUMN_POSTER_PATH, uri_poster_movie_img);
            cv.put(MovieContract.MovieEntry.COLUMN_RATING, vote_average_text);
            cv.put(MovieContract.MovieEntry.COLUMN_TITLE, title);
            Uri uri = getContentResolver().insert(MovieContract.MovieEntry.CONTENT_URI, cv);

            Picasso.with(getBaseContext())
                    .load(uri_poster_movie_img)
                    .into(target);

            fav_delete_movie.setVisibility(View.VISIBLE);
            favoriteButton.setVisibility(View.GONE);
            if (uri != null) {
                Toast.makeText(DetailsActivity.this, uri.toString(), Toast.LENGTH_SHORT).show();
            }

        }
        else
            Toast.makeText(this,"failed",Toast.LENGTH_SHORT).show();
    }


    Target target = new Target() {

        @Override
        public void onBitmapLoaded(final Bitmap bitmap, Picasso.LoadedFrom from) {
            new Thread(new Runnable() {

                @Override
                public void run() {

                    File file = new File(Environment.getExternalStorageDirectory().getPath() + "/" +movieId);
                    Log.e("_id", "path is "+ Environment.getExternalStorageDirectory().getPath()+ "/" +movieId);
                    try {
                        file.createNewFile();
                        FileOutputStream ostream = new FileOutputStream(file);
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 80, ostream);
                        ostream.close();
                        Log.e("stream","outstream:"+ostream);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
            }).start();
        }

        @Override
        public void onBitmapFailed(Drawable errorDrawable) {}

        @Override
        public void onPrepareLoad(Drawable placeHolderDrawable) {
            if (placeHolderDrawable != null) {}
        }
    };

    public Activity getActivity () {
        Context context = this;
        while (context instanceof ContextWrapper) {
            if (context instanceof Activity) {
                return (Activity) context;
            }
            context = ((ContextWrapper) context).getBaseContext();

        }
        return null;

    }
    private void initView() {

        backdrop_movie_img = findViewById(R.id.backdrop_poster);
        poster_movie_img = findViewById(R.id.poster_movie);
        overview_movie = findViewById(R.id.overview_movie);
        release_date_movie = findViewById(R.id.release_date_text);
        rate_movie = findViewById(R.id.rate_movie);
        title_movie = findViewById(R.id.title_movie);
        reviews_header = findViewById(R.id.reviews_header);
        trailers_header = findViewById(R.id.trailers_header);
        favoriteButton = findViewById(R.id.fav_fab);
        fav_delete_movie = findViewById(R.id.delete_fab);

        reviews_header.setVisibility(View.GONE);
        trailers_header.setVisibility(View.GONE);

        intent = getIntent();

        if (intent.hasExtra("movies")) {

            movie = getIntent().getParcelableExtra("movies");


            uri_backdrop_movie_img = movie.getBackdropPath();
            uri_poster_movie_img = movie.getPosterPath();
            overview_movie_text = movie.getOverview();
            release_date_movie_text = movie.getReleaseDate();
            vote_average_text = Double.toString(movie.getVoteAverage());
            title = movie.getTitle();
            movieId = movie.getId();
            Log.d("id", "id" + movieId);

        }
        else {
            Toast.makeText(this, "No API Data", Toast.LENGTH_SHORT).show();

        }

        movieTrailerRecycler = findViewById(R.id.rec_videos);
        trailer = new ArrayList<>();
        movieTrailerAdapter = new RecyclerMovieTrailerAdapter(this, trailer);

        movieTrailerRecycler.setHasFixedSize(true);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        movieTrailerRecycler.setLayoutManager(mLayoutManager);
        movieTrailerRecycler.setItemAnimator(new DefaultItemAnimator());
        movieTrailerRecycler.setAdapter(movieTrailerAdapter);
        movieTrailerAdapter.notifyDataSetChanged();


        movieReviewsRecycler = findViewById(R.id.rec_reviews);
        reviews = new ArrayList<>();
        movieReviewsAdapter = new RecyclerMovieReviewsAdapter(this,reviews);

        movieReviewsRecycler.setHasFixedSize(true);
        movieReviewsRecycler.setLayoutManager(
                new LinearLayoutManager(this));
        movieReviewsRecycler.setItemAnimator(new DefaultItemAnimator());
        movieReviewsRecycler.setAdapter(movieReviewsAdapter);
        movieReviewsAdapter.notifyDataSetChanged();

        if( networkCheck.isNetworkAvailable(this) == true ) {

            fetchTrailerJson(movieId);
            fetchReviewJson(movieId);

            pb.setVisibility(View.INVISIBLE);

        }
        else{
            onFailureConnect();
        }



        intializeViews();
        CheckFavouriteResource();


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

    private void fetchTrailerJson(int movieId) {
pb.setVisibility(View.VISIBLE);

        try {
            if (BuildConfig.THE_MOVIE_DB_API_TOKEN.isEmpty()) {

                if (mToast != null) {
                    mToast.cancel();
                }
                mToast = Toast.makeText(this, R.string.insert_api_key, Toast.LENGTH_LONG);
                mToast.show();
                return;
            }


            Call<TrailerResponse> call = apiService.getMovieVideos(movieId, BuildConfig.THE_MOVIE_DB_API_TOKEN);
            Log.e("videorl", "Videos url is " + call.request().url().toString());

            call.enqueue(new Callback<TrailerResponse>() {
                @Override
                public void onResponse(Call<TrailerResponse> call, Response<TrailerResponse> response) {
                    List<TralierResult> videos = new ArrayList<TralierResult>();
                 Log.d("fethch",call.request().url().toString());
                    try {
                        if (response.body() != null) {
                            videos = response.body().getResults();
                            trailers_header.setVisibility(View.VISIBLE);
                            movieTrailerRecycler.setVisibility(View.VISIBLE);
                        }

                        Log.e(LOG_TAG, "size is " + videos.size());

                    } catch (Exception e) {
                        Log.e(LOG_TAG, e.getMessage());
                    }

                    movieTrailerRecycler.setAdapter(new RecyclerMovieTrailerAdapter(getApplicationContext(), videos));
                    movieTrailerRecycler.smoothScrollToPosition(0);
                    pb.setVisibility(View.INVISIBLE);
//                    if (mswipeRefreshLayout.isRefreshing()) {
//                        mswipeRefreshLayout.setRefreshing(false);
//                    }

                }

                @Override
                public void onFailure(Call<TrailerResponse> call, Throwable t) {
                    Log.e(LOG_TAG, t.getMessage());

                    onFailureConnect();

                }
            });

        } catch (Exception e) {
            Log.e(LOG_TAG, e.getMessage());

            if (mToast != null) {
                mToast.cancel();
            }
            mToast = Toast.makeText(DetailsActivity.this, "this is msg"+e.getMessage(), Toast.LENGTH_LONG);
            mToast.show();
        }

    }


    private void fetchReviewJson(int movieId) {
        pb.setVisibility(View.VISIBLE);

        try {
            if (BuildConfig.THE_MOVIE_DB_API_TOKEN.isEmpty()) {

                if (mToast != null) {
                    mToast.cancel();
                }
                mToast = Toast.makeText(this, R.string.insert_api_key, Toast.LENGTH_LONG);
                mToast.show();
                return;
            }


            Call<ReviewResponse> call = apiService.getMovieReviews(movieId, BuildConfig.THE_MOVIE_DB_API_TOKEN);

            call.enqueue(new Callback<ReviewResponse>() {
                @Override
                public void onResponse(Call<ReviewResponse> call, Response<ReviewResponse> response) {
                    List<ReviewResult> reviews = new ArrayList<>();

                    try {
                        if (response.body() != null) {
                            reviews = response.body().getResults();
                            reviews_header.setVisibility(View.VISIBLE);
                            movieReviewsRecycler.setVisibility(View.VISIBLE);
                        }
                        else {
                            reviews_header.setVisibility(View.INVISIBLE);
                            movieReviewsRecycler.setVisibility(View.INVISIBLE);
                        }

                        Log.e(LOG_TAG, "size is " + reviews.size());

                    } catch (Exception e) {
                        Log.e(LOG_TAG, e.getMessage());
                    }

                    movieReviewsRecycler.setAdapter(new RecyclerMovieReviewsAdapter(getApplicationContext(), reviews));
                    movieReviewsRecycler.smoothScrollToPosition(0);
                    pb.setVisibility(View.INVISIBLE);
//                    if (mswipeRefreshLayout.isRefreshing()) {
//                        mswipeRefreshLayout.setRefreshing(false);
//                    }

                }

                @Override
                public void onFailure(Call<ReviewResponse> call, Throwable t) {
                    Log.e(LOG_TAG, t.getMessage());

                    onFailureConnect();

                }
            });

        } catch (Exception e) {
            Log.e(LOG_TAG, e.getMessage());

            if (mToast != null) {
                mToast.cancel();
            }
            mToast = Toast.makeText(DetailsActivity.this, "this is msg"+e.getMessage(), Toast.LENGTH_LONG);
            mToast.show();
        }

    }

    private void onFailureConnect() {
        pb.setVisibility(View.INVISIBLE);
        Toast.makeText(getActivity(),R.string.check_connection,Toast.LENGTH_SHORT).show();

    }

    private void intializeViews() {
        toolbar = findViewById(R.id.toolbar_details); // Attaching the layout to the toolbar object
        setUpActionBar();
        toolbarLayout = findViewById(R.id.collapsingToolbarLayout);

        overview_movie.setText(overview_movie_text);
        release_date_movie.setText(release_date_movie_text);
        rate_movie.setText(vote_average_text);
        toolbarLayout.setTitle(title);

        Picasso.with(this)
                .load(uri_backdrop_movie_img)
                .placeholder(R.drawable.loading)
                .into(backdrop_movie_img
                        , PicassoPalette.with(uri_backdrop_movie_img, backdrop_movie_img)
                                .use(PicassoPalette.Profile.MUTED_DARK)
                                .intoBackground( toolbarLayout)
                                .intoTextColor(title_movie)
                                .use(PicassoPalette.Profile.VIBRANT)
                                .intoBackground( toolbarLayout, PicassoPalette.Swatch.RGB)
                                .intoTextColor( title_movie, PicassoPalette.Swatch.BODY_TEXT_COLOR)
                );

        Picasso.with(this)
                .load(uri_poster_movie_img)
                .placeholder(R.drawable.loading)
                .into(poster_movie_img);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.detail_menu, menu);
        MenuItem menuItem = menu.findItem(R.id.share);
// Fetch and store ShareActionProvider
        mShareActionProvider = (ShareActionProvider) MenuItemCompat.getActionProvider(menuItem);
        // Set share Intent.
        // Note: You can set the share Intent afterwords if you don't want to set it right now.
        mShareActionProvider.setShareIntent(ShareMovie.createShareIntent(movie.getTitle()));
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        if (id == R.id.favorite) {
            Intent intent = new Intent(this,FavouriteActivity.class);
            startActivity(intent);
            return true;
        }

        if(id == R.id.homeAsUp){
            onBackPressed();
            return true;

        }

        if(id == R.id.share){
            Toast.makeText(this,movie.getTitle(),Toast.LENGTH_SHORT).show();
//            shareMovieName();
            return true;

        }
        return super.onOptionsItemSelected(item);
    }


}
