package com.example.passenger.Menus.api;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.passenger.Menus.restaurant.RestaurantDetail;
import com.example.passenger.R;

import java.util.List;

public class GoogleRestaurantAdapter extends RecyclerView.Adapter<GoogleRestaurantAdapter.ViewHolder> {

    private final Context context;
    private final List<Item> list;
    private final static String KEY = "API_KEY";
    private String LATITUDE_LONGITUDE;
    private boolean mute;

    public GoogleRestaurantAdapter(Context context, List<Item> list, String latitude_longitude, boolean mute) {
        this.context = context;
        this.list = list;
        this.LATITUDE_LONGITUDE = latitude_longitude;
        this.mute = mute;
    }

    public void setMute(boolean mute) {
        this.mute = mute;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView restaurantImage;
        TextView restaurantName;
        TextView restaurantOpen;
        TextView restaurantRating;
        TextView restaurantVicinity;
        TextView restaurantPriceLevel;
        TextView restaurantPhotoReference;
        TextView restaurantLatitude;
        TextView restaurantLongitude;
        RelativeLayout parentLayout;

        public ViewHolder(View view) {
            super(view);
            restaurantPhotoReference = view.findViewById(R.id.restaurantPhotoReference);
            restaurantImage = view.findViewById(R.id.restaurantImage);
            restaurantName = view.findViewById(R.id.restaurantName);
            restaurantOpen = view.findViewById(R.id.restaurantOpen);
            restaurantRating = view.findViewById(R.id.restaurantRating);
            restaurantVicinity = view.findViewById(R.id.restaurantVicinity);
            restaurantPriceLevel = view.findViewById(R.id.restaurantPriceLevel);
            restaurantLatitude = view.findViewById(R.id.restaurantLatitude);
            restaurantLongitude = view.findViewById(R.id.restaurantLongitude);
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
                holder.restaurantPhotoReference.setText(list.get(position).getIcon().get(0).getPhoto_reference());
                holder.restaurantName.setText(list.get(position).getName());
                if (list.get(position).getOpening_hours() == null) {
                    holder.restaurantOpen.setText("Sem informação");
                }
                else {
                    if (list.get(position).getOpening_hours().get("open_now").toString().equals("true")) {
                        holder.restaurantOpen.setText("Aberto");
                    }
                    else {
                        holder.restaurantOpen.setText("Fechado");
                    }
                }
                holder.restaurantRating.setText(String.valueOf(list.get(position).getRating()));
                holder.restaurantPriceLevel.setText(String.valueOf(list.get(position).getPrice_level()));
                holder.restaurantVicinity.setText(list.get(position).getMorada());
                holder.restaurantLatitude.setText(list.get(position).getGeometry().getAsJsonObject("location").get("lat").toString());
                holder.restaurantLongitude.setText(list.get(position).getGeometry().getAsJsonObject("location").get("lng").toString());
            }
            else {
                holder.restaurantName.setText(list.get(position).getName());
                if (list.get(position).getOpening_hours() == null) {
                    holder.restaurantOpen.setText("Sem informação");
                }
                else {
                    if (list.get(position).getOpening_hours().get("open_now").toString().equals("true")) {
                        holder.restaurantOpen.setText("Aberto");
                    }
                    else {
                        holder.restaurantOpen.setText("Fechado");
                    }
                }
                holder.restaurantRating.setText(String.valueOf(list.get(position).getRating()));
                holder.restaurantPriceLevel.setText(String.valueOf(list.get(position).getPrice_level()));
                holder.restaurantVicinity.setText(list.get(position).getMorada());
                holder.restaurantLatitude.setText(list.get(position).getGeometry().getAsJsonObject("geometry").getAsJsonObject("lat").toString());
                holder.restaurantLongitude.setText(list.get(position).getGeometry().getAsJsonObject("geometry").getAsJsonObject("lng").toString());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        holder.parentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, RestaurantDetail.class);
                Log.d("INFOOOO", "A enviar:\n" +
                        String.valueOf(mute) + "\n" +
                        holder.restaurantPhotoReference.getText() + "\n" +
                        holder.restaurantName.getText() + "\n" +
                        holder.restaurantOpen.getText() + "\n" +
                        holder.restaurantRating.getText() + '\n' +
                        holder.restaurantLatitude.getText() + '\n' +
                        holder.restaurantLongitude.getText());

                intent.putExtra("photo_reference", holder.restaurantPhotoReference.getText());
                intent.putExtra("name", holder.restaurantName.getText());
                intent.putExtra("open", holder.restaurantOpen.getText());
                intent.putExtra("rating", holder.restaurantRating.getText());
                intent.putExtra("price_level", holder.restaurantPriceLevel.getText());
                intent.putExtra("vicinity", holder.restaurantVicinity.getText());
                intent.putExtra("latitude_longitude", LATITUDE_LONGITUDE);
                intent.putExtra("mute", String.valueOf(mute));
                intent.putExtra("latitude_place", holder.restaurantLatitude.getText());
                intent.putExtra("longitude_place", holder.restaurantLongitude.getText());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}
