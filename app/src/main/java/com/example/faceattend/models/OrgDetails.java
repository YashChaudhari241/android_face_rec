package com.example.faceattend.models;

public class OrgDetails {
    private LocationData[] locationData;

    private String uniqueString;

    private boolean markExit;

    private boolean allowMissedExit;

    public OrgDetails(LocationData[] locationData, String uniqueString, boolean markExit, boolean allowMissedExit, String defStart, String defEnd, int defMissedInterval, String orgName, String ownerName, boolean markLoc, String ownerPic, String joinPass) {
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
        this.joinPass = joinPass;
    }

    private String defStart;

    private String defEnd;

    public boolean isAllowMissedExit() {
        return allowMissedExit;
    }

    private int defMissedInterval;

    private String orgName;

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
