package com.example.controlador;

import java.sql.Connection;
import java.sql.Date;
import java.time.LocalDate;

import com.example.dao.AutorDAO;
import com.example.dao.CicloDAO;
import com.example.dao.ModuloDAO;
import com.example.dao.PublicacionDAO;
import com.example.dao.TemaDAO;

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
    private final com.example.conexiones.DBConnection dbConnection;



    /**
     * Constructor que permite inyectar una `DBConnection` (recomendado para tests
     * y para la nueva arquitectura).
     * 
     * @param dbConnection
     */
    public ControladorNuevaPublicacionDialog(com.example.conexiones.DBConnection dbConnection) {
        if (dbConnection == null) {
            throw new IllegalArgumentException("DBConnection cannot be null");
        }
        this.dbConnection = dbConnection;
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
    public boolean crearPublicacionLibro(String isbn, String titulo, String idioma, String temasCsv, String modulosCsv,
            String ciclosCsv, String editorial, int numEdicion, LocalDate fechaPublic, String autoresCsv) {
        PublicacionDAO publicacionDAO = new PublicacionDAO(this.dbConnection);
        AutorDAO autorDAO = new AutorDAO(this.dbConnection);
        ModuloDAO moduloDAO = new ModuloDAO(this.dbConnection);
        TemaDAO temaDAO = new TemaDAO(this.dbConnection);
        CicloDAO cicloDAO = new CicloDAO(this.dbConnection);

        Connection conexion = this.dbConnection.getConnection();
        if (conexion == null) {
            System.out.println("No se puede obtener conexión a BD");
            return false;
        }
        try {
            conexion.setAutoCommit(false);

            int idPub = publicacionDAO.insertarPublicacion(conexion, titulo, editorial, isbn, idioma, 'L');
            if (idPub == -1) {
                conexion.rollback();
                return false;
            }

            // Insertar libro
            boolean okLib = publicacionDAO.insertarLibro(conexion, idPub, numEdicion, Date.valueOf(fechaPublic));
            if (!okLib) {
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
                    int idAutor = autorDAO.obtenerOCrearPorNombre(conexion, nombre);
                    if (idAutor == -1) {
                        conexion.rollback();
                        return false;
                    }
                    if (!publicacionDAO.insertarLibroAutor(conexion, idPub, idAutor)) {
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
                    if (!publicacionDAO.insertarPublicacionModulo(conexion, idPub, idModulo)) {
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
                    int idCiclo = cicloDAO.obtenerOCrear(conexion, nombre);
                    if (idCiclo == -1) {
                        conexion.rollback();
                        return false;
                    }
                    if (!publicacionDAO.insertarPublicacionCiclo(conexion, idPub, idCiclo)) {
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
                    if (!publicacionDAO.insertarPublicacionTema(conexion, idPub, idTema)) {
                        conexion.rollback();
                        return false;
                    }
                }
            }

            // Si hemos llegado hasta aquí, confirmar
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
    public boolean crearPublicacionRevista(String isbn, String titulo, String idioma, String temasCsv,
            String modulosCsv, String ciclosCsv, String editorial, String periodicidad) {
        PublicacionDAO publicacionDAO = new PublicacionDAO(this.dbConnection);
        ModuloDAO moduloDAO = new ModuloDAO(this.dbConnection);
        TemaDAO temaDAO = new TemaDAO(this.dbConnection);
        CicloDAO cicloDAO = new CicloDAO(this.dbConnection);

        Connection conexion = this.dbConnection.getConnection();
        if (conexion == null) {
            System.out.println("No se puede obtener conexión a BD");
            return false;
        }
        try {
            conexion.setAutoCommit(false);

            int idPub = publicacionDAO.insertarPublicacion(conexion, titulo, editorial, isbn, idioma, 'R');
            if (idPub == -1) {
                conexion.rollback();
                return false;
            }

            int numRev = publicacionDAO.siguienteNumRevista(conexion);
            if (!publicacionDAO.insertarRevista(conexion, idPub, periodicidad, numRev)) {
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
                    if (!publicacionDAO.insertarPublicacionModulo(conexion, idPub, idModulo)) {
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
                    int idCiclo = cicloDAO.obtenerOCrear(conexion, nombre);
                    if (idCiclo == -1) {
                        conexion.rollback();
                        return false;
                    }
                    if (!publicacionDAO.insertarPublicacionCiclo(conexion, idPub, idCiclo)) {
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
                    if (!publicacionDAO.insertarPublicacionTema(conexion, idPub, idTema)) {
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
    }

}