package com.example.passenger.Menus.popups;

import android.location.LocationManager;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.passenger.R;

import java.util.Locale;

public class BedTime extends AppCompatActivity {
    private TextToSpeech tts = null;
    private TextView hora;
    private LocationManager locationManager;
    private boolean mute;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.scroll_hora_de_dormir);
        hora = (TextView) findViewById(R.id.hora_de_dormir_hora);
        initIntentExtras();
        initializeVoice();
    }

    public void initIntentExtras() {
        if (getIntent().getExtras() == null) {
            initializeVoice();
            Toast.makeText(BedTime.this, "No information about assistent voice", Toast.LENGTH_SHORT).show();
        } else if (getIntent().getExtras().getString("mute").equals("true")) {
            mute = true;
            Toast.makeText(BedTime.this, "Muted!", Toast.LENGTH_SHORT).show();
        } else if (getIntent().getExtras().getString("mute").equals("false")) {
            mute = false;
            initializeVoice();
            Toast.makeText(BedTime.this, "Unmuted!", Toast.LENGTH_SHORT).show();
        }

        hora.setText(getIntent().getExtras().getString("bedTime"));
    }

    public void initializeVoice() {
        tts = new TextToSpeech(this, initStatus -> {
            if (initStatus == TextToSpeech.SUCCESS) {
                tts.setLanguage(new Locale("pt", "POR"));
                tts.speak("Oh chavalo, tá na hora de ir dormir!", TextToSpeech.QUEUE_FLUSH, null);
            } else {
                Log.d("TAG", "Can't initialize TextToSpeech");
            }

        });
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