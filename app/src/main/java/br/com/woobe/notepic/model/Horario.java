package br.com.woobe.notepic.model;

import java.io.Serializable;

import io.realm.RealmModel;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.RealmClass;

/**
 * Created by willian alfeu on 16/01/2017.
 */

@RealmClass
public class Horario implements Serializable, RealmModel {

    private String diaSemana;
    private String horario;

    public Horario() {
    }

    public Horario(String diaSemana, String horario) {
        this.diaSemana = diaSemana;
        this.horario = horario;
    }

    public String getDiaSemana() {
        return diaSemana;
    }

    public void setDiaSemana(String diaSemana) {
        this.diaSemana = diaSemana;
    }

    public String getHorario() {
        return horario;
    }

    public void setHorario(String horario) {
        this.horario = horario;
    }

    @Override
    public String toString() {
        return "Horario{" +
                "horario='" + horario + '\'' +
                ", diaSemana='" + diaSemana + '\'' +
                '}';
    }
}