package com.example.passenger.Menus.menus;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.passenger.R;

public class MainActivity extends AppCompatActivity {

    private Button buttonLogin;
    private Button buttonRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.scroll_main_page);

        buttonLogin = (Button) findViewById(R.id.entrar);
        buttonRegister = (Button) findViewById(R.id.registar);

        buttonLogin.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                goLogin();
            }
        });

        buttonRegister.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                goRegister();
            }
        });

    }

    public void goLogin() {
        Intent intent = new Intent(this, Login.class);
        startActivity(intent);
    }

    public void goRegister() {
        Intent intent = new Intent(this, Register.class);
        startActivity(intent);
    }
}