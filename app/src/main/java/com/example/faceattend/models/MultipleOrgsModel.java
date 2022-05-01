package com.example.faceattend.models;

public class MultipleOrgsModel {
    private OrgDetails orgs[];

    public OrgDetails[] getOrgDetails() {
        return orgs;
    }

    public MultipleOrgsModel(OrgDetails[] orgDetails) {
        this.orgs = orgDetails;
    }
}
