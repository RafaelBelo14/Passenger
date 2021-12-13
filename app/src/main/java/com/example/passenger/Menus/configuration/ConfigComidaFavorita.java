package com.example.passenger.Menus.configuration;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
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

public class ConfigComidaFavorita extends AppCompatActivity {
    private Button buttonSeguinte;
    private ImageView backButton;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private TextToSpeech tts = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        initializeVoice();
        setContentView(R.layout.scroll_config_comida_favorita_page);

        buttonSeguinte = (Button) findViewById(R.id.buttonSeguinteComidaFavorita);
        backButton = (ImageView) findViewById(R.id.back);

        buttonSeguinte.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                FirebaseUser user = mAuth.getCurrentUser();
                Context context = getApplicationContext();
                int duration = Toast.LENGTH_SHORT;

                EditText comida_layout = (EditText) findViewById(R.id.inputComidaFavorita);
                String comida = comida_layout.getText().toString();

                HashMap<String,String> mapassistant = new HashMap<>();
                if (comida.equals("")) {
                    Toast.makeText(context, "Escolhe uma comida!", duration).show();
                }
                else {
                    mapassistant.put("comida_favorita", comida);

                    CollectionReference users = db.collection("users");
                    db.collection("users").whereEqualTo("id",user.getUid())
                            .get()
                            .addOnCompleteListener(task -> {
                                if (task.isSuccessful()) {
                                    for (QueryDocumentSnapshot document : task.getResult()) {
                                        db.collection("users").document(document.getId()).set(mapassistant, SetOptions.merge());
                                        Intent intent = new Intent(ConfigComidaFavorita.this, ConfigComidaComment.class);
                                        startActivity(intent);
                                    }
                                } else {
                                    Toast.makeText(context, "Erro a processar o pedido! Tenta novamente mais tarde.", duration).show();
                                }
                            });
                }
            }
        });

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ConfigComidaFavorita.this, ConfigDestinoSonho.class);
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
                String question = ((TextView) findViewById(R.id.questionComidaFavorita)).getText().toString();
                tts.speak(question, TextToSpeech.QUEUE_FLUSH, null);
            } else {
                Log.d("TAG", "Can't initialize TextToSpeech");
            }
        });
    }
}