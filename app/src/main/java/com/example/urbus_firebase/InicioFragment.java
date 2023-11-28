package com.example.urbus_firebase;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.SearchView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.FirebaseDatabase;

public class InicioFragment extends Fragment {

    private RecyclerView recyclerView;
    private MainAdapter mainAdapter;
    private SearchView searchView;

    public InicioFragment() {
        // Required empty public constructor
    }

    public static InicioFragment newInstance(String param1, String param2) {
        InicioFragment fragment = new InicioFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true); // Habilitar opciones de menú en el Fragment
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_inicio, container, false);

        recyclerView = rootView.findViewById(R.id.rv);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        Button btnMapa = rootView.findViewById(R.id.btnVerMapa);
        searchView = rootView.findViewById(R.id.searchV);

        // Configurar las opciones de FirebaseRecyclerAdapter con todas las rutas
        FirebaseRecyclerOptions<MainModel> options =
                new FirebaseRecyclerOptions.Builder<MainModel>()
                        .setQuery(FirebaseDatabase.getInstance().getReference().child("Rutas"), MainModel.class)
                        .build();

        // Inicializar el adaptador
        mainAdapter = new MainAdapter(options);
        recyclerView.setAdapter(mainAdapter);

        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();
        mainAdapter.startListening();

        // Configurar el listener para el cambio de texto en el SearchView
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // Lógica para manejar la búsqueda cuando se envía el texto
                txtSearch(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                // Lógica para manejar la búsqueda mientras se va escribiendo
                txtSearch(newText);
                return false;
            }
        });
    }

    @Override
    public void onStop() {
        super.onStop();
        mainAdapter.stopListening();
    }

    private void txtSearch(String str) {
        // Configurar las opciones de FirebaseRecyclerAdapter con una consulta filtrada por destino
        FirebaseRecyclerOptions<MainModel> options =
                new FirebaseRecyclerOptions.Builder<MainModel>()
                        .setQuery(FirebaseDatabase.getInstance().getReference().child("Rutas").orderByChild("destino").startAt(str).endAt(str + "\uf8ff"), MainModel.class)
                        .build();

        mainAdapter = new MainAdapter(options);
        mainAdapter.startListening();
        recyclerView.setAdapter(mainAdapter);
    }
}
