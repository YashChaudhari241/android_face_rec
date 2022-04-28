package com.example.faceattend.models;

public class LocationData
{
    private String ycord;

    private int radius;

    private String xcord;

    public String getYcord ()
    {
        return ycord;
    }

    public LocationData(String ycord, int radius, String xcord) {
        this.ycord = ycord;
        this.radius = radius;
        this.xcord = xcord;
    }

    public void setYcord (String ycord)
    {
        this.ycord = ycord;
    }

    public int getRadius ()
    {
        return radius;
    }

    public void setRadius (int radius)
    {
        this.radius = radius;
    }

    public String getXcord ()
    {
        return xcord;
    }

    public void setXcord (String xcord)
    {
        this.xcord = xcord;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [ycord = "+ycord+", radius = "+ String.valueOf(radius)+", xcord = "+xcord+"]";
    }
}


