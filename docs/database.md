# Base de Datos

Este sistema utiliza **PostgreSQL** como base de datos relacional principal. La elección se basó en los siguientes criterios:

- Es **[ACID-compliant](https://dev.to/pawnsapprentice/understanding-postgresql-and-its-strong-embrace-of-acid-properties-2dpl)**, ideal para garantizar consistencia en redirecciones y actualizaciones.
- Ofrece **buen rendimiento para lecturas intensivas**, especialmente cuando se usa con índices adecuados.
- Es soportado en entornos cloud y en entornos locales vía Docker.
- Posee una activa comunidad

---

## Estructura principal

La tabla principal es `short_url`, que almacena:

| Campo          | Tipo           | Descripción                                   |
|----------------|----------------|-----------------------------------------------|
| `short_code`   | VARCHAR(255)   | Identificador único. Clave primaria.          |
| `access_count` | BIGINT         | Contador de accesos acumulados.               |
| `created_at`   | TIMESTAMPTZ(6) | Fecha de creación (con zona horaria).         |
| `is_active`    | BOOLEAN        | Indica si la URL está activa o deshabilitada. |
| `long_url`     | VARCHAR(255)   | URL larga original. Valor único.              |

---

## Índices y rendimiento

Se crearon índices en los siguientes campos:

- `short_code` → indexado automáticamente como clave primaria. Usado en redirección y actualizaciones.
- `long_url` → índice único. Usado para garantizar unicidad e idempotencia al acortar URLs.

---

## Consideraciones futuras

- Actualmente, no se utiliza una herramienta de versionado como [Liquibase](https://www.liquibase.com/) o [Flyway](https://documentation.red-gate.com/flyway/getting-started-with-flyway), pero **se recomienda integrarla** en entornos profesionales para mantener control y trazabilidad sobre los cambios en el esquema.
- Implementar limpieza de datos antiguos en procesos batch (URL inactivas o con baja frecuencia de uso).
- Evaluar estrategias como **replicación primaria-réplica ([master-slave](https://medium.com/@ayogun/master-slave-database-architecture-in-a-nutshell-e20a73e979d1))** para escalar las operaciones de lectura y mejorar la tolerancia a fallos.

