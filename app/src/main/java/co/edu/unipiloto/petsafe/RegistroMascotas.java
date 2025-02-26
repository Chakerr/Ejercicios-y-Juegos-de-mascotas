package co.edu.unipiloto.petsafe;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import java.util.HashMap;
import java.util.Map;

public class RegistroMascotas extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.registro_mascotas);

        // Ajuste de insets para el botón
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.btnRegistrarVacunas), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Inicializar vistas
        Spinner spinnerEspecie = findViewById(R.id.spinnerEspecie);
        Spinner spinnerRaza = findViewById(R.id.spinnerRaza);
        Spinner spinnerColor = findViewById(R.id.spinnerColor);
        Button btnRegistrarVacunas = findViewById(R.id.btnRegistrarVacunas);

        // Lista de especies
        String[] especies = {"Perro", "Gato", "Otro"};
        ArrayAdapter<String> adapterEspecie = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, especies);
        spinnerEspecie.setAdapter(adapterEspecie);

        // Mapa de razas por especie
        Map<String, String[]> razasPorEspecie = new HashMap<>();
        razasPorEspecie.put("Perro", new String[]{"Labrador", "Bulldog", "Pastor Alemán", "Otro"});
        razasPorEspecie.put("Gato", new String[]{"Siames", "Persa", "Bengalí", "Otro"});
        razasPorEspecie.put("Otro", new String[]{"Desconocido"});

        // Lista de colores
        String[] colores = {"Negro", "Blanco", "Marrón", "Gris", "Manchado", "Otro"};
        ArrayAdapter<String> adapterColor = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, colores);
        spinnerColor.setAdapter(adapterColor);

        // Manejar cambios en el spinner de especie para actualizar las razas
        spinnerEspecie.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String especieSeleccionada = especies[position];
                String[] razas = razasPorEspecie.get(especieSeleccionada);
                if (razas != null) {
                    ArrayAdapter<String> adapterRaza = new ArrayAdapter<>(RegistroMascotas.this, android.R.layout.simple_spinner_dropdown_item, razas);
                    spinnerRaza.setAdapter(adapterRaza);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        btnRegistrarVacunas.setOnClickListener(v -> {
            Intent intent = new Intent(RegistroMascotas.this, RegistroVacunas.class);
            startActivity(intent);
        });
    }
}
