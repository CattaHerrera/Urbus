package com.example.urbus_firebase;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.maps.DirectionsApi;
import com.google.maps.GeoApiContext;
import com.google.maps.model.DirectionsResult;
import com.google.maps.model.TravelMode;
import com.google.android.gms.maps.model.PolylineOptions;

import java.io.IOException;
import java.util.List;

public class MapaGoogleFragment extends Fragment implements OnMapReadyCallback {

    private final int FINE_PERMISSION_CODE = 1;
    private GoogleMap map;
    private Location currentLocation;
    private FusedLocationProviderClient fusedLocationProviderClient;

    private Button imageBack;
    private ImageView imageViewRuta;
    private TextView textViewOrigen;
    private TextView textViewDestino;

    public MapaGoogleFragment() {
        //constructor requerido
    }
    public static MapaGoogleFragment newInstance(String origen, String destino, String surl) {
        MapaGoogleFragment fragment = new MapaGoogleFragment();

        Bundle args = new Bundle();
        args.putString("origen", origen);
        args.putString("destino", destino);
        args.putString("imageUrl", surl);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Inicializar la instancia de FusedLocationProviderClient
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(requireContext());
        // Obtener la última ubicación conocida
        getLastLocation();
    }

    private void getLastLocation() {
        if (ActivityCompat.checkSelfPermission(requireContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(requireContext(), android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(requireActivity(), new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, FINE_PERMISSION_CODE);
            return;
        }
        Task<Location> task = fusedLocationProviderClient.getLastLocation();
        task.addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if (location != null) {
                    currentLocation = location;
                    // Inicializar el mapa cuando la ubicación esté disponible
                    initMap();
                }
            }
        });
    }

    private void initMap() {
        if (isAdded()) {
            // Obtener el SupportMapFragment y notificar cuando el mapa esté listo para ser utilizado
            SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
            if (mapFragment != null) {
                mapFragment.getMapAsync(MapaGoogleFragment.this);
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_mapa_google, container, false);

        // Inicializar vistas
        imageBack = rootView.findViewById(R.id.regresar);
        imageViewRuta = rootView.findViewById(R.id.imageViewRuta);
        textViewOrigen = rootView.findViewById(R.id.textViewOrigen);
        textViewDestino = rootView.findViewById(R.id.textViewDestino);

        // Obtener datos del bundle
        Bundle bundle = getArguments();
        if (bundle != null) {
            String origen = bundle.getString("origen", "");
            String destino = bundle.getString("destino", "");
            String imageUrl = bundle.getString("imageUrl", "");


            // muestra la información en mi interfaz como (ImageView, TextView, etc.)
            textViewOrigen.setText(origen);
            textViewDestino.setText(destino);

            // Usa Glide o la biblioteca para cargar url de imagen
            Glide.with(requireContext()).load(imageUrl).into(imageViewRuta);
        }

        // Configurar el clic del botón de regreso
        imageBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), MainActivity.class);
                startActivity(intent);
            }
        });

        return rootView;
    }


    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        map = googleMap;

        // Coordenadas específicas (Teziutlán, Centro)
        LatLng specificLocation = new LatLng(19.819024788942805, -97.36001065278393);

        // Agregar un  marcador en las coordenadas específicas
        map.addMarker(new MarkerOptions().position(specificLocation).title("Ciudad Origen"));

        // Mover la cámara al centro de las coordenadas específicas y establecer un nivel de zoom que queramos
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(specificLocation, 15f));
    }


    //19.819024788942805, -97.36001065278393 - TEZIUTLÁN
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == FINE_PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getLastLocation();
            } else {
                Toast.makeText(requireContext(), "Permiso de ubicación denegada, por favor habilite la ubicación", Toast.LENGTH_SHORT).show();
            }
        }
    }
}