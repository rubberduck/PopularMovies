package com.rubberduck.udacity.popularmovies.comm;

import com.rubberduck.udacity.popularmovies.entity.MovieParcel;

import java.util.List;


public interface MovieDataRetriever {

    public List<MovieParcel> getMovies(String sortOrder, Boolean includeAdultMovies, int page);

    public MovieParcel getMovieDetail(long movieId);

    // helper methods to compose URLs for picasso
    public String composeMoviePosterImageUrl(String posterPath);

    public String composeMoviePosterLargeImageUrl(String posterPath);
}
