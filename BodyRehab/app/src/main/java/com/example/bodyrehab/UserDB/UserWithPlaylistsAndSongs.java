package com.example.bodyrehab.UserDB;

import androidx.room.Embedded;
import androidx.room.Relation;

import java.util.List;

public class UserWithPlaylistsAndSongs {

    @Embedded
    public User user;

    @Relation(
            parentColumn = "user_id",
            entityColumn = "creator_id",
            entity = Playlist.class
    )
    public List<PlaylistWithVideos> playlists;

    public UserWithPlaylistsAndSongs(User user, List<PlaylistWithVideos> playlists) {
        this.user = user;
        this.playlists = playlists;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public List<PlaylistWithVideos> getPlaylists() {
        return playlists;
    }

    public void setPlaylists(List<PlaylistWithVideos> playlists) {
        this.playlists = playlists;
    }
}
