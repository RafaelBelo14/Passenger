package com.example.passenger.Menus.restaurant;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import androidx.appcompat.app.AppCompatActivity;

import com.example.passenger.R;

public class RestaurantList extends AppCompatActivity {
    private RelativeLayout itemButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.restaurant_list);

        View inflating_view = findViewById(R.id.restaurantListViewer);
        ViewGroup parent =(ViewGroup) inflating_view.getParent();
        int index = parent.indexOfChild(inflating_view);
        parent.removeView(inflating_view);
        inflating_view = getLayoutInflater().inflate(R.layout.restaurant_item, parent, false);
        parent.addView(inflating_view, index);

        inflating_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RestaurantList.this, RestaurantDetail.class);
                startActivity(intent);
            }
        });
    }
}