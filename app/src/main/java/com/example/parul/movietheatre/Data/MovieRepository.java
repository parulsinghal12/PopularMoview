package com.example.parul.movietheatre.Data;


import java.util.*;
import java.util.concurrent.ExecutionException;

import android.arch.lifecycle.*;
import android.app.*;
import android.util.Log;

public class MovieRepository {

    private MovieDao mMovieDao;
    private LiveData<List<MovieDataItem>> mAllWords;

    MovieRepository(Application application) {
        MovieDatabase db = MovieDatabase.getDatabase(application);
        mMovieDao = db.movieDao();
        mAllWords = mMovieDao.getAll();
    }

    LiveData<List<MovieDataItem>> getAllMovies() {
        return mAllWords;
    }

    public void insert (MovieDataItem movie) {
        new insertAsyncTask(mMovieDao).execute(movie);
    }

    private static class insertAsyncTask extends android.os.AsyncTask<MovieDataItem, Void, Void> {

        private MovieDao mAsyncTaskDao;

        insertAsyncTask(MovieDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final MovieDataItem... params) {
            mAsyncTaskDao.insert(params[0]);
            return null;
        }
    }

    public void update (MovieDataItem movie) {
        new updateAsyncTask(mMovieDao).execute(movie);
    }

    private static class updateAsyncTask extends android.os.AsyncTask<MovieDataItem, Void, Void> {

        private MovieDao mAsyncTaskDao;

        updateAsyncTask(MovieDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final MovieDataItem... params) {
            mAsyncTaskDao.update(params[0]);
            return null;
        }
    }

    public void delete (MovieDataItem movie) {
        new deleteAsyncTask(mMovieDao).execute(movie);
    }

    private static class deleteAsyncTask extends android.os.AsyncTask<MovieDataItem, Void, Void> {

        private MovieDao mAsyncTaskDao;

        deleteAsyncTask(MovieDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final MovieDataItem... params) {
            mAsyncTaskDao.delete(params[0]);
            return null;
        }

    }

    public boolean isMovieIDFav (MovieDataItem movie) {
        MovieDataItem returnedData = null;
        try {
            returnedData = new searchIdAsyncTask(mMovieDao).execute(movie).get();
        } catch (InterruptedException e) {
            Log.d("searchMovieId ","failed InterruptedException");
            e.printStackTrace();
        } catch (ExecutionException e) {
            Log.d("searchMovieId ","failed ExecutionException");
            e.printStackTrace();
        }
        if(returnedData!=null)
            return true;
        return false;
    }

    private static class searchIdAsyncTask extends android.os.AsyncTask<MovieDataItem, Void, MovieDataItem> {

        private MovieDao mAsyncTaskDao;

        searchIdAsyncTask(MovieDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected MovieDataItem doInBackground(final MovieDataItem... params) {
            MovieDataItem movie = mAsyncTaskDao.searchIfMovieExists(params[0].getId());
            return movie;
        }

    }

    public MovieDataItem getMovieFromDB (MovieDataItem movie) {
        MovieDataItem returnedData = null;
        try {
            returnedData = new searchIdAsyncTask(mMovieDao).execute(movie).get();
        } catch (InterruptedException e) {
            Log.d("searchMovieId ","failed InterruptedException");
            e.printStackTrace();
        } catch (ExecutionException e) {
            Log.d("searchMovieId ","failed ExecutionException");
            e.printStackTrace();
        }
        return returnedData;
    }



}

