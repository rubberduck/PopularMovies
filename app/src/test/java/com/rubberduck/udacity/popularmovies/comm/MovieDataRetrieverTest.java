package com.rubberduck.udacity.popularmovies.comm;

import com.rubberduck.udacity.popularmovies.comm.impl.TheMovieDBMovieDataRetriever;
import com.rubberduck.udacity.popularmovies.entity.MovieParcel;

import org.junit.Test;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.List;


public class MovieDataRetrieverTest {

    @Test
    public void basicGetMoviesTest() {

        String sortOrder = "popularity.desc";
        boolean includeAdultMovies = true;
        MovieDataRetriever retriever = new TheMovieDBMovieDataRetriever();

        List<MovieParcel> moviesTest = retriever.getMovies(sortOrder, includeAdultMovies, 1);

        assertNotNull("List of movies should not be null", moviesTest);
        assertNotNull("Title of first movie retrieved should not be null",
                moviesTest.get(0).getOriginalTitle());
    }

    @Test
    public void communicationGetMoviesTest() {

        String sortOrder = "vote_average.desc";
        Boolean includeAdultMovies = false;
        MovieDataRetriever retriever = new TheMovieDBMovieDataRetriever();

        List<MovieParcel> moviesTest = retriever.getMovies(sortOrder, includeAdultMovies, 1);

        assertNotNull("List of movies should not be null", moviesTest);
        assertTrue("List of movies should not be empty", moviesTest.size() > 0);
        assertNotNull("Title of first movie retrieved should not be null",
                moviesTest.get(0).getOriginalTitle());
    }
}
