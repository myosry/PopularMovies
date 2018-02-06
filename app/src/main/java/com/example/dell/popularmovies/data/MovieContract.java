package com.example.dell.popularmovies.data;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;


public class MovieContract {

    public  static  final  String AUTHORITY= "com.example.dell.popularmovies";
    public  static  final  String PATH_MOVIES= "movies";
    public  static  final Uri BASE_CONTENT_URI= Uri.parse("content://"+AUTHORITY);


    public String getAuthority() {
        return AUTHORITY;
    }


    public  static final class MovieEntry implements BaseColumns {

        public  static  final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_MOVIES).build();

    public static final String CONTENT_TYPE =
            ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + AUTHORITY + "/" + PATH_MOVIES;
    public static final String CONTENT_ITEM_TYPE =
            ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + AUTHORITY + "/" + PATH_MOVIES;

              public static final String TABLE_NAME = "movie";

    public static final String COLUMN_MOVIE_ID = "movie_id";
    public static final String COLUMN_TITLE = "title";
    public static final String COLUMN_IMAGE = "image";
    public static final String COLUMN_OVERVIEW = "overview";
    public static final String COLUMN_RATING = "rating";
    public static final String COLUMN_DATE = "date";
    public static final String COLUMN_POSTER_PATH = "poster_path";
    public static final String COLUMN_BACKDROP_PATH = "backdrop_path";

    public static Uri buildMovieUri(long id) {
        return ContentUris.withAppendedId(CONTENT_URI, id);
    }


}
}
