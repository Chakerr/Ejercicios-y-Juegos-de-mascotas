package co.edu.unipiloto.petsafe;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import java.util.Arrays;
import java.util.List;

public class RegistroVacunas extends AppCompatActivity {

    private Spinner spinnerVacunas;
    private Button btnRegistrarVacuna;
    private EditText etFechaVacunacion, etPesoMascota;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro_vacunas);

        // Inicializar vistas con los IDs corregidos
        spinnerVacunas = findViewById(R.id.spinnerVacunas);
        btnRegistrarVacuna = findViewById(R.id.btnRegistrarMascota);
        etFechaVacunacion = findViewById(R.id.etFechaVacunacion);

        // Lista de vacunas disponibles
        List<String> vacunas = Arrays.asList("Rabia", "Moquillo", "Parvovirus", "Hepatitis", "Leptospirosis");
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, vacunas);
        spinnerVacunas.setAdapter(adapter);

        // Configurar botón de registro
        btnRegistrarVacuna.setOnClickListener(v -> registrarVacuna());
    }

    private void registrarVacuna() {
        String fecha = etFechaVacunacion.getText().toString();
        String peso = etPesoMascota.getText().toString();
        String vacunaSeleccionada = spinnerVacunas.getSelectedItem().toString();

        if (fecha.isEmpty() || peso.isEmpty()) {
            Toast.makeText(this, "Por favor complete todos los campos", Toast.LENGTH_SHORT).show();
            return;
        }

        Toast.makeText(this, "Vacuna " + vacunaSeleccionada + " registrada con éxito", Toast.LENGTH_SHORT).show();
    }


}
