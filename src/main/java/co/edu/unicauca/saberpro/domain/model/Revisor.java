package co.edu.unicauca.saberpro.domain.model;

import co.edu.unicauca.saberpro.domain.exception.ReglaNegocioException;

import java.util.Objects;

/**
 * Entidad del contexto Revisión por Pares: docente que evalúa preguntas.
 * Su identidad es el identificador del docente; dos revisores son el mismo si comparten ese identificador.
 */
public class Revisor {

    private final Long idDocente;
    private String nombre;

    /**
     * Crea un revisor.
     *
     * @param idDocente identificador del docente
     * @param nombre    nombre del docente
     */
    public Revisor(Long idDocente, String nombre) {
        if (idDocente == null) {
            throw new ReglaNegocioException("El revisor debe tener identificador de docente.");
        }
        this.idDocente = idDocente;
        cambiarNombre(nombre);
    }

    /**
     * Cambia el nombre sin alterar la identidad.
     *
     * @param nuevoNombre nuevo nombre
     */
    public void cambiarNombre(String nuevoNombre) {
        if (nuevoNombre == null || nuevoNombre.isBlank()) {
            throw new ReglaNegocioException("El nombre del revisor es obligatorio.");
        }
        this.nombre = nuevoNombre.trim();
    }

    public Long getIdDocente() {
        return idDocente;
    }

    public String getNombre() {
        return nombre;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Revisor otro)) {
            return false;
        }
        return idDocente.equals(otro.idDocente);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idDocente);
    }
}
