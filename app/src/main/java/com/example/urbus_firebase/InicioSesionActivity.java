package com.example.urbus_firebase;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class InicioSesionActivity extends AppCompatActivity {

    private EditText editTextEmail, editTextPassword;
    private ProgressBar progressBar;
    private FirebaseAuth authProfile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inicio_sesion);
        getSupportActionBar().setTitle("Iniciar Sesión");
        TextView txtR = findViewById(R.id.txtRegistrarse);

        txtR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Crear un Intent para iniciar la actividad RegistrarseActivity
                Intent intent = new Intent(InicioSesionActivity.this, RegistrarseActivity.class);
                startActivity(intent);
            }
        });

        editTextEmail = findViewById(R.id.editLoginEmail);
        editTextPassword = findViewById(R.id.editLoginPassword);
        progressBar = findViewById(R.id.progressBar); // Agregado: inicializar el progressBar

        authProfile = FirebaseAuth.getInstance();

        //Login user
        Button buttonLogin = findViewById(R.id.btnLogin);

        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String textEmail = editTextEmail.getText().toString();
                String textPassword = editTextPassword.getText().toString();

                if (TextUtils.isEmpty(textEmail)) {
                    Toast.makeText(InicioSesionActivity.this, "Por favor ingrese tu correo electrónico",Toast.LENGTH_LONG).show();
                    editTextEmail.setError("Correo requerido");
                    editTextEmail.requestFocus();

                } else if (!Patterns.EMAIL_ADDRESS.matcher(textEmail).matches()) {
                    Toast.makeText(InicioSesionActivity.this, "Por favor vuelva a ingresar su correo",Toast.LENGTH_LONG).show();
                    editTextEmail.setError("Formato de correo incorrecto");
                    editTextEmail.requestFocus();
                } else if (TextUtils.isEmpty(textPassword)) { // Corregido: validación para la contraseña
                    Toast.makeText(InicioSesionActivity.this, "Por favor ingrese tu contraseña",Toast.LENGTH_LONG).show();
                    editTextPassword.setError("Contraseña requerida");
                    editTextPassword.requestFocus();
                } else {
                    progressBar.setVisibility(View.VISIBLE);
                    loginUser(textEmail, textPassword);
                }
            }
        });
    }

    private void loginUser(String email, String pass) {
        authProfile.signInWithEmailAndPassword(email, pass).addOnCompleteListener(InicioSesionActivity.this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(InicioSesionActivity.this, "Haz iniciado sesión", Toast.LENGTH_LONG).show();

                    // Acciones después de iniciar sesión correctamente
                    // 1. Abrir el InicioFragment

                    // Intent para abrir MainActivity
                    Intent intent = new Intent(InicioSesionActivity.this, MainActivity.class);
                    // Agregar información adicional al intent para identificar el fragmento a cargar
                    startActivity(intent);

                } else {
                    Toast.makeText(InicioSesionActivity.this, "Algo salió mal!", Toast.LENGTH_LONG).show();
                }
                progressBar.setVisibility(View.GONE);
            }
        });
    }


}
