package com.example.faceattend.models;

public class PresentModel {

    private String name;
    private boolean markExit;
    private String pubID;
    private boolean present;
    private boolean exited;
    private int priv;
    private Object startTime;
    private Object endtime;

    public PresentModel(String name, boolean markExit, String pubID, boolean present, boolean exited, int priv, Object startTime, Object endtime) {
        this.name = name;
        this.markExit = markExit;
        this.pubID = pubID;
        this.present = present;
        this.exited = exited;
        this.priv = priv;
        this.startTime = startTime;
        this.endtime = endtime;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isMarkExit() {
        return markExit;
    }

    public void setMarkExit(boolean markExit) {
        this.markExit = markExit;
    }

    public String getPubID() {
        return pubID;
    }

    public void setPubID(String pubID) {
        this.pubID = pubID;
    }

    public boolean isPresent() {
        return present;
    }

    public void setPresent(boolean present) {
        this.present = present;
    }

    public boolean isExited() {
        return exited;
    }

    public void setExited(boolean exited) {
        this.exited = exited;
    }

    public int getPriv() {
        return priv;
    }

    public void setPriv(int priv) {
        this.priv = priv;
    }

    public Object getStartTime() {
        return startTime;
    }

    public void setStartTime(Object startTime) {
        this.startTime = startTime;
    }

    public Object getEndtime() {
        return endtime;
    }

    public void setEndtime(Object endtime) {
        this.endtime = endtime;
    }

}
