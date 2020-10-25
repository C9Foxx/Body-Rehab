package com.example.bodyrehab.UserDB;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Transaction;
import androidx.room.Update;

import com.example.bodyrehab.models.VideoYT;

import java.util.List;

@Dao
public interface UserDAO {

    @Query("SELECT * FROM " + Constants.TABLE_NAME_USERS + " where email =:user_email")
    User SearchByEmail(String user_email);

    @Transaction
    @Insert
    long InsertUser(User user);

    @Update
    void UpdateUser(User user);

    @Insert
    void InsertOnePlaylist(Playlist playlist);

    @Insert
    void InsertPlaylist(List<Playlist> playlist);

    @Query("SELECT * FROM " + Constants.TABLE_NAME_PLAYLIST + " where creator_id =:user_id and playlist_name =:playlist_name")
    Playlist SearchPlaylistByName(long user_id, String playlist_name);

    @Transaction
    @Query("SELECT * FROM " + Constants.TABLE_NAME_USERS + " where user_id =:creator_id")
    UserAndPlaylist SearchByUserAndPlaylist (long creator_id);

    @Insert
    void InsertVideo(List<Video> video);

    @Transaction
    @Query("SELECT * FROM " + Constants.TABLE_NAME_USERS + " where user_id =:container_id")
    UserAndVideo SearchByUserAndVideo (long container_id);

    @Transaction
    @Query("SELECT * FROM " + Constants.TABLE_NAME_USERS + " where user_id =:creator_id")
    UserWithPlaylistsAndSongs SearchPlaylistsByUser (long creator_id);

}
