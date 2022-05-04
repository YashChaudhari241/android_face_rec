package com.example.faceattend.models;

import java.util.List;

public class StatModel {

    private boolean result;
    private double[] week = null;
    private double total;
    private double ot;
    private int missed;
    private int days;
    private int daysCame;
    private int weekends;
    private int lateArr;
    private int absent;
    private double totalLateTime;
    private String avgStart;
    private String avgEnd;
    private int properLeaves;
    private int unApprovedLeave;
    private int unDocumented;
    private int extraLeave;
    private String error;
    /**
     * No args constructor for use in serialization
     *
     */
    public StatModel() {
    }

    /**
     *
     * @param weekends
     * @param week
     * @param lateArr
     * @param ot
     * @param unApprovedLeave
     * @param missed
     * @param unDocumented
     * @param extraLeave
     * @param avgStart
     * @param daysCame
     * @param totalLateTime
     * @param properLeaves
     * @param result
     * @param total
     * @param days
     * @param absent
     * @param avgEnd
     */
    public StatModel(boolean result, double[] week, double total, double ot, int missed, int days, int daysCame, int weekends, int lateArr, int absent, double totalLateTime, String avgStart, String avgEnd, int properLeaves, int unApprovedLeave, int unDocumented, int extraLeave,String error) {
        super();
        this.result = result;
        this.week = week;
        this.total = total;
        this.ot = ot;
        this.missed = missed;
        this.days = days;
        this.daysCame = daysCame;
        this.weekends = weekends;
        this.lateArr = lateArr;
        this.absent = absent;
        this.totalLateTime = totalLateTime;
        this.avgStart = avgStart;
        this.avgEnd = avgEnd;
        this.properLeaves = properLeaves;
        this.unApprovedLeave = unApprovedLeave;
        this.unDocumented = unDocumented;
        this.extraLeave = extraLeave;
        this.error = error;
    }

    public String getError() {
        return error;
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

    public int getDaysCame() {
        return daysCame;
    }

    public void setDaysCame(int daysCame) {
        this.daysCame = daysCame;
    }

    public int getWeekends() {
        return weekends;
    }

    public void setWeekends(int weekends) {
        this.weekends = weekends;
    }

    public int getLateArr() {
        return lateArr;
    }

    public void setLateArr(int lateArr) {
        this.lateArr = lateArr;
    }

    public int getAbsent() {
        return absent;
    }

    public void setAbsent(int absent) {
        this.absent = absent;
    }

    public double getTotalLateTime() {
        return totalLateTime;
    }

    public void setTotalLateTime(double totalLateTime) {
        this.totalLateTime = totalLateTime;
    }

    public String getAvgStart() {
        return avgStart;
    }

    public void setAvgStart(String avgStart) {
        this.avgStart = avgStart;
    }

    public String getAvgEnd() {
        return avgEnd;
    }

    public void setAvgEnd(String avgEnd) {
        this.avgEnd = avgEnd;
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