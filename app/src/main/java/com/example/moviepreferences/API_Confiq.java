package com.example.moviepreferences;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class API_Confiq {
    private Context mContext;

    public API_Confiq(Context context) {
        mContext = context;
    }

    String getYouTubeApiKey() {
        return mContext.getString(R.string.youtube_APIKey);
    }

    String getMovieDbUrl() {

        return mContext.getString(R.string.movieDbUrl);
    }

    String getImageDbUrl() {
        return mContext.getString(R.string.imgPathUrl);
    }

    String getMovieDbApiKey() {
        return mContext.getString(R.string.APIKey);
    }


    public boolean netIsConnected() {
        // Get a reference to the ConnectivityManager to check state of network connectivity.
        ConnectivityManager connectivityManager = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        // Get details on the currently active default data network.
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        // If there is a network connection, fetch data
        return networkInfo != null && networkInfo.isConnected();
    }
}
