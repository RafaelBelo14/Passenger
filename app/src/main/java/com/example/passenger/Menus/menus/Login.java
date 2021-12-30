package com.example.passenger.Menus.menus;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.passenger.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Login extends AppCompatActivity {
    private Button login;
    private TextView changeToRegister;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener authStateListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.scroll_login_page);
        mAuth = FirebaseAuth.getInstance();

        authStateListener = firebaseAuth -> {
            FirebaseUser user = firebaseAuth.getCurrentUser();
            if (user != null) {
                Intent I = new Intent(Login.this, StandByAssistant.class);
                I.putExtra("type", "login");
                startActivity(I);
            } else {
                Toast.makeText(Login.this, "Login to continue", Toast.LENGTH_SHORT).show();
            }
        };

        login = (Button) findViewById(R.id.entrarButton);
        changeToRegister = (TextView) findViewById(R.id.textViewRegister);

        login.setOnClickListener(view -> {

            EditText email_layout = (EditText) findViewById(R.id.inputLoginEmail);
            EditText password_layout = (EditText) findViewById(R.id.inputLoginPassword);
            String email = email_layout.getText().toString();
            String password = password_layout.getText().toString();

            if (email.isEmpty()) {
                email_layout.setError("Insere o teu email");
                email_layout.requestFocus();
            } else if (password.isEmpty()) {
                password_layout.setError("Insere a tua password");
                password_layout.requestFocus();
            } else {
                mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(Login.this, new OnCompleteListener() {
                    @Override
                    public void onComplete(@NonNull Task task) {

                        if (!task.isSuccessful()) {
                            Toast.makeText(Login.this.getApplicationContext(),
                                    "SignUp unsuccessful: " + task.getException().getMessage(),
                                    Toast.LENGTH_SHORT).show();
                        } else {
                            Intent intent = new Intent(Login.this, StandByAssistant.class);
                            intent.putExtra("type", "login");
                            startActivity(intent);
                        }
                    }
                });
            }
        });

        changeToRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Login.this, Register.class));
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(authStateListener);
    }

}