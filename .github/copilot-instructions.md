# GitHub Copilot / AI Agent Instructions for this repo ✅

Concise, actionable guidance to help an AI agent be productive quickly in this Java Swing project (JDK 21).

## Big picture
- Desktop Java Swing app (JDK 21) for library management. The code follows an MVC-ish layout:
  - **com.example.controlador** — controllers and navigation wiring (`Controlador`, `ControladorNavegacion`).
  - **com.example.vista** — views, each exposes `public JPanel pantalla()` and receives a `Controlador` instance for callbacks.
  - **com.example.modelo** — domain models (Usuario, Publicacion, Ejemplar, etc.).
- Startup: `com.example.App` applies default fonts and constructs `Controlador` which creates `ControladorNavegacion` and shows the main window.

## Navigation & UI patterns (important)
- `ControladorNavegacion` builds two CardLayouts: a parent (`panelPadre`) for top-level screens and a child (`panelPrincipal` inside `panelHijo`) for authenticated area.
  - Parent keys: `"inicioSesion"`, `"recuperarCuenta"`, `"entrarSistema"`.
  - Child keys (examples): `"panelControl"`, `"concederPrestamo"`, `"devolverPrestamo"`, `"ejemplares"`, `"gestionUsuarios"`, `"sancionManual"`, `"publicaciones"`.
- Important: When switching child screens use `ControladorNavegacion.cambiarPantallaHijo("name")` which calls `cardHijo.show(panelPrincipal, name)` to avoid the common "wrong parent for CardLayout" exception. Use the controller, not direct CardLayout on the visible panel.
- Fonts: `Fonts.applyDefaultOpenSans()` attempts to load `resources/fonts/OpenSans-Regular.ttf` and sets default UI fonts. The code falls back to a family named "Open Sans" if resource not present.

## Data access & DB patterns
- No ORM — direct JDBC only. DAOs use `MySQLConnection` (implements `DBConnection`) + `PreparedStatement` and try-with-resources. Example: `UsuarioDAO.consultaInicioSesion(...)`.
- `ConfigLoader` loads `src/main/resources/application.properties` at class-load time; missing file throws a `RuntimeException` (fail fast).
- `MySQLConnection` reads `mysql.url`, `mysql.user`, `mysql.password` from properties and returns a `java.sql.Connection` (returns `null` on failure — callers generally check for null implicitly).
- Error handling pattern: DAOs catch exceptions and print messages/causes to stdout (no centralized logging framework). Expect console output for errors during local runs.
- SQL seeds and triggers: `inicializacion.sql` seeds data and includes triggers that generate the `usuario` field for `usuarios` on INSERT/UPDATE — the DB can override values set in code.

## Docker / local environment
- Docker Compose file (root `docker-compose.yml`) provides services:
  - `mysql` → container name `mysql_db`, port 3306, initialized with `inicializacion.sql`.
  - `phpmyadmin` → container name `phpmyadmin`, port 8080.
- Exact commands to reproduce environment:
  - Start DB: `docker compose up -d` (wait until `mysql_db` is healthy)
  - phpMyAdmin UI: http://localhost:8080 (PMA host: `mysql`, port `3306`)

## Build, run, test (exact commands) 🔧
- Build (skip tests locally): `mvn -DskipTests package`
- Run the app from classes (recommended for development): `java -cp target/classes com.example.App`
- Run tests: `mvn test` (JUnit 4.11)

## Common tasks (quick recipes) 💡
- Add a new screen (pattern):
  1. Create `src/main/java/com/example/vista/MiVista.java`, inject `Controlador` via constructor, expose `public JPanel pantalla()`.
  2. Instantiate in `ControladorNavegacion` and add to `panelPrincipal` or `panelPadre`, giving it a unique string key (see keys above).
  3. Navigate using `controlador.getControladorNavegacion().cambiarPantallaHijo("miVista")` or `cambiarPantallaPadre(...)`.
- Add DAO method: follow existing patterns in `PublicacionDAO` / `UsuarioDAO` — use `PreparedStatement`, `try-with-resources`, return arrays/objects (methods often return `null`/empty results on error).

## Tests & CI notes
- Unit tests use JUnit 4.11; current coverage is minimal (`AppTest`).
- Integration test suggestion (useful for CI): add a test that starts with `docker compose up -d` and asserts `new MySQLConnection().getConnection()` is non-null and basic queries succeed.
- No GitHub Actions workflow yet — recommended CI steps: bring up DB, run the DB-aware integration test, then `mvn test`.

## Project conventions & gotchas
- Language: **Spanish** for identifiers, comments, and UI strings — keep tests and messages in Spanish where appropriate.
- Resources must sit under `src/main/resources` to be available at runtime via classpath.
- Watch out for DB triggers that auto-generate `usuario` — when writing insert/update tests, assert DB-generated value when necessary.
- Logging: most DAOs print to console; Fonts uses `java.util.logging`. Expect simple console output for debugging.

## Files to inspect when debugging
- Startup/UI: `App.java`, `controlador/Controlador.java`, `controlador/ControladorNavegacion.java` 🔧
- DB access: `conexiones/MySQLConnection.java`, `utilities/ConfigLoader.java`, DAOs in `dao/` 🔍
- UI utils: `utilities/Fonts.java` (font loading fallback behaviour) 🎨
- DB schema and test data: `inicializacion.sql` (trigger behaviour & sample data) 🗂️

---
If you want, I can (pick one):
- add a focused integration test that asserts DB connectivity and a simple query, or
- add a minimal GitHub Actions workflow that boots the DB and runs that integration test.

Tell me which you'd prefer and I’ll implement it. Feedback welcome — specify any missing areas you'd like the instructions to expand on (e.g., CI details or additional code examples).