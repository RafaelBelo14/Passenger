package com.example.passenger.Menus.menus;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.passenger.R;

public class StandByAssistant extends AppCompatActivity {
    private Button startButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.start_assistent_page);

        startButton = (Button) findViewById(R.id.standByButton);
        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(StandByAssistant.this, MainMenuAssistant.class);
                startActivity(intent);
            }
        });
    }
}