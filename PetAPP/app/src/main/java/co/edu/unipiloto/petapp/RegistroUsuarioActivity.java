package co.edu.unipiloto.petapp;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import co.edu.unipiloto.petapp.model.Usuario;
import co.edu.unipiloto.petapp.retrofit.RetrofitService;
import co.edu.unipiloto.petapp.retrofit.PetApi;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegistroUsuarioActivity extends AppCompatActivity {

    private TextInputEditText inputEditName, inputEditEmail, inputEditTelefono, inputEditPassword;
    private MaterialButton botonRegistro;
    private PetApi usuarioApi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro_usuario);

        inicializarComponentes();
    }

    private void inicializarComponentes() {
        inputEditName = findViewById(R.id.etNombre);
        inputEditEmail = findViewById(R.id.etEmailRegistro);
        inputEditTelefono = findViewById(R.id.etTelefono);
        inputEditPassword = findViewById(R.id.etPasswordRegistro);
        botonRegistro = findViewById(R.id.btnRegistrarUsuario);

        RetrofitService retrofitService = new RetrofitService();
        usuarioApi = retrofitService.getRetrofit().create(PetApi.class);

        botonRegistro.setOnClickListener(v -> verificarCorreoAntesDeRegistrar());
    }

    private void verificarCorreoAntesDeRegistrar() {
        String correo = inputEditEmail.getText().toString().trim();

        if (correo.isEmpty()) {
            Toast.makeText(this, "Ingrese un correo", Toast.LENGTH_SHORT).show();
            return;
        }

        usuarioApi.verificarCorreo(correo).enqueue(new Callback<Boolean>() {
            @Override
            public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                if (response.isSuccessful() && response.body() != null) {
                    if (response.body()) {
                        Toast.makeText(RegistroUsuarioActivity.this, "Correo ya registrado", Toast.LENGTH_SHORT).show();
                    } else {
                        registrarUsuario();
                    }
                } else {
                    Toast.makeText(RegistroUsuarioActivity.this, "Error en la respuesta del servidor", Toast.LENGTH_SHORT).show();
                    Log.e("Retrofit", "Código de error: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<Boolean> call, Throwable t) {
                Toast.makeText(RegistroUsuarioActivity.this, "Error al verificar el correo", Toast.LENGTH_SHORT).show();
                Log.e("Retrofit", "Fallo en la conexión: " + t.getMessage());
            }
        });
    }

    private void registrarUsuario() {
        String nombre = inputEditName.getText().toString().trim();
        String correo = inputEditEmail.getText().toString().trim();
        String telefono = inputEditTelefono.getText().toString().trim();
        String password = inputEditPassword.getText().toString().trim();

        if (nombre.isEmpty() || correo.isEmpty() || telefono.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Todos los campos son obligatorios", Toast.LENGTH_SHORT).show();
            return;
        }

        Usuario usuario = new Usuario();
        usuario.setNombreCompleto(nombre);
        usuario.setCorreo(correo);
        usuario.setTelefono(telefono);
        usuario.setPassword(password);

        usuarioApi.getSaveUsuario(usuario).enqueue(new Callback<Usuario>() {
            @Override
            public void onResponse(Call<Usuario> call, Response<Usuario> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(RegistroUsuarioActivity.this, "Usuario Registrado", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(RegistroUsuarioActivity.this, "Error al registrar usuario", Toast.LENGTH_SHORT).show();
                    Log.e("Retrofit", "Error al registrar usuario: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<Usuario> call, Throwable t) {
                Toast.makeText(RegistroUsuarioActivity.this, "Error de conexión al registrar", Toast.LENGTH_SHORT).show();
                Log.e("Retrofit", "Fallo en la conexión: " + t.getMessage());
            }
        });
    }
}
