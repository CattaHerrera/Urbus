package com.example.urbus_firebase;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class MarcadorInfoFragment extends Fragment {

    private String titulo;
    private String descripcion;

    public MarcadorInfoFragment() {
        // Constructor vacío requerido por Fragment
    }

    // Método de fábrica para crear una nueva instancia del fragmento
    public static MarcadorInfoFragment newInstance(String titulo, String descripcion) {
        MarcadorInfoFragment fragment = new MarcadorInfoFragment();
        Bundle args = new Bundle();
        args.putString("titulo", titulo);
        args.putString("descripcion", descripcion);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            titulo = getArguments().getString("titulo", "");
            descripcion = getArguments().getString("descripcion", "");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflar el diseño del fragmento
        View rootView = inflater.inflate(R.layout.fragment_marcador_info, container, false);

        // Configurar la vista con la información del marcador
        TextView tituloTextView = rootView.findViewById(R.id.tituloTextView);
        TextView descripcionTextView = rootView.findViewById(R.id.descripcionTextView);

        tituloTextView.setText(titulo);
        descripcionTextView.setText(descripcion);

        return rootView;
    }
}
