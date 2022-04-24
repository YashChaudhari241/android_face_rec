package com.example.faceattend;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface UserDao {
    @Query("SELECT * FROM userobject")
    List<UserObject> getAll();

    @Insert
    void insertAll(UserObject user);

    @Update
    void update(UserObject user);

    @Delete
    void delete(UserObject user);
}
