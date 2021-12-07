package com.example.passenger;

import java.io.Serializable;

public class User implements Serializable {
    private String name;
    private String password;
    private String destinoSonho;
    private String comidaFavorita;
    private String alergiaAlimentar;
    private Hours horasDormir;
    private Hours horasAcordar;
    private Hours horasAlmoçar;
    private Hours horasJantar;
    private int numRefeicoes;

    public User(String name, String password, String destinoSonho, String comidaFavorita, String alergiaAlimentar, Hours horasDormir, Hours horasAcordar, Hours horasAlmoçar, Hours horasJantar, int numRefeicoes) {
        this.name = name;
        this.password = password;
        this.destinoSonho = destinoSonho;
        this.comidaFavorita = comidaFavorita;
        this.alergiaAlimentar = alergiaAlimentar;
        this.horasDormir = horasDormir;
        this.horasAcordar = horasAcordar;
        this.horasAlmoçar = horasAlmoçar;
        this.horasJantar = horasJantar;
        this.numRefeicoes = numRefeicoes;
    }

    public String getName() {
        return name;
    }

    public String getPassword() {
        return password;
    }

    public String getDestinoSonho() {
        return destinoSonho;
    }

    public String getComidaFavorita() {
        return comidaFavorita;
    }

    public String getAlergiaAlimentar() {
        return alergiaAlimentar;
    }

    public Hours getHorasDormir() {
        return horasDormir;
    }

    public Hours getHorasAcordar() {
        return horasAcordar;
    }

    public Hours getHorasAlmoçar() {
        return horasAlmoçar;
    }

    public Hours getHorasJantar() {
        return horasJantar;
    }

    public int getNumRefeicoes() {
        return numRefeicoes;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setDestinoSonho(String destinoSonho) {
        this.destinoSonho = destinoSonho;
    }

    public void setComidaFavorita(String comidaFavorita) {
        this.comidaFavorita = comidaFavorita;
    }

    public void setAlergiaAlimentar(String alergiaAlimentar) {
        this.alergiaAlimentar = alergiaAlimentar;
    }

    public void setHorasDormir(Hours horasDormir) {
        this.horasDormir = horasDormir;
    }

    public void setHorasAcordar(Hours horasAcordar) {
        this.horasAcordar = horasAcordar;
    }

    public void setHorasAlmoçar(Hours horasAlmoçar) {
        this.horasAlmoçar = horasAlmoçar;
    }

    public void setHorasJantar(Hours horasJantar) {
        this.horasJantar = horasJantar;
    }

    public void setNumRefeicoes(int numRefeicoes) {
        this.numRefeicoes = numRefeicoes;
    }
}
