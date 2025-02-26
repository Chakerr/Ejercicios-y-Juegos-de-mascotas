package co.edu.unipiloto.petsafe;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

import co.edu.unipiloto.petsafe.RegistroMascotas;
import co.edu.unipiloto.petsafe.RegistroUsuarioActivity;

public class LoginActivity extends AppCompatActivity {

    private EditText etEmail, etPassword;
    private Button btnLogin;
    private TextView tvRegistrar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        btnLogin = findViewById(R.id.btnLogin);
        tvRegistrar = findViewById(R.id.tvRegistrar);

        // Navegar a la pantalla de Registro de Usuario si no tiene cuenta
        tvRegistrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, RegistroUsuarioActivity.class);
                startActivity(intent);
            }
        });

        // Iniciar sesión y pasar a Registro de Mascotas
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Aquí puedes validar los datos antes de continuar
                Intent intent = new Intent(LoginActivity.this, RegistroMascotas.class);
                startActivity(intent);
                finish(); // Cierra esta actividad para que no vuelva atrás
            }
        });
    }
}
