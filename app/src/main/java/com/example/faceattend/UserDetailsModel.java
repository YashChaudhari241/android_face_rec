package com.example.faceattend;


public class UserDetailsModel
{
    private boolean result;

    private OrgDetails orgDetails;

    private int priv;

    public boolean getResult ()
    {
        return result;
    }

    public UserDetailsModel(boolean result, OrgDetails orgDetails, int priv) {
        this.result = result;
        this.orgDetails = orgDetails;
        this.priv = priv;
    }

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


    @Override
    public String toString()
    {
        return "ClassPojo [result = "+Boolean.toString(result)+", orgDetails = "+orgDetails.toString()+", priv = "+priv+"]";
    }
}
