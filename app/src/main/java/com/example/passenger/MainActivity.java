package com.example.passenger;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.util.Arrays;

public class MainActivity extends AppCompatActivity {

    private ReadNwriteDB db = null;
    private String dirPath = "/app/src/main/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_page);
        initializeDB();
        String test = Arrays.toString(db.getUserList().toArray());
        Log.i("info", test);
        User user = new User("Rafa", "r", "deef", "ef", "dqwdqw", new Hours(2, 3), new Hours(2, 3), new Hours(2, 3), new Hours(2, 3), 2);
        db.addUser(user);
        saveDB();
    }

    public void initializeDB() {
        try {
            readDB();
        } catch (FileNotFoundException f) {
            Log.i("info","Nenhuma db encontrado! A criar um...");
            db = new ReadNwriteDB();
        } catch (IOException | ClassNotFoundException i) {
            Log.i("info","Erro a ler ficheiro ou classe desconhecida");
            System.exit(0);
        }
    }

    public void saveDB() {
        writeDB();
    }

    public void readDB() throws IOException, ClassNotFoundException{
        FileInputStream fi = new FileInputStream(new File(new File(getFilesDir(),"") + File.separator + "db.srl"));
        ObjectInputStream oi = new ObjectInputStream(fi);

        db = (ReadNwriteDB) oi.readObject();

        oi.close();
        fi.close();
    }

    public void writeDB() {

        ObjectOutput out = null;

        try {
            out.close();
        } catch (FileNotFoundException e) {
            System.out.println("Erro a criar ficheiro...");
        } catch (IOException e) {
            System.out.println("Erro a escrever para o ficheiro...");
        }
    }

    public void goLogin(View view) { setContentView(R.layout.login_page); }
    public void goRegister(View view) {
        setContentView(R.layout.register_page);
    }

    //Configuration menus
    public void goConfig1(View view) {
        setContentView(R.layout.config_name_page);
    }
    public void goConfig2(View view) {
        setContentView(R.layout.config_relation_page);
    }
    public void goConfig3(View view) {
        setContentView(R.layout.config_conhecer_melhor_page);
    }
    public void goConfig4(View view) {
        setContentView(R.layout.config_destino_sonho_page);
    }
    public void goConfig5(View view) {
        setContentView(R.layout.config_destino_coment_page);
    }
    public void goConfig6(View view) {
        setContentView(R.layout.config_comida_favorita_page);
    }
    public void goConfig7(View view) {
        setContentView(R.layout.config_comida_coment_page);
    }
    public void goConfig8(View view) {
        setContentView(R.layout.config_alergia_alimentar_page);
    }
    public void goConfig9(View view) {
        setContentView(R.layout.config_alergia_coment_page);
    }
    public void goConfig10(View view) {
        setContentView(R.layout.config_deitar_page);
    }
    public void goConfig11(View view) {
        setContentView(R.layout.config_refeicoes_page);
    }
    public void goConfig12(View view) {
        setContentView(R.layout.config_final_comment_page);
    }
    public void goConfig13(View view) {
        setContentView(R.layout.config_end_page);
    }

    //Talk to assistent menus
    public void goStartMenu(View view) { setContentView(R.layout.start_assistent_page); }
    public void goMainMenu(View view) { setContentView(R.layout.activity_main_page); }

    //Restaurant menus
    public void goRestaurantList(View view) {
        setContentView(R.layout.restaurant_list);
        View inflating_view = findViewById(R.id.restaurantListViewer);
        ViewGroup parent =(ViewGroup) inflating_view.getParent();
        int index = parent.indexOfChild(inflating_view);
        parent.removeView(inflating_view);
        inflating_view = getLayoutInflater().inflate(R.layout.restaurant_item, parent, false);
        parent.addView(inflating_view, index);
    }
    public void goRestauratDetail(View view) { setContentView(R.layout.scroll_view_restaurant_item_detail); }

    //Ludicas menus
    public void goLudicasList(View view) {
        setContentView(R.layout.ludicas_list);
        View inflating_view = findViewById(R.id.ludicasListViewer);
        ViewGroup parent =(ViewGroup) inflating_view.getParent();
        int index = parent.indexOfChild(inflating_view);
        parent.removeView(inflating_view);
        inflating_view = getLayoutInflater().inflate(R.layout.ludicas_item, parent, false);
        parent.addView(inflating_view, index);
    }
    public void goLudicaDetail(View view) { setContentView(R.layout.scroll_view_ludicas_item_detail); }

    //Touristic menus
    public void goTouristicList(View view) {
        setContentView(R.layout.touristic_list);
        View inflating_view = findViewById(R.id.touristicListViewer);
        ViewGroup parent =(ViewGroup) inflating_view.getParent();
        int index = parent.indexOfChild(inflating_view);
        parent.removeView(inflating_view);
        inflating_view = getLayoutInflater().inflate(R.layout.touristic_item, parent, false);
        parent.addView(inflating_view, index);
    }
    public void goTouristicDetail(View view) { setContentView(R.layout.scroll_view_touristic_item_detail); }

}