package com.example.faceattend;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {UserObject.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {
    public abstract UserDao userDao();
}


