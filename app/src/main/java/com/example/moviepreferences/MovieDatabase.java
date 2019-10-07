package com.example.moviepreferences;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;


@Database(entities = {Movie.class}, version = 1, exportSchema = false)
@TypeConverters({Converters.class})
public abstract class MovieDatabase extends RoomDatabase {
    public abstract MovieDao movieDao();

    // Make the MovieRoomDatabase a singleton to prevent having
    // multiple instances of the database opened at the same time.
    private static volatile MovieDatabase INSTANCE;

    public static MovieDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (MovieDatabase.class) {
                if (INSTANCE == null) {
                    //Create database here.
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            MovieDatabase.class, "movie_db")
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}
