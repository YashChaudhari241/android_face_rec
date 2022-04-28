package com.example.faceattend.models;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class UserObject {
    @NonNull
    @PrimaryKey
    public String firebaseID;

    @ColumnInfo(name = "priv")
    public int priv;

    @ColumnInfo(name = "org_name")
    public String orgName;

    @ColumnInfo(name= "mark_exit")
    public boolean markExit;

    @ColumnInfo(name= "unique_string")
    public String uniqueString;

    @ColumnInfo(name = "mark_loc")
    public boolean markLoc;

    @ColumnInfo(name = "join_pass")
    public String joinPass;

    public String pubID;
    public UserObject(String firebaseID, int priv, String orgName, boolean markExit, String uniqueString, boolean markLoc, String joinPass, String defStart, String defEnd,String pubID) {
        this.firebaseID = firebaseID;
        this.priv = priv;
        this.orgName = orgName;
        this.markExit = markExit;
        this.uniqueString = uniqueString;
        this.markLoc = markLoc;
        this.joinPass = joinPass;
        this.defStart = defStart;
        this.defEnd = defEnd;
        this.pubID = pubID;
    }

    public String getPubID() {
        return pubID;
    }

    @ColumnInfo(name="def_start")
    public String defStart;

    @ColumnInfo(name="def_end")
    public String defEnd;
}
