package co.edu.unicauca.saberpro.domain.model;

/**
 * Resultado de una revisión por pares.
 */
public enum Dictamen {
    /** La revisión todavía no tiene resultado. */
    PENDIENTE,
    /** La pregunta cumple los criterios de calidad. */
    APROBADA,
    /** La pregunta debe corregirse; requiere observaciones. */
    RECHAZADA
}
