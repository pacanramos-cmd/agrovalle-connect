# Sprint 2 Retrospective — AgroValle Connect

**Sprint:** [COMPLETAR: fechas] · **Formato:** Start / Stop / Continue · **Participantes:** todo el equipo.

> Borrador elaborado a partir de los hallazgos técnicos del sprint. El equipo lo valida y completa en la reunión de
> retrospectiva.

## 1. Qué salió bien (Continue)

- El diseño completo de las 15 historias antes de programar (esquema, diagramas y arquitectura) permitió implementar HU-02
  y HU-04 sin rediseñar.
- La separación en capas facilitó probar cada capa por separado: servicios con Mockito, controladores con MockMvc y una
  prueba de integración con la cadena real de Spring Security.
- Los patrones Strategy y Observer quedaron integrados en el flujo real de publicación, no como ejemplos aislados.

## 2. Qué se debe cambiar (Stop)

| Problema detectado | Consecuencia | Causa raíz |
|---|---|---|
| El código de HU-13 se escribió sin pruebas ni commits. | Cobertura insuficiente y riesgo de perder trabajo. | Se priorizó avanzar sobre el DoD. |
| Faltaban `jwt.secret` y `jwt.expiration-ms` en la configuración. | El contexto de Spring no arrancaba y fallaban todas las pruebas de integración. | No se ejecutó `mvn test` antes de subir. |
| La entidad `Agricultor` no coincidía con `schema.sql` (tabla única frente a `usuario` más `agricultor`). | La aplicación no arrancaría contra PostgreSQL con `ddl-auto=validate`. | Diseño y código evolucionaron por separado. |
| JaCoCo estaba declarado dos veces en el `pom.xml`, con versiones y reglas distintas. | Umbral ambiguo; el chequeo de ramas no se ejecutaba en CI. | Dos ramas editaron el mismo archivo. |
| Spring Security respondía 403 y no 401 a solicitudes anónimas. | Se incumplía el escenario BDD de HU-02. | No se probó la ruta protegida de punta a punta. |
| Se fusionaron Pull Requests sin la revisión de un compañero (ya señalado en el Sprint 1). | El DoD no se cumplía. | La protección de ramas no estaba activa. |

## 3. Qué empezar a hacer (Start)

- Ejecutar `mvn test` y `mvn checkstyle:check` antes de cada push; el pre-commit lo automatiza.
- Escribir cada prueba antes del código (Red, Green, Refactor) y dejar commits `test:` antes de los `feat:`.
- Activar y respetar la protección de `main`: ningún cambio entra sin CI en verde y una aprobación.
- Revisar `schema.sql` contra las entidades JPA cada vez que se agregue una entidad.
- Probar cada endpoint protegido con y sin token, y con el rol incorrecto.
- Dejar el entorno de cada integrante listo desde el Sprint 0: JDK 17 (Temurin) con `JAVA_HOME`, el Maven Wrapper y
  `pom.xml` en la raíz del repositorio, porque el pipeline y los IDE lo buscan ahí.
- Repartir los permisos de administrador del repositorio para que más de una persona pueda configurar las reglas de rama.

## 4. Acciones de mejora

| Acción | Responsable | Plazo | Indicador |
|---|---|---|---|
| Activar la regla de protección de `main` con el check `build-and-test`. | Dueño del repositorio (`pacanramos-cmd`); es quien tiene permisos de administrador | Antes de la entrega | Merge bloqueado si el pipeline está en rojo. |
| Mantener la cobertura JaCoCo por encima del 60%. | Todo el equipo | Cada sprint | Gate 3 en verde (cobertura actual: 95% instrucciones, 93% ramas). |
| Revisar el esquema contra las entidades en cada Pull Request. | Quien abra el Pull Request, con la revisión de un compañero | Cada sprint | La aplicación arranca contra PostgreSQL. |
| Un compañero aprueba cada Pull Request. | Todo el equipo | Cada sprint | 100% de los PR con una aprobación. |

## 5. Relación con la calidad ISO/IEC 25010

La Integración Continua mejora la **mantenibilidad** (analizabilidad y modificabilidad), porque cada refactorización se
valida con las pruebas; la **fiabilidad** (madurez), porque `main` solo contiene código que pasó el 100% de las pruebas; y la
**eficiencia organizacional**, porque la revisión por pares se centra en la arquitectura y las reglas de negocio y no en
sintaxis, que revisa Checkstyle.
