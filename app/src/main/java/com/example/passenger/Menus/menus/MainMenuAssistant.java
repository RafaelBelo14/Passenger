package com.example.passenger.Menus.menus;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.example.passenger.Menus.ludicas.LudicasList;
import com.example.passenger.Menus.restaurant.RestaurantList;
import com.example.passenger.Menus.touristic.TouristicList;
import com.example.passenger.R;
import com.google.firebase.auth.FirebaseAuth;

import java.util.List;
import java.util.Locale;

public class MainMenuAssistant extends AppCompatActivity implements LocationListener {
    private boolean mute;
    private Button restaurantButton;
    private Button ludicasButton;
    private Button touristicButton;
    private FirebaseAuth mAuth;
    private DrawerLayout drawerLayout;
    private SwitchCompat switchCompat;
    private LocationManager locationManager;
    private int buttonChoice = 0;
    private TextToSpeech tts = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.drawer_activity_main_page);
        mAuth = FirebaseAuth.getInstance();
        initViews();
        initIntentExtras();

        if (ContextCompat.checkSelfPermission(MainMenuAssistant.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MainMenuAssistant.this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION},100);
        }

        switchCompat.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (mute) {
                    mute = false;
                    Toast.makeText(MainMenuAssistant.this, "Unmuted!", Toast.LENGTH_SHORT).show();
                } else {
                    mute = true;
                    Toast.makeText(MainMenuAssistant.this, "Muted!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        restaurantButton = (Button) findViewById(R.id.restaurantesButton);
        ludicasButton = (Button) findViewById(R.id.atracaoLudicaButton);
        touristicButton = (Button) findViewById(R.id.atracaoTuristicaButton);

        restaurantButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buttonChoice = 0;
                getLocation();
            }
        });

        ludicasButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buttonChoice = 1;
                getLocation();
            }
        });

        touristicButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buttonChoice = 2;
                getLocation();
            }
        });
    }

    public void initViews() {
        drawerLayout = findViewById(R.id.drawerLayout);
        switchCompat = findViewById(R.id.switchCompat);
        switchCompat.setSwitchPadding(40);
    }

    public void initIntentExtras() {
        if (getIntent().getExtras() == null) {
            Toast.makeText(MainMenuAssistant.this, "No information about assistent voice", Toast.LENGTH_SHORT).show();
        } else if (getIntent().getExtras().getString("mute").equals("true")) {
            mute = true;
            switchCompat.setChecked(true);
            Toast.makeText(MainMenuAssistant.this, "Muted!", Toast.LENGTH_SHORT).show();
        } else if (getIntent().getExtras().getString("mute").equals("false")) {
            mute = false;
            switchCompat.setChecked(false);
            initializeVoice();
            Toast.makeText(MainMenuAssistant.this, "Unmuted!", Toast.LENGTH_SHORT).show();
        }
    }

    public void initializeVoice() {
        tts = new TextToSpeech(this, initStatus -> {
            if (initStatus == TextToSpeech.SUCCESS) {
                tts.setLanguage(new Locale("pt", "POR"));
                //TODO meter frases na main menu assistant
                //tts.speak("Come me o cu", TextToSpeech.QUEUE_FLUSH, null);
            } else {
                Log.d("TAG", "Can't initialize TextToSpeech");
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
        Intent intent = new Intent(MainMenuAssistant.this, MainActivity.class);
        FirebaseAuth.getInstance().signOut();
        startActivity(intent);
    }

    public void clickExit(View view) {
        Intent intent = new Intent(MainMenuAssistant.this, StandByAssistant.class);
        intent.putExtra("mute", String.valueOf(mute));
        startActivity(intent);
    }

    @Override
    protected void onPause() {
        super.onPause();
        drawerLayout.closeDrawer(GravityCompat.END);
    }

    @SuppressLint("MissingPermission")
    private void getLocation() {

        try {
            locationManager = (LocationManager) getApplicationContext().getSystemService(LOCATION_SERVICE);
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,5000,5,MainMenuAssistant.this);
        } catch (Exception e){
            e.printStackTrace();
        }

    }

    @Override
    public void onLocationChanged(Location location) {
        try {
            Geocoder geocoder = new Geocoder(MainMenuAssistant.this, Locale.getDefault());
            List<Address> addresses = geocoder.getFromLocation(location.getLatitude(),location.getLongitude(),1);
            String address = addresses.get(0).getAddressLine(0);

            if (buttonChoice == 0) {
                Log.i("CLICK", "Clicked!");
                Intent intent = new Intent(MainMenuAssistant.this, RestaurantList.class);
                intent.putExtra("latitude_longitude", location.getLatitude()+","+location.getLongitude());
                intent.putExtra("mute", String.valueOf(mute));
                startActivity(intent);
            }
            else if (buttonChoice == 1) {
                Intent intent = new Intent(MainMenuAssistant.this, LudicasList.class);
                intent.putExtra("latitude_longitude", location.getLatitude()+","+location.getLongitude());
                intent.putExtra("mute", String.valueOf(mute));
                startActivity(intent);
            }
            else {
                Intent intent = new Intent(MainMenuAssistant.this, TouristicList.class);
                intent.putExtra("latitude_longitude", location.getLatitude()+","+location.getLongitude());
                intent.putExtra("mute", String.valueOf(mute));
                startActivity(intent);
            }


        }catch (Exception e){
            e.printStackTrace();
        }

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }
}