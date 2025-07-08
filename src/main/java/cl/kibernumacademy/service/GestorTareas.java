// Declaramos el paquete donde se encuentra esta clase
package cl.kibernumacademy.service;

// Importamos clases necesarias
import java.util.ArrayList; // Lista dinámica para almacenar tareas
import java.util.List; // Interfaz de listas
import java.util.Optional; // Para manejar resultados que pueden estar presentes o no 

import java.util.stream.Collectors; // Para recolectar resultados de streams

// Importamos la clase Tarea desde el paquete modelo
import cl.kibernumacademy.modelo.Tarea;

// Esta clase permite gestionar un conjunto de tareas
public class GestorTareas {

    // Lista privada que contiene todas las tareas
    private List<Tarea> tareas = new ArrayList<>();

    // Método público para agregar una nueva tarea
    public void agregarTarea(String titulo, String descripcion) {
        // Crea una nueva tarea con el título y descripción dados, y la agrega a la
        // lista
        tareas.add(new Tarea(titulo, descripcion));
    }

    // Método para marcar una tarea como completada según su título
    public boolean marcarTareaComoCompletada(String titulo) {
        // Busca la primera tarea cuyo título coincida (ignorando mayúsculas/minúsculas)
        Optional<Tarea> tareaOpt = tareas.stream()
                .filter(t -> t.getTitulo().equalsIgnoreCase(titulo))
                .findFirst();

        // Si se encuentra la tarea, se marca como completada y retorna true
        if (tareaOpt.isPresent()) {
            tareaOpt.get().marcarComoCompletada(); // Llama al método de la clase Tarea
            return true;
        }

        // Si no se encuentra ninguna tarea con ese título, retorna false
        return false;
    }

    // Método que devuelve solo las tareas que aún no han sido completadas
    public List<Tarea> listarTareasPendientes() {
        return tareas.stream() // Recorremos la lista como stream
                .filter(t -> !t.estaCompletada()) // Filtramos las tareas que NO están completadas
                .collect(Collectors.toList()); // Recolectamos los resultados en una nuevalista
    }

    // Método que borra todas las tareas de la lista
    public void limpiarTareas() {
        tareas.clear(); // Limpia (vacía) la lista de tareas
    }
}
