# Product Backlog — AgroValle Connect

> **Primera etapa:** HU-01 a HU-05  
> **Priorización:** MoSCoW (M = Must, S = Should, C = Could, W = Won't)  
> **Estimación:** Story Points (Fibonacci, vía Planning Poker)

## Product Vision Statement

> **Para** los productores del Valle, **que necesitan vender directamente**, AgroValle Connect es una **plataforma web en Java** que **conecta oferta y demanda a precio justo**. A diferencia de los **intermediarios tradicionales**, nuestro producto **garantiza trazabilidad y contratos de API transparentes**.

---

## Resumen del backlog — primera etapa

| ID | Historia de Usuario | Criterios de Aceptación (BDD) | MoSCoW | Story Points |
|---|---|---|:---:|---:|
| HU-01 | Como Agricultor, quiero registrarme en la plataforma para ofrecer mis productos. | Registro mediante REST, validación de datos y persistencia en PostgreSQL. | M | 5 |
| HU-02 | Como Agricultor, quiero publicar mis cosechas para que sean visibles. | Publicación autenticada con JWT, validación de fecha, persistencia e ID único. | M | 5 |
| HU-03 | Como Usuario, quiero ver los precios promedio del Valle para negociar mejor. | Consulta del promedio de transacciones recientes y resultado exacto en COP. | S | 8 |
| HU-04 | Como Comprador, quiero filtrar las cosechas por municipio y categoría, para encontrar productos locales de mi interés rápidamente. | GET con municipio y categoría, respuesta 200 y ofertas activas en JSON. | M | 3 |
| HU-05 | Como Comprador, quiero enviar una solicitud de contacto directo al agricultor, para acordar condiciones de compra y logística. | POST autenticado con JWT, persistencia de la interacción y confirmación de notificación. | M | 5 |

> **Nota de estimación:** los Story Points anteriores son una estimación inicial para organizar el backlog. El equipo debe validarlos mediante Planning Poker, utilizando la escala Fibonacci **1, 2, 3, 5, 8, 13**, antes de cerrar la versión definitiva.

---

# 1. Historias de usuario y especificación BDD

## HU-01 — Registro de agricultores

**Como** agricultor,  
**quiero** registrarme en AgroValle Connect,  
**para** ofrecer mis productos directamente a compradores.

**Prioridad:** Must Have  
**Story Points:** 5

### Escenario 1 — Registro exitoso

```gherkin
Given un usuario que desea registrarse como agricultor
And se encuentra disponible el endpoint POST /api/v1/auth/register
When envía un JSON con nombre, ubicacion_valle y una cédula válida
Then el sistema valida la información recibida
And persiste el agricultor en PostgreSQL
And responde con HTTP 201 Created
```

### Escenario 2 — Datos obligatorios inválidos

```gherkin
Given un usuario que intenta registrarse como agricultor
When envía datos obligatorios incompletos o inválidos
Then el sistema rechaza la solicitud
And responde con HTTP 400 Bad Request
And no crea el registro del agricultor
```

### Contrato REST inicial

- **Método:** POST
- **Endpoint:** `/api/v1/auth/register`
- **Resultado exitoso:** `201 Created`
- **Persistencia:** PostgreSQL

---

## HU-02 — Publicación de productos

**Como** agricultor,  
**quiero** publicar mis productos disponibles,  
**para** que los compradores puedan conocer mi oferta.

**Prioridad:** Must Have  
**Story Points:** 5

### Escenario 1 — Publicación exitosa

```gherkin
Given un agricultor autenticado mediante JWT
And dispone de permisos para publicar una oferta
When envía una solicitud para publicar un producto con tipo, cantidad y fecha_cosecha
Then el sistema valida los datos
And verifica que la fecha de cosecha no sea anterior a la fecha actual
And persiste la oferta en PostgreSQL
And genera un identificador único para el producto
And responde con HTTP 201 Created
```

### Escenario 2 — Fecha de cosecha inválida

```gherkin
Given un agricultor autenticado mediante JWT
When intenta publicar un producto con una fecha_cosecha anterior a la fecha actual
Then el sistema rechaza la publicación
And responde con HTTP 400 Bad Request
And no persiste la oferta
```

### Escenario 3 — Usuario no autenticado

```gherkin
Given un usuario sin un JWT válido
When intenta publicar un producto
Then el sistema rechaza la solicitud
And responde con HTTP 401 Unauthorized
```

### Contrato REST inicial

- **Método:** POST
- **Endpoint:** `/api/v1/productos`
- **Autenticación:** JWT
- **Resultado exitoso:** `201 Created`
- **Persistencia:** PostgreSQL

---

## HU-03 — Visualización de precios regionales

**Como** usuario,  
**quiero** consultar el precio promedio de productos agrícolas en el Valle,  
**para** contar con una referencia de precios regionales antes de realizar una negociación.

**Prioridad:** Should Have  
**Story Points:** 8

### Escenario 1 — Consulta de precio promedio

```gherkin
Given existen 50 transacciones de "Café" registradas durante las últimas 24 horas
And las transacciones contienen valores expresados en COP
When el usuario consulta el precio promedio regional de "Café"
Then el sistema calcula la media aritmética de las transacciones
And responde con HTTP 200 OK
And retorna el valor promedio exacto en COP
```

### Escenario 2 — Producto sin transacciones recientes

```gherkin
Given no existen transacciones recientes para el producto consultado
When el usuario solicita su precio promedio regional
Then el sistema responde con HTTP 200 OK
And informa que no existen datos suficientes para calcular el promedio
```

### Contrato REST inicial

- **Método:** GET
- **Endpoint sugerido:** `/api/v1/precios/promedio?producto=Cafe`
- **Resultado exitoso:** `200 OK`
- **Unidad monetaria:** COP
- **Fuente de datos:** transacciones almacenadas en PostgreSQL

---

## HU-04 — Filtro de categorías y municipios del Valle

**Como** comprador,  
**quiero** filtrar los productos por municipio y categoría,  
**para** encontrar ofertas locales que se ajusten a mis necesidades.

**Prioridad:** Must Have  
**Story Points:** 3

### Escenario 1 — Filtrado por municipio y categoría

```gherkin
Given existen productos activos almacenados en PostgreSQL
And algunos productos pertenecen al municipio Dagua
And algunos productos pertenecen a la categoría Frutas
When el comprador consulta GET /api/v1/productos?municipio=Dagua&categoria=Frutas
Then el sistema responde con HTTP 200 OK
And retorna un arreglo JSON
And los resultados corresponden únicamente a ofertas activas de Dagua y de la categoría Frutas
```

### Escenario 2 — Sin resultados coincidentes

```gherkin
Given no existen ofertas activas que coincidan con el municipio y la categoría solicitados
When el comprador realiza la consulta de productos
Then el sistema responde con HTTP 200 OK
And retorna un arreglo JSON vacío
```

### Contrato REST inicial

- **Método:** GET
- **Endpoint:** `/api/v1/productos?municipio={municipio}&categoria={categoria}`
- **Resultado exitoso:** `200 OK`
- **Persistencia:** PostgreSQL
- **Ejemplos de municipios:** Dagua, Palmira, Buga

---

## HU-05 — Contacto directo / intención de compra

**Como** comprador,  
**quiero** enviar una solicitud de contacto al agricultor de una oferta activa,  
**para** acordar directamente las condiciones de compra y logística.

**Prioridad:** Must Have  
**Story Points:** 5

### Escenario 1 — Envío exitoso de intención de compra

```gherkin
Given un comprador autenticado mediante JWT
And existe una oferta activa identificada por id_producto
When envía POST /api/v1/contacto/mensaje con id_producto y un mensaje de negociación
Then el sistema valida la solicitud
And persiste la interacción en PostgreSQL
And responde con HTTP 200 OK
And confirma que la notificación fue enviada al agricultor
```

### Escenario 2 — Oferta inexistente o inactiva

```gherkin
Given un comprador autenticado mediante JWT
When intenta enviar un mensaje asociado a una oferta inexistente o inactiva
Then el sistema rechaza la solicitud
And responde con HTTP 404 Not Found
And no persiste la interacción
```

### Escenario 3 — Usuario no autenticado

```gherkin
Given un usuario sin un JWT válido
When intenta enviar una intención de compra
Then el sistema rechaza la solicitud
And responde con HTTP 401 Unauthorized
```

### Contrato REST inicial

- **Método:** POST
- **Endpoint:** `/api/v1/contacto/mensaje`
- **Autenticación:** JWT
- **Resultado exitoso:** `200 OK`
- **Persistencia:** PostgreSQL

---
HU-13 — Inicio de sesión con JWT

Como usuario registrado,
quiero iniciar sesión mediante mis credenciales,
para acceder de forma segura a las funcionalidades de AgroValle Connect.

Prioridad: Must Have
Story Points: 5 (estimación inicial; pendiente de validación mediante Planning Poker)

BDD
Escenario 1 — Inicio de sesión exitoso

Given un usuario registrado
And proporciona credenciales válidas

When solicita el inicio de sesión

Then el sistema valida las credenciales
And genera un token JWT
And responde con HTTP 200 OK.

Escenario 2 — Credenciales inválidas

Given un usuario registrado
When proporciona credenciales incorrectas

Then el sistema rechaza el inicio de sesión
And responde con HTTP 401 Unauthorized.

Contrato REST

Método: POST
Endpoint: /api/v1/auth/login
Autenticación: Credenciales de usuario
Resultado exitoso: 200 OK
Persistencia: PostgreSQL

INVEST — HU-13
Independent: puede desarrollarse como una funcionalidad específica del módulo de autenticación.
Negotiable: los detalles del mecanismo de autenticación pueden refinarse.
Valuable: permite controlar el acceso seguro a la plataforma.
Estimable: el alcance está delimitado a validar credenciales y generar JWT.
Small: se concentra en una operación de autenticación.
Testable: contempla credenciales válidas e inválidas con respuestas HTTP verificables.

HU-14 — Historial de transacciones
Como comprador,
quiero consultar mi historial de transacciones,
para revisar las compras realizadas anteriormente en la plataforma.

Prioridad: Should Have
Story Points: 5 (estimación inicial; pendiente de validación mediante Planning Poker)

BDD
Escenario 1 — Historial disponible
Given un comprador autenticado mediante JWT
And existen transacciones asociadas a su cuenta

When consulta su historial

Then el sistema retorna las transacciones correspondientes
And responde con HTTP 200 OK
And obtiene la información desde PostgreSQL.

Escenario 2 — Sin transacciones
Given un comprador autenticado mediante JWT
And no existen transacciones asociadas

When consulta su historial

Then el sistema responde con HTTP 200 OK
And retorna un arreglo JSON vacío.

Contrato REST
Método: GET
Endpoint: /api/v1/transacciones/historial
Autenticación: JWT
Resultado exitoso: 200 OK
Persistencia: PostgreSQL

INVEST — HU-14
Independent: puede implementarse como una consulta independiente sobre las transacciones.

Negotiable: la información y filtros del historial pueden refinarse.

Valuable: permite consultar las operaciones realizadas previamente.

Estimable: el alcance está delimitado a consultar las transacciones del usuario.

Small: corresponde a una funcionalidad concreta de consulta.

Testable: contempla historial disponible y ausencia de transacciones.

HU-15 — Búsqueda por rango de precio
Como comprador,
quiero buscar productos dentro de un rango de precio,
para encontrar ofertas que se ajusten a mi presupuesto.

Prioridad: Should Have
Story Points: 5 (estimación inicial; pendiente de validación mediante Planning Poker)

BDD
Escenario 1 — Productos encontrados
Given existen ofertas activas de productos con diferentes precios
And un comprador consulta un rango de precio válido

When realiza la búsqueda indicando precio mínimo y máximo

Then el sistema retorna las ofertas cuyo precio está dentro del rango indicado
And responde con HTTP 200 OK
And obtiene las ofertas desde PostgreSQL.

Escenario 2 — Sin resultados
Given un comprador autenticado mediante JWT
And no existen ofertas dentro del rango solicitado

When realiza la búsqueda

Then el sistema responde con HTTP 200 OK
And retorna un arreglo JSON vacío.

Escenario 3 — Rango inválido
Given un comprador autenticado mediante JWT

When proporciona un precio mínimo superior al precio máximo

Then el sistema rechaza la solicitud
And responde con HTTP 400 Bad Request.

Contrato REST
Método: GET
Endpoint: /api/v1/productos?precioMin={precioMin}&precioMax={precioMax}
Autenticación: JWT
Resultado exitoso: 200 OK
Persistencia: PostgreSQL

INVEST — HU-15
Independent: puede implementarse como un filtro adicional sobre el catálogo.

Negotiable: los detalles del filtro y presentación de resultados pueden ajustarse.

Valuable: ayuda al comprador a encontrar productos según su presupuesto.

Estimable: el alcance está delimitado al filtrado por precio.

Small: se concentra en una funcionalidad concreta de búsqueda.

Testable: contempla resultados, ausencia de resultados y rangos inválidos.

# 2. Auditoría INVEST — primera etapa

| HU | Independent | Negotiable | Valuable | Estimable | Small | Testable |
|---|:---:|:---:|:---:|:---:|:---:|:---:|
| HU-01 | ✓ | ✓ | ✓ | ✓ | ✓ | ✓ |
| HU-02 | ✓ | ✓ | ✓ | ✓ | ✓ | ✓ |
| HU-03 | ✓ | ✓ | ✓ | ✓ | ✓ | ✓ |
| HU-04 | ✓ | ✓ | ✓ | ✓ | ✓ | ✓ |
| HU-05 | ✓ | ✓ | ✓ | ✓ | ✓ | ✓ |

### Justificación INVEST

- **Independent:** cada historia representa una capacidad funcional identificable y puede validarse de manera separada.
- **Negotiable:** los criterios definen el resultado esperado, pero no obligan una implementación interna específica.
- **Valuable:** cada historia aporta valor a agricultores, compradores o usuarios.
- **Estimable:** cada historia tiene Story Points iniciales y puede ser discutida por el equipo mediante Planning Poker.
- **Small:** cada historia está delimitada a una capacidad funcional concreta.
- **Testable:** cada historia contiene escenarios Given-When-Then con resultados verificables mediante API, códigos HTTP y/o persistencia.

---

# 3. Estado de esta etapa

Esta versión contiene únicamente **HU-01 a HU-05**, para ser utilizada como primera entrega funcional del backlog dentro del trabajo colaborativo.

Las **HU-06 a HU-15** se incorporarán posteriormente por los demás integrantes, conservando el mismo estándar:

1. Historia de usuario.
2. Prioridad MoSCoW.
3. Story Points Fibonacci.
4. Escenarios Given-When-Then.
5. Endpoint REST y autenticación cuando corresponda.
6. Persistencia o resultado esperado.
7. Auditoría INVEST.

> **Importante:** la versión final de Sprint 0 deberá integrar las 15 historias de usuario exigidas por la guía de trabajo.
