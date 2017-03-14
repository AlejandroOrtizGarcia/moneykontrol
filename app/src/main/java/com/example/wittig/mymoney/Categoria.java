package com.example.wittig.mymoney;

import android.util.Log;

import java.util.Map;

/**
 * Created by Wittig on 05/12/2016.
 */

public class Categoria {

    public String descripcion_categoria;
    public String nombre_categoria;
    public double gasto_categoria;
    public Categoria(){}
    public Categoria(Map cat){
        nombre_categoria = cat.get("nombre_categoria").toString();
        Log.d("CATEGORIA", cat.get("nombre_categoria").toString());
        gasto_categoria = Double.valueOf(cat.get("gasto_categoria").toString());
    }

    public String getDescripcion_categoria() {
        return descripcion_categoria;
    }

    public void setDescripcion_categoria(String descripcion_categoria) {
        this.descripcion_categoria = descripcion_categoria;
    }

    public String getNombre_categoria() {
        return nombre_categoria;
    }

    public void setNombre_categoria(String nombre_categoria) {
        this.nombre_categoria = nombre_categoria;
    }
    public void setGasto_categoria(double gasto_categoria){
        this.gasto_categoria = gasto_categoria;
    }
    public double getGasto_categoria(){
        return gasto_categoria;
    }
}
