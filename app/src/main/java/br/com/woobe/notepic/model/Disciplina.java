package br.com.woobe.notepic.model;

import android.sax.RootElement;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;

import io.realm.RealmList;
import io.realm.RealmModel;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.RealmClass;

/**
 * Created by willian alfeu on 16/01/2017.
 */

@RealmClass
public class Disciplina implements Serializable, RealmModel {

    @PrimaryKey
    private Long id;
    private String nome;
    private RealmList<Horario> horarios;

    public Disciplina() {
    }

    public Disciplina(String nome, RealmList<Horario> horarios) {
        this.nome = nome;
        this.horarios = horarios;
    }

    public Disciplina(String nome, List<Horario> horarios) {
        this.nome = nome;
        RealmList<Horario> realmList = new RealmList<>();
        realmList.addAll(horarios);
        this.horarios = realmList;
    }

    public Disciplina(Long id, String nome, RealmList<Horario> horarios) {
        this.id = id;
        this.nome = nome;
        this.horarios = horarios;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public RealmList<Horario> getHorarios() {
        return horarios;
    }

    public void setHorarios(RealmList<Horario> horarios) {
        this.horarios = horarios;
    }

    @Override
    public String toString() {
        return "Disciplina{" +
                "id=" + id +
                ", nome='" + nome + '\'' +
                ", horarios=" + (horarios.subList(0,horarios.size())).toString() +
                '}';
    }
}