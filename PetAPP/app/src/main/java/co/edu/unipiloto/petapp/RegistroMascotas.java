package co.edu.unipiloto.petapp;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

import co.edu.unipiloto.petapp.model.Usuario;
import co.edu.unipiloto.petapp.retrofit.RetrofitService;
import co.edu.unipiloto.petapp.retrofit.PetApi;
import co.edu.unipiloto.petapp.model.Mascota;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
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

        sharedPreferences = getSharedPreferences("UserSession", Context.MODE_PRIVATE);
        if (!sharedPreferences.getBoolean("isLoggedIn", false)) {
            Intent intent = new Intent(RegistroMascotas.this, LoginActivity.class);
            startActivity(intent);
            finish();
            return;
        }

        String emailUsuario = sharedPreferences.getString("email", "Usuario desconocido");
        int userId = sharedPreferences.getInt("userId", -1);


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

        etFechaNacimiento.setOnClickListener(v -> mostrarDatePicker());
        inicializarListas();

        switchMicrochip.setOnCheckedChangeListener((buttonView, isChecked) ->
                tvMicrochipEstado.setText(isChecked ? "Sí" : "No"));

        btnRegistrarMascota.setOnClickListener(v -> enviarDatos(userId,emailUsuario));
    }

    private void inicializarListas() {
        String[] especies = {"Perro", "Gato", "Otro"};
        spinnerEspecie.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, especies));

        razasPorEspecie.put("Perro", new String[]{"Labrador", "Bulldog", "Pastor Alemán", "Otro"});
        razasPorEspecie.put("Gato", new String[]{"Siames", "Persa", "Bengalí", "Otro"});
        razasPorEspecie.put("Otro", new String[]{"Desconocido"});

        actualizarRazas("Perro");
        spinnerEspecie.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                actualizarRazas(parent.getItemAtPosition(position).toString());
            }
            @Override public void onNothingSelected(AdapterView<?> parent) {}
        });

        String[] colores = {"Negro", "Blanco", "Marrón", "Gris", "Dorado", "Otro"};
        spinnerColor.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, colores));
    }

    private void actualizarRazas(String especie) {
        spinnerRaza.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, razasPorEspecie.get(especie)));
    }

    private void mostrarDatePicker() {
        int anio = calendario.get(Calendar.YEAR);
        int mes = calendario.get(Calendar.MONTH);
        int dia = calendario.get(Calendar.DAY_OF_MONTH);

        new DatePickerDialog(this, (view, year, month, dayOfMonth) ->
                etFechaNacimiento.setText(year + "-" + String.format("%02d", (month + 1)) + "-" + String.format("%02d", dayOfMonth)),
                anio, mes, dia).show();
    }


    private void enviarDatos(int userId, String emailUsuario) {
        if (userId == -1) {
            Toast.makeText(this, "Error: Usuario no identificado.", Toast.LENGTH_SHORT).show();
            return;
        }

        RetrofitService retrofitService = new RetrofitService();
        PetApi petApi = retrofitService.getRetrofit().create(PetApi.class);

        Usuario usuario = new Usuario();
        usuario.setIdUsuario(userId);

        String nombreMascota = etNombreMascota.getText().toString().trim();
        String fechaNacimiento = etFechaNacimiento.getText().toString().trim(); // Ahora es un String

        if (fechaNacimiento.isEmpty()) {
            Toast.makeText(this, "Debe ingresar la fecha de nacimiento", Toast.LENGTH_SHORT).show();
            return;
        }

        String especie = spinnerEspecie.getSelectedItem().toString();
        String raza = spinnerRaza.getSelectedItem().toString();
        String color = spinnerColor.getSelectedItem().toString();
        boolean microchip = switchMicrochip.isChecked();

        int selectedSexoId = rgSexo.getCheckedRadioButtonId();
        if (selectedSexoId == -1) {
            Toast.makeText(this, "Seleccione un sexo para la mascota", Toast.LENGTH_SHORT).show();
            return;
        }
        String sexo = selectedSexoId == R.id.rbMacho ? "Macho" : "Hembra";

        // Crea la mascota con fecha como String
        Mascota mascota = new Mascota(usuario, nombreMascota, fechaNacimiento, especie, raza, sexo, color, microchip);

        petApi.saveMascota(mascota).enqueue(new Callback<Mascota>() {
            @Override
            public void onResponse(Call<Mascota> call, Response<Mascota> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Toast.makeText(RegistroMascotas.this, "Mascota registrada exitosamente", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Toast.makeText(RegistroMascotas.this, "Error al registrar la mascota", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Mascota> call, Throwable t) {
                Toast.makeText(RegistroMascotas.this, "Error de conexión", Toast.LENGTH_SHORT).show();
            }
        });
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
