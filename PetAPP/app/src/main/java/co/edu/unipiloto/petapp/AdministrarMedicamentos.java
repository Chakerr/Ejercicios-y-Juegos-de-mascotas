package co.edu.unipiloto.petapp;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
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
    private List<Medicamento> listaMedicamentos;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_administrar_medicamentos);

        spinnerMedicamentos = findViewById(R.id.spinnerMedicamentos);
        btnMarcarAdministrado = findViewById(R.id.btnMarcarAdministrado);

        cargarMedicamentos();

        btnMarcarAdministrado.setOnClickListener(v -> {
            int posicionSeleccionada = spinnerMedicamentos.getSelectedItemPosition();
            if (posicionSeleccionada >= 0 && listaMedicamentos != null) {
                Medicamento medicamento = listaMedicamentos.get(posicionSeleccionada);
                marcarComoAdministrado(medicamento.getId());
            } else {
                Toast.makeText(this, "Selecciona un medicamento", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void cargarMedicamentos() {
        SharedPreferences sharedPreferences = getSharedPreferences("UserSession", Context.MODE_PRIVATE);
        int userId = sharedPreferences.getInt("userId", -1);

        if (userId == -1) {
            Toast.makeText(this, "Error: No se encontró el ID del usuario.", Toast.LENGTH_SHORT).show();
            return;
        }

        RetrofitService retrofitService = new RetrofitService();
        PetApi petApi = retrofitService.getRetrofit().create(PetApi.class);

        Call<List<Medicamento>> call = petApi.getMedicamentos();
        call.enqueue(new Callback<List<Medicamento>>() {
            @Override
            public void onResponse(Call<List<Medicamento>> call, Response<List<Medicamento>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    listaMedicamentos = response.body();
                    List<String> nombresMedicamentos = new ArrayList<>();
                    for (Medicamento medicamento : listaMedicamentos) {
                        nombresMedicamentos.add(medicamento.getNombre());
                    }
                    ArrayAdapter<String> adapter = new ArrayAdapter<>(AdministrarMedicamentos.this, android.R.layout.simple_spinner_dropdown_item, nombresMedicamentos);
                    spinnerMedicamentos.setAdapter(adapter);
                } else {
                    Toast.makeText(AdministrarMedicamentos.this, "Error al obtener medicamentos", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Medicamento>> call, Throwable t) {
                Toast.makeText(AdministrarMedicamentos.this, "Error de conexión", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void marcarComoAdministrado(Long id) {
        RetrofitService retrofitService = new RetrofitService();
        PetApi petApi = retrofitService.getRetrofit().create(PetApi.class);

        Call<Medicamento> call = petApi.marcarMedicamentoComoAdministrado(id);
        call.enqueue(new Callback<Medicamento>() {
            @Override
            public void onResponse(Call<Medicamento> call, Response<Medicamento> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(AdministrarMedicamentos.this, "Medicamento marcado como administrado", Toast.LENGTH_SHORT).show();
                    cargarMedicamentos(); // Recargar la lista
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
}
