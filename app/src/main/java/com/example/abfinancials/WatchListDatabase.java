package com.example.abfinancials;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.abfinancials.dao.WatchListDao;
import com.example.abfinancials.entities.WatchList;

@Database(entities = {WatchList.class}, version = 2)
public abstract class WatchListDatabase extends RoomDatabase {
    public abstract WatchListDao watchListDao();

    private static volatile WatchListDatabase INSTANCE = null;

    public static synchronized WatchListDatabase getDatabase(Context context) {
        WatchListDatabase tempInstance = INSTANCE;
        if (tempInstance != null) {
            return tempInstance;
        }
        WatchListDatabase instance = Room.databaseBuilder(
                context.getApplicationContext(),
                WatchListDatabase.class,
                "watchlist_database"
        )
                .fallbackToDestructiveMigration()
                .build();
        INSTANCE = instance;
        return instance;
    }
}
