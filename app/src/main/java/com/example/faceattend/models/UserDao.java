package com.example.faceattend.models;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.faceattend.models.UserObject;

import java.util.List;

@Dao
public interface UserDao {
    @Query("SELECT * FROM userobject")
    List<UserObject> getAll();


    @Insert
    void insertAll(UserObject user);

    @Update
    void update(UserObject user);

    @Query("UPDATE userobject SET " +
            "org_name = :value1," +
            "unique_string= :value2")
    int updateOrg(String value1,String value2);

    @Delete
    void delete(UserObject user);

    @Query("DELETE FROM userobject")
    void deleteAll();
}
