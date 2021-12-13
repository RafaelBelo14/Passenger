package com.example.passenger.Menus.menus;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.example.passenger.Menus.popups.LunchTime;
import com.example.passenger.R;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Calendar;

public class StandByAssistant extends AppCompatActivity {
    private Thread checkTime = null;
    private Button startButton;
    public DrawerLayout drawerLayout;
    private SwitchCompat switchCompat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.drawer_start_assistant_page);

        drawerLayout = findViewById(R.id.drawerLayout);

        switchCompat = findViewById(R.id.switchCompat);
        switchCompat.setSwitchPadding(40);
        switchCompat.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Toast.makeText(StandByAssistant.this, "Switch clicked!", Toast.LENGTH_SHORT).show();
            }
        });

        startButton = (Button) findViewById(R.id.standByButton);
        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(StandByAssistant.this, MainMenuAssistant.class);
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

    public void clickSair(View view) {
        Intent intent = new Intent(StandByAssistant.this, MainActivity.class);
        FirebaseAuth.getInstance().signOut();
        startActivity(intent);
    }

    @Override
    protected void onStart() {
        super.onStart();
        checkTime = new Thread(() -> {
            while(true) {
                Log.i("TIME", "A verificar tempo...");
                Calendar rightNow = Calendar.getInstance();

                if (rightNow.get(Calendar. HOUR_OF_DAY) == 15 && rightNow.get(Calendar. MINUTE) == 14) {
                    Intent intent = new Intent(StandByAssistant.this, LunchTime.class);
                    startActivity(intent);
                    checkTime.interrupt();
                } try {
                    checkTime.sleep(60000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        checkTime.start();
    }

    @Override
    protected void onPause() {
        super.onPause();
        drawerLayout.closeDrawer(GravityCompat.END);
    }
}