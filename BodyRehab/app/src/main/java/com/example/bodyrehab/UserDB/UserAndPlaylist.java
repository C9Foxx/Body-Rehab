package com.example.bodyrehab.UserDB;

import androidx.room.Embedded;
import androidx.room.Relation;

import java.util.List;

public class UserAndPlaylist {

    @Embedded public
    User user;

    @Relation (
            parentColumn = "user_id",
            entityColumn = "creator_id",
            entity = Playlist.class
    )
    public List<Playlist> playlistList;

    public UserAndPlaylist(User user, List<Playlist> playlistList) {
        this.user = user;
        this.playlistList = playlistList;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public List<Playlist> getPlaylistList() {
        return playlistList;
    }

    public void setPlaylistList(List<Playlist> playlistList) {
        this.playlistList = playlistList;
    }
}
