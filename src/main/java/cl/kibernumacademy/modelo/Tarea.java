package cl.kibernumacademy.modelo;

public class Tarea {
    private static int contadorId = 1; // Contador estático para generar únicos IDs

    private int id;
    private String titulo;
    private String descripcion;
    private boolean completada;

    public Tarea(String titulo, String descripcion) {
        this.id = contadorId++; // Genera id automáticamente
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.completada = false;
    }

    // === Getters ===
    public int getId() {
        return id;
    }

    public String getTitulo() {
        return titulo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public boolean estaCompletada() {
        return completada;
    }

    public void marcarComoCompletada() {
        this.completada = true;
    }
}
