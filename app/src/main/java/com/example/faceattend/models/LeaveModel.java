package com.example.faceattend.models;

import java.io.Serializable;
import java.util.List;

public class LeaveModel {
    private String leaveBy;
    private String startDate;
    private String endDate;
    private String msg;
    private Integer approved;
    private Object approvalTime;
    private String orgOwner;
    private String pubID;
    private List<OrgDetails> orgDetails = null;

    public String getLeaveBy() {
        return leaveBy;
    }

    public String getStartDate() {
        return startDate;
    }

    public String getOrgOwner() {
        return orgOwner;
    }

    public LeaveModel(String leaveBy, String startDate, String endDate, String msg, Integer approved, Object approvalTime, List<OrgDetails> orgDetails, String orgOwner,String pubID) {
        this.leaveBy = leaveBy;
        this.startDate = startDate;
        this.endDate = endDate;
        this.msg = msg;
        this.orgOwner = orgOwner;
        this.approved = approved;
        this.approvalTime = approvalTime;
        this.orgDetails = orgDetails;
        this.pubID = pubID;
    }

    public String getPubID() {
        return pubID;
    }

    public String getEndDate() {
        return endDate;
    }

    public String getMsg() {
        return msg;
    }

    public Integer getApproved() {
        return approved;
    }

    public Object getApprovalTime() {
        return approvalTime;
    }

    public List<OrgDetails> getOrgDetails() {
        return orgDetails;
    }
}
