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

---

## Sprint 2

> Registro de los Daily Scrum del Sprint 2. Cada reunión se anota con la misma estructura del Sprint 1: asistentes,
> estado del tablero, tabla de avances y acuerdos.

### Daily Scrum — 04/11/2026

**Asistentes:** [COMPLETAR]

**Estado del tablero:**
- Done: HU-02 (#8), HU-04 (#10), T-CI.1 (#58) y T-CI.2 (#59). Pull Request #57 fusionado en `main` con `build-and-test` en verde.
- In progress (3/3): T-CI.3 (#60, protección de `main`), T-01.2 (#25) y T-13.1 (#33).
- Ready: T-CI.4 (#61, simulación del fallo) y T-CI.5 (#62, corrección y merge con Peer Review).
- In review (0/2): ninguna.

| Integrante | Qué hice | Qué haré | Impedimentos |
|---|---|---|---|
| Andrés Felipe Ramos | [COMPLETAR] | [COMPLETAR] | [COMPLETAR] |
| Luis Felipe Porras | Actualicé la rama del Sprint 2, instalé Temurin 17 con `JAVA_HOME` y ejecuté las puertas en local; el reporte JaCoCo dio 95% en instrucciones y 93% en ramas; reuní las evidencias del Pull Request #57; creé en el tablero los issues T-CI.1 a T-CI.5 (#58 a #62), pasé HU-02 y HU-04 a Done y dejé los límites WIP en regla; completé los documentos del Sprint 2 en la rama `docs/sprint-2-cierre` | Abrir el Pull Request de la documentación, gestionar la regla de protección de `main` con el dueño y hacer la simulación del fallo | Sin permisos de administrador para activar la regla de rama |
| Santiago Narváez Rivera | Subí HU-13 (JWT), HU-02 y HU-04 con Strategy, Observer y Specification, el pipeline `ci.yml` con las 3 Quality Gates, la guía de CI y la documentación del Sprint 2; fusioné los PR #56 y #57 | Revisar la protección de `main` y apoyar la simulación del fallo | Sin permisos de administrador para activar la regla de rama |
| Milton Adolfo Cortes | [COMPLETAR] | [COMPLETAR] | [COMPLETAR] |

**Acuerdos:**
- Pedir al dueño del repositorio que cree la regla de protección de `main` (Pull Request, check `build-and-test` y una aprobación), o que otorgue rol de administrador.
- Hacer la simulación del fallo en una rama `feature/prueba-fallo-ci` cuando la regla esté activa.
- Registrar las tareas T-CI.1 a T-CI.5 en el tablero y completar los documentos del Sprint 2.
