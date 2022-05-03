package com.example.faceattend.models;

import java.util.List;

public class ReportModel {

    private boolean result;
    private List<PresentModel> present ;

    public ReportModel(boolean result, List<PresentModel> present) {
        this.result = result;
        this.present = present;
    }

    public boolean isResult() {
        return result;
    }

    public void setResult(boolean result) {
        this.result = result;
    }

    public List<PresentModel> getPresent() {
        return present;
    }



}
