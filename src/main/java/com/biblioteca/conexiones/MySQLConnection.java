package com.biblioteca.conexiones;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import com.biblioteca.utilities.ConfigLoader;

public class MySQLConnection implements DBConnection {

    // private static final String URL = "jdbc:mysql://localhost:3306/testdb";
    // private static final String USER = "usuario";
    // private static final String PASSWORD = "usuario123";

    /**
     * Implementación del método getConnection() para obtener una conexión a la base
     * de datos MySQL.
     */
    @Override
    public Connection getConnection() {
        /* Con archivo aplicacion.properties */
        String url = ConfigLoader.get("mysql.url");
        String user = ConfigLoader.get("mysql.user");
        String password = ConfigLoader.get("mysql.password");

        try {
            return DriverManager.getConnection(url, user, password);
        } catch (SQLException e) {
            System.err.println("Error conectando a MySQL: " + e.getMessage());
            return null;
        }
    }
}
