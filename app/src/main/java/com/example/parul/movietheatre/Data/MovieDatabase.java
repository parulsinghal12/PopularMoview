package com.example.parul.movietheatre.Data;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

@Database(entities = {MovieDataItem.class}, version = 1)
public abstract class MovieDatabase extends RoomDatabase {
    public abstract MovieDao movieDao();

    private static MovieDatabase INSTANCE;

    public static MovieDatabase getDatabase(Context context) {
        if (INSTANCE == null) {
            INSTANCE =
                    Room.databaseBuilder(context.getApplicationContext(), MovieDatabase.class, "movie_db")
                            .build();
        }
        return INSTANCE;
    }


}
