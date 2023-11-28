package com.example.urbus_firebase;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.concurrent.locks.ReadWriteLock;

public class RegistrarseActivity extends AppCompatActivity {

    private EditText etNombre, etEmail, etPassword, etPasswordC;
    private ProgressBar pregresBar;
    private static final String TAG = "RegistrarseActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrarse);
        getSupportActionBar().setTitle("Registrarse");

        TextView txtIS = findViewById(R.id.txtInicioSesion);
        ImageView volver = findViewById(R.id.imgVolver);

        //cambiar de actividad a traves del texto
        txtIS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Crear un Intent para iniciar la actividad RegistrarseActivity
                Intent intent;
                intent = new Intent(RegistrarseActivity.this, InicioSesionActivity.class);
                startActivity(intent);
            }
        });

        volver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent;
                intent = new Intent(RegistrarseActivity.this, SplashActivity.class);
                startActivity(intent);
            }
        });

        Toast.makeText(RegistrarseActivity.this, "Tu puedes registrarte ahora", Toast.LENGTH_SHORT).show();

        pregresBar = findViewById(R.id.progressBar);
        etNombre = findViewById(R.id.edit_nombre);
        etEmail = findViewById(R.id.edit_email);
        etPassword = findViewById(R.id.edit_password);
        etPasswordC = findViewById(R.id.edit_passwordC);



        Button button = findViewById(R.id.button_register);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String textNombre = etNombre.getText().toString();
                String textEmail = etEmail.getText().toString();
                String textPassword = etPassword.getText().toString();
                String textPasswordC = etPasswordC.getText().toString();


                if (TextUtils.isEmpty(textNombre)) {
                    Toast.makeText(RegistrarseActivity.this, "Por favor ingrese tu nombre de usuario",Toast.LENGTH_LONG).show();
                    etNombre.setError("Nombre de usuario requerido");
                    etNombre.requestFocus();

                } else if (TextUtils.isEmpty(textEmail)) {
                    Toast.makeText(RegistrarseActivity.this, "Por favor ingrese tu correo electrónico",Toast.LENGTH_LONG).show();
                    etEmail.setError("Corre electrónico requerido");
                    etEmail.requestFocus();

                }else if (TextUtils.isEmpty(textPassword)) {
                    Toast.makeText(RegistrarseActivity.this, "Por favor ingrese tu contraseña",Toast.LENGTH_LONG).show();
                    etPassword.setError("Contraseña requerida");
                    etPassword.requestFocus();

                }else if (textPassword.length() < 8) {
                    Toast.makeText(RegistrarseActivity.this, "La contraseña debe de tener al menos 8 digitos",Toast.LENGTH_LONG).show();
                    etPassword.setError("Contraseña requerida");
                    etPassword.requestFocus();


                }else if (TextUtils.isEmpty(textPasswordC)) {
                    Toast.makeText(RegistrarseActivity.this, "Por favor ingrese la confirmación de contraseña",Toast.LENGTH_LONG).show();
                    etPasswordC.setError("Confirmación de contraseña requerida");
                    etPasswordC.requestFocus();
                } else if (!textPassword.equals(textPasswordC)) {
                    Toast.makeText(RegistrarseActivity.this, "La contraseña debe de ser la misma", Toast.LENGTH_LONG).show();
                    etPasswordC.setError("Confirmación de contraseña requerida");
                    etPasswordC.requestFocus();

                    //limpiar contraseña
                    etPassword.clearComposingText();
                    etPasswordC.clearComposingText();
                }else {
                    pregresBar.setVisibility(View.VISIBLE);
                    registrarUsuario(textNombre,textEmail, textPassword, textPasswordC);

                }
            }
        });

    }

    private void registrarUsuario(String textNombre, String textEmail, String textPassword, String textPasswordC) {
        FirebaseAuth auth = FirebaseAuth.getInstance();
        auth.createUserWithEmailAndPassword(textEmail, textPassword).addOnCompleteListener(RegistrarseActivity.this,
                new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            FirebaseUser firebaseUser = auth.getCurrentUser();

                            //actualizar usuario
                            UserProfileChangeRequest profileChangeRequest = new UserProfileChangeRequest.Builder().setDisplayName(textNombre).build();
                            firebaseUser.updateProfile(profileChangeRequest);
                            //Enter user data the firebase.
                            ReadWriteUserDetails writeUserDetails = new ReadWriteUserDetails(textNombre, textEmail);

                            //Extraer la información del usuario
                            DatabaseReference referenceProfile = FirebaseDatabase.getInstance().getReference("Usuarios registrados");

                            referenceProfile.child(firebaseUser.getUid()).setValue(writeUserDetails).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    //enviar la verificación de correo
                                    if (task.isSuccessful()) {
                                        firebaseUser.sendEmailVerification();

                                        Toast.makeText(RegistrarseActivity.this, "Usuario Registrado, verifica el correo", Toast.LENGTH_SHORT).show();

                                        //abrir el perfil de usuario creado
                                        Intent intent = new Intent(RegistrarseActivity.this, InicioFragment.class);
                                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK
                                                | Intent.FLAG_ACTIVITY_NEW_TASK);

                                        startActivity(intent);
                                        finish(); //Cerrar actividad//
                                    }else {
                                        Toast.makeText(RegistrarseActivity.this, "Falló al registrar usuario. Por favor Intentalo otra vez.", Toast.LENGTH_SHORT).show();
                                        pregresBar.setVisibility(View.GONE);


                                    }
                                }
                            });


                        }else {
                            try {
                                throw task.getException();
                            } catch (FirebaseAuthWeakPasswordException e) {
                                etPassword.setError("Su contraseña es demasiado débil. Utilice una combinación de letras, números y caracteres especiales. ");
                                etPassword.requestFocus();
                            } catch (FirebaseAuthInvalidCredentialsException e) {
                                etPassword.setError("Su correo es invalido o está en uso. Utilice otro ");
                                etPassword.requestFocus();
                            } catch (FirebaseAuthUserCollisionException e) {
                                etPassword.setError("Usurio ya registrado con este correo. Usa otro correo ");
                                etPassword.requestFocus();
                            } catch (Exception e) {
                                Log.e(TAG, e.getMessage());
                                Toast.makeText(RegistrarseActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                            }
                            pregresBar.setVisibility(View.GONE);
                        }
                    }
                });
    }
}