package com.example.faceattend;

public class MarkAttendModel {
    private boolean result;
    private String error;
    private String dist;

    public MarkAttendModel(boolean result, String error, String dist) {
        this.result = result;
        this.error = error;
        this.dist = dist;
    }

    public boolean isResult() {
        return result;
    }

    public String getError() {
        return error;
    }

    public String getDist() {
        return dist;
    }
}
