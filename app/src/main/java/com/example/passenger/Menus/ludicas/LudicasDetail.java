package com.example.passenger.Menus.ludicas;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.bumptech.glide.Glide;
import com.example.passenger.Menus.menus.EditProfile;
import com.example.passenger.Menus.menus.MainActivity;
import com.example.passenger.Menus.menus.StandByAssistant;
import com.example.passenger.Menus.touristic.TouristicDetail;
import com.example.passenger.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Locale;
import java.util.Random;

public class LudicasDetail extends AppCompatActivity {
    private boolean mute;
    private Button queroIr;
    public DrawerLayout drawerLayout;
    private SwitchCompat switchCompat;
    private ImageView image;
    private TextView name;
    private TextView vicinity;
    private TextView price_level;
    private TextView open_hours;
    private ImageView star1, star2, star3, star4, star5;
    private String KEY = "AIzaSyBN0JoU7O597v0dOTCJ-oINVvoxe9BzAAM";
    private TextToSpeech tts = null;
    private double latitude;
    private double longitude;
    private double latitude_place;
    private double longitude_place;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private String relation = null;
    private ArrayList<String> falasAmiga = new ArrayList<String>() {
        {
            add("Adorei a escolha! Boa viagem até lá!");
            add("Uhhh, estou bastante curiosa para ver como é!");
            add("Diverte te!");
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.drawer_ludicas_item_detail);
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        initViews();
        getRelacion();
        initIntentExtras();

        switchCompat.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (mute) {
                    mute = false;
                }
                else {
                    mute = true;
                    if (new Random().nextInt(10) == 5) {
                        mute = false;
                        Toast.makeText(LudicasDetail.this, "Assistant enabled itself!", Toast.LENGTH_SHORT).show();
                        switchCompat.setChecked(false);
                    }
                }
            }
        });

        queroIr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!mute) {
                    if (relation.equals("amiga")) {
                        tts.speak(falasAmiga.get(new Random().nextInt(falasAmiga.size())), TextToSpeech.QUEUE_FLUSH, null);
                    }
                    while (tts.isSpeaking()) {
                        Toast.makeText(LudicasDetail.this, "Assistant is talking", Toast.LENGTH_SHORT).show();
                    }
                }
                Intent navigation = new Intent(Intent.ACTION_VIEW, Uri
                        .parse("http://maps.google.com/maps?saddr="
                                + latitude + "," + longitude +
                                "&daddr=" + latitude_place + "," + longitude_place));
                startActivity(navigation);
            }
        });
    }

    public void initViews() {
        drawerLayout = findViewById(R.id.drawerLayout);
        switchCompat = findViewById(R.id.switchCompat);
        switchCompat.setSwitchPadding(40);
        queroIr = (Button) findViewById(R.id.queroIrButton);
        image = findViewById(R.id.detalhesLudicasImage);
        name = findViewById(R.id.detalhesLudicasName);
        vicinity = findViewById(R.id.detalhesLudicasVicinity);
        price_level = findViewById(R.id.detalhesLudicasPriceLevel);
        open_hours = findViewById(R.id.detalhesLudicasOpen);
        star1 = findViewById(R.id.detalhesStar1);
        star2 = findViewById(R.id.detalhesStar2);
        star3 = findViewById(R.id.detalhesStar3);
        star4 = findViewById(R.id.detalhesStar4);
        star5 = findViewById(R.id.detalhesStar5);
    }

    public void initIntentExtras() {

        //latitude e longitude sitio
        latitude_place = Double.parseDouble(getIntent().getExtras().getString("latitude_place"));
        longitude_place = Double.parseDouble(getIntent().getExtras().getString("longitude_place"));

        //latitude e longitude pessoa
        latitude = Double.parseDouble(getIntent().getExtras().getString("latitude_longitude").split(",")[0]);
        longitude = Double.parseDouble(getIntent().getExtras().getString("latitude_longitude").split(",")[1]);

        //voice
        if (getIntent().getExtras() == null) {
            initializeVoice();
        } else if (getIntent().getExtras().getString("mute").equals("true")) {
            mute = true;
            switchCompat.setChecked(true);
        } else if (getIntent().getExtras().getString("mute").equals("false")) {
            mute = false;
            initializeVoice();
            switchCompat.setChecked(false);
        }

        //Name
        name.setText(getIntent().getExtras().getString("name"));

        //Vicinity
        if (getIntent().getExtras().getString("vicinity").equals("")) {
            vicinity.setText("Sem informação");
        }
        else {
            vicinity.setText(getIntent().getExtras().getString("vicinity"));
        }

        //Price_level
        if (getIntent().getExtras().getString("price_level").equals("1")) {
            price_level.setText("$");
        } else if (getIntent().getExtras().getString("price_level").equals("2")) {
            price_level.setText("$$");
        } else if (getIntent().getExtras().getString("price_level").equals("3")){
            price_level.setText("$$$");
        } else {
            price_level.setText("-");
        }

        //Open
        if (getIntent().getExtras().getString("open").equals("true")){
            open_hours.setText("Aberto");
        }
        else if (getIntent().getExtras().getString("open").equals("false")) {
            open_hours.setText("Fechado");
        }
        else {
            open_hours.setText("Sem informação");
        }

        //Image
        if (!getIntent().getExtras().getString("photo_reference").isEmpty()) {
            String url = "https://maps.googleapis.com/maps/api/place/photo" +
                    "?maxwidth=200" +
                    "&photoreference=" + getIntent().getExtras().getString("photo_reference") +
                    "&key=" + KEY;

            Glide.with(this)
                    .load(url)
                    .into(image);
        }

        //Rating
        if (!getIntent().getExtras().getString("rating").isEmpty()) {
            double rate = Double.valueOf(getIntent().getExtras().getString("rating"));
            rate = Math.round(rate);
            if (rate == 0.0) {
                star1.setImageResource(R.drawable.ic_baseline_star_border_24);
                star2.setImageResource(R.drawable.ic_baseline_star_border_24);
                star3.setImageResource(R.drawable.ic_baseline_star_border_24);
                star4.setImageResource(R.drawable.ic_baseline_star_border_24);
                star5.setImageResource(R.drawable.ic_baseline_star_border_24);
            }
            else if (rate == 1.0) {
                star1.setImageResource(R.drawable.ic_star);
                star2.setImageResource(R.drawable.ic_baseline_star_border_24);
                star3.setImageResource(R.drawable.ic_baseline_star_border_24);
                star4.setImageResource(R.drawable.ic_baseline_star_border_24);
                star5.setImageResource(R.drawable.ic_baseline_star_border_24);
            }
            else if (rate == 2.0) {
                star1.setImageResource(R.drawable.ic_star);
                star2.setImageResource(R.drawable.ic_star);
                star3.setImageResource(R.drawable.ic_baseline_star_border_24);
                star4.setImageResource(R.drawable.ic_baseline_star_border_24);
                star5.setImageResource(R.drawable.ic_baseline_star_border_24);
            }
            else if (rate == 3.0) {
                star1.setImageResource(R.drawable.ic_star);
                star2.setImageResource(R.drawable.ic_star);
                star3.setImageResource(R.drawable.ic_star);
                star4.setImageResource(R.drawable.ic_baseline_star_border_24);
                star5.setImageResource(R.drawable.ic_baseline_star_border_24);
            }
            else if (rate == 4.0) {
                star1.setImageResource(R.drawable.ic_star);
                star2.setImageResource(R.drawable.ic_star);
                star3.setImageResource(R.drawable.ic_star);
                star4.setImageResource(R.drawable.ic_star);
                star5.setImageResource(R.drawable.ic_baseline_star_border_24);
            }
            else if (rate == 5.0) {
                star1.setImageResource(R.drawable.ic_star);
                star2.setImageResource(R.drawable.ic_star);
                star3.setImageResource(R.drawable.ic_star);
                star4.setImageResource(R.drawable.ic_star);
                star5.setImageResource(R.drawable.ic_star);
            }
        }
    }

    public void initializeVoice() {
        tts = new TextToSpeech(this, initStatus -> {
            if (initStatus == TextToSpeech.SUCCESS) {
                tts.setLanguage(new Locale("pt", "POR"));
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
        Intent intent = new Intent(LudicasDetail.this, EditProfile.class);
        intent.putExtra("mute", String.valueOf(mute));
        startActivityForResult(intent, 0);
    }

    public void clickExit(View view) {
        Intent intent = new Intent(LudicasDetail.this, StandByAssistant.class);
        intent.putExtra("mute", String.valueOf(mute));
        startActivity(intent);
    }

    public void clickBack(View view) {
        Intent intent = new Intent(LudicasDetail.this, LudicasList.class);
        intent.putExtra("latitude_longitude", getIntent().getExtras().getString("latitude_longitude"));
        intent.putExtra("mute", String.valueOf(mute));
        startActivity(intent);
    }

    public void clickSair(View view) {
        Intent intent = new Intent(LudicasDetail.this, MainActivity.class);
        FirebaseAuth.getInstance().signOut();
        startActivity(intent);
    }

    public void getRelacion() {
        FirebaseUser user = mAuth.getCurrentUser();
        db.collection("users")
                .whereEqualTo("id", user.getUid())
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                relation = document.get("relation").toString();
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
}