# GitHub Copilot / AI Agent Instructions for this repo ✅

Concise, actionable guidance to get productive in this Java Swing (JDK 21) library app.

## Big picture
- Desktop Java Swing, MVC-like layout:
  - **controllers:** `com.example.controlador` (e.g., `Controlador`, `ControladorNavegacion`)
  - **views:** `com.example.vista` (each exposes `public JPanel pantalla()` and accepts a `Controlador`)
  - **models:** `com.example.modelo`
- Entry point: `com.example.App` — it registers fonts and builds the `Controlador` → `ControladorNavegacion` UI.

## Navigation & UI patterns 🔧
- `ControladorNavegacion` manages two CardLayouts with parent keys (`"inicioSesion"`, `"recuperarCuenta"`, `"entrarSistema"`) and child keys for the authenticated area (`"panelControl"`, `"concederPrestamo"`, `"devolverPrestamo"`, `"ejemplares"`, `"gestionUsuarios"`).
- **Always** change screens with `ControladorNavegacion.cambiarPantallaHijo("key")` — avoids "wrong parent for CardLayout" errors.
- Views are lightweight UI classes that expose `pantalla()` and receive a `Controlador`; prefer adding navigation keys rather than manipulating CardLayout directly.
- Fonts: `utilities/Fonts.applyDefaultOpenSans()` loads `resources/fonts/OpenSans-*.ttf` (fallback: system Open Sans family).

## Data & DB patterns 🗄️
- No ORM — DAOs use direct JDBC (`MySQLConnection` + `PreparedStatement` + try-with-resources). Typical return types: `String[]`, `String[][]`, model objects, `boolean` (success), or `int` (id / -1 on error).
- Many DAO methods have two variants: one that opens a new `Connection`, and a second that accepts a `Connection` parameter (useful for transactions). Follow `PublicacionDAO` as the reference.
- `ConfigLoader` loads `application.properties` at class-load time and will throw a RuntimeException if the file is missing — don't run without it.
- `MySQLConnection.getConnection()` logs to stderr and **returns null** on failure — caller code sometimes assumes non-null, so ensure DB is available when running UI or tests.
- DB initialization: `inicializacion.sql` seeds tables and triggers; the docker compose mounts this file into MySQL container during first startup.

## Build / Run / Debug / Docker 🧰
- JDK: **21** (project compiled/tested on OpenJDK 21).
- Build: `mvn -DskipTests package` (produces `target/classes` for running locally).
- Run (dev): `java -cp target/classes com.example.App` (or run from IDE main class `com.example.App`).
- Debug: attach JDWP (example): `java -agentlib:jdwp=transport=dt_socket,server=y,suspend=y,address=*:5005 -cp target/classes com.example.App`.
- Tests: `mvn test` (JUnit 4). Current test coverage is minimal; DB integration tests are not present yet.
- Docker: `docker compose up -d` brings up `mysql` (container `mysql_db`, port 3306) and `phpmyadmin` (container `phpmyadmin`, port 8080). The compose file mounts `inicializacion.sql` to seed the DB.
- Config: `src/main/resources/application.properties` contains `mysql.url`, `mysql.user`, `mysql.password` — adjust when needed (note: defaults expect DB on localhost:3306, which works with `docker compose` port mapping).

## Conventions & gotchas ⚠️
- UI strings and comments are **Spanish** — keep tests and messages consistent.
- Resources (images, fonts) must be under `src/main/resources` to be available at runtime.
- Logging is ad-hoc: DAOs print to stdout/stderr; there is no centralized logger. Handle errors explicitly in new code.
- Use the DAO transactional overloads when several DB operations must be atomic (open a `Connection`, set auto-commit=false, call DAO methods that accept the `Connection`, commit/rollback).

## Quick recipes (examples) 💡
- Add a new screen:
  1. Create `src/main/java/com/example/vista/MiVista.java` with constructor `(Controlador controlador)` and `public JPanel pantalla()`.
  2. Register view in `ControladorNavegacion` (add to `panelPrincipal` or `panelPadre`) using a unique key.
  3. Navigate with `controlador.getControladorNavegacion().cambiarPantallaHijo("miVista")`.
- Add a DAO method:
  - Follow `PublicacionDAO` pattern: provide an overload `method(Connection conexion, ...)` when the operation may be part of a larger transaction; return `-1`/`false`/`null` on error per existing conventions.
- Adding tests:
  - For DB tests, run `docker compose up -d` before `mvn test`, or write tests that mock DAOs.

## Files to inspect when debugging
- `App.java`, `controlador/Controlador.java`, `controlador/ControladorNavegacion.java`
- `conexiones/MySQLConnection.java`, `conexiones/DBConnection.java`, `utilities/ConfigLoader.java`
- `dao/*` (look for transactional overloads), `inicializacion.sql`, `src/main/resources/fonts/*`

---
If you'd like, I can add a DB integration test (starts docker-compose in CI) or a minimal GitHub Actions workflow that boots the DB and runs `mvn test`. Please tell me which you'd prefer or any missing details to include.