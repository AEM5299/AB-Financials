package com.example.abfinancials.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.abfinancials.entities.WatchList;

import java.util.List;

@Dao
public interface WatchListDao {
    @Query("SELECT * FROM watchlist")
    List<WatchList> getAll();

    @Query("SELECT * FROM watchlist")
    LiveData<List<WatchList>> getAllLive();

    @Query("SELECT * FROM watchlist where id = (:id)")
    WatchList find(int id);

    @Insert
    void insert(WatchList watchList);



    @Delete
    void delete(WatchList item);
}
