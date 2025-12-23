# GitHub Copilot / AI Agent Instructions for this repo ✅

Concise, actionable guidance to get productive in this Java Swing (JDK 21) library app.

## Big picture
- Desktop Java Swing, MVC-like layout:
  - **controllers:** `com.example.controlador` (`Controlador`, `ControladorNavegacion`)
  - **views:** `com.example.vista` (each exposes `public JPanel pantalla()` and accepts a `Controlador`)
  - **models:** `com.example.modelo`
- Entry point: `com.example.App` applies fonts and builds the `Controlador` → `ControladorNavegacion` UI.

## Navigation & UI patterns 🔧
- `ControladorNavegacion` manages two CardLayouts:
  - parent keys: `"inicioSesion"`, `"recuperarCuenta"`, `"entrarSistema"`
  - child keys (authenticated area): `"panelControl"`, `"concederPrestamo"`, `"devolverPrestamo"`, `"ejemplares"`, `"gestionUsuarios"`
- **Always** change screens using `ControladorNavegacion.cambiarPantallaHijo("key")` (avoids "wrong parent for CardLayout" errors).
- Fonts: `utilities/Fonts.applyDefaultOpenSans()` loads `resources/fonts/OpenSans-Regular.ttf` (fallback is an Open Sans family name).

## Data & DB patterns 🗄️
- No ORM — DAOs use direct JDBC with `MySQLConnection` + `PreparedStatement` + try-with-resources (see `PublicacionDAO` for examples returning `String[]`/`String[][]`).
- `ConfigLoader` loads `application.properties` at class-load time and throws if missing (fail-fast behavior).
- `MySQLConnection.getConnection()` prints errors and **returns null** on failure — code often checks for null implicitly.
- DB seeds/triggers: `inicializacion.sql` seeds data; triggers auto-generate `usuario` on insert/update (tests should account for DB-generated values).

## Build / Run / Debug / Docker 🧰
- JDK: **21** (tested on OpenJDK 21)
- Build: `mvn -DskipTests package`
- Run (development): `java -cp target/classes com.example.App`
- Debug example: `java -agentlib:jdwp=transport=dt_socket,server=y,suspend=y,address=*:5005 -cp target/classes com.example.App`
- Tests: `mvn test` (JUnit 4.11)
- Docker: `docker compose up -d` → services: `mysql` (container `mysql_db`, port 3306), `phpmyadmin` (`phpmyadmin`, port 8080). phpMyAdmin connects to host `mysql`.

## Conventions & gotchas ⚠️
- Code/UI strings and comments are **Spanish** — keep tests/messages in Spanish when appropriate.
- Resources must live under `src/main/resources` to be on the runtime classpath.
- Controllers & views pattern: views expose `pantalla()` and are added to `ControladorNavegacion` with a **string key** used to navigate.
- Error/logging: DAOs print exceptions to stdout; there's no centralized logger.

## Quick recipes (examples) 💡
- Add a new screen:
  1. Create `src/main/java/com/example/vista/MiVista.java` with constructor `(Controlador controlador)` and `public JPanel pantalla()`.
  2. Register the view in `ControladorNavegacion` (add to `panelPrincipal` or `panelPadre`) using a unique key.
  3. Navigate with `controlador.getControladorNavegacion().cambiarPantallaHijo("miVista")`.
- Add DAO method: follow `PublicacionDAO` / `UsuarioDAO` patterns: prepare statement, map ResultSet → `String[]`/model, return `null`/empty on errors.

## Files to inspect when debugging
- `App.java`, `controlador/Controlador.java`, `controlador/ControladorNavegacion.java`
- `conexiones/MySQLConnection.java`, `utilities/ConfigLoader.java`, `dao/*`
- `utilities/Fonts.java`, `inicializacion.sql`

---
If you want, I can: add a focused DB integration test that asserts `MySQLConnection` works, or add a minimal GitHub Actions workflow that boots the DB and runs it. Which would you prefer? Please review and tell me any missing or unclear sections to iterate.