package co.edu.unicauca.saberpro.domain.model;

import co.edu.unicauca.saberpro.domain.exception.ReglaNegocioException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Pruebas de las entidades del contexto Revisión por Pares.
 */
class RevisionTest {

    private Revisor revisor;
    private Pregunta pregunta;

    @BeforeEach
    void setUp() {
        revisor = new Revisor(10L, "Docente Revisora");
        pregunta = new Pregunta(new Enunciado("¿Qué es un agregado?"),
                new Justificacion("Un agregado agrupa objetos que cambian juntos."));
        pregunta.agregarOpcion("Un grupo de objetos con una raíz", true);
        pregunta.agregarOpcion("Una tabla", false);
        pregunta.agregarOpcion("Un servicio", false);
        pregunta.agregarOpcion("Un controlador", false);
        pregunta.asignarId(1L);
        pregunta.enviarARevision();
        pregunta.iniciarRevision();
    }

    @Test
    void revisoresConElMismoIdDocenteSonLaMismaEntidad() {
        Revisor mismo = new Revisor(10L, "Otro nombre");
        assertEquals(revisor, mismo);
        assertNotEquals(revisor, new Revisor(11L, "Docente Revisora"));
    }

    @Test
    void dictamenAprobadoApruebaLaPregunta() {
        Revision revision = new Revision(100L, 1L, revisor);
        revision.aprobar("Cumple los criterios.");
        revision.aplicarDictamenA(pregunta);
        assertEquals(EstadoPregunta.APROBADA, pregunta.getEstado());
    }

    @Test
    void dictamenRechazadoDevuelveLaPreguntaABorrador() {
        Revision revision = new Revision(100L, 1L, revisor);
        revision.rechazar("Los distractores no son plausibles.");
        revision.aplicarDictamenA(pregunta);
        assertEquals(EstadoPregunta.BORRADOR, pregunta.getEstado());
        assertEquals("Los distractores no son plausibles.", revision.getObservaciones());
    }

    @Test
    void unRechazoExigeObservaciones() {
        Revision revision = new Revision(100L, 1L, revisor);
        assertThrows(ReglaNegocioException.class, () -> revision.rechazar("  "));
    }

    @Test
    void elDictamenSoloSeEmiteUnaVez() {
        Revision revision = new Revision(100L, 1L, revisor);
        revision.aprobar(null);
        assertThrows(ReglaNegocioException.class, () -> revision.rechazar("Cambio de opinión"));
    }

    @Test
    void noSeAplicaUnDictamenPendiente() {
        Revision revision = new Revision(100L, 1L, revisor);
        assertThrows(ReglaNegocioException.class, () -> revision.aplicarDictamenA(pregunta));
    }

    @Test
    void noSeAplicaElDictamenAOtraPregunta() {
        Revision revision = new Revision(100L, 99L, revisor);
        revision.aprobar(null);
        assertThrows(ReglaNegocioException.class, () -> revision.aplicarDictamenA(pregunta));
    }

    @Test
    void laRevisionRequiereRevisor() {
        assertThrows(ReglaNegocioException.class, () -> new Revision(100L, 1L, null));
    }
}
