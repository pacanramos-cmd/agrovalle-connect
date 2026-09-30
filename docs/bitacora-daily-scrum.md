# Bitácora de Daily Scrum — AgroValle Connect

**Sprint 1:** 24 de septiembre – 7 de octubre de 2026
**Sprint Goal:** Dejar configurada la conexión segura a PostgreSQL y avanzar las historias HU-01 y HU-13 (10 Story Points).
**Modalidad:** reuniones de máximo 15 minutos por llamada de voz en Discord (canal "Habitat de Tryhards"), con pantalla compartida del tablero de GitHub Projects.
**Equipo:** Andres Felipe Ramos, Luis Felipe Porras, Santiago Narvaez Rivera, Milton Adolfo Cortes.

Formato: qué se hizo desde la reunión anterior, qué se hará hasta la siguiente y qué impedimentos hay. Los avances registrados corresponden a commits y Pull Requests del repositorio.

---

## Sprint 0 (preparación)

### Daily Scrum — 15/09/2026

**Asistentes:** todo el equipo.

| Integrante | Qué hice | Qué haré | Impedimentos |
|---|---|---|---|
| Andres Felipe Ramos | Inicialicé el proyecto Java 17 / Spring Boot con Maven y actualicé el backlog; resolví un conflicto de fusión en `BACKLOG.md` | Configurar Husky y Checkstyle | Conflicto en `BACKLOG.md` (resuelto usando la versión del equipo) |
| Milton Adolfo Cortes | Agregué las historias de usuario HU-10 a HU-12 al backlog (PR #3) | Apoyar en la especificación del backlog | Ninguno |
| Luis Felipe Porras | Revisé el alcance de las historias de HU-01 y HU-13 | Preparar el entorno para el Sprint 1 | Ninguno |
| Santiago Narvaez Rivera | Trabajé en la especificación de historias del backlog | Completar la especificación del Sprint 0 | Ninguno |

**Acuerdos:** el backlog se trabaja por ramas `feature/...` y se integra con Pull Request.

### Daily Scrum — 16/09/2026

**Asistentes:** todo el equipo.

| Integrante | Qué hice | Qué haré | Impedimentos |
|---|---|---|---|
| Andres Felipe Ramos | Configuré Husky con pre-commit hooks de Checkstyle y tests; excluí `node_modules` del control de versiones; fusioné el PR #5 (HU-06 a HU-09) | Dejar listo el tablero de GitHub Projects | Ninguno |
| Santiago Narvaez Rivera | Completé la especificación del backlog del Sprint 0 (`docs(backlog)`) | Preparar la planificación técnica del Sprint 1 | Ninguno |
| Milton Adolfo Cortes | Apoyé la revisión de las historias HU-10 a HU-12 | Revisar la Definition of Done | Ninguno |
| Luis Felipe Porras | Revisé el backlog y las prioridades MoSCoW | Tomar la primera tarea técnica del Sprint 1 | Ninguno |

**Acuerdos:** el Sprint 1 incluye HU-01 y HU-13 (Must, 5 puntos cada una).

---

## Sprint 1

### Daily Scrum — 29/09/2026

**Asistentes:** todo el equipo, en llamada de Discord con pantalla compartida del tablero.

**Estado del tablero:**
- Done: T-01.1 (#24). HU-01 al 11% (1/9 tareas).
- In progress (2/3): T-01.2 (#25) y T-13.1 (#33).
- Ready: T-01.3 en adelante, T-13.2 y T-13.3.
- Límites WIP: In progress ≤ 3, Code Review ≤ 2.

| Integrante | Qué hice | Qué haré | Impedimentos |
|---|---|---|---|
| Luis Felipe Porras | Configuré la conexión a PostgreSQL con variables de entorno (T-01.1, #24) y fusioné el PR #42 | Continuar con las tareas de HU-01 | Ninguno |
| Santiago Narvaez Rivera | Agregué la planificación técnica del Sprint 1 (`docs/sprint-1-planning.md`) | Crear la tarea de traducción BDD a JUnit 5 | Ninguno |
| Andres Felipe Ramos | Ajusté los límites WIP del tablero (In progress ≤ 3, Code Review ≤ 2) y asigné las tareas | Tomar tareas de HU-13 | Ninguno |
| Milton Adolfo Cortes | Revisé el estado del tablero y las tareas de HU-13 | Trabajar en T-13.1 | Ninguno |

**Acuerdos:**
- Completar `bitacora-daily-scrum.md`, `sprint-1-review.md` y `sprint-1-retrospective.md`.
- Hacer público el tablero para la revisión del docente.