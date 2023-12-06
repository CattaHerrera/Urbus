package com.example.urbus_firebase;

import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
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
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.bottomsheet.BottomSheetDialog;

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
        fusedLocationProviderClient.getLastLocation().addOnSuccessListener(location -> {
            if (location != null) {
                currentLocation = location;
                // Inicializar el mapa cuando la ubicación esté disponible
                initMap();
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

            // Configurar el listener de clics en los marcadores
            map.setOnMarkerClickListener(marker -> {
                // Obtener el título del marcador
                String marcaSeleccionada = marker.getTitle();

                // Mostrar BottomSheetDialog según la marca seleccionada
                mostrarBottomSheet(marcaSeleccionada);
                Log.d("MapaFragment", "Mostrando Bottom Sheet para marca: " + marcaSeleccionada);

                // Devuelve false para permitir que el evento se propague y se maneje adecuadamente
                return false;
            });

            // Enfocar la cámara en un límite que contenga todas las posiciones de las marcas
            ajustarLimitesDeCamara();
        } else {
            // Manejar el caso en que la ubicación actual sea nula
            Toast.makeText(requireContext(), "No se pudo obtener la ubicación actual", Toast.LENGTH_SHORT).show();
        }
    }

    private void agregarMarcadores() {
        // Añadir marcadores con información específica
        agregarMarcador(new LatLng(19.81895229522824, -97.36060114050689), "Ruta 2 - Base 1", R.drawable.icon_punterorojo);
        agregarMarcador(new LatLng(19.81890915931547, -97.36124794501956), "Ruta 1 - Base 1", R.drawable.icon_punteroazul);
        agregarMarcador(new LatLng(19.819078735970006, -97.35881587004341), "Ruta 1 - Base 2", R.drawable.icon_punteroazul);
        agregarMarcador(new LatLng(19.816581251545358, -97.35906154845205), "Ruta 2 - Base 2", R.drawable.icon_punterorojo);
        agregarMarcador(new LatLng(19.81518611877106, -97.36268796388494), "Ruta 3 - Base 1", R.drawable.icon_punteroverde);
        agregarMarcador(new LatLng(19.81756587870606, -97.36170450207595), "Urbanos Verdes - Base 1", R.drawable.icon_punteroverde);

        agregarMarcador(new LatLng(19.821181121464658, -97.3597721113316), "Parada - Calesa", R.drawable.icon_punteroparada);
        agregarMarcador(new LatLng(19.822802185955872, -97.36028804678749), "Parada - Hospital", R.drawable.icon_punteroparada);
        agregarMarcador(new LatLng(19.825885909519357, -97.35996947501688 ), "Parada - Gasolinera", R.drawable.icon_punteroparada);
        agregarMarcador(new LatLng(19.827081023646965, -97.359791167038), "Parada - Aurrera", R.drawable.icon_punteroparada);
        agregarMarcador(new LatLng(19.818863305815317, -97.3586606540281 ), "Parada - Club de leones", R.drawable.icon_punteroparada);

        agregarMarcador(new LatLng(19.81478639348607, -97.36156602143572), "Parada - Casa social", R.drawable.icon_punteroparada);
        agregarMarcador(new LatLng(19.81360031423038, -97.36159070101465), "Parada - Internado", R.drawable.icon_punteroparada);
        agregarMarcador(new LatLng(19.810829509113766, -97.3614465138631), "Parada - Farmacia", R.drawable.icon_punteroparada);
        agregarMarcador(new LatLng(19.814155258660385, -97.35954920436856), "Parada - UPAV", R.drawable.icon_punteroparada);
        agregarMarcador(new LatLng(19.811801615898677, -97.35960114634103), "Parada - Puente", R.drawable.icon_punteroparada);



    }


    //19.821181121464658, -97.3597721113316 CALESA
    //19.822802185955872, -97.36028804678749 HOSPITAL
    //19.825885909519357, -97.35996947501688 - Gasolinera
    //19.827081023646965, -97.359791167038 - aurrera
    //19.818863305815317, -97.3586606540281 - club de leones

    //19.81478639348607, -97.36156602143572 - Casa social (azules)
    //19.81360031423038, -97.36159070101465 - Internado (azules)
    //19.810829509113766, -97.3614465138631 - farmacia (azules)

    //19.814155258660385, -97.35954920436856 - UPAV (URBANOS VERDES
    // //19.811801615898677, -97.35960114634103 - PUENTE - (URBANOS VERDES)
    private void mostrarBottomSheet(String marcaSeleccionada) {
        // Obtener la posición de la marca en la lista
        int posicion = obtenerPosicionMarca(marcaSeleccionada);

        // Determinar el layout correspondiente según la posición
        int bottomSheetLayoutId = getResources().getIdentifier("bottom_sheet_base" + (posicion + 1), "layout", requireContext().getPackageName());

        // Crear una vista personalizada para el contenido del BottomSheetDialog
        View bottomSheetView = LayoutInflater.from(getActivity()).inflate(bottomSheetLayoutId, null);

        // Crear y mostrar el BottomSheetDialog
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(requireContext());
        bottomSheetDialog.setContentView(bottomSheetView);
        bottomSheetDialog.show();
    }


    //19.81756587870606, -97.36170450207595 coordenas prueba
    private int obtenerPosicionMarca(String marcaSeleccionada) {
        // Obtener la posición de la marca en la lista
        switch (marcaSeleccionada) {
            case "Ruta 1 - Base 1":
                return 0;
            case "Ruta 2 - Base 1":
                return 1;
            case "Ruta 1 - Base 2":
                return 2;
            case "Ruta 2 - Base 2":
                return 3;
            case "Ruta 3 - Base 1":
                return 4;
            case "Urbanos Verdes - Base 1":
                return 5;
            case "Parada - Calesa":
                return 6;
            case "Parada - Hospital":
                return 7;
            case "Parada - Gasolinera":
                return 8;
            case "Parada - Aurrera":
                return 9;
            case "Parada - Club de leones":
                return 10;
            case "Parada - Casa social":
                return 11;
            case "Parada - Internado":
                return 12;
            case "Parada - Farmacia":
                return 13;
            case "Parada - UPAV":
                return 14;
            case "Parada - Puente":
                return 15;
            default:
                return -1; // Marca no encontrada
        }
    }


    private void agregarMarcador(LatLng latLng, String title, int iconResourceId) {
        map.addMarker(new MarkerOptions()
                .position(latLng)
                .title(title)
                .icon(getBitmapDescriptor(iconResourceId, 150, 150)));
    }

    //19.821181121464658, -97.3597721113316 CALESA
    //19.822802185955872, -97.36028804678749 HOSPITAL
    //19.825885909519357, -97.35996947501688 - Gasolinera
    //19.827081023646965, -97.359791167038 - aurrera
    //19.818863305815317, -97.3586606540281 - club de leones

    //19.81478639348607, -97.36156602143572 - Casa social (azules)
    //19.81360031423038, -97.36159070101465 - Internado (azules)
    //19.810829509113766, -97.3614465138631 - farmacia (azules)

    //19.814155258660385, -97.35954920436856 - UPAV (URBANOS VERDES
    // //19.811801615898677, -97.35960114634103 - PUENTE - (URBANOS VERDES)
    private void ajustarLimitesDeCamara() {
        // Crear un límite que contenga todas las posiciones de las marcas
        LatLngBounds.Builder builder = new LatLngBounds.Builder();
        builder.include(new LatLng(19.81895229522824, -97.36060114050689)); // Ruta 2
        builder.include(new LatLng(19.81890915931547, -97.36124794501956)); // Ruta 1
        builder.include(new LatLng(19.819078735970006, -97.35881587004341)); // Ruta 1
        builder.include(new LatLng(19.816581251545358, -97.35906154845205)); // Ruta 2
        builder.include(new LatLng(19.81518611877106, -97.36268796388494)); // Ruta 3
        builder.include(new LatLng(19.821181121464658, -97.3597721113316));
        builder.include(new LatLng(19.822802185955872, -97.36028804678749));
        builder.include(new LatLng(19.825885909519357, -97.35996947501688));
        builder.include(new LatLng(19.827081023646965, -97.359791167038));
        builder.include(new LatLng(19.818863305815317, -97.3586606540281));


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