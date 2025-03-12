package co.edu.unipiloto.petapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;
import androidx.work.WorkRequest;
import java.util.concurrent.TimeUnit;
import co.edu.unipiloto.petapp.workers.MedicamentoWorker;
import androidx.work.ExistingPeriodicWorkPolicy;
import android.Manifest;
import android.content.pm.PackageManager;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Solicitar permiso en Android 13+
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.POST_NOTIFICATIONS}, 1);
            }
        }

        // Crear canal de notificación
        crearCanalDeNotificacion();

        // Programar notificación cada 6 horas
        programarNotificacionPeriodica();

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
    private void crearCanalDeNotificacion() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel canal = new NotificationChannel(
                    "MEDICAMENTOS_ID",
                    "Canal de Recordatorios",
                    NotificationManager.IMPORTANCE_HIGH
            );
            canal.setDescription("Canal para recordatorios de medicamentos");

            NotificationManager manager = getSystemService(NotificationManager.class);
            if (manager != null) {
                manager.createNotificationChannel(canal);
            }
        }
    }
    private void programarNotificacionPeriodica() {
        PeriodicWorkRequest workRequest = new PeriodicWorkRequest.Builder(
                MedicamentoWorker.class,
                2, TimeUnit.MINUTES)
                .setInitialDelay(1, TimeUnit.MINUTES)
                .build();

        WorkManager.getInstance(this).enqueueUniquePeriodicWork(
                "medicamento_notificacion",
                ExistingPeriodicWorkPolicy.REPLACE,
                workRequest
        );

    }
}
