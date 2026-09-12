package com.biblioteca.controlador;

import java.sql.Connection;
import java.sql.Date;
import java.sql.SQLException;
import java.time.LocalDate;

import com.biblioteca.conexiones.DBConnection;
import com.biblioteca.dao.AutorDAO;
import com.biblioteca.dao.CicloDAO;
import com.biblioteca.dao.ModuloDAO;
import com.biblioteca.dao.PublicacionDAO;
import com.biblioteca.dao.TemaDAO;
import com.biblioteca.utilities.PublicationTransactionHelper;
import com.biblioteca.utilities.RelacionPublicacionHelperEnum;

/**
 * Controlador responsable de la creación y persistencia de nuevas
 * publicaciones.
 *
 * Contiene métodos que encapsulan la lógica de inserción en varias tablas
 * relacionadas (`publicaciones`, `libros`/`revistas`, relaciones con módulos,
 * ciclos, temas y autores).
 */
public class ControladorNuevaPublicacionDialog {
    /**
     * DBConnection para conexiones a la base de datos
     */
    private final DBConnection dbConnection;

    /**
     * Ayudante para transacciones de publicaciones
     */
    private final PublicationTransactionHelper transactionHelper;

    /**
     * Método para procesar autores en la creación de publicaciones.
     *
     * @param conexion       conexión a la base de datos
     * @param publicacionDAO DAO para operaciones de publicación
     * @param autorDAO       DAO para operaciones de autores
     * @param autoresCsv     CSV de autores
     * @param idPub          id de la publicación
     * @return true si se procesaron correctamente, false si hay error
     */
    private boolean procesarAutores(final Connection conexion, final PublicacionDAO publicacionDAO,
            final AutorDAO autorDAO, final String autoresCsv, final int idPub) {
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
            if (!publicacionDAO.insertarLibroAutor(idPub, idAutor)) {
                return false;
            }
        }
        return true;
    }

    /**
     * Constructor que permite inyectar una `DBConnection` (recomendado para tests
     * y para la nueva arquitectura).
     * 
     * @param dbConnection DBConnection para conexiones a la base de datos
     */
    public ControladorNuevaPublicacionDialog(final DBConnection dbConnection) {
        if (dbConnection == null) {
            throw new IllegalArgumentException("DBConnection cannot be null");
        }
        this.dbConnection = dbConnection;
        this.transactionHelper = new PublicationTransactionHelper();
    }

    /**
     * Versión transaccional: crea una publicación de tipo Libro usando una única
     * Connection y commit/rollback. Esto asegura consistencia si alguna inserción
     * falla.
     *
     * @param isbn        código ISBN o identificador
     * @param titulo      título de la publicación
     * @param idioma      idioma
     * @param temasCsv    lista de temas separados por comas
     * @param modulosCsv  lista de módulos separados por comas
     * @param ciclosCsv   lista de ciclos separados por comas
     * @param editorial   editorial
     * @param numEdicion  número de edición (>0)
     * @param fechaPublic fecha publicación (java.time.LocalDate)
     * @param autoresCsv  lista de autores separados por comas
     * @return true si la creación fue satisfactoria y commit realizado
     */
    public boolean crearPublicacionLibro(final String isbn, final String titulo, final String idioma,
            final String temasCsv, final String modulosCsv,
            final String ciclosCsv, final String editorial, final int numEdicion, final LocalDate fechaPublic,
            final String autoresCsv) {

        // Generar conexión a la base de datos
        try (Connection conexion = this.dbConnection.getConnection();) {
            // Comprueba si la conexión es nula antes de continuar.
            if (conexion == null) {
                System.out.println("No se puede obtener conexión a BD");
                return false;
            }

            // Creación de DAOs para gestionar una transacción completa
            PublicacionDAO publicacionDAO = new PublicacionDAO(conexion);
            AutorDAO autorDAO = new AutorDAO(conexion);
            ModuloDAO moduloDAO = new ModuloDAO(conexion);
            TemaDAO temaDAO = new TemaDAO(conexion);
            CicloDAO cicloDAO = new CicloDAO(conexion);

            try {
                conexion.setAutoCommit(false);

                int idPub = publicacionDAO.insertarPublicacion(titulo, editorial, isbn, idioma, 'L');
                if (idPub == -1) {
                    conexion.rollback();
                    return false;
                }

                // Insertar libro
                boolean okLib = publicacionDAO.insertarLibro(idPub, numEdicion, Date.valueOf(fechaPublic));
                if (!okLib) {
                    conexion.rollback();
                    return false;
                }

                // Procesar autores
                if (!procesarAutores(conexion, publicacionDAO, autorDAO, autoresCsv, idPub)) {
                    return false;
                }

                // Procesar modulos, ciclos y temas usando helper
                if (!transactionHelper.procesarRelaciones(conexion, publicacionDAO, moduloDAO, modulosCsv, idPub, RelacionPublicacionHelperEnum.MODULO)) {
                    return false;
                }
                if (!transactionHelper.procesarRelaciones(conexion, publicacionDAO, cicloDAO, ciclosCsv, idPub, RelacionPublicacionHelperEnum.CICLO)) {
                    return false;
                }
                if (!transactionHelper.procesarRelaciones(conexion, publicacionDAO, temaDAO, temasCsv, idPub, RelacionPublicacionHelperEnum.TEMA)) {
                    return false;
                }

                // Si hemos llegado hasta aquí, confirmar
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
                    System.out.println("Error habilitando el modo autocommit de la base de datos: " + ex.getMessage());
                }
            }
        } catch (SQLException e) {
            System.out.println("Error al obtener conexión: " + e.getMessage());
            return false;
        }

    }

    /**
     * Versión transaccional para crear una revista. Usa una única Connection y
     * commit/rollback para asegurar consistencia.
     *
     * @param isbn         código o identificador
     * @param titulo       título
     * @param idioma       idioma
     * @param temasCsv     temas separados por comas
     * @param modulosCsv   módulos separados por comas
     * @param ciclosCsv    ciclos separados por comas
     * @param editorial    editorial
     * @param periodicidad texto de periodicidad
     * @return true si la creación fue satisfactoria y commit realizado
     */
    public boolean crearPublicacionRevista(final String isbn, final String titulo, final String idioma,
            final String temasCsv,
            final String modulosCsv, final String ciclosCsv, final String editorial, final String periodicidad) {
        try (Connection conexion = this.dbConnection.getConnection();) {
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
                conexion.setAutoCommit(false);

                int idPub = publicacionDAO.insertarPublicacion(titulo, editorial, isbn, idioma, 'R');
                if (idPub == -1) {
                    conexion.rollback();
                    return false;
                }

                int numRev = publicacionDAO.siguienteNumRevista();
                if (!publicacionDAO.insertarRevista(idPub, periodicidad, numRev)) {
                    conexion.rollback();
                    return false;
                }

                // Procesar modulos, ciclos y temas usando helper
                if (!transactionHelper.procesarRelaciones(conexion, publicacionDAO, moduloDAO, modulosCsv, idPub, RelacionPublicacionHelperEnum.MODULO)) {
                    return false;
                }
                if (!transactionHelper.procesarRelaciones(conexion, publicacionDAO, cicloDAO, ciclosCsv, idPub, RelacionPublicacionHelperEnum.CICLO)) {
                    return false;
                }
                if (!transactionHelper.procesarRelaciones(conexion, publicacionDAO, temaDAO, temasCsv, idPub, RelacionPublicacionHelperEnum.TEMA)) {
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
                    System.out.println("Error habilitando el modo autocommit de la base de datos: " + ex.getMessage());
                }
            }
        } catch (SQLException e1) {
            System.out.println("Error al obtener conexión: " + e1.getMessage());
            return false;
        }
    }

}
