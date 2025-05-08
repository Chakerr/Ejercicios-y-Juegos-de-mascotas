package co.edu.unipiloto.petapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class MenuPaseador extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_paseador);

        Button btnCerrarSesion = findViewById(R.id.btn_cerrar_sesionPaseador);
        Button btnIniciarRecorrido = findViewById(R.id.btnIniciarRecorrido);
        Button btnGestionTarifas = findViewById(R.id.btnGestionTarifas); // <--- Nuevo botón

        btnCerrarSesion.setOnClickListener(v -> cerrarSesion());

        btnIniciarRecorrido.setOnClickListener(v -> {
            Intent intent = new Intent(MenuPaseador.this, InicioRecorridoActivity.class);
            startActivity(intent);
        });

        btnGestionTarifas.setOnClickListener(v -> {
            Intent intent = new Intent(MenuPaseador.this, TarifaPaseadorActivity.class);
            startActivity(intent);
        });
    }

    private void cerrarSesion() {
        SharedPreferences sharedPreferences = getSharedPreferences("UserSession", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();

        Intent intent = new Intent(MenuPaseador.this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }
}
