package com.example.faceattend;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.example.faceattend.models.AttendanceDao;
import com.example.faceattend.models.UserDao;
import com.example.faceattend.models.UserObject;

@Database(entities = {UserObject.class,Attendance.class}, version = 5)
public abstract class AppDatabase extends RoomDatabase {
    public abstract UserDao userDao();
    public abstract AttendanceDao attendanceDao();
}


