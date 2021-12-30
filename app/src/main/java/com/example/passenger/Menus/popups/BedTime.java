package com.example.passenger.Menus.popups;

import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.passenger.R;

import java.util.Locale;

public class BedTime extends AppCompatActivity {
    private TextToSpeech tts = null;
    private TextView hora;
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
        } else if (getIntent().getExtras().getString("mute").equals("true")) {
            mute = true;
        } else if (getIntent().getExtras().getString("mute").equals("false")) {
            mute = false;
            initializeVoice();
        }

        hora.setText(getIntent().getExtras().getString("bedTime"));
    }

    public void initializeVoice() {
        tts = new TextToSpeech(this, initStatus -> {
            if (initStatus == TextToSpeech.SUCCESS) {
                tts.setLanguage(new Locale("pt", "POR"));
                tts.speak("Está na hora de ir dormir!", TextToSpeech.QUEUE_FLUSH, null);
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
        if (tts != null) {
            tts.stop();
        }
        finish();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (tts != null) {
            tts.stop();
        }
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (tts != null) {
            tts.stop();
        }
        finish();
    }
}