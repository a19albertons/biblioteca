package com.biblioteca.dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.biblioteca.dto.ObtenerPublicacionDetallesPorIdDTO;
import com.biblioteca.modelo.TipoPublicacion;
import com.biblioteca.utilities.RelacionPublicacionHelperEnum;

/**
 * DAO para Publicacion
 */
public class PublicacionDAO {
    /**
     * DBConnection para conexiones a la base de datos
     */
    private final Connection conexion;

    /**
     * Constante SQL para listar editoriales de publicaciones
     */
    private static final String SQL_LISTA_EDITORIALES = "SELECT editorial FROM publicaciones GROUP BY editorial ORDER BY editorial ASC";
    /**
     * Constante SQL para insertar una nueva publicación
     */
    private static final String SQL_INSERT_PUBLICACION = "INSERT INTO publicaciones (titulo, editorial, codigo_isbn, idioma, tipo) VALUES (?, ?, ?, ?, ?)";
    /**
     * Constante SQL para insertar un libro
     */
    private static final String SQL_INSERT_LIBRO = "INSERT INTO libros (id_publicacion, num_edicion, fecha_publicacion) VALUES (?, ?, ?)";
    /**
     * Constante SQL para insertar una revista
     */
    private static final String SQL_INSERT_REVISTA = "INSERT INTO revistas (id_publicacion, periodicidad, num_revista) VALUES (?, ?, ?)";
    /**
     * Constante SQL para insertar una relación entre publicación y módulo
     */
    private static final String SQL_INSERT_PUBLICACION_MODULO = "INSERT INTO publicacion_modulo (id_publicacion, id_modulo) VALUES (?, ?)";
    /**
     * Constante SQL para insertar una relación entre publicación y ciclo
     */
    private static final String SQL_INSERT_PUBLICACION_CICLO = "INSERT INTO publicacion_ciclo (id_publicacion, id_ciclo) VALUES (?, ?)";
    /**
     * Constante SQL para insertar una relación entre publicación y tema
     */
    private static final String SQL_INSERT_PUBLICACION_TEMA = "INSERT INTO publicacion_tema (id_publicacion, id_tema) VALUES (?, ?)";
    /**
     * Constante SQL para insertar una relación entre libro y autor
     */
    private static final String SQL_INSERT_LIBRO_AUTOR = "INSERT INTO libros_autores (id_libro, id_autor) VALUES (?, ?)";
    /**
     * Constante SQL para obtener el máximo número de revista
     */
    private static final String SQL_SELECT_MAX_NUM_REVISTA = "SELECT COALESCE(MAX(num_revista),0) AS m FROM revistas";
    /**
     * Constante SQL para obtener un resumen de publicaciones activas
     */
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
    /**
     * Constante SQL para obtener un resumen de una publicación por id
     */
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
    /**
     * Constante SQL para obtener los detalles de una publicación por id
     */
    private static final String SQL_PUBLICACION_DETALLES_POR_ID = "SELECT p.id, p.titulo, p.codigo_isbn, p.idioma, p.tipo, "
            + "COALESCE(GROUP_CONCAT(DISTINCT t.nombre SEPARATOR ', '),'') AS temas, "
            + "COALESCE(GROUP_CONCAT(DISTINCT m.nombre SEPARATOR ', '),'') AS modulos, "
            + "COALESCE(GROUP_CONCAT(DISTINCT c.nombre SEPARATOR ', '),'') AS ciclos, "
            + "p.editorial, l.num_edicion, l.fecha_publicacion, "
            + "COALESCE(GROUP_CONCAT(DISTINCT a.nombre SEPARATOR ', '),'') AS autores, "
            + "r.periodicidad, "
            + "p.estado "
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
    /**
     * Constante SQL para actualizar publicaciones
     */
    private static final String SQL_UPDATE_PUBLICACION = "UPDATE publicaciones SET titulo = ?, editorial = ?, codigo_isbn = ?, idioma = ?, tipo = ? WHERE id = ?";
    /**
     * Constante SQL para actualizar libros
     */
    private static final String SQL_UPDATE_LIBROS = "UPDATE libros SET num_edicion = ?, fecha_publicacion = ? WHERE id_publicacion = ?";
    /**
     * Constante SQL para actualizar revistas
     */
    private static final String SQL_UPDATE_REVISTAS = "UPDATE revistas SET periodicidad = ? WHERE id_publicacion = ?";
    /**
     * Constante SQL para eliminar relaciones publicación-módulo
     */
    private static final String SQL_DELETE_PUBLICACION_MODULO = "DELETE FROM publicacion_modulo WHERE id_publicacion = ?";
    /**
     * Constante SQL para eliminar relaciones publicación-ciclo
     */
    private static final String SQL_DELETE_PUBLICACION_CICLO = "DELETE FROM publicacion_ciclo WHERE id_publicacion = ?";
    /**
     * Constante SQL para eliminar relaciones publicación-tema
     */
    private static final String SQL_DELETE_PUBLICACION_TEMA = "DELETE FROM publicacion_tema WHERE id_publicacion = ?";
    /**
     * Constante SQL para eliminar relaciones libro-autor
     */
    private static final String SQL_DELETE_LIBROS_AUTORES = "DELETE FROM libros_autores WHERE id_libro = ?";
    /**
     * Constante SQL para comprobar préstamos activos
     */
    private static final String SQL_TIENEPRESTAMOS_ACTIVOS = "SELECT COUNT(*) AS cnt FROM prestamos p JOIN ejemplares e ON p.id_ejemplar = e.id WHERE e.id_publicacion = ? AND p.estado = TRUE";
    /**
     * Constante SQL para marcar ejemplares como inactivos
     */
    private static final String SQL_BAJA_EJEMPLARES = "UPDATE ejemplares SET estado = FALSE WHERE id_publicacion = ?";
    /**
     * Constante SQL para marcar una publicación como inactiva
     */
    private static final String SQL_BAJA_PUBLICACION = "UPDATE publicaciones SET estado = FALSE WHERE id = ?";

    /**
     * Constructor del DAO
     * 
     * @param conexion connection to the database
     */
    public PublicacionDAO(final Connection conexion) {

        if (conexion == null) {
            throw new IllegalArgumentException("conexion cannot be null");
        }
        this.conexion = conexion;
    }

    /**
     * Obtiene las editoriales de las publicaciones
     * 
     * @return array of editorial names
     */
    public String[] listaEditoriales() {
        // Listado de editoriales
        ArrayList<String> devolver = new ArrayList<>();
        try (
                // Consulta SQL
                PreparedStatement ps = conexion.prepareStatement(SQL_LISTA_EDITORIALES);) {
            // Ejecutar consulta
            try (ResultSet rs = ps.executeQuery();) {
                while (rs.next()) {
                    devolver.add(rs.getString("editorial"));
                }
            }
        } catch (SQLException e) {
            devolver = new ArrayList<>();
            System.out.println(e.getMessage());
            System.out.println(e.getCause());

        }
        return devolver.toArray(new String[0]);
    }

    /**
     * Inserta una nueva publicación y devuelve su id generado.
     *
     * @param titulo     título de la publicación
     * @param editorial  editorial de la publicación
     * @param codigoIsbn código ISBN de la publicación
     * @param idioma     idioma de la publicación
     * @param tipo       tipo de publicación ('L' o 'R')
     * @return id generado o -1 en caso de error
     */
    public int insertarPublicacion(final String titulo, final String editorial, final String codigoIsbn,
            final String idioma, final char tipo) {
        try (
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
        } catch (SQLException e) {
            // debug
            System.out.println(e.getMessage());
            System.out.println(e.getCause());
        }
        return -1;
    }

    /**
     * Inserta los datos de libros (tabla libros)
     *
     * @param idPublicacion    id de la publicación
     * @param numEdicion       número de edición
     * @param fechaPublicacion fecha SQL (java.sql.Date)
     * @return true si ok
     */
    public boolean insertarLibro(final int idPublicacion, final int numEdicion, final Date fechaPublicacion) {
        try (
                PreparedStatement ps = conexion.prepareStatement(SQL_INSERT_LIBRO)) {
            // establecer parámetros
            ps.setInt(1, idPublicacion);
            ps.setInt(2, numEdicion);
            ps.setDate(3, fechaPublicacion);
            // ejecutar
            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            // debug
            System.out.println(e.getMessage());
            System.out.println(e.getCause());
            return false;
        }
    }

    /**
     * Inserta los datos de revistas (tabla revistas)
     *
     * @param idPublicacion id de la publicación
     * @param periodicidad  periodicidad de la revista
     * @param numRevista    número de revista
     * @return true si ok
     */
    public boolean insertarRevista(final int idPublicacion, final String periodicidad, final int numRevista) {
        try (
                PreparedStatement ps = conexion.prepareStatement(SQL_INSERT_REVISTA)) {
            // establecer parámetros
            ps.setInt(1, idPublicacion);
            ps.setString(2, periodicidad);
            ps.setInt(3, numRevista);
            // ejecutar
            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            // debug
            System.out.println(e.getMessage());
            System.out.println(e.getCause());
            return false;
        }
    }

    /**
     * Inserta relación publicacion <-> modulo
     *
     * @param idPublicacion id de la publicación
     * @param idModulo      id del módulo
     * @return true si ok
     */
    public boolean insertarPublicacionModulo(final int idPublicacion, final int idModulo) {
        try (
                PreparedStatement ps = conexion.prepareStatement(SQL_INSERT_PUBLICACION_MODULO)) {
            // establecer parámetros
            ps.setInt(1, idPublicacion);
            ps.setInt(2, idModulo);
            // ejecutar
            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            // debug
            System.out.println(e.getMessage());
            System.out.println(e.getCause());
            return false;
        }
    }

    /**
     * Inserta relación publicacion <-> ciclo
     *
     * @param idPublicacion id de la publicación
     * @param idCiclo       id del ciclo
     * @return true si ok
     */
    public boolean insertarPublicacionCiclo(final int idPublicacion, final int idCiclo) {
        try (
                PreparedStatement ps = conexion.prepareStatement(SQL_INSERT_PUBLICACION_CICLO)) {
            // establecer parámetros
            ps.setInt(1, idPublicacion);
            ps.setInt(2, idCiclo);
            // ejecutar
            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            // debug
            System.out.println(e.getMessage());
            System.out.println(e.getCause());
            return false;
        }
    }

    /**
     * Inserta relación publicacion <-> tema
     *
     * @param idPublicacion id de la publicación
     * @param idTema        id del tema
     * @return true si ok
     */
    public boolean insertarPublicacionTema(final int idPublicacion, final int idTema) {
        try (
                PreparedStatement ps = conexion.prepareStatement(SQL_INSERT_PUBLICACION_TEMA)) {
            // establecer parámetros
            ps.setInt(1, idPublicacion);
            ps.setInt(2, idTema);
            // ejecutar
            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            // debug
            System.out.println(e.getMessage());
            System.out.println(e.getCause());
            return false;
        }
    }

    /**
     * Inserta relación publicacion <-> relación (modulos, ciclos, temas).
     * Método genérico para evitar duplicación de código.
     *
     * @param idPublicacion id de la publicación
     * @param idRelacion    id de la relación
     * @param tipoRelacion  tipo de relación: 'M' para modulos, 'C' para ciclos, 'T' para temas
     * @return true si ok
     */
    public boolean insertarPublicacionRelacion(final int idPublicacion, final int idRelacion, final RelacionPublicacionHelperEnum tipoRelacion) {
        switch (tipoRelacion) {
            case MODULO:
                return insertarPublicacionModulo(idPublicacion, idRelacion);
            case CICLO:
                return insertarPublicacionCiclo(idPublicacion, idRelacion);
            case TEMA:
                return insertarPublicacionTema(idPublicacion, idRelacion);
            default:
                System.out.println("Tipo de relación no válido: " + tipoRelacion);
                return false;
        }
    }

    /**
     * Inserta relación libro <-> autor
     *
     * @param idLibro id del libro
     * @param idAutor id del autor
     * @return true si ok
     */
    public boolean insertarLibroAutor(final int idLibro, final int idAutor) {
        try (
                PreparedStatement ps = conexion.prepareStatement(SQL_INSERT_LIBRO_AUTOR)) {
            // establecer parámetros
            ps.setInt(1, idLibro);
            ps.setInt(2, idAutor);
            // ejecutar
            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
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
        try (
                PreparedStatement ps = conexion.prepareStatement(SQL_SELECT_MAX_NUM_REVISTA)) {
            // ejecutar consulta
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("m") + 1;
                }
            }
        } catch (SQLException e) {
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
     * @return matriz String[][] con columnas en este orden: titulo, isbn, autores,
     *         ciclos, editorial, disponibles, id
     */
    public String[][] listaPublicacionesResumen() {
        List<String[]> lista = new ArrayList<>();
        final String sql = SQL_LISTA_PUBLICACIONES_RESUMEN;

        try (
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

                    if (autores == null) {
                        autores = "";
                    }
                    if (ciclos == null) {
                        ciclos = "";
                    }
                    if (editorial == null) {
                        editorial = "";
                    }

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
        } catch (SQLException e) {
            // debug
            lista = new ArrayList<>();
            System.out.println(e.getMessage());
            System.out.println(e.getCause());
        }
        return lista.toArray(new String[0][0]);
    }

    /**
     * Obtiene el resumen de una publicación (mismo formato que
     * listaPublicacionesResumen) por su id
     *
     * @param id id de la publicación
     * @return String[] con columnas:
     *         titulo,isbn,autores,ciclos,editorial,disponibles,id
     */
    public String[] obtenerResumenPublicacionPorId(final int id) {
        final String sql = SQL_OBTENER_PUBLICACION_RESUMEN_POR_ID;

        try (
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

                    if (autores == null) {
                        autores = "";
                    }
                    if (ciclos == null) {
                        ciclos = "";
                    }
                    if (editorial == null) {
                        editorial = "";
                    }

                    return new String[] { titulo, isbn, autores, ciclos, editorial, disponibles, String.valueOf(id) };
                }
            }
        } catch (SQLException e) {
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
    public ObtenerPublicacionDetallesPorIdDTO obtenerPublicacionDetallesPorId(final int id) {
        final String sql = SQL_PUBLICACION_DETALLES_POR_ID;

        try (
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
                    Boolean estado = rs.getBoolean("estado");

                    // devolver arreglo con valores (evitar nulls)
                    return new ObtenerPublicacionDetallesPorIdDTO(
                            TipoPublicacion.valueOf(tipo),
                            titulo == null ? "" : titulo,
                            isbn == null ? "" : isbn,
                            idioma == null ? "" : idioma,
                            temas == null ? "" : temas,
                            modulos == null ? "" : modulos,
                            ciclos == null ? "" : ciclos,
                            editorial == null ? "" : editorial,
                            numEd == null ? "" : numEd,
                            fechaPub == null ? "" : fechaPub,
                            autores == null ? "" : autores,
                            periodicidad == null ? "" : periodicidad,
                            id,
                            estado);
                }
            }
        } catch (SQLException e) {
            // debug
            System.out.println(e.getMessage());
            System.out.println(e.getCause());
        }
        return null;
    }

    /**
     * Actualiza los campos base de la tabla `publicaciones`.
     *
     * @param id         id de la publicación a actualizar
     * @param titulo     nuevo título
     * @param editorial  nueva editorial
     * @param codigoIsbn nuevo código ISBN
     * @param idioma     nuevo idioma
     * @param tipo       tipo ('L' o 'R')
     * @return true si la actualización tuvo éxito (o no hubo cambios), false en
     *         caso de error
     */
    public boolean actualizarPublicacion(final int id, final String titulo, final String editorial,
            final String codigoIsbn, final String idioma, final char tipo) {
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
        } catch (SQLException e) {
            // debug
            System.out.println(e.getMessage());
            System.out.println(e.getCause());
            return false;
        }
    }

    /**
     * Actualiza o inserta la fila en `libros` para la publicación dada.
     * 
     * @param idPublicacion    id de la publicación
     * @param numEdicion       nuevo número de edición
     * @param fechaPublicacion nueva fecha de publicación (java.sql.Date)
     * @return true si la actualización o inserción tuvo éxito, false en caso de
     *         error
     */
    public boolean actualizarLibro(final int idPublicacion, final int numEdicion, final Date fechaPublicacion) {
        try (PreparedStatement ps = conexion.prepareStatement(SQL_UPDATE_LIBROS)) {
            // establecer parámetros
            ps.setInt(1, numEdicion);
            ps.setDate(2, fechaPublicacion);
            ps.setInt(3, idPublicacion);
            // ejecutar
            int updated = ps.executeUpdate();
            if (updated == 0) {
                // No existe, insertar
                return insertarLibro(idPublicacion, numEdicion, fechaPublicacion);
            }
            return true;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            System.out.println(e.getCause());
            return false;
        }
    }

    /**
     * Actualiza o inserta la fila en `revistas` para la publicación dada.
     * 
     * @param idPublicacion id de la publicación
     * @param periodicidad  nueva periodicidad
     * @return true si la actualización o inserción tuvo éxito, false en caso de
     *         error
     */
    public boolean actualizarRevista(final int idPublicacion, final String periodicidad) {
        try (PreparedStatement ps = conexion.prepareStatement(SQL_UPDATE_REVISTAS)) {
            // establecer parámetros
            ps.setString(1, periodicidad);
            ps.setInt(2, idPublicacion);
            // ejecutar
            int updated = ps.executeUpdate();
            if (updated == 0) {
                // necesitar asignar num_revista; usar siguienteNumRevista
                int numRev = siguienteNumRevista();
                return insertarRevista(idPublicacion, periodicidad, numRev);
            }
            return true;
        } catch (SQLException e) {
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
     * @param idPublicacion id de la publicación
     * @return true si las eliminaciones se realizaron correctamente, false en caso
     *         de error
     */
    public boolean eliminarRelacionesPublicacion(final int idPublicacion) {
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
        } catch (SQLException e) {
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
     * @return true si existe al menos un préstamo activo, false en caso contrario
     */
    public boolean tienePrestamosActivos(final int idPublicacion) {
        final String sql = SQL_TIENEPRESTAMOS_ACTIVOS;
        try (
                PreparedStatement ps = conexion.prepareStatement(sql)) {
            // establecer parámetro
            ps.setInt(1, idPublicacion);
            // Ejecutar consulta
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("cnt") > 0;
                }
            }
        } catch (SQLException e) {
            // debug
            System.out.println(e.getMessage());
            System.out.println(e.getCause());
            return false;
        }
        return false;
    }

    /**
     * En el contexto de una transacción, marca la publicación y sus ejemplares
     * como "baja" (estado = FALSE).
     *
     * @param idPublicacion id publicación
     * @return true si OK
     */
    public boolean bajaPublicacion(final int idPublicacion) {
        try (PreparedStatement ps1 = conexion.prepareStatement(SQL_BAJA_EJEMPLARES);
                PreparedStatement ps2 = conexion.prepareStatement(SQL_BAJA_PUBLICACION)) {
            // establecer parámetros y ejecutar
            ps1.setInt(1, idPublicacion);
            ps1.executeUpdate();
            ps2.setInt(1, idPublicacion);
            ps2.executeUpdate();
            return true;
        } catch (SQLException e) {
            // debug
            System.out.println(e.getMessage());
            System.out.println(e.getCause());
            return false;
        }
    }

}
