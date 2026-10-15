# Modelo de datos — Normalización (3FN) y mapeo JPA

**AgroValle Connect · Sprint 1 (HU-01 registro de agricultores, HU-13 inicio de sesión con JWT)**

Documento de apoyo a `modelo-entidad-relacion.png`, `modelo-datos-der.png` y `schema-agrovalle.sql`.

## 1. Tablas del modelo

| Tabla | Propósito | Llave primaria | Llaves foráneas | Unicidad |
|---|---|---|---|---|
| `rol` | Catálogo de roles; el JWT de HU-13 lleva el rol | `id` | — | `nombre` |
| `municipio` | Catálogo de municipios del Valle del Cauca | `id` | — | `nombre` |
| `agricultor` | Usuario que se registra (HU-01) e inicia sesión (HU-13) | `id` | `municipio_id` → `municipio.id`, `rol_id` → `rol.id` | `cedula`, `correo` |

Relaciones: un municipio tiene muchos agricultores (1:N) y un rol tiene muchos agricultores (1:N). Todo agricultor
tiene exactamente un municipio y un rol (participación total, columnas `NOT NULL`). La integridad referencial usa
`ON DELETE RESTRICT`: no se puede borrar un municipio o un rol que esté en uso.

## 2. Normalización

**Primera forma normal (1FN).** Todos los atributos son atómicos: no hay listas ni grupos repetidos en una columna, y
cada tabla tiene llave primaria. La ubicación del agricultor (`ubicacion_valle` en el BDD de HU-01) no se guarda como
texto libre, sino como referencia a `municipio`.

**Segunda forma normal (2FN).** Está en 1FN y todas las llaves primarias son simples (una sola columna `id`), por lo que
no pueden existir dependencias parciales de la llave.

**Tercera forma normal (3FN).** Está en 2FN y no hay dependencias transitivas. Dependencias funcionales del modelo:

- `agricultor`: `id → nombre, cedula, correo, contrasena_hash, municipio_id, rol_id, creado_en`. También
  `cedula → id` y `correo → id`, porque ambas son claves candidatas (únicas); sus determinantes son llaves, así que
  tampoco hay anomalías de tipo Boyce-Codd.
- `municipio`: `id → nombre` y `nombre → id`.
- `rol`: `id → nombre` y `nombre → id`.

Ningún atributo no clave determina a otro atributo no clave. Si `agricultor` guardara el nombre del municipio, ese
nombre dependería de `municipio_id` y no de `id` (dependencia transitiva `id → municipio_id → nombre_municipio`). Por
eso el nombre vive solo en `municipio`.

## 3. Reglas de negocio y dónde se hacen cumplir

| Regla | HU | Base de datos (PostgreSQL) | Aplicación |
|---|---|---|---|
| Cédula única y de 6 a 10 dígitos | HU-01 | `UNIQUE` + `CHECK (cedula ~ '^[0-9]{6,10}$')` | `@Pattern` en el DTO; el servicio detecta duplicados |
| Correo único y en minúsculas | HU-01, HU-13 | `UNIQUE` + `CHECK (correo = LOWER(correo))` | El servicio normaliza a minúsculas antes de guardar |
| Nombre obligatorio y no vacío | HU-01 | `NOT NULL` + `CHECK (BTRIM(nombre) <> '')` | `@NotBlank` en el DTO |
| Municipio válido del Valle | HU-01 | `FOREIGN KEY` a `municipio` | El servicio busca el municipio por nombre; si no existe responde 400 |
| Contraseña nunca en texto plano | HU-01, HU-13 | `contrasena_hash VARCHAR(100) NOT NULL` | BCrypt en el servicio de registro; comparación en el login |
| Rol del usuario en el token | HU-13 | `FOREIGN KEY` a `rol` | `JwtService` incluye el rol en el token |

## 4. Mapeo DDL ↔ entidades JPA (paquete `com.agrovalle.connect.model`)

| Tabla / columna SQL | Tipo SQL | Entidad · atributo Java | Anotaciones |
|---|---|---|---|
| `rol` | — | `Rol` | `@Entity`, `@Table(name = "rol")` |
| `rol.id` | `BIGINT` identity | `Long id` | `@Id`, `@GeneratedValue(IDENTITY)` |
| `rol.nombre` | `VARCHAR(30)` | `String nombre` | `@Column(nullable = false, unique = true, length = 30)` |
| `municipio` | — | `Municipio` | `@Entity`, `@Table(name = "municipio")` |
| `municipio.id` | `BIGINT` identity | `Long id` | `@Id`, `@GeneratedValue(IDENTITY)` |
| `municipio.nombre` | `VARCHAR(60)` | `String nombre` | `@Column(nullable = false, unique = true, length = 60)` |
| `agricultor` | — | `Agricultor` | `@Entity`, `@Table` con `@UniqueConstraint` (`uq_agricultor_cedula`, `uq_agricultor_correo`) |
| `agricultor.id` | `BIGINT` identity | `Long id` | `@Id`, `@GeneratedValue(IDENTITY)` |
| `agricultor.nombre` | `VARCHAR(120)` | `String nombre` | `@Column(nullable = false, length = 120)` |
| `agricultor.cedula` | `VARCHAR(10)` | `String cedula` | `@Column(nullable = false, length = 10)` |
| `agricultor.correo` | `VARCHAR(150)` | `String correo` | `@Column(nullable = false, length = 150)` |
| `agricultor.contrasena_hash` | `VARCHAR(100)` | `String contrasenaHash` | `@Column(name = "contrasena_hash", nullable = false, length = 100)` |
| `agricultor.municipio_id` | `BIGINT` FK | `Municipio municipio` | `@ManyToOne(fetch = LAZY, optional = false)`, `@JoinColumn(name = "municipio_id", nullable = false)` |
| `agricultor.rol_id` | `BIGINT` FK | `Rol rol` | `@ManyToOne(fetch = LAZY, optional = false)`, `@JoinColumn(name = "rol_id", nullable = false)` |
| `agricultor.creado_en` | `TIMESTAMPTZ` | `Instant creadoEn` | `@Column(name = "creado_en", nullable = false, updatable = false)`; se asigna en `@PrePersist` |

## 5. Decisiones y notas

- **Validación del esquema:** `application.properties` usa `spring.jpa.hibernate.ddl-auto=validate`. Hibernate no crea
  tablas en PostgreSQL: el script `schema-agrovalle.sql` las crea y las entidades deben coincidir con él.
- **Pruebas:** el perfil de pruebas usa H2 en modo PostgreSQL con `create-drop`, donde Hibernate genera las tablas a
  partir de las entidades. `ModeloDatosTest` comprueba la llave generada, la fecha de creación, la unicidad de cédula
  y correo, y la obligatoriedad del municipio.
- **Contrato REST:** el JSON de registro conserva `ubicacion_valle` del BDD, que corresponde al nombre del municipio. El
  servicio lo traduce a `municipio_id`.
- **Pendientes con el Product Owner** (Parte 3 del `sprint-1-planning.md`): correo y contraseña en el registro, y
  respuesta `409 Conflict` ante cédula o correo duplicados.
- **Evolución:** si se incorporan compradores, `rol` ya permite distinguirlos; separar `usuario` de `agricultor` queda
  como mejora posterior.
