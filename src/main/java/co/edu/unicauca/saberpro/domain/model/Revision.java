package co.edu.unicauca.saberpro.domain.model;

import co.edu.unicauca.saberpro.domain.exception.ReglaNegocioException;

import java.util.Objects;

/**
 * Entidad del contexto Revisión por Pares: evaluación que un revisor hace de una pregunta.
 * <p>
 * Referencia a la pregunta solo por su identificador para no acoplar este contexto al agregado de Autoría.
 * Invariantes: el dictamen se emite una sola vez y un rechazo siempre lleva observaciones.
 */
public class Revision {

    private final Long id;
    private final Long idPregunta;
    private final Revisor revisor;
    private Dictamen dictamen = Dictamen.PENDIENTE;
    private String observaciones;

    /**
     * Crea una revisión pendiente.
     *
     * @param id         identificador de la evaluación
     * @param idPregunta identificador de la pregunta evaluada
     * @param revisor    revisor asignado
     */
    public Revision(Long id, Long idPregunta, Revisor revisor) {
        if (id == null) {
            throw new ReglaNegocioException("La revisión debe tener identificador.");
        }
        if (idPregunta == null) {
            throw new ReglaNegocioException("La revisión debe referenciar una pregunta.");
        }
        if (revisor == null) {
            throw new ReglaNegocioException("La revisión debe tener un revisor asignado.");
        }
        this.id = id;
        this.idPregunta = idPregunta;
        this.revisor = revisor;
    }

    /**
     * Aprueba la pregunta.
     *
     * @param comentario observaciones opcionales
     */
    public void aprobar(String comentario) {
        emitir(Dictamen.APROBADA, comentario);
    }

    /**
     * Rechaza la pregunta. Las observaciones son obligatorias para que el autor sepa qué corregir.
     *
     * @param comentario observaciones para el autor
     */
    public void rechazar(String comentario) {
        if (comentario == null || comentario.isBlank()) {
            throw new ReglaNegocioException("Un rechazo debe incluir observaciones para el autor.");
        }
        emitir(Dictamen.RECHAZADA, comentario);
    }

    private void emitir(Dictamen resultado, String comentario) {
        if (dictamen != Dictamen.PENDIENTE) {
            throw new ReglaNegocioException("La revisión ya tiene dictamen: " + dictamen + ".");
        }
        this.dictamen = resultado;
        this.observaciones = comentario == null ? null : comentario.trim();
    }

    /**
     * Indica si la revisión ya tiene un resultado.
     *
     * @return {@code true} si el dictamen es APROBADA o RECHAZADA
     */
    public boolean tieneDictamen() {
        return dictamen != Dictamen.PENDIENTE;
    }

    public Long getId() {
        return id;
    }

    public Long getIdPregunta() {
        return idPregunta;
    }

    public Revisor getRevisor() {
        return revisor;
    }

    public Dictamen getDictamen() {
        return dictamen;
    }

    public String getObservaciones() {
        return observaciones;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Revision otra)) {
            return false;
        }
        return id.equals(otra.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
