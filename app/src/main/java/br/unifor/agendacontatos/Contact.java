package br.unifor.agendacontatos;

import io.realm.RealmObject;

/**
 * Created by rafaelpinheiro on 21/02/17.
 */

public class Contact extends RealmObject {

    private String id;
    private String nome;
    private String telefone;
    private String email;
    private String observacao;
    private String created_at;
    private String updated_at;

    public Contact(){}

//    public Contact(String id, String name, String phone, String email, String observation, String created_at, String updated_at){
//        this.id = id;
//        this.name = name;
//        this.phone = phone;
//        this.email = email;
//        this.observation = observation;
//        this.created_at = created_at;
//        this.updated_at = updated_at;
//    }

    public Contact(String name, String phone, String email, String observation){
        this.nome = name;
        this.telefone = phone;
        this.email = email;
        this.observacao = observation;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return nome;
    }

    public String getPhone() {
        return telefone;
    }

    public String getEmail() {
        return email;
    }

    public String getObservation() {
        return observacao;
    }

    public String getCreated_at() {
        return created_at;
    }

    public String getUpdated_at() {
        return updated_at;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.nome = name;
    }

    public void setPhone(String phone) {
        this.telefone = phone;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setObservation(String observation) {
        this.observacao = observation;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public void setUpdated_at(String updated_at) {
        this.updated_at = updated_at;
    }
}
