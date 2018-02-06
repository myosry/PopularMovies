package com.example.dell.popularmovies.model;

import java.util.List;

import com.example.dell.popularmovies.model.TralierResult;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class TrailerResponse {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("results")
    @Expose
    private List<TralierResult> results = null;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public List<TralierResult> getResults() {
        return results;
    }


}