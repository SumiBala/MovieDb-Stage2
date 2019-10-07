package com.example.moviepreferences;


import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.loader.content.AsyncTaskLoader;

public class ReviewLoader extends AsyncTaskLoader<Reviews> {
    private String mUrl;



    public ReviewLoader(@NonNull Context context, String url) {
        super(context);
        mUrl = url;
    }

    @Nullable
    @Override
    public Reviews loadInBackground() {
        if (mUrl == null) {
            return null;
        }
        //Perform the network request, parse the response and extract data.
       return QueryUtils.fetchReviewData(mUrl);
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }

}
