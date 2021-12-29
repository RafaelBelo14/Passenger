package com.example.passenger.Menus.menus;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.passenger.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;

import java.util.HashMap;
import java.util.Locale;

public class EditProfile extends AppCompatActivity {
    private boolean mute;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private TextToSpeech tts = null;
    private EditText inputName;
    private RadioButton inputRelacionAmiga;
    private RadioButton inputRelacionGuia;
    private EditText inputAlergia;
    private TimePicker inputHoraAlmoco;
    private TimePicker inputHoraJantar;
    private TimePicker inputHoraDeitar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.scroll_edit_profile_page);
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        initViews();
        initUserDatabaseInfo();
        initIntentExtras();
    }

    public void initViews() {
        inputName = (EditText) findViewById(R.id.editInputName);
        inputAlergia = (EditText) findViewById(R.id.editInputAlergia);
        inputRelacionAmiga = (RadioButton) findViewById(R.id.editRelationAmiga);
        inputRelacionGuia = (RadioButton) findViewById(R.id.editRelationGuia);
        inputHoraAlmoco = (TimePicker) findViewById(R.id.editTimePickerAlmoco);
        inputHoraJantar = (TimePicker) findViewById(R.id.editTimePickerJantar);
        inputHoraDeitar= (TimePicker) findViewById(R.id.editTimePickerDeitar);
    }

    public void initIntentExtras() {
        if (getIntent().getExtras() == null) {
        } else if (getIntent().getExtras().getString("mute").equals("true")) {
            mute = true;
        } else if (getIntent().getExtras().getString("mute").equals("false")) {
            mute = false;
            initializeVoice();
        }
    }

    public void initUserDatabaseInfo() {
        getName();
        getAlergia();
        getRelation();
        getHoraAlmoço();
        getHoraJantar();
        getBedTimeHour();
    }

    public void initializeVoice() {
        tts = new TextToSpeech(this, initStatus -> {
            if (initStatus == TextToSpeech.SUCCESS) {
                tts.setLanguage(new Locale("pt", "POR"));
                //TODO meter frases na main menu assistant
                //tts.speak("Come me o cu", TextToSpeech.QUEUE_FLUSH, null);
            } else {
                Log.d("TAG", "Can't initialize TextToSpeech");
            }
        });
    }

    public void clickBack(View view) {
        Intent intent = new Intent();
        intent.putExtra("mute", String.valueOf(mute));
        setResult(0, intent);
        finish();
    }

    public void clickSave(View view) {
        FirebaseUser user = mAuth.getCurrentUser();
        Context context = getApplicationContext();
        int duration = Toast.LENGTH_SHORT;

        HashMap<String,String> mapassistant = new HashMap<>();
        if (!inputName.equals("")) {
            mapassistant.put("name", inputName.getText().toString());
        }

        if (inputRelacionAmiga.isChecked()) {
            mapassistant.put("relation", "amiga");
        } else {
            mapassistant.put("relation", "guia");
        }

        if (!inputAlergia.equals("")) {
            mapassistant.put("alergia_alimentar", inputAlergia.getText().toString());
        }

        mapassistant.put("hora_almoco", inputHoraAlmoco.getHour() + ":" + inputHoraAlmoco.getMinute());
        mapassistant.put("hora_jantar", inputHoraJantar.getHour() + ":" + inputHoraJantar.getMinute());
        mapassistant.put("hora_deitar", inputHoraDeitar.getHour() + ":" + inputHoraDeitar.getMinute());

        CollectionReference users = db.collection("users");
        db.collection("users").whereEqualTo("id",user.getUid())
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            db.collection("users").document(document.getId()).set(mapassistant, SetOptions.merge());
                            finish();
                        }
                    } else {
                        Toast.makeText(context, "Erro a processar o pedido! Tenta novamente mais tarde.", duration).show();
                    }
                });
    }

    public void getName() {
        FirebaseUser user = mAuth.getCurrentUser();
        db.collection("users")
                .whereEqualTo("id", user.getUid())
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                String name = document.get("name").toString();
                                inputName.setText(name);
                            }
                        }
                    }
                });
    }

    public void getAlergia() {
        FirebaseUser user = mAuth.getCurrentUser();
        db.collection("users")
                .whereEqualTo("id", user.getUid())
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                String alergia = document.get("alergia_alimentar").toString();
                                inputAlergia.setText(alergia);
                            }
                        }
                    }
                });
    }

    public void getRelation() {
        FirebaseUser user = mAuth.getCurrentUser();
        db.collection("users")
                .whereEqualTo("id", user.getUid())
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                String relation = document.get("relation").toString();
                                if (relation.equals("amiga")) {
                                    inputRelacionAmiga.setChecked(true);
                                }
                                else {
                                    inputRelacionGuia.setChecked(true);
                                }
                            }
                        }
                    }
                });
    }

    public void getHoraAlmoço() {
        FirebaseUser user = mAuth.getCurrentUser();
        db.collection("users")
                .whereEqualTo("id", user.getUid())
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                String hora_almoco = document.get("hora_almoco").toString();
                                inputHoraAlmoco.setHour(Integer.parseInt(hora_almoco.split(":")[0]));
                                inputHoraAlmoco.setMinute(Integer.parseInt(hora_almoco.split(":")[1]));
                            }
                        }
                    }
                });
    }

    public void getHoraJantar() {
        FirebaseUser user = mAuth.getCurrentUser();
        db.collection("users")
                .whereEqualTo("id", user.getUid())
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                String hora_jantar = document.get("hora_jantar").toString();
                                inputHoraJantar.setHour(Integer.parseInt(hora_jantar.split(":")[0]));
                                inputHoraJantar.setMinute(Integer.parseInt(hora_jantar.split(":")[1]));
                            }
                        }
                    }
                });
    }

    public void getBedTimeHour() {
        FirebaseUser user = mAuth.getCurrentUser();
        db.collection("users")
                .whereEqualTo("id", user.getUid())
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                String hora_deitar = document.get("hora_deitar").toString();
                                inputHoraDeitar.setHour(Integer.parseInt(hora_deitar.split(":")[0]));
                                inputHoraDeitar.setMinute(Integer.parseInt(hora_deitar.split(":")[1]));
                            }
                        }
                    }
                });
    }
}