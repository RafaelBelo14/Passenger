package com.example.passenger.Menus.api;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class GoogleResponse {
    @SerializedName("results")
    private List<Item> itemList;

    public List<Item> getItemList() {
        System.out.println(itemList);
        return itemList;
    }

}
