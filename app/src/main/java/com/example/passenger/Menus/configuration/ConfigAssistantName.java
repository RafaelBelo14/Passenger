package com.example.passenger.Menus.configuration;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
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

public class ConfigAssistantName extends AppCompatActivity {
    private Button buttonSeguinte;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private TextToSpeech tts = null;
    private FirebaseAuth.AuthStateListener authStateListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        initializeVoice();
        setContentView(R.layout.scroll_config_name_page);

        buttonSeguinte = (Button) findViewById(R.id.buttonSeguinteName);
        buttonSeguinte.setOnClickListener(v -> {

            FirebaseUser user = mAuth.getCurrentUser();
            Context context = getApplicationContext();
            int duration = Toast.LENGTH_SHORT;

            EditText assistent_name_layout = (EditText) findViewById(R.id.inputNome);
            String assistent_name = assistent_name_layout.getText().toString();

            HashMap<String,String> mapassistant = new HashMap<>();
            if (assistent_name.equals("")) {
                Toast.makeText(context, "Escolhe um nome!", duration).show();
            }
            else {
                mapassistant.put("assistant_name", assistent_name);

                CollectionReference users = db.collection("users");
                db.collection("users").whereEqualTo("id",user.getUid())
                        .get()
                        .addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    db.collection("users").document(document.getId()).set(mapassistant, SetOptions.merge());
                                    Intent I = new Intent(ConfigAssistantName.this, ConfigRelation.class);
                                    startActivity(I);
                                }
                            } else {
                                Toast.makeText(context, "Erro a processar o pedido! Tenta novamente mais tarde.", duration).show();
                            }
                        });
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
                String question = ((TextView) findViewById(R.id.questionNomeAssistente)).getText().toString();
                TextView name_layout1 = (TextView) findViewById(R.id.heyHeading);

                //TODO ACEDER À BASE DE DADOS PARA IR BUSCAR O NOME ( NÃO FUNCIONA COM getIntent() )
                name_layout1.setText("Olá, " + getIntent().getExtras().getString("userName") + "!");
                tts.speak(name_layout1.getText().toString() + " " + question, TextToSpeech.QUEUE_FLUSH, null);
            } else {
                Log.d("TAG", "Can't initialize TextToSpeech");
            }
        });
    }
}