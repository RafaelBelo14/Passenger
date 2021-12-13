package com.example.passenger.Menus.restaurant;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.example.passenger.Menus.menus.MainActivity;
import com.example.passenger.Menus.menus.MainMenuAssistant;
import com.example.passenger.Menus.menus.StandByAssistant;
import com.example.passenger.R;
import com.google.firebase.auth.FirebaseAuth;

public class RestaurantDetail extends AppCompatActivity {
    private Button queroIr;
    private TextView verOutros;
    private DrawerLayout drawerLayout;
    private SwitchCompat switchCompat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.drawer_restaurant_item_detail);

        drawerLayout = findViewById(R.id.drawerLayout);

        switchCompat = findViewById(R.id.switchCompat);
        switchCompat.setSwitchPadding(40);
        switchCompat.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Toast.makeText(RestaurantDetail.this, "Switch clicked!", Toast.LENGTH_SHORT).show();
            }
        });

        queroIr = (Button) findViewById(R.id.queroIrButton);
        verOutros = (TextView) findViewById(R.id.outrasOpcoes);

        queroIr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RestaurantDetail.this, MainMenuAssistant.class);
                startActivity(intent);
            }
        });

        verOutros.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RestaurantDetail.this, RestaurantList.class);
                startActivity(intent);
            }
        });
    }

    public void clickMenu(View view) {
        StandByAssistant.openDrawer(drawerLayout);
    }

    public static void openDrawer(DrawerLayout drawerLayout) {
        drawerLayout.openDrawer(GravityCompat.END);
    }

    public void clickExit(View view) {
        Intent intent = new Intent(RestaurantDetail.this, StandByAssistant.class);
        startActivity(intent);
    }

    public void clickBack(View view) {
        Intent intent = new Intent(RestaurantDetail.this, RestaurantList.class);
        startActivity(intent);
    }

    public void clickSair(View view) {
        Intent intent = new Intent(RestaurantDetail.this, MainActivity.class);
        FirebaseAuth.getInstance().signOut();
        startActivity(intent);
    }

    @Override
    protected void onPause() {
        super.onPause();
        drawerLayout.closeDrawer(GravityCompat.END);
    }
}