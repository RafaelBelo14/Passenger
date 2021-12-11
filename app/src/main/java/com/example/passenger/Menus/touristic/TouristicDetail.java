package com.example.passenger.Menus.touristic;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.passenger.Menus.menus.MainMenuAssistant;
import com.example.passenger.R;

public class TouristicDetail extends AppCompatActivity {
    private Button queroIr;
    private TextView verOutros;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.scroll_view_touristic_item_detail);

        queroIr = (Button) findViewById(R.id.queroIrButton);
        verOutros = (TextView) findViewById(R.id.outrasOpcoes);

        queroIr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TouristicDetail.this, MainMenuAssistant.class);
                startActivity(intent);
            }
        });

        verOutros.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TouristicDetail.this, TouristicList.class);
                startActivity(intent);
            }
        });
    }
}