package com.example.passenger;

import java.io.Serializable;

public class Hours implements Serializable {
    private int horas;
    private int minutos;

    public Hours(int horas, int minutos) {
        this.horas = horas;
        this.minutos = minutos;
    }

    public int getHoras() {
        return horas;
    }

    public void setHoras(int horas) {
        this.horas = horas;
    }

    public String getHorasMinutos() {
        return horas + ":" + minutos;
    }

    public int getMinutos() {
        return minutos;
    }

    public void setMinutos(int minutos) {
        this.minutos = minutos;
    }
}




