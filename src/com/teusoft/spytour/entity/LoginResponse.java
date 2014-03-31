package com.teusoft.spytour.entity;

import com.google.gson.annotations.SerializedName;

/**
 * Created by DungLV on 28/3/2014.
 */
public class LoginResponse {
    @SerializedName("status")
    private String status;

    private UserData userData;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public UserData getUserData() {
        return userData;
    }

    public void setUserData(UserData userData) {
        this.userData = userData;
    }
}



