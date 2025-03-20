package co.edu.unipiloto.petapp;

import android.content.Context;
import android.content.Intent;
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
import android.location.Address;
import android.location.Geocoder;
import java.io.IOException;
import java.util.Locale;
import org.osmdroid.config.Configuration;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;
import android.preference.PreferenceManager;
import co.edu.unipiloto.petapp.AdministrarMedicamentos;


public class perfil_mascota extends AppCompatActivity {

    private Spinner spinnerMascotas;
    private TextView tvPetName, tvSpecies, tvBreed, tvGender, tvBirthDate, tvColor, tvMicrochip;
    private ImageView qrImageView;
    private Button btnGenerateQR;
    private PetApi petApi;
    private SharedPreferences sharedPreferences;
    private List<Mascota> listaMascotas = new ArrayList<>();
    private TextView tvDireccion;
    private MapView mapView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil_mascota);

        Button btnAdministrarMedicamentos = findViewById(R.id.btnAdministrarMedicamentos);
        btnAdministrarMedicamentos.setOnClickListener(v -> {
            Intent intent = new Intent(perfil_mascota.this, AdministrarMedicamentos.class);
            startActivity(intent);
        });

        Configuration.getInstance().load(getApplicationContext(), PreferenceManager.getDefaultSharedPreferences(this));
        mapView = findViewById(R.id.mapView);
        mapView.setTileSource(org.osmdroid.tileprovider.tilesource.TileSourceFactory.MAPNIK);
        mapView.setBuiltInZoomControls(true);
        mapView.setMultiTouchControls(true);

        // Referencias a los elementos de la UI
        spinnerMascotas = findViewById(R.id.spinnerMascotas);
        tvPetName = findViewById(R.id.tvPetName);
        tvSpecies = findViewById(R.id.tvSpecies);
        tvBreed = findViewById(R.id.tvBreed);
        tvGender = findViewById(R.id.tvGender);
        tvBirthDate = findViewById(R.id.tvBirthDate);
        tvColor = findViewById(R.id.tvColor);
        tvMicrochip = findViewById(R.id.tvMicrochip);
        tvDireccion = findViewById(R.id.tvDireccion);
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

        // Convertir latitud y longitud en dirección
        if (mascota.getLatitud() != 0 && mascota.getLongitud() != 0) {
            String direccion = obtenerDireccion(mascota.getLatitud(), mascota.getLongitud());
            mostrarUbicacionEnMapa(mascota.getLatitud(), mascota.getLongitud());
            tvDireccion.setText(direccion);
        } else {
            tvDireccion.setText("Ubicación no disponible");
        }
    }

    private String obtenerDireccion(double latitud, double longitud) {
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        try {
            List<Address> direcciones = geocoder.getFromLocation(latitud, longitud, 1);
            if (direcciones != null && !direcciones.isEmpty()) {
                Address direccion = direcciones.get(0);
                return direccion.getAddressLine(0);

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "Dirección no disponible";
    }

    private void mostrarUbicacionEnMapa(double latitud, double longitud) {
        GeoPoint ubicacion = new GeoPoint(latitud, longitud);
        mapView.getController().setZoom(18.0);
        mapView.getController().setCenter(ubicacion);

        // Limpiar marcadores previos
        mapView.getOverlays().clear();

        // Agregar marcador
        Marker marcador = new Marker(mapView);
        marcador.setPosition(ubicacion);
        marcador.setTitle("Ubicación de la mascota");
        marcador.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);
        mapView.getOverlays().add(marcador);

        // Refrescar mapa
        mapView.invalidate();
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
