package co.edu.unipiloto.petapp;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;

import java.util.List;

import co.edu.unipiloto.petapp.model.ServicioPaseo;
import co.edu.unipiloto.petapp.retrofit.PetApi;
import co.edu.unipiloto.petapp.retrofit.RetrofitService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MenuPaseador extends AppCompatActivity {

    private PetApi petApi;
    private int idPaseador;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_paseador);

        Button btnCerrarSesion = findViewById(R.id.btn_cerrar_sesionPaseador);
        Button btnIniciarRecorrido = findViewById(R.id.btnIniciarRecorrido);
        Button btnGestionTarifas = findViewById(R.id.btnGestionTarifas);

        // Obtener id del paseador desde SharedPreferences
        SharedPreferences preferences = getSharedPreferences("UserSession", Context.MODE_PRIVATE);
        idPaseador = preferences.getInt("userId", -1);

        // Inicializar Retrofit
        RetrofitService retrofitService = new RetrofitService();
        petApi = retrofitService.getRetrofit().create(PetApi.class);


        // Verificar si hay servicios pendientes al iniciar
        if (idPaseador != -1) {
            verificarServiciosPendientes(idPaseador);
        }

        btnCerrarSesion.setOnClickListener(v -> cerrarSesion());

        btnIniciarRecorrido.setOnClickListener(v -> {
            Intent intent = new Intent(MenuPaseador.this, InicioRecorridoActivity.class);
            startActivity(intent);
        });

        btnGestionTarifas.setOnClickListener(v -> {
            Intent intent = new Intent(MenuPaseador.this, TarifaPaseadorActivity.class);
            startActivity(intent);
        });

        MaterialButton btnPeticiones = findViewById(R.id.btnPeticiones);
        btnPeticiones.setOnClickListener(v -> {
            Intent intent = new Intent(MenuPaseador.this, Peticiones.class);
            startActivity(intent);
        });

    }

    private void verificarServiciosPendientes(int idPaseador) {
        Call<List<ServicioPaseo>> call = petApi.obtenerPendientes(idPaseador);

        call.enqueue(new Callback<List<ServicioPaseo>>() {
            @Override
            public void onResponse(Call<List<ServicioPaseo>> call, Response<List<ServicioPaseo>> response) {
                if (response.isSuccessful() && response.body() != null && !response.body().isEmpty()) {
                    mostrarAlertaPendientes(response.body().size());
                }
            }

            @Override
            public void onFailure(Call<List<ServicioPaseo>> call, Throwable t) {
                Log.e("MenuPaseador", "Error al obtener servicios pendientes", t);
            }
        });
    }

    private void mostrarAlertaPendientes(int cantidad) {
        new AlertDialog.Builder(this)
                .setTitle("Servicios pendientes")
                .setMessage("Tienes " + cantidad + " servicio(s) pendiente(s) por aceptar o atender.")
                .setPositiveButton("OK", null)
                .show();
    }

    private void cerrarSesion() {
        SharedPreferences sharedPreferences = getSharedPreferences("UserSession", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();

        Intent intent = new Intent(MenuPaseador.this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }
}
