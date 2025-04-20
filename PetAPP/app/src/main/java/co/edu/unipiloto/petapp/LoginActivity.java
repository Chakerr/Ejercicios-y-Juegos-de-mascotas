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

import co.edu.unipiloto.petapp.model.LoginResponse;
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
            redirigirSegunRol(sharedPreferences.getString("rol", ""));
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
        Call<LoginResponse> call = usuarioApi.login(correo, password);

        call.enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    LoginResponse loginResponse = response.body();

                    if (loginResponse.isSuccess()) {
                        // Guardar sesión con el rol
                        guardarSesion(loginResponse.getUserId(), loginResponse.getRole());
                        // Redirigir según el rol
                        redirigirSegunRol(loginResponse.getRole());
                    } else {
                        Toast.makeText(LoginActivity.this, "Credenciales incorrectas", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(LoginActivity.this, "Error en la respuesta del servidor", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                Toast.makeText(LoginActivity.this, "Error de conexión: " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void guardarSesion(int userId, String rol) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt("userId", userId);
        editor.putString("rol", rol);
        editor.putBoolean("isLoggedIn", true);
        editor.apply();
    }

    private void redirigirSegunRol(String rol) {
        Intent intent;
        if ("Dueño de Mascota".equals(rol)) {
            intent = new Intent(LoginActivity.this, MenuDueño.class);
        } else if ("Paseador de mascota".equals(rol)) {
            intent = new Intent(LoginActivity.this, MenuPaseador.class);
        } else if ("Admin".equals(rol)) {
            intent = new Intent(LoginActivity.this, MenuEstadisticas.class);
        }else {
            Toast.makeText(this, "Rol no reconocido", Toast.LENGTH_SHORT).show();
            return;
        }
        startActivity(intent);
        finish();
    }
}
