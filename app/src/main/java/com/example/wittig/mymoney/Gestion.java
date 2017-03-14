package com.example.wittig.mymoney;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.backendless.Backendless;
import com.backendless.BackendlessCollection;
import com.backendless.BackendlessUser;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;
import com.backendless.persistence.BackendlessDataQuery;
import com.backendless.persistence.QueryOptions;

import org.w3c.dom.Text;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by Wittig on 03/12/2016.
 */

public class Gestion extends Fragment {

    // Elementos //
    TextView fondos;
    TextView gastos;
    Button aniadirFondos;
    Button aniadirCategoria;
    Button resetGastos;
    GridView gridView;
    TextView ultimo_move;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_gestion, null);

        // Recuperar Elementos del Layout //
        fondos = (TextView) rootView.findViewById(R.id.gestion_fondos);
        gastos = (TextView) rootView.findViewById(R.id.gestion_gastos);
        aniadirFondos = (Button) rootView.findViewById(R.id.gestion_aniadirFondos);
        aniadirCategoria = (Button) rootView.findViewById(R.id.gestion_aniadirCategoria);
        resetGastos = (Button) rootView.findViewById(R.id.gestion_resetGastos);
        gridView = (GridView) rootView.findViewById(R.id.gestion_gridview);
        ultimo_move = (TextView) rootView.findViewById(R.id.gestion_ultimo_movimiento);


        refreshGrid();
        // PROBANDO //



        // /PROBANDO //


        // Aniadir Fondos Button -> TERMINADO //
        aniadirFondos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                LinearLayout linearLayout = new LinearLayout(getContext());
                linearLayout.setOrientation(LinearLayout.VERTICAL);

                final EditText input = new EditText(getActivity());
                int type = InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL;
                input.setInputType(type);

                final ToggleButton toggleButton = new ToggleButton(getActivity());
                toggleButton.setTextOn("Sumar fondos");
                toggleButton.setTextOff("Establecer fondos");
                toggleButton.setChecked(true);

                linearLayout.addView(input);
                linearLayout.addView(toggleButton);

                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setMessage("Introduce una cantidad...");
                builder.setTitle("+ Fondos +");
                builder.setPositiveButton("Ok...", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        if (input.getText().toString().equals("")) {
                            Toast.makeText(getContext(), "Por favor, añade una cantidad..", Toast.LENGTH_LONG).show();
                        } else {

                            double fondosAntes = 0.0;
                            Object object = Home.user.getProperty("fondos");
                            if (object instanceof Integer) {
                                fondosAntes = fondosAntes + (int) object;
                            } else if (object instanceof Double) {
                                fondosAntes = fondosAntes + (double) object;
                            }

                            double fondosAniadidos = Double.valueOf(input.getText().toString());

                            BackendlessUser newuser = Home.user;

                            if (toggleButton.isChecked()) {
                                // Sumar fondos //
                                double fondosAhora = fondosAntes + fondosAniadidos;
                                newuser.setProperty("fondos", fondosAhora);

                                Backendless.UserService.update(newuser, new AsyncCallback<BackendlessUser>() {
                                    @Override
                                    public void handleResponse(BackendlessUser response) {
                                        Home.user = response;
                                        refreshGrid();
                                    }

                                    @Override
                                    public void handleFault(BackendlessFault fault) {
                                        Toast.makeText(getContext(), "Error: " + fault.getMessage(), Toast.LENGTH_LONG).show();
                                    }
                                });
                            } else {
                                // Establecer //
                                double fondosAhora = fondosAniadidos;
                                newuser.setProperty("fondos", fondosAhora);

                                Backendless.UserService.update(newuser, new AsyncCallback<BackendlessUser>() {
                                    @Override
                                    public void handleResponse(BackendlessUser response) {
                                        Home.user = response;
                                        refreshGrid();
                                    }

                                    @Override
                                    public void handleFault(BackendlessFault fault) {
                                        Toast.makeText(getContext(), "Error: " + fault.getMessage(), Toast.LENGTH_LONG).show();
                                    }
                                });
                            }
                        }
                    }
                });
                builder.setNegativeButton("Cancelar...", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                    }
                });

                AlertDialog dialog = builder.create();

                dialog.setView(linearLayout);
                dialog.show();
            }
        });

        // Reset Gastos Button -> TERMINADO //
        resetGastos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle("¿Estas seguro?");
                builder.setPositiveButton("Sí", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        double gastosAhora = 0;
                        BackendlessUser newuser = Home.user;
                        newuser.setProperty("gastos", gastosAhora);
                        Backendless.UserService.update(newuser, new AsyncCallback<BackendlessUser>() {
                            @Override
                            public void handleResponse(BackendlessUser response) {
                                Home.user = response;
                                String gastos_s = "Gastos: " + Home.user.getProperty("gastos") + " €";
                                gastos.setText(gastos_s);
                            }

                            @Override
                            public void handleFault(BackendlessFault fault) {
                                Toast.makeText(getContext(), "Error: " + fault.getMessage(), Toast.LENGTH_LONG).show();
                            }
                        });
                         Backendless.Persistence.of("Categoria").find(new AsyncCallback<BackendlessCollection<Map>>() {
                             @Override
                             public void handleResponse(BackendlessCollection<Map> response) {

                                 final List<Map> categorias = response.getCurrentPage();
                                 for(Map map : categorias){
                                     map.put("gasto_categoria",0);
                                 }
                                 new Thread(new Runnable() {
                                     @Override
                                     public void run() {
                                         for(Map cat : categorias){
                                             Backendless.Persistence.of("Categoria").save(cat);
                                         }
                                         refreshGrid();
                                     }
                                 }).start();

                             }

                             @Override
                             public void handleFault(BackendlessFault fault) {

                             }
                         });


                    }
                });
                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });

        // Aniadir Categoria Button ->  //
        aniadirCategoria.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LinearLayout linearLayout = new LinearLayout(getContext());
                linearLayout.setOrientation(LinearLayout.VERTICAL);

                final EditText input = new EditText(getActivity());
                linearLayout.addView(input);
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setMessage("Introduce una categoria...");
                builder.setTitle("+ Categoria +");
                builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(final DialogInterface dialog, int which) {

                        if(input.getText().toString().equals("")){

                        }else{
                            Categoria cat = new Categoria();
                            cat.setNombre_categoria(input.getText().toString());
                            Backendless.Persistence.of(Categoria.class).save(cat, new AsyncCallback<Categoria>() {
                                @Override
                                public void handleResponse(Categoria response) {
                                    dialog.dismiss();
                                    refreshGrid();
                                }

                                @Override
                                public void handleFault(BackendlessFault fault) {

                                }
                            });
                        }
                    }
                });
                builder.setNegativeButton("Cancelar...", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });


                AlertDialog dialog = builder.create();

                dialog.setView(linearLayout);
                dialog.show();

            }
        });
        // Return //
        return rootView;
    }
    DecimalFormat df = new DecimalFormat("#.##");

    String lastmove = "";
    public void refreshGrid() {




        BackendlessDataQuery query = new BackendlessDataQuery();
        QueryOptions queryOptions = new QueryOptions();
        queryOptions.setPageSize(100);
        query.setQueryOptions(queryOptions);
        final ArrayList<Categoria> categorias = new ArrayList<>();
        Backendless.Data.of("Categoria").find(query, new AsyncCallback<BackendlessCollection<Map>>() {
            @Override
            public void handleResponse(BackendlessCollection<Map> response) {

                double gasto_acumulado = 0;
                for (Map r : response.getCurrentPage()) {
                    categorias.add(new Categoria(r));
                    gasto_acumulado = gasto_acumulado + Double.valueOf(r.get("gasto_categoria").toString());
                    //Log.d("INFO", r.toString() + categorias.size());
                }
                double fondos_acumulados = (Double.valueOf(Home.user.getProperty("fondos").toString())-gasto_acumulado);
                String fondos_s = "Fondos: " + df.format(fondos_acumulados) + " €";
                fondos.setText(fondos_s);
                gastos.setText("Gastos: "+df.format(gasto_acumulado) + " €");
                gridView.setAdapter(new CategoriaAdapter(getContext(), categorias));
                gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                        LinearLayout linearLayout = new LinearLayout(getContext());
                        linearLayout.setOrientation(LinearLayout.VERTICAL);

                        final EditText input = new EditText(getActivity());
                        int type = InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL;
                        input.setInputType(type);

                        linearLayout.addView(input);
                        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                        builder.setMessage("Introduce una cantidad...");
                        builder.setTitle("+ Gastos +");

                        final TextView categoria_name = (TextView)view.findViewById(R.id.categoria_item_titulo);
                        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(final DialogInterface dialog, int which) {
                                BackendlessDataQuery query = new BackendlessDataQuery("nombre_categoria = '"+categoria_name.getText().toString()+"'");
                                Backendless.Persistence.of("Categoria").find(query, new AsyncCallback<BackendlessCollection<Map>>() {
                                    @Override
                                    public void handleResponse(BackendlessCollection<Map> response) {
                                        final Map categoria = response.getCurrentPage().get(0);
                                        final Categoria categoria_id = new Categoria(categoria);
                                        double antes = Double.valueOf(categoria.get("gasto_categoria").toString());
                                        double nuevo_gasto = Double.valueOf(input.getText().toString());

                                        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("LastMove",Context.MODE_PRIVATE);
                                        sharedPreferences.edit().putString("lastmove", "Ultimo movimiento: "+nuevo_gasto+ " € en <"+categoria_name.getText().toString()+">.").commit();

                                        double ahora = antes + nuevo_gasto;
                                        categoria.put("gasto_categoria", ahora);
                                        new Thread(new Runnable() {
                                            @Override
                                            public void run() {
                                                Backendless.Persistence.of("Categoria").save(categoria);
                                                refreshGrid();
                                            }
                                        }).start();

                                        Date date = new Date();
                                        date.setTime(date.getTime());

                                        final Historico historico = new Historico(nuevo_gasto, categoria.get("nombre_categoria").toString(), date);

                                        new Thread(new Runnable() {
                                            @Override
                                            public void run() {
                                                Backendless.Persistence.of(Historico.class).save(historico);
                                            }
                                        }).start();

                                        dialog.dismiss();
                                    }

                                    @Override
                                    public void handleFault(BackendlessFault fault) {

                                    }
                                });
                            }
                        });
                        builder.setNegativeButton("Cancelar..", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });

                        AlertDialog dialog = builder.create();

                        dialog.setView(linearLayout);
                        dialog.show();


                    }
                });

                SharedPreferences prefs = getActivity().getSharedPreferences("LastMove", Context.MODE_PRIVATE);
                lastmove = prefs.getString("lastmove","none");
                ultimo_move.setText(lastmove);
            }

            @Override
            public void handleFault(BackendlessFault fault) {

            }
        });
    }
}
