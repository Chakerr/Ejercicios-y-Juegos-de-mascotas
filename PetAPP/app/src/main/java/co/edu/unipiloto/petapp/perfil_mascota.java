package co.edu.unipiloto.petapp;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import com.google.gson.Gson;
import com.journeyapps.barcodescanner.BarcodeEncoder;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import java.util.ArrayList;
import java.util.List;
import co.edu.unipiloto.petapp.model.Mascota;
import co.edu.unipiloto.petapp.retrofit.PetApi;
import co.edu.unipiloto.petapp.retrofit.RetrofitService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class perfil_mascota extends AppCompatActivity {

    private Spinner spinnerMascotas;
    private TextView tvPetName, tvSpecies, tvBreed, tvGender, tvBirthDate, tvColor, tvMicrochip;
    private ImageView qrImageView;
    private Button btnGenerateQR;
    private PetApi petApi;
    private SharedPreferences sharedPreferences;
    private List<Mascota> listaMascotas = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil_mascota);

        // Referencias a los elementos de la UI
        spinnerMascotas = findViewById(R.id.spinnerMascotas);
        tvPetName = findViewById(R.id.tvPetName);
        tvSpecies = findViewById(R.id.tvSpecies);
        tvBreed = findViewById(R.id.tvBreed);
        tvGender = findViewById(R.id.tvGender);
        tvBirthDate = findViewById(R.id.tvBirthDate);
        tvColor = findViewById(R.id.tvColor);
        tvMicrochip = findViewById(R.id.tvMicrochip);
        qrImageView = findViewById(R.id.qrImageView);
        btnGenerateQR = findViewById(R.id.btnGenerarQR);

        // Inicializar SharedPreferences
        sharedPreferences = getSharedPreferences("UserSession", Context.MODE_PRIVATE);
        int userId = sharedPreferences.getInt("userId", -1);

        if (userId == -1) {
            tvPetName.setText("Error: No se encontró el ID del usuario.");
            return;
        }

        // Inicializar Retrofit
        RetrofitService retrofitService = new RetrofitService();
        petApi = retrofitService.getRetrofit().create(PetApi.class);

        // Obtener mascotas del usuario
        obtenerMascotas(userId);

        // Listener del botón para generar QR
        btnGenerateQR.setOnClickListener(v -> {
            int selectedPosition = spinnerMascotas.getSelectedItemPosition();
            if (!listaMascotas.isEmpty() && selectedPosition >= 0) {
                generarQR(listaMascotas.get(selectedPosition));
            } else {
                Toast.makeText(this, "No hay mascota seleccionada", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void obtenerMascotas(int userId) {
        Call<List<Mascota>> call = petApi.getMascotasByUsuarioId(userId);
        call.enqueue(new Callback<List<Mascota>>() {
            @Override
            public void onResponse(Call<List<Mascota>> call, Response<List<Mascota>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    listaMascotas = response.body();
                    if (!listaMascotas.isEmpty()) {
                        configurarSpinner();
                    } else {
                        tvPetName.setText("No se encontraron mascotas.");
                    }
                } else {
                    tvPetName.setText("Error al obtener mascotas.");
                }
            }

            @Override
            public void onFailure(Call<List<Mascota>> call, Throwable t) {
                Log.e("RetrofitError", "Error al obtener mascotas: " + t.getMessage());
                tvPetName.setText("Error al obtener datos.");
            }
        });
    }

    private void configurarSpinner() {
        List<String> nombresMascotas = new ArrayList<>();
        for (Mascota mascota : listaMascotas) {
            nombresMascotas.add(mascota.getNombreMascota());
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, nombresMascotas);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerMascotas.setAdapter(adapter);

        spinnerMascotas.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                actualizarDatosMascota(listaMascotas.get(position));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        if (!listaMascotas.isEmpty()) {
            actualizarDatosMascota(listaMascotas.get(0));
        }
    }

    private void actualizarDatosMascota(Mascota mascota) {
        tvPetName.setText(mascota.getNombreMascota());
        tvSpecies.setText(mascota.getEspecie());
        tvBreed.setText(mascota.getRaza());
        tvGender.setText(mascota.getSexo());
        tvBirthDate.setText(mascota.getFechaNacimiento());
        tvColor.setText(mascota.getColor());
        tvMicrochip.setText(mascota.getMicrochip() ? "Sí" : "No");


    }

    private void generarQR(Mascota mascota) {
        try {
            Gson gson = new Gson();
            String mascotaJson = gson.toJson(mascota); // Convertir mascota a JSON

            BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
            Bitmap bitmap = barcodeEncoder.encodeBitmap(mascotaJson, BarcodeFormat.QR_CODE, 400, 400);

            // Verifica si el ImageView es nulo antes de asignar el QR
            if (qrImageView != null) {
                qrImageView.setImageBitmap(bitmap);
                qrImageView.setVisibility(View.VISIBLE); // Asegurar que sea visible
            } else {
                Log.e("QR_ERROR", "ImageView es nulo");
            }

        } catch (WriterException e) {
            Log.e("QR_ERROR", "Error al generar QR: " + e.getMessage());
            Toast.makeText(this, "Error al generar QR", Toast.LENGTH_SHORT).show();
        }
    }
}
