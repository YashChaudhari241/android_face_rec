package com.example.faceattend;

public class Attendance
{
    private String timeStamp;

    private boolean automated;

    private boolean entryExit;

    private String locx;

    private String locy;

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