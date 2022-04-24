package com.example.faceattend;

import org.json.JSONObject;

public class InitOrgModel {
    private boolean result;
    private String uniqueStr;
    private String error;
//    public InitOrgModel(String result,String error, String uniqueStr){
//
//    }
    public InitOrgModel(boolean result, String uniqueStr, String error) {
        this.result = result;
        this.uniqueStr = uniqueStr;
        this.error = error;
    }

    public boolean getResult() {
        return result;
    }

    public String getUniqueStr() {
        return uniqueStr;
    }

    public String getError() {
        return error;
    }
}
