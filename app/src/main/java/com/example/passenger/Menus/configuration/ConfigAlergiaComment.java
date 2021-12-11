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

import com.example.passenger.R;

import java.util.Locale;

public class ConfigAlergiaComment extends AppCompatActivity {
    private Button buttonSeguinte;
    private ImageView backButton;
    private TextToSpeech tts = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initializeVoice();
        setContentView(R.layout.scroll_config_alergia_coment_page);

        buttonSeguinte = (Button) findViewById(R.id.buttonSeguinteAlergiaComment);
        backButton = (ImageView) findViewById(R.id.back);

        buttonSeguinte.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ConfigAlergiaComment.this, ConfigHoraDeitar.class);
                startActivity(intent);
            }
        });

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ConfigAlergiaComment.this, ConfigAlergiaAlimentar.class);
                startActivity(intent);
            }
        });
    }

    public void initializeVoice() {
        tts = new TextToSpeech(this, initStatus -> {
            if (initStatus == TextToSpeech.SUCCESS) {
                tts.setLanguage(new Locale("pt", "POR"));

                String comment = ((TextView) findViewById(R.id.commentAlergiaAlimentar)).getText().toString();
                String alergia = getIntent().getExtras().getString("alergia");

                if (alergia.equals("Não") || alergia.equals("não") || alergia.equals("Nao") || alergia.equals("nao")){
                    String comment2 = "Ainda bem que não tens qualquer alergia alimentar, é bom ver-te saudável!";
                    TextView comment_text = (TextView) findViewById(R.id.commentAlergiaAlimentar);
                    comment_text.setText(comment2);
                    tts.speak(comment2, TextToSpeech.QUEUE_FLUSH, null);
                }
                else{
                    tts.speak(comment, TextToSpeech.QUEUE_FLUSH, null);
                }

            } else {
                Log.d("TAG", "Can't initialize TextToSpeech");
            }
        });
    }
}