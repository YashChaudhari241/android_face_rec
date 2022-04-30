package com.example.faceattend.models;

import java.util.List;

public class StatModel {

    private boolean result;
    private double[] week = null;
    private double total;
    private double ot;
    private int missed;
    private int days;
    private int properLeaves;
    private int unApprovedLeave;
    private int unDocumented;
    private int extraLeave;
    private String error;

    public String getError() {
        return error;
    }

    public StatModel(boolean result, double[] week, double total, double ot, int missed, int days, int properLeaves, int unApprovedLeave, int unDocumented, int extraLeave, String error) {
        super();
        this.result = result;
        this.week = week;
        this.total = total;
        this.ot = ot;
        this.missed = missed;
        this.days = days;
        this.properLeaves = properLeaves;
        this.unApprovedLeave = unApprovedLeave;
        this.unDocumented = unDocumented;
        this.extraLeave = extraLeave;
        this.error = error;
    }


    public boolean isResult() {
        return result;
    }

    public void setResult(boolean result) {
        this.result = result;
    }

    public double[] getWeek() {
        return week;
    }

    public void setWeek(double[] week) {
        this.week = week;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public double getOt() {
        return ot;
    }

    public void setOt(double ot) {
        this.ot = ot;
    }

    public int getMissed() {
        return missed;
    }

    public void setMissed(int missed) {
        this.missed = missed;
    }

    public int getDays() {
        return days;
    }

    public void setDays(int days) {
        this.days = days;
    }

    public int getProperLeaves() {
        return properLeaves;
    }

    public void setProperLeaves(int properLeaves) {
        this.properLeaves = properLeaves;
    }

    public int getUnApprovedLeave() {
        return unApprovedLeave;
    }

    public void setUnApprovedLeave(int unApprovedLeave) {
        this.unApprovedLeave = unApprovedLeave;
    }

    public int getUnDocumented() {
        return unDocumented;
    }

    public void setUnDocumented(int unDocumented) {
        this.unDocumented = unDocumented;
    }

    public int getExtraLeave() {
        return extraLeave;
    }

    public void setExtraLeave(int extraLeave) {
        this.extraLeave = extraLeave;
    }

}
