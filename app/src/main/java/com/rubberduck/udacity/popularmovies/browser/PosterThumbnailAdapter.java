package com.rubberduck.udacity.popularmovies.browser;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import com.rubberduck.udacity.popularmovies.comm.impl.TheMovieDBMovieDataRetriever;
import com.rubberduck.udacity.popularmovies.entity.MovieParcel;
import com.squareup.picasso.Picasso;

import java.util.List;


public class PosterThumbnailAdapter extends BaseAdapter {
    private Context context;
    private List<MovieParcel> listOfMovies;

    private static final String LOG_TAG = PosterThumbnailAdapter.class.getSimpleName();


    public PosterThumbnailAdapter(Context c, List<MovieParcel> initList) {
        this.context = c;
        this.listOfMovies = initList;
    }

    @Override
    public int getCount() {
        return getMovies().size();
    }

    public Object getItem(int position) {
        return getMovies().get(position);
    }

    public long getItemId(int position) {
        Log.i(LOG_TAG, "GetItemId was called.");
        return getMovies().get(position).getId();
    }

    // create a new ImageView for each item referenced by the Adapter
    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView imageView;
        if (convertView == null) {
            // if the convertView is is null then it means it is not being recycled
            // therefore it should be initially filled with an imageView
            imageView = new ImageView(context);
            imageView.setLayoutParams(new GridView.LayoutParams(342, 462));
            imageView.setPadding(4, 4, 4, 4);
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);

        } else {
            imageView = (ImageView) convertView;
        }
        String posterUrl = new TheMovieDBMovieDataRetriever().composeMoviePosterImageUrl(
                getMovies().get(position).getPosterPath());
        Picasso.with(this.context).load(posterUrl).into(imageView);


        return imageView;
    }

    public List<MovieParcel> getMovies() {
        return listOfMovies;
    }

    public void clearMovies() {
        this.listOfMovies.clear();
    }

    public void setMovies(List<MovieParcel> movieParcels) {
        this.listOfMovies.addAll(movieParcels);
        Log.i(LOG_TAG, "New List of MovieParcels was set on adapter.");
    }

}