//package com.example.dell.popularmovies.data;
//
//import android.net.Uri;
//
//import de.triplet.simpleprovider.AbstractProvider;
//import de.triplet.simpleprovider.Column;
//import de.triplet.simpleprovider.Table;
//
///**
// * Created by DELL on 12/21/2017.
// */
//
//public class MovieProvider extends AbstractProvider {
//
//    public  static  final  String AUTHORITY= "com.example.dell.popularmovies";
//    public  static  final  String PATH_MOVIES= "movies";
//    public  static  final Uri BASE_CONTENT_URI= Uri.parse("content://"+AUTHORITY);
//    public  static  final Uri CONTENT_URI= BASE_CONTENT_URI.buildUpon().appendPath(PATH_MOVIES).build();
//
//    @Override
//    public String getAuthority() {
//        return AUTHORITY;
//    }
//
//
//
//
//    @Table
//    public class Movie{
//
//        @Column(value = Column.FieldType.INTEGER, primaryKey = true ,unique = true)
//        public static final String MOVIE_ID = "_id";
//
//        @Column(Column.FieldType.TEXT)
//        public static final String MOVIE_TITLE = "title";
//
//        @Column(Column.FieldType.TEXT)
//        public static final String MOVIE_OVERVIEW = "overview";
//
//        @Column(Column.FieldType.TEXT)
//        public static final String MOVIE_RATE = "rating";
//
//        @Column(Column.FieldType.TEXT)
//        public static final String MOVIE_DATE = "date";
//
//        @Column(Column.FieldType.TEXT)
//        public static final String MOVIE_POSTER_PATH = "poster_path";
//
//        @Column(Column.FieldType.TEXT)
//        public static final String MOVIE_BACKDROP_PATH = "backdrop_path";
//
//    }
//}
