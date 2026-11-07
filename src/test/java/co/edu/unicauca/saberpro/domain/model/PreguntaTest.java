package co.edu.unicauca.saberpro.domain.model;

import co.edu.unicauca.saberpro.domain.exception.ReglaNegocioException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Pruebas de las invariantes de la raíz de agregado {@link Pregunta}.
 */
class PreguntaTest {

    private Pregunta preguntaConContenido() {
        return new Pregunta(new Enunciado("¿Qué patrón separa el dominio de la persistencia?"),
                new Justificacion("El repositorio abstrae el acceso a datos del dominio."));
    }

    private Pregunta preguntaCompleta() {
        Pregunta pregunta = preguntaConContenido();
        pregunta.agregarOpcion("Repositorio", true);
        pregunta.agregarOpcion("Singleton", false);
        pregunta.agregarOpcion("Observador", false);
        pregunta.agregarOpcion("Constructor", false);
        return pregunta;
    }

    @Test
    void unaPreguntaNuevaIniciaEnBorrador() {
        assertEquals(EstadoPregunta.BORRADOR, new Pregunta().getEstado());
    }

    @Test
    void noDebePermitirEnviarARevisionSinCuatroOpciones() {
        Pregunta pregunta = new Pregunta();
        pregunta.agregarOpcion("Opción A", true);
        assertThrows(ReglaNegocioException.class, pregunta::enviarARevision);
    }

    @Test
    void noDebePermitirEnviarARevisionSinOpcionCorrecta() {
        Pregunta pregunta = preguntaConContenido();
        pregunta.agregarOpcion("A", false);
        pregunta.agregarOpcion("B", false);
        pregunta.agregarOpcion("C", false);
        pregunta.agregarOpcion("D", false);
        ReglaNegocioException ex = assertThrows(ReglaNegocioException.class, pregunta::enviarARevision);
        assertEquals("Debe existir exactamente 1 opción correcta.", ex.getMessage());
    }

    @Test
    void noDebePermitirEnviarARevisionConDosOpcionesCorrectas() {
        Pregunta pregunta = preguntaConContenido();
        pregunta.agregarOpcion("A", true);
        pregunta.agregarOpcion("B", true);
        pregunta.agregarOpcion("C", false);
        pregunta.agregarOpcion("D", false);
        assertThrows(ReglaNegocioException.class, pregunta::enviarARevision);
        assertEquals(EstadoPregunta.BORRADOR, pregunta.getEstado());
    }

    @Test
    void noDebePermitirEnviarARevisionSinEnunciadoNiJustificacion() {
        Pregunta pregunta = new Pregunta();
        pregunta.agregarOpcion("A", true);
        pregunta.agregarOpcion("B", false);
        pregunta.agregarOpcion("C", false);
        pregunta.agregarOpcion("D", false);
        assertThrows(ReglaNegocioException.class, pregunta::enviarARevision);
    }

    @Test
    void noDebePermitirUnaQuintaOpcion() {
        Pregunta pregunta = preguntaCompleta();
        assertThrows(ReglaNegocioException.class, () -> pregunta.agregarOpcion("E", false));
    }

    @Test
    void noDebePermitirOpcionesRepetidas() {
        Pregunta pregunta = new Pregunta();
        pregunta.agregarOpcion("Repositorio", true);
        assertThrows(ReglaNegocioException.class, () -> pregunta.agregarOpcion("repositorio", false));
    }

    @Test
    void preguntaCompletaPasaAPendienteDeRevision() {
        Pregunta pregunta = preguntaCompleta();
        pregunta.enviarARevision();
        assertEquals(EstadoPregunta.PENDIENTE_REVISION, pregunta.getEstado());
    }

    @Test
    void soloSePuedenModificarOpcionesEnBorrador() {
        Pregunta pregunta = preguntaCompleta();
        pregunta.enviarARevision();
        ReglaNegocioException ex = assertThrows(ReglaNegocioException.class,
                () -> pregunta.agregarOpcion("Otra", false));
        assertTrue(ex.getMessage().contains("Borrador"));
        assertThrows(ReglaNegocioException.class, pregunta::quitarOpciones);
    }

    @Test
    void noSePuedeCambiarElContenidoFueraDeBorrador() {
        Pregunta pregunta = preguntaCompleta();
        pregunta.enviarARevision();
        assertThrows(ReglaNegocioException.class, () -> pregunta.cambiarEnunciado(new Enunciado("Otro enunciado")));
    }

    @Test
    void lasOpcionesNoSePuedenModificarDesdeAfuera() {
        Pregunta pregunta = preguntaCompleta();
        assertThrows(UnsupportedOperationException.class,
                () -> pregunta.getOpciones().add(new OpcionRespuesta("Intrusa", false)));
    }

    @Test
    void cicloDeVidaCompletoHastaArchivar() {
        Pregunta pregunta = preguntaCompleta();
        pregunta.enviarARevision();
        pregunta.iniciarRevision();
        pregunta.aprobar();
        assertTrue(pregunta.esUtilizableEnSimulacro());
        pregunta.publicar();
        pregunta.archivar();
        assertEquals(EstadoPregunta.ARCHIVADA, pregunta.getEstado());
        assertFalse(pregunta.esUtilizableEnSimulacro());
    }

    @Test
    void preguntaRechazadaVuelveABorradorYPuedeCorregirse() {
        Pregunta pregunta = preguntaCompleta();
        pregunta.enviarARevision();
        pregunta.iniciarRevision();
        pregunta.rechazar();
        assertEquals(EstadoPregunta.BORRADOR, pregunta.getEstado());
        pregunta.quitarOpciones();
        assertEquals(0, pregunta.getOpciones().size());
    }

    @Test
    void noSePuedePublicarSinAprobar() {
        Pregunta pregunta = preguntaCompleta();
        pregunta.enviarARevision();
        assertThrows(ReglaNegocioException.class, pregunta::publicar);
    }

    @Test
    void elIdentificadorSoloSeAsignaUnaVez() {
        Pregunta pregunta = new Pregunta();
        pregunta.asignarId(1L);
        assertThrows(ReglaNegocioException.class, () -> pregunta.asignarId(2L));
    }
}
