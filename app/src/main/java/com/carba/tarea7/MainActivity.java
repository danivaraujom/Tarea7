package com.carba.tarea7;

import android.os.Bundle;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements NuevaTareaDialogoFragment.OnTareaSavedListener {

    private RecyclerView recyclerView;
    private TareaAdapter tareaAdapter;
    private List<Tarea> listaTareas;
    private FloatingActionButton btnAgregarTarea;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Inicializar la lista de tareas y el adaptador
        listaTareas = new ArrayList<>();
        tareaAdapter = new TareaAdapter(listaTareas, this::showTareaBottomSheet, this::onTareaLongClick);

        // Configurar RecyclerView
        recyclerView = findViewById(R.id.listaTareas);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(tareaAdapter);

        // Configurar el botón flotante para agregar una nueva tarea
        btnAgregarTarea = findViewById(R.id.agregarTarea);
        btnAgregarTarea.setOnClickListener(v -> showNuevaTareaDialog(null)); // Pasar null para crear una nueva tarea
    }

    // Mostrar el cuadro de diálogo para crear o editar una tarea
    private void showNuevaTareaDialog(Tarea tarea) {
        NuevaTareaDialogoFragment nuevaTareaDialogoFragment = new NuevaTareaDialogoFragment();
        if (tarea != null) {
            // Si la tarea no es nula, la tarea es para editar
            nuevaTareaDialogoFragment.setTarea(tarea);
        }
        nuevaTareaDialogoFragment.setOnTareaSavedListener(this);
        nuevaTareaDialogoFragment.show(getSupportFragmentManager(), "NuevaTareaDialogo");
    }

    // Método que se llama cuando se guarda o edita una tarea
    @Override
    public void onTareaSaved(Tarea tarea, boolean isEdit) {
        if (isEdit) {
            // Si es una edición, actualizamos la tarea
            int position = listaTareas.indexOf(tarea);
            listaTareas.set(position, tarea);
            tareaAdapter.notifyItemChanged(position);
            Toast.makeText(this, "Tarea actualizada", Toast.LENGTH_SHORT).show();
        } else {
            // Si es una nueva tarea, la agregamos a la lista
            listaTareas.add(tarea);
            tareaAdapter.notifyItemInserted(listaTareas.size() - 1);
            recyclerView.scrollToPosition(listaTareas.size() - 1);
            Toast.makeText(this, "Tarea guardada", Toast.LENGTH_SHORT).show();
        }
    }

    // Mostrar el BottomSheet con opciones
    private void showTareaBottomSheet(Tarea tarea, int position) {
        TareaEditarDialogo bottomSheetDialog = new TareaEditarDialogo();
        bottomSheetDialog.setTarea(tarea);
        bottomSheetDialog.setOnOptionSelectedListener(new TareaEditarDialogo.OnBottomSheetOptionSelectedListener() {
            @Override
            public void onEditSelected(Tarea tarea) {
                showNuevaTareaDialog(tarea); // Pasar la tarea a editar
            }

            @Override
            public void onDeleteSelected(Tarea tarea) {
                showDeleteConfirmationDialog(tarea, position); // Mostrar confirmación de eliminación
            }

            @Override
            public void onCompleteSelected(Tarea tarea) {
                tarea.setEstado(true); // Cambiar estado a completado
                tareaAdapter.notifyItemChanged(position);
                Toast.makeText(MainActivity.this, "Tarea completada", Toast.LENGTH_SHORT).show();
            }
        });

        bottomSheetDialog.show(getSupportFragmentManager(), "TareaBottomSheet");
    }

    // Mostrar confirmación de eliminación
    private void showDeleteConfirmationDialog(Tarea tarea, int position) {
        new AlertDialog.Builder(this)
                .setTitle("Eliminar tarea")
                .setMessage("¿Estás seguro de que deseas eliminar esta tarea?")
                .setPositiveButton("Eliminar", (dialog, which) -> {
                    if (position >= 0 && position < listaTareas.size()) {
                        listaTareas.remove(position);
                        tareaAdapter.notifyItemRemoved(position);
                        Toast.makeText(this, "Tarea eliminada", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("Cancelar", null)
                .show();
    }

    // Método para manejar clic largo en la tarea
    private void onTareaLongClick(Tarea tarea, int position) {
        // Aquí se puede manejar la lógica para el clic largo si es necesario
    }
}
