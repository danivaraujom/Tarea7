package com.carba.tarea7;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import androidx.fragment.app.DialogFragment;
import java.util.Calendar;

public class NuevaTareaDialogoFragment extends DialogFragment {
    private Spinner spinner;
    private EditText editTextNombreTarea;
    private EditText editTextDescripcion;
    private EditText editTextDate;
    private EditText editTextTime;
    private Button btnCancelar;
    private Button btnGuardar;
    private OnTareaSavedListener listener;
    private Tarea tarea; // Tarea a editar

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater layoutInflater = requireActivity().getLayoutInflater();
        View view = layoutInflater.inflate(R.layout.agregar_dialogo, null);

        // Inicializar los elementos del formulario
        spinner = view.findViewById(R.id.asignaturas);
        editTextNombreTarea = view.findViewById(R.id.nombreTarea);
        editTextDescripcion = view.findViewById(R.id.descripcionTarea);
        editTextDate = view.findViewById(R.id.fechaTarea);
        editTextTime = view.findViewById(R.id.horaTarea);
        btnCancelar = view.findViewById(R.id.btnCancelar);
        btnGuardar = view.findViewById(R.id.btnGuardar);

        // Si la tarea es no nula, es una tarea a editar, por lo que llenamos el formulario
        if (tarea != null) {
            editTextNombreTarea.setText(tarea.getNombre());
            editTextDescripcion.setText(tarea.getDescripcion());
            editTextDate.setText(tarea.getFechaEntrega());
            editTextTime.setText(tarea.getHoraEntrega());
            // Establecer el spinner según el valor de asignatura
            // Suponiendo que hay una forma de obtener el índice correcto en el spinner
        }

        // Mostrar el DatePicker cuando el usuario hace clic en la fecha
        editTextDate.setOnClickListener(v -> showDatePickerDialog());

        // Mostrar el TimePicker cuando el usuario hace clic en la hora
        editTextTime.setOnClickListener(v -> showTimePickerDialog());

        // Configurar el diálogo sin botones de guardar/cancelar
        builder.setView(view)
                .setCancelable(true);

        // Configurar el botón de Cancelar
        btnCancelar.setOnClickListener(v -> dismiss());

        // Configurar el botón de Guardar
        btnGuardar.setOnClickListener(v -> saveTarea());

        return builder.create();
    }

    private void showDatePickerDialog() {
        Calendar calendar = Calendar.getInstance();
        new DatePickerDialog(
                getContext(),
                (view, year, month, dayOfMonth) -> {
                    String date = dayOfMonth + "/" + (month + 1) + "/" + year;
                    editTextDate.setText(date);
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
        ).show();
    }

    private void showTimePickerDialog() {
        Calendar calendar = Calendar.getInstance();
        new TimePickerDialog(getContext(), (view, hourOfDay, minute) -> {
            String time = String.format("%02d:%02d", hourOfDay, minute);
            editTextTime.setText(time);
        }, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), true).show();
    }

    public void saveTarea() {
        String nombre = editTextNombreTarea.getText().toString();
        String descripcion = editTextDescripcion.getText().toString();
        String fecha = editTextDate.getText().toString();
        String hora = editTextTime.getText().toString();
        String asignatura = spinner.getSelectedItem().toString();

        if (nombre.isEmpty() || descripcion.isEmpty() || fecha.isEmpty() || hora.isEmpty()) {
            Toast.makeText(getActivity(), "Por favor complete todos los campos", Toast.LENGTH_SHORT).show();
            return;
        }

        if (tarea == null) {
            // Si tarea es nula, es una nueva tarea
            tarea = new Tarea(nombre, descripcion, fecha, hora, asignatura, false);
            if (listener != null) {
                listener.onTareaSaved(tarea, false); // false para indicar que es nueva
            }
        } else {
            // Si tarea no es nula, es una edición
            tarea.setNombre(nombre);
            tarea.setDescripcion(descripcion);
            tarea.setFechaEntrega(fecha);
            tarea.setHoraEntrega(hora);
            tarea.setNombreAsignatura(asignatura);
            if (listener != null) {
                listener.onTareaSaved(tarea, true); // true para indicar que es una edición
            }
        }

        // Limpiar los campos después de guardar
        editTextNombreTarea.setText("");
        editTextDescripcion.setText("");
        editTextDate.setText("");
        editTextTime.setText("");
        spinner.setSelection(0);

        dismiss();
    }

    public void setOnTareaSavedListener(OnTareaSavedListener listener) {
        this.listener = listener;
    }

    public void setTarea(Tarea tarea) {
        this.tarea = tarea;
    }

    public interface OnTareaSavedListener {
        void onTareaSaved(Tarea tarea, boolean isEdit);
    }
}
