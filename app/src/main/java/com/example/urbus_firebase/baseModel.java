package com.example.urbus_firebase;

public class baseModel {
    String nombre;
    static String clave;
    static String destinos;
    static String frecuencia;
    static String horario;
    static String unidades;


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

    public static String getClave() {
        return clave;
    }

    public void setClave(String clave) {
        this.clave = clave;
    }

    public static String getDestinos() {
        return destinos;
    }

    public void setDestinos(String destinos) {
        this.destinos = destinos;
    }

    public static String getFrecuencia() {
        return frecuencia;
    }

    public void setFrecuencia(String frecuencia) {
        this.frecuencia = frecuencia;
    }

    public static String getHorario() {
        return horario;
    }

    public void setHorario(String horario) {
        this.horario = horario;
    }

    public static String getUnidades() {
        return unidades;
    }

    public void setUnidades(String unidades) {
        this.unidades = unidades;
    }
}
