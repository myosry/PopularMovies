package com.example.dell.popularmovies.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class MovieResponse {



    @SerializedName("page")
    @Expose
    private Integer page;
    @SerializedName("total_results")
    @Expose
    private Integer totalResults;
    @SerializedName("total_pages")
    @Expose
    private Integer totalPages;
    @SerializedName("results")
    @Expose
    private List<Movie> results = null;

    public Integer getPage() {
        return page;
    }


    public Integer getTotalResults() {
        return totalResults;
    }


    public Integer getTotalPages() {
        return totalPages;
    }


    public List<Movie> getResults() {
        return results;
    }


}