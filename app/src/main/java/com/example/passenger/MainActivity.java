package com.example.passenger;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;

import java.util.HashMap;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private String relation = null;
    private TextToSpeech tts = null;
    private User userAfterLogin = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_page);
        initializeFirebase();
        initializeVoice();

    }

    public void initializeFirebase() {
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
    }

    public void initializeVoice() {
        tts = new TextToSpeech(this, initStatus -> {
            if (initStatus == TextToSpeech.SUCCESS) {
                tts.setLanguage(new Locale("pt", "POR"));
                tts.speak("Prazer em ter te aqui!", TextToSpeech.QUEUE_FLUSH, null);
            } else {
                Log.d("TAG", "Can't initialize TextToSpeech");
            }

        });
    }

    //----------------------------------------------------------------------------------

    public void goLogin(View view) {
        setContentView(R.layout.login_page);
    }
    public void goRegister(View view) {
        setContentView(R.layout.register_page);
    }

    //----------------------------------------------------------------------------------

    public void backConfig1(View view) {
        setContentView(R.layout.config_name_page);
    }
    @SuppressLint("SetTextI18n")
    public void goConfig1(View view) {

        Context context = getApplicationContext();
        int duration = Toast.LENGTH_SHORT;
        EditText name_layout = (EditText) findViewById(R.id.inputName);
        EditText password_layout = (EditText) findViewById(R.id.inputPassword);
        EditText email_layout = (EditText) findViewById(R.id.inputEmail);

        String name = name_layout.getText().toString();
        String email = email_layout.getText().toString();
        String password = password_layout.getText().toString();

        mAuth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this, task -> {
                if (task.isSuccessful()) {

                    FirebaseUser user = mAuth.getCurrentUser();
                    HashMap <String,String> usermap = new HashMap<>();

                    usermap.put("name", name);
                    usermap.put("email", user.getEmail());
                    usermap.put("id", user.getUid());

                    db.collection("users")
                            .add(usermap);
                    setContentView(R.layout.config_name_page);

                    String question = ((TextView) findViewById(R.id.questionNomeAssistente)).getText().toString();
                    TextView name_layout1 = (TextView) findViewById(R.id.heyHeading);
                    name_layout1.setText("Olá, " + name + "!");
                    tts.speak(name_layout1.getText().toString() + " " + question, TextToSpeech.QUEUE_FLUSH, null);

                } else {
                    Toast.makeText(context, "Falha na autenticação!", duration).show();
                }
            });
    }

    //----------------------------------------------------------------------------------

    public void backConfig2(View view) {
        setContentView(R.layout.config_relation_page);
    }
    public void goConfig2(View view) {

        FirebaseUser user = mAuth.getCurrentUser();
        Context context = getApplicationContext();
        int duration = Toast.LENGTH_SHORT;

        EditText assistent_name_layout = (EditText) findViewById(R.id.inputNome);
        String assistent_name = assistent_name_layout.getText().toString();

        HashMap <String,String> mapassistant = new HashMap<>();
        if (assistent_name.equals("")) {
            Toast.makeText(context, "Escolhe um nome!", duration).show();
        }
        else {
            mapassistant.put("assistant_name", assistent_name);

            CollectionReference users = db.collection("users");
            db.collection("users").whereEqualTo("id",user.getUid())
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            db.collection("users").document(document.getId()).set(mapassistant, SetOptions.merge());
                            setContentView(R.layout.config_relation_page);

                            String question = ((TextView) findViewById(R.id.questionRelacao)).getText().toString();
                            tts.speak(question, TextToSpeech.QUEUE_FLUSH, null);

                        }
                    } else {
                        Toast.makeText(context, "Erro a processar o pedido! Tenta novamente mais tarde.", duration).show();
                    }
                });
        }
    }

    //----------------------------------------------------------------------------------

    @SuppressLint("NonConstantResourceId")
    public void goConfig3(View view) {

        Context context = getApplicationContext();
        int duration = Toast.LENGTH_SHORT;
        HashMap<String, String> mapassistant = new HashMap<>();

        if (relation == null) {
            Toast.makeText(context, "Escolha uma opção!", duration).show();
        }
        else {
            mapassistant.put("relation", relation);
            FirebaseUser user = mAuth.getCurrentUser();
            CollectionReference users = db.collection("users");
            db.collection("users").whereEqualTo("id", user.getUid())
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            db.collection("users").document(document.getId()).set(mapassistant, SetOptions.merge());
                            setContentView(R.layout.config_conhecer_melhor_page);

                            String comment = ((TextView) findViewById(R.id.commentConhecerMelhor)).getText().toString();
                            tts.speak(comment, TextToSpeech.QUEUE_FLUSH, null);

                        }
                    } else {
                        Toast.makeText(context, "Erro a processar o pedido! Tenta novamente mais tarde.", duration).show();
                    }
                });
        }
    }

    //----------------------------------------------------------------------------------

    public void backConfig4(View view) {
        setContentView(R.layout.config_destino_sonho_page);
    }
    public void goConfig4(View view) {

        setContentView(R.layout.config_destino_sonho_page);
        String question = ((TextView) findViewById(R.id.questionDestinoSonho)).getText().toString();
        tts.speak(question, TextToSpeech.QUEUE_FLUSH, null);

    }

    //----------------------------------------------------------------------------------

    @SuppressLint("SetTextI18n")
    public void goConfig5(View view) {

        Context context = getApplicationContext();
        int duration = Toast.LENGTH_SHORT;
        EditText destinoSonho_layout = (EditText) findViewById(R.id.inputDestinoSonho);
        String destinoSonho = destinoSonho_layout.getText().toString();

        HashMap<String, String> mapassistant = new HashMap<>();
        if (destinoSonho.equals("")) {
            Toast.makeText(context, "Digita qualquer coisa!", duration).show();
        }
        else {
            mapassistant.put("destino_sonho", destinoSonho);
            FirebaseUser user = mAuth.getCurrentUser();
            CollectionReference users = db.collection("users");
            db.collection("users").whereEqualTo("id", user.getUid())
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            db.collection("users").document(document.getId()).set(mapassistant, SetOptions.merge());
                            setContentView(R.layout.config_destino_coment_page);

                            TextView comment = (TextView) findViewById(R.id.comentDestinoSonho);
                            comment.setText(destinoSonho + " " + comment.getText().toString());
                            tts.speak(comment.getText().toString(), TextToSpeech.QUEUE_FLUSH, null);

                        }
                    } else {
                        Toast.makeText(context, "Erro a processar o pedido! Tenta novamente mais tarde.", duration).show();
                    }
                });
        }
    }

    //----------------------------------------------------------------------------------

    public void backConfig6(View view) {
        setContentView(R.layout.config_comida_favorita_page);
    }
    public void goConfig6(View view) {
        setContentView(R.layout.config_comida_favorita_page);
        String question = ((TextView) findViewById(R.id.questionComidaFavorita)).getText().toString();
        tts.speak(question, TextToSpeech.QUEUE_FLUSH, null);
    }

    //----------------------------------------------------------------------------------

    public void goConfig7(View view) {
        Context context = getApplicationContext();
        int duration = Toast.LENGTH_SHORT;
        EditText comidaFavorita_layout = (EditText) findViewById(R.id.inputComidaFavorita);
        String comidaFavorita = comidaFavorita_layout.getText().toString();

        HashMap<String, String> mapassistant = new HashMap<>();
        if (comidaFavorita.equals("")) {
            Toast.makeText(context, "Digita qualquer coisa!", duration).show();
        }
        else {
            mapassistant.put("comida_favorita", comidaFavorita);
            FirebaseUser user = mAuth.getCurrentUser();
            CollectionReference users = db.collection("users");
            db.collection("users").whereEqualTo("id", user.getUid())
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            db.collection("users").document(document.getId()).set(mapassistant, SetOptions.merge());
                            setContentView(R.layout.config_comida_coment_page);

                            String comment = ((TextView) findViewById(R.id.commentComidaFavorita)).getText().toString();
                            tts.speak(comment, TextToSpeech.QUEUE_FLUSH, null);
                        }
                    } else {
                        Toast.makeText(context, "Erro a processar o pedido! Tenta novamente mais tarde.", duration).show();
                    }
                });
        }
    }

    //----------------------------------------------------------------------------------

    public void backConfig8(View view) {
        setContentView(R.layout.config_alergia_alimentar_page);
    }

    public void goConfig8(View view) {
        setContentView(R.layout.config_alergia_alimentar_page);
        String question = ((TextView) findViewById(R.id.questionAlergiaALimentar)).getText().toString();
        tts.speak(question, TextToSpeech.QUEUE_FLUSH, null);
    }

    //----------------------------------------------------------------------------------

    public void goConfig9(View view) {

        Context context = getApplicationContext();
        int duration = Toast.LENGTH_SHORT;
        EditText alergiaAlimentar_layout = (EditText) findViewById(R.id.inputAlergiaAlimentar);
        String alergiaAlimentar = alergiaAlimentar_layout.getText().toString();

        HashMap<String, String> mapassistant = new HashMap<>();
        if (alergiaAlimentar.equals("")) {
            Toast.makeText(context, "Digita qualquer coisa!", duration).show();
        }
        else {
            mapassistant.put("alergia_alimentar", alergiaAlimentar);
            FirebaseUser user = mAuth.getCurrentUser();
            CollectionReference users = db.collection("users");
            db.collection("users").whereEqualTo("id", user.getUid())
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            db.collection("users").document(document.getId()).set(mapassistant, SetOptions.merge());
                            setContentView(R.layout.config_alergia_coment_page);

                            String comment = ((TextView) findViewById(R.id.commentAlergiaAlimentar)).getText().toString();
                            if (alergiaAlimentar.equals("Não") || alergiaAlimentar.equals("não") || alergiaAlimentar.equals("Nao") || alergiaAlimentar.equals("nao")){
                                String comment2 = "Ainda bem que não tens qualquer alergia alimentar, é bom ver-te saudável!";
                                TextView comment_text = (TextView) findViewById(R.id.commentAlergiaAlimentar);
                                comment_text.setText(comment2);
                                tts.speak(comment2, TextToSpeech.QUEUE_FLUSH, null);
                            }
                            else{
                                tts.speak(comment, TextToSpeech.QUEUE_FLUSH, null);
                            }

                        }
                    } else {
                        Toast.makeText(context, "Erro a processar o pedido! Tenta novamente mais tarde.", duration).show();
                    }
                });
        }
    }

    //----------------------------------------------------------------------------------

    public void backConfig10(View view) {
        setContentView(R.layout.scroll_config_deitar_page);
    }
    public void goConfig10(View view) {
        setContentView(R.layout.scroll_config_deitar_page);
        String question = ((TextView) findViewById(R.id.questionDeitar)).getText().toString();
        tts.speak(question, TextToSpeech.QUEUE_FLUSH, null);
    }

    //----------------------------------------------------------------------------------

    public void backConfig11(View view) {
        setContentView(R.layout.config_refeicoes_page);
    }
    public void goConfig11(View view) {
        Context context = getApplicationContext();
        int duration = Toast.LENGTH_SHORT;
        TimePicker timePicker_layout = (TimePicker) findViewById(R.id.timePicker);
        int timePicker_hour = timePicker_layout.getHour();
        int timePicker_minute = timePicker_layout.getMinute();

        HashMap<String, String> mapassistant = new HashMap<>();
        mapassistant.put("hora_deitar", timePicker_hour + ":" + timePicker_minute);
        FirebaseUser user = mAuth.getCurrentUser();
        CollectionReference users = db.collection("users");
        db.collection("users").whereEqualTo("id", user.getUid())
            .get()
            .addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        db.collection("users").document(document.getId()).set(mapassistant, SetOptions.merge());
                        setContentView(R.layout.config_refeicoes_page);

                        String question = ((TextView) findViewById(R.id.questionRefeicoes)).getText().toString();
                        tts.speak(question, TextToSpeech.QUEUE_FLUSH, null);
                    }
                } else {
                    Toast.makeText(context, "Erro a processar o pedido! Tenta novamente mais tarde.", duration).show();
                }
            });
    }

    //----------------------------------------------------------------------------------

    public void goConfig12(View view) {
        Context context = getApplicationContext();
        int duration = Toast.LENGTH_SHORT;
        EditText refeicoes_layout = (EditText) findViewById(R.id.inputRefeicoes);
        String refeicoes = refeicoes_layout.getText().toString();

        HashMap<String, String> mapassistant = new HashMap<>();
        if (refeicoes.equals("")) {
            Toast.makeText(context, "Digita qualquer coisa!", duration).show();
        }
        else {
            mapassistant.put("refeicoes", refeicoes);
            FirebaseUser user = mAuth.getCurrentUser();
            CollectionReference users = db.collection("users");
            db.collection("users").whereEqualTo("id", user.getUid())
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            db.collection("users").document(document.getId()).set(mapassistant, SetOptions.merge());
                            setContentView(R.layout.config_final_comment_page);

                            String comment = ((TextView) findViewById(R.id.commentFinal)).getText().toString();
                            tts.speak(comment, TextToSpeech.QUEUE_FLUSH, null);
                        }
                    } else {
                        Toast.makeText(context, "Erro a processar o pedido! Tenta novamente mais tarde.", duration).show();
                    }
                });
        }
    }

    //----------------------------------------------------------------------------------

    public void goStartMenuFromRegister(View view) {
        setContentView(R.layout.start_assistent_page);
    }
    public void goStartMenuFromLogin(View view) {
        Context context = getApplicationContext();
        int duration = Toast.LENGTH_SHORT;
        EditText email_layout = (EditText) findViewById(R.id.inputLoginEmail);
        EditText password_layout = (EditText) findViewById(R.id.inputLoginPassword);

        String email = email_layout.getText().toString();
        String password = password_layout.getText().toString();

        mAuth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        FirebaseUser user = mAuth.getCurrentUser();

                        setContentView(R.layout.start_assistent_page);
                        importDataToUser(user.getUid());
                        tts.speak("Como é mambo?", TextToSpeech.QUEUE_FLUSH, null);

                    } else {
                        Toast.makeText(context, "Credenciais incorretas! Já estás registado?", duration).show();
                    }
                }
            });
    }

    //----------------------------------------------------------------------------------

    public void goMainMenu(View view) { setContentView(R.layout.activity_main_page); }

    //----------------------------------------------------------------------------------

    public void goRestaurantList(View view) {
        setContentView(R.layout.restaurant_list);
        View inflating_view = findViewById(R.id.restaurantListViewer);
        ViewGroup parent =(ViewGroup) inflating_view.getParent();
        int index = parent.indexOfChild(inflating_view);
        parent.removeView(inflating_view);
        inflating_view = getLayoutInflater().inflate(R.layout.restaurant_item, parent, false);
        parent.addView(inflating_view, index);
    }

    //----------------------------------------------------------------------------------

    public void goRestauratDetail(View view) { setContentView(R.layout.scroll_view_restaurant_item_detail); }

    //----------------------------------------------------------------------------------

    public void goLudicasList(View view) {
        setContentView(R.layout.ludicas_list);
        View inflating_view = findViewById(R.id.ludicasListViewer);
        ViewGroup parent =(ViewGroup) inflating_view.getParent();
        int index = parent.indexOfChild(inflating_view);
        parent.removeView(inflating_view);
        inflating_view = getLayoutInflater().inflate(R.layout.ludicas_item, parent, false);
        parent.addView(inflating_view, index);
    }

    //----------------------------------------------------------------------------------

    public void goLudicaDetail(View view) { setContentView(R.layout.scroll_view_ludicas_item_detail); }

    //----------------------------------------------------------------------------------

    public void goTouristicList(View view) {
        setContentView(R.layout.touristic_list);
        View inflating_view = findViewById(R.id.touristicListViewer);
        ViewGroup parent =(ViewGroup) inflating_view.getParent();
        int index = parent.indexOfChild(inflating_view);
        parent.removeView(inflating_view);
        inflating_view = getLayoutInflater().inflate(R.layout.touristic_item, parent, false);
        parent.addView(inflating_view, index);
    }

    //----------------------------------------------------------------------------------

    public void goTouristicDetail(View view) { setContentView(R.layout.scroll_view_touristic_item_detail); }

    //----------------------------------------------------------------------------------

    public void onRadioButtonClicked(View view) {

        RadioButton rb1 = (RadioButton) findViewById(R.id.amiga);

        if (rb1.isChecked()) {
            relation = "amiga";
        }
        else {
            relation = "guia";
        }
    }

    public void importDataToUser(String userId) {
        db.collection("users")
                .whereEqualTo("id", userId)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                userAfterLogin = new User(document.getData().get("name").toString(),
                                        document.getData().get("email").toString(),
                                        document.getData().get("assistant_name").toString(),
                                        document.getData().get("relation").toString(),
                                        document.getData().get("destino_sonho").toString(),
                                        document.getData().get("comida_favorita").toString(),
                                        document.getData().get("alergia_alimentar").toString(),
                                        document.getData().get("hora_deitar").toString(),
                                        document.getData().get("refeicoes").toString());

                                Log.d("CONAAA", userAfterLogin.getAssistant_name());
                            }
                        } else {
                            Log.d("TAG", "Error getting documents: ", task.getException());
                        }
                    }
                });
    }

}