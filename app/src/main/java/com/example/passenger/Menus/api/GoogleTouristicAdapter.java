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
import com.example.passenger.Menus.touristic.TouristicDetail;
import com.example.passenger.R;

import java.util.List;

public class GoogleTouristicAdapter extends RecyclerView.Adapter<GoogleTouristicAdapter.ViewHolder> {

    private final Context context;
    private final List<Item> list;
    private final static String KEY = "AIzaSyBN0JoU7O597v0dOTCJ-oINVvoxe9BzAAM";
    private String LATITUDE_LONGITUDE;
    private boolean mute;

    public GoogleTouristicAdapter(Context context, List<Item> list, String latitude_longitude, boolean mute) {
        this.context = context;
        this.list = list;
        this.LATITUDE_LONGITUDE = latitude_longitude;
        this.mute = mute;
    }

    public void setMute(boolean mute) {
        this.mute = mute;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView touristicImage;
        TextView touristicName;
        TextView touristicOpen;
        TextView touristicRating;
        TextView touristicVicinity;
        TextView touristicPriceLevel;
        TextView touristicPhotoReference;
        TextView touristicLatitude;
        TextView touristicLongitude;
        RelativeLayout parentLayout;

        public ViewHolder(View view) {
            super(view);
            touristicPhotoReference = view.findViewById(R.id.touristicPhotoReference);
            touristicImage = view.findViewById(R.id.touristicImage);
            touristicName = view.findViewById(R.id.touristicName);
            touristicOpen = view.findViewById(R.id.touristicOpen);
            touristicRating = view.findViewById(R.id.touristicRating);
            touristicVicinity = view.findViewById(R.id.touristicVicinity);
            touristicPriceLevel = view.findViewById(R.id.touristicPriceLevel);
            touristicLatitude = view.findViewById(R.id.ludicasLatitude);
            touristicLongitude = view.findViewById(R.id.ludicasLongitude);
            parentLayout = view.findViewById(R.id.touristicItem);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.touristic_item, parent, false);

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
                        .into(holder.touristicImage);
                holder.touristicPhotoReference.setText(list.get(position).getIcon().get(0).getPhoto_reference());

                holder.touristicName.setText(list.get(position).getName());
                if (list.get(position).getOpening_hours() == null) {
                    holder.touristicOpen.setText("Sem informação");
                }
                else {
                    if (list.get(position).getOpening_hours().get("open_now").toString().equals("true")) {
                        holder.touristicOpen.setText("Aberto");
                    }
                    else {
                        holder.touristicOpen.setText("Fechado");
                    }
                }
                holder.touristicRating.setText(String.valueOf(list.get(position).getRating()));
                holder.touristicPriceLevel.setText(String.valueOf(list.get(position).getPrice_level()));
                holder.touristicVicinity.setText(list.get(position).getMorada());
                holder.touristicLatitude.setText(list.get(position).getGeometry().getAsJsonObject("location").get("lat").toString());
                holder.touristicLongitude.setText(list.get(position).getGeometry().getAsJsonObject("location").get("lng").toString());

            }
            else {
                holder.touristicName.setText(list.get(position).getName());
                if (list.get(position).getOpening_hours() == null) {
                    holder.touristicOpen.setText("Sem informação");
                }
                else {
                    if (list.get(position).getOpening_hours().get("open_now").toString().equals("true")) {
                        holder.touristicOpen.setText("Aberto");
                    }
                    else {
                        holder.touristicOpen.setText("Fechado");
                    }
                }
                holder.touristicRating.setText(String.valueOf(list.get(position).getRating()));
                holder.touristicPriceLevel.setText(String.valueOf(list.get(position).getPrice_level()));
                holder.touristicVicinity.setText(list.get(position).getMorada());
                holder.touristicLatitude.setText(list.get(position).getGeometry().getAsJsonObject("location").get("lat").toString());
                holder.touristicLongitude.setText(list.get(position).getGeometry().getAsJsonObject("location").get("lng").toString());

            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        holder.parentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, TouristicDetail.class);
                Log.d("INFOOOO", "A enviar:\n" +
                        String.valueOf(mute) + "\n" +
                        holder.touristicPhotoReference.getText() + "\n" +
                        holder.touristicName.getText() + "\n" +
                        holder.touristicOpen.getText() + "\n" +
                        holder.touristicRating.getText());

                intent.putExtra("photo_reference", holder.touristicPhotoReference.getText());
                intent.putExtra("name", holder.touristicName.getText());
                intent.putExtra("open", holder.touristicOpen.getText());
                intent.putExtra("rating", holder.touristicRating.getText());
                intent.putExtra("price_level", holder.touristicPriceLevel.getText());
                intent.putExtra("vicinity", holder.touristicVicinity.getText());
                intent.putExtra("latitude_longitude", LATITUDE_LONGITUDE);
                intent.putExtra("mute", String.valueOf(mute));
                intent.putExtra("latitude_place", holder.touristicLatitude.getText());
                intent.putExtra("longitude_place", holder.touristicLongitude.getText());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}