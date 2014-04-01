package com.teusoft.spytour.entity;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by DungLV on 31/3/2014.
 */
public class Tour implements Serializable {
    @SerializedName("id")
    private int tourId;

    @SerializedName("tour_name")
    private String tourName;

    @SerializedName("image")
    private String imageUrl;

    @SerializedName("tour_des")
    private String description;

    public int getTourId() {
        return tourId;
    }

    public void setTourId(int tourId) {
        this.tourId = tourId;
    }

    public String getTourName() {
        return tourName;
    }

    public void setTourName(String tourName) {
        this.tourName = tourName;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
