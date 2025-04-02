package co.edu.unipiloto.petapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;

public class MenuDueño extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        Button btnRegistrarMascota = findViewById(R.id.btn_registrar_mascota);
        Button btnPerfilMascota = findViewById(R.id.btn_perfil_mascota);
        Button btnRegistrarMedicamento = findViewById(R.id.btn_registrar_medicamento);
        Button btnHistorialMedico = findViewById(R.id.btn_historial_medico);
        Button btnCerrarSesion = findViewById(R.id.btn_cerrar_sesion);

        btnRegistrarMascota.setOnClickListener(v -> {
            Intent intent = new Intent(MenuDueño.this, RegistroMascotas.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);

        });

        btnPerfilMascota.setOnClickListener(v -> {
            Intent intent = new Intent(MenuDueño.this, perfil_mascota.class);
            startActivity(intent);

        });

        btnRegistrarMedicamento.setOnClickListener(v -> {
            Intent intent = new Intent(MenuDueño.this, RegistroMedicamento.class);
            startActivity(intent);
        });

        btnHistorialMedico.setOnClickListener(v -> {
            Intent intent = new Intent(MenuDueño.this, HistorialMedicoActivity.class);
            startActivity(intent);
        });

        btnCerrarSesion.setOnClickListener(v -> {
              cerrarSesion();
        });
        Button btnTestNotificacion = findViewById(R.id.btnTestNotificacion);

        btnTestNotificacion.setOnClickListener(v -> {
            OneTimeWorkRequest notificacionWork =
                    new OneTimeWorkRequest.Builder(co.edu.unipiloto.petapp.workers.RutaNotificacionWorker.class)
                            .build();

            WorkManager.getInstance(this).enqueue(notificacionWork);
            Toast.makeText(this, "Worker lanzado manualmente", Toast.LENGTH_SHORT).show();
        });

    }
    private void cerrarSesion() {
        SharedPreferences sharedPreferences = getSharedPreferences("UserSession", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();  // Elimina todos los datos guardados en SharedPreferences
        editor.apply();

        // Redirigir al usuario a la pantalla de login
        Intent intent = new Intent(MenuDueño.this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK); // Limpiar la pila de actividades
        startActivity(intent);
        finish();
    }
}