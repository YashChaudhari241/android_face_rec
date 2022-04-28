package com.example.faceattend.models;

public class MultipleOrgsModel {
    private OrgDetails orgDetails[];

    public OrgDetails[] getOrgDetails() {
        return orgDetails;
    }

    public MultipleOrgsModel(OrgDetails[] orgDetails) {
        this.orgDetails = orgDetails;
    }
}
