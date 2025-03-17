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
import co.edu.unipiloto.petapp.model.Medicamento;
import co.edu.unipiloto.petapp.retrofit.PetApi;
import co.edu.unipiloto.petapp.retrofit.RetrofitService;
import retrofit2.Call;
import retrofit2.Response;

public class MedicamentoWorker extends Worker {

    private static final String CHANNEL_ID = "medicamento_channel";

    public MedicamentoWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }

    @NonNull
    @Override
    public Result doWork() {
        // Llamada a la API para obtener medicamentos pendientes
        RetrofitService retrofitService = new RetrofitService();
        PetApi petApi = retrofitService.getRetrofit().create(PetApi.class);

        Call<List<Medicamento>> call = petApi.obtenerMedicamentosPendientes();
        try {
            Response<List<Medicamento>> response = call.execute();
            if (response.isSuccessful() && response.body() != null) {
                for (Medicamento medicamento : response.body()) {
                    mostrarNotificacion(medicamento.getNombre());
                }
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

        manager.notify((int) System.currentTimeMillis(), builder.build());
    }
}