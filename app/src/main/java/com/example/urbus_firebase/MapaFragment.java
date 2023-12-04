package com.example.urbus_firebase;

import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class MapaFragment extends Fragment implements OnMapReadyCallback {

    private final int FINE_PERMISSION_CODE = 1;
    private GoogleMap map;
    private Location currentLocation;
    private FusedLocationProviderClient fusedLocationProviderClient;

    public MapaFragment() {
        // Required empty public constructor
    }

    public static MapaFragment newInstance(String param1, String param2) {
        MapaFragment fragment = new MapaFragment();
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
        // Obtener la última ubicación conocida
        fusedLocationProviderClient.getLastLocation().addOnSuccessListener(new OnSuccessListener<Location>() {
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
                mapFragment.getMapAsync(MapaFragment.this);
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_mapa, container, false);
    }

    // Método que convierte imágenes en BitmapDescriptor
    private BitmapDescriptor getBitmapDescriptor(int drawableId, int width, int height) {
        Drawable vectorDrawable = ContextCompat.getDrawable(requireContext(), drawableId);
        vectorDrawable.setBounds(0, 0, width, height);
        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        vectorDrawable.draw(canvas);
        return BitmapDescriptorFactory.fromBitmap(bitmap);
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        map = googleMap;

        // Verificar si la ubicación actual es nula antes de intentar usarla
        if (currentLocation != null) {
            LatLng currentLatLng = new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude());

            // Centrar el mapa en la ubicación actual y establecer un nivel de zoom adecuado
            map.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLatLng, 15f));

            // Añadir marcadores adicionales
            agregarMarcadores();

            // Enfocar la cámara en un límite que contenga todas las posiciones de las marcas
            ajustarLimitesDeCamara();
        } else {
            // Manejar el caso en que la ubicación actual sea nula
            Toast.makeText(requireContext(), "No se pudo obtener la ubicación actual", Toast.LENGTH_SHORT).show();
        }
    }


    private void agregarMarcadores() {
        // Añadir marcadores con información específica
        agregarMarcador(new LatLng(19.81895229522824, -97.36060114050689), "Ruta 2 - Base 1", R.drawable.icon_ruta2);
        agregarMarcador(new LatLng(19.81890915931547, -97.36124794501956), "Ruta 1 - Base 1", R.drawable.icon_ruta1);
        agregarMarcador(new LatLng(19.819078735970006, -97.35881587004341), "Ruta 1 - Base 2", R.drawable.icon_ruta1);
        agregarMarcador(new LatLng(19.816581251545358, -97.35906154845205), "Ruta 2 - Base 2", R.drawable.icon_ruta2);
        agregarMarcador(new LatLng(19.81518611877106, -97.36268796388494), "Ruta 3 - Base 1", R.drawable.icon_ruta3);
    }

    private void agregarMarcador(LatLng latLng, String title, int iconResourceId) {
        map.addMarker(new MarkerOptions()
                .position(latLng)
                .title(title)
                .icon(getBitmapDescriptor(iconResourceId, 64, 100)));
    }

    private void ajustarLimitesDeCamara() {
        // Crear un límite que contenga todas las posiciones de las marcas
        LatLngBounds.Builder builder = new LatLngBounds.Builder();
        builder.include(new LatLng(19.81895229522824, -97.36060114050689)); // Ruta 2
        builder.include(new LatLng(19.81890915931547, -97.36124794501956)); // Ruta 1
        builder.include(new LatLng(19.819078735970006, -97.35881587004341)); // Ruta 1
        builder.include(new LatLng(19.816581251545358, -97.35906154845205)); // Ruta 2
        builder.include(new LatLng(19.81518611877106, -97.36268796388494)); // Ruta 3

        // Obtener el límite
        LatLngBounds bounds = builder.build();

        // Crear un margen para que las posiciones no estén en los bordes
        int padding = 100;

        // Mover la cámara para enfocarse en el límite
        map.animateCamera(CameraUpdateFactory.newLatLngBounds(bounds, padding));
    }

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
