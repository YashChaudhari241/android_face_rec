package com.example.faceattend;

public class GetOrgModel {
    private String orgName;
    private String ownerName;
    private String ownerPhoto;
    private boolean result;
    private boolean verified;

    public String getOrgName() {
        return orgName;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public String getOwnerPhoto() {
        return ownerPhoto;
    }

    public boolean isResult() {
        return result;
    }

    public boolean isVerified() {
        return verified;
    }

    public GetOrgModel(String orgName, String ownerName, String ownerPhoto, boolean result, boolean verified) {
        this.orgName = orgName;
        this.ownerName = ownerName;
        this.ownerPhoto = ownerPhoto;
        this.result = result;
        this.verified = verified;
    }
}
