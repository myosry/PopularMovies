package com.example.dell.popularmovies.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class TralierResult {

    @SerializedName("key")
    @Expose
    private String key;

    @SerializedName("name")
    @Expose
    private String name;

    public void setName(String name){
        this.name = name;
    }
    public void setKey(String key){
        this.key = key;
    }
    public String getKey() {
        return "https://www.youtube.com/watch?v="+key;
    }

    public String getName() {
        return name;
    }

}