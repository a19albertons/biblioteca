# GitHub Copilot / AI Agent Instructions for this repo ✅

Concise, actionable guidance to help an AI agent be productive quickly in this Java Swing project.

## Big picture
- Desktop Java Swing app (JDK 21) for library management. MVC-ish layout: **com.example.controlador** (controllers), **com.example.vista** (views that expose `pantalla()` -> `JPanel`), and **com.example.modelo** (domain models).
- App bootstrap: `com.example.App` → creates `Controlador` and `ControladorNavegacion` which wires views into a `CardLayout`.

## Key files & patterns
- Entry: `src/main/java/com/example/App.java` (startup, fonts, and main window).
- Navigation: `ControladorNavegacion.java` (uses `CardLayout`, primary keys: parent/child panels; methods `cambiarPantallaPadre(String)` / `cambiarPantallaHijo(String)`).
- Views: `src/main/java/com/example/vista/*.java` — each view exposes `public JPanel pantalla()`; register in `ControladorNavegacion` and navigate with the controller.
- DB adapter: `com.example.conexiones.DBConnection` + `MySQLConnection` (reads `mysql.*` from `application.properties`).
- DAO example: `src/main/java/com/example/dao/UsuarioDAO.java` — follow explicit JDBC usage (no ORM), use `PreparedStatement` and close resources.

## How to run locally (exact commands) 🔧
- Start DB + phpMyAdmin (initial SQL seeded automatically):
  - docker compose up -d
  - phpMyAdmin: http://localhost:8080 (connect to host `mysql` / container `mysql_db`)
- Build & run:
  - mvn -DskipTests package
  - java -cp target/classes com.example.App
- Run tests:
  - mvn test

## Database specifics & gotchas ⚠️
- `inicializacion.sql` (in repo root) seeds schema and sample data and is mounted into the MySQL container at `/docker-entrypoint-initdb.d/`.
- Triggers in the SQL auto-generate the `usuario` field for `usuarios`. Note: `Usuario.setUsuario()` exists in code but the DB trigger may override values—be careful when writing insert/update tests.
- Default docker-compose service names: `mysql` (container `mysql_db`, port 3306) and `phpmyadmin` (port 8080).

## Conventions & style
- Language: source code and comments are primarily **Spanish** — prefer Spanish for variable names, UI strings, and test texts.
- UI/Fonts: bundled Open Sans TTFs live in `src/main/resources/fonts/`. `Fonts.applyDefaultOpenSans()` is called in `App.main` — use `Fonts.openSans(size)` when specific sizes are needed.
- Resources: load images/assets via `getResource("/filename")` and put them in `src/main/resources` so they are present in `target/classes`.
- No ORM: use direct JDBC with `PreparedStatement` and explicit resource closing (see `UsuarioDAO` as a pattern).

## Tests & CI notes
- Unit tests use JUnit 4.11; current coverage is minimal (`AppTest`).
- No CI workflow exists yet; for CI, recommended steps are: bring up MySQL with `docker compose up -d`, run an integration test that asserts `DBConnection.getConnection()` and then `mvn test`.

## Common tasks (quick recipes) 💡
- Add a new screen:
  1. Create `src/main/java/com/example/vista/MiVista.java` implementing `pantalla()` returning a `JPanel`.
  2. Instantiate and add to `ControladorNavegacion` (`panelPrincipal.add(miVista.pantalla(), "miVista");`).
  3. Navigate using `controlador.getControladorNavegacion().cambiarPantallaHijo("miVista");`.

- Fix DB connection failures:
  - Ensure `docker compose up -d` is running, verify `mysql_db` container is healthy, confirm credentials in `src/main/resources/application.properties` match the docker-compose env vars.

## Where to look
- Start reading: `App.java` → `controlador/Controlador.java` → `controlador/ControladorNavegacion.java` → `vista/*` classes.
- DB schema & test data: `inicializacion.sql`.

---
If you'd like, I can:
- add a small integration test that brings up the DB and asserts `DBConnection.getConnection()` (useful for CI), or
- add a minimal GitHub Actions workflow that runs `docker compose up -d` and the integration test so PRs validate DB connectivity.

Feedback welcome — tell me if you want more examples or translations into Spanish.