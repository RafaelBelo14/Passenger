package com.example.passenger.Menus.api;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface GoogleApiRetrofit {
    @GET("place/nearbysearch/json?")
    Call<GoogleResponse> listRepo(@Query("key") String APIkey, @Query("location") String latitudeLongitude, @Query("radius") String distance, @Query("type") String type);
}
