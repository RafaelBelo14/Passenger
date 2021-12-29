package com.example.passenger.Menus.restaurant;

import android.content.Intent;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.passenger.Menus.api.GoogleApiRetrofit;
import com.example.passenger.Menus.api.GoogleResponse;
import com.example.passenger.Menus.api.GoogleRestaurantAdapter;
import com.example.passenger.Menus.api.Item;
import com.example.passenger.Menus.menus.EditProfile;
import com.example.passenger.Menus.menus.MainActivity;
import com.example.passenger.Menus.menus.MainMenuAssistant;
import com.example.passenger.Menus.menus.StandByAssistant;
import com.example.passenger.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Random;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RestaurantList extends AppCompatActivity {
    private boolean mute;
    private DrawerLayout drawerLayout;
    private SwitchCompat switchCompat;
    private TextView errorMessage;
    private RecyclerView dataListView;
    private final static String URL_GOOGLE = "https://maps.googleapis.com/maps/api/";
    private final static String KEY = "AIzaSyBN0JoU7O597v0dOTCJ-oINVvoxe9BzAAM";
    private final static String RADIUS = "500";
    private final static String TYPE_RESTAURANT = "restaurant";
    private TextToSpeech tts = null;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private GoogleRestaurantAdapter adapter;
    private String relation = null;
    private ArrayList<String> falasAmiga = new ArrayList<String>() {
        {
            add("Hum, estou a ver que estás com fome. Tens aqui os restaurantes mais perto de ti.");
            add("Existem imensas opções boas perto de ti, escolhe um bom restaurante para irmos.");
            add("Também estava com fome, o que queres comer?");
        }
    };

    private ArrayList<String> falasGuia = new ArrayList<String>() {
        {
            add("Apresento-lhe a lista de restaurantes mais próximos.");
            add("Agora pode escolher o restaurante que mais gostar.");
            add("Escolha o restaurante que achar melhor.");
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.drawer_restaurant_list);
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        initViews();
        initIntentExtras();
        makeGithubSearchQuery();

        switchCompat.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (mute) {
                    mute = false;
                    adapter.setMute(false);
                } else {
                    mute = true;
                    adapter.setMute(true);
                    if (new Random().nextInt(10) == 5) {
                        mute = false;
                        Toast.makeText(RestaurantList.this, "Assistant enabled itself!", Toast.LENGTH_SHORT).show();
                        switchCompat.setChecked(false);
                    }
                }
            }
        });
    }

    private void initViews() {
        errorMessage = findViewById(R.id.errorMessage);
        drawerLayout = findViewById(R.id.drawerLayout);
        dataListView = findViewById(R.id.restaurantListViewer);
        switchCompat = findViewById(R.id.switchCompat);
        switchCompat.setSwitchPadding(40);
    }

    public void initIntentExtras() {
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

    public void clickBack(View view) {
        Intent intent = new Intent(RestaurantList.this, MainMenuAssistant.class);
        intent.putExtra("mute", String.valueOf(mute));
        startActivity(intent);
    }

    public void clickMenu(View view) {
        StandByAssistant.openDrawer(drawerLayout);
    }

    public static void openDrawer(DrawerLayout drawerLayout) {
        drawerLayout.openDrawer(GravityCompat.END);
    }

    public void clickEditProfile(View view) {
        Intent intent = new Intent(RestaurantList.this, EditProfile.class);
        intent.putExtra("mute", String.valueOf(mute));
        startActivityForResult(intent, 0);
    }

    public void clickExit(View view) {
        Intent intent = new Intent(RestaurantList.this, StandByAssistant.class);
        intent.putExtra("mute", String.valueOf(mute));
        startActivity(intent);
    }

    public void clickSair(View view) {
        Intent intent = new Intent(RestaurantList.this, MainActivity.class);
        FirebaseAuth.getInstance().signOut();
        startActivity(intent);
    }

    private void showErrorMessage() {
        errorMessage.setVisibility(View.VISIBLE);
        dataListView.setVisibility(View.INVISIBLE);
    }

    private void showRestaurants() {
        dataListView.setVisibility(View.VISIBLE);
        errorMessage.setVisibility(View.INVISIBLE);
    }

    @Override
    protected void onPause() {
        super.onPause();
        drawerLayout.closeDrawer(GravityCompat.END);
    }

    private void makeGithubSearchQuery() {
        OkHttpClient client = getOkHttpClient();
        Retrofit retrofit = getRetrofit(client);
        GoogleApiRetrofit service = retrofit.create(GoogleApiRetrofit.class);

        Call<GoogleResponse> call = service.listRepo(KEY, getIntent().getExtras().getString("latitude_longitude"), RADIUS, TYPE_RESTAURANT);
        call.enqueue(new Callback<GoogleResponse>() {
            @Override
            public void onResponse(Call<GoogleResponse> call, Response<GoogleResponse> response) {
                if (response.isSuccessful()) {
                    int statusCode = response.code();
                    System.out.println(statusCode);
                    printResults(response.body().getItemList());
                    showRestaurants();
                }
            }

            @Override
            public void onFailure(Call<GoogleResponse> call, Throwable t) {
                showErrorMessage();
                t.printStackTrace();
            }
        });
    }

    private void printResults(List<Item> items) {
        dataListView = findViewById(R.id.restaurantListViewer);
        adapter = new GoogleRestaurantAdapter(this, items, getIntent().getExtras().getString("latitude_longitude"), mute);
        Log.i("R/MUTE", String.valueOf(mute));
        dataListView.setAdapter(adapter);
        dataListView.setLayoutManager(new LinearLayoutManager(this));
    }

    @NonNull
    private Retrofit getRetrofit(OkHttpClient client) {
        return new Retrofit.Builder()
                .baseUrl(URL_GOOGLE)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build();
    }

    @NonNull
    private OkHttpClient getOkHttpClient() {
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        return new OkHttpClient.Builder().addInterceptor(loggingInterceptor).build();
    }
}