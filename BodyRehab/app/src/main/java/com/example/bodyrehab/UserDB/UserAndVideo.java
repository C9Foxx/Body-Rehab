package com.example.bodyrehab.UserDB;

import androidx.room.Embedded;
import androidx.room.Relation;

import java.util.List;

public class UserAndVideo {

    @Embedded
    public
    User user;

    @Relation(
            parentColumn = "user_id",
            entityColumn = "container_id",
            entity = Video.class
    )
    public List<Video> videoList;

    public UserAndVideo(User user, List<Video> videoList) {
        this.user = user;
        this.videoList = videoList;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public List<Video> getVideoList() {
        return videoList;
    }

    public void setVideoList(List<Video> videoList) {
        this.videoList = videoList;
    }
}
