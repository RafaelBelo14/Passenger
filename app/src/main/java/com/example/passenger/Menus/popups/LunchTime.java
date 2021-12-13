package com.example.passenger.Menus.popups;

import android.content.Intent;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.example.passenger.Menus.restaurant.RestaurantList;
import com.example.passenger.R;

import java.util.Locale;

public class LunchTime extends AppCompatActivity {
    private TextToSpeech tts = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.scroll_hora_almoco);
        initializeVoice();
    }

    public void initializeVoice() {
        tts = new TextToSpeech(this, initStatus -> {
            if (initStatus == TextToSpeech.SUCCESS) {
                tts.setLanguage(new Locale("pt", "POR"));
                tts.speak("Oh chavalo, tá na hora do almoço!", TextToSpeech.QUEUE_FLUSH, null);
            } else {
                Log.d("TAG", "Can't initialize TextToSpeech");
            }

        });
    }

    public void seeRestaurants(View view) {
        Intent intent = new Intent(LunchTime.this, RestaurantList.class);
        startActivity(intent);
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
}