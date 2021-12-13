package com.example.passenger.Menus.ludicas;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.passenger.Menus.api.GoogleApiRetrofit;
import com.example.passenger.Menus.api.GoogleLudicasAdapter;
import com.example.passenger.Menus.api.GoogleResponse;
import com.example.passenger.Menus.api.Item;
import com.example.passenger.Menus.menus.MainActivity;
import com.example.passenger.Menus.menus.MainMenuAssistant;
import com.example.passenger.Menus.menus.StandByAssistant;
import com.example.passenger.R;
import com.google.firebase.auth.FirebaseAuth;

import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class LudicasList extends AppCompatActivity {
    private DrawerLayout drawerLayout;
    private SwitchCompat switchCompat;
    private RecyclerView dataListView;
    private final static String URL_GOOGLE = "https://maps.googleapis.com/maps/api/";
    private final static String KEY = "AIzaSyBN0JoU7O597v0dOTCJ-oINVvoxe9BzAAM";
    private final static String RADIUS = "1000";
    private final static String TYPE_RESTAURANT = "night_club";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.drawer_ludicas_list);
        initViews();
        makeGithubSearchQuery();
        switchCompat = findViewById(R.id.switchCompat);
        switchCompat.setSwitchPadding(40);
        switchCompat.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Toast.makeText(LudicasList.this, "Switch clicked!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void initViews() {
        drawerLayout = findViewById(R.id.drawerLayout);
        dataListView = findViewById(R.id.ludicasListViewer);
    }

    public void clickMenu(View view) {
        StandByAssistant.openDrawer(drawerLayout);
    }

    public static void openDrawer(DrawerLayout drawerLayout) {
        drawerLayout.openDrawer(GravityCompat.END);
    }

    public void clickExit(View view) {
        Intent intent = new Intent(LudicasList.this, StandByAssistant.class);
        startActivity(intent);
    }

    public void clickBack(View view) {
        Intent intent = new Intent(LudicasList.this, MainMenuAssistant.class);
        startActivity(intent);
    }

    public void clickSair(View view) {
        Intent intent = new Intent(LudicasList.this, MainActivity.class);
        FirebaseAuth.getInstance().signOut();
        startActivity(intent);
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
                }
            }

            @Override
            public void onFailure(Call<GoogleResponse> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    private void printResults(List<Item> items) {
        dataListView = findViewById(R.id.ludicasListViewer);
        GoogleLudicasAdapter adapter = new GoogleLudicasAdapter(this, items);
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