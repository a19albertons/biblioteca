package com.example.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

import com.example.conexiones.MySQLConnection;

/**
 * DAO para Publicacion
 */
public class PublicacionDAO {
    /**
     * Obtiene las editoriales de las publicaciones
     * 
     * @return
     */
    public String[] listaEditoriales() {
        // Listado de editoriales
        ArrayList<String> devolver = new ArrayList<>();
        try (Connection conexion = new MySQLConnection().getConnection();
                // Consulta SQL
                PreparedStatement ps = conexion.prepareStatement(
                        "SELECT editorial FROM publicaciones GROUP BY editorial ORDER BY editorial ASC");) {
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
     * Devuelve un resumen de las publicaciones activas con los campos solicitados:
     * Título, ISBN, Autor(es), Ciclos, Editorial, Disponibles, id
     *
     * @return Matriz String[][] con columnas en este orden: titulo, isbn, autores, ciclos, editorial, disponibles, id
     */
    public String[][] listaPublicacionesResumen() {
        java.util.List<String[]> lista = new ArrayList<>();
        String sql = "SELECT p.id, p.titulo, p.codigo_isbn, "
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

        try (Connection conexion = new MySQLConnection().getConnection();
                PreparedStatement ps = conexion.prepareStatement(sql)) {
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
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
            lista = new ArrayList<>();
            System.out.println(e.getMessage());
            System.out.println(e.getCause());
        }
        return lista.toArray(new String[0][0]);
    }

    /**
     * Obtiene el resumen de una publicacion (mismo formato que listaPublicacionesResumen)
     * por su id
     *
     * @param id
     * @return String[] con columnas: titulo,isbn,autores,ciclos,editorial,disponibles,id
     */
    public String[] obtenerResumenPublicacionPorId(int id) {
        String sql = "SELECT p.id, p.titulo, p.codigo_isbn, "
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

        try (Connection conexion = new MySQLConnection().getConnection();
                PreparedStatement ps = conexion.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
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
            System.out.println(e.getMessage());
            System.out.println(e.getCause());
        }
        return null;
    }

    
}
