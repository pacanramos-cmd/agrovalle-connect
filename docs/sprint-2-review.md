# Sprint 2 Review — AgroValle Connect

**Sprint:** [COMPLETAR: fechas]
**Sprint Goal:** Permitir que un agricultor autenticado publique sus productos y que cualquier comprador los encuentre
filtrando por municipio y categoría, con cada cambio validado por Integración Continua.
**Equipo:** Andrés Felipe Ramos, Luis Felipe Porras, Santiago Narváez Rivera, Milton Adolfo Cortes.
**Modalidad de la demo:** [COMPLETAR: llamada de Discord con pantalla compartida / presencial]

---

## 1. Incremento demostrado

| Historia | Endpoint | Resultado demostrado |
|---|---|---|
| HU-01 | `POST /api/v1/auth/register` | `201` con el agricultor; `400` por datos inválidos; `409` por cédula o correo duplicados. Se guardan `usuario` y `agricultor` en una transacción. |
| HU-13 | `POST /api/v1/auth/login` | `200` con el token JWT, el tipo `Bearer`, el identificador y el rol; `401` con credenciales incorrectas, con el mismo mensaje para correo o contraseña. |
| HU-02 | `POST /api/v1/productos` | `201` con el producto y la comisión estimada; `401` sin token válido; `403` si el rol no es AGRICULTOR; `400` por fecha de cosecha anterior a hoy, tipo inexistente o campos inválidos. |
| HU-04 | `GET /api/v1/productos?municipio=&categoria=` | `200` con las ofertas que cumplen los filtros; arreglo vacío si no hay coincidencias; funciona sin filtros y sin token. |

**Patrones y principios mostrados:** Strategy (`SelectorComision` con tres estrategias por zona), Observer
(`ProductoPublicadoEvent` y su listener), Specification (`ProductoSpecifications`), y los cinco principios SOLID descritos
en `docs/diseno/arquitectura-mvc-gof.md`.

## 2. Integración Continua

- Pipeline `.github/workflows/ci.yml`, job `build-and-test`, con tres Quality Gates: Checkstyle, pruebas JUnit 5 y umbral
  JaCoCo del 60% en instrucciones y ramas.
- Regla de protección de `main` (Pull Request obligatorio, check `build-and-test` en verde y una aprobación): **pendiente
  de activar**. El repositorio pertenece a `pacanramos-cmd` y los demás integrantes son colaboradores sin acceso a
  *Settings → Branches*; se solicitó al dueño que la cree. [COMPLETAR cuando esté activa: fecha y quién la creó].
- Resultado verificado: el Pull Request #57 (`feature/sprint-2-hu02-hu04-ci` → `main`, 6 commits) pasó `build-and-test` en
  verde en las dos ejecuciones del pipeline (`push` y `pull_request`). Cobertura JaCoCo medida el 4 de noviembre con
  Temurin 17.0.21: **95% en instrucciones y 93% en ramas**, por encima del umbral del 60%.
- Evidencias anexas: reporte JaCoCo, Pull Request #57 con los checks en verde y pestaña Actions. Se anexarán cuando se
  hagan: la regla de protección guardada y la simulación del fallo (Pull Request en rojo con el merge bloqueado, el mismo
  en verde y la aprobación de un compañero).

## 3. Estado del tablero al cierre

| Columna | Estado |
|---|---|
| Done | HU-02 (#8), HU-04 (#10), T-CI.1 (#58) y T-CI.2 (#59) |
| In progress (3/3) | T-CI.3 (#60), que depende de que el dueño del repositorio active la protección de `main`; y T-01.2 (#25) y T-13.1 (#33), heredadas del Sprint 1 |
| Ready | T-CI.4 (#61) y T-CI.5 (#62): la simulación del fallo y su corrección se hacen cuando la regla esté activa |
| Backlog | HU-03, HU-05 a HU-12, HU-14 y HU-15 |

**Puntos completados:** 8 de 8 comprometidos (HU-02: 5 y HU-04: 3, fusionadas en `main` con el CI en verde). **Cobertura JaCoCo:** 95% en
instrucciones y 93% en ramas.

> Las tarjetas de HU-01 (#6) y HU-13 (#21) y sus tareas del Sprint 1 siguen sin actualizarse en el tablero. [COMPLETAR tras
> confirmar con el equipo cuáles se pasan a Done.]
>
> Observación de calidad: el Pull Request #57 se fusionó sin aprobación de un compañero (la pestaña Conversation muestra 0
> interacciones). Es la razón de T-CI.3 y queda registrada en la retrospectiva.

## 4. Retroalimentación del Product Owner

[COMPLETAR: comentarios de la docente durante la demo]. Pendientes por confirmar con el Product Owner: las tasas de
comisión por zona (3%, 4% y 5% son valores de trabajo), el código `409` para duplicados y el acceso público de HU-04.

## 5. Siguiente sprint

HU-06 (registro de finca) y HU-07 (inventario), que dependen de los productos publicados.
