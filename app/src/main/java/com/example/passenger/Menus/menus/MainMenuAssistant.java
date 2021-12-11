package com.example.passenger.Menus.menus;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.passenger.Menus.ludicas.LudicasList;
import com.example.passenger.Menus.restaurant.RestaurantList;
import com.example.passenger.Menus.touristic.TouristicList;
import com.example.passenger.R;
import com.google.firebase.auth.FirebaseAuth;

public class MainMenuAssistant extends AppCompatActivity {
    private Button restaurantButton;
    private Button ludicasButton;
    private Button touristicButton;
    private ImageView exitSessionButton;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener authStateListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.scroll_activity_main_page);
        mAuth = FirebaseAuth.getInstance();

        restaurantButton = (Button) findViewById(R.id.restaurantesButton);
        ludicasButton = (Button) findViewById(R.id.atracaoLudicaButton);
        touristicButton = (Button) findViewById(R.id.atracaoTuristicaButton);
        exitSessionButton = (ImageView) findViewById(R.id.exitSession);

        restaurantButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainMenuAssistant.this, RestaurantList.class);
                startActivity(intent);
            }
        });

        ludicasButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainMenuAssistant.this, LudicasList.class);
                startActivity(intent);
            }
        });

        touristicButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainMenuAssistant.this, TouristicList.class);
                startActivity(intent);
            }
        });

        exitSessionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAuth.signOut();
                Intent intent = new Intent(MainMenuAssistant.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }
}