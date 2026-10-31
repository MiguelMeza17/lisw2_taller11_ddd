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
