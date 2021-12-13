package com.example.passenger.Menus.api;

import com.google.gson.annotations.SerializedName;

public class Photo {
    @SerializedName("photo_reference")
    private String photo_reference;

    public String getPhoto_reference() {
        return photo_reference;
    }

    public void setPhoto_reference(String photo_reference) {
        this.photo_reference = photo_reference;
    }
}
