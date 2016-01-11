Notes on Submission of Popular Movies, Part 1
=============================================

Before Executing App
--------------------

-- The theMovieDB.org API key had to be removed when storing the soruce code in Github. You need to place your own API key in the class TheMovieDBMovieDataRetriever in the package com.rubberduck.udacity.popularmovies.comm.impl before executing the App. 


Resources Consulted
-------------------

-- API documentation: developer.android.com

-- ViewHolder: After makign up my own design, I looked at a couple of github projects. However, mostly this was more confusing than helpful. Someone used a ViewHolder class which led me to the ViewHolder pattern. I looked it up on developer.android.com and decided to use it in my design. More from here: https://www.codeofaninja.com/2013/09/android-viewholder-pattern-example.html

-- Excellent information on concepts of views: http://lucasr.org/2012/04/05/performance-tips-for-androids-listview/

-- Fitting the images to the grid: http://www.rogcg.com/blog/2013/11/01/gridview-with-auto-resized-images-on-android


Design Decisions
----------------

1. GridView instead of Fragements
I decided against using Fragments, as this first part I need only 2 views to display:
-- the grid with the thumbnails as overview 
-- the details of the movie

The grid will always expand to the size of the parent, so if the user has a tablet, the grid will be larger, 
but the functionality and the user experience will still be the same. When clicking on the image I will switch to the Details view.
Hence, I go for the normal GridLayout, instead of Fragments, where I could display the grid view and the details at the same time, at leaat on tablets.
I did not look at the requirements for Part 2 of the project. I might still be that I will use Fragements later.

2. Async fetching of movie data
This is one of the most important lessons: never use the man GUI thread to load resources as it might block the rendering of the UI.
When watching the video about the AsyncTask, it did not feel right to use it in this scenario. A service would be better. So I stopped the video and went looking for resources on services. It looks like the SyncAdapter togethr with a content Provider is the best suitable solution. However, there were too many new concepts to digest. I stuggled and was not sure which approach to choose. I went back to the video. At the end, the teacher mentions the services, finally. However, there must be a reaosn why the Async Task was so dominently explained. Therefore, I decided to use this plain and simple approach.

