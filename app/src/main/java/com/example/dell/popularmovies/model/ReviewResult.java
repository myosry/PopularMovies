package com.example.dell.popularmovies.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;



public class ReviewResult {
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("author")
    @Expose
    private String author;
    @SerializedName("content")
    @Expose
    private String content;
    @SerializedName("url")
    @Expose
    private String url;

    public String getId() {
        return id;
    }


    public String getAuthor() {
        return author;
    }


    public String getContent() {
        return content;
    }


    public String getUrl() {
        return url;
    }


}
