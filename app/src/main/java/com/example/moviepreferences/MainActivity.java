package com.example.moviepreferences;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.Loader;
import androidx.preference.PreferenceManager;
import androidx.recyclerview.widget.GridLayoutManager;

import android.view.Menu;
import android.view.MenuItem;
import android.view.View;


import com.example.moviepreferences.database.MovieViewModel;
import com.example.moviepreferences.databinding.ActivityMainBinding;

import java.util.List;
import java.util.Random;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<Movie>>, MovieAdapter.MovieAdapterOnClickHandler, SharedPreferences.OnSharedPreferenceChangeListener {

    private String PATH_URL;
    private MovieAdapter mAdapter;
    private int rdm;
    private String viewType;
    public static final int DETAIL_ACTIVITY_REQUEST_CODE = 1;
    private MovieViewModel movieViewModel;
    private boolean favMovie = false;
    private ActivityMainBinding mainBinding;
    private API_Confiq confiq;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mainBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        confiq = new API_Confiq(this);
        setupSharedPreferences();
        String QTAG = getString(R.string.qTag);
        PATH_URL = confiq.getMovieDbUrl() + viewType + QTAG + confiq.getMovieDbApiKey();
        //Generate Random value for loader.
        Random random = new Random();
        //Used For Random values
        int min = 20;
        int max = 80;
        rdm = random.nextInt((max - min) + 1);
        // RecyclerView's size is not affected by the adapter contents.
        mainBinding.rListView.movieRv.setHasFixedSize(true);
        //Change the columns due to orientation.
        GridLayoutManager gridLayoutManager;
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            gridLayoutManager = new GridLayoutManager(this, 2);
        } else {
            gridLayoutManager = new GridLayoutManager(this, 4);
        }
        mainBinding.rListView.movieRv.setLayoutManager(gridLayoutManager);
        mAdapter = new MovieAdapter(this, this);
        mainBinding.rListView.movieRv.setAdapter(mAdapter);
        movieViewModel = ViewModelProviders.of(this).get(MovieViewModel.class);
        movieViewModel.getAllMovies().observe(this, new Observer<List<Movie>>() {
            @Override
            public void onChanged(List<Movie> movies) {
                if (viewType.equals(getString(R.string.favourite))) {
                    if (movies.size() == 0) {
                        showError("No Fav movies");
                    } else {
                        showControls();
                        mAdapter.loadMovies(movies);
                        mAdapter.notifyDataSetChanged();
                        favMovie = true;
                    }
                } else {
                    loadMoviesFromNet();
                    favMovie = false;
                }
            }
        });
        if (viewType.equals( getString(R.string.popular))) {
            setTitle(getString(R.string.titlePopular));
        } else if (viewType.equals(getString(R.string.top_rated))) {
            setTitle(getString(R.string.titleToprated));
        } else {
            setTitle(getString(R.string.titleFavourite));
        }
    }

    private void showControls() {
        mainBinding.rListView.loadingIndicator.setVisibility(View.GONE);
        mainBinding.rListView.movieRv.setVisibility(View.VISIBLE);
        mainBinding.rListView.emptyTextView.setVisibility(View.INVISIBLE);
    }

    private void setupSharedPreferences() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        loadViewTypeFromPreferences(sharedPreferences);
        // Register the listener
        sharedPreferences.registerOnSharedPreferenceChangeListener(this);
    }

    private void loadViewTypeFromPreferences(SharedPreferences sharedPreferences) {
        viewType = sharedPreferences.getString(getString(R.string.key), getString(R.string.top_rated));
    }

    private void loadMoviesFromNet() {
        if (confiq.netIsConnected()) {
            // Get a reference to the LoaderManager, in order to interact with loaders.
            LoaderManager loaderManager = getSupportLoaderManager();
            loaderManager.initLoader(rdm, null, this);
        } else {
            // Update empty state with no connection error message
            showError(getString(R.string.no_internet_connection));
        }
    }

    @NonNull
    @Override
    public Loader<List<Movie>> onCreateLoader(int i, @Nullable Bundle bundle) {
        return new MovieLoader(this, PATH_URL);
    }

    @Override
    public void onLoadFinished(@NonNull Loader<List<Movie>> loader, List<Movie> movies) {
        // Hide loading indicator because the data has been loaded
        if (movies!=null&&movies.size() != 0) {
            mAdapter.loadMovies(movies);
            mAdapter.notifyDataSetChanged();
            showControls();
        } else {            // Set empty state text to display "No Movies found."
            showError(getString(R.string.no_movies));
        }
    }

    public void showError(String msg) {
        mainBinding.rListView.movieRv.setVisibility(View.GONE);
        mainBinding.rListView.emptyTextView.setVisibility(View.VISIBLE);
        mainBinding.rListView.loadingIndicator.setVisibility(View.GONE);
        mainBinding.rListView.emptyTextView.setText(msg);
    }

    @Override
    public void onLoaderReset(@NonNull Loader<List<Movie>> loader) {

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_settings) {
            Intent startSettingsActivity = new Intent(this, SettingsActivity.class);
            startActivity(startSettingsActivity);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(Movie movie) {
        Intent intent = new Intent(this, DetailActivity.class);
        intent.putExtra("movieData", movie);
        intent.putExtra("isFavList", favMovie);
        startActivityForResult(intent, DETAIL_ACTIVITY_REQUEST_CODE);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if (key.equals(getString(R.string.key))) {
            loadViewTypeFromPreferences(sharedPreferences);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == DETAIL_ACTIVITY_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                final Movie favMovie;
                if (data != null) {
                    favMovie = data.getParcelableExtra("favMovie");
                    movieViewModel.getMovieById(favMovie.getId()).observe(this, new Observer<Movie>() {
                        @Override
                        public void onChanged(Movie movie) {
                            if (movie == null) {
                                movieViewModel.insert(favMovie);
                            }
                        }

                    });
                }
            } else if (resultCode == RESULT_CANCELED) {
                if (data != null) {
                    Movie favMovie = data.getParcelableExtra("favMovie");
                    movieViewModel.delete(favMovie);
                }
            }
        }
    }
}
