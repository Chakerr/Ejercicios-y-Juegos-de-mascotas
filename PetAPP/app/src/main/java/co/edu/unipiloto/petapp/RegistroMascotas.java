package co.edu.unipiloto.petapp;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import co.edu.unipiloto.petapp.retrofit.PetApi;
import co.edu.unipiloto.petapp.retrofit.RetrofitService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.util.HashMap;
import java.util.Map;

public class RegistroMascotas extends AppCompatActivity {

    private EditText etNombre, etFecha, etEspecie, etRaza, etSexo, etColor;
    @SuppressLint("UseSwitchCompatOrMaterialCode")
    private Switch switchMicrochip;
    private PetApi petApi;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.registro_mascotas);

        // Vincular elementos con el layout
        etNombre = findViewById(R.id.etNombreMascota);
        etFecha = findViewById(R.id.etFechaNacimiento);
        etEspecie = findViewById(R.id.etEspecie);
        etRaza = findViewById(R.id.etRaza);
        etSexo = findViewById(R.id.etSexo);
        etColor = findViewById(R.id.etColor);
        switchMicrochip = findViewById(R.id.switchMicrochip);
        Button btnRegistrarMascota = findViewById(R.id.btnRegistrarMascota);

        // Inicializar Retrofit y SharedPreferences
        RetrofitService retrofitService = new RetrofitService();
        petApi = retrofitService.getRetrofit().create(PetApi.class);
        sharedPreferences = getSharedPreferences("UserSession", Context.MODE_PRIVATE);

        // Evento del botón para registrar mascota
        btnRegistrarMascota.setOnClickListener(v -> registrarMascota());
        Intent intent = new Intent(RegistroMascotas.this, menu.class);
        startActivity(intent);
    }

    private void registrarMascota() {
        int userId = sharedPreferences.getInt("userId", -1);
        if (userId == -1) {
            Toast.makeText(this, "Error: Usuario no identificado", Toast.LENGTH_SHORT).show();
            return;
        }

        // Crear objeto de mascota (puede ser un Map o una clase Mascota)
        Map<String, Object> mascota = new HashMap<>();
        mascota.put("nombreMascota", etNombre.getText().toString().trim());
        mascota.put("fechaNacimiento", etFecha.getText().toString().trim());
        mascota.put("especie", etEspecie.getText().toString().trim());
        mascota.put("raza", etRaza.getText().toString().trim());
        mascota.put("sexo", etSexo.getText().toString().trim());
        mascota.put("color", etColor.getText().toString().trim());
        mascota.put("microchip", switchMicrochip.isChecked());

        // Enviar petición POST al servidor con el ID del usuario
        Call<Map<String, Object>> call = petApi.agregarMascota(userId, mascota);

        call.enqueue(new Callback<>() {
            @Override
            public void onResponse(@NonNull Call<Map<String, Object>> call, @NonNull Response<Map<String, Object>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Toast.makeText(RegistroMascotas.this, "Mascota registrada con éxito", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(RegistroMascotas.this, "Error en el registro", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<Map<String, Object>> call, @NonNull Throwable t) {
                Toast.makeText(RegistroMascotas.this, "Error de conexión: " + t.getMessage(), Toast.LENGTH_LONG).show();
            }


        });

    }

}