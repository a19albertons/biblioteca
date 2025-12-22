# GitHub Copilot / AI Agent Instructions for this repo ✅

Short, targeted guidance to help an AI agent be productive immediately in this project.

## Big picture (what this app is)
- Desktop Java Swing application (JDK 21) implementing a small **library management** UI.
- Small MVC-ish layout: **com.example.controlador** (controller entry points), **com.example.vista** (Swing views, each exposing a `pantalla()` -> `JPanel`), and **com.example.modelo** (data models that mirror the DB schema).
- Entry point: `com.example.App` -> creates `Controlador` -> `ControladorNavegacion` which builds the UI and shows the initial screen.

## Key files & patterns (quick map)
- Main: `src/main/java/com/example/App.java` ✅
- Navigation: `ControladorNavegacion.java` — uses `CardLayout` and two named-level panels (`panelPadre` and `panelHijo`). Use `cambiarPantallaPadre(String)` and `cambiarPantallaHijo(String)` to switch screens.
- Views: `src/main/java/com/example/vista/*.java` — each view returns a `JPanel` via `pantalla()`; add and register views in `ControladorNavegacion`.
- Config: `src/main/resources/application.properties` — loaded via `com.example.utilities.ConfigLoader.get(key)`.
- DB: `src/main/resources/inicializacion.sql` seeds schema and test data; contains triggers that auto-generate the `usuario` value for `usuarios` records.
- DB connector adapter: `com.example.conexiones.MySQLConnection` implements `DBConnection` and reads keys `mysql.url`, `mysql.user`, `mysql.password` from the properties file.

## How to run locally (exact commands) 🔧
- Start DB and phpMyAdmin (seeds DB automatically):
  - docker-compose up -d
  - phpMyAdmin available at http://localhost:8080
- Build and run the app (examples):
  - mvn -DskipTests package
  - java -cp target/classes com.example.App
- Run tests:
  - mvn test

Note: the project already includes the MySQL JDBC driver (`mysql-connector-java` 8.0.33) in `pom.xml`.

If you experience driver/connection errors, confirm the DB container is running via `docker-compose up -d` and that `src/main/resources/application.properties` matches the container credentials (example values already present):

```properties
mysql.url = jdbc:mysql://localhost:3306/biblioteca
mysql.user = biblioteca_user
mysql.password = abc123.
```

If you need a newer driver, update the dependency version in `pom.xml`.

## Local debugging tips 🐞
- Run `com.example.App` from your IDE (set the main class to `com.example.App`). Ensure the MySQL container is up first (`docker-compose up -d`).
- Use phpMyAdmin at http://localhost:8080 to inspect and modify seeded test data (schema is seeded from `inicializacion.sql`).
- If UI fonts don't render as expected, confirm the Open Sans TTF files exist in `src/main/resources/fonts/`; `App.main` calls `Fonts.applyDefaultOpenSans()` early.
- For DB-only validation in CI or locally, start the DB (`docker-compose up -d`) and run the integration test (recommended) or `mvn test`.

## Project-specific conventions & gotchas ⚠️
- Language: code and comments are in **Spanish**; prefer Spanish for variable names and UI strings when adding new code or tests.
- UI pattern: Add new view classes under `com.example.vista` with a `pantalla()` method returning a `JPanel`. Register it in `ControladorNavegacion` and choose a unique string key (e.g., "miVista"). Use `cambiarPantallaHijo("miVista")` to show it.
- DB schema: `inicializacion.sql` is authoritative for the local dev DB; it defines foreign keys, triggers (auto-generate `usuario`) and sample data. The SQL file is mounted into the MySQL container at `/docker-entrypoint-initdb.d/` by `docker-compose.yml`.
- Models and DB duplication: `Usuario` class has `setUsuario()` logic **and** the DB triggers also auto-generate `usuario`. Be mindful when inserting/updating: the DB triggers may override the in-memory value.
- No ORM/DAO layer present: the codebase currently uses a simple `DBConnection` adapter; prefer using `PreparedStatement` and explicit resource closing if adding JDBC code.

## Integration points & environment variables
- Runtime config keys in `application.properties`: `mysql.url`, `mysql.user`, `mysql.password` (example values already set for the docker-compose service).
- Static resources (logo) are loaded with `getResource("/logo.png")`. Place resources in `src/main/resources` so they end up in `target/classes` at runtime.

## Fonts & UI styling 🔤
- This project includes embedded **Open Sans** TTF files in `src/main/resources/fonts/` (e.g. `OpenSans-Regular.ttf`, `OpenSans-Bold.ttf`).
- Use `com.example.utilities.Fonts.applyDefaultOpenSans()` early in `main` (it is already called in `App.main`) to register the bundled fonts and apply **Open Sans (plain)** as the UI default via `UIManager`.
- Prefer the helper over hard-coding font names/sizes in views. Use `Fonts.openSans(size)` when you need a specific size, or rely on UI defaults (labels/buttons will inherit Open Sans).
- If you need additional variants (italic, semibold), add the corresponding TTF into `resources/fonts/` — `Fonts` will attempt to load `OpenSans-Regular.ttf` by default; extend it if you need automatic mapping of other variants.
- Rationale: embedding the TTFs ensures consistent rendering across developer machines and CI environments when the OS does not have Open Sans installed.

## Tests & CI notes
- Unit tests use JUnit 4.11. Current tests are minimal (`AppTest`).
- There is no CI configuration in the repo yet; recommended quick checks for a PR: `mvn -DskipTests=false test` and `mvn -DskipTests package`.

## Common work items and short examples 💡
- Add a new view:
  1. Create `src/main/java/com/example/vista/MiVista.java` with `pantalla()` returning a `JPanel`.
  2. Instantiate and register it in `ControladorNavegacion` (e.g., `panelPrincipal.add(miVista.pantalla(), "miVista");`).
  3. Navigate: `controlador.getControladorNavegacion().cambiarPantallaHijo("miVista");`.

- Fix DB connection issues: ensure the DB container is running (`docker-compose up -d`), confirm `src/main/resources/application.properties` matches the container credentials, and verify `mysql-connector-java` exists in `pom.xml` (currently 8.0.33). To change the driver version, update `pom.xml` and run `mvn package`.

## Where to look to learn more
- Start at `App.java` → `Controlador` → `ControladorNavegacion` → `vista/*` classes.
- DB schema and sample data: `inicializacion.sql` (good source of test cases).

---
If you'd like, I can:
- add a short integration test that spins up the MySQL container via `docker-compose` and asserts the `DBConnection.getConnection()` works (good for CI), or
- add a minimal GitHub Actions workflow that runs `docker-compose up -d` and the integration test so PRs validate DB connectivity.

Is anything important missing from this file or would you like a different format or more examples? ✍️
