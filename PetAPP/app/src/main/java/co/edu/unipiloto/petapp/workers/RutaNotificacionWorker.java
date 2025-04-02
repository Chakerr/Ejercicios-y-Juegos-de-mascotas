package co.edu.unipiloto.petapp.workers;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import java.util.List;

import co.edu.unipiloto.petapp.R;
import co.edu.unipiloto.petapp.retrofit.PetApi;
import co.edu.unipiloto.petapp.retrofit.RetrofitService;
import co.edu.unipiloto.petapp.model.RutaMascota;
import retrofit2.Call;
import retrofit2.Response;

public class RutaNotificacionWorker extends Worker {

    public RutaNotificacionWorker(@NonNull Context context, @NonNull WorkerParameters params) {
        super(context, params);
    }

    @NonNull
    @Override
    public Result doWork() {
        RetrofitService retrofitService = new RetrofitService();
        PetApi petApi = retrofitService.getRetrofit().create(PetApi.class);

        try {
            Call<List<RutaMascota>> call = petApi.obtenerRutasNoNotificadas();
            Response<List<RutaMascota>> response = call.execute();

            if (response.isSuccessful() && response.body() != null) {
                for (RutaMascota ruta : response.body()) {
                    if (!ruta.isNotificadoInicio()) {
                        mostrarNotificacion("📍 Inicio de recorrido", "Tu mascota ha iniciado un recorrido.");
                        petApi.marcarRutaNotificada((long) ruta.getId(), true, false).execute();
                    } else if (!ruta.isNotificadoFin()) {
                        mostrarNotificacion("🏁 Fin de recorrido", "El cuidador ha finalizado el recorrido.");
                        petApi.marcarRutaNotificada((long) ruta.getId(), false, true).execute();
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            return Result.retry(); // Si algo falla, vuelve a intentar más tarde
        }

        return Result.success();
    }

    private void mostrarNotificacion(String titulo, String mensaje) {
        String canalId = "canal_rutas";
        NotificationManager notificationManager = (NotificationManager)
                getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);

        // Crear canal para Android 8+
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel canal = new NotificationChannel(
                    canalId,
                    "Notificaciones de recorridos",
                    NotificationManager.IMPORTANCE_HIGH
            );
            notificationManager.createNotificationChannel(canal);
        }

        NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext(), canalId)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(titulo)
                .setContentText(mensaje)
                .setPriority(NotificationCompat.PRIORITY_HIGH);

        notificationManager.notify((int) System.currentTimeMillis(), builder.build());
    }
}
