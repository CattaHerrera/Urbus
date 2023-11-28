package com.example.urbus_firebase;

public class MainModel {
    String nombre, origen, destino, costo, tiempo, parada, distancia, surl;


    MainModel(){

    }
    public MainModel(String nombre, String origen, String destino, String costo, String tiempo, String parada, String distancia, String surl) {
        this.nombre = nombre;
        this.origen = origen;
        this.destino = destino;
        this.costo = costo;
        this.tiempo = tiempo;
        this.parada = parada;
        this.distancia = distancia;
        this.surl = surl;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getOrigen() {
        return origen;
    }

    public void setOrigen(String origen) {
        this.origen = origen;
    }

    public String getDestino() {
        return destino;
    }

    public void setDestino(String destino) {
        this.destino = destino;
    }

    public String getCosto() {
        return costo;
    }

    public void setCosto(String costo) {
        this.costo = costo;
    }

    public String getTiempo() {
        return tiempo;
    }

    public void setTiempo(String tiempo) {
        this.tiempo = tiempo;
    }

    public String getParada() {
        return parada;
    }

    public void setParada(String parada) {
        this.parada = parada;
    }

    public String getDistancia() {
        return distancia;
    }

    public void setDistancia(String distancia) {
        this.distancia = distancia;
    }

    public String getSurl() {
        return surl;
    }

    public void setSurl(String surl) {
        this.surl = surl;
    }
}
