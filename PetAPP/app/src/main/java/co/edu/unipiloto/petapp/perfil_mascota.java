package co.edu.unipiloto.petapp;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.textfield.TextInputEditText;

public class perfil_mascota extends AppCompatActivity {

    private TextInputEditText tvPetName, tvBirthDate, tvSpecies, tvBreed, tvGender, tvColor, tvMicrochip;
    private Button btnGenerarQR;
    private ImageView qrImageView;

    private static final String SERVER_URL = "http://TU_SERVIDOR/api/qrcode"; // Reemplazar con tu URL real

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil_mascota);

        // Referenciar los elementos del layout
        tvPetName = findViewById(R.id.tvPetName);
        tvBirthDate = findViewById(R.id.tvBirthDate);
        tvSpecies = findViewById(R.id.tvSpecies);
        tvBreed = findViewById(R.id.tvBreed);
        tvGender = findViewById(R.id.tvGender);
        tvColor = findViewById(R.id.tvColor);
        tvMicrochip = findViewById(R.id.tvMicrochip);
        qrImageView = findViewById(R.id.qrImageView);
        btnGenerarQR = findViewById(R.id.btnGenerarQR);

        // Acción del botón para generar el QR
        btnGenerarQR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                generarQR();
            }
        });
    }

    private void generarQR() {
        // Construir la información de la mascota en formato JSON o cadena simple
        String petData = "Nombre: " + tvPetName.getText().toString() + "\n" +
                "Nacimiento: " + tvBirthDate.getText().toString() + "\n" +
                "Especie: " + tvSpecies.getText().toString() + "\n" +
                "Raza: " + tvBreed.getText().toString() + "\n" +
                "Sexo: " + tvGender.getText().toString() + "\n" +
                "Color: " + tvColor.getText().toString() + "\n" +
                "Microchip: " + tvMicrochip.getText().toString();

        // Reemplazar espacios por `%20` para URL
        String encodedData = petData.replace(" ", "%20");

        // Crear la petición al servidor
        String url = SERVER_URL + "?text=" + encodedData;
        RequestQueue queue = Volley.newRequestQueue(this);

        // Solicitud de imagen codificada en Base64
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        byte[] decodedString = Base64.decode(response, Base64.DEFAULT);
                        Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                        qrImageView.setImageBitmap(decodedByte);
                        qrImageView.setVisibility(View.VISIBLE); // Mostrar la imagen
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });

        queue.add(stringRequest);
    }
}
