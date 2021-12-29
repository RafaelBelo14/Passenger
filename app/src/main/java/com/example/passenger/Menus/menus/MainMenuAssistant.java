package com.example.passenger.Menus.menus;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.example.passenger.Menus.ludicas.LudicasList;
import com.example.passenger.Menus.restaurant.RestaurantList;
import com.example.passenger.Menus.touristic.TouristicList;
import com.example.passenger.R;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
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

public class MainMenuAssistant extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {
    private boolean mute;
    private Button restaurantButton;
    private Button ludicasButton;
    private Button touristicButton;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private DrawerLayout drawerLayout;
    private SwitchCompat switchCompat;
    private int buttonChoice = 0;
    private TextToSpeech tts = null;
    private String relation = null;
    private static int REQUEST_CODE_RECOVER_PLAY_SERVICES = 200;
    private ArrayList<String> falasAmiga = new ArrayList<String>() {
        {
            add("O que queres fazer agora?");
            add("Vamos fazer o quê?");
            add("Onde queres ir comigo?");
        }
    };

    private ArrayList<String> falasGuia = new ArrayList<String>() {
        {
            add("Qual das opções pretende escolher?");
            add("Que tipo de atividade quer fazer?");
            add("A que tipo de local gostaria de ir?");
        }
    };

    private GoogleApiClient mGoogleApiClient;
    private Location mLastLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.drawer_activity_main_page);
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        initViews();
        initIntentExtras();
        Log.i("AQUI", "1");
        if (checkGooglePlayServices()) {
            Log.i("AQUI", "2");
            buildGoogleApiClient();
        }

        switchCompat.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (mute) {
                    mute = false;
                } else {
                    mute = true;
                    if (new Random().nextInt(10) == 5) {
                        mute = false;
                        Toast.makeText(MainMenuAssistant.this, "Assistant enabled itself!", Toast.LENGTH_SHORT).show();
                        switchCompat.setChecked(false);
                    }
                }
            }
        });

        restaurantButton = (Button) findViewById(R.id.restaurantesButton);
        ludicasButton = (Button) findViewById(R.id.atracaoLudicaButton);
        touristicButton = (Button) findViewById(R.id.atracaoTuristicaButton);

        restaurantButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buttonChoice = 0;
                getLocation();
            }
        });

        ludicasButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buttonChoice = 1;
                getLocation();
            }
        });

        touristicButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buttonChoice = 2;
                getLocation();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (mGoogleApiClient != null) {
            mGoogleApiClient.connect();
        }

    }

    public void initViews() {
        drawerLayout = findViewById(R.id.drawerLayout);
        switchCompat = findViewById(R.id.switchCompat);
        switchCompat.setSwitchPadding(40);
    }

    public void initIntentExtras() {
        if (getIntent().getExtras() == null) {
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
        Intent intent = new Intent(MainMenuAssistant.this, EditProfile.class);
        intent.putExtra("mute", String.valueOf(mute));
        startActivityForResult(intent, 0);
    }

    public void clickSair(View view) {
        Intent intent = new Intent(MainMenuAssistant.this, MainActivity.class);
        FirebaseAuth.getInstance().signOut();
        startActivity(intent);
    }

    public void clickExit(View view) {
        Intent intent = new Intent(MainMenuAssistant.this, StandByAssistant.class);
        intent.putExtra("mute", String.valueOf(mute));
        startActivity(intent);
    }

    private boolean checkGooglePlayServices() {
        Log.i("AQUI", "3");
        int checkGooglePlayServices = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
        if (checkGooglePlayServices != ConnectionResult.SUCCESS) {
            Log.i("AQUI", "erro 1");
            GooglePlayServicesUtil.getErrorDialog(checkGooglePlayServices,
                    this, REQUEST_CODE_RECOVER_PLAY_SERVICES).show();

            return false;
        }
        Log.i("AQUI", "4");
        return true;
    }

    @Override
    protected void onPause() {
        super.onPause();
        drawerLayout.closeDrawer(GravityCompat.END);
    }

    public void getLocation() {
        try {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(MainMenuAssistant.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 100);
            }
            mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);

            if (mLastLocation != null) {
                if (buttonChoice == 0) {
                    Log.i("CLICK", "Clicked!");
                    Intent intent = new Intent(MainMenuAssistant.this, RestaurantList.class);
                    intent.putExtra("latitude_longitude", mLastLocation.getLatitude()+","+mLastLocation.getLongitude());
                    intent.putExtra("mute", String.valueOf(mute));
                    startActivity(intent);
                }
                else if (buttonChoice == 1) {
                    Intent intent = new Intent(MainMenuAssistant.this, LudicasList.class);
                    intent.putExtra("latitude_longitude", mLastLocation.getLatitude()+","+mLastLocation.getLongitude());
                    intent.putExtra("mute", String.valueOf(mute));
                    startActivity(intent);
                }
                else {
                    Intent intent = new Intent(MainMenuAssistant.this, TouristicList.class);
                    intent.putExtra("latitude_longitude", mLastLocation.getLatitude()+","+mLastLocation.getLongitude());
                    intent.putExtra("mute", String.valueOf(mute));
                    startActivity(intent);
                }
            }

        }catch (Exception e){
            e.printStackTrace();
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_RECOVER_PLAY_SERVICES) {

            if (resultCode == RESULT_OK) {
                // Make sure the app is not already connected or attempting to connect
                if (!mGoogleApiClient.isConnecting() &&
                        !mGoogleApiClient.isConnected()) {
                    mGoogleApiClient.connect();
                }
            } else if (resultCode == RESULT_CANCELED) {
                Toast.makeText(this, "Google Play Services must be installed.",
                        Toast.LENGTH_SHORT).show();
                finish();
            }
        }
    }

    protected synchronized void buildGoogleApiClient() {
        Log.i("AQUI", "5");
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();

    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        Log.i("AQUI", "6");
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MainMenuAssistant.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 100);
        }
        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(
                mGoogleApiClient);
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
}