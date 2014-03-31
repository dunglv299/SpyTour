package com.teusoft.spytour.entity;

import com.google.gson.annotations.SerializedName;

public class UserData {
    @SerializedName("id")
    public long id;

    @SerializedName("partner_name")
    public String partnerName;

    @SerializedName("partner_phone")
    public String parnerPhoneNumber;

    @SerializedName("email")
    public String email;

    @SerializedName("password")
    public String password;

    @SerializedName("partner_adress")
    public String partnerAddress;

    @SerializedName("partners_note")
    public String partnerNote;

    public String getPartnerNote() {
        return partnerNote;
    }

    public void setPartnerNote(String partnerNote) {
        this.partnerNote = partnerNote;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getPartnerName() {
        return partnerName;
    }

    public void setPartnerName(String partnerName) {
        this.partnerName = partnerName;
    }

    public String getParnerPhoneNumber() {
        return parnerPhoneNumber;
    }

    public void setParnerPhoneNumber(String parnerPhoneNumber) {
        this.parnerPhoneNumber = parnerPhoneNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPartnerAddress() {
        return partnerAddress;
    }

    public void setPartnerAddress(String partnerAddress) {
        this.partnerAddress = partnerAddress;
    }
}