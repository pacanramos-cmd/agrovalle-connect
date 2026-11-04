# Guía: llevar los cambios a GitHub, activar la Integración Continua y proteger `main`

Guía operativa de la Sesión 10 (Taller TID) aplicada a AgroValle Connect. Los pasos de GitHub se hacen en el sitio web;
no se pueden automatizar desde el código.

## Parte A. Subir los cambios del Sprint 2 al repositorio

1. Abrir una terminal en la carpeta del repositorio y actualizar `main`:
   ```bash
   git checkout main
   git pull origin main
   git checkout -b feature/sprint-2-hu02-hu04-ci
   ```
2. Copiar el contenido del paquete de cambios **encima** de la carpeta del repositorio, aceptando reemplazar archivos.
3. Revisar qué cambió: `git status`. Deben aparecer archivos nuevos y modificados, y ninguno dentro de `target/`.
4. Confirmar en grupos pequeños, con Conventional Commits (cada grupo es un commit):
   ```bash
   git add pom.xml .env.example src/main/resources src/test/resources
   git commit -m "fix(config): unificar JaCoCo y definir propiedades JWT"

   git add src/main/java/com/agrovalle/connect/model src/main/java/com/agrovalle/connect/repository \
           src/main/java/com/agrovalle/connect/mapper src/main/java/com/agrovalle/connect/service/AgricultorService.java \
           src/test/java/com/agrovalle/connect/model src/test/java/com/agrovalle/connect/service/AgricultorServiceTest.java
   git commit -m "refactor(model): separar Usuario y Agricultor segun schema.sql"

   git add src/main/java/com/agrovalle/connect/security src/main/java/com/agrovalle/connect/service/AuthService.java \
           src/main/java/com/agrovalle/connect/controller/AuthController.java \
           src/main/java/com/agrovalle/connect/controller/GlobalExceptionHandler.java \
           src/main/java/com/agrovalle/connect/dto src/main/java/com/agrovalle/connect/exception \
           src/test/java/com/agrovalle/connect/security src/test/java/com/agrovalle/connect/controller/AuthControllerTest.java \
           src/test/java/com/agrovalle/connect/service/AuthServiceTest.java
   git commit -m "feat(auth): HU-13 inicio de sesion con JWT y pruebas"

   git add src/main/java/com/agrovalle/connect/service src/main/java/com/agrovalle/connect/event \
           src/main/java/com/agrovalle/connect/config src/main/java/com/agrovalle/connect/controller/ProductoController.java \
           src/test/java/com/agrovalle/connect/service src/test/java/com/agrovalle/connect/event \
           src/test/java/com/agrovalle/connect/repository src/test/java/com/agrovalle/connect/controller
   git commit -m "feat(productos): HU-02 publicacion y HU-04 filtro con Strategy, Observer y Specification"

   git add .github
   git commit -m "ci: pipeline build-and-test con checkstyle, pruebas y umbral JaCoCo"

   git add docs README.md
   git commit -m "docs: sprint 2, guia de CI y arquitectura actualizada"
   ```
   Si algún `git add` indica que una ruta no existe, se omite y se sigue con el siguiente. Al final, `git status` debe
   mostrar el árbol limpio.
5. Antes de subir, ejecutar localmente lo mismo que hará GitHub:
   ```bash
   mvn clean compile
   mvn checkstyle:check
   mvn test
   mvn jacoco:check
   ```
   Si algo falla, se corrige aquí y no en GitHub. El reporte queda en `target/site/jacoco/index.html`.
6. Subir la rama y abrir el Pull Request:
   ```bash
   git push -u origin feature/sprint-2-hu02-hu04-ci
   ```
   En GitHub aparece el botón **Compare & pull request**; el destino es `main`.

## Parte B. Proteger la rama `main` (Gate de la Sesión 10)

El check `build-and-test` solo aparece en la lista de checks obligatorios **después de haberse ejecutado al menos una
vez**, por eso primero se abre el Pull Request de la Parte A y se espera a que el pipeline termine.

1. En GitHub: **Settings → Branches → Add branch protection rule** (o **Settings → Rules → Rulesets → New branch
   ruleset**, según la versión de la interfaz).
2. Branch name pattern: `main`.
3. Activar:
   - **Require a pull request before merging**, con **Require approvals = 1**.
   - **Require status checks to pass before merging** y buscar y marcar **`build-and-test`**.
   - **Require branches to be up to date before merging**.
   - **Do not allow bypassing the above settings** (o **Include administrators**), para que ni el líder pueda saltarse la regla.
4. Guardar. Tomar una captura de la regla guardada: es evidencia del entregable.

## Parte C. Simulación de fallo intencional (tareas 4 y 5 del taller)

1. Con `main` ya protegida y actualizada, crear una rama:
   ```bash
   git checkout main && git pull origin main
   git checkout -b feature/prueba-fallo-ci
   ```
2. Romper una prueba a propósito, por ejemplo cambiar `assertEquals("Mango", ...)` por `"Banano"` en
   `ProductoControllerTest`, o quitar el Javadoc de una clase pública para violar Checkstyle.
3. `git commit -am "test: fallo intencional para probar la proteccion de main"` y `git push -u origin feature/prueba-fallo-ci`.
4. Abrir el Pull Request hacia `main`. El pipeline queda en rojo y el botón **Merge** aparece bloqueado. **Captura 1:** el
   Pull Request con la X roja y el merge bloqueado. Entrar a **Details** y ver el log de Maven.
5. Corregir el fallo en la misma rama, `git commit` y `git push`: el pipeline se ejecuta solo y queda en verde.
   **Captura 2:** el Pull Request con todas las comprobaciones en verde.
6. Un **compañero** (no quien abrió el Pull Request) entra a **Files changed → Review changes → Approve**. **Captura 3:** la
   aprobación. Después se hace **Merge pull request**.

## Parte D. Lista de evidencias para el PDF

| Evidencia | Dónde se obtiene |
|---|---|
| Pestaña Actions con la ejecución en verde | GitHub → Actions |
| Reporte JaCoCo con el porcentaje real | `target/site/jacoco/index.html` o el artefacto `reporte-jacoco` del Actions |
| Regla de protección de `main` guardada | Settings → Branches |
| Pull Request bloqueado por el fallo, y luego en verde | Capturas 1 y 2 de la Parte C |
| Aprobación de un compañero | Captura 3 de la Parte C |
| Tablero con columnas y límites WIP visibles | GitHub Projects (tablero público) |
| Historial de commits en Conventional Commits | GitHub → Commits |

## Nota sobre el tablero Kanban

La protección de ramas y el pipeline se configuran en GitHub, no en el tablero. El tablero (criterio Scrumban) solo
registra el trabajo: cada tarea `T-CI.x` de `docs/sprint-2-planning.md` es una tarjeta que se mueve por Backlog → Ready →
In progress → In review → Done con los límites WIP del equipo (In progress ≤ 3, In review ≤ 2).
