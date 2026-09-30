# Sprint Planning — Sprint 1
**AgroValle Connect**

| | |
|---|---|
| Institución | Institución Universitaria Antonio José Camacho (UNIAJC) |
| Curso | Ingeniería de Software II |
| Docente | Paola Andrea Bedoya Toro |
| Equipo | Andrés Felipe Ramos, Luis Felipe Porras, Santiago Narváez Rivera, Milton Adolfo Cortes |
| Duración del sprint | 2 semanas |
| Repositorio | https://github.com/pacanramos-cmd/agrovalle-connect |
| Estrategia de branching | GitFlow (`main`, `develop`, `feature/*`, `release/*`) |

---

## Parte 1. El Qué: selección y objetivo del Sprint

### 1.1 Proceso de selección

1. El Product Owner presenta las historias priorizadas del Product Backlog (HU-01 a HU-15, ordenadas con MoSCoW).
2. El equipo revisa los escenarios BDD (Given-When-Then) de cada historia candidata y aclara dudas.
3. Se evalúa la capacidad del equipo. Al ser el primer sprint no existe una velocidad histórica, por lo que se parte de la capacidad de 10 Story Points indicada por la docente para equipos con experiencia inicial baja.
4. Se seleccionan las historias a las que el equipo se compromete para el sprint.
5. Se construye y aprueba el Sprint Goal.

### 1.2 Historias del Sprint 1

| ID | Historia de usuario | MoSCoW | Pts | Sprint |
|---|---|:---:|---:|---|
| HU-01 | Registro de agricultores | Must | 5 | Sprint 1 |
| HU-13 | Inicio de sesión con JWT | Must | 5 | Sprint 1 |
| HU-02 | Publicación de productos | Must | 5 | Sprint 2 (tentativo) |
| HU-04 | Filtro por municipio y categoría | Must | 3 | Sprint 2 (tentativo) |
| HU-06 | Registro de finca | Should | 3 | Sprint 2 (alternativa) |

*Nota: se evaluó incluir HU-04 en el Sprint 1, pero se descartó por indicación de la docente: aún no existen las categorías de producto ni una historia que las defina.*

### 1.3 Capacidad del sprint

Capacidad máxima: **10 Story Points**. El equipo se compromete con los 10 puntos, sin margen. El principal riesgo es configurar Spring Security con poca experiencia previa; por eso el orden de implementación es HU-01 primero y HU-13 sobre ella, de modo que si algo se retrasa, al menos el registro quede completo.

### 1.4 Sprint Goal

> "Habilitar el registro de agricultores del Valle del Cauca, como los de Dagua y Palmira, y su inicio de sesión seguro mediante JWT, garantizando la integridad de los datos en PostgreSQL y dejando lista la base de autenticación de la plataforma."

### 1.5 Historias seleccionadas

**HU-01: Registro de agricultores (5 Story Points, Must Have)**
- **Endpoint:** `POST /api/v1/auth/register` → `201 Created` o `400 Bad Request`.
- **Por qué entra:** es la puerta de entrada de la plataforma y no depende de ninguna otra historia. Obliga a recorrer todas las capas por primera vez (entidad, repositorio, servicio, controlador), además de validación y manejo de errores. Crea los usuarios que necesita el inicio de sesión.

**HU-13: Inicio de sesión con JWT (5 Story Points, Must Have)**
- **Endpoint:** `POST /api/v1/auth/login` → `200 OK` con token, o `401 Unauthorized`.
- **Por qué entra:** es la base de seguridad de la plataforma; casi todas las historias restantes exigen un usuario autenticado con JWT. Junto con HU-01 cierra un flujo completo: un agricultor se registra e inicia sesión.

**Total del Sprint 1: 10 Story Points (capacidad: 10).**

### 1.6 Proyección Sprint 2 (tentativa)

HU-02 (publicación de productos, primer endpoint protegido con JWT) junto con HU-04 (si se definen las categorías) o HU-06 (registro de finca): 8 puntos en cualquiera de los dos casos.

---

## Parte 2. El Cómo: desglose técnico por historia

Cada tarea se vincula a un escenario BDD, a un componente y a un atributo de calidad ISO/IEC 25010.

### 2.1 HU-01: Registro de agricultores (5 pts)

| ID Tarea | Descripción Técnica | Componente / Tecnología | Atributo ISO 25010 |
|---|---|---|---|
| T-01.1 | Configurar la conexión a PostgreSQL y las dependencias (web, JPA, validación, driver). Credenciales en variables de entorno. | Spring Boot, PostgreSQL | Seguridad |
| T-01.2 | Crear la tabla `agricultor` y la entidad `Agricultor` (nombre, ubicacion_valle, cédula única, correo único, contraseña cifrada). | PostgreSQL, JPA (`@Entity`) | Fiabilidad |
| T-01.3 | Crear `AgricultorRepository` con métodos para verificar cédula/correo existentes. | Spring Data JPA | Mantenibilidad |
| T-01.4 | Crear DTOs de entrada/salida con validaciones (campos obligatorios, formato de cédula y correo). | Java 17, Bean Validation | Adecuación funcional |
| T-01.5 | Implementar `AgricultorService.registrar()`: reglas de negocio, cifrado BCrypt, persistencia transaccional. | Spring (`@Service`) | Seguridad |
| T-01.6 | Implementar `POST /api/v1/auth/register` → `201 Created`. | Spring Web (`@RestController`) | Compatibilidad |
| T-01.7 | Manejar errores: `400 Bad Request` (datos inválidos), `409 Conflict` (cédula/correo duplicados, por confirmar). | Spring (`@RestControllerAdvice`) | Usabilidad |
| T-01.8 | Pruebas unitarias e integradas: caso exitoso (201) y datos inválidos (400). | JUnit 5, Mockito, MockMvc | Mantenibilidad |
| T-01.9 | Documentar el endpoint en `/docs` y el README. | Markdown | Mantenibilidad |

**Trazabilidad BDD:** Escenario 1 (201) → T-01.2 a T-01.6 y pruebas. Escenario 2 (400) → T-01.4, T-01.7 y pruebas.

### 2.2 HU-13: Inicio de sesión con JWT (5 pts)

| ID Tarea | Descripción Técnica | Componente / Tecnología | Atributo ISO 25010 |
|---|---|---|---|
| T-13.1 | Agregar dependencias de Spring Security y JWT; clave secreta y vigencia como variables de entorno. | Spring Security, jjwt | Seguridad |
| T-13.2 | Configurar Spring Security: política stateless, BCrypt, acceso libre a `/api/v1/auth/**`. | Spring Security | Seguridad |
| T-13.3 | Crear DTOs `LoginRequest`/`LoginResponse` con validación de campos. | Java 17, Bean Validation | Adecuación funcional |
| T-13.4 | Implementar `JwtService`: generación del token firmado (id, rol, expiración). | jjwt (HS256) | Seguridad |
| T-13.5 | Implementar `AuthService.login()`: busca por correo, compara contraseña cifrada, genera JWT. | Spring (`@Service`) | Seguridad |
| T-13.6 | Implementar `POST /api/v1/auth/login` → `200 OK` con token. | Spring Web (`@RestController`) | Compatibilidad |
| T-13.7 | Responder `401 Unauthorized` ante credenciales inválidas, sin revelar cuál campo falló. | Spring (`@RestControllerAdvice`) | Seguridad |
| T-13.8 | Pruebas: `AuthService`, `JwtService`, controlador (200 y 401). | JUnit 5, Mockito, MockMvc | Mantenibilidad |
| T-13.9 | Documentar endpoint, variables de entorno y formato del token. | Markdown | Mantenibilidad |

**Trazabilidad BDD:** Escenario 1 (200, JWT) → T-13.1 a T-13.6 y pruebas. Escenario 2 (401) → T-13.5, T-13.7 y pruebas.

### 2.3 Consideraciones de arquitectura

- **Orden de implementación:** HU-13 depende de la entidad y el repositorio de HU-01. Mientras se construye el registro, se puede avanzar en paralelo en T-13.1, T-13.2 y T-13.4, que no dependen del agricultor.
- La clave secreta del JWT y su vigencia van en variables de entorno, nunca en el repositorio. El token va firmado pero no cifrado: no debe llevar contraseñas ni datos sensibles.
- HU-13, tal como está especificada, genera el token. El filtro que lo valida en endpoints protegidos se aborda con la primera historia protegida (HU-02, Sprint 2).
- Las tareas se reparten por capa (base de datos, repositorio, servicio, controlador, pruebas) para que los integrantes trabajen en archivos distintos y se reduzcan los conflictos en Git.

---

## Parte 3. Aspectos por aclarar con el Product Owner

| # | Aspecto | Propuesta | Estado |
|---|---|---|---|
| 1 | Credenciales en el registro (HU-01): el BDD no incluye correo ni contraseña, y HU-13 las necesita en el mismo sprint. | Incluir correo y contraseña cifrada en el registro (ya incorporado en T-01.2 a T-01.5). | Por confirmar |
| 2 | Cédula o correo ya registrados (HU-01): el BDD solo define 201 y 400. | Responder `409 Conflict` y agregar un escenario BDD. | Pendiente |
| 3 | Contenido y vigencia del token (HU-13): no está definido qué lleva el JWT ni cuánto dura. | Incluir identificador y rol, con vigencia corta configurable (ej. 1 hora). | Pendiente |
| 4 | Usuarios que pueden iniciar sesión (HU-13): la historia habla de "usuario registrado" pero solo existe registro de agricultores. | En el Sprint 1 el login aplica a agricultores; evaluar una historia de registro de compradores. | Pendiente |
| 5 | Categorías de producto (HU-04/HU-02): no existe una definición de categorías. | Definir la categoría como atributo del producto antes de planificar HU-04. | Pendiente (Sprint 2) |

*Nota de estimación: los Story Points son iniciales y deben validarse con Planning Poker. Como el Sprint 1 usa toda la capacidad, una re-estimación al alza obligaría a revisar la selección.*

---

## Seguimiento en GitHub Projects

El avance de HU-01 y HU-13, y de sus tareas técnicas (T-01.x, T-13.x) como sub-issues, se documenta en el tablero Kanban del repositorio (columnas: Backlog, Ready, In progress, In review, Done), con los campos Priority (MoSCoW), Estimate (Story Points), Size y la iteración Sprint 1.
