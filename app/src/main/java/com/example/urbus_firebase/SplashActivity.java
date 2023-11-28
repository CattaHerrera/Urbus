package com.example.urbus_firebase;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);


    }

    public void abreInicio(View view) {
        try {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "Error al abrir MainActivity", Toast.LENGTH_SHORT).show();
        }
    }



    public void abreRegistrarse(View view){
        try {
            Intent intent = new Intent(this, RegistrarseActivity.class);
            startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "Error al abrir la RegistrarseActivity", Toast.LENGTH_SHORT).show();
        }
    }
}