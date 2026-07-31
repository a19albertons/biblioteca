package com.biblioteca.controlador;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDate;

import com.biblioteca.conexiones.DBConnection;
import com.biblioteca.dao.AutorDAO;
import com.biblioteca.dao.CicloDAO;
import com.biblioteca.dao.ModuloDAO;
import com.biblioteca.dao.PublicacionDAO;
import com.biblioteca.dao.TemaDAO;

/**
 * Controlador responsable de la edición de publicaciones.
 *
 * Provee métodos para obtener los detalles de una publicación y para editarla
 * (tanto libros como revistas). Todas las operaciones de edición se ejecutan
 * dentro de una transacción JDBC para mantener la consistencia de las tablas
 * relacionadas (publicaciones, libros/revistas y las tablas multivaluadas).
 */
public class ControladorEditarPublicacionDialog {
    /**
     * DBConnection para conexiones a la base de datos
     */
    private final DBConnection dbConnection;

    /**
     * Constructor que permite inyectar una `DBConnection` (recomendado para tests
     * y para la nueva arquitectura).
     * 
     * @param dbConnection
     */
    public ControladorEditarPublicacionDialog(DBConnection dbConnection) {
        if (dbConnection == null) {
            throw new IllegalArgumentException("DBConnection cannot be null");
        }
        this.dbConnection = dbConnection;
    }

    /**
     * Obtiene un conjunto de campos que representan los detalles de la publicación
     * tal y como requiere la vista de edición.
     *
     * @param id id de la publicación
     * @return arreglo con campos: tipo, titulo, codigo_isbn, idioma, temasCSV,
     *         modulosCSV, ciclosCSV, editorial, num_edicion, fecha_publicacion,
     *         autoresCSV, periodicidad, id — o null si no se encuentra
     */
    public String[] obtenerDetallesPublicacion(int id) {
        PublicacionDAO dao = new PublicacionDAO(this.dbConnection);
        return dao.obtenerPublicacionDetallesPorId(id);
    }

    /**
     * Edita una publicación de tipo Libro (transaccional).
     *
     * @param idPublicacion id de la publicación
     * @param isbn          código ISBN o identificador
     * @param titulo        título de la publicación
     * @param idioma        idioma
     * @param temasCsv      CSV de temas
     * @param modulosCsv    CSV de módulos
     * @param ciclosCsv     CSV de ciclos
     * @param editorial     editorial
     * @param numEdicion    número de edición
     * @param fechaPublic   fecha de publicación
     * @param autoresCsv    CSV de autores
     * @return true si la edición fue satisfactoria (commit realizado), false en
     *         caso de error
     */
    public boolean editarPublicacionLibro(int idPublicacion, String isbn, String titulo, String idioma,
            String temasCsv, String modulosCsv, String ciclosCsv, String editorial, int numEdicion,
            LocalDate fechaPublic, String autoresCsv) {

        try (Connection conexion = this.dbConnection.getConnection();) {
            if (conexion == null) {
                System.out.println("No se puede obtener conexión a BD");
                return false;
            }

            // Creación de DAOs para gestionar una transacción completa
            PublicacionDAO publicacionDAO = new PublicacionDAO(this.dbConnection);
            AutorDAO autorDAO = new AutorDAO(conexion);
            ModuloDAO moduloDAO = new ModuloDAO(conexion);
            TemaDAO temaDAO = new TemaDAO(this.dbConnection);
            CicloDAO cicloDAO = new CicloDAO(conexion);

            try {
                // Begin transaction
                conexion.setAutoCommit(false);

                // Actualizar tabla publicaciones
                if (!publicacionDAO.actualizarPublicacion(conexion, idPublicacion, titulo, editorial, isbn, idioma,
                        'L')) {
                    conexion.rollback();
                    return false;
                }

                // Actualizar o insertar fila libros
                if (!publicacionDAO.actualizarLibro(conexion, idPublicacion, numEdicion, Date.valueOf(fechaPublic))) {
                    conexion.rollback();
                    return false;
                }

                // Si estamos convirtiendo a Libro, asegurarnos de eliminar la fila en revistas
                // (si existía)
                try (PreparedStatement delRev = conexion
                        .prepareStatement("DELETE FROM revistas WHERE id_publicacion = ?")) {
                    delRev.setInt(1, idPublicacion);
                    delRev.executeUpdate();
                } catch (Exception ex) {
                    System.out.println("Error eliminando fila revista anterior: " + ex.getMessage());
                }

                // Eliminar relaciones previas
                if (!publicacionDAO.eliminarRelacionesPublicacion(conexion, idPublicacion)) {
                    conexion.rollback();
                    return false;
                }

                // Procesar autores
                if (autoresCsv != null && !autoresCsv.trim().isEmpty()) {
                    String[] autores = autoresCsv.split(",");
                    for (String a : autores) {
                        String nombre = a.trim();
                        if (nombre.isEmpty())
                            continue;
                        int idAutor = autorDAO.obtenerOCrearPorNombre(nombre);
                        if (idAutor == -1) {
                            conexion.rollback();
                            return false;
                        }
                        if (!publicacionDAO.insertarLibroAutor(conexion, idPublicacion, idAutor)) {
                            conexion.rollback();
                            return false;
                        }
                    }
                }

                // Procesar modulos
                if (modulosCsv != null && !modulosCsv.trim().isEmpty()) {
                    String[] modulos = modulosCsv.split(",");
                    for (String m : modulos) {
                        String nombre = m.trim();
                        if (nombre.isEmpty())
                            continue;
                        int idModulo = moduloDAO.obtenerOCrear(conexion, nombre);
                        if (idModulo == -1) {
                            conexion.rollback();
                            return false;
                        }
                        if (!publicacionDAO.insertarPublicacionModulo(conexion, idPublicacion, idModulo)) {
                            conexion.rollback();
                            return false;
                        }
                    }
                }

                // Procesar ciclos
                if (ciclosCsv != null && !ciclosCsv.trim().isEmpty()) {
                    String[] ciclos = ciclosCsv.split(",");
                    for (String cc : ciclos) {
                        String nombre = cc.trim();
                        if (nombre.isEmpty())
                            continue;
                        int idCiclo = cicloDAO.obtenerOCrear(nombre);
                        if (idCiclo == -1) {
                            conexion.rollback();
                            return false;
                        }
                        if (!publicacionDAO.insertarPublicacionCiclo(conexion, idPublicacion, idCiclo)) {
                            conexion.rollback();
                            return false;
                        }
                    }
                }

                // Procesar temas
                if (temasCsv != null && !temasCsv.trim().isEmpty()) {
                    String[] temas = temasCsv.split(",");
                    for (String t : temas) {
                        String nombre = t.trim();
                        if (nombre.isEmpty())
                            continue;
                        int idTema = temaDAO.obtenerOCrear(conexion, nombre);
                        if (idTema == -1) {
                            conexion.rollback();
                            return false;
                        }
                        if (!publicacionDAO.insertarPublicacionTema(conexion, idPublicacion, idTema)) {
                            conexion.rollback();
                            return false;
                        }
                    }
                }

                conexion.commit();
                return true;
            } catch (Exception e) {
                try {
                    conexion.rollback();
                } catch (Exception ex) {
                    System.out.println("Error al hacer rollback: " + ex.getMessage());
                }
                System.out.println(e.getMessage());
                System.out.println(e.getCause());
                return false;
            } finally {
                try {
                    conexion.setAutoCommit(true);
                    conexion.close();
                } catch (Exception ex) {
                    System.out.println("Error cerrando conexión: " + ex.getMessage());
                }
            }
        } catch (SQLException e1) {
            System.out.println("Error al obtener conexión: " + e1.getMessage());
            return false;
        }

    }

    /**
     * Edita una publicación de tipo Revista (transaccional).
     *
     * @param idPublicacion id de la publicación
     * @param isbn          código o identificador
     * @param titulo        título
     * @param idioma        idioma
     * @param temasCsv      CSV de temas
     * @param modulosCsv    CSV de módulos
     * @param ciclosCsv     CSV de ciclos
     * @param editorial     editorial
     * @param periodicidad  periodicidad (texto)
     * @return true si la edición fue satisfactoria y commit realizado
     */
    public boolean editarPublicacionRevista(int idPublicacion, String isbn, String titulo, String idioma,
            String temasCsv, String modulosCsv, String ciclosCsv, String editorial, String periodicidad) {

        try (Connection conexion = this.dbConnection.getConnection();) {
            if (conexion == null) {
                System.out.println("No se puede obtener conexión a BD");
                return false;
            }

            // Creación de DAOs para gestionar una transacción completa
            PublicacionDAO publicacionDAO = new PublicacionDAO(this.dbConnection);
            ModuloDAO moduloDAO = new ModuloDAO(conexion);
            TemaDAO temaDAO = new TemaDAO(this.dbConnection);
            CicloDAO cicloDAO = new CicloDAO(conexion);

            try {
                // Begin transaction
                conexion.setAutoCommit(false);

                // Actualizar tabla publicaciones
                if (!publicacionDAO.actualizarPublicacion(conexion, idPublicacion, titulo, editorial, isbn, idioma,
                        'R')) {
                    conexion.rollback();
                    return false;
                }

                // Actualizar o insertar fila revistas
                if (!publicacionDAO.actualizarRevista(conexion, idPublicacion, periodicidad)) {
                    conexion.rollback();
                    return false;
                }

                // Si estamos convirtiendo a Revista, eliminar la fila en libros si existiera
                try (PreparedStatement delLib = conexion
                        .prepareStatement("DELETE FROM libros WHERE id_publicacion = ?")) {
                    delLib.setInt(1, idPublicacion);
                    delLib.executeUpdate();
                } catch (Exception ex) {
                    System.out.println("Error eliminando fila libro anterior: " + ex.getMessage());
                }

                // Eliminar relaciones previas
                if (!publicacionDAO.eliminarRelacionesPublicacion(conexion, idPublicacion)) {
                    conexion.rollback();
                    return false;
                }

                // Procesar modulos
                if (modulosCsv != null && !modulosCsv.trim().isEmpty()) {
                    String[] modulos = modulosCsv.split(",");
                    for (String m : modulos) {
                        String nombre = m.trim();
                        if (nombre.isEmpty())
                            continue;
                        int idModulo = moduloDAO.obtenerOCrear(conexion, nombre);
                        if (idModulo == -1) {
                            conexion.rollback();
                            return false;
                        }
                        if (!publicacionDAO.insertarPublicacionModulo(conexion, idPublicacion, idModulo)) {
                            conexion.rollback();
                            return false;
                        }
                    }
                }

                // Procesar ciclos
                if (ciclosCsv != null && !ciclosCsv.trim().isEmpty()) {
                    String[] ciclos = ciclosCsv.split(",");
                    for (String cc : ciclos) {
                        String nombre = cc.trim();
                        if (nombre.isEmpty())
                            continue;
                        int idCiclo = cicloDAO.obtenerOCrear(nombre);
                        if (idCiclo == -1) {
                            conexion.rollback();
                            return false;
                        }
                        if (!publicacionDAO.insertarPublicacionCiclo(conexion, idPublicacion, idCiclo)) {
                            conexion.rollback();
                            return false;
                        }
                    }
                }

                // Procesar temas
                if (temasCsv != null && !temasCsv.trim().isEmpty()) {
                    String[] temas = temasCsv.split(",");
                    for (String t : temas) {
                        String nombre = t.trim();
                        if (nombre.isEmpty())
                            continue;
                        int idTema = temaDAO.obtenerOCrear(conexion, nombre);
                        if (idTema == -1) {
                            conexion.rollback();
                            return false;
                        }
                        if (!publicacionDAO.insertarPublicacionTema(conexion, idPublicacion, idTema)) {
                            conexion.rollback();
                            return false;
                        }
                    }
                }

                conexion.commit();
                return true;
            } catch (Exception e) {
                try {
                    // Rollback en caso de error
                    conexion.rollback();
                } catch (Exception ex) {
                    System.out.println("Error al hacer rollback: " + ex.getMessage());
                }
                System.out.println(e.getMessage());
                System.out.println(e.getCause());
                return false;
            } finally {
                try {
                    conexion.setAutoCommit(true);
                    conexion.close();
                } catch (Exception ex) {
                    System.out.println("Error cerrando conexión: " + ex.getMessage());
                }
            }

        }
        catch (SQLException e1) {
            System.out.println("Error al obtener conexión: " + e1.getMessage());
            return false;
        }

    }

}