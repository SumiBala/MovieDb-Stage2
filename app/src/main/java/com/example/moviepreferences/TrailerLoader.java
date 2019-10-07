package com.example.moviepreferences;

import android.content.Context;

import androidx.annotation.Nullable;
import androidx.loader.content.AsyncTaskLoader;


import java.util.List;

class TrailerLoader extends AsyncTaskLoader<List<Trailers>> {
    private String mUrl;

    public TrailerLoader(Context context, String url) {
        super(context);
        mUrl = url;
    }

    @Nullable
    @Override
    public List<Trailers> loadInBackground() {
        if (mUrl == null) {
            return null;
        }
        //Perform the network request, parse the response and extract data.
        return  QueryUtils.fetchTrailerData(mUrl);
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }

}
