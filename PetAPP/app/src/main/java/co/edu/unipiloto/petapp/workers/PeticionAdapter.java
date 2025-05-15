package co.edu.unipiloto.petapp.workers;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import co.edu.unipiloto.petapp.Peticiones;
import co.edu.unipiloto.petapp.R;
import co.edu.unipiloto.petapp.model.ServicioPaseo;
import co.edu.unipiloto.petapp.model.ServicioPaseoRequestDTO;
import co.edu.unipiloto.petapp.retrofit.PetApi;
import co.edu.unipiloto.petapp.retrofit.RetrofitService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PeticionAdapter extends RecyclerView.Adapter<PeticionAdapter.PeticionViewHolder> {

    private List<ServicioPaseo> lista;
    private Context context;
    private PetApi apiService;

    public PeticionAdapter(List<ServicioPaseo> lista, Context context) {
        this.lista = lista;
        this.context = context;
        this.apiService = new RetrofitService().getRetrofit().create(PetApi.class);
    }

    @NonNull
    @Override
    public PeticionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_peticion, parent, false);
        return new PeticionViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PeticionViewHolder holder, int position) {
        ServicioPaseo servicio = lista.get(position);
        String direccion = Peticiones.obtenerDireccionDesdeCoordenadas(context,
                servicio.getMascota().getLatitud(), servicio.getMascota().getLongitud());

        String info = "Mascota: " + servicio.getMascota().getNombreMascota() + "\n"
                + "Dueño: " + servicio.getDueño().getNombreCompleto() + "\n"
                + "Correo: " + servicio.getDueño().getCorreo() + "\n"
                + "Teléfono: " + servicio.getDueño().getTelefono() + "\n"
                + "Fecha: " + servicio.getFecha() + " " + servicio.getHora() + "\n"
                + "Dirección: " + direccion;

        holder.textMascota.setText(info);
        holder.textEstado.setText("Estado: " + servicio.getEstadoServicio());

        holder.btnAceptar.setOnClickListener(v -> enviarEstado(servicio, "aceptado", position));
        holder.btnRechazar.setOnClickListener(v -> enviarEstado(servicio, "rechazado", position));
    }

    private void enviarEstado(ServicioPaseo servicio, String nuevoEstado, int position) {
        // Construimos un DTO solo con los estados que queremos actualizar (puedes dejar los demás null)
        ServicioPaseoRequestDTO request = new ServicioPaseoRequestDTO();
        request.setEstadoServicio(nuevoEstado);
        // Opcional: mantener estadoPaseo actual o cambiarlo también
        request.setEstadoPaseo(servicio.getEstadoPaseo());

        apiService.actualizarEstadoServicio(servicio.getId(), request)
                .enqueue(new Callback<ServicioPaseo>() {
                    @Override
                    public void onResponse(Call<ServicioPaseo> call, Response<ServicioPaseo> response) {
                        if (response.isSuccessful()) {
                            Toast.makeText(context, "Estado actualizado a " + nuevoEstado, Toast.LENGTH_SHORT).show();
                            lista.remove(position);
                            notifyItemRemoved(position);
                        } else {
                            Log.e("PeticionAdapter", "Error al actualizar: " + response.code());
                            Toast.makeText(context, "Error al actualizar", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<ServicioPaseo> call, Throwable t) {
                        Log.e("PeticionAdapter", "Fallo de red: ", t);
                        Toast.makeText(context, "Fallo: " + t.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
    }

    @Override
    public int getItemCount() {
        return lista.size();
    }

    public static class PeticionViewHolder extends RecyclerView.ViewHolder {
        TextView textMascota, textEstado;
        Button btnAceptar, btnRechazar;

        public PeticionViewHolder(@NonNull View itemView) {
            super(itemView);
            textMascota = itemView.findViewById(R.id.textMascota);
            textEstado = itemView.findViewById(R.id.textEstado);
            btnAceptar = itemView.findViewById(R.id.btnAceptar);
            btnRechazar = itemView.findViewById(R.id.btnRechazar);
        }
    }
}