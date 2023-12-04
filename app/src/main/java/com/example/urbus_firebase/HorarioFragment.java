package com.example.urbus_firebase;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.FirebaseDatabase;

public class HorarioFragment extends Fragment {

    private RecyclerView recyclerView;
    private MainAdapter mainAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_horario, container, false);

        // Configurar las opciones de FirebaseRecyclerAdapter con rutas favoritas
        FirebaseRecyclerOptions<MainModel> options =
                new FirebaseRecyclerOptions.Builder<MainModel>()
                        .setQuery(FirebaseDatabase.getInstance().getReference().child("Rutas").orderByChild("favorito").equalTo(true), MainModel.class)
                        .build();

        // Inicializar el adaptador
        mainAdapter = new MainAdapter(options, null, null);

        // Obtener el RecyclerView y establecer el adaptador
        recyclerView = rootView.findViewById(R.id.rv);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        recyclerView.setAdapter(mainAdapter);

        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();
        mainAdapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        mainAdapter.stopListening();
    }
}
