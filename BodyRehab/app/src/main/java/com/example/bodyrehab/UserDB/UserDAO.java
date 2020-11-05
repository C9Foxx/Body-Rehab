package com.example.bodyrehab.UserDB;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Transaction;
import androidx.room.Update;

import com.example.bodyrehab.models.VideoYT;

import java.util.ArrayList;
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

    @Update
    void UpdatePlaylistName(Playlist playlist);

    @Transaction
    @Delete
    void DeletePlaylist(Playlist playlist);

    @Insert
    void InsertPlaylist(List<Playlist> playlist);

    @Query("SELECT * FROM " + Constants.TABLE_NAME_PLAYLIST + " where creator_id =:user_id and playlist_name =:playlist_name")
    Playlist SearchPlaylistByName(long user_id, String playlist_name);

    @Query("SELECT * FROM " + Constants.TABLE_NAME_PLAYLIST + " where playlist_id =:container_id")
    Playlist SearchPlaylistByVideo(long container_id);

    @Transaction
    @Query("SELECT * FROM " + Constants.TABLE_NAME_USERS + " where user_id =:creator_id")
    UserAndPlaylist SearchByUserAndPlaylist (long creator_id);

    @Transaction
    @Query("SELECT * FROM " + Constants.TABLE_NAME_PLAYLIST + " where creator_id =:user_id and playlist_name =:playlist_name")
    PlaylistWithVideos LoadVideosOnPlaylist (long user_id, String playlist_name);

    @Query("SELECT * FROM " + Constants.TABLE_NAME_VIDEOS + " where video_id =:video_id")
    Video SearchVideo (long video_id);

    @Insert
    void InsertVideo(List<Video> video);

    @Update
    void UpdateVideoTitle(Video video);

    @Delete
    void DeleteVideo(Video video);

    @Delete
    void DeleteVideoList(List <Video> videoList);

    @Transaction
    @Query("SELECT * FROM " + Constants.TABLE_NAME_USERS + " where user_id =:container_id")
    UserAndVideo SearchByUserAndVideo (long container_id);

    @Transaction
    @Query("SELECT * FROM " + Constants.TABLE_NAME_USERS + " where user_id =:creator_id")
    UserWithPlaylistsAndSongs SearchPlaylistsByUser (long creator_id);

    @Transaction
    @Query("SELECT * FROM " + Constants.TABLE_NAME_PLAYLIST + " where creator_id =:user_id")
    List<Playlist>  loadUserPlaylits (long user_id);


}
