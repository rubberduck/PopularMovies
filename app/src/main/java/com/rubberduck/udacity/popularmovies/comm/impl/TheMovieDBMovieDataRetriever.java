package com.rubberduck.udacity.popularmovies.comm.impl;

import com.rubberduck.udacity.popularmovies.entity.MovieParcel;
import com.rubberduck.udacity.popularmovies.comm.MovieDataRetriever;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;


public class TheMovieDBMovieDataRetriever implements MovieDataRetriever {

    private static final String LOG_TAG = TheMovieDBMovieDataRetriever.class.getSimpleName();

    // remove value of API Key before making public (legal reasons)
    private final String THEMOVIEDB_API_KEY = "<please paste your own API key here>";

    private static final String URL_SLASH = "/";
    private static final String THEMOVIEDB_API_URL = "http://api.themoviedb.org/";
    private static final String THEMOVIEDB_POSTER_URL = "http://image.tmdb.org/t/p/w342";
    private static final String THEMOVIEDB_LARGE_POSTER_URL = "http://image.tmdb.org/t/p/w780";
    private static final String THEMOVIEDB_API_VERSION = "3";
    private static final int THEMOVIEDB_READTIMEOUT = 4000;
    private static final int THEMOVIEDB_CONNECTIONTIMEOUT = 2000;
    private static final String THEMOVIEDB_API_REQUEST_LIST = "discover/movie";
    private static final String THEMOVIEDB_API_REQUEST_DETAIL = "movie";



    public List<MovieParcel> getMovies(String sortOrder, Boolean includeAdultMovies, int page) {
        if(page >= 1000) return new ArrayList<MovieParcel>();

        URL requestURL = null;
        try {
            requestURL = buildTheMovieDBListUrl(sortOrder, includeAdultMovies, page);
        } catch (MalformedURLException mue) {
            mue.printStackTrace();
            return null;
        }

        String rawJsonString = retrieveJsonString(requestURL);
        List<MovieParcel> resultMovies = parseListForMovieParcels(rawJsonString);
        //Log.i(LOG_TAG, "Found " + resultMovies.size() + " movies");

        return resultMovies;
    }

    public String retrieveJsonString(URL requestURL){
        String returnedJSONString = "";
        InputStream inputStream = null;

        try {
            HttpURLConnection connection = (HttpURLConnection) requestURL.openConnection();
            connection.setConnectTimeout(this.THEMOVIEDB_CONNECTIONTIMEOUT);
            connection.setReadTimeout(this.THEMOVIEDB_READTIMEOUT);
            connection.setRequestMethod("GET");
            connection.addRequestProperty("Accept", "application/json");
            connection.setDoInput(true);
            System.out.println("**** Calling "+requestURL.toString());
            connection.connect();

            int responseCode = connection.getResponseCode();
            inputStream = connection.getInputStream();

            Reader inputReader = new InputStreamReader(inputStream, "UTF-8");
            BufferedReader bufferedReader = new BufferedReader(inputReader);
            returnedJSONString = bufferedReader.readLine();

        } catch (Exception exception) {
            //Log.e(LOG_TAG, "Connection code threw an error: ");
            exception.printStackTrace();
        }
        return returnedJSONString;
    }

    private URL buildTheMovieDBListUrl(String sortOrder, Boolean includeAdultMovies, int page)
            throws MalformedURLException {
        StringBuilder stringUrl = new StringBuilder();
        stringUrl.append(this.THEMOVIEDB_API_URL);
        stringUrl.append(this.THEMOVIEDB_API_VERSION);
        stringUrl.append(this.URL_SLASH);
        stringUrl.append(this.THEMOVIEDB_API_REQUEST_LIST);
        stringUrl.append("?");
        stringUrl.append("api_key=" + this.THEMOVIEDB_API_KEY);
        stringUrl.append("&");
        stringUrl.append("include_adult=" + includeAdultMovies);
        stringUrl.append("&");
        stringUrl.append("sort_by=" + sortOrder);
        stringUrl.append("&");
        stringUrl.append("page=" + page);
        URL theRequestUrl = new URL(stringUrl.toString());
        return theRequestUrl;
    }

    private URL buildTheMovieDBDetailUrl(long movieId)
            throws MalformedURLException {
        StringBuilder stringUrl = new StringBuilder();
        stringUrl.append(this.THEMOVIEDB_API_URL);
        stringUrl.append(this.THEMOVIEDB_API_VERSION);
        stringUrl.append(this.URL_SLASH);
        stringUrl.append(this.THEMOVIEDB_API_REQUEST_DETAIL);
        stringUrl.append(this.URL_SLASH);
        stringUrl.append(movieId);
        stringUrl.append("?");
        stringUrl.append("api_key=" + this.THEMOVIEDB_API_KEY);
        URL theRequestUrl = new URL(stringUrl.toString());
        return theRequestUrl;
    }

    public List<MovieParcel> parseListForMovieParcels(String rawJsonString) {

        List<MovieParcel> resultMovies = new ArrayList<>();

        try {
            JSONObject jsonObject = new JSONObject(rawJsonString);
            JSONArray arrayOfMovieResults = jsonObject.getJSONArray("results");

            for(int i=0; i< arrayOfMovieResults.length(); i++) {
                MovieParcel movieParcel = parseMovieParcelFrom(arrayOfMovieResults.getJSONObject(i));
                resultMovies.add(movieParcel);
                System.out.println("Found movie " + movieParcel.getOriginalTitle()+" in inputstream");
            }
        } catch (JSONException jsone) {
            jsone.printStackTrace();
        }

        return resultMovies;
    }

    private MovieParcel parseMovieParcelFrom(JSONObject jsonObject) {
        MovieParcel extractedMovie = new MovieParcel();
        try {
            extractedMovie.setId(Long.parseLong(jsonObject.getString("id")));
            extractedMovie.setAdultMovie((boolean) jsonObject.getBoolean("adult"));
            extractedMovie.setReleaseDate(jsonObject.getString("release_date"));
            extractedMovie.setOriginalTitle(jsonObject.getString("original_title"));
            extractedMovie.setPosterPath(jsonObject.getString("poster_path"));
            extractedMovie.setVoteAverage(jsonObject.getDouble("vote_average"));
            extractedMovie.setSynopsis(jsonObject.getString("overview"));
        } catch (JSONException jsone) {
            jsone.printStackTrace();
        }
        return extractedMovie;
    }


    private MovieParcel parseForMovieParcelDetails(String rawJsonString) {
        MovieParcel extractedMovie = new MovieParcel();
        try {
            JSONObject jsonObject = new JSONObject(rawJsonString);
            extractedMovie = parseMovieParcelFrom(jsonObject);

            // add additional data to fields
            extractedMovie.setRevenue(jsonObject.getLong("revenue"));
            extractedMovie.setBudget(jsonObject.getLong("budget"));
        } catch (JSONException jsone) {
            jsone.printStackTrace();
        }

        return extractedMovie;
    }

    public String composeMoviePosterImageUrl(String posterPath) {
        return TheMovieDBMovieDataRetriever.THEMOVIEDB_POSTER_URL + posterPath;
    }

    public String composeMoviePosterLargeImageUrl(String posterPath) {
        return TheMovieDBMovieDataRetriever.THEMOVIEDB_LARGE_POSTER_URL + posterPath;
    }

    public MovieParcel getMovieDetail(long movieId) {
        URL requestURL = null;
        try {
            requestURL = buildTheMovieDBDetailUrl(movieId);
        } catch (MalformedURLException mue) {
            mue.printStackTrace();
            return null;
        }
        String rawJsonString = retrieveJsonString(requestURL);
        MovieParcel resultMovie = parseForMovieParcelDetails(rawJsonString);
        //Log.i(LOG_TAG, "Found " + resultMovies.size() + " movies");
        return resultMovie;
    }

}
