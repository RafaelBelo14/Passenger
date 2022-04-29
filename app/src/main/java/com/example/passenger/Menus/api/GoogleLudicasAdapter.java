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
import com.example.passenger.Menus.ludicas.LudicasDetail;
import com.example.passenger.R;

import java.util.List;

public class GoogleLudicasAdapter extends RecyclerView.Adapter<GoogleLudicasAdapter.ViewHolder> {

    private final Context context;
    private final List<Item> list;
    private final static String KEY = "API_KEY";
    private String LATITUDE_LONGITUDE;
    private boolean mute;

    public GoogleLudicasAdapter(Context context, List<Item> list, String latitude_longitude, boolean mute) {
        this.context = context;
        this.list = list;
        this.LATITUDE_LONGITUDE = latitude_longitude;
        this.mute = mute;
    }

    public void setMute(boolean mute) {
        this.mute = mute;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView ludicasImage;
        TextView ludicasName;
        TextView ludicasOpen;
        TextView ludicasRating;
        TextView ludicasVicinity;
        TextView ludicasPriceLevel;
        TextView ludicasPhotoReference;
        TextView ludicasLatitude;
        TextView ludicasLongitude;
        RelativeLayout parentLayout;

        public ViewHolder(View view) {
            super(view);
            ludicasPhotoReference = view.findViewById(R.id.ludicasPhotoReference);
            ludicasImage = view.findViewById(R.id.ludicasImage);
            ludicasName = view.findViewById(R.id.ludicasName);
            ludicasOpen = view.findViewById(R.id.ludicasOpen);
            ludicasRating = view.findViewById(R.id.ludicasRating);
            ludicasVicinity = view.findViewById(R.id.ludicasVinicity);
            ludicasPriceLevel = view.findViewById(R.id.ludicasPriceLevel);
            ludicasLatitude = view.findViewById(R.id.ludicasLatitude);
            ludicasLongitude = view.findViewById(R.id.ludicasLongitude);
            parentLayout = view.findViewById(R.id.ludicasItem);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.ludicas_item, parent, false);

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
                        .into(holder.ludicasImage);
                holder.ludicasPhotoReference.setText(list.get(position).getIcon().get(0).getPhoto_reference());
                holder.ludicasName.setText(list.get(position).getName());
                if (list.get(position).getOpening_hours() == null) {
                    holder.ludicasOpen.setText("Sem informação");
                }
                else {
                    if (list.get(position).getOpening_hours().get("open_now").toString().equals("true")) {
                        holder.ludicasOpen.setText("Aberto");
                    }
                    else {
                        holder.ludicasOpen.setText("Fechado");
                    }
                }
                holder.ludicasRating.setText(String.valueOf(list.get(position).getRating()));
                holder.ludicasPriceLevel.setText(String.valueOf(list.get(position).getPrice_level()));
                holder.ludicasVicinity.setText(list.get(position).getMorada());
                holder.ludicasLatitude.setText(list.get(position).getGeometry().getAsJsonObject("location").get("lat").toString());
                holder.ludicasLongitude.setText(list.get(position).getGeometry().getAsJsonObject("location").get("lng").toString());
            }
            else {
                holder.ludicasName.setText(list.get(position).getName());
                if (list.get(position).getOpening_hours() == null) {
                    holder.ludicasOpen.setText("Sem informação");
                }
                else {
                    if (list.get(position).getOpening_hours().get("open_now").toString().equals("true")) {
                        holder.ludicasOpen.setText("Aberto");
                    }
                    else {
                        holder.ludicasOpen.setText("Fechado");
                    }
                }

                holder.ludicasRating.setText(String.valueOf(list.get(position).getRating()));
                holder.ludicasPriceLevel.setText(String.valueOf(list.get(position).getPrice_level()));
                holder.ludicasVicinity.setText(list.get(position).getMorada());
                holder.ludicasLatitude.setText(list.get(position).getGeometry().getAsJsonObject("location").get("lat").toString());
                holder.ludicasLongitude.setText(list.get(position).getGeometry().getAsJsonObject("location").get("lng").toString());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        holder.parentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, LudicasDetail.class);
                Log.d("INFOOOO", "A enviar:\n" +
                        String.valueOf(mute) + "\n" +
                        holder.ludicasPhotoReference.getText() + "\n" +
                        holder.ludicasName.getText() + "\n" +
                        holder.ludicasOpen.getText() + "\n" +
                        holder.ludicasRating.getText());

                intent.putExtra("photo_reference", holder.ludicasPhotoReference.getText());
                intent.putExtra("name", holder.ludicasName.getText());
                intent.putExtra("open", holder.ludicasOpen.getText());
                intent.putExtra("rating", holder.ludicasRating.getText());
                intent.putExtra("price_level", holder.ludicasPriceLevel.getText());
                intent.putExtra("vicinity", holder.ludicasVicinity.getText());
                intent.putExtra("latitude_longitude", LATITUDE_LONGITUDE);
                intent.putExtra("mute", String.valueOf(mute));
                intent.putExtra("latitude_place", holder.ludicasLatitude.getText());
                intent.putExtra("longitude_place", holder.ludicasLongitude.getText());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}
