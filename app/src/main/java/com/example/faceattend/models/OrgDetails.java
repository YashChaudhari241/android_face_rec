package com.example.faceattend.models;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity
public class OrgDetails {
    @Ignore
    private LocationData[] locationData;

    @NonNull
    @PrimaryKey
    private String uniqueString;

    public void setMarkExit(boolean markExit) {
        this.markExit = markExit;
    }

    public void setJoinPass(String joinPass) {
        this.joinPass = joinPass;
    }

    private boolean markExit;

    public void setAllowMissedExit(boolean allowMissedExit) {
        this.allowMissedExit = allowMissedExit;
    }

    public void setDefStart(String defStart) {
        this.defStart = defStart;
    }

    public void setDefEnd(String defEnd) {
        this.defEnd = defEnd;
    }

    public void setOrgName(String orgName) {
        this.orgName = orgName;
    }

    public void setMarkLoc(boolean markLoc) {
        this.markLoc = markLoc;
    }

    @Ignore
    private boolean allowMissedExit;
    private boolean selected;
    public OrgDetails(){

    }

    public void setUniqueString(@NonNull String uniqueString) {
        this.uniqueString = uniqueString;
    }

    public OrgDetails(String unqiueString,String orgName){
        this.uniqueString = unqiueString;
        this.orgName = orgName;
        this.selected = true;
    }
    public OrgDetails(LocationData[] locationData, String uniqueString, boolean markExit, boolean allowMissedExit, String defStart, String defEnd, int defMissedInterval, String orgName, String ownerName, boolean markLoc, String ownerPic, String joinPass,boolean selected) {
        this.locationData = locationData;
        this.uniqueString = uniqueString;
        this.markExit = markExit;
        this.allowMissedExit = allowMissedExit;
        this.defStart = defStart;
        this.defEnd = defEnd;
        this.defMissedInterval = defMissedInterval;
        this.orgName = orgName;
        this.ownerName = ownerName;
        this.markLoc = markLoc;
        this.ownerPic = ownerPic;
        this.selected = selected;
        this.joinPass = joinPass;
    }
    private String defStart;

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    private String defEnd;

    public boolean isAllowMissedExit() {
        return allowMissedExit;
    }
    @Ignore
    private int defMissedInterval;

    private String orgName;
    @Ignore
    private String ownerName;


    public String getDefStart() {
        return defStart;
    }

    public String getDefEnd() {
        return defEnd;
    }

    public int getDefMissedInterval() {
        return defMissedInterval;
    }

    private boolean markLoc;
    @Ignore
    private String ownerPic;

    private String joinPass;

    public LocationData[] getLocationData() {
        return locationData;
    }

    public String getUniqueString() {
        return uniqueString;
    }

    public boolean getMarkExit() {
        return markExit;
    }

    public String getOrgName() {
        return orgName;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public boolean getMarkLoc() {
        return markLoc;
    }

    public String getOwnerPic() {
        return ownerPic;
    }

    public String getJoinPass() {
        return joinPass;
    }

    @Override
    public String toString() {
        return "ClassPojo [locationData = " + locationData.toString() + ", uniqueString = " + uniqueString + ", markExit = " + Boolean.toString(markExit) + ", orgName = " + orgName + ", ownerName = " + ownerName + ", markLoc = " + Boolean.toString(markLoc) + ", ownerPic = " + ownerPic + ", joinPass = " + joinPass + "]";
    }
}
