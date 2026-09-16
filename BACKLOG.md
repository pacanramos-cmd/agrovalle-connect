# Product Backlog --- AgroValle Connect

> **Primera etapa:** HU-01 a HU-15\
> **Priorización:** MoSCoW (M = Must, S = Should, C = Could, W = Won't)\
> **Estimación:** Story Points (Fibonacci, vía Planning Poker)

## Product Vision Statement

> **Para** los productores del Valle, **que necesitan vender
> directamente**, AgroValle Connect es una **plataforma web en Java**
> que **conecta oferta y demanda a precio justo**. A diferencia de los
> **intermediarios tradicionales**, nuestro producto **garantiza
> trazabilidad y contratos de API transparentes**.

------------------------------------------------------------------------

## Resumen del backlog --- HU-01 a HU-15

  -----------------------------------------------------------------------------------
  ID          Historia de       Criterios de            MoSCoW           Story Points
              Usuario           Aceptación (BDD)                      
  ----------- ----------------- ----------------- ------------------- ---------------
  HU-01       Como Agricultor,  Registro mediante          M                        5
              quiero            REST, validación                      
              registrarme en la de datos y                            
              plataforma para   persistencia en                       
              ofrecer mis       PostgreSQL.                           
              productos.                                              

  HU-02       Como Agricultor,  Publicación                M                        5
              quiero publicar   autenticada con                       
              mis cosechas para JWT, validación                       
              que sean          de fecha,                             
              visibles.         persistencia e ID                     
                                único.                                

  HU-03       Como Usuario,     Consulta del               S                        8
              quiero ver los    promedio de                           
              precios promedio  transacciones                         
              del Valle para    recientes y                           
              negociar mejor.   resultado exacto                      
                                en COP.                               

  HU-04       Como Comprador,   GET con municipio          M                        3
              quiero filtrar    y categoría,                          
              las cosechas por  respuesta 200 y                       
              municipio y       ofertas activas                       
              categoría, para   en JSON.                              
              encontrar                                               
              productos locales                                       
              de mi interés                                           
              rápidamente.                                            

  HU-05       Como Comprador,   POST autenticado           M                        5
              quiero enviar una con JWT,                              
              solicitud de      persistencia de                       
              contacto directo  la interacción y                      
              al agricultor,    confirmación de                       
              para acordar      notificación.                         
              condiciones de                                          
              compra y                                                
              logística.                                              

  HU-06       Como Agricultor,  Registro                   S                        3
              quiero registrar  autenticado de                        
              una finca con su  finca, validación                     
              información       de datos,                             
              básica y          persistencia e ID                     
              ubicación, para   único.                                
              asociar mis                                             
              productos a un                                          
              lugar de                                                
              producción y                                            
              facilitar su                                            
              trazabilidad.                                           

  HU-07       Como Agricultor,  Consulta                   M                        3
              quiero consultar  autenticada del                       
              el inventario     inventario y                          
              disponible de mis cantidades                            
              productos, para   disponibles.                          
              conocer las                                             
              cantidades                                              
              disponibles antes                                       
              de aceptar o                                            
              gestionar                                               
              pedidos.                                                

  HU-08       Como Comprador,   Creación                   M                        8
              quiero crear una  autenticada de                        
              orden de compra   orden, validación                     
              seleccionando     de                                    
              productos         disponibilidad,                       
              disponibles, para persistencia e ID                     
              solicitar         único.                                
              formalmente los                                         
              productos que                                           
              deseo adquirir.                                         

  HU-09       Como Agricultor,  Actualización              M                        5
              quiero confirmar  autenticada del                       
              el alistamiento   estado de                             
              de una orden de   alistamiento y                        
              compra, para      persistencia.                         
              informar que los                                        
              productos están                                         
              preparados para                                         
              continuar con el                                        
              proceso                                                 
              logístico.                                              

  HU-10       Como agricultor,  Programación               M                        5
              quiero programar  autenticada del                       
              el despacho de un despacho,                             
              pedido            actualización del                     
              confirmado, para  pedido y                              
              coordinar la      persistencia en                       
              entrega de los    PostgreSQL.                           
              productos con el                                        
              comprador.                                              

  HU-11       Como comprador,   Consulta                   M                        5
              quiero consultar  autenticada del                       
              el estado de mi   estado del pedido                     
              pedido, para      y respuesta con                       
              conocer el avance información                           
              de la preparación actualizada.                          
              y entrega de los                                        
              productos.                                              

  HU-12       Como agricultor,  Registro de la             S                        5
              quiero recibir    notificación                          
              una notificación  asociada al                           
              cuando se genere  pedido y consulta                     
              o actualice un    autenticada de                        
              pedido            notificaciones.                       
              relacionado con                                         
              mis productos,                                          
              para conocer                                            
              oportunamente los                                       
              cambios que                                             
              requieren mi                                            
              atención.                                               

  HU-13       Como usuario      Validación de              M                        5
              registrado,       credenciales,                         
              quiero iniciar    generación de JWT                     
              sesión mediante   y respuesta HTTP                      
              mis credenciales, 200; credenciales                     
              para acceder de   inválidas                             
              forma segura a    retornan 401.                         
              las                                                     
              funcionalidades                                         
              de AgroValle                                            
              Connect.                                                

  HU-14       Como comprador,   Consulta                   S                        5
              quiero consultar  autenticada del                       
              mi historial de   historial y                           
              transacciones,    respuesta 200,                        
              para revisar las  incluyendo                            
              compras           arreglo vacío si                      
              realizadas        no existen                            
              anteriormente en  transacciones.                        
              la plataforma.                                          

  HU-15       Como comprador,   Filtrado                   S                        5
              quiero buscar     autenticado por                       
              productos dentro  precio mínimo y                       
              de un rango de    máximo, respuesta                     
              precio, para      200 o 400 si el                       
              encontrar ofertas rango es                              
              que se ajusten a  inválido.                             
              mi presupuesto.                                         
  -----------------------------------------------------------------------------------

> **Nota de estimación:** los Story Points anteriores son una estimación
> inicial para organizar el backlog. El equipo debe validarlos mediante
> Planning Poker, utilizando la escala Fibonacci **1, 2, 3, 5, 8, 13**,
> antes de cerrar la versión definitiva.

# 1. Historias de usuario y especificación BDD

**\## HU-01 --- Registro de agricultores**

\*\*\*\*Como\*\*\*\* agricultor,  

\*\*\*\*quiero\*\*\*\* registrarme en AgroValle Connect,  

\*\*\*\*para\*\*\*\* ofrecer mis productos directamente a compradores.

\*\*\*\*Prioridad:\*\*\*\* Must Have  

\*\*\*\*Story Points:\*\*\*\* 5

**\### Escenario 1 --- Registro exitoso**

``` gherkin

Given un usuario que desea registrarse como agricultor

And se encuentra disponible el endpoint POST /api/v1/auth/register

When envía un JSON con nombre, ubicacion_valle y una cédula válida

Then el sistema valida la información recibida

And persiste el agricultor en PostgreSQL

And responde con HTTP 201 Created
```

**\### Escenario 2 --- Datos obligatorios inválidos**

``` gherkin

Given un usuario que intenta registrarse como agricultor

When envía datos obligatorios incompletos o inválidos

Then el sistema rechaza la solicitud

And responde con HTTP 400 Bad Request

And no crea el registro del agricultor
```

**\### Contrato REST inicial**

-   \*\*\*\*Método:\*\*\*\* POST

-   \*\*\*\*Endpoint:\*\*\*\* `/api/v1/auth/register`

-   \*\*\*\*Resultado exitoso:\*\*\*\* `201 Created`

-   \*\*\*\*Persistencia:\*\*\*\* PostgreSQL

**---**

------------------------------------------------------------------------

**\## HU-02 --- Publicación de productos**

\*\*\*\*Como\*\*\*\* agricultor,  

\*\*\*\*quiero\*\*\*\* publicar mis productos disponibles,  

\*\*\*\*para\*\*\*\* que los compradores puedan conocer mi oferta.

\*\*\*\*Prioridad:\*\*\*\* Must Have  

\*\*\*\*Story Points:\*\*\*\* 5

**\### Escenario 1 --- Publicación exitosa**

``` gherkin

Given un agricultor autenticado mediante JWT

And dispone de permisos para publicar una oferta

When envía una solicitud para publicar un producto con tipo, cantidad y fecha_cosecha

Then el sistema valida los datos

And verifica que la fecha de cosecha no sea anterior a la fecha actual

And persiste la oferta en PostgreSQL

And genera un identificador único para el producto

And responde con HTTP 201 Created
```

**\### Escenario 2 --- Fecha de cosecha inválida**

``` gherkin

Given un agricultor autenticado mediante JWT

When intenta publicar un producto con una fecha_cosecha anterior a la fecha actual

Then el sistema rechaza la publicación

And responde con HTTP 400 Bad Request

And no persiste la oferta
```

**\### Escenario 3 --- Usuario no autenticado**

``` gherkin

Given un usuario sin un JWT válido

When intenta publicar un producto

Then el sistema rechaza la solicitud

And responde con HTTP 401 Unauthorized
```

**\### Contrato REST inicial**

-   \*\*\*\*Método:\*\*\*\* POST

-   \*\*\*\*Endpoint:\*\*\*\* `/api/v1/productos`

-   \*\*\*\*Autenticación:\*\*\*\* JWT

-   \*\*\*\*Resultado exitoso:\*\*\*\* `201 Created`

-   \*\*\*\*Persistencia:\*\*\*\* PostgreSQL

**---**

------------------------------------------------------------------------

**\## HU-03 --- Visualización de precios regionales**

\*\*\*\*Como\*\*\*\* usuario,  

\*\*\*\*quiero\*\*\*\* consultar el precio promedio de productos
agrícolas en el Valle,  

\*\*\*\*para\*\*\*\* contar con una referencia de precios regionales
antes de realizar una negociación.

\*\*\*\*Prioridad:\*\*\*\* Should Have  

\*\*\*\*Story Points:\*\*\*\* 8

**\### Escenario 1 --- Consulta de precio promedio**

``` gherkin

Given existen 50 transacciones de "Café" registradas durante las últimas 24 horas

And las transacciones contienen valores expresados en COP

When el usuario consulta el precio promedio regional de "Café"

Then el sistema calcula la media aritmética de las transacciones

And responde con HTTP 200 OK

And retorna el valor promedio exacto en COP
```

**\### Escenario 2 --- Producto sin transacciones recientes**

``` gherkin

Given no existen transacciones recientes para el producto consultado

When el usuario solicita su precio promedio regional

Then el sistema responde con HTTP 200 OK

And informa que no existen datos suficientes para calcular el promedio
```

**\### Contrato REST inicial**

-   \*\*\*\*Método:\*\*\*\* GET

-   \*\*\*\*Endpoint sugerido:\*\*\*\*
    `/api/v1/precios/promedio?producto=Cafe`

-   \*\*\*\*Resultado exitoso:\*\*\*\* `200 OK`

-   \*\*\*\*Unidad monetaria:\*\*\*\* COP

-   \*\*\*\*Fuente de datos:\*\*\*\* transacciones almacenadas en
    PostgreSQL

**---**

------------------------------------------------------------------------

**\## HU-04 --- Filtro de categorías y municipios del Valle**

\*\*\*\*Como\*\*\*\* comprador,  

\*\*\*\*quiero\*\*\*\* filtrar los productos por municipio y categoría,
 

\*\*\*\*para\*\*\*\* encontrar ofertas locales que se ajusten a mis
necesidades.

\*\*\*\*Prioridad:\*\*\*\* Must Have  

\*\*\*\*Story Points:\*\*\*\* 3

**\### Escenario 1 --- Filtrado por municipio y categoría**

``` gherkin

Given existen productos activos almacenados en PostgreSQL

And algunos productos pertenecen al municipio Dagua

And algunos productos pertenecen a la categoría Frutas

When el comprador consulta GET /api/v1/productos?municipio=Dagua&categoria=Frutas

Then el sistema responde con HTTP 200 OK

And retorna un arreglo JSON

And los resultados corresponden únicamente a ofertas activas de Dagua y de la categoría Frutas
```

**\### Escenario 2 --- Sin resultados coincidentes**

``` gherkin

Given no existen ofertas activas que coincidan con el municipio y la categoría solicitados

When el comprador realiza la consulta de productos

Then el sistema responde con HTTP 200 OK

And retorna un arreglo JSON vacío
```

**\### Contrato REST inicial**

-   \*\*\*\*Método:\*\*\*\* GET

-   \*\*\*\*Endpoint:\*\*\*\*
    `/api/v1/productos?municipio={municipio}&categoria={categoria}`

-   \*\*\*\*Resultado exitoso:\*\*\*\* `200 OK`

-   \*\*\*\*Persistencia:\*\*\*\* PostgreSQL

-   \*\*\*\*Ejemplos de municipios:\*\*\*\* Dagua, Palmira, Buga

**---**

------------------------------------------------------------------------

**\## HU-05 --- Contacto directo / intención de compra**

\*\*\*\*Como\*\*\*\* comprador,  

\*\*\*\*quiero\*\*\*\* enviar una solicitud de contacto al agricultor de
una oferta activa,  

\*\*\*\*para\*\*\*\* acordar directamente las condiciones de compra y
logística.

\*\*\*\*Prioridad:\*\*\*\* Must Have  

\*\*\*\*Story Points:\*\*\*\* 5

**\### Escenario 1 --- Envío exitoso de intención de compra**

``` gherkin

Given un comprador autenticado mediante JWT

And existe una oferta activa identificada por id_producto

When envía POST /api/v1/contacto/mensaje con id_producto y un mensaje de negociación

Then el sistema valida la solicitud

And persiste la interacción en PostgreSQL

And responde con HTTP 200 OK

And confirma que la notificación fue enviada al agricultor
```

**\### Escenario 2 --- Oferta inexistente o inactiva**

``` gherkin

Given un comprador autenticado mediante JWT

When intenta enviar un mensaje asociado a una oferta inexistente o inactiva

Then el sistema rechaza la solicitud

And responde con HTTP 404 Not Found

And no persiste la interacción
```

**\### Escenario 3 --- Usuario no autenticado**

``` gherkin

Given un usuario sin un JWT válido

When intenta enviar una intención de compra

Then el sistema rechaza la solicitud

And responde con HTTP 401 Unauthorized
```

**\### Contrato REST inicial**

-   \*\*\*\*Método:\*\*\*\* POST

<<<<<<< Updated upstream
---

## HU-06 — Registro de finca

**Como** agricultor,

**quiero** registrar una finca con su información básica y ubicación,

**para** asociar mis productos a un lugar de producción y facilitar su trazabilidad.

**Prioridad:** Should Have

**Story Points:** 3

### Escenario 1 — Registro exitoso

```gherkin
=======
-   \*\*\*\*Endpoint:\*\*\*\* `/api/v1/contacto/mensaje`

-   \*\*\*\*Autenticación:\*\*\*\* JWT

-   \*\*\*\*Resultado exitoso:\*\*\*\* `200 OK`

-   \*\*\*\*Persistencia:\*\*\*\* PostgreSQL

**---**

------------------------------------------------------------------------

**\## HU-06 --- Registro de finca**

\*\*\*\*Como\*\*\*\* agricultor,

\*\*\*\*quiero\*\*\*\* registrar una finca con su información básica y
ubicación,

\*\*\*\*para\*\*\*\* asociar mis productos a un lugar de producción y
facilitar su trazabilidad.

\*\*\*\*Prioridad:\*\*\*\* Should Have

\*\*\*\*Story Points:\*\*\*\* 3

**\### Escenario 1 --- Registro exitoso**

``` gherkin

>>>>>>> Stashed changes
Given un agricultor autenticado mediante JWT

And dispone de una cuenta registrada en AgroValle Connect

When registra una finca proporcionando nombre, municipio y ubicación

Then el sistema valida la información recibida

And persiste la finca en PostgreSQL

And genera un identificador único para la finca

And responde con HTTP 201 Created
```

### Escenario 2 --- Datos obligatorios inválidos

\`\`\`gherkin

Given un agricultor autenticado mediante JWT

When intenta registrar una finca sin completar los datos obligatorios

Then el sistema rechaza la solicitud

And responde con HTTP 400 Bad Request

And no crea el registro de la finca

Contrato REST inicial

Método: POST

Endpoint: /api/v1/fincas

Autenticación: JWT

Resultado exitoso: 201 Created

Persistencia: PostgreSQL

------------------------------------------------------------------------

------------------------------------------------------------------------

## HU-07 --- Consulta de inventario

Como agricultor,

quiero consultar el inventario disponible de mis productos,

para conocer las cantidades disponibles antes de aceptar o gestionar
pedidos.

Prioridad: Must Have

Story Points: 3

### Escenario 1 --- Consulta exitosa

\`\`\`gherkin

Given un agricultor autenticado mediante JWT

And existen productos publicados asociados a su cuenta

When consulta su inventario

Then el sistema responde con HTTP 200 OK

And retorna un arreglo JSON con los productos y sus cantidades
disponibles

### Escenario 2 --- Inventario sin productos disponibles

\`\`\`gherkin

Given un agricultor autenticado mediante JWT

And no existen productos disponibles en su inventario

When consulta su inventario

Then el sistema responde con HTTP 200 OK

And retorna un arreglo JSON vacío

Contrato REST inicial

Método: GET

Endpoint: /api/v1/inventario

Autenticación: JWT

Resultado exitoso: 200 OK

Fuente de datos: PostgreSQL

------------------------------------------------------------------------

------------------------------------------------------------------------

## HU-08 --- Creación de orden de compra

Como comprador,

quiero crear una orden de compra seleccionando productos disponibles,

para solicitar formalmente los productos que deseo adquirir.

Prioridad: Must Have

Story Points: 8

### Escenario 1 --- Creación exitosa

\`\`\`gherkin

Given un comprador autenticado mediante JWT

And existe una oferta activa con cantidad disponible

When envía una solicitud para crear una orden indicando el producto y la
cantidad solicitada

Then el sistema valida la disponibilidad del producto

And registra la orden de compra en PostgreSQL

And genera un identificador único para la orden

And responde con HTTP 201 Created

### Escenario 2 --- Cantidad superior al stock disponible

\`\`\`gherkin

Given un comprador autenticado mediante JWT

And existe una oferta activa con una cantidad disponible determinada

When solicita una cantidad superior al stock disponible

Then el sistema rechaza la solicitud

And responde con HTTP 400 Bad Request

And no crea la orden de compra

### Escenario 3 --- Usuario no autenticado

\`\`\`gherkin

Given un usuario sin un JWT válido

When intenta crear una orden de compra

Then el sistema rechaza la solicitud

And responde con HTTP 401 Unauthorized

Contrato REST inicial

Método: POST

Endpoint: /api/v1/pedidos

Autenticación: JWT

Resultado exitoso: 201 Created

Persistencia: PostgreSQL

------------------------------------------------------------------------

------------------------------------------------------------------------

## HU-09 --- Confirmación de alistamiento

Como agricultor,

quiero confirmar el alistamiento de una orden de compra,

para informar que los productos están preparados para continuar con el
proceso logístico.

Prioridad: Must Have

Story Points: 5

### Escenario 1 --- Confirmación exitosa

\`\`\`gherkin

Given un agricultor autenticado mediante JWT

And existe una orden de compra asociada a uno de sus productos

And la orden se encuentra pendiente de alistamiento

When confirma que la orden está lista

Then el sistema actualiza el estado de la orden

And registra la actualización en PostgreSQL

And responde con HTTP 200 OK

### Escenario 2 --- Orden inexistente o no asociada

\`\`\`gherkin

Given un agricultor autenticado mediante JWT

When intenta confirmar una orden inexistente o que no pertenece a sus
productos

Then el sistema rechaza la solicitud

And responde con HTTP 404 Not Found

And no modifica ninguna orden

### Escenario 3 --- Usuario no autenticado

\`\`\`gherkin

Given un usuario sin un JWT válido

When intenta confirmar el alistamiento de una orden

Then el sistema rechaza la solicitud

And responde con HTTP 401 Unauthorized

Contrato REST inicial

Método: PATCH

Endpoint: /api/v1/pedidos/{id_pedido}/alistamiento

Autenticación: JWT

Resultado exitoso: 200 OK

Persistencia: PostgreSQL

<<<<<<< Updated upstream
---
=======
------------------------------------------------------------------------

------------------------------------------------------------------------

## HU-10 --- Programación de despacho

Como agricultor,

quiero programar el despacho de un pedido confirmado,

para coordinar la entrega de los productos con el comprador.

Prioridad: Must Have

Story Points: 5 (estimación inicial; pendiente de validación mediante
Planning Poker)

### BDD

Escenario 1 --- Programación exitosa

Given un agricultor autenticado mediante JWT

And existe un pedido confirmado asociado a uno de sus productos

When registra la fecha y hora de despacho

Then el sistema valida la información recibida

And actualiza la programación del pedido

And persiste la información en PostgreSQL

And responde con HTTP 200 OK.

Escenario 2 --- Pedido no disponible

Given un agricultor autenticado mediante JWT

When intenta programar el despacho de un pedido inexistente

Then el sistema rechaza la solicitud

And responde con HTTP 404 Not Found

And no modifica ningún registro.

### Contrato REST

Método: PATCH

Endpoint: /api/v1/pedidos/{id_pedido}/despacho

Autenticación: JWT

Resultado exitoso: 200 OK

Persistencia: PostgreSQL

### INVEST --- HU-10

Independent: puede desarrollarse como una operación específica posterior
a la confirmación del pedido.

Negotiable: los detalles de fecha y hora de despacho pueden ajustarse
durante el desarrollo.

Valuable: permite coordinar el proceso de entrega de los productos.

Estimable: el alcance está delimitado a programar el despacho de un
pedido.

Small: se concentra en actualizar la programación de una orden.

Testable: cuenta con escenarios BDD y respuestas HTTP verificables.

------------------------------------------------------------------------

## HU-11 --- Seguimiento de pedido

Como comprador,

quiero consultar el estado de mi pedido,

para conocer el avance de la preparación y entrega de los productos.

Prioridad: Must Have

Story Points: 5 (estimación inicial; pendiente de validación mediante
Planning Poker)

### BDD

Escenario 1 --- Consulta exitosa

Given un comprador autenticado mediante JWT

And existe un pedido asociado a su cuenta

When consulta el estado del pedido

Then el sistema retorna la información actualizada del pedido

And responde con HTTP 200 OK

And obtiene la información desde PostgreSQL.

Escenario 2 --- Pedido inexistente

Given un comprador autenticado mediante JWT

When consulta un pedido inexistente

Then el sistema responde con HTTP 404 Not Found

And no retorna información de otro pedido.

### Contrato REST

Método: GET

Endpoint: /api/v1/pedidos/{id_pedido}/seguimiento

Autenticación: JWT

Resultado exitoso: 200 OK

Persistencia: PostgreSQL

### INVEST --- HU-11

Independent: puede implementarse como una consulta independiente sobre
los pedidos.

Negotiable: la información mostrada en el seguimiento puede refinarse.

Valuable: permite al comprador conocer el avance de su pedido.

Estimable: el alcance está delimitado a consultar el estado de una
orden.

Small: corresponde principalmente a una operación de consulta.

Testable: contempla escenarios de consulta exitosa y pedido inexistente.

------------------------------------------------------------------------

## HU-12 --- Notificación al agricultor

Como agricultor,

quiero recibir una notificación cuando se genere o actualice un pedido
relacionado con mis productos,

para conocer oportunamente los cambios que requieren mi atención.

Prioridad: Should Have

Story Points: 5 (estimación inicial; pendiente de validación mediante
Planning Poker)

### BDD

Escenario 1 --- Notificación generada

Given un agricultor autenticado mediante JWT

And existe un pedido relacionado con uno de sus productos

When se genera una actualización relevante del pedido

Then el sistema registra la notificación correspondiente

And persiste la información en PostgreSQL

And responde con HTTP 200 OK.

Escenario 2 --- Usuario no autenticado

Given un usuario sin JWT válido

When intenta consultar sus notificaciones

Then el sistema rechaza la solicitud

And responde con HTTP 401 Unauthorized.

### Contrato REST

Método: GET

Endpoint: /api/v1/notificaciones

Autenticación: JWT

Resultado exitoso: 200 OK

Persistencia: PostgreSQL

### INVEST --- HU-12

Independent: puede desarrollarse como una funcionalidad diferenciada del
procesamiento de pedidos.

Negotiable: el contenido y presentación de las notificaciones pueden
ajustarse.

Valuable: informa oportunamente al agricultor sobre cambios relevantes.

Estimable: el alcance está delimitado al registro y consulta de
notificaciones.

Small: se concentra en la gestión de notificaciones.

Testable: contempla escenarios de generación y acceso no autorizado.

------------------------------------------------------------------------

## HU-13 --- Inicio de sesión con JWT

Como usuario registrado,

quiero iniciar sesión mediante mis credenciales,

para acceder de forma segura a las funcionalidades de AgroValle Connect.

Prioridad: Must Have

Story Points: 5 (estimación inicial; pendiente de validación mediante
Planning Poker)

### BDD

Escenario 1 --- Inicio de sesión exitoso

Given un usuario registrado

And proporciona credenciales válidas

When solicita el inicio de sesión

Then el sistema valida las credenciales

And genera un token JWT

And responde con HTTP 200 OK.

Escenario 2 --- Credenciales inválidas

Given un usuario registrado

When proporciona credenciales incorrectas

Then el sistema rechaza el inicio de sesión

And responde con HTTP 401 Unauthorized.

### Contrato REST

Método: POST

Endpoint: /api/v1/auth/login

Autenticación: Credenciales de usuario

Resultado exitoso: 200 OK

Persistencia: PostgreSQL

### INVEST --- HU-13

Independent: puede desarrollarse como una funcionalidad específica del
módulo de autenticación.

Negotiable: los detalles del mecanismo de autenticación pueden
refinarse.

Valuable: permite controlar el acceso seguro a la plataforma.

Estimable: el alcance está delimitado a validar credenciales y generar
JWT.

Small: se concentra en una operación de autenticación.

Testable: contempla credenciales válidas e inválidas con respuestas HTTP
verificables.

------------------------------------------------------------------------

## HU-14 --- Historial de transacciones

Como comprador,

quiero consultar mi historial de transacciones,

para revisar las compras realizadas anteriormente en la plataforma.

Prioridad: Should Have

Story Points: 5 (estimación inicial; pendiente de validación mediante
Planning Poker)

### BDD

Escenario 1 --- Historial disponible

Given un comprador autenticado mediante JWT

And existen transacciones asociadas a su cuenta

When consulta su historial

Then el sistema retorna las transacciones correspondientes

And responde con HTTP 200 OK

And obtiene la información desde PostgreSQL.

Escenario 2 --- Sin transacciones

Given un comprador autenticado mediante JWT

And no existen transacciones asociadas

When consulta su historial

Then el sistema responde con HTTP 200 OK

And retorna un arreglo JSON vacío.

### Contrato REST

Método: GET

Endpoint: /api/v1/transacciones/historial

Autenticación: JWT

Resultado exitoso: 200 OK

Persistencia: PostgreSQL

### INVEST --- HU-14

Independent: puede implementarse como una consulta independiente sobre
las transacciones.

Negotiable: la información y filtros del historial pueden refinarse.

Valuable: permite consultar las operaciones realizadas previamente.

Estimable: el alcance está delimitado a consultar las transacciones del
usuario.

Small: corresponde a una funcionalidad concreta de consulta.

Testable: contempla historial disponible y ausencia de transacciones.

------------------------------------------------------------------------

## HU-15 --- Búsqueda por rango de precio

Como comprador,

quiero buscar productos dentro de un rango de precio,

para encontrar ofertas que se ajusten a mi presupuesto.

Prioridad: Should Have

Story Points: 5 (estimación inicial; pendiente de validación mediante
Planning Poker)

### BDD

Escenario 1 --- Productos encontrados

Given un comprador autenticado mediante JWT

And existen ofertas activas de productos con diferentes precios

And consulta un rango de precio válido

When realiza la búsqueda indicando precio mínimo y máximo

Then el sistema retorna las ofertas cuyo precio está dentro del rango
indicado

And responde con HTTP 200 OK

And obtiene las ofertas desde PostgreSQL.

Escenario 2 --- Sin resultados

Given un comprador autenticado mediante JWT

And no existen ofertas dentro del rango solicitado

When realiza la búsqueda

Then el sistema responde con HTTP 200 OK

And retorna un arreglo JSON vacío.

Escenario 3 --- Rango inválido

Given un comprador autenticado mediante JWT

When proporciona un precio mínimo superior al precio máximo

Then el sistema rechaza la solicitud

And responde con HTTP 400 Bad Request.

### Contrato REST

Método: GET

Endpoint: /api/v1/productos?precioMin={precioMin}&precioMax={precioMax}

Autenticación: JWT

Resultado exitoso: 200 OK

Persistencia: PostgreSQL

### INVEST --- HU-15

Independent: puede implementarse como un filtro adicional sobre el
catálogo.

Negotiable: los detalles del filtro y presentación de resultados pueden
ajustarse.

Valuable: ayuda al comprador a encontrar productos según su presupuesto.

Estimable: el alcance está delimitado al filtrado por precio.

Small: se concentra en una funcionalidad concreta de búsqueda.

Testable: contempla resultados, ausencia de resultados y rangos
inválidos.

------------------------------------------------------------------------

>>>>>>> Stashed changes
# 2. Auditoría INVEST

### Auditoría de HU-01 a HU-15

  ----------------------------------------------------------------------------------
  HU         Independent   Negotiable   Valuable   Estimable   Small      Testable
  ---------- ------------- ------------ ---------- ----------- ---------- ----------
  HU-01      ✓             ✓            ✓          ✓           ✓          ✓

  HU-02      ✓             ✓            ✓          ✓           ✓          ✓

  HU-03      ✓             ✓            ✓          ✓           ✓          ✓

  HU-04      ✓             ✓            ✓          ✓           ✓          ✓

  HU-05      ✓             ✓            ✓          ✓           ✓          ✓

  HU-06      ✓             ✓            ✓          ✓           ✓          ✓

  HU-07      ✓             ✓            ✓          ✓           ✓          ✓

  HU-08      ✓             ✓            ✓          ✓           ✓          ✓

  HU-09      ✓             ✓            ✓          ✓           ✓          ✓

  HU-10      ✓             ✓            ✓          ✓           ✓          ✓

  HU-11      ✓             ✓            ✓          ✓           ✓          ✓

  HU-12      ✓             ✓            ✓          ✓           ✓          ✓

  HU-13      ✓             ✓            ✓          ✓           ✓          ✓

  HU-14      ✓             ✓            ✓          ✓           ✓          ✓

  HU-15      ✓             ✓            ✓          ✓           ✓          ✓
  ----------------------------------------------------------------------------------

### Justificación INVEST --- HU-01 a HU-05

Las HU-01 a HU-05 cuentan con criterios de aceptación BDD y contratos
REST definidos en el backlog. Su revisión INVEST se mantiene de acuerdo
con el alcance funcional establecido para cada historia.

### Justificación INVEST --- HU-06 a HU-09

#### HU-06 --- Registro de finca

-   **Independent:** puede desarrollarse como una funcionalidad
    independiente del registro de productos.
-   **Negotiable:** los datos específicos de la finca pueden ajustarse
    durante el desarrollo.
-   **Valuable:** permite asociar la producción a un lugar y contribuye
    a la trazabilidad.
-   **Estimable:** el alcance está delimitado al registro y persistencia
    de una finca.
-   **Small:** se concentra en una operación principal de registro.
-   **Testable:** cuenta con escenarios BDD y respuestas HTTP
    verificables.

#### HU-07 --- Consulta de inventario

-   **Independent:** puede implementarse como una consulta independiente
    sobre los productos del agricultor.
-   **Negotiable:** la representación del inventario puede evolucionar
    durante el desarrollo.
-   **Valuable:** permite al agricultor conocer las cantidades
    disponibles.
-   **Estimable:** el alcance inicial se limita a consultar productos y
    cantidades.
-   **Small:** corresponde principalmente a una operación de consulta.
-   **Testable:** puede validarse mediante escenarios BDD y respuesta
    HTTP 200.

#### HU-08 --- Creación de orden de compra

-   **Independent:** representa una operación funcional diferenciada
    dentro del módulo de pedidos.
-   **Negotiable:** los detalles de la orden pueden refinarse sin
    modificar el objetivo principal.
-   **Valuable:** permite formalizar la intención de adquisición de un
    comprador.
-   **Estimable:** el alcance está delimitado a validar disponibilidad y
    crear la orden.
-   **Small:** se concentra en una operación principal de creación.
-   **Testable:** contempla escenarios de éxito, stock insuficiente y
    autenticación inválida.

#### HU-09 --- Confirmación de alistamiento

-   **Independent:** puede implementarse como una operación específica
    sobre una orden existente.
-   **Negotiable:** los detalles del proceso de alistamiento pueden
    evolucionar.
-   **Valuable:** permite informar que el pedido está preparado para
    continuar con la logística.
-   **Estimable:** la funcionalidad está limitada a validar y actualizar
    el estado.
-   **Small:** corresponde a una operación puntual sobre el estado de
    una orden.
-   **Testable:** contempla escenarios exitosos, orden inexistente/no
    perteneciente y autenticación inválida.

### Justificación INVEST --- HU-10 a HU-12

#### HU-10 --- Programación de despacho

-   **Independent:** puede desarrollarse como una operación específica
    posterior a la confirmación del pedido.
-   **Negotiable:** los detalles de fecha y hora de despacho pueden
    ajustarse durante el desarrollo.
-   **Valuable:** permite coordinar el proceso de entrega de los
    productos.
-   **Estimable:** el alcance está delimitado a programar el despacho de
    un pedido.
-   **Small:** se concentra en actualizar la programación de una orden.
-   **Testable:** cuenta con escenarios BDD y respuestas HTTP
    verificables.

#### HU-11 --- Seguimiento de pedido

-   **Independent:** puede implementarse como una consulta independiente
    sobre los pedidos.
-   **Negotiable:** la información mostrada en el seguimiento puede
    refinarse.
-   **Valuable:** permite al comprador conocer el avance de su pedido.
-   **Estimable:** el alcance está delimitado a consultar el estado de
    una orden.
-   **Small:** corresponde principalmente a una operación de consulta.
-   **Testable:** contempla escenarios de consulta exitosa y pedido
    inexistente.

#### HU-12 --- Notificación al agricultor

-   **Independent:** puede desarrollarse como una funcionalidad
    diferenciada del procesamiento de pedidos.
-   **Negotiable:** el contenido y presentación de las notificaciones
    pueden ajustarse.
-   **Valuable:** informa oportunamente al agricultor sobre cambios
    relevantes.
-   **Estimable:** el alcance está delimitado al registro y consulta de
    notificaciones.
-   **Small:** se concentra en la gestión de notificaciones.
-   **Testable:** contempla escenarios de generación y acceso no
    autorizado.

### Justificación INVEST --- HU-13 a HU-15

#### HU-13 --- Inicio de sesión con JWT

-   **Independent:** puede desarrollarse como una funcionalidad
    específica del módulo de autenticación.
-   **Negotiable:** los detalles del mecanismo de autenticación pueden
    refinarse.
-   **Valuable:** permite controlar el acceso seguro a la plataforma.
-   **Estimable:** el alcance está delimitado a validar credenciales y
    generar JWT.
-   **Small:** se concentra en una operación de autenticación.
-   **Testable:** contempla credenciales válidas e inválidas con
    respuestas HTTP verificables.

#### HU-14 --- Historial de transacciones

-   **Independent:** puede implementarse como una consulta independiente
    sobre las transacciones.
-   **Negotiable:** la información y filtros del historial pueden
    refinarse.
-   **Valuable:** permite consultar las operaciones realizadas
    previamente.
-   **Estimable:** el alcance está delimitado a consultar las
    transacciones del usuario.
-   **Small:** corresponde a una funcionalidad concreta de consulta.
-   **Testable:** contempla historial disponible y ausencia de
    transacciones.

#### HU-15 --- Búsqueda por rango de precio

-   **Independent:** puede implementarse como un filtro adicional sobre
    el catálogo.
-   **Negotiable:** los detalles del filtro y presentación de resultados
    pueden ajustarse.
-   **Valuable:** ayuda al comprador a encontrar productos según su
    presupuesto.
-   **Estimable:** el alcance está delimitado al filtrado por precio.
-   **Small:** se concentra en una funcionalidad concreta de búsqueda.
-   **Testable:** contempla resultados, ausencia de resultados y rangos
    inválidos.

------------------------------------------------------------------------

# 3. Estado de esta etapa

Esta versión integra las **15 historias de usuario (HU-01 a HU-15)**
requeridas para el Sprint 0.

Cada historia contiene:

1.  Historia de usuario.
2.  Prioridad MoSCoW.
3.  Story Points mediante escala Fibonacci.
4.  Escenarios Given-When-Then.
5.  Endpoint REST y autenticación cuando corresponde.
6.  Persistencia o resultado esperado.
7.  Auditoría INVEST.

> **Importante:** los Story Points son estimaciones iniciales y deben
> ser validados por el equipo mediante Planning Poker antes de cerrar la
> versión definitiva del backlog.