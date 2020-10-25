package com.example.bodyrehab.UserDB;


import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {User.class, Playlist.class, Video.class}, version = 1)
public abstract class UserDataBase extends RoomDatabase {

    public abstract UserDAO getUserDao();

    private static UserDataBase INSTANCE;

    public static synchronized UserDataBase getINSTANCE(Context context){
        if(INSTANCE == null){
            INSTANCE = buildDatabaseInstance(context);
        }
        return INSTANCE;
    }

    private static UserDataBase buildDatabaseInstance(Context context) {

        return Room.databaseBuilder(context,
                UserDataBase.class,
                Constants.DB_NAME).allowMainThreadQueries().build();
    }
}
