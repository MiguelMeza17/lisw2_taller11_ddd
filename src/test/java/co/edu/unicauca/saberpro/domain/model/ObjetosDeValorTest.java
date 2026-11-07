package co.edu.unicauca.saberpro.domain.model;

import co.edu.unicauca.saberpro.domain.exception.ReglaNegocioException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Pruebas de los objetos de valor: validación al construir, igualdad por valor e inmutabilidad.
 */
class ObjetosDeValorTest {

    @Test
    void justificacionConMenosDeQuinceCaracteresEsRechazada() {
        ReglaNegocioException ex = assertThrows(ReglaNegocioException.class, () -> new Justificacion("Muy corta"));
        assertEquals("La justificación debe contener al menos 15 caracteres.", ex.getMessage());
    }

    @Test
    void justificacionCuentaCaracteresSinEspaciosExtremos() {
        assertThrows(ReglaNegocioException.class, () -> new Justificacion("   corta        "));
        assertDoesNotThrow(() -> new Justificacion("Exactamente 15c"));
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "Obviamente la respuesta es la opción A.",
            "Es evidente que el repositorio desacopla capas.",
            "La correcta es TODAS LAS ANTERIORES sin más.",
            "Es la correcta porque sí y no hay otra razón."
    })
    void justificacionConExpresionesProhibidasEsRechazada(String texto) {
        assertThrows(ReglaNegocioException.class, () -> new Justificacion(texto));
    }

    @Test
    void justificacionValidaConservaElTexto() {
        Justificacion j = new Justificacion("  El repositorio aísla la persistencia.  ");
        assertEquals("El repositorio aísla la persistencia.", j.getTexto());
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {"   "})
    void enunciadoNuloOVacioEsRechazado(String texto) {
        assertThrows(ReglaNegocioException.class, () -> new Enunciado(texto));
    }

    @ParameterizedTest
    @NullAndEmptySource
    void opcionSinTextoEsRechazada(String texto) {
        assertThrows(ReglaNegocioException.class, () -> new OpcionRespuesta(texto, false));
    }

    @Test
    void objetosDeValorSeComparanPorValor() {
        assertEquals(new Enunciado("Texto"), new Enunciado(" Texto "));
        assertEquals(new OpcionRespuesta("A", true), new OpcionRespuesta("A", true));
        assertNotEquals(new OpcionRespuesta("A", true), new OpcionRespuesta("A", false));
        assertEquals(new Justificacion("Una justificación válida."), new Justificacion("Una justificación válida."));
    }

    @Test
    void objetosDeValorSonInmutables() {
        for (Class<?> tipo : new Class<?>[]{Enunciado.class, Justificacion.class, OpcionRespuesta.class}) {
            assertTrue(Modifier.isFinal(tipo.getModifiers()), tipo.getSimpleName() + " debe ser final");
            for (Field campo : tipo.getDeclaredFields()) {
                assertTrue(Modifier.isFinal(campo.getModifiers()),
                        tipo.getSimpleName() + "." + campo.getName() + " debe ser final");
            }
        }
    }
}
