package com.example.faceattend;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.example.faceattend.models.AttendanceDao;
import com.example.faceattend.models.OrgDetails;
import com.example.faceattend.models.OwnedOrgsDao;
import com.example.faceattend.models.UserDao;
import com.example.faceattend.models.UserObject;

@Database(entities = {UserObject.class,Attendance.class, OrgDetails.class}, version = 6)
public abstract class AppDatabase extends RoomDatabase {
    public abstract UserDao userDao();
    public abstract AttendanceDao attendanceDao();
    public abstract OwnedOrgsDao ownedOrgsDao();
}


