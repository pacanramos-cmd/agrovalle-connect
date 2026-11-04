# AgroValle Connect 🌾

[![CI](https://github.com/pacanramos-cmd/agrovalle-connect/actions/workflows/ci.yml/badge.svg)](https://github.com/pacanramos-cmd/agrovalle-connect/actions/workflows/ci.yml)

## 📌 Visión del Producto
> Para **productores del Valle del Cauca**,
> que **necesitan vender directo sin intermediarios**,
> AgroValle Connect es **una plataforma web desarrollada en Java 17 / Spring Boot**,
> que **conecta la oferta agrícola con la demanda comercial urbana a precio justo**,
> a diferencia de **los intermediarios tradicionales**,
> nuestro producto **garantiza trazabilidad y contratos de API transparentes**.

## 👥 Integrantes del equipo

| _Nombre 1_ | Andres Felipe Ramos |
| _Nombre 2_ | Luis Felipe Porras |
| _Nombre 3_ | Santiago Narvaez Rivera |
| _Nombre 4_ | Milton Adolfo Cortez  |


## 🌿 Estrategia de Control de Versiones: GitFlow

Elegimos **GitFlow** porque nuestro proyecto tiene entregas por sprint (lanzamientos versionados) y varios integrantes trabajando en historias distintas en paralelo. Esto nos permite mantener `main` siempre estable mientras `develop` integra el trabajo en curso, minimizando los tiempos de espera y evitando fusiones extensas de última hora, ya que cada Historia de Usuario vive en una rama `feature/` de corta duración.

```mermaid
gitGraph
    commit id: "Initial"
    branch develop
    checkout develop
    commit id: "Setup-Project"
    branch feature/HU-01
    checkout feature/HU-01
    commit id: "feat: logic-hu-01"
    checkout develop
    merge feature/HU-01
    branch release/v1.0.0
    checkout release/v1.0.0
    commit id: "fix: minor-bug"
    checkout main
    merge release/v1.0.0 tag: "v1.0.0"
    checkout develop
    merge release/v1.0.0
```

## 📝 Convención de Commits
Usamos **Conventional Commits**:
- `feat:` nueva funcionalidad
- `fix:` corrección de error
- `docs:` documentación
- `test:` pruebas
- `chore:` tareas de mantenimiento

Ejemplo: `feat(api): implementar endpoint de autenticación con JWT`

## ⚙️ Stack Tecnológico
- Java 17 / Spring Boot
- PostgreSQL
- Maven
- JUnit 5 + JaCoCo (cobertura mínima 60%)
- Checkstyle (Google Java Style)
- Husky (pre-commit hooks)

## 📂 Estructura del repositorio
```
/src/main/java     → código fuente (MVC)
/src/test/java     → pruebas unitarias
/docs/dod.md       → Definition of Done
BACKLOG.md         → Product Backlog (15 HUs, MoSCoW, BDD, Story Points)
checkstyle.xml     → reglas de estilo
.gitignore
```

## ✅ Definition of Done
Ver [docs/dod.md](docs/dod.md)

## 📋 Product Backlog
Ver [BACKLOG.md](BACKLOG.md)

## 🔐 Variables de entorno

Copiar `.env.example` y completar los valores. Nunca se suben credenciales al repositorio.

| Variable | Uso |
|---|---|
| `DB_HOST`, `DB_PORT`, `DB_NAME`, `DB_USER`, `DB_PASSWORD` | Conexión a PostgreSQL |
| `JWT_SECRET` | Clave para firmar los tokens JWT (mínimo 32 caracteres) |
| `JWT_EXPIRATION_MS` | Vigencia del token en milisegundos (por defecto 3600000) |

## ✅ Integración Continua

Cada push a `main` o `feature/*` y cada Pull Request hacia `main` ejecuta `.github/workflows/ci.yml` (job
`build-and-test`) con tres Quality Gates: `mvn checkstyle:check`, `mvn test` y `mvn jacoco:check` (cobertura mínima del 60%
en instrucciones y ramas). `main` está protegida: exige Pull Request, el check en verde y la aprobación de un compañero.
Pasos de configuración y de la simulación de fallo en `docs/guia-ci-y-proteccion-de-ramas.md`.
