# Definition of Done (DoD) — AgroValle Connect

Contrato técnico de calidad basado en ISO/IEC 25010. Ninguna Historia de Usuario se considera terminada sin cumplir **todos** los puntos siguientes.

- [ ] **Build Local**: el proyecto compila sin errores (`mvn clean install`).
- [ ] **Linter Pass (Checkstyle)**: cero advertencias al ejecutar `mvn checkstyle:check`, usando reglas de Google Java Style.
- [ ] **Functional Correctness**: el 100% de las pruebas unitarias existentes pasan (`mvn test`), con cobertura mínima del 60% verificada con JaCoCo.
- [ ] **Peer Review**: todo Pull Request fue revisado y aprobado por al menos un compañero de equipo antes del merge.
- [ ] **Documentation**: el `README.md` y la carpeta `/docs` están actualizados con cada cambio relevante.
- [ ] **Commits**: el historial sigue estrictamente la convención de Conventional Commits.
- [ ] **Automatización**: los hooks de Husky (`.husky/pre-commit`) están activos y bloquean commits que no pasen linter o tests.

---

**Equipo AgroValle Connect**
Firmado (nombres de todos los integrantes confirmando el cumplimiento):

- Andres Felipe Ramos
- Luis Felipe Porras 
- Santiago Narvaez Rivera
- Milton Adolfo Cortes
