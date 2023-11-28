package com.example.urbus_firebase;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity {

    // Añade una variable para gestionar la información del usuario
    private FirebaseAuth auth;
    private FirebaseUser currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();

        FirebaseConfig.init(this);

        // Inicializar FirebaseAuth
        auth = FirebaseAuth.getInstance();
        currentUser = auth.getCurrentUser();

        // Obtener el Intent que inició esta actividad
        Intent intent = getIntent();
        // Verificar si hay información adicional (nombre del fragmento a cargar)
        if (intent.hasExtra("fragmentToLoad")) {
            // Obtener el nombre del fragmento a cargar
            String fragmentToLoad = intent.getStringExtra("fragmentToLoad");

            // Manejar la navegación según el fragmento a cargar
            if (fragmentToLoad.equals("InicioFragment")) {
                // Cargar el fragmento InicioFragment
                getSupportFragmentManager().beginTransaction().replace(R.id.nav_hostfragment, new InicioFragment()).commit();
            } else if (fragmentToLoad.equals("OtroFragmento")) {
                // Cargar otro fragmento si es necesario
                // getSupportFragmentManager().beginTransaction().replace(R.id.nav_hostfragment, new OtroFragmento()).commit();
            }
        }

        setupNavegacion();
    }

    private void setupNavegacion() {
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.nav_hostfragment);

        // Configurar la navegación con NavController
        NavController navController = navHostFragment.getNavController();
        NavigationUI.setupWithNavController(bottomNavigationView, navController);

    }
}
