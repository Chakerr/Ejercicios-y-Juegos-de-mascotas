package co.edu.unipiloto.petapp;

import android.os.Bundle;
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

public class RegistroMedicamento extends AppCompatActivity {

    private EditText etNombre, etDosis, etFrecuencia, etFechaHora;
    private Button btnGuardar;
    private PetApi petApi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro_medicamento);

        // Inicializar Retrofit
        RetrofitService retrofitService = new RetrofitService();
        petApi = retrofitService.getRetrofit().create(PetApi.class);

        // Referencias a los elementos del layout
        etNombre = findViewById(R.id.etNombre);
        etDosis = findViewById(R.id.etDosis);
        etFrecuencia = findViewById(R.id.etFrecuencia);
        etFechaHora = findViewById(R.id.etFechaHora);
        btnGuardar = findViewById(R.id.btnGuardar);

        // Evento del botón guardar
        btnGuardar.setOnClickListener(v -> guardarMedicamento());
    }

    private void guardarMedicamento() {
        // Obtener valores de los campos
        String nombre = etNombre.getText().toString();
        String dosis = etDosis.getText().toString();
        String frecuencia = etFrecuencia.getText().toString();
        String fechaHoraTexto = etFechaHora.getText().toString();

        // Validar que los campos no estén vacíos
        if (nombre.isEmpty() || dosis.isEmpty() || frecuencia.isEmpty() || fechaHoraTexto.isEmpty()) {
            Toast.makeText(this, "Todos los campos son obligatorios", Toast.LENGTH_SHORT).show();
            return;
        }

        // Convertir la fecha ingresada en el campo a un objeto Date
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault());
        Date fechaHora = null;
        try {
            fechaHora = sdf.parse(fechaHoraTexto);
        } catch (ParseException e) {
            e.printStackTrace();
            Toast.makeText(this, "Formato de fecha incorrecto. Usa: yyyy-MM-dd HH:mm", Toast.LENGTH_SHORT).show();
            return;
        }

        // Crear objeto Medicamento
        Medicamento medicamento = new Medicamento(null, nombre, dosis, frecuencia, false, fechaHora);

        // Llamar a la API para guardar el medicamento
        petApi.guardarMedicamento(medicamento).enqueue(new Callback<Medicamento>() {
            @Override
            public void onResponse(Call<Medicamento> call, Response<Medicamento> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(RegistroMedicamento.this, "Medicamento guardado correctamente", Toast.LENGTH_SHORT).show();
                    finish(); // Cierra la actividad después de guardar
                } else {
                    Toast.makeText(RegistroMedicamento.this, "Error al guardar el medicamento", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Medicamento> call, Throwable t) {
                Toast.makeText(RegistroMedicamento.this, "Error de conexión: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
