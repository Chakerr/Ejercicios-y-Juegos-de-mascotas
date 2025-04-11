package co.edu.unipiloto.petapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
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

        androidx.appcompat.widget.Toolbar toolbar = findViewById(R.id.toolbar_menu);
        setSupportActionBar(toolbar);

        Button btnPerfilMascota = findViewById(R.id.btn_perfil_mascota);
        Button btnRegistrarMedicamento = findViewById(R.id.btn_registrar_medicamento);
        Button btnHistorialMedico = findViewById(R.id.btn_historial_medico);
        Button btnCerrarSesion = findViewById(R.id.btn_cerrar_sesion);

        btnPerfilMascota.setOnClickListener(v -> {
            startActivity(new Intent(MenuDueño.this, perfil_mascota.class));
        });

        btnRegistrarMedicamento.setOnClickListener(v -> {
            startActivity(new Intent(MenuDueño.this, RegistroMedicamento.class));
        });

        btnHistorialMedico.setOnClickListener(v -> {
            startActivity(new Intent(MenuDueño.this, HistorialMedicoActivity.class));
        });

        btnCerrarSesion.setOnClickListener(v -> cerrarSesion());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_toolbar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_registrar_mascota) {
            startActivity(new Intent(this, RegistroMascotas.class));
            return true;
        } else if (id == R.id.action_test_notificacion) {
            OneTimeWorkRequest notificacionWork =
                    new OneTimeWorkRequest.Builder(co.edu.unipiloto.petapp.workers.RutaNotificacionWorker.class).build();
            WorkManager.getInstance(this).enqueue(notificacionWork);
            Toast.makeText(this, "Worker lanzado manualmente", Toast.LENGTH_SHORT).show();
            return true;
        }

        return super.onOptionsItemSelected(item);
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