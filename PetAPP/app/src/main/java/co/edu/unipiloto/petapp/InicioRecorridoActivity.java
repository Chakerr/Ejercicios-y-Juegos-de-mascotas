package co.edu.unipiloto.petapp;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Collections;
import java.util.List;

import co.edu.unipiloto.petapp.model.Mascota;
import co.edu.unipiloto.petapp.retrofit.PetApi;
import co.edu.unipiloto.petapp.retrofit.RetrofitService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class InicioRecorridoActivity extends AppCompatActivity {

    private Button btnSimular;
    private Spinner spinnerMascotas;
    private List<Mascota> listaMascotas;
    private int mascotaSeleccionadaId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inicio_recorrido);

        btnSimular = findViewById(R.id.btnSimularRecorrido);
        Button btnFinalizarRecorrido = findViewById(R.id.btnFinalizarRecorrido);
        spinnerMascotas = findViewById(R.id.spinnerMascotas);

        RetrofitService retrofitService = new RetrofitService();
        PetApi petApi = retrofitService.getRetrofit().create(PetApi.class);

        SharedPreferences prefs = getSharedPreferences("UserSession", Context.MODE_PRIVATE);
        int usuarioId = prefs.getInt("userId", -1);

        Log.d("Mascotas", "ID de usuario recuperado: " + usuarioId);
        Toast.makeText(this, "Cargando mascotas...", Toast.LENGTH_SHORT).show();

        petApi.obtenerTodasLasMascotas().enqueue(new Callback<List<Mascota>>() {
            @Override
            public void onResponse(Call<List<Mascota>> call, Response<List<Mascota>> response) {
                Log.d("Mascotas", "onResponse ejecutado");

                if (response.isSuccessful() && response.body() != null) {
                    listaMascotas = response.body();
                    Log.d("Mascotas", "Mascotas cargadas: " + listaMascotas.size());

                    ArrayAdapter<Mascota> adapter = new ArrayAdapter<>(
                            InicioRecorridoActivity.this,
                            android.R.layout.simple_spinner_item,
                            listaMascotas
                    );
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinnerMascotas.setAdapter(adapter);

                    spinnerMascotas.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            mascotaSeleccionadaId = listaMascotas.get(position).getIdMascota();
                            Log.d("Mascotas", "Mascota seleccionada: " + mascotaSeleccionadaId);
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {}
                    });
                } else {
                    Log.e("Mascotas", "Error en la respuesta: código " + response.code());
                    Toast.makeText(InicioRecorridoActivity.this, "Error en la respuesta del servidor", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Mascota>> call, Throwable t) {
                Log.e("Mascotas", "Fallo en la conexión: " + t.getMessage());
                Toast.makeText(InicioRecorridoActivity.this, "Error de conexión: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        btnSimular.setOnClickListener(view -> {
            int userId = prefs.getInt("userId", -1);
            if (userId == -1 || mascotaSeleccionadaId == 0) {
                Toast.makeText(this, "Debe seleccionar una mascota y haber iniciado sesión", Toast.LENGTH_SHORT).show();
                return;
            }

        });
    }
}
