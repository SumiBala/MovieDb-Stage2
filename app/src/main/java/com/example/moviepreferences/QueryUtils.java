package com.example.moviepreferences;

import android.text.TextUtils;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

public class QueryUtils {
    private static final String LOG = QueryUtils.class.getSimpleName();
    private static final String RESULTS = "results";
    private static final String TITLE = "original_title";
    private static final String RATING = "vote_average";
    private static final String OVERVIEW = "overview";
    private static final String R_DATE = "release_date";
    private static final String P_PATH = "poster_path";
    private static final String M_ID = "id";
    private static final String CONTENT = "content";
    private static final String KEY = "key";
    private static final String R_ID = "id";

    public static ArrayList<Movie> fetchMovieData(String requestUrl) {
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        URL url = createUrl(requestUrl);
        String jsonResponse = "";
        try {
            jsonResponse = makeHttpRequest(url);
        } catch (IOException e) {
            Log.e(LOG, "Error closing InputStream", e);
        }
        return extractDataFromJson(jsonResponse);

    }

    //Creating URL.
    private static URL createUrl(String stringUrl) {
        URL url = null;
        try {
            url = new URL(stringUrl);
        } catch (MalformedURLException e) {
            Log.e(LOG, "Error with creating Url.", e);
        }
        return url;
    }

    private static String makeHttpRequest(URL url) throws IOException {
        String jsonResponse = "";
        if (url == null) {
            return jsonResponse;
        }
        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(10000);
            urlConnection.setConnectTimeout(15000);
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();
            if (urlConnection.getResponseCode() == 200) {
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            } else {
                Log.e(LOG, "Error response Code : " + urlConnection.getResponseCode());

            }

        } catch (IOException e) {
            Log.e(LOG, "Problem in retrieving data ", e);
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (inputStream != null) {
                inputStream.close();
            }
        }
        return jsonResponse;
    }

    private static String readFromStream(InputStream inputStream) throws IOException {
        StringBuilder output = new StringBuilder();
        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while (line != null) {
                output.append(line);
                line = reader.readLine();
            }
        }
        return output.toString();
    }

    private static ArrayList<Movie> extractDataFromJson(String MovieJson) {

        //If MovieJson is null and empty ,return null.
        if (TextUtils.isEmpty(MovieJson)) {
            return null;
        }
        ArrayList<Movie> MovieList = new ArrayList<>();
        try {
            JSONObject root = new JSONObject(MovieJson);
            JSONArray resultsArray = root.getJSONArray(RESULTS);
            for (int i = 0; i < resultsArray.length(); i++) {
                JSONObject currentMovie = resultsArray.getJSONObject(i);
                int movieId = currentMovie.getInt(M_ID);
                String movieTitle = currentMovie.getString(TITLE);
                long movieRating = currentMovie.getLong(RATING);
                String movieReleaseDate = currentMovie.getString(R_DATE);
                String movieOverview = currentMovie.getString(OVERVIEW);
                String moviePosterPath = currentMovie.getString(P_PATH);
                MovieList.add(new Movie(movieId, movieTitle, moviePosterPath, movieOverview, movieRating, movieReleaseDate, null));
            }
        } catch (JSONException e) {
            Log.e(LOG, "Problem in parsing JSON response results ", e);
        }
        return MovieList;
    }

    public static Reviews fetchReviewData(String mUrl) {
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        URL url = createUrl(mUrl);
        String jsonResponse = "";
        try {
            jsonResponse = makeHttpRequest(url);
        } catch (IOException e) {
            Log.e(LOG, "Error closing InputStream", e);
        }
        return extractReviewDataFromJson(jsonResponse);
    }

    private static Reviews extractReviewDataFromJson(String reviewJson) {
        //If ReviewJson is null and empty ,return null.
        if (TextUtils.isEmpty(reviewJson)) {
            return null;
        }
        int i;
        ArrayList<String> reviewList = new ArrayList<>();
        try {
            JSONObject root = new JSONObject(reviewJson);
            JSONArray resultsArray = root.getJSONArray(RESULTS);
            if (resultsArray.length() == 0) {
                return null;
            }
            for (i = 0; i < resultsArray.length(); i++) {
                JSONObject currentReview = resultsArray.getJSONObject(i);
                String content = currentReview.getString(CONTENT);
                reviewList.add(content);
            }
        } catch (JSONException e) {
            Log.e(LOG, "Problem in parsing JSON response results ", e);
        }
        return new Reviews(reviewList);
    }

    public static List<Trailers> fetchTrailerData(String mUrl) {
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        URL url = createUrl(mUrl);
        String jsonResponse = "";
        try {
            jsonResponse = makeHttpRequest(url);
        } catch (IOException e) {
            Log.e(LOG, "Error closing InputStream", e);
        }
        return extractTrailerDataFromJson(jsonResponse);

    }

    private static List<Trailers> extractTrailerDataFromJson(String trailerJson) {
        //If TrailerJson is null and empty ,return null.
        if (TextUtils.isEmpty(trailerJson)) {
            return null;
        }
        ArrayList<Trailers> trailers = new ArrayList<>();
        try {
            JSONObject root = new JSONObject(trailerJson);
            JSONArray resultsArray = root.getJSONArray(RESULTS);
            int j = 0;
            for (int i = 0; i < resultsArray.length(); i++) {
                JSONObject currentReview = resultsArray.getJSONObject(i);
                j = j + 1;
                String name = "Trailer " + (j);
                String content = currentReview.getString(KEY);
                trailers.add(new Trailers(name, content));
            }
        } catch (JSONException e) {
            Log.e(LOG, "Problem in parsing JSON response results ", e);
        }
        return trailers;
    }
}

