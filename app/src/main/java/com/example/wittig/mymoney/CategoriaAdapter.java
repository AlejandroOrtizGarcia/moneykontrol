package com.example.wittig.mymoney;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.util.ArrayList;

/**
 * Created by Wittig on 06/12/2016.
 */

public class CategoriaAdapter extends BaseAdapter {

    private Context context;
    private Categoria[] items;


    public CategoriaAdapter(Context context, Categoria[] items){
        this.context = context;
        this.items = items;
    }
    public CategoriaAdapter(Context context, ArrayList<Categoria> items_list){
        this.context = context;
        items = new Categoria[items_list.size()];
        for(int i = 0; i<items_list.size(); i++){
            items[i] = items_list.get(i);
        }
        Log.d("INFO", ""+items.length);
    }

    @Override
    public int getCount() {
        return items.length;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }
DecimalFormat df = new DecimalFormat("#.##");
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View vista;
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        vista = inflater.inflate(R.layout.categoria_item, null);
        TextView nombre = (TextView) vista.findViewById(R.id.categoria_item_titulo);
        TextView gasto = (TextView) vista.findViewById(R.id.categoria_item_gasto);
        gasto.setText(""+df.format(items[position].getGasto_categoria()));
        nombre.setText(items[position].getNombre_categoria());

        return vista;
    }
}
