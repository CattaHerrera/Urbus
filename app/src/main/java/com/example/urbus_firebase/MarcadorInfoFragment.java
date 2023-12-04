package com.example.urbus_firebase;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class MarcadorInfoFragment extends Fragment {



    public MarcadorInfoFragment() {
        // Constructor vacío requerido por Fragment
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflar el diseño del fragmento
        View rootView = inflater.inflate(R.layout.fragment_marcador_info, container, false);


        return rootView;
    }
}
