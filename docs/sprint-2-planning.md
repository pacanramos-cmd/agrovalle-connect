# Sprint Planning — Sprint 2
**AgroValle Connect**

| | |
|---|---|
| Institución | Institución Universitaria Antonio José Camacho (UNIAJC) |
| Curso | Ingeniería de Software II |
| Docente | Paola Andrea Bedoya Toro |
| Equipo | Andrés Felipe Ramos, Luis Felipe Porras, Santiago Narváez Rivera, Milton Adolfo Cortes |
| Duración del sprint | [COMPLETAR: fechas de inicio y fin] |
| Repositorio | https://github.com/pacanramos-cmd/agrovalle-connect |
| Estrategia de branching | GitFlow (`main`, `develop`, `feature/*`); todo cambio entra por Pull Request |

---

## Parte 1. El Qué: selección y objetivo del Sprint

### 1.1 Proceso de selección

1. El Product Owner presenta el Product Backlog restante (HU-02 a HU-12, HU-14 y HU-15) y confirma que HU-01 y HU-13 se
   cierran en este sprint si quedaron tareas abiertas.
2. El equipo revisa los escenarios BDD de HU-02 y HU-04 y las decisiones de diseño que dependen de ellos.
3. Se calcula la capacidad con la velocidad del Sprint 1: 0 de 10 puntos acreditados al cierre, porque HU-01 (5) y HU-13 (5)
   seguían abiertas según la Definition of Done. Por eso el sprint se compromete con 8 puntos y cierra primero esas dos historias.
4. Se seleccionan las historias y se construye el Sprint Goal.

### 1.2 Historias del Sprint 2

| ID | Historia de usuario | MoSCoW | Pts | Sprint |
|---|---|:---:|---:|---|
| HU-02 | Publicación de productos | Must | 5 | Sprint 2 |
| HU-04 | Filtro por municipio y categoría | Must | 3 | Sprint 2 |
| HU-06 | Registro de finca | Should | 3 | Sprint 3 (aplazada) |

HU-04 entra en este sprint porque las categorías ya quedaron resueltas en el diseño: `categoria` y `tipo_producto` son
catálogos del esquema (3FN), por lo que no hay que definir categorías nuevas. HU-06 se aplaza porque la finca es un dato
opcional de la publicación (`finca_id`) y no bloquea HU-02.

### 1.3 Capacidad del sprint

Total comprometido: **8 Story Points** (HU-02: 5, HU-04: 3). Además de las historias, el sprint incluye trabajo
habilitador sin puntos: el pipeline de Integración Continua de la Sesión 10 y la deuda técnica detectada al cerrar el
Sprint 1 (sección 2.4).

### 1.4 Sprint Goal

> "Permitir que un agricultor autenticado publique sus productos y que cualquier comprador los encuentre filtrando por
> municipio y categoría, con cada cambio validado automáticamente por Integración Continua (Checkstyle, pruebas y
> cobertura JaCoCo de al menos 60%) antes de llegar a `main`."

### 1.5 Historias seleccionadas

**HU-02: Publicación de productos (5 Story Points, Must Have)**
- **Endpoint:** `POST /api/v1/productos` → `201 Created`, `400`, `401` sin token válido, `403` si el rol no es AGRICULTOR.
- **Por qué entra:** es el primer endpoint protegido con el JWT de HU-13 y crea la oferta que alimenta el resto de las
  historias (HU-04, HU-07, HU-08). Es también el lugar natural para aplicar los patrones de comportamiento del bloque 2:
  Strategy (comisión por municipio) y Observer (evento de producto publicado).

**HU-04: Filtro por municipio y categoría (3 Story Points, Must Have)**
- **Endpoint:** `GET /api/v1/productos?municipio=&categoria=` → `200 OK` con un arreglo JSON, vacío si no hay coincidencias.
- **Por qué entra:** completa el flujo de oferta y demanda con el menor esfuerzo, y los filtros se arman con
  Specification sin una consulta por cada combinación.

**Total del Sprint 2: 8 Story Points.**

### 1.6 Proyección Sprint 3

HU-06 (registro de finca) y HU-07 (inventario), que dependen de los productos publicados en este sprint.

---

## Parte 2. El Cómo: desglose técnico por historia

### 2.1 HU-02: Publicación de productos (5 pts)

| ID Tarea | Descripción Técnica | Componente / Tecnología | Atributo ISO 25010 |
|---|---|---|---|
| T-02.1 | Crear la entidad `Producto` (tabla `producto`) y la enumeración `UnidadMedida`. | JPA (`@Entity`) | Fiabilidad |
| T-02.2 | Crear `ProductoRepository`, `TipoProductoRepository` y `CategoriaRepository`. | Spring Data JPA | Mantenibilidad |
| T-02.3 | Crear `ProductoRequest` y `ProductoResponse` con validación (cantidad y precio mayores que cero, unidad válida). | Java 17 records, Bean Validation | Adecuación funcional |
| T-02.4 | Implementar el patrón Strategy: `CalculadorComisionService`, tres estrategias por zona y `SelectorComision`. | Spring, GoF | Mantenibilidad |
| T-02.5 | Implementar `ProductoService.publicar()`: verifica perfil de agricultor, fecha de cosecha (con `Clock` inyectado) y tipo de producto; guarda y publica `ProductoPublicadoEvent` (Observer). | Spring `@Service`, `@Transactional`, eventos | Fiabilidad |
| T-02.6 | Crear `ProductoController` (POST) y proteger la ruta en `SecurityConfig`: rol AGRICULTOR, 401 sin token. | Spring MVC, Spring Security | Seguridad |
| T-02.7 | Traducir los escenarios BDD a pruebas JUnit 5: servicio con Mockito, controlador con MockMvc e integración con la cadena de seguridad real. | JUnit 5, Mockito, MockMvc | Fiabilidad |

### 2.2 HU-04: Filtro por municipio y categoría (3 pts)

| ID Tarea | Descripción Técnica | Componente / Tecnología | Atributo ISO 25010 |
|---|---|---|---|
| T-04.1 | Crear `ProductoSpecifications` con `activo()`, `deMunicipio()` y `deCategoria()`, sin distinguir mayúsculas. | Spring Data JPA Specification | Mantenibilidad |
| T-04.2 | Implementar `ProductoService.listar()`: compone solo los filtros recibidos y ordena por fecha de creación descendente. | Spring `@Service` | Eficiencia de desempeño |
| T-04.3 | Agregar `GET /api/v1/productos` con parámetros opcionales; ruta pública en `SecurityConfig`. | Spring MVC | Adecuación funcional |
| T-04.4 | Pruebas BDD: filtro por ambos criterios, sin coincidencias (arreglo vacío) y sin filtros. | JUnit 5, H2 | Fiabilidad |

### 2.3 Integración Continua (Sesión 10, trabajo habilitador)

| ID Tarea | Descripción Técnica | Componente / Tecnología | Atributo ISO 25010 |
|---|---|---|---|
| T-CI.1 | Crear `.github/workflows/ci.yml` con el job `build-and-test` y los tres Quality Gates: `mvn checkstyle:check`, `mvn test` y `mvn jacoco:check`. | GitHub Actions | Mantenibilidad |
| T-CI.2 | Dejar una sola configuración de JaCoCo con umbral del 60% en instrucciones y ramas. | Maven, JaCoCo | Fiabilidad |
| T-CI.3 | Activar la regla de protección de `main`: Pull Request obligatorio, check `build-and-test` en verde y una aprobación de un compañero. | GitHub Settings → Branches | Seguridad |
| T-CI.4 | Simular un fallo intencional en una rama `feature/*` y comprobar que el Pull Request bloquea el merge. | GitHub Actions | Fiabilidad |
| T-CI.5 | Corregir el fallo, verificar el pipeline en verde y fusionar con Peer Review. | GitHub Pull Requests | Mantenibilidad |

### 2.4 Deuda técnica del Sprint 1 que se salda en este sprint

| ID Tarea | Descripción Técnica | Motivo |
|---|---|---|
| T-D.1 | Reemplazar la entidad única `Agricultor` por `Usuario` más `Agricultor` (relación 1 a 1 con llave compartida), como define `schema.sql`. | Con `ddl-auto=validate` la aplicación no arrancaría contra PostgreSQL con el esquema del diseño. |
| T-D.2 | Escribir las pruebas de HU-13: `AuthService`, `JwtService`, `JwtAuthenticationFilter` y `AuthController`. | El código de autenticación no tenía pruebas y bajaba la cobertura. |
| T-D.3 | Definir `jwt.secret` y `jwt.expiration-ms` en `application.properties` (variables de entorno) y en las propiedades de prueba. | Sin ellas el contexto de Spring no arranca. |
| T-D.4 | Configurar el código de estado 401 en `SecurityConfig` para solicitudes sin token. | Por defecto Spring Security responde 403 a una solicitud anónima, y el escenario de HU-02 exige 401. |

---

## Parte 3. Tablero y límites de trabajo

El Sprint 2 se gestiona en el mismo tablero de GitHub Projects del Sprint 1, con las columnas Backlog, Ready, In progress,
In review y Done y los límites WIP **In progress ≤ 3** e **In review ≤ 2**. Cada tarea de las tablas anteriores es un
issue vinculado a su Pull Request.
