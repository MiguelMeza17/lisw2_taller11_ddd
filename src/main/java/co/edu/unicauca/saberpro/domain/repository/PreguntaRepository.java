package co.edu.unicauca.saberpro.domain.repository;

import co.edu.unicauca.saberpro.domain.model.EstadoPregunta;
import co.edu.unicauca.saberpro.domain.model.Pregunta;

import java.util.List;
import java.util.Optional;

/**
 * Contrato de persistencia del agregado {@link Pregunta}.
 * <p>
 * Es una interfaz pura: no conoce JPA, SQL ni ningún framework. Trabaja con el agregado completo
 * (la raíz y sus opciones), nunca con partes sueltas de él.
 */
public interface PreguntaRepository {

    /**
     * Guarda una pregunta nueva o actualiza una existente.
     *
     * @param pregunta agregado a guardar
     * @return la pregunta guardada, con identificador
     */
    Pregunta guardar(Pregunta pregunta);

    /**
     * Busca una pregunta por su identificador.
     *
     * @param id identificador
     * @return la pregunta, si existe
     */
    Optional<Pregunta> buscarPorId(Long id);

    /**
     * Lista todas las preguntas.
     *
     * @return preguntas registradas
     */
    List<Pregunta> listar();

    /**
     * Lista las preguntas que están en un estado.
     *
     * @param estado estado del ciclo de vida
     * @return preguntas en ese estado
     */
    List<Pregunta> buscarPorEstado(EstadoPregunta estado);
}
