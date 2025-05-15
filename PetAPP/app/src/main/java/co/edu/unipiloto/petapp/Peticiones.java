package co.edu.unipiloto.petapp;

import android.content.Context;
import android.content.SharedPreferences;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.io.IOException;
import java.util.List;
import java.util.Locale;


import co.edu.unipiloto.petapp.model.ServicioPaseo;
import co.edu.unipiloto.petapp.retrofit.PetApi;

import co.edu.unipiloto.petapp.retrofit.RetrofitService;
import co.edu.unipiloto.petapp.workers.PeticionAdapter;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Peticiones extends AppCompatActivity {

    private RecyclerView recyclerPeticiones;
    private int idPaseador;
    private PetApi apiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_peticiones);

        recyclerPeticiones = findViewById(R.id.recyclerPeticiones);
        recyclerPeticiones.setLayoutManager(new LinearLayoutManager(this));

        RetrofitService retrofitService = new RetrofitService();
        apiService = retrofitService.getRetrofit().create(PetApi.class);

        SharedPreferences preferences = getSharedPreferences("UserSession", Context.MODE_PRIVATE);
        idPaseador = preferences.getInt("userId", -1);

        if (idPaseador != -1) {
            cargarPeticiones(idPaseador);
        }
    }

    private void cargarPeticiones(int idPaseador) {
        apiService.getServiciosPorPaseador(idPaseador).enqueue(new Callback<List<ServicioPaseo>>() {
            @Override
            public void onResponse(Call<List<ServicioPaseo>> call, Response<List<ServicioPaseo>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    PeticionAdapter adapter = new PeticionAdapter(response.body(), Peticiones.this);
                    recyclerPeticiones.setAdapter(adapter);
                }
            }

            @Override
            public void onFailure(Call<List<ServicioPaseo>> call, Throwable t) {
                Log.e("Peticiones", "Error cargando peticiones", t);
            }
        });
    }

    public static String obtenerDireccionDesdeCoordenadas(Context context, double lat, double lon) {
        Geocoder geocoder = new Geocoder(context, Locale.getDefault());
        try {
            List<Address> direcciones = geocoder.getFromLocation(lat, lon, 1);
            if (direcciones != null && !direcciones.isEmpty()) {
                return direcciones.get(0).getAddressLine(0);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "Dirección no disponible";
    }

}
