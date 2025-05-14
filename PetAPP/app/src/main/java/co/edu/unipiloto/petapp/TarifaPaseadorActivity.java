package co.edu.unipiloto.petapp;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;
import co.edu.unipiloto.petapp.model.TarifaPaseador;
import co.edu.unipiloto.petapp.model.TarifaPaseadorRequestDTO;
import co.edu.unipiloto.petapp.retrofit.PetApi;
import co.edu.unipiloto.petapp.retrofit.RetrofitService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TarifaPaseadorActivity extends AppCompatActivity {

    private EditText etPrecio, etDistanciaKm;
    private Button btnGuardar, btnActualizar;
    private RecyclerView rvTarifas;
    private PetApi paseadorApi;
    private SharedPreferences sharedPreferences;
    private int paseadorId;
    private TarifaAdapter tarifaAdapter;
    private TarifaPaseador tarifaExistente; // Guarda la primera tarifa (opcional)

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tarifa_paseador);

        // Inicializar SharedPreferences
        sharedPreferences = getSharedPreferences("UserSession", Context.MODE_PRIVATE);
        paseadorId = sharedPreferences.getInt("userId", -1);

        if (paseadorId == -1) {
            Toast.makeText(this, "Error: No se encontró el ID del paseador.", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        // Inicializar Retrofit
        RetrofitService retrofitService = new RetrofitService();
        paseadorApi = retrofitService.getRetrofit().create(PetApi.class);

        // Referencias a la vista
        etPrecio = findViewById(R.id.etPrecio);
        etDistanciaKm = findViewById(R.id.etDistanciaKm);
        btnGuardar = findViewById(R.id.btnGuardar);
        btnActualizar = findViewById(R.id.btnActualizar);
        rvTarifas = findViewById(R.id.rvTarifas);

        // Configurar RecyclerView
        rvTarifas.setLayoutManager(new LinearLayoutManager(this));
        tarifaAdapter = new TarifaAdapter(new ArrayList<>());
        rvTarifas.setAdapter(tarifaAdapter);

        // Botón guardar
        btnGuardar.setOnClickListener(v -> guardarTarifa());

        // Botón actualizar
        btnActualizar.setOnClickListener(v -> actualizarTarifa());

        tarifaAdapter.setOnTarifaClickListener(tarifa -> {
            tarifaExistente = tarifa;
            etPrecio.setText(String.valueOf(tarifa.getPrecio()));
            etDistanciaKm.setText(String.valueOf(tarifa.getDistanciaKm()));
            Toast.makeText(this, "Tarifa seleccionada para editar", Toast.LENGTH_SHORT).show();
        });


        // Cargar tarifas existentes
        obtenerTarifasExistentes();
    }

    private void guardarTarifa() {
        String precioStr = etPrecio.getText().toString().trim();
        String distanciaStr = etDistanciaKm.getText().toString().trim();

        if (precioStr.isEmpty() || distanciaStr.isEmpty()) {
            Toast.makeText(this, "Por favor completa todos los campos.", Toast.LENGTH_SHORT).show();
            return;
        }

        double precio = Double.parseDouble(precioStr);
        double distancia = Double.parseDouble(distanciaStr);

        TarifaPaseadorRequestDTO nuevaTarifa = new TarifaPaseadorRequestDTO();
        nuevaTarifa.setIdPaseador(paseadorId);
        nuevaTarifa.setPrecio(precio);
        nuevaTarifa.setDistanciaKm(distancia);

        paseadorApi.crearTarifa(nuevaTarifa).enqueue(new Callback<TarifaPaseador>() {
            @Override
            public void onResponse(Call<TarifaPaseador> call, Response<TarifaPaseador> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(TarifaPaseadorActivity.this, "Tarifa guardada exitosamente.", Toast.LENGTH_SHORT).show();
                    limpiarCampos();
                    obtenerTarifasExistentes(); // Recargar la lista
                } else {
                    Toast.makeText(TarifaPaseadorActivity.this, "Error al guardar tarifa.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<TarifaPaseador> call, Throwable t) {
                Log.e("RetrofitError", "Error al guardar tarifa: " + t.getMessage());
                Toast.makeText(TarifaPaseadorActivity.this, "Error de conexión.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void actualizarTarifa() {
        if (tarifaExistente == null) {
            Toast.makeText(this, "No hay tarifa existente para actualizar.", Toast.LENGTH_SHORT).show();
            return;
        }

        String precioStr = etPrecio.getText().toString().trim();
        String distanciaStr = etDistanciaKm.getText().toString().trim();

        if (precioStr.isEmpty() || distanciaStr.isEmpty()) {
            Toast.makeText(this, "Por favor completa todos los campos.", Toast.LENGTH_SHORT).show();
            return;
        }

        double nuevoPrecio = Double.parseDouble(precioStr);
        double nuevaDistancia = Double.parseDouble(distanciaStr);

        TarifaPaseadorRequestDTO tarifaActualizada = new TarifaPaseadorRequestDTO();
        tarifaActualizada.setIdPaseador(paseadorId);
        tarifaActualizada.setPrecio(nuevoPrecio);
        tarifaActualizada.setDistanciaKm(nuevaDistancia);

        paseadorApi.actualizarTarifa(tarifaExistente.getIdTarifa(), tarifaActualizada).enqueue(new Callback<TarifaPaseador>() {
            @Override
            public void onResponse(Call<TarifaPaseador> call, Response<TarifaPaseador> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(TarifaPaseadorActivity.this, "Tarifa actualizada exitosamente.", Toast.LENGTH_SHORT).show();
                    obtenerTarifasExistentes(); // Recargar la lista
                } else {
                    Toast.makeText(TarifaPaseadorActivity.this, "Error al actualizar tarifa.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<TarifaPaseador> call, Throwable t) {
                Log.e("RetrofitError", "Error al actualizar tarifa: " + t.getMessage());
                Toast.makeText(TarifaPaseadorActivity.this, "Error de conexión.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void obtenerTarifasExistentes() {
        paseadorApi.listarTarifasPorPaseador(paseadorId).enqueue(new Callback<List<TarifaPaseador>>() {
            @Override
            public void onResponse(Call<List<TarifaPaseador>> call, Response<List<TarifaPaseador>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<TarifaPaseador> tarifas = response.body();
                    tarifaAdapter.setTarifas(tarifas);

                    if (!tarifas.isEmpty()) {
                        // Se asegura que se obtiene la primera tarifa
                        tarifaExistente = tarifas.get(0);

                        // Ahora verificamos que tarifaExistente no sea null y tiene precio y distancia
                        if (tarifaExistente != null) {
                            if (tarifaExistente.getPrecio() != null) {
                                etPrecio.setText(String.valueOf(tarifaExistente.getPrecio()));
                            }
                            if (tarifaExistente.getDistanciaKm() != null) {
                                etDistanciaKm.setText(String.valueOf(tarifaExistente.getDistanciaKm()));
                            }
                        }
                    }

                    Log.d("Tarifas", "Cantidad de tarifas cargadas: " + tarifas.size());
                } else {
                    Log.d("Tarifas", "No se encontraron tarifas.");
                    tarifaAdapter.setTarifas(new ArrayList<>()); // Vaciar el RecyclerView si no hay tarifas
                }
            }

            @Override
            public void onFailure(Call<List<TarifaPaseador>> call, Throwable t) {
                Log.e("RetrofitError", "Error al obtener tarifas: " + t.getMessage());
            }
        });
    }


    private void limpiarCampos() {
        etPrecio.setText("");
        etDistanciaKm.setText("");
    }
}
