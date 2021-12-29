package com.example.passenger.Menus.configuration;

import android.content.Intent;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.passenger.Menus.menus.Login;
import com.example.passenger.Menus.menus.StandByAssistant;
import com.example.passenger.R;

import java.util.Locale;

public class ConfigFinalComment extends AppCompatActivity {
    private Button buttonSeguinte;
    private ImageView backButton;
    private TextToSpeech tts = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initializeVoice();
        setContentView(R.layout.scroll_config_final_coment_page);

        buttonSeguinte = (Button) findViewById(R.id.buttonSeguinteFinalComment);
        backButton = (ImageView) findViewById(R.id.back);

        buttonSeguinte.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ConfigFinalComment.this, StandByAssistant.class);
                intent.putExtra("type", "register");
                startActivity(intent);
            }
        });

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ConfigFinalComment.this, ConfigHoraAlmoco.class);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onStop() {
        super.onStop();
        tts.stop();
    }

    public void initializeVoice() {
        tts = new TextToSpeech(this, initStatus -> {
            if (initStatus == TextToSpeech.SUCCESS) {
                tts.setLanguage(new Locale("pt", "POR"));
                String comment = ((TextView) findViewById(R.id.commentFinal)).getText().toString();
                tts.speak(comment, TextToSpeech.QUEUE_FLUSH, null);
            } else {
                Log.d("TAG", "Can't initialize TextToSpeech");
            }
        });
    }
}