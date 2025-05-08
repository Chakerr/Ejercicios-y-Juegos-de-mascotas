package co.edu.unipiloto.petapp;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import java.util.List;
import co.edu.unipiloto.petapp.model.TarifaPaseador;
import co.edu.unipiloto.petapp.retrofit.PetApi;
import co.edu.unipiloto.petapp.retrofit.RetrofitService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TarifaPaseador extends AppCompatActivity {

    private EditText etPrecio, etDistanciaKm;
    private Button btnGuardar, btnActualizar;
    private PetApi paseadorApi;
    private SharedPreferences sharedPreferences;
    private int paseadorId; // ID del paseador obtenido del SharedPreferences

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tarifa_paseador);

        // Configurar SharedPreferences
        sharedPreferences = getSharedPreferences("UserSession", Context.MODE_PRIVATE);
        paseadorId = sharedPreferences.getInt("userId", -1); // Igual que tu perfil_mascota
        if (paseadorId == -1) {
            Toast.makeText(this, "Error: No se encontró el ID del paseador.", Toast.LENGTH_SHORT).show();
            finish(); // Cierra la actividad si no hay ID
            return;
        }

        // Inicializar Retrofit
        RetrofitService retrofitService = new RetrofitService();
        paseadorApi = retrofitService.getRetrofit().create(PetApi.class);

        // Inicializar vistas
        etPrecio = findViewById(R.id.etPrecio);
        etDistanciaKm = findViewById(R.id.etDistanciaKm);
        btnGuardar = findViewById(R.id.btnGuardar);
        btnActualizar = findViewById(R.id.btnActualizar);

        // Listener Botón Guardar
        btnGuardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                guardarTarifa();
            }
        });

        // Listener Botón Actualizar
        btnActualizar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                actualizarTarifas();
            }
        });

        // Opcional: aquí podrías cargar tarifas existentes en un RecyclerView si quieres
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

        TarifaPaseador nuevaTarifa = new TarifaPaseador();
        nuevaTarifa.setIdPaseador(paseadorId);
        nuevaTarifa.setPrecio(precio);
        nuevaTarifa.setDistanciaKm(distancia);

        paseadorApi.guardarTarifa(nuevaTarifa).enqueue(new Callback<TarifaPaseador>() {
            @Override
            public void onResponse(Call<TarifaPaseador> call, Response<TarifaPaseador> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(TarifaPaseadorActivity.this, "Tarifa guardada exitosamente.", Toast.LENGTH_SHORT).show();
                    limpiarCampos();
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

    private void actualizarTarifas() {
        // Aquí puedes implementar actualización si ya tienes tarifas existentes (opcional)
        Toast.makeText(this, "Funcionalidad de actualización no implementada aún.", Toast.LENGTH_SHORT).show();
    }

    private void limpiarCampos() {
        etPrecio.setText("");
        etDistanciaKm.setText("");
    }
}
}
