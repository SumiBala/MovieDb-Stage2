package com.example.moviepreferences;


import android.os.Bundle;
import android.view.View;

import androidx.databinding.DataBindingUtil;

import com.example.moviepreferences.databinding.ActivityTrailerBinding;
import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;


public class TrailerActivity extends YouTubeBaseActivity {


    API_Confiq config;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityTrailerBinding mTBinding = DataBindingUtil.setContentView(this, R.layout.activity_trailer);
        final String tUrl = getIntent().getStringExtra("tUrl");
        config = new API_Confiq(this);
        YouTubePlayer.OnInitializedListener onInitializedListener = new YouTubePlayer.OnInitializedListener() {
            @Override
            public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean b) {
                youTubePlayer.cueVideo(tUrl);
            }

            @Override
            public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {
            }
        };
        if (config.netIsConnected()) {
            mTBinding.emptytxt.setVisibility(View.GONE);
            mTBinding.youtubePlayerView.initialize(config.getYouTubeApiKey(), onInitializedListener);
        } else {
            mTBinding.emptytxt.setVisibility(View.VISIBLE);
        }
        mTBinding.backView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}
