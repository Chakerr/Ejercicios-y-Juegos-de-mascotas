package co.edu.unipiloto.petsafe;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main); // Asignar el layout principal

        // Inicializar y abrir la base de datos

        Database database = new Database(this);
        SQLiteDatabase db = database.getWritableDatabase(); // Esto crea/abre la base de datos

        /* Cursor cursor = db.rawQuery("SELECT name FROM sqlite_master WHERE type='table' AND name='Dueno'", null);
        if (cursor.getCount() == 0) {
            Log.e("DatabaseError", "La tabla 'Dueno' no fue creada.");
        } else {
            Log.d("Database", "Tabla 'Dueno' encontrada.");
        }
        cursor.close();*/
        db.close();

        // Referencias a los botones
        Button btnIngresar = findViewById(R.id.btnIngresar);
        Button btnRegistrar = findViewById(R.id.btnRegistrar);

        // Botón para ir a la pantalla de inicio de sesión
        btnIngresar.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(intent);
        });

        // Botón para ir a la pantalla de registro de usuario
        btnRegistrar.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, RegistroUsuarioActivity.class);
            startActivity(intent);
        });
    }
}
