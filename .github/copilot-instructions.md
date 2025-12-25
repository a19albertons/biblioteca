# GitHub Copilot / AI Agent Instructions for this repo ✅

Concise, actionable guidance to get productive in this Java Swing (JDK 21) library app.

## Big picture
- Desktop Java Swing with an MVC-like organization:
  - **controllers:** `com.example.controlador` (many controllers are small, single-responsibility classes).
  - **views:** `com.example.vista` (each view has a constructor `(Controlador controlador)` and exposes `public JPanel pantalla()`).
  - **models / dao:** `com.example.modelo`, `com.example.dao` (plain JDBC DAOs).
- Entry point: `com.example.App` — calls `Fonts.applyDefaultOpenSans()`, builds a `DBConnection` (usually `MySQLConnection`) and a `Controlador`, then calls `controlador.iniciarAplicacion()`.

## Navigation & UI patterns 🔧
- `ControladorNavegacion` initializes all views and manages navigation with two CardLayouts (parent/child).
  - Top-level parent keys: `"inicioSesion"`, `"recuperarCuenta"`, `"entrarSistema"`.
  - Common child keys (inside authenticated area): `"panelControl"`, `"concederPrestamo"`, `"devolverPrestamo"`, `"ejemplares"`, `"gestionUsuarios"`, `"sancionManual"`, `"publicaciones"`.
- Always change screens with `ControladorNavegacion.cambiarPantallaPadre(...)` or `cambiarPantallaHijo(...)` — do not call `CardLayout.show` directly on a view (it often leads to "wrong parent for CardLayout" issues).
- Adding a new view:
  1. Create `src/main/java/com/example/vista/MiVista.java` with constructor `(Controlador controlador)` and `public JPanel pantalla()`.
  2. Register/instantiate it in `ControladorNavegacion`'s constructor (look where other views are created: e.g., `new GestionUsuarios(controlador)`).
  3. Navigate with `controlador.getControladorNavegacion().cambiarPantallaHijo("miVista")`.
- Fonts: `utilities/Fonts.applyDefaultOpenSans()` expects OpenSans TTFs in `src/main/resources/fonts/` (there's a fallback to system fonts).

## DB & DAOs (concrete patterns) 🗄️
- `DBConnection` is an interface (see `conexiones/DBConnection.java`). `MySQLConnection` reads `mysql.url`, `mysql.user`, `mysql.password` from `application.properties` via `ConfigLoader`.
- Important: `MySQLConnection.getConnection()` catches SQLException, prints to stderr and **returns null** on failure — many controllers/DAOs use try-with-resources directly on `dbConnection.getConnection()` and can NPE if DB is down. Always ensure the DB is reachable in integration runs, or add explicit null checks.
- DAOs use plain JDBC & try-with-resources. Patterns:
  - Provide two overloads for mutating operations: one that opens its own Connection and one that accepts a Connection so it can participate in a transaction (example: `PublicacionDAO.insertarPublicacion(...)` and `insertarPublicacion(Connection, ...)`).
  - Error signaling is consistent: return `-1` for generated id errors, `false` for boolean failures, `null` for missing objects.
  - DAO constructors usually validate: e.g., `new PublicacionDAO(dbConnection)` throws if null — prefer injecting a `DBConnection` (test doubles are easy to pass).

## Transactions & examples 🔁
- Example transaction flow (already used in project): `ControladorNuevaPublicacionDialog` opens a `Connection conexion = dbConnection.getConnection(); conexion.setAutoCommit(false);` then calls `publicacionDAO.insertarPublicacion(conexion, ...)`, `insertarLibro(conexion, ...)`, `insertarLibroAutor(conexion, ...)`. If any insert fails, the controller rolls back; otherwise it commits.
- When adding multi-step persistence, use the `Connection`-accepting DAO overloads and handle rollback/commit in the controller.

## Build / Run / Debug / Docker 🧰
- JDK: **21** (maven compiler set to 21 in `pom.xml`).
- Build: `mvn -DskipTests package` (produces `target/classes`).
- Run: `java -cp target/classes com.example.App` (or run `com.example.App` from your IDE).
- Debug (JDWP):
  - Example: `java -agentlib:jdwp=transport=dt_socket,server=y,suspend=y,address=*:5005 -cp target/classes com.example.App`.
- Tests: `mvn test` (JUnit 4). Run a single test with `mvn -Dtest=ClassName test`.
- DB-backed tests: start services with `docker compose up -d` — docker-compose sets up `mysql` (container name `mysql_db`, port 3306) and `phpmyadmin` (port 8080). `inicializacion.sql` is mounted and will seed the DB on the container's first start.

## Conventions & gotchas ⚠️
- Project language: UI strings, variable names and comments are Spanish — keep translations/tests consistent.
- Resources must be in `src/main/resources` to be available at runtime (fonts, images, `application.properties`).
- Logging: DAOs and controllers use `System.out/err` (no centralized logger) — surface errors explicitly when adding new code.
- Default constructors: some controllers provide both a constructor that accepts a `DBConnection` and a no-arg/default one that creates `new MySQLConnection()` (e.g., `ControladorEliminarPublicacion`). Prefer dependency injection in tests to control DB access.
- Be defensive: many places call `dbConnection.getConnection()` inside a try-with-resources without checking for null — starting the app with the DB down may cause immediate failures; tests that require DB should start the dockerized DB first.

## Quick recipes (examples) 💡
- Add a new screen (example):
  - Create `MiVista` in `com.example.vista` with `(Controlador controlador)` and `public JPanel pantalla()`.
  - Register it in `ControladorNavegacion`'s constructor next to other views.
  - Navigate via `controlador.getControladorNavegacion().cambiarPantallaHijo("miVista")`.
- Add a DAO method:
  - Follow `PublicacionDAO` patterns: add `method(params)` and `method(Connection conexion, params)` variants; return `-1`/`false`/`null` on errors.
- Writing integration tests:
  - Bring up DB: `docker compose up -d`, wait for it to accept connections, then `mvn -Dtest=NameTest test`.

## Files to inspect when debugging (start here)
- `src/main/java/com/example/App.java` (startup)
- `src/main/java/com/example/controlador/ControladorNavegacion.java` (view wiring, keys)
- `src/main/java/com/example/controlador/Controlador.java` (central DI/constructors)
- `src/main/java/com/example/conexiones/MySQLConnection.java` (DB connection; returns `null` on failure)
- `src/main/java/com/example/utilities/ConfigLoader.java` (loads `application.properties` at class-load time)
- `src/main/java/com/example/dao/PublicacionDAO.java` (DAO patterns + transactional overloads)
- `src/main/java/com/example/controlador/ControladorNuevaPublicacionDialog.java` (transaction example)
- `inicializacion.sql`, `docker-compose.yml`, `src/main/resources/application.properties`, `src/main/resources/fonts/`

## Configuration / secrets 🔑
- Application properties live at `src/main/resources/application.properties`. The app reads `mysql.url`, `mysql.user`, `mysql.password` via `ConfigLoader` on class load.
- Docker Compose contains a ready-to-use MySQL setup (see `docker-compose.yml`):
  - DB name: `biblioteca`
  - User: `biblioteca_user`
  - Password: `abc123.`
  - Container name: `mysql_db` (port `3306` exposed)
- Important: `MySQLConnection.getConnection()` catches SQL errors, prints to stderr, and **returns `null`** on failure — check for `null` or ensure the DB is reachable in test/CI runs.

## Tests & CI 🧪
- Tests use **JUnit 4** and are under `test/java/com/example/` (see names like `NuevoUsuarioTest`). Maven surefire reports appear in `target/surefire-reports/`.
- Many integration tests rely on a seeded DB (`inicializacion.sql`) — start the DB before running tests: `docker compose up -d` then `mvn test` or `mvn -Dtest=ClassName test` for a single test.
- For CI, start the MySQL service and wait for it to accept connections before running `mvn test`; consider a health-check or a short wait loop to avoid flaky failures.

---
If helpful I can:
- Add a small GitHub Actions workflow that brings up MySQL, waits for readiness, runs `mvn test`, and uploads surefire artifacts (unit + integration test support) ✅
- Add a short integration test template that shows how to bootstrap the DB and assert a DAO method uses the seeded data ✅

Would you like me to add either of those? Any part of these instructions unclear or missing examples you want included?