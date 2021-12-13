package com.example.passenger.Menus.configuration;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.passenger.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.SetOptions;

import java.util.HashMap;
import java.util.Locale;

public class ConfigRelation extends AppCompatActivity {
    private Button buttonSeguinte;
    private ImageView backButton;
    private RadioButton rb1;
    private RadioButton rb2;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private TextToSpeech tts = null;
    private String relation = null;
    private FirebaseAuth.AuthStateListener authStateListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        initializeVoice();
        setContentView(R.layout.scroll_config_relation_page);

        buttonSeguinte = (Button) findViewById(R.id.buttonSeguinteRelation);
        rb1 = (RadioButton) findViewById(R.id.amiga);
        rb2 = (RadioButton) findViewById(R.id.guia);
        backButton = (ImageView) findViewById(R.id.back);

        buttonSeguinte.setOnClickListener(v -> {

            FirebaseUser user = mAuth.getCurrentUser();
            Context context = getApplicationContext();
            int duration = Toast.LENGTH_SHORT;
            HashMap<String, String> mapassistant = new HashMap<>();

            if (relation == null) {
                Toast.makeText(context, "Escolha uma opção!", duration).show();
            }
            else {
                mapassistant.put("relation", relation);
                CollectionReference users = db.collection("users");
                db.collection("users").whereEqualTo("id", user.getUid())
                        .get()
                        .addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    db.collection("users").document(document.getId()).set(mapassistant, SetOptions.merge());
                                    Intent I = new Intent(ConfigRelation.this, ConfigConhecerMelhor.class);
                                    startActivity(I);
                                }
                            } else {
                                Toast.makeText(context, "Erro a processar o pedido! Tenta novamente mais tarde.", duration).show();
                            }
                        });
            }
        });

        rb1.setOnClickListener(new android.view.View.OnClickListener() {
            @Override
            public void onClick(android.view.View v) {
                relation = "amiga";
            }
        });

        rb2.setOnClickListener(new android.view.View.OnClickListener() {
            @Override
            public void onClick(android.view.View v) {
                relation = "guia";
            }
        });

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ConfigRelation.this, ConfigAssistantName.class);
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
                String question = ((TextView) findViewById(R.id.questionRelacao)).getText().toString();
                tts.speak(question, TextToSpeech.QUEUE_FLUSH, null);
            } else {
                Log.d("TAG", "Can't initialize TextToSpeech");
            }
        });
    }
}