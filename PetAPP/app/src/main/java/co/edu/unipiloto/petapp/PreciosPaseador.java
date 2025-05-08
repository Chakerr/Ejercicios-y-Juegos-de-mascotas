package co.edu.unipiloto.petapp;

import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import co.edu.unipiloto.petapp.model.TarifaPaseador;
import co.edu.unipiloto.petapp.retrofit.PetApi;
import co.edu.unipiloto.petapp.retrofit.RetrofitService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PreciosPaseador extends AppCompatActivity {

    private RecyclerView recyclerView;
    private TarifaAdapter adapter;
    private PetApi apiService;  // CAMBIO: usar la interfaz correcta

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_precios_paseador);

        recyclerView = findViewById(R.id.recyclerTarifas);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new TarifaAdapter(null);
        recyclerView.setAdapter(adapter);

        // CAMBIO: usar RetrofitService para obtener Retrofit
        RetrofitService retrofitService = new RetrofitService();
        apiService = retrofitService.getRetrofit().create(PetApi.class);

        int idPaseador = 2; // O tomarlo de SharedPreferences
        obtenerTarifas(idPaseador);
    }

    private void obtenerTarifas(int idPaseador) {
        apiService.listarTarifasPorPaseador(idPaseador).enqueue(new Callback<List<TarifaPaseador>>() {
            @Override
            public void onResponse(Call<List<TarifaPaseador>> call, Response<List<TarifaPaseador>> response) {
                if (response.isSuccessful()) {
                    adapter.setTarifas(response.body());
                } else {
                    Log.e("API", "Error en la respuesta: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<List<TarifaPaseador>> call, Throwable t) {
                Log.e("API", "Error de conexión", t);
            }
        });
    }
}
