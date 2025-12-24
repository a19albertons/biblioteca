# GitHub Copilot / AI Agent Instructions for this repo ✅

Concise, actionable guidance to get productive in this Java Swing (JDK 21) library app.

## Big picture
- Desktop Java Swing, MVC-like structure:
  - **controllers:** `com.example.controlador` (e.g., `Controlador`, `ControladorNavegacion`)
  - **views:** `com.example.vista` (each exposes `public JPanel pantalla()` and accepts a `Controlador`)
  - **models / dao:** `com.example.modelo`, `com.example.dao`
- Entry point: `com.example.App` — calls `Fonts.applyDefaultOpenSans()` then creates `Controlador` and starts the UI.

## Navigation & UI patterns 🔧
- `ControladorNavegacion` manages two CardLayouts:
  - parent keys: `"inicioSesion"`, `"recuperarCuenta"`, `"entrarSistema"` (top-level screens)
  - child keys inside the authenticated area: `"panelControl"`, `"concederPrestamo"`, `"devolverPrestamo"`, `"ejemplares"`, `"gestionUsuarios"`, `"sancionManual"`, `"publicaciones"`
- **Always** change screens using `ControladorNavegacion.cambiarPantallaPadre(...)` or `cambiarPantallaHijo(...)` — do not call `CardLayout.show` directly on views; use the controller to avoid "wrong parent for CardLayout" errors.
- New view pattern: create class in `com.example.vista` with constructor `(Controlador controlador)` and a `public JPanel pantalla()` method; register it in `ControladorNavegacion`.
- Fonts: `utilities/Fonts.applyDefaultOpenSans()` expects `src/main/resources/fonts/OpenSans-*.ttf` (fallback: system Open Sans).

## Data & DB patterns 🗄️
- No ORM — DAOs use plain JDBC (see `com.example.dao`): `MySQLConnection`, `PreparedStatement`, try-with-resources.
- Many DAO methods provide two overloads: one that opens its own `Connection`, and one that accepts a `Connection` parameter for transactional composition (see `PublicacionDAO` as the reference).
- Error conventions: return `-1` for id errors, `false` for failure booleans, `null` for missing objects — follow existing DAO patterns.
- `ConfigLoader` loads `application.properties` at class-load time and throws a RuntimeException if the file is missing — ensure `src/main/resources/application.properties` exists when running.
- `MySQLConnection.getConnection()` logs to stderr and **returns null** on failure — calling code sometimes assumes a non-null `Connection`, so verify the DB is reachable before running UI or tests.
- DB seeding: `inicializacion.sql` seeds schema & triggers; `docker-compose.yml` mounts it into the MySQL container on first startup.

## Build / Run / Debug / Docker 🧰
- JDK: **21** (compile & run with OpenJDK 21).
- Build for dev: `mvn -DskipTests package` (produces `target/classes`).
- Run locally: `java -cp target/classes com.example.App` or run `com.example.App` from your IDE.
- Debug (JDWP):
  - Example: `java -agentlib:jdwp=transport=dt_socket,server=y,suspend=y,address=*:5005 -cp target/classes com.example.App`.
- Tests: `mvn test` (JUnit 4). Some tests may require a running MySQL instance seeded by `inicializacion.sql` — start it via `docker compose up -d` or mock the DAOs.
- Docker: `docker compose up -d` starts services including `mysql` (container `mysql_db`, default port 3306) and `phpmyadmin` (`phpmyadmin`, port 8080).

## Conventions & gotchas ⚠️
- Project language: UI strings and comments are **Spanish** — keep messages and tests consistent.
- Resources (images, fonts) must be placed under `src/main/resources` to be on the classpath.
- Logging is ad-hoc: DAOs print to stdout/stderr (no centralized logger) — surface errors explicitly in new code.
- Use DAO transactional overloads for multi-step operations (open `Connection`, set `autoCommit=false`, call DAO methods that accept the connection, commit/rollback).
- Watch for `null` `Connection` from `MySQLConnection.getConnection()`; defensive checks are common.

## Quick recipes (examples) 💡
- Add a new screen:
  1. Create `src/main/java/com/example/vista/MiVista.java` with constructor `(Controlador controlador)` and `public JPanel pantalla()`.
  2. Register the view in `ControladorNavegacion` (add to `panelPrincipal` or `panelPadre`) and pick a unique navigation key.
  3. Navigate using `controlador.getControladorNavegacion().cambiarPantallaHijo("miVista")`.
- Add a DAO method:
  - Follow `PublicacionDAO` pattern: add an overload `method(Connection conexion, ...)` when the operation may be part of a transaction; use `-1`/`false`/`null` to signal errors.
- Adding tests:
  - For DB integration tests, run `docker compose up -d` to start MySQL before `mvn test`. For unit tests, prefer mocking DAO classes.

## Files to inspect when debugging
- `src/main/java/com/example/App.java`
- `src/main/java/com/example/controlador/ControladorNavegacion.java`
- `src/main/java/com/example/controlador/Controlador.java`
- `src/main/java/com/example/conexiones/MySQLConnection.java`
- `src/main/java/com/example/utilities/ConfigLoader.java`
- `src/main/java/com/example/dao/` (see `PublicacionDAO` for patterns)
- `inicializacion.sql`, `docker-compose.yml`, `src/main/resources/application.properties`, `src/main/resources/fonts/`

---
If you'd like, I can add a DB integration test and a small GitHub Actions workflow that boots the DB and runs `mvn test` (or a focused job that runs only unit tests). Any preferences or missing details to add?