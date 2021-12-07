package com.example.passenger;

import java.io.Serializable;

public class Assitant implements Serializable {
    private String nome;
    private int tipoRelacao;

    public Assitant(String nome, int tipoRelacao) {
        this.nome = nome;
        this.tipoRelacao = tipoRelacao;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public int getTipoRelacao() {
        return tipoRelacao;
    }

    public void setTipoRelacao(int tipoRelacao) {
        this.tipoRelacao = tipoRelacao;
    }
}
