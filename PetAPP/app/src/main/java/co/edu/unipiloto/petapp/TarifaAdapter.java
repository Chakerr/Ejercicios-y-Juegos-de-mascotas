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

    public TarifaAdapter(List<TarifaPaseador> tarifas) {
        this.tarifas = tarifas;
    }

    @NonNull
    @Override
    public TarifaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_tarifa, parent, false);
        return new TarifaViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TarifaViewHolder holder, int position) {
        TarifaPaseador tarifa = tarifas.get(position);
        holder.precio.setText("Precio: $" + tarifa.getPrecio());
        holder.distancia.setText("Distancia: " + tarifa.getDistanciaKm() + " km");
    }

    @Override
    public int getItemCount() {
        return tarifas != null ? tarifas.size() : 0;
    }

    public void setTarifas(List<TarifaPaseador> tarifas) {
        this.tarifas = tarifas;
        notifyDataSetChanged();
    }

    public static class TarifaViewHolder extends RecyclerView.ViewHolder {
        TextView precio, distancia;

        public TarifaViewHolder(@NonNull View itemView) {
            super(itemView);
            precio = itemView.findViewById(R.id.tvPrecio);
            distancia = itemView.findViewById(R.id.tvDistancia);
        }
    }
}

