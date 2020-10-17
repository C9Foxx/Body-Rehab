package com.example.bodyrehab.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class VideoYT {
    @SerializedName("id")
    @Expose
    private VideoID id;

    @SerializedName("snippet")
    @Expose
    private Snippet snippet;

    public VideoYT(VideoID id, Snippet snippet) {
        this.id = id;
        this.snippet = snippet;
    }

    public VideoID getId() {
        return id;
    }

    public void setId(VideoID id) {
        this.id = id;
    }

    public Snippet getSnippet() {
        return snippet;
    }

    public void setSnippet(Snippet snippet) {
        this.snippet = snippet;
    }
}
