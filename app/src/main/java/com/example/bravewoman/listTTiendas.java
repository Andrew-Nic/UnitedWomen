package com.example.bravewoman;

public class listTTiendas {

    public String nombreNegocio;
    public String descNegocio;
    public String fotoPerfil;

    public listTTiendas() {

    }
    public listTTiendas(String nombreNegocio, String descNegocio, String fotoPerfil) {
        this.nombreNegocio = nombreNegocio;
        this.descNegocio = descNegocio;
        this.fotoPerfil = fotoPerfil;
    }

    public String getNombreNegocio() {
        return nombreNegocio;
    }

    public void setNombreNegocio(String nombreNegocio) {
        this.nombreNegocio = nombreNegocio;
    }

    public String getDescNegocio() {
        return descNegocio;
    }

    public void setDescNegocio(String descNegocio) {
        this.descNegocio = descNegocio;
    }

    public String getFotoPerfil() {
        return fotoPerfil;
    }

    public void setFotoPerfil(String fotoPerfil) {
        this.fotoPerfil = fotoPerfil;
    }
}
