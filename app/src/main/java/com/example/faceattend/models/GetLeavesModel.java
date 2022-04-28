package com.example.faceattend.models;

public class GetLeavesModel {
    private boolean result;
    private LeaveModel leaves[];
    private String error;

    public boolean isResult() {
        return result;
    }

    public LeaveModel[] getLeaves() {
        return leaves;
    }

    public GetLeavesModel(boolean result, LeaveModel[] leaves, String error) {
        this.result = result;
        this.leaves = leaves;
        this.error = error;
    }

    public String getError() {
        return error;
    }
}
