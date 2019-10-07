package com.example.moviepreferences;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.Loader;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.moviepreferences.databinding.ActivityVideoBinding;

import java.util.List;

public class VideoActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<Trailers>>, TrailerAdapter.VideoAdapterOnClickHandler {
    ActivityVideoBinding mVideoBinding;
    TrailerAdapter mVideoAdapter;
    private String VPATH_URL;
    String QTAG = "?";
    int movieId;
    String movieTitle;
    API_Confiq confiq;
    String STAG = "/";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mVideoBinding = DataBindingUtil.setContentView(this, R.layout.activity_video);
        String videos = getString(R.string.videos);
        Intent intent = getIntent();
        movieId = intent.getIntExtra("movieId", 0);
        movieTitle = intent.getStringExtra("mTitle");
        mVideoBinding.titleMovie.movieTitle.setText(movieTitle);
        mVideoAdapter = new TrailerAdapter(this, this);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mVideoBinding.rListView.movieRv.setLayoutManager(linearLayoutManager);
        DividerItemDecoration mDividerItemDecoration = new DividerItemDecoration(mVideoBinding.rListView.movieRv.getContext(),
                linearLayoutManager.getOrientation());
        mVideoBinding.rListView.movieRv.addItemDecoration(mDividerItemDecoration);
        mDividerItemDecoration.setDrawable(getResources().getDrawable(R.drawable.dividerline));
        mVideoBinding.rListView.movieRv.setHasFixedSize(true);
        mVideoBinding.rListView.movieRv.setAdapter(mVideoAdapter);
        confiq = new API_Confiq(this);
        VPATH_URL = confiq.getMovieDbUrl() + movieId + STAG + videos + QTAG + confiq.getMovieDbApiKey();
        loadTrailerFromNet();
    }

    @Override
    public void onClick(Trailers trailers) {
        Intent intent = new Intent(this, TrailerActivity.class);
        intent.putExtra("tUrl", trailers.getKey());
        startActivity(intent);
    }

    private void loadTrailerFromNet() {
        if (confiq.netIsConnected()) {
            // Get a reference to the LoaderManager, in order to interact with loaders.
            LoaderManager loaderManager = getSupportLoaderManager();
            loaderManager.initLoader(121, null, this);
        } else {
            // Update empty state with no connection error message
            showError(getString(R.string.no_internet_connection));
        }
    }

    public void showError(String msg) {
        mVideoBinding.rListView.movieRv.setVisibility(View.GONE);
        mVideoBinding.rListView.emptyTextView.setVisibility(View.VISIBLE);
        mVideoBinding.rListView.loadingIndicator.setVisibility(View.GONE);
        mVideoBinding.rListView.emptyTextView.setText(msg);
    }

    private void showControls() {
        mVideoBinding.rListView.loadingIndicator.setVisibility(View.GONE);
        mVideoBinding.rListView.movieRv.setVisibility(View.VISIBLE);
        mVideoBinding.rListView.emptyTextView.setVisibility(View.INVISIBLE);
    }

    @NonNull
    @Override
    public Loader<List<Trailers>> onCreateLoader(int id, @Nullable Bundle args) {
        return new TrailerLoader(this, VPATH_URL);
    }

    @Override
    public void onLoadFinished(@NonNull Loader<List<Trailers>> loader, List<Trailers> data) {
        if (data.size() != 0) {
            mVideoAdapter.loadTrailers(data);
            mVideoAdapter.notifyDataSetChanged();
            showControls();
        } else {            // Set empty state text to display "No Trailers found."
            showError(getString(R.string.no_trailers));
        }
    }

    @Override
    public void onLoaderReset(@NonNull Loader<List<Trailers>> loader) {

    }
}
