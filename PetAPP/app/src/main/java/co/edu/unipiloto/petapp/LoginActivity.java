package co.edu.unipiloto.petapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Patterns;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import co.edu.unipiloto.petapp.retrofit.PetApi;
import co.edu.unipiloto.petapp.retrofit.RetrofitService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    private EditText etEmail, etPassword;
    private PetApi usuarioApi;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        Button btnLogin = findViewById(R.id.btnLogin);
        TextView tvRegistrar = findViewById(R.id.tvRegistrar);

        RetrofitService retrofitService = new RetrofitService();
        usuarioApi = retrofitService.getRetrofit().create(PetApi.class);

        // Inicializar SharedPreferences
        sharedPreferences = getSharedPreferences("UserSession", Context.MODE_PRIVATE);

        // Verificar si ya hay una sesión iniciada
        if (sharedPreferences.getBoolean("isLoggedIn", false)) {
            irARegistroMascotas();
        }

        // Navegar a la pantalla de Registro de Usuario
        tvRegistrar.setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this, RegistroUsuarioActivity.class);
            startActivity(intent);
        });

        // Iniciar sesión
        btnLogin.setOnClickListener(v -> {
            String correo = etEmail.getText().toString().trim();
            String contrasena = etPassword.getText().toString().trim();

            if (correo.isEmpty() || contrasena.isEmpty()) {
                Toast.makeText(LoginActivity.this, "Por favor, ingresa todos los datos.", Toast.LENGTH_SHORT).show();
                return;
            }
            if (!Patterns.EMAIL_ADDRESS.matcher(correo).matches()) {
                Toast.makeText(LoginActivity.this, "Por favor, ingresa un correo electrónico válido.", Toast.LENGTH_SHORT).show();
                return;
            }

            realizarLogin(correo, contrasena);
        });
    }

    private void realizarLogin(String correo, String password) {
        Call<Boolean> call = usuarioApi.login(correo, password);

        call.enqueue(new Callback<Boolean>() {
            @Override
            public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                if (response.isSuccessful() && response.body() != null) {
                    if (response.body()) {
                        obtenerIdYGuardarSesion(correo);
                    } else {
                        Toast.makeText(LoginActivity.this, "Credenciales incorrectas", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(LoginActivity.this, "Error en la respuesta del servidor", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Boolean> call, Throwable t) {
                Toast.makeText(LoginActivity.this, "Error de conexión: " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void obtenerIdYGuardarSesion(String correo) {
        Call<Integer> call = usuarioApi.obtenerIdUsuario(correo);

        call.enqueue(new Callback<Integer>() {
            @Override
            public void onResponse(Call<Integer> call, Response<Integer> response) {
                if (response.isSuccessful() && response.body() != null) {
                    int userId = response.body();
                    guardarSesion(userId);
                    Toast.makeText(LoginActivity.this, "Login exitoso", Toast.LENGTH_SHORT).show();
                    irARegistroMascotas();
                } else {
                    Toast.makeText(LoginActivity.this, "Error al obtener ID de usuario", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Integer> call, Throwable t) {
                Toast.makeText(LoginActivity.this, "Error de conexión al obtener ID", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void guardarSesion(int userId) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt("userId", userId);
        editor.putBoolean("isLoggedIn", true);
        editor.apply();
    }

    private void irARegistroMascotas() {
        Intent intent = new Intent(LoginActivity.this, RegistroMascotas.class);
        startActivity(intent);
        finish();
    }
}
