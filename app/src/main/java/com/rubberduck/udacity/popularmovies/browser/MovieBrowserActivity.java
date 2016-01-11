package com.rubberduck.udacity.popularmovies.browser;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.GridView;

import com.rubberduck.udacity.popularmovies.details.MovieDetailsActivity;
import com.rubberduck.udacity.popularmovies.R;
import com.rubberduck.udacity.popularmovies.comm.MovieDataRetriever;
import com.rubberduck.udacity.popularmovies.comm.impl.TheMovieDBMovieDataRetriever;
import com.rubberduck.udacity.popularmovies.entity.MovieParcel;

import java.util.ArrayList;
import java.util.List;


public class MovieBrowserActivity extends AppCompatActivity {

    private static final String LOG_TAG = MovieBrowserActivity.class.getSimpleName();

    private static final int REQUEST_SETTINGS = 1;

    private GridView gridview;
    private int pageRetrived = 0;
    private final int PAGE_SIZE = 20;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // set the default values as described in the official android developer docs
        // (however, this call is not required, as the defaults gets set anyways ->?!)
        PreferenceManager.setDefaultValues(getApplicationContext(), R.xml.preferences, false);

        setContentView(R.layout.activity_movie_browser);

        gridview = (GridView) findViewById(R.id.moviebrowsergrid);
        gridview.setAdapter(new PosterThumbnailAdapter(this, new ArrayList<MovieParcel>()));

        gridview.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                // only execute "end of data reached"-check when Grid is properly setup
                if(visibleItemCount != 0) {
                    // only load new movies, if end of scroll reached
                    // AND totalItemCount equals currently retrieved movies, to prevent multiple fetching
                    if (((firstVisibleItem + visibleItemCount) >= totalItemCount)
                            && (totalItemCount == (pageRetrived * PAGE_SIZE))) {
                        loadMovieData();
                    }
                }
            }
        });

        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {
                SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(
                        getApplicationContext());
                        // could not figure out how to use a custom file like this:
                        //getSharedPreferences(BROWSER_PREFERENCES_NAME, Context.MODE_PRIVATE);

                Intent showMovieDetailIntent = new Intent(v.getContext(), MovieDetailsActivity.class);
                showMovieDetailIntent.putExtra(getString(R.string.key_intent_movie_selected),
                        (MovieParcel)parent.getAdapter().getItem(position));
                startActivity(showMovieDetailIntent);
            }
        });
    }

    public void onStart() {
        super.onStart();
        loadMovieData();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main_moviebrowser_activity, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case R.id.action_settings: {
                Intent intent = new Intent();
                intent.setClassName(this, "com.rubberduck.udacity.popularmovies.browser.MovieBrowserSettingsActivity");
                startActivityForResult(intent, REQUEST_SETTINGS);
                return true;
            }
        }
        // default behavior, always execute
        return super.onOptionsItemSelected(item);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch(requestCode) {
            case REQUEST_SETTINGS: {
                if (resultCode == RESULT_OK) {
                    Boolean settingsChanged = data.getBooleanExtra(
                            getString(R.string.key_intent_settings_changed), false);
                    if (settingsChanged) {
                        clearMovieData();
                        loadMovieData();
                    }
                }
            }
        }
    }


    private void clearMovieData() {
        ((PosterThumbnailAdapter)this.gridview.getAdapter()).clearMovies();
        this.pageRetrived = 0;
    }

    private void loadMovieData() {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(
                getApplicationContext());
        String sortOrder = prefs.getString("movieBrowserSortingOrder", "NULL");
        boolean adultMoviesIncluded = prefs.getBoolean("movieBrowserIncludeAdultMovies", false);
        pageRetrived++;
        new DownloadMovieData().execute(sortOrder, ""+adultMoviesIncluded, ""+pageRetrived);
    }

    protected void updateMovieData(List<MovieParcel> movieList) {
        Log.i(LOG_TAG, "Updating data on PosterThumbnailAdapter");
        PosterThumbnailAdapter adapter = (PosterThumbnailAdapter)this.gridview.getAdapter();
        adapter.setMovies(movieList);
        adapter.notifyDataSetChanged();
        Log.i(LOG_TAG, "Data on PosterThumbnailAdapter was updated.");
    }


    private class DownloadMovieData extends AsyncTask<String, Void, List<MovieParcel>> {

        protected List<MovieParcel> doInBackground(String... inputParams){
            String sortOrder = inputParams[0];
            Boolean includeAdultMovies = Boolean.getBoolean(inputParams[1]);
            int page = Integer.parseInt(inputParams[2]);

            MovieDataRetriever retriever = new TheMovieDBMovieDataRetriever();
            List<MovieParcel> movieList = retriever.getMovies(sortOrder, includeAdultMovies, page);
            return movieList;
        }

        protected void onPostExecute(List<MovieParcel> movieList) {
            updateMovieData(movieList);
        }
    }


}
