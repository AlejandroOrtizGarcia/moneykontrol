package com.example.wittig.mymoney;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.backendless.Backendless;
import com.backendless.BackendlessUser;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;

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


        // Interacción //
        String fondos_s = "Fondos: " + Home.user.getProperty("fondos") + " €";
        String gastos_s = "Gastos: " + Home.user.getProperty("gastos") + " €";
        fondos.setText(fondos_s);
        gastos.setText(gastos_s);

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

                                        String fondos_s = "Fondos: " + Home.user.getProperty("fondos") + " €";
                                        fondos.setText(fondos_s);
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

                                        String fondos_s = "Fondos: " + Home.user.getProperty("fondos") + " €";
                                        fondos.setText(fondos_s);
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

        // Return //
        return rootView;
    }
}
