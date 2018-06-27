package com.example.parul.movietheatre.Data;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;

import java.util.List;

public class MovieViewModel extends AndroidViewModel {
    private MovieRepository mRepository;
    private final LiveData<List<MovieDataItem>> movieList;

    public MovieViewModel(Application application) {
        super(application);
        mRepository = new MovieRepository(application);

        movieList = mRepository.getAllMovies();
    }


    public LiveData<List<MovieDataItem>> getAllFavMovieList() {
        return movieList;
    }

    public MovieDataItem getMovieFromFavList(MovieDataItem movie) {
        return mRepository.getMovieFromDB(movie);
    }

    public void deleteItem(MovieDataItem movie) {
        mRepository.delete(movie);
    }

    public void insertItem(MovieDataItem movie) {
        mRepository.insert(movie);
    }

    public void updateItem(MovieDataItem movie) {
        mRepository.update(movie);
    }

    public boolean isMovieIDFav(MovieDataItem movie) {
        return mRepository.isMovieIDFav(movie);
    }
}
