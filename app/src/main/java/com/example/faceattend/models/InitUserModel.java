package com.example.faceattend.models;

import org.json.JSONObject;

public class InitUserModel {
    //    JSONObject priv=new JSONObject();
//    String token;
    private boolean result;
    private String error;

    public InitUserModel(boolean result, String error) {
//        this.priv=priv;
//        this.token=token;
        this.result = result;
        this.error = error;
    }

    //    public String getToken() {
//        return token;
//    }
//    public String getResult(){
//        return result;
//    }
    public boolean getResult() {
        return result;
    }

    public String getError() {
        return error;
    }
}
