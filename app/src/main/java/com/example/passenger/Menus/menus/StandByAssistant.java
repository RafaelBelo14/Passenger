package com.example.passenger.Menus.menus;

import android.content.Intent;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.example.passenger.Menus.popups.BedTime;
import com.example.passenger.Menus.popups.DinnerTime;
import com.example.passenger.Menus.popups.LunchTime;
import com.example.passenger.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;
import java.util.Random;

public class StandByAssistant extends AppCompatActivity {
    private boolean mute;
    int hora_deitar_h;
    int hora_deitar_m;
    int hora_almoco_h;
    int hora_almoco_m;
    int hora_jantar_h;
    int hora_jantar_m;
    private Thread checkTimeLunch = null;
    private Thread checkBedTime = null;
    private Thread checkTimeDinner = null;
    private Button startButton;
    public DrawerLayout drawerLayout;
    private SwitchCompat switchCompat;
    private TextToSpeech tts = null;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private String relation = null;
    private ArrayList<String> falasAmiga = new ArrayList<String>() {
        {
            add("Olá, fico feliz em ver-te");
            add("Que bom ter-te aqui!");
            add("Olá, espero ver te aqui mais vezes!");
        }
    };

    private ArrayList<String> falasGuia = new ArrayList<String>() {
        {
            add("Seja bem-vindo!");
            add("Estou pronta para ajudar!");
            add("Olá, estarei aqui para acompanhar!");
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.drawer_start_assistant_page);
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        initViews();
        initIntentExtras();
        getHoraAlmoço();
        getHoraJantar();
        getBedTimeHour();

        switchCompat.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (mute) {
                    mute = false;
                } else {
                    mute = true;
                    if (new Random().nextInt(5) == 2) {
                        mute = false;
                        tts.speak("Vou fingir que clicaste no botão errado!", TextToSpeech.QUEUE_FLUSH, null);
                        while (tts.isSpeaking()) {
                        }
                        switchCompat.setChecked(false);
                    }
                }
            }
        });

        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(StandByAssistant.this, MainMenuAssistant.class);
                intent.putExtra("mute", String.valueOf(mute));
                startActivity(intent);
            }
        });
    }

    public void initViews() {
        drawerLayout = findViewById(R.id.drawerLayout);
        switchCompat = findViewById(R.id.switchCompat);
        switchCompat.setSwitchPadding(40);
        startButton = (Button) findViewById(R.id.standByButton);
    }

    public void initIntentExtras() {
        if (getIntent().getExtras().getString("type") != null) {
            initializeVoice();
        } else if (getIntent().getExtras().getString("mute").equals("true")) {
            mute = true;
            switchCompat.setChecked(true);
        } else if (getIntent().getExtras().getString("mute").equals("false")) {
            mute = false;
            switchCompat.setChecked(false);
            initializeVoice();
        }
    }

    public void initializeVoice() {
        tts = new TextToSpeech(this, initStatus -> {
            if (initStatus == TextToSpeech.SUCCESS) {
                tts.setLanguage(new Locale("pt", "POR"));
                FirebaseUser user = mAuth.getCurrentUser();
                if (getIntent().getExtras().getString("type") != null) {
                    db.collection("users")
                            .whereEqualTo("id", user.getUid())
                            .get()
                            .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                    if (task.isSuccessful()) {
                                        for (QueryDocumentSnapshot document : task.getResult()) {
                                            relation = document.get("relation").toString();
                                            if (relation.equals("amiga")) {
                                                Log.d("TAG", "amiga");
                                                tts.speak(falasAmiga.get(new Random().nextInt(falasAmiga.size())), TextToSpeech.QUEUE_FLUSH, null);
                                            } else {
                                                Log.d("TAG", "guia");
                                                tts.speak(falasGuia.get(new Random().nextInt(falasGuia.size())), TextToSpeech.QUEUE_FLUSH, null);
                                            }
                                        }
                                    }
                                }
                            });
                }
            } else {
                Log.d("TAG", "Can't initialize TextToSpeech");
            }
        });
    }

    public void clickMenu(View view) {
        StandByAssistant.openDrawer(drawerLayout);
    }

    public static void openDrawer(DrawerLayout drawerLayout) {
        drawerLayout.openDrawer(GravityCompat.END);
    }

    public void clickEditProfile(View view) {
        Intent intent = new Intent(StandByAssistant.this, EditProfile.class);
        intent.putExtra("mute", String.valueOf(mute));
        startActivityForResult(intent, 0);
    }

    public void clickSair(View view) {
        Intent intent = new Intent(StandByAssistant.this, MainActivity.class);
        FirebaseAuth.getInstance().signOut();
        startActivity(intent);
    }

    @Override
    protected void onStart() {

        super.onStart();
        checkTimeLunch = new Thread(() -> {
            while(true) {
                Log.i("TIME", "A verificar tempo..." + hora_almoco_h + ":" + hora_almoco_m);
                Calendar rightNow = Calendar.getInstance();

                if (rightNow.get(Calendar. HOUR_OF_DAY) == hora_almoco_h && rightNow.get(Calendar. MINUTE) == hora_almoco_m) {
                    Intent intent = new Intent(StandByAssistant.this, LunchTime.class);
                    intent.putExtra("lunchTime", hora_almoco_h + ":" + hora_almoco_m);
                    intent.putExtra("mute", String.valueOf(mute));
                    startActivity(intent);
                } try {
                    checkTimeLunch.sleep(60000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        checkTimeLunch.start();

        checkTimeDinner = new Thread(() -> {
            while(true) {
                Log.i("TIME", "A verificar tempo..." + hora_jantar_h + ":" + hora_jantar_m);
                Calendar rightNow = Calendar.getInstance();

                if (rightNow.get(Calendar. HOUR_OF_DAY) == hora_jantar_h && rightNow.get(Calendar. MINUTE) == hora_jantar_m) {
                    Intent intent = new Intent(StandByAssistant.this, DinnerTime.class);
                    intent.putExtra("dinnerTime", hora_jantar_h + ":" + hora_jantar_m);
                    intent.putExtra("mute", String.valueOf(mute));
                    startActivity(intent);
                } try {
                    checkTimeDinner.sleep(60000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        checkTimeDinner.start();

        checkBedTime = new Thread(() -> {
            while(true) {
                Log.i("TIME", "A verificar tempo..." + hora_deitar_h + ":" + hora_deitar_m);
                Calendar rightNow = Calendar.getInstance();

                if (rightNow.get(Calendar. HOUR_OF_DAY) == hora_deitar_h && rightNow.get(Calendar. MINUTE) == hora_deitar_m) {
                    Intent intent = new Intent(StandByAssistant.this, BedTime.class);
                    intent.putExtra("bedTime", hora_deitar_h + ":" + hora_deitar_m);
                    intent.putExtra("mute", String.valueOf(mute));
                    startActivity(intent);
                } try {
                    checkBedTime.sleep(60000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        checkBedTime.start();
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
                                hora_almoco_h = Integer.parseInt(hora_almoco.split(":")[0]);
                                hora_almoco_m = Integer.parseInt(hora_almoco.split(":")[1]);
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
                                hora_jantar_h = Integer.parseInt(hora_jantar.split(":")[0]);
                                hora_jantar_m = Integer.parseInt(hora_jantar.split(":")[1]);
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
                                hora_deitar_h = Integer.parseInt(hora_deitar.split(":")[0]);
                                hora_deitar_m = Integer.parseInt(hora_deitar.split(":")[1]);
                            }
                        }
                    }
                });
    }

    @Override
    protected void onPause() {
        super.onPause();
        drawerLayout.closeDrawer(GravityCompat.END);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (tts != null) {
            tts.stop();
        }
    }
}