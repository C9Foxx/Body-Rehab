package com.example.bodyrehab.UserDB;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity(tableName = Constants.TABLE_NAME_PLAYLIST)
public class Playlist implements Serializable {

    @PrimaryKey(autoGenerate = true)
    private long playlist_id;

    private long creator_id;
    private String playlist_name;
    private int size;

    public Playlist(String playlist_name) {
        this.playlist_name = playlist_name;
    }

    public long getPlaylist_id() {
        return playlist_id;
    }

    public void setPlaylist_id(long playlist_id) {
        this.playlist_id = playlist_id;
    }

    public long getCreator_id() {
        return creator_id;
    }

    public void setCreator_id(long creator_id) {
        this.creator_id = creator_id;
    }

    public String getPlaylist_name() {
        return playlist_name;
    }

    public void setPlaylist_name(String playlist_name) {
        this.playlist_name = playlist_name;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }
}
