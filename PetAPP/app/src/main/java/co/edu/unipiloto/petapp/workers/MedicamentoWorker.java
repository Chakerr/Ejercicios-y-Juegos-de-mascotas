package co.edu.unipiloto.petapp.workers;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.work.Worker;
import androidx.work.WorkerParameters;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import co.edu.unipiloto.petapp.R;
import co.edu.unipiloto.petapp.model.Medicamento;
import co.edu.unipiloto.petapp.retrofit.PetApi;
import co.edu.unipiloto.petapp.retrofit.RetrofitService;
import retrofit2.Call;
import retrofit2.Response;

public class MedicamentoWorker extends Worker {

    private static final String CHANNEL_ID = "medicamento_channel";
    private static final String PREFS_NAME = "MedicamentoPrefs";
    private static final String KEY_NOTIFIED_MEDICAMENTOS = "notified_medicamentos";

    public MedicamentoWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }

    @NonNull
    @Override
    public Result doWork() {
        Context context = getApplicationContext();
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        Set<String> medicamentosNotificados = sharedPreferences.getStringSet(KEY_NOTIFIED_MEDICAMENTOS, new HashSet<>());

        // Llamada a la API para obtener medicamentos pendientes
        RetrofitService retrofitService = new RetrofitService();
        PetApi petApi = retrofitService.getRetrofit().create(PetApi.class);

        Call<List<Medicamento>> call = petApi.obtenerMedicamentosPendientes();
        try {
            Response<List<Medicamento>> response = call.execute();
            if (response.isSuccessful() && response.body() != null) {
                Set<String> nuevosNotificados = new HashSet<>(medicamentosNotificados);

                for (Medicamento medicamento : response.body()) {
                    String nombreMedicamento = medicamento.getNombre();
                    if (!medicamentosNotificados.contains(nombreMedicamento)) {
                        mostrarNotificacion(nombreMedicamento);
                        nuevosNotificados.add(nombreMedicamento);
                    }
                }

                // Guardar medicamentos notificados
                sharedPreferences.edit().putStringSet(KEY_NOTIFIED_MEDICAMENTOS, nuevosNotificados).apply();

                return Result.success();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Result.retry();
    }

    private void mostrarNotificacion(String medicamento) {
        Context context = getApplicationContext();
        NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    CHANNEL_ID, "Recordatorio de Medicamento",
                    NotificationManager.IMPORTANCE_HIGH
            );
            manager.createNotificationChannel(channel);
        }

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setContentTitle("Recordatorio de Medicamento")
                .setContentText("Es hora de tomar: " + medicamento)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setAutoCancel(true);

        manager.notify(medicamento.hashCode(), builder.build());
    }
}
