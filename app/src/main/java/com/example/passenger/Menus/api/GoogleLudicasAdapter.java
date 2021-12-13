package com.example.passenger.Menus.api;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.passenger.Menus.ludicas.LudicasDetail;
import com.example.passenger.R;

import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class GoogleLudicasAdapter extends RecyclerView.Adapter<GoogleLudicasAdapter.ViewHolder> {

    private final Context context;
    private final List<Item> list;
    private final static String URL_GOOGLE = "https://maps.googleapis.com/maps/api/place/";
    private final static String KEY = "AIzaSyBN0JoU7O597v0dOTCJ-oINVvoxe9BzAAM";

    public GoogleLudicasAdapter(Context context, List<Item> list) {
        this.context = context;
        this.list = list;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView restaurantImage;
        TextView restaurantName;
        TextView restaurantOpen;
        RelativeLayout parentLayout;

        public ViewHolder(View view) {
            super(view);
            restaurantImage = view.findViewById(R.id.restaurantImage);
            restaurantName = view.findViewById(R.id.restaurantName);
            restaurantOpen = view.findViewById(R.id.restaurantOpen);
            parentLayout = view.findViewById(R.id.restaurantItem);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.restaurant_item, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        try {
            if (list.get(position).getIcon() != null) {
                String url = "https://maps.googleapis.com/maps/api/place/photo" +
                        "?maxwidth=100" +
                        "&photoreference=" + list.get(position).getIcon().get(0).getPhoto_reference() +
                        "&key=" + KEY;

                Glide.with(context)
                        .load(url)
                        .into(holder.restaurantImage);
                holder.restaurantName.setText(list.get(position).getName());
                holder.restaurantOpen.setText(String.valueOf(list.get(position).getRating()));
            }
            else {
                holder.restaurantName.setText(list.get(position).getName());
                holder.restaurantOpen.setText(String.valueOf(list.get(position).getRating()));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        holder.parentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, LudicasDetail.class);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    @NonNull
    private Retrofit getRetrofit(OkHttpClient client) {
        return new Retrofit.Builder()
                .baseUrl(URL_GOOGLE)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build();
    }

    @NonNull
    private OkHttpClient getOkHttpClient() {
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        return new OkHttpClient.Builder().addInterceptor(loggingInterceptor).build();
    }
}