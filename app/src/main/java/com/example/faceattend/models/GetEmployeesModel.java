package com.example.faceattend.models;

public class GetEmployeesModel {
    private UserDetailsModel employees[];
    private boolean result;
    private String error;

    public GetEmployeesModel(UserDetailsModel[] employees, boolean result, String error) {
        this.employees = employees;
        this.result = result;
        this.error = error;
    }

    public UserDetailsModel[] getEmployees() {
        return employees;
    }

    public boolean isResult() {
        return result;
    }

    public String getError() {
        return error;
    }
}
