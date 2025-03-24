package co.edu.unipiloto.petapp;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import co.edu.unipiloto.petapp.model.HistorialMedico;
import co.edu.unipiloto.petapp.model.Mascota;
import co.edu.unipiloto.petapp.retrofit.PetApi;
import co.edu.unipiloto.petapp.retrofit.RetrofitService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HistorialMedicoActivity extends AppCompatActivity {

    private Spinner spinnerMascotas;
    private TextInputEditText etCirugias, etDesparasitaciones, etEnfermedadesPrevias;
    private TextInputEditText etVacunas, etAlergias, etPeso, etFechaUltimoControl;
    private Switch switchEsterilizado;
    private MaterialButton btnGuardarHistorial;
    private PetApi petApi;
    private List<Mascota> listaMascotas = new ArrayList<>();
    private SharedPreferences sharedPreferences;
    private int userId;
    private HistorialMedico historialActual;
    private boolean esEdicion = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_historial_medico);

        RetrofitService retrofitService = new RetrofitService();
        petApi = retrofitService.getRetrofit().create(PetApi.class);

        sharedPreferences = getSharedPreferences("UserSession", Context.MODE_PRIVATE);
        userId = sharedPreferences.getInt("userId", -1);

        if (userId == -1) {
            Toast.makeText(this, "Error: No se encontró el ID del usuario.", Toast.LENGTH_LONG).show();
            finish();
            return;
        }

        spinnerMascotas = findViewById(R.id.spinnerMascotas);
        etVacunas = findViewById(R.id.etVacunas);
        etAlergias = findViewById(R.id.etAlergias);
        etFechaUltimoControl = findViewById(R.id.etFechaUltimoControl);
        switchEsterilizado = findViewById(R.id.switchEsterilizado);
        btnGuardarHistorial = findViewById(R.id.btnGuardarHistorial);
        etCirugias = findViewById(R.id.etCirugias);
        etDesparasitaciones = findViewById(R.id.etDesparasitaciones);
        etEnfermedadesPrevias = findViewById(R.id.etEnfermedadesPrevias);
        obtenerMascotas();
        etFechaUltimoControl.setOnClickListener(v -> seleccionarFecha());
        btnGuardarHistorial.setOnClickListener(view -> guardarHistorialMedico());
        etVacunas.setOnClickListener(v -> mostrarListaSeleccion(etVacunas, "Vacunas", new String[]{"Rabia", "Parvovirus", "Moquillo", "Hepatitis", "Leptospirosis"}));
        etAlergias.setOnClickListener(v -> mostrarListaSeleccion(etAlergias, "Alergias", new String[]{"Polen", "Ácaros", "Alimentos", "Medicamentos"}));
        etCirugias.setOnClickListener(v -> mostrarListaSeleccion(etCirugias, "Cirugías", new String[]{"Esterilización", "Extracción de tumor", "Fractura", "Oftalmológica"}));
        etDesparasitaciones.setOnClickListener(v -> mostrarListaSeleccion(etDesparasitaciones, "Desparasitaciones", new String[]{"Interna", "Externa", "Mixta"}));
        etEnfermedadesPrevias.setOnClickListener(v -> mostrarListaSeleccion(etEnfermedadesPrevias, "Enfermedades Previas", new String[]{"Gastroenteritis", "Otitis", "Artritis", "Diabetes", "Epilepsia"}));
    }

    private void mostrarListaSeleccion(TextInputEditText campo, String titulo, String[] opciones) {
        boolean[] seleccionados = new boolean[opciones.length];
        List<String> seleccionadosList = new ArrayList<>();

        new AlertDialog.Builder(this)
                .setTitle("Selecciona " + titulo)
                .setMultiChoiceItems(opciones, seleccionados, (dialog, which, isChecked) -> {
                    if (isChecked) {
                        seleccionadosList.add(opciones[which]);
                    } else {
                        seleccionadosList.remove(opciones[which]);
                    }
                })
                .setPositiveButton("Aceptar", (dialog, which) -> {
                    String resultado = TextUtils.join(", ", seleccionadosList);
                    campo.setText(resultado);
                })
                .setNegativeButton("Cancelar", null)
                .show();
    }

    private void obtenerMascotas() {
        petApi.getMascotasByUsuarioId(userId).enqueue(new Callback<List<Mascota>>() {
            @Override
            public void onResponse(Call<List<Mascota>> call, Response<List<Mascota>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    listaMascotas = response.body();
                    configurarSpinner();
                } else {
                    Toast.makeText(HistorialMedicoActivity.this, "Error al obtener mascotas", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Mascota>> call, Throwable t) {
                Log.e("RetrofitError", "Error al obtener mascotas: " + t.getMessage());
                Toast.makeText(HistorialMedicoActivity.this, "No se pudo cargar las mascotas", Toast.LENGTH_SHORT).show();
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

        // Listener para detectar cambios en la selección
        spinnerMascotas.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (!listaMascotas.isEmpty()) {
                    int idMascotaSeleccionada = listaMascotas.get(position).getIdMascota();
                    cargarHistorialMedico(idMascotaSeleccionada);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // No hacer nada
            }
        });

        // Si hay al menos una mascota, cargar su historial al inicio
        if (!listaMascotas.isEmpty()) {
            int idMascotaInicial = listaMascotas.get(0).getIdMascota();
            cargarHistorialMedico(idMascotaInicial);
        }
    }


    private void seleccionarFecha() {
        final Calendar calendario = Calendar.getInstance();
        int anio = calendario.get(Calendar.YEAR);
        int mes = calendario.get(Calendar.MONTH);
        int dia = calendario.get(Calendar.DAY_OF_MONTH);

        new DatePickerDialog(this, (view, year, month, dayOfMonth) -> {
            final int mesReal = month + 1;
            String fechaSeleccionada = String.format("%02d/%02d/%04d", dayOfMonth, mesReal, year);

            // Validación para evitar fechas futuras
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
            try {
                Date fechaIngresada = sdf.parse(fechaSeleccionada);
                if (fechaIngresada.after(new Date())) {
                    Toast.makeText(this, "La fecha no puede ser futura", Toast.LENGTH_SHORT).show();
                } else {
                    etFechaUltimoControl.setText(fechaSeleccionada);
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }

        }, anio, mes, dia).show();
    }

    private void llenarCampos(HistorialMedico historial) {
        etVacunas.setText(historial.getVacunas());
        etAlergias.setText(historial.getAlergias());
        etFechaUltimoControl.setText(historial.getFechaUltimoControl());
        etCirugias.setText(historial.getCirugias());
        etDesparasitaciones.setText(historial.getDesparasitaciones());
        etEnfermedadesPrevias.setText(historial.getEnfermedadesPrevias());
        switchEsterilizado.setChecked(historial.getEsterilizado());
    }

    private void limpiarCampos() {
        etVacunas.setText("");
        etAlergias.setText("");
        etFechaUltimoControl.setText("");
        etCirugias.setText("");
        etDesparasitaciones.setText("");
        etEnfermedadesPrevias.setText("");
        switchEsterilizado.setChecked(false);
    }

    private void cargarHistorialMedico(int idMascota) {
        petApi.getHistorialMedicoPorMascota(idMascota).enqueue(new Callback<HistorialMedico>() {
            @Override
            public void onResponse(Call<HistorialMedico> call, Response<HistorialMedico> response) {
                if (response.isSuccessful() && response.body() != null) {
                    historialActual = response.body();
                    esEdicion = true;
                    llenarCampos(historialActual);
                    btnGuardarHistorial.setText("Actualizar Historial");
                } else {
                    esEdicion = false;
                    historialActual = null;
                    limpiarCampos();
                    btnGuardarHistorial.setText("Guardar Historial");
                }
            }

            @Override
            public void onFailure(Call<HistorialMedico> call, Throwable t) {
                Log.e("RetrofitError", "Error al obtener historial: " + t.getMessage());
                Toast.makeText(HistorialMedicoActivity.this, "No se pudo cargar el historial", Toast.LENGTH_SHORT).show();
            }
        });
    }



    private void guardarHistorialMedico() {
        int selectedPosition = spinnerMascotas.getSelectedItemPosition();
        if (selectedPosition < 0 || listaMascotas.isEmpty()) {
            Toast.makeText(this, "Selecciona una mascota", Toast.LENGTH_SHORT).show();
            return;
        }

        Mascota mascotaSeleccionada = listaMascotas.get(selectedPosition);
        String vacunas = etVacunas.getText().toString().trim();
        String alergias = etAlergias.getText().toString().trim();
        String fechaIngresada = etFechaUltimoControl.getText().toString().trim();
        String cirugias = etCirugias.getText().toString().trim();
        String desparasitaciones = etDesparasitaciones.getText().toString().trim();
        String enfermedadesPrevias = etEnfermedadesPrevias.getText().toString().trim();

        if (vacunas.isEmpty() || alergias.isEmpty() || fechaIngresada.isEmpty() || cirugias.isEmpty() || desparasitaciones.isEmpty() || enfermedadesPrevias.isEmpty()) {
            Toast.makeText(this, "Por favor, complete todos los campos", Toast.LENGTH_SHORT).show();
            return;
        }

        SimpleDateFormat inputFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        SimpleDateFormat outputFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        String fechaFormatoCorrecto;

        try {
            Date date = inputFormat.parse(fechaIngresada);
            fechaFormatoCorrecto = outputFormat.format(date);
        } catch (ParseException e) {
            Toast.makeText(this, "Formato de fecha inválido", Toast.LENGTH_SHORT).show();
            return;
        }

        HistorialMedico historial = new HistorialMedico();
        historial.setCirugias(cirugias);
        historial.setDesparasitaciones(desparasitaciones);
        historial.setEnfermedadesPrevias(enfermedadesPrevias);
        historial.setVacunas(vacunas);
        historial.setAlergias(alergias);
        historial.setFechaUltimoControl(fechaFormatoCorrecto);
        historial.setEsterilizado(switchEsterilizado.isChecked());

        if (esEdicion) {
            actualizarHistorial(historialActual.getIdHistorial(), historial);
        } else {
            guardarNuevoHistorial(mascotaSeleccionada.getIdMascota(), historial);
        }
    }

    private void guardarNuevoHistorial(int idMascota, HistorialMedico historial) {
        petApi.guardarHistorialMedico(idMascota, historial).enqueue(new Callback<HistorialMedico>() {
            @Override
            public void onResponse(Call<HistorialMedico> call, Response<HistorialMedico> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(HistorialMedicoActivity.this, "Historial guardado exitosamente", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Toast.makeText(HistorialMedicoActivity.this, "Error al guardar el historial", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<HistorialMedico> call, Throwable t) {
                Log.e("RetrofitError", "Fallo en la conexión: " + t.getMessage());
                Toast.makeText(HistorialMedicoActivity.this, "Error de conexión", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void actualizarHistorial(int id, HistorialMedico historial) {
        petApi.editarHistorial(id, historial).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(HistorialMedicoActivity.this, "Historial actualizado", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(HistorialMedicoActivity.this, "Error al actualizar", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Log.e("RetrofitError", "Error en la solicitud: " + t.getMessage());
            }
        });

    }


}

