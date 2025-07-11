````java
// Declaramos el paquete donde está esta clase de pruebas
package cl.kibernumacademy.service;

// ===== IMPORTACIONES =====

// Importaciones de Hamcrest para hacer las verificaciones más expresivas
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;

// Importaciones de JUnit 5 para hacer pruebas
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assumptions.assumeTrue;
import static org.junit.jupiter.api.Assumptions.assumingThat;

import java.util.List; // Para trabajar con listas
import java.util.stream.Stream; // Para trabajar con flujos de datos

// Importamos anotaciones y clases para pruebas
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

// Importamos la clase que representa una tarea
import cl.kibernumacademy.modelo.Tarea;

// ===== CLASE DE PRUEBAS =====
class GestorTareasTest {

    // Creamos una variable que usaremos para probar nuestro sistema de tareas
    private GestorTareas gestor;

    // Esta función se ejecuta **antes de cada prueba**
    // Sirve para crear un nuevo gestor vacío en cada prueba
    @BeforeEach
    void Inicializar() {
        gestor = new GestorTareas();
    }

    // Esta función se ejecuta **después de cada prueba**
    // Sirve para limpiar las tareas y que cada prueba empiece desde cero
    @AfterEach
    void Limpiar() {
        gestor.limpiarTareas();
    }

    // ===== PRUEBAS =====

    // Prueba para verificar que se agregue una tarea correctamente
    @Test
    void agregarTarea_DeberiaAgregarCorrectamente() {
        gestor.agregarTarea("Estudiar", "Repasar JUnit y  programación");

        // Obtenemos las tareas que están pendientes (no completadas)
        List<Tarea> pendientes = gestor.listarTareasPendientes();

        // Verificamos que haya 1 tarea
        assertEquals(1, pendientes.size());

        // Verificamos que el título sea "Estudiar"
        assertEquals("Estudiar", pendientes.get(0).getTitulo());

        // Verificamos que la descripción contenga la palabra "JUnit"
        assertThat(pendientes.get(0).getDescripcion(), containsString("JUnit"));

        // Verificamos que la tarea no esté marcada como completada
        assertThat(pendientes.get(0).estaCompletada(), is(false));
    }

    // Prueba para verificar que marcar una tarea como completada funcione
    @Test
    void marcarTareaCompletada_DeberiaCambiarEstado() {
        gestor.agregarTarea("Tarea1", "Descripcion1");

        // Marcamos la tarea como completada
        boolean resultado = gestor.marcarTareaComoCompletada("Tarea1");

        // Verificamos que el método devolvió true (es decir, que sí se marcó)
        assertTrue(resultado);

        // Verificamos que ya no haya tareas pendientes
        assertThat(gestor.listarTareasPendientes(), is(empty()));
    }

    // Prueba para verificar que solo se muestren las tareas NO completadas
    @Test
    void listarSoloPendientes_DeberiaMostrarSoloTareasNoCompletadas() {
        gestor.agregarTarea("A", "...");
        gestor.agregarTarea("B", "...");

        // Marcamos una como completada
        gestor.marcarTareaComoCompletada("A");

        // Listamos solo las tareas pendientes
        List<Tarea> pendientes = gestor.listarTareasPendientes();

        // Verificamos que haya solo 1 pendiente
        assertThat(pendientes, hasSize(1));

        // Verificamos que la tarea pendiente sea la "B"
        assertEquals("B", pendientes.get(0).getTitulo());
    }

    // Prueba que se ejecuta SOLO si hay tareas pendientes
    @Test
    void pruebaConAssumeTrue() {
        gestor.agregarTarea("Importante", "Leer documentación");

        // Solo ejecuta la prueba si hay tareas pendientes (sino, se omite)
        assumeTrue(gestor.listarTareasPendientes().size() > 0, "Debe haber tareas pendientes");

        // Verificamos que la primera tarea tenga el título correcto
        assertThat(gestor.listarTareasPendientes().get(0).getTitulo(), equalTo("Importante"));
    }

    // Prueba que ejecuta una parte del código solo si se cumple una condición
    @Test
    void pruebaConAssumingThat() {
        gestor.agregarTarea("Curso", "Terminar módulo");

        // Si hay exactamente 1 tarea pendiente, ejecuta el bloque de prueba
        assumingThat(gestor.listarTareasPendientes().size() == 1, () -> {
            assertEquals("Curso", gestor.listarTareasPendientes().get(0).getTitulo());
        });
    }

    // Proveedor de datos para una prueba parametrizada
    static Stream<String> proveedorTitulosTareas() {
        return Stream.of("Uno", "Dos", "Tres");
    }

    // Prueba parametrizada que se repite con los valores anteriores
    @ParameterizedTest
    @MethodSource("proveedorTitulosTareas")
    void pruebaAgregarMultiplesTareas(String titulo) {
        // Agrega una tarea con el título que se entrega como parámetro
        gestor.agregarTarea(titulo, "Genérica");

        // Verifica que esa tarea se haya agregado correctamente
        List<Tarea> pendientes = gestor.listarTareasPendientes();

        // Verifica que alguna de las tareas tenga el título dado
        assertTrue(pendientes.stream().anyMatch(t -> t.getTitulo().equals(titulo)));
    }

    @Test
    void tarea_DeberiaTenerIdUnico() {
        Tarea t1 = new Tarea("A", "...");
        Tarea t2 = new Tarea("B", "...");
        assertTrue(t2.getId() > t1.getId());
    }
}
