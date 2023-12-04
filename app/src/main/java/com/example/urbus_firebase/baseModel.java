package com.example.urbus_firebase;

public class baseModel {
    String nombre;
    String clave;
     String destinos;
     String frecuencia;
     String horario;
     String unidades;


    public baseModel(){

    }
    public baseModel(String nombre, String clave, String destinos, String frecuencia, String horario, String unidades) {
        this.nombre = nombre;
        this.clave = clave;
        this.destinos = destinos;
        this.frecuencia = frecuencia;
        this.horario = horario;
        this.unidades = unidades;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public  String getClave() {
        return clave;
    }

    public void setClave(String clave) {
        this.clave = clave;
    }

    public  String getDestinos() {
        return destinos;
    }

    public void setDestinos(String destinos) {
        this.destinos = destinos;
    }

    public  String getFrecuencia() {
        return frecuencia;
    }

    public void setFrecuencia(String frecuencia) {
        this.frecuencia = frecuencia;
    }

    public  String getHorario() {
        return horario;
    }

    public void setHorario(String horario) {
        this.horario = horario;
    }

    public  String getUnidades() {
        return unidades;
    }

    public void setUnidades(String unidades) {
        this.unidades = unidades;
    }
}
