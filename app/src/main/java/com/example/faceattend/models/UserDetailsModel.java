package com.example.faceattend.models;


import com.example.faceattend.Attendance;

public class UserDetailsModel
{
    private boolean result;

    private OrgDetails orgDetails;

    private int priv;
    private Attendance[] attendance;
    public boolean getResult ()
    {
        return result;
    }
    private String pubID;
    private String empName;
    public UserDetailsModel(boolean result, OrgDetails orgDetails, int priv,Attendance[] attendance,String pubID,String empName) {
        this.result = result;
        this.orgDetails = orgDetails;
        this.priv = priv;
        this.attendance = attendance;
        this.pubID = pubID;
        this.empName=empName;
    }

    public String getPubID() {
        return pubID;
    }
    public String getEmpName(){ return  empName; }

    public OrgDetails getOrgDetails ()
    {
        return orgDetails;
    }

    public void setOrgDetails (OrgDetails orgDetails)
    {
        this.orgDetails = orgDetails;
    }

    public int getPriv ()
    {
        return priv;
    }

    public Attendance[] getAttendance() {
        return attendance;
    }

    @Override
    public String toString()
    {
        if (orgDetails != null)
            return "ClassPojo [result = "+Boolean.toString(result)+", orgDetails = "+orgDetails.toString()+", priv = "+priv+"]";
        else{
            return "ClassPojo [result = "+Boolean.toString(result)+", No org , priv = "+priv+"]";
        }
    }
}
