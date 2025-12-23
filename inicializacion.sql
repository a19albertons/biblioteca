CREATE DATABASE IF NOT EXISTS biblioteca;

USE biblioteca;

create table
    usuarios (
        id int AUTO_INCREMENT PRIMARY KEY,
        dni char(9) NOT NULL UNIQUE,
        nombre varchar(50) NOT NULL,
        apellido1 varchar(50) NOT NULL,
        apellido2 varchar(50),
        /* calculado: formato AYYNombreIJ .
           Ejemplo: A25JuanLP -> 'A' + 25 (año 2025) + Nombre (Juan) + 1ª letra apellido1 (L) + 1ª letra apellido2 (P) */
        usuario varchar(100) NOT NULL DEFAULT '' UNIQUE,
        email varchar(100) NOT NULL UNIQUE,
        contrasena varchar(50) NOT NULL,
        /* E=Estudiante, P=Profesor, A=Administrativo, C=Conserje, L=Limpiador */
        tipo char(1) NOT NULL CHECK (Tipo IN ('E', 'P', 'A', 'C', 'L')),
        estado Boolean NOT NULL DEFAULT TRUE /* TRUE=activo, FALSE=desactivado */
    );

/* Tabla de atributo multivaluado modulo */
create table
    modulo (
        id int AUTO_INCREMENT PRIMARY KEY,
        nombre varchar(100) NOT NULL UNIQUE
    );

/* Tabla de atributo multivaluado ciclos */
create table
    ciclos (
        id int AUTO_INCREMENT PRIMARY KEY,
        nombre varchar(100) NOT NULL UNIQUE
    );

/* Tabla de atributo multivaluado temas */
create table
    temas (
        id int AUTO_INCREMENT PRIMARY KEY,
        nombre varchar(100) NOT NULL UNIQUE
    );

/* Tabla de atributo multivaluado autores */
create table
    autores (
        id int AUTO_INCREMENT PRIMARY KEY,
        nombre varchar(100) NOT NULL UNIQUE,
        nacionalidad varchar(50) NOT NULL
    );

create table
    publicaciones (
        id int AUTO_INCREMENT PRIMARY KEY,
        titulo varchar(200) NOT NULL,
        editorial varchar(100) NOT NULL,
        codigo_isbn varchar(20) NOT NULL,
        idioma varchar(50) NOT NULL,
        /* R=Revista, L=Libro */
        tipo char(1) NOT NULL CHECK (tipo IN ('R', 'L')), 
        estado Boolean NOT NULL DEFAULT TRUE
    );

/* Tabla hija de publicaciones */
create table
    revistas (
        id_publicacion int PRIMARY KEY,
        periodicidad varchar(50) NOT NULL,
        num_revista int NOT NULL,
        FOREIGN KEY (id_publicacion) REFERENCES publicaciones (id)
    );

/* Tabla hija de publicaciones */
create table
    libros (
        id_publicacion int PRIMARY KEY,
        num_edicion int NOT NULL,
        fecha_publicacion date NOT NULL,
        FOREIGN KEY (id_publicacion) REFERENCES publicaciones (id)
    );

/* Tabla de ejemplares */
create table
    ejemplares (
        id int AUTO_INCREMENT PRIMARY KEY,
        id_publicacion int NOT NULL,
        num_ejemplar int NOT NULL,
        fecha_adquisicion date NOT NULL,
        estado Boolean NOT NULL DEFAULT TRUE,
        FOREIGN KEY (id_publicacion) REFERENCES publicaciones (id)
    );

/* Tabla de prestamos */
create table
    prestamos (
        id int AUTO_INCREMENT PRIMARY KEY,
        id_usuario int NOT NULL,
        id_ejemplar int NOT NULL,
        fecha_inicio date NOT NULL,
        fecha_fin date NOT NULL,
        estado Boolean NOT NULL DEFAULT TRUE, /* TRUE=sin devolver, FALSE=devuelto */
        FOREIGN KEY (id_usuario) REFERENCES usuarios (id),
        FOREIGN KEY (id_ejemplar) REFERENCES ejemplares (id)
    );

/* Tabla de sanciones */
create table
    sanciones (
        id int AUTO_INCREMENT PRIMARY KEY,
        id_usuario int NOT NULL,
        id_prestamo int NOT NULL,
        inicio_sancion date NOT NULL,
        fin_sancion date NOT NULL,
        descripcion varchar(200) NOT NULL,
        estado Boolean NOT NULL DEFAULT TRUE,
        FOREIGN KEY (id_usuario) REFERENCES usuarios (id),
        FOREIGN KEY (id_prestamo) REFERENCES prestamos (id)
    );

/* Tablas intermedias para atributos multivaluados */
create table
    publicacion_modulo (
        id_publicacion int NOT NULL,
        id_modulo int NOT NULL,
        PRIMARY KEY (id_publicacion, id_modulo),
        FOREIGN KEY (id_publicacion) REFERENCES publicaciones (id),
        FOREIGN KEY (id_modulo) REFERENCES modulo (id)
    );

create table
    publicacion_ciclo (
        id_publicacion int NOT NULL,
        id_ciclo int NOT NULL,
        PRIMARY KEY (id_publicacion, id_ciclo),
        FOREIGN KEY (id_publicacion) REFERENCES publicaciones (id),
        FOREIGN KEY (id_ciclo) REFERENCES ciclos (id)
    );

create table
    publicacion_tema (
        id_publicacion int NOT NULL,
        id_tema int NOT NULL,
        PRIMARY KEY (id_publicacion, id_tema),
        FOREIGN KEY (id_publicacion) REFERENCES publicaciones (id),
        FOREIGN KEY (id_tema) REFERENCES temas (id)
    );

create table
    libros_autores (
        id_libro int NOT NULL,
        id_autor int NOT NULL,
        PRIMARY KEY (id_libro, id_autor),
        FOREIGN KEY (id_libro) REFERENCES libros (id_publicacion),
        FOREIGN KEY (id_autor) REFERENCES autores (id)
    );

/* Triggers para generar el campo usuario según el formato AYYNombreIJ */
DROP TRIGGER IF EXISTS usuarios_before_insert;
DROP TRIGGER IF EXISTS usuarios_before_update;

DELIMITER $$
CREATE TRIGGER usuarios_before_insert
BEFORE INSERT ON usuarios
FOR EACH ROW
BEGIN
  SET NEW.usuario = CONCAT(
    'A',
    RIGHT(YEAR(CURDATE()), 2),
    NEW.nombre,
    COALESCE(SUBSTRING(NEW.apellido1,1,1), ''),
    COALESCE(SUBSTRING(NEW.apellido2,1,1), '')
  );
END$$

CREATE TRIGGER usuarios_before_update
BEFORE UPDATE ON usuarios
FOR EACH ROW
BEGIN
  SET NEW.usuario = CONCAT(
    'A',
    RIGHT(YEAR(CURDATE()), 2),
    NEW.nombre,
    COALESCE(SUBSTRING(NEW.apellido1,1,1), ''),
    COALESCE(SUBSTRING(NEW.apellido2,1,1), '')
  );
END$$
DELIMITER ;

-- ==========================
-- Datos de prueba / Población
-- Centro educativo: Informática (alumnos, profes, módulos, recursos...)
-- ==========================

-- Módulos
INSERT INTO modulo (id, nombre) VALUES
(1,'Programación'),
(2,'Bases de Datos'),
(3,'Redes y Comunicaciones'),
(4,'Desarrollo Web'),
(5,'Sistemas Operativos'),
(6,'Seguridad Informática'),
(7,'Ingeniería de Software');

-- Ciclos
INSERT INTO ciclos (id, nombre) VALUES
(1,'DAM'),
(2,'DAW'),
(3,'ASIR'),
(4,'CFGS Desarrollo de Aplicaciones Multiplataforma');

-- Temas
INSERT INTO temas (id, nombre) VALUES
(1,'Algoritmos y Estructuras de Datos'),
(2,'SQL y Diseño de Bases de Datos'),
(3,'Administración de Sistemas'),
(4,'Desarrollo Frontend'),
(5,'Desarrollo Backend'),
(6,'Redes'),
(7,'Seguridad'),
(8,'Programación Java'),
(9,'Programación Python');

-- Autores
INSERT INTO autores (id, nombre, nacionalidad) VALUES
(1,'Ana García','ES'),
(2,'Carlos Pérez','ES'),
(3,'María López','ES'),
(4,'John Doe','US'),
(5,'Pablo Martínez','ES');

-- Publicaciones (libros = 'l', revistas = 'r')
INSERT INTO publicaciones (id, titulo, editorial, codigo_isbn, idioma, tipo, estado) VALUES
(1,'Introducción a la Programación con Java','Editorial Edu','978-1-23456-789-0','Español','l',TRUE),
(2,'SQL Avanzado y Optimización','Editorial Datos','978-1-23456-789-1','Español','l',TRUE),
(3,'Revista Tecnología Educativa Vol.1','Revista Edu','RV-2024-001','Español','r',TRUE),
(4,'Redes y Comunicaciones 5ª Ed.','RedesPress','978-1-23456-789-2','Español','l',TRUE),
(5,'Desarrollo Web Moderno','WebBooks','978-1-23456-789-3','Español','l',TRUE),
(6,'Revista Sistemas y Seguridad Vol.3','Revista Sistemas','RV-2025-003','Español','r',TRUE);

-- Libros (detalles para tipo 'l')
INSERT INTO libros (id_publicacion, num_edicion, fecha_publicacion) VALUES
(1,3,'2021-09-01'),
(2,2,'2020-05-10'),
(4,5,'2019-08-15'),
(5,1,'2022-03-20');

-- Revistas (detalles para tipo 'r')
INSERT INTO revistas (id_publicacion, periodicidad, num_revista) VALUES
(3,'Trimestral',1),
(6,'Semestral',3);

-- Ejemplares (copias físicas)
INSERT INTO ejemplares (id, id_publicacion, num_ejemplar, fecha_adquisicion, estado) VALUES
(1,1,1,'2021-09-10',TRUE),
(2,1,2,'2022-01-15',TRUE),
(3,2,1,'2020-06-01',TRUE),
(4,3,1,'2024-02-02',TRUE),
(5,4,1,'2019-09-01',TRUE),
(6,5,1,'2022-03-22',TRUE),
(7,6,1,'2025-01-05',TRUE),
(8,2,2,'2021-11-20',TRUE),
(9,5,2,'2023-07-10',TRUE),
(10,1,3,'2024-09-12',TRUE);

-- Usuarios (alumnos A, profesores P, empleados E, conserje C, limpiador L)
INSERT INTO usuarios (id, dni, nombre, apellido1, apellido2, email, contrasena, Tipo, estado) VALUES
(1,'11111111A','Juan','Pérez','García','juan.perez@example.com','passJuan1','A',TRUE),
(2,'22222222B','Lucía','Martínez','Sánchez','lucia.martinez@example.com','passLucia2','A',TRUE),
(3,'33333333C','Miguel','López','Pena','miguel.lopez@example.com','passMiguel3','P',TRUE),
(4,'44444444D','Ana','García','Ramírez','ana.garcia@example.com','passAna4','P',TRUE),
(5,'55555555E','Carlos','Ruiz','Fernández','carlos.ruiz@example.com','passCarlos5','E',TRUE),
(6,'66666666F','Laura','Díaz','Torres','laura.conserje@example.com','passLaura6','C',TRUE),
(7,'77777777G','Sergio','Navarro','Gómez','sergio.navarro@example.com','passSergio7','L',TRUE),
(8,'88888888H','Marta','Ortega','Sanz','marta.ortega@example.com','passMarta8','A',TRUE),
(9,'99999999I','Isabel','Soto','Molina','isabel.soto@example.com','passIsabel9','A',TRUE),
(10,'00000000J','David','Giménez','Ruano','david.gimenez@example.com','passDavid10','P',TRUE);

-- Usuarios de prueba adicionales para tests de inicio de sesión
-- id 11: cuenta desactivada (para probar mensaje de cuenta desactivada)
-- id 12: contraseña vacía (para probar validaciones de formulario)
-- id 13: conserje de prueba (para probar la restricción de acceso de conserjes)
INSERT INTO usuarios (id, dni, nombre, apellido1, apellido2, email, contrasena, Tipo, estado) VALUES
(11,'12121212K','Bloqueado','Usuario','Test','bloqueado.usuario@example.com','passBloq11','A',FALSE),
(12,'13131313L','NoPass','Usuario','Test','nopass.usuario@example.com','','A',TRUE),
(13,'14141414M','ConserjePrueba','Soler','Márquez','conserje@example.com','conserje123','C',TRUE);

-- Préstamos (algunos abiertos, algunos cerrados)
INSERT INTO prestamos (id, id_usuario, id_ejemplar, fecha_inicio, fecha_fin, estado) VALUES
(1,1,1,'2025-12-01','2025-12-15',TRUE),
(2,2,2,'2025-11-01','2025-11-15',FALSE),
(3,3,3,'2025-10-05','2025-10-20',TRUE),
(4,8,9,'2025-12-05','2025-12-19',TRUE);

-- Sanciones (ligadas a préstamos)
INSERT INTO sanciones (id, id_usuario, id_prestamo, inicio_sancion, fin_sancion, descripcion, estado) VALUES
(1,1,1,'2025-12-16','2026-01-16','Retraso en devolución',TRUE),
(2,2,2,'2025-11-16','2025-12-16','Daño en libro (tapa)',FALSE);

-- Relaciones publicación <-> módulo / ciclo / tema
INSERT INTO publicacion_modulo (id_publicacion, id_modulo) VALUES
(1,1), -- Java -> Programación
(2,2), -- SQL -> Bases de Datos
(3,4), -- Revista Tecnología Educativa -> Desarrollo Web (ejemplo)
(4,3), -- Redes
(5,4), -- Desarrollo Web
(6,6); -- Seguridad

INSERT INTO publicacion_ciclo (id_publicacion, id_ciclo) VALUES
(1,1), -- libro Java -> DAM
(1,2), -- también DAW
(2,1),
(4,3),
(5,2);

INSERT INTO publicacion_tema (id_publicacion, id_tema) VALUES
(1,8), -- Java
(1,1), -- Algoritmos
(2,2), -- SQL
(4,6), -- Redes
(5,4), -- Frontend
(6,7); -- Seguridad

-- Relación libros <-> autores
INSERT INTO libros_autores (id_libro, id_autor) VALUES
(1,1),
(2,2),
(4,4),
(5,5);

-- Datos extra: crear algunos préstamos históricos para tests
INSERT INTO prestamos (id, id_usuario, id_ejemplar, fecha_inicio, fecha_fin, estado) VALUES
(5,9,5,'2024-01-10','2024-01-24',FALSE),
(6,10,6,'2024-03-12','2024-03-26',FALSE);

-- Préstamo dinámico: usa la fecha de importación (CURDATE()) para fecha_inicio y fecha_fin = fecha_inicio + 14 días
INSERT INTO prestamos (id_usuario, id_ejemplar, fecha_inicio, fecha_fin, estado) VALUES
(1, 10, CURDATE(), DATE_ADD(CURDATE(), INTERVAL 14 DAY), TRUE);

-- Fin de datos de prueba

