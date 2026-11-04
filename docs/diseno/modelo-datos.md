# Modelo de datos — normalización (3FN) y mapeo JPA

**AgroValle Connect · cobertura de las 15 historias de usuario (HU-01 a HU-15) · 14 tablas**

Documento de apoyo a `modelo-entidad-relacion.png` (MER), `modelo-datos-der.png` (DER en 3FN) y `schema.sql` (DDL de
PostgreSQL con datos iniciales).

## 1. Decisiones de diseño

- **Usuario con perfiles.** `usuario` guarda lo común a todo actor (correo, contraseña cifrada, rol) y la usa el inicio
  de sesión de HU-13. `agricultor` y `comprador` son perfiles 1 a 1 que comparten su llave primaria con `usuario`.
- **Categoría fuera del producto.** Cada `tipo_producto` pertenece a una `categoria`; el producto publicado solo
  referencia su tipo. Así la categoría no se repite en cada fila (3FN) y se puede filtrar (HU-04) y promediar por tipo
  (HU-03).
- **Precio copiado en el pedido.** `orden_item.precio_unitario` conserva el precio del momento de la compra, de modo que
  el historial (HU-14) y el precio promedio (HU-03) no cambian si el agricultor modifica su precio después.
- **Una orden, un agricultor.** HU-09 habla de órdenes "de sus productos"; por eso `orden_compra` tiene un solo
  `agricultor_id` y el servicio valida que todos sus ítems sean de ese agricultor.
- **Finca opcional pero coherente.** `producto.finca_id` puede ser nulo (HU-06 es independiente de HU-02). Cuando existe,
  una llave foránea compuesta `(finca_id, agricultor_id)` obliga a que la finca sea del mismo agricultor del producto.
- **Despacho aparte.** `despacho` es 0..1 por orden y comparte su llave con `orden_compra`, así no hay columnas nulas.
- **Comunicación.** `mensaje_contacto` (HU-05) y `notificacion` (HU-05, HU-09, HU-10, HU-12) son tablas independientes;
  `notificacion.orden_id` es opcional porque un contacto no pertenece a un pedido.

## 2. Tablas del modelo

| Tabla | Propósito | HU | Llave primaria | Llaves foráneas | Unicidad |
|---|---|---|---|---|---|
| `rol` | Catálogo de roles (`AGRICULTOR`, `COMPRADOR`) | 01, 13 | `id` | — | `nombre` |
| `municipio` | Municipios del Valle del Cauca (42 iniciales) | 01, 04, 06 | `id` | — | `nombre` |
| `categoria` | Categorías de producto | 04 | `id` | — | `nombre` |
| `tipo_producto` | Tipos de producto de cada categoría | 02, 03, 04 | `id` | `categoria_id` | `nombre` |
| `usuario` | Datos de acceso comunes | 01, 13 | `id` | `rol_id` | `correo` |
| `agricultor` | Perfil del productor | 01 | `usuario_id` | `usuario_id`, `municipio_id` | `cedula` |
| `comprador` | Perfil del comprador y dirección de entrega | 05, 08, 10 | `usuario_id` | `usuario_id`, `municipio_id` | — |
| `finca` | Lugar de producción | 06 | `id` | `agricultor_id`, `municipio_id` | (`agricultor_id`, `nombre`) y (`id`, `agricultor_id`) |
| `producto` | Cosecha publicada e inventario | 02, 04, 07, 15 | `id` | `agricultor_id`, `(finca_id, agricultor_id)`, `tipo_producto_id` | — |
| `orden_compra` | Pedido de un comprador a un agricultor | 08, 09, 11, 14 | `id` | `comprador_id`, `agricultor_id` | — |
| `orden_item` | Productos y cantidades de la orden | 03, 08, 14 | (`orden_id`, `producto_id`) | `orden_id`, `producto_id` | — |
| `despacho` | Programación de la entrega | 10 | `orden_id` | `orden_id` | — |
| `mensaje_contacto` | Intención de compra | 05 | `id` | `producto_id`, `comprador_id` | — |
| `notificacion` | Avisos al usuario | 05, 12 | `id` | `usuario_id`, `orden_id` (opcional) | — |

Todas las llaves foráneas usan `ON DELETE RESTRICT`: no se puede borrar un municipio, un producto o una orden que otros
datos todavía referencian.

## 3. Normalización

**Primera forma normal (1FN).** Todos los atributos son atómicos y cada tabla tiene llave primaria. No hay listas en una
columna: los productos de una orden viven en filas de `orden_item`, no en un campo de `orden_compra`, y la ubicación del
agricultor no es texto libre sino una referencia a `municipio`.

**Segunda forma normal (2FN).** Está en 1FN y ningún atributo depende de una parte de una llave compuesta. Las únicas
llaves compuestas son `orden_item` (`orden_id`, `producto_id`), donde `cantidad` y `precio_unitario` dependen del par
completo, y la llave de `producto` hacia `finca`, que es una restricción y no una llave primaria. En el resto, la llave
primaria es una sola columna.

**Tercera forma normal (3FN).** Está en 2FN y ningún atributo no clave depende de otro atributo no clave.
Dependencias funcionales principales:

- `usuario`: `id → nombre, correo, contrasena_hash, rol_id, creado_en`; `correo → id` (clave candidata).
- `agricultor`: `usuario_id → cedula, municipio_id`; `cedula → usuario_id` (clave candidata).
- `comprador`: `usuario_id → municipio_id, direccion_entrega`.
- `producto`: `id → agricultor_id, finca_id, tipo_producto_id, cantidad_disponible, unidad_medida, precio_unitario,
  fecha_cosecha, activo, creado_en`.
- `tipo_producto`: `id → nombre, categoria_id`. La categoría de un producto se obtiene por
  `producto → tipo_producto → categoria`, por eso no se almacena en `producto`.
- `orden_compra`: `id → comprador_id, agricultor_id, estado, creado_en, actualizado_en`.
- `orden_item`: `(orden_id, producto_id) → cantidad, precio_unitario`.
- Tablas catálogo (`rol`, `municipio`, `categoria`): `id → nombre` y `nombre → id`.

Anomalías que el diseño evita: repetir el nombre del municipio, del rol o de la categoría en cada fila; modificar la
categoría de un tipo de producto en muchos registros; y perder el precio histórico al cambiar el precio del producto.

Dos puntos que conviene justificar ante la docente:

- `orden_item.precio_unitario` no es un dato derivable del producto: es el precio acordado en el momento de la compra,
  y por eso pertenece a la llave completa (`orden_id`, `producto_id`).
- `orden_compra.agricultor_id` es un atributo propio de la orden (a quién se le compra) y no depende de otro atributo no
  clave de la misma tabla; la coherencia con los ítems se valida en `PedidoService`.

En `agricultor` y `usuario` todos los determinantes son llaves (la llave primaria o claves candidatas únicas), de modo que
tampoco hay anomalías de tipo Boyce-Codd.

## 4. Reglas de negocio y dónde se hacen cumplir

| Regla | HU | Base de datos (PostgreSQL) | Aplicación |
|---|---|---|---|
| Cédula única, de 6 a 10 dígitos | 01 | `UNIQUE` + `CHECK` con expresión regular | `@Pattern` en el DTO; el servicio detecta duplicados |
| Correo único y en minúsculas | 01, 13 | `UNIQUE` + `CHECK (correo = LOWER(correo))` | El servicio normaliza antes de guardar |
| Contraseña nunca en texto plano | 01, 13 | `contrasena_hash NOT NULL` | BCrypt en `AgricultorService`; comparación en `AuthService` |
| Municipio válido del Valle | 01, 06 | Llave foránea a `municipio` | El servicio resuelve el nombre; si no existe, error 400 |
| La finca pertenece al agricultor del producto | 02 | Llave foránea compuesta `(finca_id, agricultor_id)` | `ProductoService` valida antes de guardar |
| Cantidad disponible no negativa; precio mayor que cero | 02 | `CHECK` en `producto` | `@Positive` / `@PositiveOrZero` en el DTO |
| Fecha de cosecha no anterior a hoy | 02 | — (`CURRENT_DATE` no es válido en un `CHECK`) | `ProductoService` |
| Unidad de medida permitida | 02 | `CHECK (unidad_medida IN (...))` | `@Pattern` o enumeración en el DTO |
| Cantidad pedida no supera el inventario | 08 | `CHECK (cantidad_disponible >= 0)` como red de seguridad | `PedidoService` valida y descuenta |
| Estados de la orden y sus transiciones | 09, 10, 11 | `CHECK` con los 5 estados | `PedidoService` (PENDIENTE → ALISTADA → DESPACHO_PROGRAMADO) |
| Un comprador no puede comprarse a sí mismo | 08 | `CHECK (comprador_id <> agricultor_id)` | — |
| Una orden pertenece al agricultor que la gestiona | 09, 10 | Llave foránea `agricultor_id` | Consultas `findByIdAndAgricultorId`; si no coincide, 404 |
| Tipos de notificación permitidos | 05, 12 | `CHECK (tipo IN (...))` | Enumeración `TipoNotificacion` |

## 5. Mapeo DDL ↔ entidades JPA (paquete `com.agrovalle.connect.model`)

| Tabla | Entidad | Mapeo principal |
|---|---|---|
| `rol`, `municipio`, `categoria` | `Rol`, `Municipio`, `Categoria` | `@Entity`, `@Table`, `@Id @GeneratedValue(IDENTITY)`, `@Column(unique = true)` en `nombre` |
| `tipo_producto` | `TipoProducto` | `@ManyToOne(fetch = LAZY, optional = false) @JoinColumn(name = "categoria_id")` |
| `usuario` | `Usuario` (abstracta) | `@Inheritance(strategy = JOINED)`; `@ManyToOne` a `Rol`; `Instant creadoEn` con `@PrePersist` |
| `agricultor` | `Agricultor extends Usuario` | `@PrimaryKeyJoinColumn(name = "usuario_id")`; `cedula`; `@ManyToOne Municipio` |
| `comprador` | `Comprador extends Usuario` | `@PrimaryKeyJoinColumn(name = "usuario_id")`; `@ManyToOne Municipio`; `direccionEntrega` |
| `finca` | `Finca` | `@ManyToOne Agricultor`, `@ManyToOne Municipio`; `@Table` con las dos restricciones únicas |
| `producto` | `Producto` | `@ManyToOne Agricultor`, `@ManyToOne(optional = true) Finca`, `@ManyToOne TipoProducto`; `BigDecimal` con `@Column(precision, scale)`; `LocalDate fechaCosecha`; `Boolean activo` |
| `orden_compra` | `OrdenCompra` | `@ManyToOne Comprador`, `@ManyToOne Agricultor`; `@Enumerated(STRING) EstadoOrden`; `@OneToMany(mappedBy = "orden", cascade = ALL)` hacia `OrdenItem`; `@OneToOne(mappedBy = "orden")` hacia `Despacho` |
| `orden_item` | `OrdenItem` | `@EmbeddedId OrdenItemId` con `@MapsId` hacia `OrdenCompra` y `Producto` |
| `despacho` | `Despacho` | `@Id` compartido con `@OneToOne @MapsId OrdenCompra`; `OffsetDateTime`/`Instant` para `fechaHoraProgramada` |
| `mensaje_contacto` | `MensajeContacto` | `@ManyToOne Producto`, `@ManyToOne Comprador` |
| `notificacion` | `Notificacion` | `@ManyToOne Usuario`, `@ManyToOne(optional = true) OrdenCompra`; `@Enumerated(STRING) TipoNotificacion` |

Notas de implementación:

- La jerarquía `Usuario` → `Agricultor` / `Comprador` es la que permite mostrar el principio de sustitución de Liskov
  (los dos perfiles pueden usarse donde se espera un `Usuario`, por ejemplo en `AuthService`).
- La llave foránea compuesta de `producto` solo la hace cumplir la base de datos; JPA mapea `finca` y `agricultor` como
  relaciones separadas.
- Para respetar `MagicNumber` de Checkstyle, las longitudes y precisiones de columna se declaran como constantes.
- `application.properties` mantiene `spring.jpa.hibernate.ddl-auto=validate`: Hibernate no crea tablas, solo comprueba que
  las entidades coincidan con `schema.sql`.

## 6. Impacto sobre el código del Sprint 1

La entidad `Agricultor` implementada hasta ahora guarda también correo y contraseña. Con este modelo esos campos pasan a
`Usuario`, y `Agricultor` conserva la cédula y el municipio. El cambio afecta a `Agricultor`, `AgricultorRepository`,
`AgricultorService`, `AgricultorMapper` y sus pruebas, y se hace antes de implementar HU-13 para no duplicar el acceso
de agricultores y compradores. Como todavía no hay datos reales, no se necesita migración: basta aplicar `schema.sql` a
una base nueva.

## 7. Supuestos por confirmar con el Product Owner

- El backlog no incluye una historia de registro de compradores; se asume que se registran por el mismo
  `POST /api/v1/auth/register` con el rol `COMPRADOR`.
- Los tipos de producto y las categorías iniciales son ejemplos.
- Al crear una orden se descuenta el inventario del producto.
- El municipio de un producto es el de su finca o, si no tiene, el de su agricultor.
