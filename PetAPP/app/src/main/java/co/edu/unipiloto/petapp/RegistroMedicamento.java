package co.edu.unipiloto.petapp;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import co.edu.unipiloto.petapp.model.Medicamento;
import co.edu.unipiloto.petapp.retrofit.PetApi;
import co.edu.unipiloto.petapp.retrofit.RetrofitService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import java.util.Calendar;

public class RegistroMedicamento extends AppCompatActivity {

    private EditText etNombre, etDosis, etFrecuencia, etFechaHora;
    private Button btnGuardar, btnSeleccionarFechaHora;
    private PetApi petApi;
    private Calendar calendarioSeleccionado;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro_medicamento);

        // Inicializar Retrofit
        RetrofitService retrofitService = new RetrofitService();
        petApi = retrofitService.getRetrofit().create(PetApi.class);

        // Referencias UI
        etNombre = findViewById(R.id.etNombre);
        etDosis = findViewById(R.id.etDosis);
        etFrecuencia = findViewById(R.id.etFrecuencia);
        etFechaHora = findViewById(R.id.etFechaHora);
        btnGuardar = findViewById(R.id.btnGuardar);
        btnSeleccionarFechaHora = findViewById(R.id.btnSeleccionarFechaHora);

        calendarioSeleccionado = Calendar.getInstance();

        // Evento del botón para seleccionar fecha y hora
        btnSeleccionarFechaHora.setOnClickListener(v -> mostrarDatePicker());

        // Evento del botón guardar
        btnGuardar.setOnClickListener(v -> guardarMedicamento());
    }

    private void guardarMedicamento() {
        String nombre = etNombre.getText().toString().trim();
        String dosis = etDosis.getText().toString().trim();
        String frecuencia = etFrecuencia.getText().toString().trim();
        String fechaHoraTexto = etFechaHora.getText().toString().trim();

        Log.d("RegistroMedicamento", "Fecha ingresada: " + fechaHoraTexto);

        if (nombre.isEmpty() || dosis.isEmpty() || frecuencia.isEmpty() || fechaHoraTexto.isEmpty()) {
            Toast.makeText(this, "Todos los campos son obligatorios", Toast.LENGTH_SHORT).show();
            return;
        }

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault());
        Date fechaHora = null;
        try {
            fechaHora = sdf.parse(fechaHoraTexto);
            Log.d("RegistroMedicamento", "Fecha convertida: " + fechaHora);
        } catch (ParseException e) {
            e.printStackTrace();
            Log.e("RegistroMedicamento", "Error en el formato de la fecha: " + fechaHoraTexto);
            Toast.makeText(this, "Formato de fecha incorrecto. Usa: yyyy-MM-dd'T'HH:mm:ss", Toast.LENGTH_SHORT).show();
            return;
        }

        Medicamento medicamento = new Medicamento(null, nombre, dosis, frecuencia, false, fechaHora);
        Log.d("RegistroMedicamento", "Objeto Medicamento: " + medicamento.toString());

        petApi.guardarMedicamento(medicamento).enqueue(new Callback<Medicamento>() {
            @Override
            public void onResponse(Call<Medicamento> call, Response<Medicamento> response) {
                if (response.isSuccessful()) {
                    Log.d("RegistroMedicamento", "Medicamento guardado exitosamente");
                    Toast.makeText(RegistroMedicamento.this, "Medicamento guardado correctamente", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Log.e("RegistroMedicamento", "Error en la respuesta del servidor");
                    Toast.makeText(RegistroMedicamento.this, "Error al guardar el medicamento", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Medicamento> call, Throwable t) {
                Log.e("RegistroMedicamento", "Error de conexión: " + t.getMessage());
                Toast.makeText(RegistroMedicamento.this, "Error de conexión: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void mostrarDatePicker() {
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this,
                (view, year, month, dayOfMonth) -> {
                    calendarioSeleccionado.set(Calendar.YEAR, year);
                    calendarioSeleccionado.set(Calendar.MONTH, month);
                    calendarioSeleccionado.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                    mostrarTimePicker(); // Después de seleccionar la fecha, mostrar el TimePicker
                },
                calendarioSeleccionado.get(Calendar.YEAR),
                calendarioSeleccionado.get(Calendar.MONTH),
                calendarioSeleccionado.get(Calendar.DAY_OF_MONTH)
        );
        datePickerDialog.show();
    }

    private void mostrarTimePicker() {
        TimePickerDialog timePickerDialog = new TimePickerDialog(
                this,
                (view, hourOfDay, minute) -> {
                    calendarioSeleccionado.set(Calendar.HOUR_OF_DAY, hourOfDay);
                    calendarioSeleccionado.set(Calendar.MINUTE, minute);
                    actualizarCampoFechaHora();
                },
                calendarioSeleccionado.get(Calendar.HOUR_OF_DAY),
                calendarioSeleccionado.get(Calendar.MINUTE),
                true
        );
        timePickerDialog.show();
    }

    private void actualizarCampoFechaHora() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault());
        String fechaHoraFormateada = sdf.format(calendarioSeleccionado.getTime());
        etFechaHora.setText(fechaHoraFormateada);
    }

}
