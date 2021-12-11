package com.example.passenger.Menus.ludicas;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;

import androidx.appcompat.app.AppCompatActivity;

import com.example.passenger.R;

public class LudicasList extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ludicas_list);

        View inflating_view = findViewById(R.id.ludicasListViewer);
        ViewGroup parent =(ViewGroup) inflating_view.getParent();
        int index = parent.indexOfChild(inflating_view);
        parent.removeView(inflating_view);
        inflating_view = getLayoutInflater().inflate(R.layout.ludicas_item, parent, false);
        parent.addView(inflating_view, index);

        inflating_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LudicasList.this, LudicasDetail.class);
                startActivity(intent);
            }
        });
    }
}