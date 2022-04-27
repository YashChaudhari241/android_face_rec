package com.example.faceattend;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface AttendanceDao {
    @Query("SELECT * FROM Attendance")
    List<Attendance> getAll();

    @Insert
    void insertAll(Attendance[] user);

    @Update
    void update(Attendance user);

    @Query("DELETE FROM Attendance")
    void deleteAll();
}
