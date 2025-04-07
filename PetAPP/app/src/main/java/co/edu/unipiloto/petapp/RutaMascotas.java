package co.edu.unipiloto.petapp;

import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import org.json.JSONArray;
import org.json.JSONObject;
import org.osmdroid.config.Configuration;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Polyline;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import org.osmdroid.views.overlay.Marker;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import android.preference.PreferenceManager;

public class RutaMascotas extends AppCompatActivity {

    private MapView mapView;
    private GeoPoint puntoInicial;

    private EditText inputAround;
    private int radioBusqueda = 50;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Configuration.getInstance().load(getApplicationContext(), PreferenceManager.getDefaultSharedPreferences(this));
        setContentView(R.layout.activity_ruta_mascotas);

        mapView = findViewById(R.id.mapView);
        mapView.setTileSource(TileSourceFactory.MAPNIK);
        mapView.setBuiltInZoomControls(true);
        mapView.setMultiTouchControls(true);

        double latitudMascota = getIntent().getDoubleExtra("latitud_mascota", 0);
        double longitudMascota = getIntent().getDoubleExtra("longitud_mascota", 0);

        puntoInicial = new GeoPoint(latitudMascota, longitudMascota);

        mapView.getController().setZoom(18.0);
        mapView.getController().setCenter(puntoInicial);

        obtenerYMostrarCaminos(puntoInicial.getLatitude(), puntoInicial.getLongitude());

        inputAround = findViewById(R.id.inputAround);

        Button btnActualizar = findViewById(R.id.btnActualizar);
        btnActualizar.setOnClickListener(v -> {
            try {
                int nuevoValor = Integer.parseInt(inputAround.getText().toString());
                radioBusqueda = (nuevoValor > 0) ? nuevoValor : 50;
            } catch (NumberFormatException e) {
                radioBusqueda = 50;
            }

            obtenerYMostrarCaminos(puntoInicial.getLatitude(), puntoInicial.getLongitude());
        });

    }

    private void obtenerYMostrarCaminos(double lat, double lon) {
        new ObtenerCaminosTask().execute(lat, lon);
    }


    private class ObtenerCaminosTask extends AsyncTask<Double, Void, String> {
        @Override
        protected String doInBackground(Double... coords) {
            double lat = coords[0];
            double lon = coords[1];
            String overpassUrl = "https://overpass-api.de/api/interpreter";
            String query = "[out:json];"
                    + "way(around:" + radioBusqueda + "," + lat + "," + lon + ")[highway];"
                    + "out body;"
                    + ">;out skel qt;";

            try {
                HttpURLConnection connection = (HttpURLConnection) new URL(overpassUrl + "?data=" + query).openConnection();
                connection.setRequestMethod("GET");
                connection.setConnectTimeout(10000);
                connection.setReadTimeout(10000);
                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                StringBuilder result = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) result.append(line);
                return result.toString();
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(String json) {
            if (json == null) {
                Toast.makeText(RutaMascotas.this, "Error al obtener caminos", Toast.LENGTH_SHORT).show();
                return;
            }

            try {
                mapView.getOverlays().clear(); // limpiar mapa
                JSONObject root = new JSONObject(json);
                JSONArray elements = root.getJSONArray("elements");

                // Guardar nodos
                ArrayList<GeoPoint> puntos = new ArrayList<>();
                java.util.Map<Long, GeoPoint> nodos = new java.util.HashMap<>();

                for (int i = 0; i < elements.length(); i++) {
                    JSONObject el = elements.getJSONObject(i);
                    if (el.getString("type").equals("node")) {
                        long id = el.getLong("id");
                        double lat = el.getDouble("lat");
                        double lon = el.getDouble("lon");
                        nodos.put(id, new GeoPoint(lat, lon));
                    }
                }

                for (int i = 0; i < elements.length(); i++) {
                    JSONObject el = elements.getJSONObject(i);
                    if (el.getString("type").equals("way")) {
                        JSONArray nodeRefs = el.getJSONArray("nodes");
                        ArrayList<GeoPoint> wayPoints = new ArrayList<>();

                        for (int j = 0; j < nodeRefs.length(); j++) {
                            long nodeId = nodeRefs.getLong(j);
                            GeoPoint pt = nodos.get(nodeId);
                            if (pt != null) {
                                wayPoints.add(pt);
                            }
                        }

                        if (!wayPoints.isEmpty()) {
                            Polyline polyline = new Polyline();
                            polyline.setPoints(wayPoints);

                            // Obtener tipo de highway
                            String highwayType = el.optJSONObject("tags") != null ? el.getJSONObject("tags").optString("highway", "") : "";

                            switch (highwayType) {
                                case "footway":
                                case "path":
                                    polyline.setColor(Color.GREEN);
                                    break;
                                case "cycleway":
                                    polyline.setColor(Color.BLUE);
                                    break;
                                case "residential":
                                case "service":
                                    polyline.setColor(Color.GRAY);
                                    break;
                                default:
                                    polyline.setColor(Color.DKGRAY);
                                    break;
                            }

                            polyline.setWidth(6f);
                            mapView.getOverlays().add(polyline);
                        }
                    }
                }

                // Agregar marcador de la mascota
                Marker marker = new Marker(mapView);
                marker.setPosition(puntoInicial);
                marker.setTitle("Ubicación de la mascota");
                marker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);
                mapView.getOverlays().add(marker);

                mapView.invalidate();

            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(RutaMascotas.this, "Error al procesar datos", Toast.LENGTH_SHORT).show();
            }
        }
    }
}


