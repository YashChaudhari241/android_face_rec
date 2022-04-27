package com.example.faceattend;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Attendance
{
    @NonNull
    @PrimaryKey
    public String timeStamp;

    public boolean automated;

    public boolean entryExit;

    public String locx;

    public String locy;

    public String getTimeStamp() {
        return timeStamp;
    }

    public boolean isAutomated() {
        return automated;
    }

    public boolean isEntryExit() {
        return entryExit;
    }

    public String getLocx() {
        return locx;
    }

    public String getLocy() {
        return locy;
    }

    public Attendance(String timeStamp, boolean automated, boolean entryExit, String locx, String locy) {
        this.timeStamp = timeStamp;
        this.automated = automated;
        this.entryExit = entryExit;
        this.locx = locx;
        this.locy = locy;
    }

}