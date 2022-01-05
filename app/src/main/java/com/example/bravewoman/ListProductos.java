package com.example.bravewoman;

public class ListProductos {
    public String nombreProducto;
    public String nombreTienda;
    public String precioProducto;
    public String fotoProducto;

    public ListProductos() {
    }
    public ListProductos(String mNombreProducto, String mNombreNegocio, String mprecioProducto,String mfotoProducto) {
        this.nombreProducto = mNombreProducto;
        this.nombreTienda = mNombreNegocio;
        this.precioProducto = mprecioProducto;
        this.fotoProducto = mfotoProducto;
    }

    public String getPrecioProducto() {
        return precioProducto;
    }

    public void setPrecioProducto(String precioProducto) {
        this.precioProducto = precioProducto;
    }

    public String getFotoProducto() {
        return fotoProducto;
    }

    public void setFotoProducto(String fotoProducto) {
        this.fotoProducto = fotoProducto;
    }



    public String getmNombreProducto() {
        return nombreProducto;
    }

    public void setmNombreProducto(String mNombreProducto) {
        this.nombreProducto = mNombreProducto;
    }

    public String getmNombreNegocio() {
        return nombreTienda;
    }

    public void setmNombreNegocio(String mNombreNegocio) {
        this.nombreTienda = mNombreNegocio;
    }
}
