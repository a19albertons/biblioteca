package com.example.dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.example.conexiones.DBConnection;

/**
 * DAO para Publicacion
 */
public class PublicacionDAO {
    /**
     * DBConnection para conexiones a la base de datos
     */
    private final DBConnection dbConnection;

    // SQL constants 🔧
    private static final String SQL_LISTA_EDITORIALES = "SELECT editorial FROM publicaciones GROUP BY editorial ORDER BY editorial ASC";
    private static final String SQL_INSERT_PUBLICACION = "INSERT INTO publicaciones (titulo, editorial, codigo_isbn, idioma, tipo) VALUES (?, ?, ?, ?, ?)";
    private static final String SQL_INSERT_LIBRO = "INSERT INTO libros (id_publicacion, num_edicion, fecha_publicacion) VALUES (?, ?, ?)";
    private static final String SQL_INSERT_REVISTA = "INSERT INTO revistas (id_publicacion, periodicidad, num_revista) VALUES (?, ?, ?)";
    private static final String SQL_INSERT_PUBLICACION_MODULO = "INSERT INTO publicacion_modulo (id_publicacion, id_modulo) VALUES (?, ?)";
    private static final String SQL_INSERT_PUBLICACION_CICLO = "INSERT INTO publicacion_ciclo (id_publicacion, id_ciclo) VALUES (?, ?)";
    private static final String SQL_INSERT_PUBLICACION_TEMA = "INSERT INTO publicacion_tema (id_publicacion, id_tema) VALUES (?, ?)";
    private static final String SQL_INSERT_LIBRO_AUTOR = "INSERT INTO libros_autores (id_libro, id_autor) VALUES (?, ?)";
    private static final String SQL_SELECT_MAX_NUM_REVISTA = "SELECT COALESCE(MAX(num_revista),0) AS m FROM revistas";
    private static final String SQL_LISTA_PUBLICACIONES_RESUMEN = "SELECT p.id, p.titulo, p.codigo_isbn, "
            + "COALESCE(GROUP_CONCAT(DISTINCT a.nombre SEPARATOR ', '),'') AS autores, "
            + "COALESCE(GROUP_CONCAT(DISTINCT ci.nombre SEPARATOR ', '),'') AS ciclos, "
            + "p.editorial, "
            + "(SELECT COUNT(*) FROM ejemplares e WHERE e.id_publicacion = p.id AND e.estado = TRUE AND e.id NOT IN (SELECT id_ejemplar FROM prestamos WHERE estado = TRUE)) AS disponibles "
            + "FROM publicaciones p "
            + "LEFT JOIN libros_autores la ON p.id = la.id_libro "
            + "LEFT JOIN autores a ON la.id_autor = a.id "
            + "LEFT JOIN publicacion_ciclo pc ON p.id = pc.id_publicacion "
            + "LEFT JOIN ciclos ci ON pc.id_ciclo = ci.id "
            + "WHERE p.estado = TRUE "
            + "GROUP BY p.id "
            + "ORDER BY p.titulo ASC";
    private static final String SQL_OBTENER_PUBLICACION_RESUMEN_POR_ID = "SELECT p.id, p.titulo, p.codigo_isbn, "
            + "COALESCE(GROUP_CONCAT(DISTINCT a.nombre SEPARATOR ', '),'') AS autores, "
            + "COALESCE(GROUP_CONCAT(DISTINCT ci.nombre SEPARATOR ', '),'') AS ciclos, "
            + "p.editorial, "
            + "(SELECT COUNT(*) FROM ejemplares e WHERE e.id_publicacion = p.id AND e.estado = TRUE AND e.id NOT IN (SELECT id_ejemplar FROM prestamos WHERE estado = TRUE)) AS disponibles "
            + "FROM publicaciones p "
            + "LEFT JOIN libros_autores la ON p.id = la.id_libro "
            + "LEFT JOIN autores a ON la.id_autor = a.id "
            + "LEFT JOIN publicacion_ciclo pc ON p.id = pc.id_publicacion "
            + "LEFT JOIN ciclos ci ON pc.id_ciclo = ci.id "
            + "WHERE p.estado = TRUE AND p.id = ? "
            + "GROUP BY p.id "
            + "ORDER BY p.titulo ASC";
    private static final String SQL_PUBLICACION_DETALLES_POR_ID = "SELECT p.id, p.titulo, p.codigo_isbn, p.idioma, p.tipo, "
            + "COALESCE(GROUP_CONCAT(DISTINCT t.nombre SEPARATOR ', '),'') AS temas, "
            + "COALESCE(GROUP_CONCAT(DISTINCT m.nombre SEPARATOR ', '),'') AS modulos, "
            + "COALESCE(GROUP_CONCAT(DISTINCT c.nombre SEPARATOR ', '),'') AS ciclos, "
            + "p.editorial, l.num_edicion, l.fecha_publicacion, "
            + "COALESCE(GROUP_CONCAT(DISTINCT a.nombre SEPARATOR ', '),'') AS autores, "
            + "r.periodicidad "
            + "FROM publicaciones p "
            + "LEFT JOIN libros l ON p.id = l.id_publicacion "
            + "LEFT JOIN revistas r ON p.id = r.id_publicacion "
            + "LEFT JOIN libros_autores la ON p.id = la.id_libro "
            + "LEFT JOIN autores a ON la.id_autor = a.id "
            + "LEFT JOIN publicacion_tema pt ON p.id = pt.id_publicacion "
            + "LEFT JOIN temas t ON pt.id_tema = t.id "
            + "LEFT JOIN publicacion_modulo pm ON p.id = pm.id_publicacion "
            + "LEFT JOIN modulo m ON pm.id_modulo = m.id "
            + "LEFT JOIN publicacion_ciclo pc ON p.id = pc.id_publicacion "
            + "LEFT JOIN ciclos c ON pc.id_ciclo = c.id "
            + "WHERE p.id = ? "
            + "GROUP BY p.id";
    private static final String SQL_UPDATE_PUBLICACION = "UPDATE publicaciones SET titulo = ?, editorial = ?, codigo_isbn = ?, idioma = ?, tipo = ? WHERE id = ?";
    private static final String SQL_UPDATE_LIBROS = "UPDATE libros SET num_edicion = ?, fecha_publicacion = ? WHERE id_publicacion = ?";
    private static final String SQL_UPDATE_REVISTAS = "UPDATE revistas SET periodicidad = ? WHERE id_publicacion = ?";
    private static final String SQL_DELETE_PUBLICACION_MODULO = "DELETE FROM publicacion_modulo WHERE id_publicacion = ?";
    private static final String SQL_DELETE_PUBLICACION_CICLO = "DELETE FROM publicacion_ciclo WHERE id_publicacion = ?";
    private static final String SQL_DELETE_PUBLICACION_TEMA = "DELETE FROM publicacion_tema WHERE id_publicacion = ?";
    private static final String SQL_DELETE_LIBROS_AUTORES = "DELETE FROM libros_autores WHERE id_libro = ?";
    private static final String SQL_TIENEPRESTAMOS_ACTIVOS = "SELECT COUNT(*) AS cnt FROM prestamos p JOIN ejemplares e ON p.id_ejemplar = e.id WHERE e.id_publicacion = ? AND p.estado = TRUE";
    private static final String SQL_BAJA_EJEMPLARES = "UPDATE ejemplares SET estado = FALSE WHERE id_publicacion = ?";
    private static final String SQL_BAJA_PUBLICACION = "UPDATE publicaciones SET estado = FALSE WHERE id = ?";

    /**
     * Constructor del DAO
     * 
     * @param dbConnection
     */
    public PublicacionDAO(DBConnection dbConnection) {

        if (dbConnection == null) {
            throw new IllegalArgumentException("DBConnection cannot be null");
        }
        this.dbConnection = dbConnection;
    }

    /**
     * Obtiene las editoriales de las publicaciones
     * 
     * @return
     */
    public String[] listaEditoriales() {
        // Listado de editoriales
        ArrayList<String> devolver = new ArrayList<>();
        try (Connection conexion = dbConnection.getConnection();
                // Consulta SQL
                PreparedStatement ps = conexion.prepareStatement(SQL_LISTA_EDITORIALES);) {
            // Ejecutar consulta
            try (ResultSet rs = ps.executeQuery();) {
                while (rs.next()) {
                    devolver.add(rs.getString("editorial"));
                }
            }
        } catch (Exception e) {
            devolver = new ArrayList<>();
            System.out.println(e.getMessage());
            System.out.println(e.getCause());

        }
        return devolver.toArray(new String[0]);
    }

    /**
     * Inserta una nueva publicación y devuelve su id generado.
     *
     * @param titulo
     * @param editorial
     * @param codigoIsbn
     * @param idioma
     * @param tipo       'L' o 'R'
     * @return id generado o -1 en caso de error
     */
    public int insertarPublicacion(String titulo, String editorial, String codigoIsbn, String idioma, char tipo) {
        try (Connection conexion = dbConnection.getConnection();
                PreparedStatement ps = conexion.prepareStatement(SQL_INSERT_PUBLICACION,
                        Statement.RETURN_GENERATED_KEYS)) {
            // establecer parámetros
            ps.setString(1, titulo);
            ps.setString(2, editorial);
            ps.setString(3, codigoIsbn);
            ps.setString(4, idioma);
            ps.setString(5, String.valueOf(Character.toUpperCase(tipo)));
            // ejecutar
            ps.executeUpdate();
            // obtener id generado
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        } catch (Exception e) {
            // debug
            System.out.println(e.getMessage());
            System.out.println(e.getCause());
        }
        return -1;
    }

    /**
     * Variante que utiliza una Connection existente para que pueda formar parte de
     * una transacción
     */
    public int insertarPublicacion(Connection conexion, String titulo, String editorial, String codigoIsbn,
            String idioma, char tipo) {
        try (PreparedStatement ps = conexion.prepareStatement(SQL_INSERT_PUBLICACION,
                Statement.RETURN_GENERATED_KEYS)) {
            // establecer parámetros
            ps.setString(1, titulo);
            ps.setString(2, editorial);
            ps.setString(3, codigoIsbn);
            ps.setString(4, idioma);
            ps.setString(5, String.valueOf(Character.toUpperCase(tipo)));
            // ejecutar
            ps.executeUpdate();
            // obtener id generado
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
            System.out.println(e.getCause());
        }
        return -1;
    }

    /**
     * Inserta los datos de libros (tabla libros)
     *
     * @param idPublicacion
     * @param numEdicion
     * @param fechaPublicacion fecha SQL (java.sql.Date)
     * @return true si ok
     */
    public boolean insertarLibro(int idPublicacion, int numEdicion, Date fechaPublicacion) {
        try (Connection conexion = dbConnection.getConnection();
                PreparedStatement ps = conexion.prepareStatement(SQL_INSERT_LIBRO)) {
            // establecer parámetros
            ps.setInt(1, idPublicacion);
            ps.setInt(2, numEdicion);
            ps.setDate(3, fechaPublicacion);
            // ejecutar
            ps.executeUpdate();
            return true;
        } catch (Exception e) {
            // debug
            System.out.println(e.getMessage());
            System.out.println(e.getCause());
            return false;
        }
    }

    /**
     * Variante que utiliza una Connection existente (transacción)
     */
    public boolean insertarLibro(Connection conexion, int idPublicacion, int numEdicion, Date fechaPublicacion) {
        try (PreparedStatement ps = conexion.prepareStatement(SQL_INSERT_LIBRO)) {
            // establecer parámetros
            ps.setInt(1, idPublicacion);
            ps.setInt(2, numEdicion);
            ps.setDate(3, fechaPublicacion);
            // ejecutar
            ps.executeUpdate();
            return true;
        } catch (Exception e) {
            // debug
            System.out.println(e.getMessage());
            System.out.println(e.getCause());
            return false;
        }
    }

    /**
     * Inserta los datos de revistas (tabla revistas)
     *
     * @param idPublicacion
     * @param periodicidad
     * @param numRevista
     * @return true si ok
     */
    public boolean insertarRevista(int idPublicacion, String periodicidad, int numRevista) {
        try (Connection conexion = dbConnection.getConnection();
                PreparedStatement ps = conexion.prepareStatement(SQL_INSERT_REVISTA)) {
            // establecer parámetros
            ps.setInt(1, idPublicacion);
            ps.setString(2, periodicidad);
            ps.setInt(3, numRevista);
            // ejecutar
            ps.executeUpdate();
            return true;
        } catch (Exception e) {
            // debug
            System.out.println(e.getMessage());
            System.out.println(e.getCause());
            return false;
        }
    }

    /**
     * Variante que utiliza una Connection existente (transaccional)
     */
    public boolean insertarRevista(Connection conexion, int idPublicacion, String periodicidad, int numRevista) {
        try (PreparedStatement ps = conexion.prepareStatement(SQL_INSERT_REVISTA)) {
            // establecer parámetros
            ps.setInt(1, idPublicacion);
            ps.setString(2, periodicidad);
            ps.setInt(3, numRevista);
            // ejecutar
            ps.executeUpdate();
            return true;
        } catch (Exception e) {
            // debug
            System.out.println(e.getMessage());
            System.out.println(e.getCause());
            return false;
        }
    }

    /**
     * Inserta relación publicacion <-> modulo
     */
    public boolean insertarPublicacionModulo(int idPublicacion, int idModulo) {
        try (Connection conexion = dbConnection.getConnection();
                PreparedStatement ps = conexion.prepareStatement(SQL_INSERT_PUBLICACION_MODULO)) {
            // establecer parámetros
            ps.setInt(1, idPublicacion);
            ps.setInt(2, idModulo);
            // ejecutar
            ps.executeUpdate();
            return true;
        } catch (Exception e) {
            // debug
            System.out.println(e.getMessage());
            System.out.println(e.getCause());
            return false;
        }
    }

    /**
     * Variante transaccional que usa una Connection existente
     */
    public boolean insertarPublicacionModulo(Connection conexion, int idPublicacion, int idModulo) {
        try (PreparedStatement ps = conexion.prepareStatement(SQL_INSERT_PUBLICACION_MODULO)) {
            // establecer parámetros
            ps.setInt(1, idPublicacion);
            ps.setInt(2, idModulo);
            // ejecutar
            ps.executeUpdate();
            return true;
        } catch (Exception e) {
            // debug
            System.out.println(e.getMessage());
            System.out.println(e.getCause());
            return false;
        }
    }

    /**
     * Inserta relación publicacion <-> ciclo
     */
    public boolean insertarPublicacionCiclo(int idPublicacion, int idCiclo) {
        try (Connection conexion = dbConnection.getConnection();
                PreparedStatement ps = conexion.prepareStatement(SQL_INSERT_PUBLICACION_CICLO)) {
            // establecer parámetros
            ps.setInt(1, idPublicacion);
            ps.setInt(2, idCiclo);
            // ejecutar
            ps.executeUpdate();
            return true;
        } catch (Exception e) {
            // debug
            System.out.println(e.getMessage());
            System.out.println(e.getCause());
            return false;
        }
    }

    /**
     * Variante transaccional que usa una Connection existente
     */
    public boolean insertarPublicacionCiclo(Connection conexion, int idPublicacion, int idCiclo) {
        try (PreparedStatement ps = conexion.prepareStatement(SQL_INSERT_PUBLICACION_CICLO)) {
            // establecer parámetros
            ps.setInt(1, idPublicacion);
            ps.setInt(2, idCiclo);
            // ejecutar
            ps.executeUpdate();
            return true;
        } catch (Exception e) {
            // debug
            System.out.println(e.getMessage());
            System.out.println(e.getCause());
            return false;
        }
    }

    /**
     * Inserta relación publicacion <-> tema
     */
    public boolean insertarPublicacionTema(int idPublicacion, int idTema) {
        try (Connection conexion = dbConnection.getConnection();
                PreparedStatement ps = conexion.prepareStatement(SQL_INSERT_PUBLICACION_TEMA)) {
            // establecer parámetros
            ps.setInt(1, idPublicacion);
            ps.setInt(2, idTema);
            // ejecutar
            ps.executeUpdate();
            return true;
        } catch (Exception e) {
            // debug
            System.out.println(e.getMessage());
            System.out.println(e.getCause());
            return false;
        }
    }

    /**
     * Variante transaccional que usa una Connection existente
     */
    public boolean insertarPublicacionTema(Connection conexion, int idPublicacion, int idTema) {
        try (PreparedStatement ps = conexion.prepareStatement(SQL_INSERT_PUBLICACION_TEMA)) {
            // establecer parámetros
            ps.setInt(1, idPublicacion);
            ps.setInt(2, idTema);
            // ejecutar
            ps.executeUpdate();
            return true;
        } catch (Exception e) {
            // debug
            System.out.println(e.getMessage());
            System.out.println(e.getCause());
            return false;
        }
    }

    /**
     * Inserta relación libro <-> autor
     */
    public boolean insertarLibroAutor(int idLibro, int idAutor) {
        try (Connection conexion = dbConnection.getConnection();
                PreparedStatement ps = conexion.prepareStatement(SQL_INSERT_LIBRO_AUTOR)) {
            // establecer parámetros
            ps.setInt(1, idLibro);
            ps.setInt(2, idAutor);
            // ejecutar
            ps.executeUpdate();
            return true;
        } catch (Exception e) {
            // debug
            System.out.println(e.getMessage());
            System.out.println(e.getCause());
            return false;
        }
    }

    /**
     * Variante transaccional que usa una Connection existente
     */
    public boolean insertarLibroAutor(Connection conexion, int idLibro, int idAutor) {
        try (PreparedStatement ps = conexion.prepareStatement(SQL_INSERT_LIBRO_AUTOR)) {
            // establecer parámetros
            ps.setInt(1, idLibro);
            ps.setInt(2, idAutor);
            // ejecutar
            ps.executeUpdate();
            return true;
        } catch (Exception e) {
            // debug
            System.out.println(e.getMessage());
            System.out.println(e.getCause());
            return false;
        }
    }

    /**
     * Devuelve el siguiente número de revista disponible (max(num_revista)+1)
     * 
     * @return siguiente num_revista (>=1) o 1 en caso de error
     */
    public int siguienteNumRevista() {
        try (Connection conexion = dbConnection.getConnection();
                PreparedStatement ps = conexion.prepareStatement(SQL_SELECT_MAX_NUM_REVISTA)) {
            // ejecutar consulta
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("m") + 1;
                }
            }
        } catch (Exception e) {
            // debug
            System.out.println(e.getMessage());
            System.out.println(e.getCause());
        }
        return 1;
    }

    /**
     * Variante transaccional que usa una Connection existente
     */
    public int siguienteNumRevista(Connection conexion) {
        try (PreparedStatement ps = conexion.prepareStatement(SQL_SELECT_MAX_NUM_REVISTA)) {
            // ejecutar consulta
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("m") + 1;
                }
            }
        } catch (Exception e) {
            // debug
            System.out.println(e.getMessage());
            System.out.println(e.getCause());
        }
        return 1;
    }

    /**
     * Devuelve un resumen de las publicaciones activas con los campos solicitados:
     * Título, ISBN, Autor(es), Ciclos, Editorial, Disponibles, id
     *
     * @return Matriz String[][] con columnas en este orden: titulo, isbn, autores,
     *         ciclos, editorial, disponibles, id
     */
    public String[][] listaPublicacionesResumen() {
        List<String[]> lista = new ArrayList<>();
        final String sql = SQL_LISTA_PUBLICACIONES_RESUMEN;

        try (Connection conexion = dbConnection.getConnection();
                PreparedStatement ps = conexion.prepareStatement(sql)) {
            // Ejecutar consulta
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    // obtener datos
                    int id = rs.getInt("id");
                    String titulo = rs.getString("titulo");
                    String isbn = rs.getString("codigo_isbn");
                    String autores = rs.getString("autores");
                    String ciclos = rs.getString("ciclos");
                    String editorial = rs.getString("editorial");
                    String disponibles = String.valueOf(rs.getInt("disponibles"));

                    if (autores == null)
                        autores = "";
                    if (ciclos == null)
                        ciclos = "";
                    if (editorial == null)
                        editorial = "";

                    String[] fila = new String[7];
                    fila[0] = titulo;
                    fila[1] = isbn;
                    fila[2] = autores;
                    fila[3] = ciclos;
                    fila[4] = editorial;
                    fila[5] = disponibles;
                    fila[6] = String.valueOf(id);
                    lista.add(fila);
                }
            }
        } catch (Exception e) {
            // debug
            lista = new ArrayList<>();
            System.out.println(e.getMessage());
            System.out.println(e.getCause());
        }
        return lista.toArray(new String[0][0]);
    }

    /**
     * Obtiene el resumen de una publicacion (mismo formato que
     * listaPublicacionesResumen)
     * por su id
     *
     * @param id
     * @return String[] con columnas:
     *         titulo,isbn,autores,ciclos,editorial,disponibles,id
     */
    public String[] obtenerResumenPublicacionPorId(int id) {
        final String sql = SQL_OBTENER_PUBLICACION_RESUMEN_POR_ID;

        try (Connection conexion = dbConnection.getConnection();
                PreparedStatement ps = conexion.prepareStatement(sql)) {
            // establecer parámetro
            ps.setInt(1, id);
            // Ejecutar consulta
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    // obtener datos
                    String titulo = rs.getString("titulo");
                    String isbn = rs.getString("codigo_isbn");
                    String autores = rs.getString("autores");
                    String ciclos = rs.getString("ciclos");
                    String editorial = rs.getString("editorial");
                    String disponibles = String.valueOf(rs.getInt("disponibles"));

                    if (autores == null)
                        autores = "";
                    if (ciclos == null)
                        ciclos = "";
                    if (editorial == null)
                        editorial = "";

                    return new String[] { titulo, isbn, autores, ciclos, editorial, disponibles, String.valueOf(id) };
                }
            }
        } catch (Exception e) {
            // debug
            System.out.println(e.getMessage());
            System.out.println(e.getCause());
        }
        return null;
    }

    /**
     * Obtiene detalles completos de una publicación (para edición).
     *
     * @param id id de la publicación
     * @return arreglo con campos en el orden: tipo, titulo, codigo_isbn, idioma,
     *         temasCSV, modulosCSV, ciclosCSV, editorial, num_edicion,
     *         fecha_publicacion (YYYY-MM-DD), autoresCSV, periodicidad, id
     *         (o null si no existe la publicación)
     */
    public String[] obtenerPublicacionDetallesPorId(int id) {
        final String sql = SQL_PUBLICACION_DETALLES_POR_ID;

        try (Connection conexion = dbConnection.getConnection();
                PreparedStatement ps = conexion.prepareStatement(sql)) {
            // establecer parámetro
            ps.setInt(1, id);
            // Ejecutar consulta
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    // obtener datos
                    String tipo = rs.getString("tipo");
                    String titulo = rs.getString("titulo");
                    String isbn = rs.getString("codigo_isbn");
                    String idioma = rs.getString("idioma");
                    String temas = rs.getString("temas");
                    String modulos = rs.getString("modulos");
                    String ciclos = rs.getString("ciclos");
                    String editorial = rs.getString("editorial");
                    String numEd = rs.getString("num_edicion");
                    String fechaPub = rs.getString("fecha_publicacion");
                    String autores = rs.getString("autores");
                    String periodicidad = rs.getString("periodicidad");

                    // devolver arreglo con valores (evitar nulls)
                    return new String[] { tipo == null ? "" : tipo, titulo == null ? "" : titulo,
                            isbn == null ? "" : isbn, idioma == null ? "" : idioma,
                            temas == null ? "" : temas, modulos == null ? "" : modulos,
                            ciclos == null ? "" : ciclos, editorial == null ? "" : editorial,
                            numEd == null ? "" : numEd, fechaPub == null ? "" : fechaPub,
                            autores == null ? "" : autores, periodicidad == null ? "" : periodicidad,
                            String.valueOf(id) };
                }
            }
        } catch (Exception e) {
            // debug
            System.out.println(e.getMessage());
            System.out.println(e.getCause());
        }
        return null;
    }

    /**
     * Actualiza los campos base de la tabla `publicaciones`.
     *
     * @param conexion   Connection en contexto transaccional
     * @param id         id de la publicación a actualizar
     * @param titulo     nuevo título
     * @param editorial  nueva editorial
     * @param codigoIsbn nuevo código ISBN
     * @param idioma     nuevo idioma
     * @param tipo       tipo ('L' o 'R')
     * @return true si la actualización tuvo éxito (o no hubo cambios), false en
     *         caso de error
     */
    public boolean actualizarPublicacion(Connection conexion, int id, String titulo, String editorial,
            String codigoIsbn, String idioma, char tipo) {
        try (PreparedStatement ps = conexion.prepareStatement(SQL_UPDATE_PUBLICACION)) {
            // establecer parámetros
            ps.setString(1, titulo);
            ps.setString(2, editorial);
            ps.setString(3, codigoIsbn);
            ps.setString(4, idioma);
            ps.setString(5, String.valueOf(Character.toUpperCase(tipo)));
            ps.setInt(6, id);
            // ejecutar
            int updated = ps.executeUpdate();
            return updated >= 0;
        } catch (Exception e) {
            // debug
            System.out.println(e.getMessage());
            System.out.println(e.getCause());
            return false;
        }
    }

    /**
     * Actualiza o inserta la fila en `libros` para la publicación dada.
     */
    public boolean actualizarLibro(Connection conexion, int idPublicacion, int numEdicion, Date fechaPublicacion) {
        try (PreparedStatement ps = conexion.prepareStatement(SQL_UPDATE_LIBROS)) {
            // establecer parámetros
            ps.setInt(1, numEdicion);
            ps.setDate(2, fechaPublicacion);
            ps.setInt(3, idPublicacion);
            // ejecutar
            int updated = ps.executeUpdate();
            if (updated == 0) {
                // No existe, insertar
                return insertarLibro(conexion, idPublicacion, numEdicion, fechaPublicacion);
            }
            return true;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            System.out.println(e.getCause());
            return false;
        }
    }

    /**
     * Actualiza o inserta la fila en `revistas` para la publicación dada.
     */
    public boolean actualizarRevista(Connection conexion, int idPublicacion, String periodicidad) {
        try (PreparedStatement ps = conexion.prepareStatement(SQL_UPDATE_REVISTAS)) {
            // establecer parámetros
            ps.setString(1, periodicidad);
            ps.setInt(2, idPublicacion);
            // ejecutar
            int updated = ps.executeUpdate();
            if (updated == 0) {
                // necesitar asignar num_revista; usar siguienteNumRevista
                int numRev = siguienteNumRevista(conexion);
                return insertarRevista(conexion, idPublicacion, periodicidad, numRev);
            }
            return true;
        } catch (Exception e) {
            // debug
            System.out.println(e.getMessage());
            System.out.println(e.getCause());
            return false;
        }
    }

    /**
     * Elimina relaciones multivaluadas (módulos, ciclos, temas, autores) para una
     * publicación dada. Usar en contexto transaccional (no hace commit/rollback).
     *
     * @param conexion      Connection en uso
     * @param idPublicacion id de la publicación
     * @return true si las eliminaciones se realizaron correctamente, false en caso
     *         de error
     */
    public boolean eliminarRelacionesPublicacion(Connection conexion, int idPublicacion) {
        try (PreparedStatement ps1 = conexion.prepareStatement(SQL_DELETE_PUBLICACION_MODULO);
                PreparedStatement ps2 = conexion.prepareStatement(SQL_DELETE_PUBLICACION_CICLO);
                PreparedStatement ps3 = conexion.prepareStatement(SQL_DELETE_PUBLICACION_TEMA);
                PreparedStatement ps4 = conexion.prepareStatement(SQL_DELETE_LIBROS_AUTORES)) {
            // establecer parámetros y ejecutar
            ps1.setInt(1, idPublicacion);
            ps1.executeUpdate();
            ps2.setInt(1, idPublicacion);
            ps2.executeUpdate();
            ps3.setInt(1, idPublicacion);
            ps3.executeUpdate();
            ps4.setInt(1, idPublicacion);
            ps4.executeUpdate();
            return true;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            System.out.println(e.getCause());
            return false;
        }
    }

    /**
     * Comprueba si existen préstamos activos para alguna copia de la publicación
     * indicada.
     *
     * @param idPublicacion id de la publicación
     * @return true si existe al menos un préstamo activo, false en caso
     *         contrario
     */
    public boolean tienePrestamosActivos(int idPublicacion) {
        final String sql = SQL_TIENEPRESTAMOS_ACTIVOS;
        try (Connection conexion = dbConnection.getConnection();
                PreparedStatement ps = conexion.prepareStatement(sql)) {
            // establecer parámetro
            ps.setInt(1, idPublicacion);
            // Ejecutar consulta
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("cnt") > 0;
                }
            }
        } catch (Exception e) {
            // debug
            System.out.println(e.getMessage());
            System.out.println(e.getCause());
        }
        return false;
    }

    /**
     * En el contexto de una transacción, marca la publicación y sus ejemplares
     * como "baja" (estado = FALSE).
     *
     * @param conexion      connection transaccional
     * @param idPublicacion id publicación
     * @return true si OK
     */
    public boolean bajaPublicacion(Connection conexion, int idPublicacion) {
        try (PreparedStatement ps1 = conexion.prepareStatement(SQL_BAJA_EJEMPLARES);
                PreparedStatement ps2 = conexion.prepareStatement(SQL_BAJA_PUBLICACION)) {
            // establecer parámetros y ejecutar
            ps1.setInt(1, idPublicacion);
            ps1.executeUpdate();
            ps2.setInt(1, idPublicacion);
            ps2.executeUpdate();
            return true;
        } catch (Exception e) {
            // debug
            System.out.println(e.getMessage());
            System.out.println(e.getCause());
            return false;
        }
    }

}
