package com.example.bodyrehab.models;

public class VideoClass {
    String id;
    String thumbnail;
    String title;
    String routine;

    public VideoClass (String id, String thumbnail, String title, String routine){
        id = this.id;
        thumbnail =  this.thumbnail;
        title = this.title;
        routine = this.routine;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getRoutine() {
        return routine;
    }

    public void setRoutine(String routine) {
        this.routine = routine;
    }
}
