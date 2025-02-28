package co.edu.unipiloto.petsafe;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity {

    private EditText etEmail, etPassword;
    private Database database; // Instancia de la base de datos

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        Button btnLogin = findViewById(R.id.btnLogin);
        TextView tvRegistrar = findViewById(R.id.tvRegistrar);

        database = new Database(this); // Inicializamos la base de datos

        // Navegar a la pantalla de Registro de Usuario si no tiene cuenta
        tvRegistrar.setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this, RegistroUsuarioActivity.class);
            startActivity(intent);
        });

        // Iniciar sesión y validar credenciales
        btnLogin.setOnClickListener(v -> {
            String correo = etEmail.getText().toString().trim();
            String contrasena = etPassword.getText().toString().trim();

            if (correo.isEmpty() || contrasena.isEmpty()) {
                Toast.makeText(LoginActivity.this, "Por favor, ingresa todos los datos.", Toast.LENGTH_SHORT).show();
                return;
            }

            boolean usuarioValido = database.autenticarUsuario(correo, contrasena);

            if (usuarioValido) {
                Toast.makeText(LoginActivity.this, "Inicio de sesión exitoso.", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(LoginActivity.this, RegistroMascotas.class);
                startActivity(intent);
                finish(); // Cierra la actividad de login
            } else {
                Toast.makeText(LoginActivity.this, "Correo o contraseña incorrectos.", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
