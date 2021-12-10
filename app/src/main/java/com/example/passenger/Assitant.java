package com.example.passenger;

import java.io.Serializable;

public class Assitant implements Serializable {
    private String nome;
    private String tipoRelacao;

    public Assitant(String nome, String tipoRelacao) {
        this.nome = nome;
        this.tipoRelacao = tipoRelacao;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getTipoRelacao() {
        return tipoRelacao;
    }

    public void setTipoRelacao(String tipoRelacao) {
        this.tipoRelacao = tipoRelacao;
    }
}
