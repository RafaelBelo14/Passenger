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

import com.example.passenger.Menus.configuration.ConfigAssistantName;
import com.example.passenger.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;

public class Register extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private Button buttonRegister;
    private TextView changeToLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.scroll_register_page);
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        buttonRegister = (Button) findViewById(R.id.buttonRegister);
        changeToLogin = (TextView) findViewById(R.id.changeToLogin);

        buttonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                EditText name_layout = (EditText) findViewById(R.id.inputName);
                EditText email_layout = (EditText) findViewById(R.id.inputEmail);
                EditText password_layout = (EditText) findViewById(R.id.inputPassword);

                String name = name_layout.getText().toString();
                String email = email_layout.getText().toString();
                String password = password_layout.getText().toString();

                if (name.isEmpty()) {
                    name_layout.setError("Provide your Email first!");
                    name_layout.requestFocus();
                } else if (email.isEmpty()) {
                    email_layout.setError("Provide your Email first!");
                    email_layout.requestFocus();
                } else if (password.isEmpty()) {
                    password_layout.setError("Set your password");
                    password_layout.requestFocus();
                } else if (!(name.isEmpty() && email.isEmpty() && password.isEmpty())) {
                    mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(Register.this, new OnCompleteListener() {
                        @Override
                        public void onComplete(@NonNull Task task) {

                            if (!task.isSuccessful()) {
                                Toast.makeText(Register.this.getApplicationContext(),
                                        "SignUp unsuccessful: " + task.getException().getMessage(),
                                        Toast.LENGTH_SHORT).show();
                            } else {
                                FirebaseUser user = mAuth.getCurrentUser();
                                HashMap<String,String> usermap = new HashMap<>();

                                usermap.put("name", name);
                                usermap.put("email", user.getEmail());
                                usermap.put("id", user.getUid());

                                db.collection("users")
                                        .add(usermap);
                                Intent intent = new Intent(Register.this, ConfigAssistantName.class);
                                intent.putExtra("userName", name);
                                startActivity(intent);
                            }
                        }
                    });
                } else {
                    Toast.makeText(Register.this, "Error", Toast.LENGTH_SHORT).show();
                }
            }
        });

        changeToLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Register.this, Login.class));
            }
        });
    }
}