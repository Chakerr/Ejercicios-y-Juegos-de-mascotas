package co.edu.unipiloto.petapp;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MotionEvent;
import android.widget.Button;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import org.osmdroid.config.Configuration;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;
import org.osmdroid.views.overlay.Overlay;
import org.osmdroid.views.overlay.Polyline;
import java.util.ArrayList;
import java.util.List;
import co.edu.unipiloto.petapp.model.Punto;
import co.edu.unipiloto.petapp.model.RutaMascota;
import co.edu.unipiloto.petapp.retrofit.PetApi;
import co.edu.unipiloto.petapp.retrofit.RetrofitService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RutaMascotas extends AppCompatActivity {

    private MapView mapView;
    private List<GeoPoint> rutaPuntos = new ArrayList<>();
    private Polyline rutaPolyline;
    private Button btnGuardarRuta, btnEliminarPuntos;
    private PetApi apiService;
    private int userId;
    private RutaMascota rutaExistente; // Para almacenar la ruta del usuario

    private static final GeoPoint PUNTO_POR_DEFECTO = new GeoPoint(4.6046387, -74.1516223);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ruta_mascotas);

        SharedPreferences sharedPreferences = getSharedPreferences("UserSession", MODE_PRIVATE);
        userId = sharedPreferences.getInt("userId", -1);

        if (userId == -1) {
            Toast.makeText(this, "Error: No se encontró el ID del usuario.", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        Configuration.getInstance().load(this, getSharedPreferences("osmdroid", MODE_PRIVATE));

        mapView = findViewById(R.id.mapView);
        mapView.setMultiTouchControls(true);

        double latitudMascota = getIntent().getDoubleExtra("latitud_mascota", 0);
        double longitudMascota = getIntent().getDoubleExtra("longitud_mascota", 0);

        GeoPoint puntoInicial = (latitudMascota != 0 && longitudMascota != 0)
                ? new GeoPoint(latitudMascota, longitudMascota)
                : PUNTO_POR_DEFECTO;

        mapView.getController().setZoom(18.0);
        mapView.getController().setCenter(puntoInicial);

        Marker marcador = new Marker(mapView);
        marcador.setPosition(puntoInicial);
        marcador.setTitle("Ubicación de la mascota");
        marcador.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);
        mapView.getOverlays().add(marcador);

        RetrofitService retrofitService = new RetrofitService();
        apiService = retrofitService.getRetrofit().create(PetApi.class);

        btnGuardarRuta = findViewById(R.id.btnGuardarRuta);
        btnEliminarPuntos = findViewById(R.id.btnEliminarPuntos);

        rutaPuntos.add(puntoInicial);
        rutaPolyline = new Polyline();
        mapView.getOverlayManager().add(rutaPolyline);

        btnGuardarRuta.setOnClickListener(v -> guardarRuta());
        btnEliminarPuntos.setOnClickListener(v -> eliminarPuntos());

        // Cargar la ruta guardada si existe
        cargarRutaGuardada();

        mapView.invalidate();
    }

    private void cargarRutaGuardada() {
        apiService.obtenerRutas(userId).enqueue(new Callback<List<RutaMascota>>() {
            @Override
            public void onResponse(Call<List<RutaMascota>> call, Response<List<RutaMascota>> response) {
                if (response.isSuccessful() && response.body() != null && !response.body().isEmpty()) {
                    rutaExistente = response.body().get(0);
                    rutaPuntos.clear();

                    for (Punto p : rutaExistente.getCoordenadas()) {
                        rutaPuntos.add(new GeoPoint(p.getLatitud(), p.getLongitud()));
                    }

                    actualizarRuta();
                    mapView.getController().setCenter(rutaPuntos.get(0));
                    Toast.makeText(RutaMascotas.this, "Ruta cargada correctamente", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(RutaMascotas.this, "No hay rutas guardadas", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<RutaMascota>> call, Throwable t) {
                Toast.makeText(RutaMascotas.this, "Error al cargar la ruta", Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void actualizarRuta() {
        rutaPolyline.setPoints(rutaPuntos);
        mapView.invalidate();
    }

    private void guardarRuta() {
        if (rutaPuntos.size() <= 1) {
            Toast.makeText(this, "Agrega más puntos antes de guardar.", Toast.LENGTH_SHORT).show();
            return;
        }

        // Convertir GeoPoints a objetos Punto
        List<Punto> coordenadas = new ArrayList<>();
        for (GeoPoint punto : rutaPuntos) {
            coordenadas.add(new Punto(punto.getLatitude(), punto.getLongitude()));
        }

        apiService.obtenerRutas(userId).enqueue(new Callback<List<RutaMascota>>() {
            @Override
            public void onResponse(Call<List<RutaMascota>> call, Response<List<RutaMascota>> response) {
                if (response.isSuccessful() && response.body() != null && !response.body().isEmpty()) {
                    rutaExistente = response.body().get(0);
                    eliminarRuta(() -> {
                        // Guardar la nueva ruta solo después de eliminar la anterior
                        RutaMascota nuevaRuta = new RutaMascota("Ruta Usuario " + userId, userId, coordenadas);
                        apiService.guardarRuta(nuevaRuta).enqueue(new Callback<RutaMascota>() {
                            @Override
                            public void onResponse(Call<RutaMascota> call, Response<RutaMascota> response) {
                                if (response.isSuccessful()) {
                                    Toast.makeText(RutaMascotas.this, "Ruta actualizada con éxito", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(RutaMascotas.this, "Error al guardar la ruta", Toast.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void onFailure(Call<RutaMascota> call, Throwable t) {
                                Toast.makeText(RutaMascotas.this, "Error de conexión: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
                    });
                } else {
                    // Si no hay ruta previa, simplemente guardamos la nueva
                    RutaMascota nuevaRuta = new RutaMascota("Ruta Usuario " + userId, userId, coordenadas);
                    apiService.guardarRuta(nuevaRuta).enqueue(new Callback<RutaMascota>() {
                        @Override
                        public void onResponse(Call<RutaMascota> call, Response<RutaMascota> response) {
                            if (response.isSuccessful()) {
                                Toast.makeText(RutaMascotas.this, "Ruta guardada con éxito", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(RutaMascotas.this, "Error al guardar la ruta", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<RutaMascota> call, Throwable t) {
                            Toast.makeText(RutaMascotas.this, "Error de conexión: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }

            @Override
            public void onFailure(Call<List<RutaMascota>> call, Throwable t) {
                Toast.makeText(RutaMascotas.this, "Error al verificar ruta existente", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void eliminarRuta(Runnable onSuccess) {
        if (rutaExistente != null) {
            apiService.eliminarRuta(rutaExistente.getId()).enqueue(new Callback<Void>() {
                @Override
                public void onResponse(Call<Void> call, Response<Void> response) {
                    if (response.isSuccessful()) {
                        Toast.makeText(RutaMascotas.this, "Ruta eliminada con éxito", Toast.LENGTH_SHORT).show();
                        limpiarRuta(); // Limpiar la ruta en el mapa
                        activarMarcadoDePuntos(); // Permitir que el usuario marque nuevos puntos
                        onSuccess.run();
                    } else {
                        Toast.makeText(RutaMascotas.this, "Error al eliminar la ruta", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<Void> call, Throwable t) {
                    Toast.makeText(RutaMascotas.this, "Error de conexión al eliminar la ruta", Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            limpiarRuta();
            activarMarcadoDePuntos();
            onSuccess.run();
        }
    }

    private void limpiarRuta() {
        rutaPuntos.clear(); // Eliminar todos los puntos
        rutaPolyline.setPoints(new ArrayList<>()); // Limpiar la Polyline
        mapView.getOverlays().clear(); // Limpiar los overlays del mapa
        mapView.invalidate();
    }



    private void activarMarcadoDePuntos() {
        mapView.getOverlays().clear(); // Limpia los overlays anteriores
        rutaPuntos.clear(); // Reinicia la lista de puntos
        actualizarRuta(); // Actualiza el mapa

        // Permitir agregar nuevos puntos tocando el mapa
        mapView.getOverlays().add(new Overlay() {
            @Override
            public boolean onSingleTapConfirmed(MotionEvent e, MapView mapView) {
                GeoPoint nuevoPunto = (GeoPoint) mapView.getProjection().fromPixels((int) e.getX(), (int) e.getY());

                rutaPuntos.add(nuevoPunto);
                actualizarRuta();

                // Agregar marcador en el punto seleccionado
                Marker marcador = new Marker(mapView);
                marcador.setPosition(nuevoPunto);
                marcador.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);
                marcador.setTitle("Nuevo Punto");
                mapView.getOverlays().add(marcador);

                mapView.invalidate();
                return true;
            }
        });

        mapView.invalidate();
    }




    private void eliminarPuntos() {
        if (rutaPuntos.size() > 1) {
            rutaPuntos.subList(1, rutaPuntos.size()).clear();
            actualizarRuta();
            Toast.makeText(this, "Puntos eliminados.", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "No hay puntos adicionales para eliminar.", Toast.LENGTH_SHORT).show();
        }
    }
}
