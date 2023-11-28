package com.example.urbus_firebase;

import static android.app.Activity.RESULT_OK;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class PerfilFragment extends Fragment {

    private static final int GALLERY_REQUEST_CODE = 1; // Puedes usar cualquier valor único

    private TextView txtNombreUsuario;
    private TextView txtEmailUsuario;
    private Button btnIniciarSesion;
    private Button btnCerrarSesion;
    private TextView textMensajeNoRegistrado;
private ImageView  imagePerfil;
    public PerfilFragment() {
        // Required empty public constructor
    }

    public static PerfilFragment newInstance(String param1, String param2) {
        PerfilFragment fragment = new PerfilFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            // Puedes manejar argumentos si es necesario
        }
    }
// ...

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_perfil, container, false);

        txtNombreUsuario = view.findViewById(R.id.textNombre);
        txtEmailUsuario = view.findViewById(R.id.textEmail);
        btnIniciarSesion = view.findViewById(R.id.btnIniciarSesion);
        btnCerrarSesion = view.findViewById(R.id.btnCerrarSesion);
        textMensajeNoRegistrado = view.findViewById(R.id.textMensajeNoRegistrado);
        imagePerfil = view.findViewById(R.id.imgPerfil);

        // Verificar el estado de autenticación
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            // El usuario ha iniciado sesión
            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("usuarios")
                    .child(currentUser.getUid())
                    .child("imagenPerfil");

            databaseReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        // Si hay información de imagen en la base de datos, cárgala en ImageView
                        String imagenUri = snapshot.getValue(String.class);
                        if (imagenUri != null && !imagenUri.isEmpty()) {
                            // Utiliza la biblioteca Glide o Picasso para cargar la imagen desde la URI
                            // Glide.with(getActivity()).load(imagenUri).into(imagePerfil);
                            // o
                            // Picasso.get().load(imagenUri).into(imagePerfil);
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    // Manejar errores si es necesario
                }
            });

            // Muestra la información del perfil
            txtNombreUsuario.setText(currentUser.getDisplayName());
            txtEmailUsuario.setText(currentUser.getEmail());
            // Oculta el botón de iniciar sesión
            btnIniciarSesion.setVisibility(View.GONE);
            textMensajeNoRegistrado.setVisibility(View.GONE);  // Oculta el mensaje de bienvenida
        } else {
            // El usuario no ha iniciado sesión, muestra el botón de iniciar sesión
            txtNombreUsuario.setVisibility(View.GONE);
            txtEmailUsuario.setVisibility(View.GONE);
            btnCerrarSesion.setVisibility(View.GONE); // Oculta el botón de cerrar sesión
            textMensajeNoRegistrado.setVisibility(View.VISIBLE);  // Muestra el mensaje de bienvenida
            btnIniciarSesion.setVisibility(View.VISIBLE);

            // Configura el clic del botón de iniciar sesión
            btnIniciarSesion.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Iniciar la actividad de inicio de sesión
                    Intent intent = new Intent(getActivity(), InicioSesionActivity.class);
                    startActivity(intent);
                }
            });
        }

        // Configura el clic del botón de cerrar sesión
        btnCerrarSesion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cerrarSesion();
            }
        });

        // Configura el clic del imagePerfil
        imagePerfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Verificar el estado de autenticación nuevamente para evitar duplicados
                FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
                if (currentUser != null) {
                    // El usuario está autenticado, permite cambiar la imagen
                    // Abre la galería para seleccionar una imagen
                    Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(galleryIntent, GALLERY_REQUEST_CODE);
                } else {
                    // El usuario no está autenticado, muestra un mensaje o toma la acción adecuada
                    // Por ejemplo:
                    Toast.makeText(getActivity(), "Debes iniciar sesión para cambiar la imagen", Toast.LENGTH_SHORT).show();
                }
            }
        });

        return view;
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == GALLERY_REQUEST_CODE && resultCode == RESULT_OK && data != null) {
            // Verificar el estado de autenticación
            FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
            if (currentUser != null) {
                // El usuario está autenticado, permite cambiar la imagen
                // Obtiene la URI de la imagen seleccionada
                Uri imageUri = data.getData();

                // Aquí puedes hacer lo que necesites con la URI, como mostrar la imagen en tu ImageView
                imagePerfil.setImageURI(imageUri);

                // Ahora, también puedes guardar la URI en Firebase para mantener la persistencia
                // Esto podría ser en una base de datos Realtime Database o Firestore
                // Por ejemplo (usando Realtime Database):
                DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("usuarios")
                        .child(currentUser.getUid())
                        .child("imagenPerfil");
                databaseReference.setValue(imageUri.toString());
            } else {
                // El usuario no está autenticado, muestra un mensaje o toma la acción adecuada
                // Puedes mostrar un Toast o cualquier otra acción que consideres apropiada
                // Por ejemplo:
                Toast.makeText(getActivity(), "Debes iniciar sesión para cambiar la imagen", Toast.LENGTH_SHORT).show();
            }
        }
    }




    private void cerrarSesion() {
        // Agrega el código necesario para cerrar sesión en FirebaseAuth
        FirebaseAuth.getInstance().signOut();

        // Navega de regreso a la pantalla de inicio de sesión
        Intent intent = new Intent(getActivity(), InicioSesionActivity.class);
        startActivity(intent);
        getActivity().finish(); // Cierra la actividad actual
    }
}
