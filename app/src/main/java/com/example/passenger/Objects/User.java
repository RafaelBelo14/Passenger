package com.example.passenger.Objects;

public class User {
    private String name;
    private String email;
    private String assistant_name;
    private String relation;
    private String destino_sonho;
    private String comida_favorita;
    private String alergia;
    private String hora_deitar;
    private String refeicoes;

    public User(String name, String email, String assistant_name, String relation, String destino_sonho, String comida_favorita, String alergia, String hora_deitar, String refeicoes) {
        this.name = name;
        this.email = email;
        this.assistant_name = assistant_name;
        this.relation = relation;
        this.destino_sonho = destino_sonho;
        this.comida_favorita = comida_favorita;
        this.alergia = alergia;
        this.hora_deitar = hora_deitar;
        this.refeicoes = refeicoes;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAssistant_name() {
        return assistant_name;
    }

    public void setAssistant_name(String assistant_name) {
        this.assistant_name = assistant_name;
    }

    public String getRelation() {
        return relation;
    }

    public void setRelation(String relation) {
        this.relation = relation;
    }

    public String getDestino_sonho() {
        return destino_sonho;
    }

    public void setDestino_sonho(String destino_sonho) {
        this.destino_sonho = destino_sonho;
    }

    public String getComida_favorita() {
        return comida_favorita;
    }

    public void setComida_favorita(String comida_favorita) {
        this.comida_favorita = comida_favorita;
    }

    public String getAlergia() {
        return alergia;
    }

    public void setAlergia(String alergia) {
        this.alergia = alergia;
    }

    public String getHora_deitar() {
        return hora_deitar;
    }

    public void setHora_deitar(String hora_deitar) {
        this.hora_deitar = hora_deitar;
    }

    public String getRefeicoes() {
        return refeicoes;
    }

    public void setRefeicoes(String refeicoes) {
        this.refeicoes = refeicoes;
    }
}
