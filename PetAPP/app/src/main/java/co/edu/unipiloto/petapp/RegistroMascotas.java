package co.edu.unipiloto.petapp;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class RegistroMascotas extends AppCompatActivity {

    private TextInputEditText etNombreMascota, etFechaNacimiento;
    private Spinner spinnerEspecie, spinnerRaza, spinnerColor;
    private RadioGroup rgSexo;
    private Switch switchMicrochip;
    private TextView tvMicrochipEstado;
    private MaterialButton btnRegistrarMascota;
    private Calendar calendario;
    private SharedPreferences sharedPreferences;

    private final Map<String, String[]> razasPorEspecie = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.registro_mascotas);

        // Inicializar SharedPreferences
        sharedPreferences = getSharedPreferences("UserSession", Context.MODE_PRIVATE);

        // Verificar si hay una sesión activa
        if (!sharedPreferences.getBoolean("isLoggedIn", false)) {
            // Si no hay sesión activa, redirigir a LoginActivity
            Intent intent = new Intent(RegistroMascotas.this, LoginActivity.class);
            startActivity(intent);
            finish();
            return;
        }

        // Obtener el email del usuario desde SharedPreferences
        String emailUsuario = sharedPreferences.getString("email", "Usuario desconocido");
        int userId = sharedPreferences.getInt("userId", -1);


        // Referencias a los elementos del layout
        etNombreMascota = findViewById(R.id.etNombreMascota);
        etFechaNacimiento = findViewById(R.id.etFechaNacimiento);
        spinnerEspecie = findViewById(R.id.spinnerEspecie);
        spinnerRaza = findViewById(R.id.spinnerRaza);
        spinnerColor = findViewById(R.id.spinnerColor);
        rgSexo = findViewById(R.id.rgSexo);
        switchMicrochip = findViewById(R.id.switchMicrochip);
        tvMicrochipEstado = findViewById(R.id.tvMicrochipEstado);
        btnRegistrarMascota = findViewById(R.id.btnRegistrarMascota);
        calendario = Calendar.getInstance();

        // Configurar DatePickerDialog para seleccionar la fecha
        etFechaNacimiento.setOnClickListener(v -> mostrarDatePicker());

        // Inicializar listas de especies, razas y colores
        inicializarListas();

        // Manejar el cambio de estado del switch de microchip
        switchMicrochip.setOnCheckedChangeListener((buttonView, isChecked) -> {
            tvMicrochipEstado.setText(isChecked ? "Sí" : "No");
        });

        btnRegistrarMascota.setOnClickListener(v -> {
            enviarDatos(userId,emailUsuario);
        });
    }

    private void inicializarListas() {
        String[] especies = {"Perro", "Gato", "Otro"};
        ArrayAdapter<String> adapterEspecie = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, especies);
        spinnerEspecie.setAdapter(adapterEspecie);

        razasPorEspecie.put("Perro", new String[]{"Labrador", "Bulldog", "Pastor Alemán", "Otro"});
        razasPorEspecie.put("Gato", new String[]{"Siames", "Persa", "Bengalí", "Otro"});
        razasPorEspecie.put("Otro", new String[]{"Desconocido"});

        actualizarRazas("Perro");

        spinnerEspecie.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String especieSeleccionada = parent.getItemAtPosition(position).toString();
                actualizarRazas(especieSeleccionada);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        String[] colores = {"Negro", "Blanco", "Marrón", "Gris", "Dorado", "Otro"};
        ArrayAdapter<String> adapterColor = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, colores);
        spinnerColor.setAdapter(adapterColor);
    }

    private void actualizarRazas(String especie) {
        String[] razas = razasPorEspecie.get(especie);
        if (razas != null) {
            ArrayAdapter<String> adapterRaza = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, razas);
            spinnerRaza.setAdapter(adapterRaza);
        }
    }

    private void mostrarDatePicker() {
        int anio = calendario.get(Calendar.YEAR);
        int mes = calendario.get(Calendar.MONTH);
        int dia = calendario.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                (view, year, month, dayOfMonth) -> {
                    String fecha = year + "-" + String.format("%02d", (month + 1)) + "-" + String.format("%02d", dayOfMonth);
                    etFechaNacimiento.setText(fecha);
                }, anio, mes, dia);

        datePickerDialog.show();
    }

    private void enviarDatos(int userId,String emailUsuario) {
        // Aquí puedes usar el email del usuario para enviar los datos al backend
        Toast.makeText(this, "ID user: " + userId, Toast.LENGTH_SHORT).show();
        Toast.makeText(this, "Registrando mascota para: " + emailUsuario, Toast.LENGTH_SHORT).show();
        cerrarSesion();
    }

    private void cerrarSesion() {
        SharedPreferences sharedPreferences = getSharedPreferences("UserSession", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();  // Elimina todos los datos guardados en SharedPreferences
        editor.apply();

        // Redirigir al usuario a la pantalla de login
        Intent intent = new Intent(RegistroMascotas.this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK); // Limpiar la pila de actividades
        startActivity(intent);
        finish();
    }
}
