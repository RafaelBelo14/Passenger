package com.example.passenger.Menus.popups;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.passenger.Menus.restaurant.RestaurantList;
import com.example.passenger.R;

import java.util.List;
import java.util.Locale;

public class DinnerTime extends AppCompatActivity implements LocationListener {
    private TextToSpeech tts = null;
    private TextView hora;
    private LocationManager locationManager;
    private boolean mute;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.scroll_hora_almoco);
        hora = (TextView) findViewById(R.id.hora_almoço_hora);
        initIntentExtras();
        initializeVoice();
    }

    public void initIntentExtras() {
        if (getIntent().getExtras() == null) {
            initializeVoice();
            Toast.makeText(DinnerTime.this, "No information about assistent voice", Toast.LENGTH_SHORT).show();
        } else if (getIntent().getExtras().getString("mute").equals("true")) {
            mute = true;
            Toast.makeText(DinnerTime.this, "Muted!", Toast.LENGTH_SHORT).show();
        } else if (getIntent().getExtras().getString("mute").equals("false")) {
            mute = false;
            initializeVoice();
            Toast.makeText(DinnerTime.this, "Unmuted!", Toast.LENGTH_SHORT).show();
        }

        hora.setText(getIntent().getExtras().getString("dinnerTime"));
    }

    public void initializeVoice() {
        tts = new TextToSpeech(this, initStatus -> {
            if (initStatus == TextToSpeech.SUCCESS) {
                tts.setLanguage(new Locale("pt", "POR"));
                tts.speak("Oh chavalo, tá na hora de jantar!", TextToSpeech.QUEUE_FLUSH, null);
            } else {
                Log.d("TAG", "Can't initialize TextToSpeech");
            }

        });
    }

    public void seeRestaurants(View view) {
        getLocation();
    }

    public void onReject(View view) {
        finish();
    }

    @Override
    protected void onPause() {
        super.onPause();
        finish();
    }

    @Override
    protected void onStop() {
        super.onStop();
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        finish();
    }

    @SuppressLint("MissingPermission")
    private void getLocation() {

        try {
            locationManager = (LocationManager) getApplicationContext().getSystemService(LOCATION_SERVICE);
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,5000,5, DinnerTime.this);
        } catch (Exception e){
            e.printStackTrace();
        }

    }

    @Override
    public void onLocationChanged(Location location) {
        try {
            Geocoder geocoder = new Geocoder(DinnerTime.this, Locale.getDefault());
            List<Address> addresses = geocoder.getFromLocation(location.getLatitude(),location.getLongitude(),1);

            Intent intent = new Intent(DinnerTime.this, RestaurantList.class);
            intent.putExtra("latitude_longitude", location.getLatitude()+","+location.getLongitude());
            intent.putExtra("mute", String.valueOf(mute));
            startActivity(intent);


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