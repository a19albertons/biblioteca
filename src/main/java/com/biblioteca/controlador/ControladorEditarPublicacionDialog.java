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
import com.biblioteca.dto.ObtenerPublicacionDetallesPorIdDTO;
import com.biblioteca.utilities.PublicationTransactionHelper;
import com.biblioteca.utilities.RelacionPublicacionHelperEnum;

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
     * Ayudante para transacciones de publicaciones
     */
    private final PublicationTransactionHelper transactionHelper;

    /**
     * Método para procesar autores en la edición de libros.
     *
     * @param conexion       conexión a la base de datos
     * @param publicacionDAO DAO para operaciones de publicación
     * @param autorDAO       DAO para operaciones de autores
     * @param autoresCsv     CSV de autores
     * @param idPublicacion  id de la publicación
     * @return true si se procesaron correctamente, false si hay error
     */
    private boolean procesarAutores(final Connection conexion, final PublicacionDAO publicacionDAO,
            final AutorDAO autorDAO, final String autoresCsv, final int idPublicacion) {
        if (autoresCsv == null || autoresCsv.trim().isEmpty()) {
            return true;
        }

        String[] autores = autoresCsv.split(",");
        for (String a : autores) {
            String nombre = a.trim();
            if (nombre.isEmpty()) {
                continue;
            }
            int idAutor = autorDAO.obtenerOCrearPorNombre(nombre);
            if (idAutor == -1) {
                return false;
            }
            if (!publicacionDAO.insertarLibroAutor(idPublicacion, idAutor)) {
                return false;
            }
        }
        return true;
    }

    /**
     * Método para procesar modulos en la edición de libros.
     *
     * @param conexion       conexión a la base de datos
     * @param publicacionDAO DAO para operaciones de publicación
     * @param moduloDAO      DAO para operaciones de modulos
     * @param modulosCsv     CSV de modulos
     * @param idPublicacion  id de la publicación
     * @return true si se procesaron correctamente, false si hay error
     */
    private boolean procesarModulos(final Connection conexion, final PublicacionDAO publicacionDAO,
            final ModuloDAO moduloDAO, final String modulosCsv, final int idPublicacion) {
        try {
            return transactionHelper.procesarRelaciones(conexion, publicacionDAO, moduloDAO, modulosCsv,
                    idPublicacion, RelacionPublicacionHelperEnum.MODULO);
        } catch (SQLException e) {
            System.out.println("Error al procesar modulos: " + e.getMessage());
            return false;
        }
    }

    /**
     * Método para procesar ciclos en la edición de libros.
     *
     * @param conexion       conexión a la base de datos
     * @param publicacionDAO DAO para operaciones de publicación
     * @param cicloDAO       DAO para operaciones de ciclos
     * @param ciclosCsv      CSV de ciclos
     * @param idPublicacion  id de la publicación
     * @return true si se procesaron correctamente, false si hay error
     */
    private boolean procesarCiclos(final Connection conexion, final PublicacionDAO publicacionDAO,
            final CicloDAO cicloDAO, final String ciclosCsv, final int idPublicacion) {
        try {
            return transactionHelper.procesarRelaciones(conexion, publicacionDAO, cicloDAO, ciclosCsv,
                    idPublicacion, RelacionPublicacionHelperEnum.CICLO);
        } catch (SQLException e) {
            System.out.println("Error al procesar ciclos: " + e.getMessage());
            return false;
        }
    }

    /**
     * Método para procesar temas en la edición de libros.
     *
     * @param conexion       conexión a la base de datos
     * @param publicacionDAO DAO para operaciones de publicación
     * @param temaDAO        DAO para operaciones de temas
     * @param temasCsv       CSV de temas
     * @param idPublicacion  id de la publicación
     * @return true si se procesaron correctamente, false si hay error
     */
    private boolean procesarTemas(final Connection conexion, final PublicacionDAO publicacionDAO,
            final TemaDAO temaDAO, final String temasCsv, final int idPublicacion) {
        try {
            return transactionHelper.procesarRelaciones(conexion, publicacionDAO, temaDAO, temasCsv,
                    idPublicacion, RelacionPublicacionHelperEnum.TEMA);
        } catch (SQLException e) {
            System.out.println("Error al procesar temas: " + e.getMessage());
            return false;
        }
    }

    /**
     * Constructor que permite inyectar una `DBConnection` (recomendado para tests
     * y para la nueva arquitectura).
     * 
     * @param dbConnection conexión a la base de datos
     */
    public ControladorEditarPublicacionDialog(final DBConnection dbConnection) {
        if (dbConnection == null) {
            throw new IllegalArgumentException("DBConnection cannot be null");
        }
        this.dbConnection = dbConnection;
        this.transactionHelper = new PublicationTransactionHelper();
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
    public ObtenerPublicacionDetallesPorIdDTO obtenerDetallesPublicacion(final int id) {
        try (Connection conexion = this.dbConnection.getConnection()) {
            if (conexion == null) {
                System.out.println("No se puede obtener conexión a BD");
                return null;
            }
            PublicacionDAO dao = new PublicacionDAO(conexion);
            return dao.obtenerPublicacionDetallesPorId(id);
        } catch (SQLException e) {
            System.out.println("Error al obtener conexión: " + e.getMessage());
            return null;
        }
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
    public boolean editarPublicacionLibro(final int idPublicacion, final String isbn, final String titulo,
            final String idioma, final String temasCsv, final String modulosCsv, final String ciclosCsv,
            final String editorial, final int numEdicion, final LocalDate fechaPublic, final String autoresCsv) {
        try (Connection conexion = this.dbConnection.getConnection()) {
            if (conexion == null) {
                return false;
            }
            PublicacionDAO publicacionDAO = new PublicacionDAO(conexion);
            AutorDAO autorDAO = new AutorDAO(conexion);
            ModuloDAO moduloDAO = new ModuloDAO(conexion);
            TemaDAO temaDAO = new TemaDAO(conexion);
            CicloDAO cicloDAO = new CicloDAO(conexion);

            try {
                conexion.setAutoCommit(false);

                if (!publicacionDAO.actualizarPublicacion(idPublicacion, titulo, editorial, isbn, idioma, 'L')) {
                    return false;
                }
                if (!publicacionDAO.actualizarLibro(idPublicacion, numEdicion, Date.valueOf(fechaPublic))) {
                    return false;
                }

                try (PreparedStatement delRev = conexion
                        .prepareStatement("DELETE FROM revistas WHERE id_publicacion = ?")) {
                    delRev.setInt(1, idPublicacion);
                    delRev.executeUpdate();
                }

                if (!publicacionDAO.eliminarRelacionesPublicacion(idPublicacion)) {
                    return false;
                }

                if (!procesarAutores(conexion, publicacionDAO, autorDAO, autoresCsv, idPublicacion)) {
                    return false;
                }

                if (!procesarModulos(conexion, publicacionDAO, moduloDAO, modulosCsv, idPublicacion)) {
                    return false;
                }

                if (!procesarCiclos(conexion, publicacionDAO, cicloDAO, ciclosCsv, idPublicacion)) {
                    return false;
                }

                if (!procesarTemas(conexion, publicacionDAO, temaDAO, temasCsv, idPublicacion)) {
                    return false;
                }

                conexion.commit();
                return true;
            } catch (SQLException e) {
                try {
                    conexion.rollback();
                } catch (SQLException ex) {
                    System.out.println("Error al hacer rollback: " + ex.getMessage());
                }
                System.out.println(e.getMessage());
                System.out.println(e.getCause());
                return false;
            } finally {
                try {
                    conexion.setAutoCommit(true);
                } catch (SQLException ex) {
                    System.out.println("Error al restaurar auto-commit: " + ex.getMessage());
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
    public boolean editarPublicacionRevista(final int idPublicacion, final String isbn, final String titulo,
            final String idioma, final String temasCsv, final String modulosCsv, final String ciclosCsv,
            final String editorial, final String periodicidad) {

        try (Connection conexion = this.dbConnection.getConnection()) {
            if (conexion == null) {
                System.out.println("No se puede obtener conexión a BD");
                return false;
            }

            // Creación de DAOs para gestionar una transacción completa
            PublicacionDAO publicacionDAO = new PublicacionDAO(conexion);
            ModuloDAO moduloDAO = new ModuloDAO(conexion);
            TemaDAO temaDAO = new TemaDAO(conexion);
            CicloDAO cicloDAO = new CicloDAO(conexion);

            try {
                // Begin transaction
                conexion.setAutoCommit(false);

                // Actualizar tabla publicaciones
                if (!publicacionDAO.actualizarPublicacion(idPublicacion, titulo, editorial, isbn, idioma,
                        'R')) {
                    conexion.rollback();
                    return false;
                }

                // Actualizar o insertar fila revistas
                if (!publicacionDAO.actualizarRevista(idPublicacion, periodicidad)) {
                    conexion.rollback();
                    return false;
                }

                // Si estamos convirtiendo a Revista, eliminar la fila en libros si existiera
                try (PreparedStatement delLib = conexion
                        .prepareStatement("DELETE FROM libros WHERE id_publicacion = ?")) {
                    delLib.setInt(1, idPublicacion);
                    delLib.executeUpdate();
                } catch (SQLException ex) {
                    System.out.println("Error eliminando fila libro anterior: " + ex.getMessage());
                }

                // Eliminar relaciones previas
                if (!publicacionDAO.eliminarRelacionesPublicacion(idPublicacion)) {
                    conexion.rollback();
                    return false;
                }

                // Procesar modulos
                if (modulosCsv != null && !modulosCsv.trim().isEmpty()) {
                    String[] modulos = modulosCsv.split(",");
                    for (String m : modulos) {
                        String nombre = m.trim();
                        if (nombre.isEmpty()) {
                            continue;
                        }
                        int idModulo = moduloDAO.obtenerOCrear(nombre);
                        if (idModulo == -1) {
                            conexion.rollback();
                            return false;
                        }
                        if (!publicacionDAO.insertarPublicacionModulo(idPublicacion, idModulo)) {
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
                        if (nombre.isEmpty()) {
                            continue;
                        }
                        int idCiclo = cicloDAO.obtenerOCrear(nombre);
                        if (idCiclo == -1) {
                            conexion.rollback();
                            return false;
                        }
                        if (!publicacionDAO.insertarPublicacionCiclo(idPublicacion, idCiclo)) {
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
                        if (nombre.isEmpty()) {
                            continue;
                        }
                        int idTema = temaDAO.obtenerOCrear(conexion, nombre);
                        if (idTema == -1) {
                            conexion.rollback();
                            return false;
                        }
                        if (!publicacionDAO.insertarPublicacionTema(idPublicacion, idTema)) {
                            conexion.rollback();
                            return false;
                        }
                    }
                }

                conexion.commit();
                return true;
            } catch (SQLException e) {
                try {
                    // Rollback en caso de error
                    conexion.rollback();
                } catch (SQLException ex) {
                    System.out.println("Error al hacer rollback: " + ex.getMessage());
                }
                System.out.println(e.getMessage());
                System.out.println(e.getCause());
                return false;
            } finally {
                try {
                    conexion.setAutoCommit(true);
                } catch (SQLException ex) {
                    System.out.println("Error al restaurar auto-commit: " + ex.getMessage());
                }
            }

        } catch (SQLException e1) {
            System.out.println("Error al obtener conexión: " + e1.getMessage());
            return false;
        }

    }
}
