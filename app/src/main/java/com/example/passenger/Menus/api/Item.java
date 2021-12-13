package com.example.passenger.Menus.api;

import com.google.gson.JsonObject;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class Item {
    @SerializedName("photos")
    private ArrayList<Photo> icon;
    @SerializedName("name")
    private String name;
    @SerializedName("opening_hours")
    private JsonObject opening_hours;
    @SerializedName("price_level")
    private int price_level;
    @SerializedName("rating")
    private float rating;
    @SerializedName("vicinity")
    private String morada;

    public ArrayList<Photo> getIcon() {
        return icon;
    }

    public void setIcon(ArrayList<Photo> icon) {
        this.icon = icon;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public JsonObject getOpening_hours() {
        return opening_hours;
    }

    public void setOpening_hours(JsonObject opening_hours) {
        this.opening_hours = opening_hours;
    }

    public int getPrice_level() {
        return price_level;
    }

    public void setPrice_level(int price_level) {
        this.price_level = price_level;
    }

    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }

    public String getMorada() {
        return morada;
    }

    public void setMorada(String morada) {
        this.morada = morada;
    }
}
