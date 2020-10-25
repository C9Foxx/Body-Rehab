package com.example.bodyrehab.UserDB;

import androidx.room.Embedded;
import androidx.room.Relation;

import java.util.List;

public class PlaylistWithVideos {

    @Embedded
    public Playlist playlist;

    @Relation(
            parentColumn = "playlist_id",
            entityColumn = "container_id",
            entity = Video.class
    )
    public List<Video> videos;

    public PlaylistWithVideos(Playlist playlist, List<Video> videos) {
        this.playlist = playlist;
        this.videos = videos;
    }

    public Playlist getPlaylist() {
        return playlist;
    }

    public void setPlaylist(Playlist playlist) {
        this.playlist = playlist;
    }

    public List<Video> getVideos() {
        return videos;
    }

    public void setVideos(List<Video> videos) {
        this.videos = videos;
    }
}
