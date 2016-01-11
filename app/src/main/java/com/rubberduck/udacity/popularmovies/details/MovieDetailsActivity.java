package com.rubberduck.udacity.popularmovies.details;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.rubberduck.udacity.popularmovies.R;
import com.rubberduck.udacity.popularmovies.comm.MovieDataRetriever;
import com.rubberduck.udacity.popularmovies.comm.impl.TheMovieDBMovieDataRetriever;
import com.rubberduck.udacity.popularmovies.entity.MovieParcel;
import com.squareup.picasso.Picasso;


public class MovieDetailsActivity extends AppCompatActivity {

    private static final String LOG_TAG = MovieDetailsActivity.class.getSimpleName();

    MovieParcel movieDetails;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);

        if(savedInstanceState != null) {
            movieDetails = savedInstanceState.getParcelable(getString(R.string.key_parcel_movie_details_saved));
            Log.i(LOG_TAG, "Retrieved saved movie details state from savedInstanceState ("
                    + movieDetails.getOriginalTitle() + ").");
        } else {
            Intent receivedIntent = this.getIntent();
            movieDetails = receivedIntent.getParcelableExtra(getString(R.string.key_intent_movie_selected));
            Log.i(LOG_TAG, "Received movie details from intent ("
                    + movieDetails.getOriginalTitle() + ").");
        }
        loadMovieData(movieDetails.getId());
    }

    private void fillUIFields() {
        ImageView posterView = (ImageView) findViewById(R.id.moviedetails_poster);
        String posterUrl = new TheMovieDBMovieDataRetriever().composeMoviePosterLargeImageUrl(
                movieDetails.getPosterPath());
        Picasso.with(this.getApplicationContext()).load(posterUrl).into(posterView);

        TextView originalTitleTextView = (TextView) findViewById(R.id.moviedetails_title);
        originalTitleTextView.setText(movieDetails.getOriginalTitle());

        TextView releaseDateTextView = (TextView) findViewById(R.id.moviedetails_releasedate);
        releaseDateTextView.setText(movieDetails.getReleaseDate());

        TextView revenueTextView = (TextView) findViewById(R.id.moviedetails_revenue);
        revenueTextView.setText(String.valueOf(movieDetails.getRevenue()));

        TextView budgetTextView = (TextView) findViewById(R.id.moviedetails_budget);
        budgetTextView.setText(String.valueOf(movieDetails.getBudget()));

        TextView averageVoteTextView = (TextView) findViewById(R.id.moviedetails_voteaverage);
        averageVoteTextView.setText(String.valueOf(movieDetails.getVoteAverage()));

        TextView synopsisTextView = (TextView) findViewById(R.id.moviedetails_synopsis);
        synopsisTextView.setText(movieDetails.getSynopsis());
    }


    private void loadMovieData(long movieId) {
        new DownloadMovieDetailData().execute(movieId);
    }

    protected void onSaveInstanceState(Bundle stateTobeSaved) {
        super.onSaveInstanceState(stateTobeSaved);
        stateTobeSaved.putParcelable(getString(R.string.key_parcel_movie_details_saved), movieDetails);
    }

    protected void onRestoreInstanceState(Bundle stateTobeLoaded) {
        super.onRestoreInstanceState(stateTobeLoaded);
        movieDetails = stateTobeLoaded.getParcelable(getString(R.string.key_parcel_movie_details_saved));
    }


    private class DownloadMovieDetailData extends AsyncTask<Long, Void, MovieParcel> {

        protected MovieParcel doInBackground(Long... inputParams){
            long movieId = inputParams[0];

            MovieDataRetriever retriever = new TheMovieDBMovieDataRetriever();
            MovieParcel movieDetails = retriever.getMovieDetail(movieId);
            return movieDetails;
        }

        protected void onPostExecute(MovieParcel extractedMovieDetails) {
            movieDetails = extractedMovieDetails;
            fillUIFields();
        }
    }

}
