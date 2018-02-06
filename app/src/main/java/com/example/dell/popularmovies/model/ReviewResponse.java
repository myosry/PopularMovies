package com.example.dell.popularmovies.model;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ReviewResponse {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("page")
    @Expose
    private Integer page;
    @SerializedName("results")
    @Expose
    private List<ReviewResult> results = null;
    @SerializedName("total_pages")
    @Expose
    private Integer totalPages;
    @SerializedName("total_results")
    @Expose
    private Integer totalResults;

    public Integer getId() {
        return id;
    }


    public Integer getPage() {
        return page;
    }


    public List<ReviewResult> getResults() {
        return results;
    }



    public Integer getTotalPages() {
        return totalPages;
    }


    public Integer getTotalResults() {
        return totalResults;
    }



}