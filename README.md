# Taller 11 — Domain-Driven Design (DDD)

Modelado estratégico y táctico del Banco de Preguntas Saber Pro · Laboratorio de Ingeniería de Software II · Universidad del Cauca · 2026.2

## Tecnologías

- Java 17+ puro, **sin Spring ni JPA**
- Maven
- JUnit 5 (única dependencia, solo para pruebas)

## Estructura de paquetes

```
co.edu.unicauca.saberpro.domain/
├── model/          # Agregado, entidades, objetos de valor y enums
├── exception/      # Excepciones de negocio
└── repository/     # Interfaces de repositorio (puras)
```

## Pruebas

```bash
mvn test
```

## Verificación

El workflow `.github/workflows/verificacion.yml` corre en cada push y PR a `main` y `dev`:

1. Comprueba que el dominio no importe nada de Spring ni JPA:
   `grep -r "org.springframework\|jakarta.persistence" src` debe salir vacío.
2. Ejecuta `mvn test`.
