package com.example.moviepreferences.database;

import android.app.Application;

import androidx.lifecycle.LiveData;

import android.os.AsyncTask;

import com.example.moviepreferences.Movie;
import com.example.moviepreferences.MovieDao;
import com.example.moviepreferences.MovieDatabase;

import java.util.List;

public class MovieRepository {
    private MovieDao movieDao;
    private LiveData<List<Movie>> mAllMovies;


    public MovieRepository(Application application) {
        MovieDatabase db = MovieDatabase.getDatabase(application);
        movieDao = db.movieDao();
        mAllMovies = movieDao.getAllMovies();

    }

    LiveData<List<Movie>> getAllMovies() {
        return mAllMovies;
    }


    public void insert(Movie movie) {
        new insertAsyncTask(movieDao).execute(movie);
    }

    public LiveData<Movie> getMovieById(int id) {
        return movieDao.getMovieById(id);
    }

    public void delete(Movie movie) {
        new deleteAsyncTask(movieDao).execute(movie);
    }

    private static class insertAsyncTask extends AsyncTask<Movie, Void, Void> {
        private MovieDao mAsyncTaskDao;

        insertAsyncTask(MovieDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(Movie... movies) {
            mAsyncTaskDao.insert(movies[0]);
            return null;
        }
    }

    private static class deleteAsyncTask extends AsyncTask<Movie, Void, Void> {
        private MovieDao mAsyncTaskDao;

        deleteAsyncTask(MovieDao movieDao) {
            mAsyncTaskDao = movieDao;
        }

        @Override
        protected Void doInBackground(Movie... ids) {
            System.out.println("Repo delete");
            mAsyncTaskDao.delete(ids[0]);
            return null;
        }
    }
}
