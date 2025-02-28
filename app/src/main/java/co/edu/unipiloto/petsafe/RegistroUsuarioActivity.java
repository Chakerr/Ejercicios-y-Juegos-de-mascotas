package co.edu.unipiloto.petsafe;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.UUID;

public class RegistroUsuarioActivity extends AppCompatActivity {

    private EditText etNombre, etTelefono, etEmail, etPassword;
    private Button btnRegistrar;
    private Database databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro_usuario);

        // Inicializar base de datos
        databaseHelper = new Database(this);

        // Referenciar elementos del layout
        etNombre = findViewById(R.id.etNombre);
        etTelefono = findViewById(R.id.etTelefono);
        etEmail = findViewById(R.id.etEmailRegistro);
        etPassword = findViewById(R.id.etPasswordRegistro);
        btnRegistrar = findViewById(R.id.btnRegistrarUsuario);
        TextView tvIniciarSesion = findViewById(R.id.tvIniciarSesion);

        // Acción del botón de registrar usuario
        btnRegistrar.setOnClickListener(v -> registrarUsuario());

        // Acción para ir a la pantalla de inicio de sesión
        tvIniciarSesion.setOnClickListener(v -> {
            Intent intent = new Intent(RegistroUsuarioActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        });
    }

    private void registrarUsuario() {
        String nombre = etNombre.getText().toString().trim();
        String telefono = etTelefono.getText().toString().trim();
        String email = etEmail.getText().toString().trim();
        String password = etPassword.getText().toString().trim();

        if (nombre.isEmpty() || telefono.isEmpty() || email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Todos los campos son obligatorios", Toast.LENGTH_SHORT).show();
            return;
        }

        SQLiteDatabase db = null;
        Cursor cursor = null;

        try {
            db = databaseHelper.getWritableDatabase();

            // Verificar si el correo ya existe
            cursor = db.rawQuery("SELECT * FROM Dueno WHERE Correo = ?", new String[]{email});
            if (cursor.getCount() > 0) {
                Toast.makeText(this, "El correo ya está registrado", Toast.LENGTH_LONG).show();
                return;
            }

            // Generar un ID único para el dueño
            String idDueno = UUID.randomUUID().toString();

            // Cifrar la contraseña antes de almacenarla
            String passwordHash = hashPassword(password);

            ContentValues valores = new ContentValues();
            valores.put("IDDueno", idDueno);
            valores.put("Nombre", nombre);
            valores.put("Telefono", telefono);
            valores.put("Correo", email);
            valores.put("Contrasena", passwordHash);

            long resultado = db.insert("Dueno", null, valores);

            if (resultado != -1) {
                Toast.makeText(this, "Registro exitoso", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(RegistroUsuarioActivity.this, LoginActivity.class));
                finish();
            } else {
                Toast.makeText(this, "Error en el registro", Toast.LENGTH_LONG).show();
            }

        } catch (Exception e) {
            Toast.makeText(this, "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
        } finally {
            if (cursor != null) cursor.close();
            if (db != null) db.close();
        }
    }

    // Método para cifrar la contraseña con SHA-256
    private String hashPassword(String password) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(password.getBytes());
            StringBuilder hexString = new StringBuilder();
            for (byte b : hash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Error al cifrar la contraseña", e);
        }
    }
}
