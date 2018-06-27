package com.example.parul.movietheatre.Data;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

@Dao
public interface MovieDao {

    @Query("SELECT * FROM movie")
    LiveData<List<MovieDataItem>> getAll();


    @Query("SELECT COUNT(*) from movie")
    int countUsers();

    @Insert
    void insertAll(MovieDataItem... movies);

    @Insert
    void insert(MovieDataItem movie);

    @Update
    void update(MovieDataItem movie);

    @Delete
    void delete(MovieDataItem movie);

    @Query("SELECT * FROM movie WHERE id =:movie_id LIMIT 1")
    MovieDataItem searchIfMovieExists(int movie_id);

}
