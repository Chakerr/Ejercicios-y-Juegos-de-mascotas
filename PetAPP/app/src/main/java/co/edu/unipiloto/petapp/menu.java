package co.edu.unipiloto.petapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class menu extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        Button btnRegistrarMascota = findViewById(R.id.btn_registrar_mascota);
        Button btnPerfilMascota = findViewById(R.id.btn_perfil_mascota);
        Button btnCerrarSesion = findViewById(R.id.btn_cerrar_sesion);

        btnRegistrarMascota.setOnClickListener(v -> {
            Log.d("DEBUG", "Botón Registrar Mascota presionado");
            Intent intent = new Intent(menu.this, RegistroMascotas.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);

        });

        btnPerfilMascota.setOnClickListener(v -> {
            Intent intent = new Intent(menu.this, perfil_mascota.class);
            startActivity(intent);

        });
        btnCerrarSesion.setOnClickListener(v -> {
              cerrarSesion();
        });

    }
    private void cerrarSesion() {
        SharedPreferences sharedPreferences = getSharedPreferences("UserSession", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();  // Elimina todos los datos guardados en SharedPreferences
        editor.apply();

        // Redirigir al usuario a la pantalla de login
        Intent intent = new Intent(menu.this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK); // Limpiar la pila de actividades
        startActivity(intent);
        finish();
    }
}