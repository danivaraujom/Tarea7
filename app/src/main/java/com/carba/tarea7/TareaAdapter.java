package com.carba.tarea7;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class TareaAdapter extends RecyclerView.Adapter<TareaAdapter.TareaViewHolder> {

    private final List<Tarea> listaTareas;
    private final OnTareaClickListener clickListener;
    private final OnTareaLongClickListener longClickListener;

    public TareaAdapter(List<Tarea> listaTareas, OnTareaClickListener clickListener, OnTareaLongClickListener longClickListener) {
        this.listaTareas = listaTareas;
        this.clickListener = clickListener;
        this.longClickListener = longClickListener;
    }

    @NonNull
    @Override
    public TareaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_tarea, parent, false);
        return new TareaViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TareaViewHolder holder, int position) {
        Tarea tarea = listaTareas.get(position);
        holder.bind(tarea);
        holder.itemView.setOnClickListener(v -> {
            if (clickListener != null) {
                clickListener.onTareaClick(tarea, position);
            }
        });

        holder.itemView.setOnLongClickListener(v -> {
            if (longClickListener != null) {
                longClickListener.onTareaLongClick(tarea, position);
            }
            return true;
        });
    }

    @Override
    public int getItemCount() {
        return listaTareas.size();
    }

    // Interfaz para manejar clics simples
    public interface OnTareaClickListener {
        void onTareaClick(Tarea tarea, int position);
    }

    // Interfaz para manejar clics largos
    public interface OnTareaLongClickListener {
        void onTareaLongClick(Tarea tarea, int position);
    }

    public static class TareaViewHolder extends RecyclerView.ViewHolder {
        private final TextView tvNombreAsignatura;
        private final TextView tvNombreTarea;
        private final TextView tvDescripcionTarea;
        private final TextView tvFechaEntrega;
        private final TextView tvHoraEntrega;
        private final TextView tvEstadoTarea;

        public TareaViewHolder(@NonNull View itemView) {
            super(itemView);
            tvNombreAsignatura= itemView.findViewById(R.id.tv_nombre_asignatura);
            tvNombreTarea = itemView.findViewById(R.id.tv_nombre_tarea);
            tvDescripcionTarea = itemView.findViewById(R.id.tv_descripcion_tarea);
            tvFechaEntrega = itemView.findViewById(R.id.tv_fecha_entrega);
            tvHoraEntrega = itemView.findViewById(R.id.tv_hora_entrega);
            tvEstadoTarea = itemView.findViewById(R.id.tv_estado_tarea);
        }

        public void bind(Tarea tarea) {
            tvNombreAsignatura.setText((tarea.getNombreAsignatura()));
            tvNombreTarea.setText(tarea.getNombre());
            tvDescripcionTarea.setText(tarea.getDescripcion());
            tvFechaEntrega.setText(tarea.getFechaEntrega());
            tvHoraEntrega.setText(tarea.getHoraEntrega());
            tvEstadoTarea.setText(tarea.isEstado() ? "Completado" : "Pendiente");
            tvEstadoTarea.setTextColor(tarea.isEstado() ?
                    itemView.getResources().getColor(android.R.color.holo_green_dark) :
                    itemView.getResources().getColor(android.R.color.holo_red_dark));
        }
    }
}
