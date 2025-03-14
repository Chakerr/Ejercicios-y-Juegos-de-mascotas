package co.edu.unipiloto.petapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Build;
import android.Manifest;
import android.content.pm.PackageManager;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.POST_NOTIFICATIONS}, 1);
            }
        }

        // Referencias a los botones
        Button btnIngresar = findViewById(R.id.btnIngresar);
        Button btnRegistrar = findViewById(R.id.btnRegistrar);

        // Botón para ir a la pantalla de inicio de sesión
        btnIngresar.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(intent);
        });

        // Botón para ir a la pantalla de registro de usuario
        btnRegistrar.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, RegistroUsuarioActivity.class);
            startActivity(intent);
        });
    }


    }

