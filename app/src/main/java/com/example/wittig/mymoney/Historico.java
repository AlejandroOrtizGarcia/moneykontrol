package com.example.wittig.mymoney;

import java.util.Date;

/**
 * Created by Wittig on 06/12/2016.
 */

public class Historico {

    private double cantidad;
    private Categoria categoria;
    private Date timestamp;

    public double getCantidad() {
        return cantidad;
    }

    public void setCantidad(double cantidad) {
        this.cantidad = cantidad;
    }

    public Categoria getCategoria() {
        return categoria;
    }

    public void setCategoria(Categoria categoria) {
        this.categoria = categoria;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }
}
