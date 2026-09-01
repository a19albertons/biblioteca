package com.biblioteca.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

/**
 * DAO para Ciclo
 */
public class CicloDAO {
    /**
     * Conexión a la base de datos
     * 
     * @param conexion la conexión a la base de datos
     */
    private final Connection conexion;

    /**
     * SQL para listar todos los ciclos ordenados por nombre
     */
    private static final String SQL_LISTA_CICLOS = "SELECT nombre FROM ciclos ORDER BY nombre ASC";
    /**
     * SQL para obtener el id de un ciclo por nombre
     */
    private static final String SQL_SELECT_CICLO_ID_POR_NOMBRE = "SELECT id FROM ciclos WHERE nombre = ?";
    /**
     * SQL para insertar un nuevo ciclo
     */
    private static final String SQL_INSERT_CICLO = "INSERT INTO ciclos (nombre) VALUES (?)";

    /**
     * Constructor del DAO
     * 
     * @param conexion conexión a la base de datos (no puede ser null)
     */
    public CicloDAO(final Connection conexion) {
        if (conexion == null) {
            throw new IllegalArgumentException("DBConnection cannot be null");
        }
        this.conexion = conexion;
    }

    /**
     * Obtiene los nombres de los ciclos
     * 
     * @return lista de nombres de ciclos
     */
    public String[] listaCiclos() {
        // Listado de ciclos
        ArrayList<String> devolver = new ArrayList<>();
        try (PreparedStatement ps = conexion.prepareStatement(SQL_LISTA_CICLOS);) {
            // Ejecutar consulta
            try (ResultSet rs = ps.executeQuery();) {
                while (rs.next()) {
                    devolver.add(rs.getString("nombre"));
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
     * Obtiene el id de un ciclo por nombre o lo crea si no existe
     *
     * @param nombre nombre del ciclo
     * @return id del ciclo o -1 en caso de error
     */
    public int obtenerOCrear(final String nombre) {
        try (
                PreparedStatement ps = conexion.prepareStatement(SQL_SELECT_CICLO_ID_POR_NOMBRE);) {
            // establecer parámetro
            ps.setString(1, nombre);
            // ejecutar consulta
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("id");
                }
            }
            // Si no existe, insertamos
            try (PreparedStatement ins = conexion.prepareStatement(SQL_INSERT_CICLO,
                    Statement.RETURN_GENERATED_KEYS)) {
                ins.setString(1, nombre);
                ins.executeUpdate();
                try (ResultSet rs2 = ins.getGeneratedKeys()) {
                    if (rs2.next()) {
                        return rs2.getInt(1);
                    }
                }
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
            System.out.println(e.getCause());
        }
        return -1;
    }

}
