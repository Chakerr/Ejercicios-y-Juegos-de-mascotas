package co.edu.unipiloto.petapp;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Calendar;
import java.util.List;

import co.edu.unipiloto.petapp.model.ServicioPaseo;
import co.edu.unipiloto.petapp.model.ServicioPaseoRequestDTO;
import co.edu.unipiloto.petapp.model.TarifaPaseador;
import co.edu.unipiloto.petapp.retrofit.PetApi;
import co.edu.unipiloto.petapp.retrofit.RetrofitService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PreciosPaseador extends AppCompatActivity {

    private RecyclerView recyclerView;
    private TarifaAdapter adapter;
    private PetApi apiService;
    private TarifaPaseador tarifaSeleccionada;

    private int selectedYear, selectedMonth, selectedDay, selectedHour, selectedMinute;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_precios_paseador);

        recyclerView = findViewById(R.id.recyclerTarifas);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new TarifaAdapter(null);
        recyclerView.setAdapter(adapter);

        RetrofitService retrofitService = new RetrofitService();
        apiService = retrofitService.getRetrofit().create(PetApi.class);

        // Intent puede tener o no el ID
        int idPaseador = getIntent().getIntExtra("id_paseador", -1);
        if (idPaseador != -1) {
            obtenerTarifasPorPaseador(idPaseador);
        } else {
            obtenerTodasLasTarifas();
        }

        // Configurar el botón Agendar Servicio
        Button btnAgendarServicio = findViewById(R.id.btnAgendarServicio);
        btnAgendarServicio.setEnabled(false); // Deshabilitado al inicio
        btnAgendarServicio.setOnClickListener(v -> mostrarFechaYHora());

        // Establecer el Listener para cuando una tarifa es seleccionada
        adapter.setOnTarifaClickListener(tarifa -> {
            tarifaSeleccionada = tarifa;
            btnAgendarServicio.setEnabled(true); // Habilitar el botón
        });
    }

    private void obtenerTarifasPorPaseador(int idPaseador) {
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

    private void obtenerTodasLasTarifas() {
        apiService.listarTodasLasTarifas().enqueue(new Callback<List<TarifaPaseador>>() {
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

    private void mostrarFechaYHora() {
        // Obtener la fecha actual
        final Calendar calendar = Calendar.getInstance();
        selectedYear = calendar.get(Calendar.YEAR);
        selectedMonth = calendar.get(Calendar.MONTH);
        selectedDay = calendar.get(Calendar.DAY_OF_MONTH);

        // Mostrar el DatePickerDialog
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                PreciosPaseador.this,
                (view, year, month, dayOfMonth) -> {
                    selectedYear = year;
                    selectedMonth = month;
                    selectedDay = dayOfMonth;
                    mostrarHora();
                },
                selectedYear,
                selectedMonth,
                selectedDay
        );
        datePickerDialog.show();
    }

    private void mostrarHora() {
        // Mostrar el TimePickerDialog después de seleccionar la fecha
        final Calendar calendar = Calendar.getInstance();
        selectedHour = calendar.get(Calendar.HOUR_OF_DAY);
        selectedMinute = calendar.get(Calendar.MINUTE);

        TimePickerDialog timePickerDialog = new TimePickerDialog(
                PreciosPaseador.this,
                (view, hourOfDay, minute) -> {
                    selectedHour = hourOfDay;
                    selectedMinute = minute;
                    // Al seleccionar la hora, agendar el servicio
                    agendarServicio();
                },
                selectedHour,
                selectedMinute,
                true
        );
        timePickerDialog.show();
    }

    private void agendarServicio() {
        if (tarifaSeleccionada != null) {
            // Construir fecha y hora en formato correcto
            String fecha = String.format("%04d-%02d-%02d", selectedYear, selectedMonth + 1, selectedDay);
            String hora = String.format("%02d:%02d:00", selectedHour, selectedMinute);

            // Obtener datos del Intent
            int idDueño = getIntent().getIntExtra("id_dueño", -1);
            int idMascota = getIntent().getIntExtra("id_mascota", -1);
            int idPaseador = tarifaSeleccionada.getIdPaseador(); // Obtenido desde la tarifa seleccionada
            int idTarifa = tarifaSeleccionada.getIdTarifa();

            // Agregar logs para depurar
            Log.d("AGENDAR_SERVICIO", "idDueño: " + idDueño);
            Log.d("AGENDAR_SERVICIO", "idMascota: " + idMascota);
            Log.d("AGENDAR_SERVICIO", "idPaseador: " + idPaseador);
            Log.d("AGENDAR_SERVICIO", "idTarifa: " + idTarifa);
            Log.d("AGENDAR_SERVICIO", "Fecha: " + fecha + ", Hora: " + hora);

            // Validar que los IDs estén presentes
            if (idDueño == -1 || idMascota == -1 || idPaseador == -1) {
                Toast.makeText(this, "Faltan datos para agendar el servicio", Toast.LENGTH_SHORT).show();
                return;
            }

            // Crear el objeto DTO para enviar
            ServicioPaseoRequestDTO nuevoServicio = new ServicioPaseoRequestDTO();
            nuevoServicio.setIdPaseador(idPaseador);
            nuevoServicio.setIdDueño(idDueño);
            nuevoServicio.setIdMascota(idMascota);
            nuevoServicio.setIdTarifa(idTarifa);
            nuevoServicio.setFecha(fecha);
            nuevoServicio.setHora(hora);
            nuevoServicio.setEstadoServicio("pendiente");
            nuevoServicio.setEstadoPaseo("pendiente");

            // Llamada API para crear el servicio
            apiService.crearOActualizarServicio(nuevoServicio).enqueue(new Callback<ServicioPaseo>() {
                @Override
                public void onResponse(Call<ServicioPaseo> call, Response<ServicioPaseo> response) {
                    if (response.isSuccessful()) {
                        Toast.makeText(PreciosPaseador.this,
                                "Servicio agendado correctamente para el " + fecha + " a las " + hora,
                                Toast.LENGTH_LONG).show();

                        Intent resultIntent = new Intent();
                        resultIntent.putExtra("id_tarifa", idTarifa);
                        setResult(RESULT_OK, resultIntent);
                        finish();

                    } else {
                        Toast.makeText(PreciosPaseador.this,
                                "Error al agendar el servicio: " + response.code(),
                                Toast.LENGTH_LONG).show();
                    }
                }

                @Override
                public void onFailure(Call<ServicioPaseo> call, Throwable t) {
                    Toast.makeText(PreciosPaseador.this,
                            "Error de conexión: " + t.getMessage(),
                            Toast.LENGTH_LONG).show();
                }
            });

        } else {
            Toast.makeText(this, "Por favor selecciona una tarifa", Toast.LENGTH_SHORT).show();
        }
    }


}
