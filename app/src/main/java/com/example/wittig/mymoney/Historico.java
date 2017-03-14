package com.example.wittig.mymoney;

import java.util.Date;

/**
 * Created by Wittig on 06/12/2016.
 */

public class Historico {

    private double cantidad;
    private String categoria;
    private Date timestamp;

    public Historico(){}
    public Historico(double cantidad, String categoria, Date timestamp){
        this.cantidad = cantidad;
        this.categoria = categoria;
        this.timestamp = timestamp;
    }
    public double getCantidad() {
        return cantidad;
    }

    public void setCantidad(double cantidad) {
        this.cantidad = cantidad;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }
}
