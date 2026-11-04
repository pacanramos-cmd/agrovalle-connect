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
- Regla de protección de `main`: Pull Request obligatorio, check `build-and-test` en verde y una aprobación.
- Evidencias: [COMPLETAR: capturas del Actions en verde, del reporte JaCoCo con el porcentaje real y del Pull Request
  bloqueado por el fallo intencional de la simulación].

## 3. Estado del tablero al cierre

| Columna | Estado |
|---|---|
| Done | [COMPLETAR: tareas T-02.x, T-04.x, T-CI.x y T-D.x terminadas] |
| In progress | [COMPLETAR] |
| Backlog | HU-03, HU-05 a HU-12, HU-14 y HU-15 |

**Puntos completados:** [COMPLETAR] de 8 comprometidos. **Cobertura JaCoCo:** [COMPLETAR]%.

## 4. Retroalimentación del Product Owner

[COMPLETAR: comentarios de la docente durante la demo]. Pendientes por confirmar con el Product Owner: las tasas de
comisión por zona (3%, 4% y 5% son valores de trabajo), el código `409` para duplicados y el acceso público de HU-04.

## 5. Siguiente sprint

HU-06 (registro de finca) y HU-07 (inventario), que dependen de los productos publicados.
