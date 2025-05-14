package co.edu.unipiloto.petapp;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
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
            // Aquí puedes manejar el agendamiento del servicio con la tarifa seleccionada, fecha y hora
            String fechaHora = selectedYear + "-" + (selectedMonth + 1) + "-" + selectedDay + " " + selectedHour + ":" + selectedMinute;
            Log.d("Servicio", "Servicio agendado con tarifa: " + tarifaSeleccionada.getPrecio() + " para la fecha: " + fechaHora);

            // Mostrar un mensaje de éxito
            Toast.makeText(this, "Servicio agendado para el " + fechaHora, Toast.LENGTH_SHORT).show();

            // Aquí podrías hacer una llamada a la API para registrar el agendamiento
        } else {
            Toast.makeText(this, "Por favor selecciona una tarifa", Toast.LENGTH_SHORT).show();
        }
    }
}
