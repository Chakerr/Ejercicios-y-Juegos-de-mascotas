package co.edu.unipiloto.petapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import co.edu.unipiloto.petapp.model.TarifaPaseador;

public class TarifaAdapter extends RecyclerView.Adapter<TarifaAdapter.TarifaViewHolder> {

    private List<TarifaPaseador> tarifas;
    private OnTarifaClickListener listener;

    public interface OnTarifaClickListener {
        void onTarifaClick(TarifaPaseador tarifa);
    }

    public TarifaAdapter(List<TarifaPaseador> tarifas) {
        this.tarifas = tarifas;
    }

    public void setOnTarifaClickListener(OnTarifaClickListener listener) {
        this.listener = listener;
    }

    public void setTarifas(List<TarifaPaseador> tarifas) {
        this.tarifas = tarifas;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public TarifaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View vista = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_tarifa, parent, false);
        return new TarifaViewHolder(vista);
    }

    @Override
    public void onBindViewHolder(@NonNull TarifaViewHolder holder, int position) {
        TarifaPaseador tarifa = tarifas.get(position);

        holder.nombre.setText("Nombre: " + tarifa.getNombre());
        holder.correo.setText("Correo: " + tarifa.getCorreo());
        holder.telefono.setText("Teléfono: " + tarifa.getTelefono());
        holder.precio.setText("Precio: $" + tarifa.getPrecio());
        holder.distancia.setText("Distancia: " + tarifa.getDistanciaKm() + " km");

        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onTarifaClick(tarifa);
            }
        });
    }

    @Override
    public int getItemCount() {
        return tarifas != null ? tarifas.size() : 0;
    }

    static class TarifaViewHolder extends RecyclerView.ViewHolder {

        TextView nombre, correo, telefono, precio, distancia;

        public TarifaViewHolder(@NonNull View itemView) {
            super(itemView);
            nombre = itemView.findViewById(R.id.textNombre);
            correo = itemView.findViewById(R.id.textCorreo);
            telefono = itemView.findViewById(R.id.textTelefono);
            precio = itemView.findViewById(R.id.textPrecio);
            distancia = itemView.findViewById(R.id.textDistancia);
        }
    }
}
