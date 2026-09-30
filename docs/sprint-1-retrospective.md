# Sprint 1 Retrospective — AgroValle Connect

**Sprint:** 24 de septiembre – 7 de octubre de 2026
**Equipo:** Andres Felipe Ramos, Luis Felipe Porras, Santiago Narvaez Rivera, Milton Adolfo Cortes.
**Formato:** Qué salió bien, qué salió mal y qué se mejora.

> Retrospectiva registrada al 29 de septiembre de 2026 (día 6 de 14). El equipo la actualiza el 7 de octubre con el resultado final del sprint.

---

## 1. Qué salió bien

- El equipo dejó operativo el flujo de trabajo con Git y GitHub: ramas por tarea, Pull Requests vinculados al issue con `Closes #N` y fusión hacia `main`. La tarea T-01.1 completó todo el ciclo (rama, commit, PR #42, merge y cierre del issue).
- El tablero Kanban quedó configurado con las columnas operativas y los límites de trabajo en progreso que pide la rúbrica (In progress ≤ 3 y Code Review ≤ 2).
- Los workflows del tablero mueven las tarjetas de forma automática al abrir un Pull Request y al cerrar un issue.
- La configuración de PostgreSQL se implementó con variables de entorno, sin credenciales en el repositorio, y se agregó `.env.example` como plantilla para todo el equipo.
- Todos los integrantes participaron en el Sprint 0 y el Sprint 1, con aportes verificables en el historial de commits: backlog, infraestructura, planificación técnica y tarea inicial de base de datos.
- El equipo mantuvo comunicación por llamadas de Discord con pantalla compartida del tablero.

## 2. Qué salió mal o costó trabajo

- Al inicio, los límites del tablero no coincidían con la rúbrica: el Backlog mostraba 15/5 en rojo y la columna In review tenía un límite de 5 en lugar de 2. Se corrigió durante el sprint.
- Al mover una tarjeta a Done por error, el workflow cerró el issue #24 antes de tiempo y fue necesario reabrirlo.
- Un nombre de rama se creó con un error de escritura (`featute`) y debió renombrarse antes de publicarse.
- El commit de T-01.1 no siguió la convención de Conventional Commits que exige la Definition of Done.
- Los Pull Requests #42 y el de la bitácora se fusionaron sin la aprobación de otro integrante, lo que incumple el criterio de Peer Review de la Definition of Done.
- La Definition of Done menciona cobertura mínima del 60% con JaCoCo, pero el plugin todavía no está configurado en el `pom.xml`.
- La documentación del sprint (bitácora, review y retrospectiva) se elaboró después de iniciado el trabajo técnico, y no de forma continua.
- El tablero se mantuvo privado, lo que impide que el docente lo consulte hasta cambiar su visibilidad.

## 3. Qué se mejora

| Acción | Motivo |
|---|---|
| Asignar un revisor distinto al autor en cada Pull Request y aprobarlo antes del merge | Cumplir el criterio de Peer Review de la Definition of Done |
| Escribir los mensajes de commit con Conventional Commits (`feat:`, `fix:`, `docs:`, `test:`) | Cumplir la Definition of Done y mantener un historial legible |
| Registrar el Daily Scrum el mismo día en que ocurre | Evitar documentar el sprint a posteriori |
| Crear la tarea de traducción de criterios BDD a pruebas JUnit 5 y configurar JaCoCo | Cumplir la rúbrica y la cobertura definida en la Definition of Done |
| Revisar la columna de destino antes de mover una tarjeta a Done | Evitar cierres accidentales de issues |
| Hacer público el tablero y verificar el enlace en una ventana de incógnito | Permitir la revisión del docente |

## 4. Compromisos para el Sprint 2

- Aplicar las mejoras de la tabla anterior desde el primer día del sprint.
- Mantener los límites WIP definidos (In progress ≤ 3 y Code Review ≤ 2).
- Cerrar las historias solo cuando cumplan toda la Definition of Done.

## 5. Actualización de cierre (7 de octubre)

[COMPLETAR el día del cierre: qué mejoras se aplicaron, qué historias se terminaron y qué aprendió el equipo.]