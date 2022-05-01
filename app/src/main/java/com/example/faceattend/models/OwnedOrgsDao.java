package com.example.faceattend.models;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface OwnedOrgsDao {
    @Query("SELECT * FROM orgDetails")
    List<OrgDetails> getAll();

    @Insert
    void insert(OrgDetails org);
    @Insert
    void insertAll(List<OrgDetails> org);

    @Update
    void update(OrgDetails user);

    @Query("UPDATE orgDetails SET " +
            "selected = :bool WHERE uniqueString=:uniqStr")
    int selectOrg(boolean bool,String uniqStr);

    @Delete
    void delete(OrgDetails user);

    @Query("SELECT * FROM orgdetails WHERE selected=:value1 LIMIT 1 ")
    OrgDetails getSelected(boolean value1);

    @Query("UPDATE orgdetails SET selected=:value1")
    int unselect(boolean value1);

    @Query("DELETE FROM orgdetails")
    void deleteAll();
}
