package co.edu.unipiloto.petapp.workers;

import android.app.NotificationManager;
import android.content.Context;
import android.util.Log;
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
import retrofit2.Callback;
import retrofit2.Response;

public class MedicamentoWorker extends Worker {

    public MedicamentoWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }

    @NonNull
    @Override
    public Result doWork() {
        verificarMedicamentosPendientes();
        return Result.success();
    }

    private void verificarMedicamentosPendientes() {
        // Obtener la instancia de RetrofitService
        RetrofitService retrofitService = new RetrofitService();
        PetApi apiService = retrofitService.getRetrofit().create(PetApi.class);

        Call<List<Medicamento>> call = apiService.obtenerMedicamentosPendientes();
        call.enqueue(new Callback<List<Medicamento>>() {
            @Override
            public void onResponse(Call<List<Medicamento>> call, Response<List<Medicamento>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Medicamento> medicamentos = response.body();
                    if (!medicamentos.isEmpty()) {
                        mostrarNotificacion(medicamentos.get(0).getNombre());
                    }
                }
            }

            @Override
            public void onFailure(Call<List<Medicamento>> call, Throwable t) {
                Log.e("MedicamentoWorker", "Error al obtener medicamentos: " + t.getMessage());
            }
        });
    }

    private void mostrarNotificacion(String nombreMedicamento) {
        NotificationManager manager = (NotificationManager) getApplicationContext()
                .getSystemService(Context.NOTIFICATION_SERVICE);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext(), "MEDICAMENTOS_ID")
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setContentTitle("Recordatorio de Medicamento")
                .setContentText("Es hora de administrar " + nombreMedicamento + ".")
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setAutoCancel(true);

        if (manager != null) {
            manager.notify(1, builder.build());
        }
    }
}
