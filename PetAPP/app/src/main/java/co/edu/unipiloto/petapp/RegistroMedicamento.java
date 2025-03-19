package co.edu.unipiloto.petapp;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import co.edu.unipiloto.petapp.retrofit.PetApi;
import co.edu.unipiloto.petapp.retrofit.RetrofitService;
import co.edu.unipiloto.petapp.model.Mascota;
import co.edu.unipiloto.petapp.model.Medicamento;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

@RequiresApi(api = Build.VERSION_CODES.O)
public class RegistroMedicamento extends AppCompatActivity {

    private Spinner spinnerMascotas;
    private EditText etNombre, etDosis, etFrecuencia, etFechaHora;
    private Button btnSeleccionarFechaHora, btnGuardar;
    private List<Mascota> listaMascotas;

    private static final DateTimeFormatter INPUT_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
    private static final DateTimeFormatter OUTPUT_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro_medicamento);

        spinnerMascotas = findViewById(R.id.spinnerMascotas);
        etNombre = findViewById(R.id.etNombre);
        etDosis = findViewById(R.id.etDosis);
        etFrecuencia = findViewById(R.id.etFrecuencia);
        etFechaHora = findViewById(R.id.etFechaHora);
        btnSeleccionarFechaHora = findViewById(R.id.btnSeleccionarFechaHora);
        btnGuardar = findViewById(R.id.btnGuardar);

        cargarMascotas();
        btnSeleccionarFechaHora.setOnClickListener(v -> seleccionarFechaHora());
        btnGuardar.setOnClickListener(v -> guardarMedicamento());
    }

    private void cargarMascotas() {
        SharedPreferences sharedPreferences = getSharedPreferences("UserSession", Context.MODE_PRIVATE);
        int userId = sharedPreferences.getInt("userId", -1);

        if (userId == -1) {
            Toast.makeText(this, "Error: No se encontró el ID del usuario.", Toast.LENGTH_SHORT).show();
            return;
        }

        RetrofitService retrofitService = new RetrofitService();
        PetApi petApi = retrofitService.getRetrofit().create(PetApi.class);

        Call<List<Mascota>> call = petApi.getMascotasByUsuarioId(userId);
        call.enqueue(new Callback<List<Mascota>>() {
            @Override
            public void onResponse(Call<List<Mascota>> call, Response<List<Mascota>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    listaMascotas = response.body();
                    if (!listaMascotas.isEmpty()) {
                        List<String> nombresMascotas = new ArrayList<>();
                        for (Mascota mascota : listaMascotas) {
                            nombresMascotas.add(mascota.getNombreMascota());
                        }
                        ArrayAdapter<String> adapter = new ArrayAdapter<>(RegistroMedicamento.this, android.R.layout.simple_spinner_item, nombresMascotas);
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        spinnerMascotas.setAdapter(adapter);
                    } else {
                        Toast.makeText(RegistroMedicamento.this, "No tienes mascotas registradas.", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(RegistroMedicamento.this, "Error al obtener mascotas.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Mascota>> call, Throwable t) {
                Toast.makeText(RegistroMedicamento.this, "Error de conexión: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void seleccionarFechaHora() {
        final Calendar calendario = Calendar.getInstance();
        int anio = calendario.get(Calendar.YEAR);
        int mes = calendario.get(Calendar.MONTH);
        int dia = calendario.get(Calendar.DAY_OF_MONTH);

        new DatePickerDialog(this, (view, year, month, dayOfMonth) -> {
            final int mesReal = month + 1;
            String fechaSeleccionada = String.format("%02d/%02d/%04d", dayOfMonth, mesReal, year);

            int hora = calendario.get(Calendar.HOUR_OF_DAY);
            int minuto = calendario.get(Calendar.MINUTE);

            new TimePickerDialog(this, (timeView, hourOfDay, minute) -> {
                String fechaHora = fechaSeleccionada + " " + String.format("%02d:%02d", hourOfDay, minute);
                etFechaHora.setText(fechaHora);
            }, hora, minuto, true).show();

        }, anio, mes, dia).show();
    }

    private void guardarMedicamento() {
        if (listaMascotas == null || listaMascotas.isEmpty()) {
            Toast.makeText(this, "No hay mascotas disponibles para registrar un medicamento.", Toast.LENGTH_SHORT).show();
            return;
        }

        String nombreMedicamento = etNombre.getText().toString().trim();
        String dosis = etDosis.getText().toString().trim();
        String frecuencia = etFrecuencia.getText().toString().trim();
        String fechaHora = etFechaHora.getText().toString().trim();
        int posicionSeleccionada = spinnerMascotas.getSelectedItemPosition();

        if (nombreMedicamento.isEmpty() || dosis.isEmpty() || frecuencia.isEmpty() || fechaHora.isEmpty()) {
            Toast.makeText(this, "Todos los campos son obligatorios", Toast.LENGTH_SHORT).show();
            return;
        }

        Mascota mascotaSeleccionada = listaMascotas.get(posicionSeleccionada);

        try {
            LocalDateTime fechaHoraLocalDateTime = LocalDateTime.parse(fechaHora, INPUT_FORMATTER);
            String fechaHoraFormateada = fechaHoraLocalDateTime.format(OUTPUT_FORMATTER);

            Medicamento medicamento = new Medicamento();
            medicamento.setNombre(nombreMedicamento);
            medicamento.setDosis(dosis);
            medicamento.setFrecuencia(frecuencia);
            medicamento.setProximaDosis(fechaHoraFormateada);
            medicamento.setAdministrado(false);  // Agregar campo faltante
            medicamento.setMascota(mascotaSeleccionada);

            RetrofitService retrofitService = new RetrofitService();
            PetApi petApi = retrofitService.getRetrofit().create(PetApi.class);

            Call<Medicamento> call = petApi.guardarMedicamento(medicamento);
            call.enqueue(new Callback<Medicamento>() {
                @Override
                public void onResponse(Call<Medicamento> call, Response<Medicamento> response) {
                    if (response.isSuccessful()) {
                        Toast.makeText(RegistroMedicamento.this, "Medicamento registrado con éxito.", Toast.LENGTH_SHORT).show();
                        limpiarCampos();
                    } else {
                        Toast.makeText(RegistroMedicamento.this, "Error al registrar el medicamento.", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<Medicamento> call, Throwable t) {
                    Toast.makeText(RegistroMedicamento.this, "Error de conexión: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });

        } catch (Exception e) {
            Toast.makeText(this, "Error al formatear la fecha.", Toast.LENGTH_SHORT).show();
        }
    }


    private void limpiarCampos() {
        etNombre.setText("");
        etDosis.setText("");
        etFrecuencia.setText("");
        etFechaHora.setText("");
    }
}
