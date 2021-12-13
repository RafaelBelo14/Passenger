package com.example.passenger.Menus.menus;

import android.content.Intent;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.passenger.Objects.Assitant;
import com.example.passenger.Objects.User;
import com.example.passenger.R;

import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private String relation = null;
    private TextToSpeech tts = null;
    private User userAfterLogin = null;
    private Assitant assistant = null;
    private Button buttonLogin;
    private Button buttonRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.scroll_main_page);
        initializeVoice();

        buttonLogin = (Button) findViewById(R.id.entrar);
        buttonRegister = (Button) findViewById(R.id.registar);

        buttonLogin.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                goLogin();
            }
        });

        buttonRegister.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                goRegister();
            }
        });

    }

    @Override
    protected void onStop() {
        super.onStop();
        tts.stop();
    }

    public void goLogin() {
        Intent intent = new Intent(this, Login.class);
        startActivity(intent);
    }

    public void goRegister() {
        Intent intent = new Intent(this, Register.class);
        startActivity(intent);
    }

    public void initializeVoice() {
        tts = new TextToSpeech(this, initStatus -> {
            if (initStatus == TextToSpeech.SUCCESS) {
                tts.setLanguage(new Locale("pt", "POR"));
                tts.speak("Prazer em ter te aqui!", TextToSpeech.QUEUE_FLUSH, null);
            } else {
                Log.d("TAG", "Can't initialize TextToSpeech");
            }

        });
    }
}