package com.example.moviepreferences;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.loader.content.AsyncTaskLoader;


import java.util.List;

public class MovieLoader extends AsyncTaskLoader<List<Movie>> {
    private String mUrl;

    public MovieLoader(@NonNull Context context, String url) {
        super(context);
        mUrl = url;
    }

    @Nullable
    @Override
    public List<Movie> loadInBackground() {
        if (mUrl == null) {
            return null;
        }
        //Perform the network request, parse the response and extract data.
       return QueryUtils.fetchMovieData(mUrl);
           }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }

}
