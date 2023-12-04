package com.example.urbus_firebase;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.SearchView;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.FirebaseDatabase;

public class InicioFragment extends Fragment implements MainAdapter.OnVerMapaClickListener, MainAdapter.OnFavoritoClickListener {

    private RecyclerView recyclerView;
    private MainAdapter mainAdapter;
    private SearchView searchView;

    public InicioFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_inicio, container, false);

        recyclerView = rootView.findViewById(R.id.rv);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));

        searchView = rootView.findViewById(R.id.searchV);

        // Configurar las opciones de FirebaseRecyclerAdapter con todas las rutas
        FirebaseRecyclerOptions<MainModel> options =
                new FirebaseRecyclerOptions.Builder<MainModel>()
                        .setQuery(FirebaseDatabase.getInstance().getReference().child("Rutas"), MainModel.class)
                        .build();

        // Inicializar el adaptador
        mainAdapter = new MainAdapter(options, this, this);
        recyclerView.setAdapter(mainAdapter);

        // Configurar el listener para el cambio de texto en el SearchView
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                txtSearch(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                txtSearch(newText);
                return false;
            }
        });

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

    private void txtSearch(String str) {
        // Configurar las opciones de FirebaseRecyclerAdapter con una consulta filtrada por destino
        FirebaseRecyclerOptions<MainModel> options =
                new FirebaseRecyclerOptions.Builder<MainModel>()
                        .setQuery(FirebaseDatabase.getInstance().getReference().child("Rutas").orderByChild("destino").startAt(str).endAt(str + "\uf8ff"), MainModel.class)
                        .build();

        mainAdapter.updateOptions(options);
        mainAdapter.notifyDataSetChanged();
    }

    // Implementa el método de la interfaz para manejar el clic del botón "Ver Mapa"
    @Override
    public void onVerMapaClick(MainModel mainModel) {
        // Abre el nuevo fragmento (MapaGoogleFragment) aquí
        FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        MapaGoogleFragment mapaGoogleFragment = MapaGoogleFragment.newInstance(mainModel.getOrigen(), mainModel.getDestino(), mainModel.getSurl());

        fragmentTransaction.replace(R.id.nav_hostfragment, mapaGoogleFragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    @Override
    public void onFavoritoClick(MainModel mainModel) {
        // Obtener la referencia de la ruta actual y cambiar el estado de favorito en la base de datos
        String rutaId = mainModel.getId();

        // Asegúrate de que rutaId no sea nulo antes de proceder
        if (rutaId != null && !rutaId.isEmpty()) {
            boolean nuevoEstado = !mainModel.isFavorito();

            // Cambiar el estado de favorito en la base de datos
            FirebaseDatabase.getInstance().getReference().child("Rutas").child(rutaId).child("favorito").setValue(nuevoEstado);

            // Puedes realizar alguna lógica adicional aquí si es necesario

            // Cambiar al fragmento adecuado
            Fragment fragment = new HorarioFragment();  // Cambia a tu fragmento deseado
            FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
            transaction.replace(R.id.nav_hostfragment, fragment);
            transaction.addToBackStack(null);
            transaction.commit();
        } else {
            // Manejar el caso cuando rutaId es nulo o vacío
            // Puedes mostrar un mensaje de error, registrar, o realizar alguna acción adecuada.
        }
    }



}
