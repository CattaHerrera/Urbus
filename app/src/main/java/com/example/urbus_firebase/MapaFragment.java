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
import androidx.fragment.app.FragmentTransaction;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

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
                mapFragment.getMapAsync(MapaFragment.this);
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_mapa_google, container, false);

    }


    //método que convierte esas imágenes en BitmapDescriptor
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

        if (currentLocation != null) {
            LatLng currentLatLng = new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude());

            // Centrar el mapa en la ubicación actual y establecer un nivel de zoom adecuado
            map.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLatLng, 15f));

            // Añadir un marcador en la ubicación actual
            map.addMarker(new MarkerOptions().position(currentLatLng).title("Mi Ubicación"));

            // Añadir un marcador en la ubicación actual con una imagen específica
            map.addMarker(new MarkerOptions()
                    .position(currentLatLng)
                    .title("Mi Ubicación")
                    .icon(getBitmapDescriptor(R.drawable.icon_persona, 64, 100)));

            // Añadir marcadores adicionales
            Antut(googleMap);

            // Configurar el listener de clics en los marcadores
            map.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                @Override
                public boolean onMarkerClick(Marker marker) {
                    // Muestra el BottomSheetDialog al hacer clic en un marcador
                    mostrarBottomSheet(marker.getTitle());
                    return true;
                }
            });
        } else {
            // Manejar el caso en que la ubicación actual sea nula
            Toast.makeText(requireContext(), "No se pudo obtener la ubicación actual", Toast.LENGTH_SHORT).show();
        }
    }


    // Método para mostrar información del BottomSheetDialog
    private void mostrarBottomSheet(String markerTitle) {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("bases_combis");

        databaseReference.child(markerTitle).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    // La base de datos contiene información para la base de combis específica
                    baseModel base = snapshot.getValue(baseModel.class);

                    // Crea una vista personalizada para el contenido del BottomSheetDialog
                    View bottomSheetView = LayoutInflater.from(getActivity()).inflate(R.layout.bottom_sheet_layout, null);

                    // Configura los elementos de diseño y la lógica para mostrar información de la base de combis
                    TextView nombreTextView = bottomSheetView.findViewById(R.id.txNombre);
                    TextView clave = bottomSheetView.findViewById(R.id.txClave);
                    TextView destinosText = bottomSheetView.findViewById(R.id.txDestino);
                    TextView frecuenciaText = bottomSheetView.findViewById(R.id.txFrecuencia);
                    TextView horarioText = bottomSheetView.findViewById(R.id.txHorario);
                    TextView unidadesText = bottomSheetView.findViewById(R.id.txUnidades);

                    nombreTextView.setText(base.getNombre());
                    clave.setText("Clave: " + base.getClave());
                    frecuenciaText.setText("Frecuencia: " + base.getFrecuencia());
                    horarioText.setText("Horario: " + base.getHorario());
                    unidadesText.setText("Unidades: " + base.getUnidades());
                    destinosText.setText("Destinos: " + base.getDestinos());

                    // Crea el BottomSheetDialog
                    BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(requireContext());
                    bottomSheetDialog.setContentView(bottomSheetView);

                    // Muestra el BottomSheetDialog
                    bottomSheetDialog.show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Manejar errores de consulta
                Toast.makeText(requireContext(), "Error al obtener información de la base de combis", Toast.LENGTH_SHORT).show();
            }
        });
    }


    public void Antut(GoogleMap googleMap) {
        map = googleMap;
        final LatLng base1 = new LatLng(19.81895229522824, -97.36060114050689);
        map.addMarker(new MarkerOptions()
                .position(base1)
                .title("Teziutlán, Ruta 2")
                .icon(getBitmapDescriptor(R.drawable.icon_ruta2, 64, 100)));

        final LatLng base2 = new LatLng(19.81890915931547, -97.36124794501956);
        map.addMarker(new MarkerOptions()
                .position(base2)
                .title("Teziutlán, Ruta 1")
                .icon(getBitmapDescriptor(R.drawable.icon_ruta1, 64, 100)));

        final LatLng base3 = new LatLng(19.819078735970006, -97.35881587004341);
        map.addMarker(new MarkerOptions()
                .position(base3)
                .title("Teziutlán, Ruta 1")
                .icon(getBitmapDescriptor(R.drawable.icon_ruta1, 64, 100)));

        final LatLng base4 = new LatLng(19.816581251545358, -97.35906154845205);
        map.addMarker(new MarkerOptions()
                .position(base4)
                .title("Teziutlán, Ruta 1")
                .icon(getBitmapDescriptor(R.drawable.icon_ruta1, 64, 100)));

        final LatLng base5 = new LatLng(19.81518611877106, -97.36268796388494);
        map.addMarker(new MarkerOptions()
                .position(base5)
                .title("Teziutlán, Ruta 3")
                .icon(getBitmapDescriptor(R.drawable.icon_ruta3, 64, 100)));
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
