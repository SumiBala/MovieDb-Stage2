package com.example.moviepreferences;


import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.Loader;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.moviepreferences.database.MovieViewModel;
import com.example.moviepreferences.databinding.ActivityDetailBinding;
import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DetailActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Reviews> {

    private ActivityDetailBinding mBinding;
    private Movie dataMovie;
    private boolean movieFromFav;
    private Intent replyIntent;
    private ReviewAdapter mAdapter;
    private String RPATH_URL;
    private Reviews reviewList;
    private int movieId;
    private API_Confiq confiq;
    private String year;
    public static final int TRAILER_ACTIVITY_REQUEST_CODE = 1;
    private static final String LOG = DetailActivity.class.getSimpleName();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        confiq = new API_Confiq(this);
        String REVIEWS = getString(R.string.reviews);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_detail);
        dataMovie = getIntent().getParcelableExtra("movieData");
        movieFromFav = getIntent().getBooleanExtra("isFavList", false);

        if (movieFromFav) {
            mBinding.detailInfo.trailerBtn.setVisibility(View.GONE);
            mBinding.detailInfo.favBtn.setImageResource(R.drawable.ic_unfavourite);
        } else {
            mBinding.detailInfo.trailerBtn.setVisibility(View.VISIBLE);
            mBinding.detailInfo.favBtn.setImageResource(R.drawable.ic_add_favorite);
        }
        if (dataMovie != null) {
            showControls(true);
            movieId = dataMovie.getId();
            mBinding.titleMovie.movieTitle.setText(dataMovie.getTitle());
            try {
                year = check(dataMovie.getReleaseDate());
            } catch (ParseException r) {
                Log.e(LOG, "Parsing Error", r);
            }
            mBinding.detailInfo.releaseDateTv.setText(year);
            mBinding.detailInfo.overviewTv.setText(dataMovie.getOverview());
            mBinding.detailInfo.ratingTv.setText(String.format("%s/10", String.valueOf(dataMovie.getRating())));
            String path = confiq.getImageDbUrl() + dataMovie.getImage();
            Picasso.with(this)
                    .load(path)
                    .error(R.drawable.placholder)
                    .placeholder(R.drawable.placholder)
                    .into(mBinding.detailInfo.posterImage);
        } else {
            showControls(false);
        }
        //Review
        String STAG = getString(R.string.slashTag);
        String QTAG = getString(R.string.qTag);
        RPATH_URL = confiq.getMovieDbUrl() + movieId + STAG + REVIEWS + QTAG + confiq.getMovieDbApiKey();
        mBinding.rListView.movieRv.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mBinding.rListView.movieRv.setLayoutManager(linearLayoutManager);
        mAdapter = new ReviewAdapter(DetailActivity.this);
        mBinding.rListView.movieRv.setAdapter(mAdapter);
        mBinding.rListView.loadingIndicator.setVisibility(View.VISIBLE);
        MovieViewModel movieViewModel = ViewModelProviders.of(this).get(MovieViewModel.class);
        movieViewModel.getMovieById(movieId).observe(this, new Observer<Movie>() {
            @Override
            public void onChanged(Movie movie) {
                if (movieFromFav) {
                    //Movies From Room.
                    if (movie != null) {
                        if (movie.getReview() != null) {
                            showControls();
                            mAdapter.loadReviews(dataMovie.getReview().getReviews());
                            mAdapter.notifyDataSetChanged();
                        } else {
                            showError(getString(R.string.no_reviews));
                        }
                    }
                } else {
                    //Movies From Net.
                    loadReviewsFromNet();
                }
            }
        });

        mBinding.detailInfo.favBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                replyIntent = new Intent();
                if (movieFromFav) {
                    replyIntent.putExtra("favMovie", dataMovie);
                    setResult(RESULT_CANCELED, replyIntent);
                } else {
                    dataMovie.setReview(reviewList);
                    replyIntent.putExtra("favMovie", dataMovie);
                    setResult(RESULT_OK, replyIntent);
                }
                finish();
            }
        });
        mBinding.detailInfo.trailerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mBinding.rListView.loadingIndicator.setVisibility(View.VISIBLE);
                Intent intent = new Intent(DetailActivity.this, VideoActivity.class);
                intent.putExtra("mTitle", dataMovie.getTitle());
                intent.putExtra("movieId", dataMovie.getId());
                startActivityForResult(intent, TRAILER_ACTIVITY_REQUEST_CODE);
            }
        });
    }

    public void showControls(boolean isAvail) {
        if (isAvail) {
            mBinding.detailInfo.infoLayout.setVisibility(View.VISIBLE);
        } else {
            mBinding.detailInfo.infoLayout.setVisibility(View.INVISIBLE);
            mBinding.titleMovie.movieTitle.setText(R.string.no_movies);
        }
    }

    @NonNull
    @Override
    public Loader<Reviews> onCreateLoader(int id, @Nullable Bundle args) {
        return new ReviewLoader(this, RPATH_URL);
    }

    @Override
    public void onLoadFinished(@NonNull Loader<Reviews> loader, Reviews data) {
        if (data != null) {
            reviewList = data;
            mAdapter.loadReviews(data.getReviews());
            mAdapter.notifyDataSetChanged();
            showControls();
        } else {           // Set empty state text to display "No Reviews found."
            showError(getString(R.string.no_reviews));
        }
    }

    @Override
    public void onLoaderReset(@NonNull Loader<Reviews> loader) {

    }

    private void loadReviewsFromNet() {
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
        mBinding.rListView.movieRv.setVisibility(View.GONE);
        mBinding.rListView.emptyTextView.setVisibility(View.VISIBLE);
        mBinding.rListView.loadingIndicator.setVisibility(View.GONE);
        mBinding.rListView.emptyTextView.setText(msg);
    }

    private void showControls() {
        mBinding.rListView.loadingIndicator.setVisibility(View.GONE);
        mBinding.rListView.movieRv.setVisibility(View.VISIBLE);
        mBinding.rListView.emptyTextView.setVisibility(View.INVISIBLE);
    }

    public String check(String datestring) throws ParseException {
        @SuppressLint("SimpleDateFormat") SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy");
        Date d = dateFormat.parse(datestring);
        return dateFormat.format(d);
    }
}
