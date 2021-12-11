package com.example.passenger.Objects;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Random;

// Amiga - 0
// Guia - 1

public class Assitant implements Serializable {
    private int tipoRelacao;
    private ArrayList<String> welcomeAmigavel = new ArrayList<String>() {
        {
            add("Que bom ter te aqui ");
            add("Viva ");
            add("Já tinha saudades, ");
            add("Olá olá, ");
            add("Já tinha saudades, ");
            add("Bons olhos te vejam, ");
        }
    };
    private ArrayList<String> welcomeGuia = new ArrayList<String>() {
        {
            add("Prazer em ter te aqui ");
            add("Olá ");
            add("Estou pronta para te acompanhar, ");
            add("Olá, como estás ");
        }
    };

    public Assitant(int tipoRelacao) {
        this.tipoRelacao = tipoRelacao;
    }

    public int getTipoRelacao() {
        return tipoRelacao;
    }

    public void setTipoRelacao(int tipoRelacao) {
        this.tipoRelacao = tipoRelacao;
    }

    public String sayWelcome() {
        if (tipoRelacao == 0) {
            int rnd = new Random().nextInt(welcomeAmigavel.size());
            return welcomeAmigavel.get(rnd);
        }
        else {
            int rnd = new Random().nextInt(welcomeGuia.size());
            return welcomeGuia.get(rnd);
        }
    }
}
