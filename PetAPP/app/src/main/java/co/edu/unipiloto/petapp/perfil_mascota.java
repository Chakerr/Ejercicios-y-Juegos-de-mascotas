package co.edu.unipiloto.petapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.*;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.gson.Gson;
import com.journeyapps.barcodescanner.BarcodeEncoder;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import co.edu.unipiloto.petapp.model.Mascota;
import co.edu.unipiloto.petapp.retrofit.PetApi;
import co.edu.unipiloto.petapp.retrofit.RetrofitService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.location.Address;
import android.location.Geocoder;
import android.preference.PreferenceManager;

import org.osmdroid.config.Configuration;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;

public class perfil_mascota extends AppCompatActivity {

    private Spinner spinnerMascotas;
    private TextView tvPetName, tvSpecies, tvBreed, tvGender, tvBirthDate, tvColor, tvMicrochip, tvDireccion;
    private ImageView qrImageView;
    private PetApi petApi;
    private SharedPreferences sharedPreferences;
    private List<Mascota> listaMascotas = new ArrayList<>();
    private MapView mapView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil_mascota);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        sharedPreferences = getSharedPreferences("UserSession", Context.MODE_PRIVATE);
        int userId = sharedPreferences.getInt("userId", -1);
        if (userId == -1) {
            Toast.makeText(this, "Error: No se encontró el ID del usuario.", Toast.LENGTH_SHORT).show();
            return;
        }

        RetrofitService retrofitService = new RetrofitService();
        petApi = retrofitService.getRetrofit().create(PetApi.class);

        Configuration.getInstance().load(getApplicationContext(), PreferenceManager.getDefaultSharedPreferences(this));
        mapView = findViewById(R.id.mapView);
        mapView.setTileSource(org.osmdroid.tileprovider.tilesource.TileSourceFactory.MAPNIK);
        mapView.setBuiltInZoomControls(true);
        mapView.setMultiTouchControls(true);

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

        obtenerMascotas(userId);
    }

    private void obtenerMascotas(int userId) {
        petApi.getMascotasByUsuarioId(userId).enqueue(new Callback<List<Mascota>>() {
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

        if (mascota.getLatitud() != 0 && mascota.getLongitud() != 0) {
            tvDireccion.setText(obtenerDireccion(mascota.getLatitud(), mascota.getLongitud()));
            mostrarUbicacionEnMapa(mascota.getLatitud(), mascota.getLongitud());
        } else {
            tvDireccion.setText("Ubicación no disponible");
        }
    }

    private String obtenerDireccion(double latitud, double longitud) {
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        try {
            List<Address> direcciones = geocoder.getFromLocation(latitud, longitud, 1);
            if (direcciones != null && !direcciones.isEmpty()) {
                return direcciones.get(0).getAddressLine(0);
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
        mapView.getOverlays().clear();

        Marker marcador = new Marker(mapView);
        marcador.setPosition(ubicacion);
        marcador.setTitle("Ubicación de la mascota");
        marcador.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);
        mapView.getOverlays().add(marcador);
        mapView.invalidate();
    }

    private void generarQR(Mascota mascota) {
        try {
            Gson gson = new Gson();
            String mascotaJson = gson.toJson(mascota);
            Bitmap bitmap = new BarcodeEncoder().encodeBitmap(mascotaJson, BarcodeFormat.QR_CODE, 400, 400);
            qrImageView.setImageBitmap(bitmap);
            qrImageView.setVisibility(View.VISIBLE);
        } catch (WriterException e) {
            Log.e("QR_ERROR", "Error al generar QR: " + e.getMessage());
            Toast.makeText(this, "Error al generar QR", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        int selectedPosition = spinnerMascotas.getSelectedItemPosition();

        if (selectedPosition < 0 || listaMascotas.isEmpty()) {
            Toast.makeText(this, "No hay mascota seleccionada", Toast.LENGTH_SHORT).show();
            return true;
        }

        Mascota mascotaSeleccionada = listaMascotas.get(selectedPosition);

        if (id == R.id.action_medicamentos) {
            Intent intent = new Intent(this, AdministrarMedicamentos.class);
            intent.putExtra("mascotaId", mascotaSeleccionada.getIdMascota());
            startActivity(intent);
            return true;
        } else if (id == R.id.action_ruta) {
            Intent intent = new Intent(this, RutaMascotas.class);
            intent.putExtra("latitud_mascota", mascotaSeleccionada.getLatitud());
            intent.putExtra("longitud_mascota", mascotaSeleccionada.getLongitud());
            startActivity(intent);
            return true;
        } else if (id == R.id.action_qr) {
            generarQR(mascotaSeleccionada);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_perfil_mascota, menu);
        return true;
    }
}
