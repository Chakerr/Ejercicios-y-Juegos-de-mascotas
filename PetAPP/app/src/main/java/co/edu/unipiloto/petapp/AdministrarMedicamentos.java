package co.edu.unipiloto.petapp;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;
import java.util.List;
import co.edu.unipiloto.petapp.model.Medicamento;
import co.edu.unipiloto.petapp.retrofit.PetApi;
import co.edu.unipiloto.petapp.retrofit.RetrofitService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AdministrarMedicamentos extends AppCompatActivity {

    private Spinner spinnerMedicamentos;
    private Button btnMarcarAdministrado;
    private List<Medicamento> listaMedicamentos = new ArrayList<>();
    private int mascotaId; // Variable para almacenar el ID de la mascota

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_administrar_medicamentos);

        spinnerMedicamentos = findViewById(R.id.spinnerMedicamentos);
        btnMarcarAdministrado = findViewById(R.id.btnMarcarAdministrado);

        // Obtener el ID de la mascota desde el Intent
        mascotaId = getIntent().getIntExtra("mascotaId", -1);
        if (mascotaId == -1) {
            Toast.makeText(this, "Error: No se encontró el ID de la mascota.", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        // Cargar medicamentos de la mascota
        cargarMedicamentos(mascotaId);

        // Marcar medicamento como administrado cuando se presiona el botón
        btnMarcarAdministrado.setOnClickListener(v -> {
            int posicionSeleccionada = spinnerMedicamentos.getSelectedItemPosition();
            if (posicionSeleccionada >= 0 && !listaMedicamentos.isEmpty()) {
                Medicamento medicamento = listaMedicamentos.get(posicionSeleccionada);
                marcarComoAdministrado(medicamento.getId());
            } else {
                Toast.makeText(this, "Selecciona un medicamento", Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * Carga los medicamentos de una mascota específica desde el backend
     */
    private void cargarMedicamentos(int mascotaId) {
        RetrofitService retrofitService = new RetrofitService();
        PetApi petApi = retrofitService.getRetrofit().create(PetApi.class);

        Call<List<Medicamento>> call = petApi.getMedicamentosPorMascota(mascotaId);
        call.enqueue(new Callback<List<Medicamento>>() {
            @Override
            public void onResponse(Call<List<Medicamento>> call, Response<List<Medicamento>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    listaMedicamentos = response.body();
                    actualizarSpinnerMedicamentos();
                } else {
                    Toast.makeText(AdministrarMedicamentos.this, "No se encontraron medicamentos.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Medicamento>> call, Throwable t) {
                Toast.makeText(AdministrarMedicamentos.this, "Error de conexión", Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * Marca un medicamento como administrado en el backend
     */
    private void marcarComoAdministrado(Long idMedicamento) {
        RetrofitService retrofitService = new RetrofitService();
        PetApi petApi = retrofitService.getRetrofit().create(PetApi.class);

        Call<Medicamento> call = petApi.marcarMedicamentoComoAdministrado(idMedicamento);
        call.enqueue(new Callback<Medicamento>() {
            @Override
            public void onResponse(Call<Medicamento> call, Response<Medicamento> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(AdministrarMedicamentos.this, "Medicamento marcado como administrado", Toast.LENGTH_SHORT).show();
                    cargarMedicamentos(mascotaId); // Recargar la lista con los medicamentos actualizados
                } else {
                    Toast.makeText(AdministrarMedicamentos.this, "Error al actualizar medicamento", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Medicamento> call, Throwable t) {
                Toast.makeText(AdministrarMedicamentos.this, "Error de conexión", Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * Actualiza el Spinner con los medicamentos cargados
     */
    private void actualizarSpinnerMedicamentos() {
        List<String> nombresMedicamentos = new ArrayList<>();
        for (Medicamento medicamento : listaMedicamentos) {
            nombresMedicamentos.add(medicamento.getNombre());
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, nombresMedicamentos);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerMedicamentos.setAdapter(adapter);
    }
}
