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

Important: the project currently does NOT declare a MySQL JDBC dependency in `pom.xml`. Without `mysql-connector-java` you will get a "No suitable driver" SQLException at runtime. Add this dependency to `pom.xml` (example):

```xml
<dependency>
  <groupId>com.mysql</groupId>
  <artifactId>mysql-connector-j</artifactId>
  <version>8.0.34</version>
</dependency>
```

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

- Fix DB driver error: add `mysql-connector-j` dependency to `pom.xml` and run `mvn package`.

## Where to look to learn more
- Start at `App.java` → `Controlador` → `ControladorNavegacion` → `vista/*` classes.
- DB schema and sample data: `inicializacion.sql` (good source of test cases).

---
If you'd like, I can:
- add the missing `mysql-connector-j` dependency to `pom.xml` and open a small PR, or
- add a short integration test that spins up a MySQL container via `docker-compose` and asserts a `getConnection()` works.

Is anything important missing from this file or would you like a different format or more examples? ✍️
