package com.example.moviepreferences;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface MovieDao {
    @Insert
    void insert(Movie movie);

    @Query("SELECT * from movie_table")
    LiveData<List<Movie>> getAllMovies();

    @Query("SELECT * from movie_table where mid=:id")
    LiveData<Movie> getMovieById(int id);

    @Delete
    int delete(Movie movie);

}
