package com.example.urbus_firebase;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

public class BuscarRutaFragment extends Fragment {

    public BuscarRutaFragment() {
        // Required empty public constructor
    }

    public static BuscarRutaFragment newInstance() {
        BuscarRutaFragment fragment = new BuscarRutaFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_buscar_ruta, container, false);

        EditText etSource = rootView.findViewById(R.id.source);
        EditText etDestination = rootView.findViewById(R.id.destination);
        Button btnSubmit = rootView.findViewById(R.id.btnSubmit);

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String source = etSource.getText().toString();
                String destination = etDestination.getText().toString();

                if (source.isEmpty() || destination.isEmpty()) {
                    Toast.makeText(requireContext(), "Ingrese origen y destino", Toast.LENGTH_SHORT).show();
                } else {
                    // Construye la URI para la dirección en Google Maps
                    Uri uri = Uri.parse("https://www.google.com/maps/dir/" + source + "/" + destination);

                    // Crea una intención para abrir Google Maps con la ruta
                    Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                    intent.setPackage("com.google.android.apps.maps");
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                    // Inicia la actividad
                    startActivity(intent);
                }
            }
        });

        return rootView;
    }
}
