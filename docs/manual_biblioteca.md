# Manual de Usuario — Sistema de Gestión de Biblioteca 📚

**Versión:** Sistema de Gestión Académica v1.0  
**Fecha:** 25-12-2025

![Logo](../src/main/resources/logo.png)

---

## Contenido
1. [Introducción](#introducción) ✨
2. [Requisitos mínimos y acceso](#requisitos-mínimos-y-acceso) ⚠️
3. [Primeros pasos / Iniciar sesión](#primeros-pasos--iniciar-sesión) 🔑
4. [Navegación general (barra lateral)](#navegación-general-barra-lateral) 🔧
5. [Panel de control (resúmenes y últimos movimientos)](#panel-de-control-resúmenes-y-últimos-movimientos) 📊
6. [Catálogo de publicaciones (buscar, filtrar, ver)](#catálogo-de-publicaciones-buscar-filtrar-ver) 🔎
7. [Préstamos — conceder libro (paso a paso)](#préstamos--conceder-libro-paso-a-paso) 📖 ➕
8. [Devoluciones — registrar devolución (paso a paso)](#devoluciones--registrar-devolución-paso-a-paso) 🔁
9. [Gestión de usuarios (Socios / Usuarios)](#gestión-de-usuarios-socios--usuarios) 👥
10. [Gestión de publicaciones y ejemplares](#gestión-de-publicaciones-y-ejemplares) ➕📚
11. [Sanciones manuales](#sanciones-manuales) ⚖️
12. [Errores comunes y FAQ](#errores-comunes-y-faq) 💡
13. [Generar PDF (opciones)](#generar-pdf-opciones) 🖨️
14. [Contacto y soporte](#contacto-y-soporte) 🛠️

---

## 1. Introducción ✨

Este manual explica cómo usar la aplicación de gestión de la biblioteca orientada al personal (conserjes). Aquí encontrarás instrucciones paso a paso para realizar las tareas habituales: búsqueda de libros, concesión y devolución de préstamos, gestión de socios/usuarios, administración de sanciones y registro de ejemplares y publicaciones.

---

## 2. Requisitos mínimos y acceso ⚠️

- El sistema es una aplicación de escritorio Java; quien gestiona la instalación debe tener Java (JDK) instalado.
- Solo los usuarios con rol **Conserje** tienen permiso para entrar en la aplicación.
- Si la aplicación no puede acceder a la base de datos (MySQL), algunas funciones mostrarán mensajes de error; en ese caso contacta con soporte.

---

## 3. Primeros pasos / Iniciar sesión 🔑

- Abre la aplicación. Verás la pantalla de **Inicio de sesión**.
- Introduce tu **usuario** y **contraseña** y pulsa **Acceder**.

<!-- ![Inicio de sesión - placeholder](screenshots/inicio_sesion.png) -->

**Mensajes habituales:**
- "Usuario o contraseña incorrectos." → revisa credenciales.
- "La cuenta está desactivada." → contacta con el administrador.
- "Solo los conserjes tienen acceso al sistema." → tu cuenta no tiene permiso.

**Recuperar cuenta:**
- Si olvidaste la contraseña, en la pantalla de inicio haz clic en el enlace de recuperación y sigue el formulario.

---

## 4. Navegación general (barra lateral) 🔧

En el área principal, a la izquierda hay una **Barra lateral** con los botones:
- **Inicio** → Panel de control (resúmenes y últimos movimientos)
- **Catálogo de libros** → Buscar y ver publicaciones y ejemplares
- **Préstamos** → Formulario para conceder préstamos
- **Socios / Usuarios** → Gestión de usuarios (listar, editar, crear, eliminar)
- **Sanciones** → Crear sanciones manuales

<!-- ![Barra lateral - placeholder](screenshots/barra_lateral.png) -->

Pulsa el botón correspondiente para cambiar de sección. El botón activo se resaltará.

---

## 5. Panel de control (resúmenes y últimos movimientos) 📊

El **Panel de Control** muestra:
- Tarjetas resumen: **Préstamos hoy**, **Pendientes**, **Socios activos**.
- Tabla **Últimos movimientos**: muestra actividad reciente (ID ejemplar, libro, estado: PRESTADO/DEVUELTO).

<!-- ![Panel de control - placeholder](screenshots/panel_control.png) -->

**Uso:** consulta rápidamente estadísticas y movimientos recientes para tener visión global.

---

## 6. Catálogo de publicaciones (buscar, filtrar, ver) 🔎

- **Barra de búsqueda**: busca por Título, ISBN o Autor.
- **Filtros**: Ciclos y Editorial.
- Visualización en cards: cada publicación muestra título, ISBN, autores, ciclos, editorial y número de ejemplares disponibles (o “AGOTADO”).
- Botón **+ Nueva Publicación**: abre un diálogo para registrar una publicación.

<!-- ![Catálogo - placeholder](screenshots/catalogo.png) -->

Desde el catálogo puedes abrir la vista de ejemplares de una publicación para ver/añadir ejemplares.

---

## 7. Préstamos — conceder libro (paso a paso) 📖 ➕

Pantalla: **Nuevo prestamo**.

**Pasos**:
1. **Identificar socio (usuario)**
   - Introduce **DNI / ID** y pulsa **Buscar**.
   - Resultado muestra nombre, tipo y estado de sanciones.
2. **Seleccionar ejemplar / libro**
   - Introduce el **ID de Ejemplar**. El sistema detecta la publicación y muestra título/edición.
   - Se autocompletan **Fecha inicio** (hoy) y **Fecha devolución prevista** (reglas por tipo).
3. **Registrar préstamo**
   - Pulsa **Registrar Préstamo**.

<!-- ![Conceder préstamo - placeholder](screenshots/conceder_prestamo.png) -->

**Reglas importantes:**
- Para revistas la devolución puede ser el mismo día; para libros suele aplicarse un plazo estándar (+7 días), ajustado según tipo de usuario.

---

## 8. Devoluciones — registrar devolución (paso a paso) 🔁

Pantalla: **Devolución Préstamo**.

**Pasos**:
1. **Identificar socio** (DNI / ID) y pulsar **Buscar**.
2. **Identificar ejemplar** (ID Ejemplar). El sistema muestra la publicación detectada.
3. **Comprobar existencia de préstamo activo**: la app valida que existe un préstamo activo entre usuario y ejemplar.
4. **Devolver**: pulsa **DEVOLVER PRESTAMO**.

<!-- ![Devolver préstamo - placeholder](screenshots/devolver_prestamo.png) -->

**Resultados:**
- Éxito: "Devolución registrada correctamente" y, si procede, notificación sobre sanción generada.
- Error: muestra motivo (ej. no existe préstamo activo).

---

## 9. Gestión de usuarios (Socios / Usuarios) 👥

Acceso: **Socios / Usuarios** en la barra lateral.

**Funciones**:
- Listado de usuarios con columnas: ID, DNI, Nombre, Tipo, Estado (incluye sanciones).
- **+ Nuevo Usuario**: DNI, Nombre, Apellidos, Email, Tipo. La contraseña inicial se fija al DNI.
- **Editar** y **Eliminar** según permisos.

<!-- ![Gestión usuarios - placeholder](screenshots/gestion_usuarios.png) -->

Al crear/editar/eliminar, la lista se refresca automáticamente.

---

## 10. Gestión de publicaciones y ejemplares ➕📚

**Publicaciones:**
- **+ Nueva Publicación**: wizard paso a paso (datos comunes → datos por tipo).

**Ejemplares:**
- Desde la vista de ejemplares de una publicación, **Añadir nuevo ejemplar** (fecha de adquisición).

<!-- ![Nueva publicación / Nuevo ejemplar - placeholder](screenshots/nueva_publicacion.png) -->

---

## 11. Sanciones manuales ⚖️

Pantalla: **Sancion Manual**.

**Uso:**
1. **Seleccionar usuario** → **Cambiar** abre selector.
2. **Rellenar datos**: motivo, ID ejemplar, descripción, fecha fin.
3. **Aplicar sanción**: la app valida historial y aplica la sanción; notifica acumulaciones.

<!-- ![Sanción manual - placeholder](screenshots/sancion_manual.png) -->

---

## 12. Errores comunes y FAQ 💡

- **No puedo iniciar sesión**: revisa usuario/contraseña; si dice que no eres conserje, pide al admin revisar tu rol.
- **Al conceder préstamo**: verifica estado del usuario (sancionado/alta/baja) y que el ID ejemplar es correcto.
- **Errores de conexión a BD**: contacta al administrador de la base de datos.
- **Recuperar contraseña**: usa la pantalla de recuperación; si no recibes correo, solicita intervención del admin.

---

## 13. Generar PDF (opciones) 🖨️

He preparado este manual en Markdown. Para generar el PDF tienes varias opciones (elige la que prefieras):

1. **Pandoc + LaTeX (calidad alta)**
   - Instalar (Debian/Ubuntu): `sudo apt install pandoc texlive-latex-recommended texlive-fonts-recommended`  
   - Comando: `pandoc docs/manual_biblioteca.md -o manual_biblioteca.pdf --resource-path=.`

2. **wkhtmltopdf (render HTML → PDF)**
   - Instalar: `sudo apt install wkhtmltopdf`  
   - Comando (convertir MD a HTML con `pandoc` o `markdown` y luego `wkhtmltopdf`):
     - `pandoc docs/manual_biblioteca.md -o manual_biblioteca.html`
     - `wkhtmltopdf manual_biblioteca.html manual_biblioteca.pdf`

3. **Generar desde un editor (VS Code) — imprimir a PDF**
   - Abre `docs/manual_biblioteca.md` en VS Code y usa la extensión de Markdown Preview; desde la vista previa imprime a PDF.

<!-- Notas:
- Las imágenes están referenciadas en `docs/screenshots/`. Si quieres que incluya capturas reales, puedo intentar ejecutar la aplicación y tomar las capturas; **pide permiso** para que lo haga (puede fallar si no hay entorno gráfico). Si lo prefieres, puedes ejecutar la app en tu equipo, tomar capturas y colocarlas en `docs/screenshots/` con estos nombres:
  - `inicio_sesion.png`
  - `panel_control.png`
  - `catalogo.png`
  - `conceder_prestamo.png`
  - `devolver_prestamo.png`
  - `gestion_usuarios.png`
  - `sancion_manual.png`
  - `nueva_publicacion.png`

Una vez colocadas, vuelve a generar el PDF con cualquiera de las opciones anteriores para que aparezcan en el documento final. -->