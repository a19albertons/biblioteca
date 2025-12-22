package com.example.conexiones;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import com.example.utilities.ConfigLoader;


public class MySQLConnection implements DBConnection{

    // private static final String URL = "jdbc:mysql://localhost:3306/testdb";
    // private static final String USER = "usuario";
    // private static final String PASSWORD = "usuario123";


    @Override
    public Connection getConnection() {
        /* Con archivo aplicacion.properties */
        String URL = ConfigLoader.get ("mysql.url");
        String USER = ConfigLoader.get ("mysql.user");
        String PASSWORD = ConfigLoader.get ("mysql.password");

        try {
            return DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (SQLException e) {
            System.err.println("Error conectando a MySQL: " + e.getMessage());
            return null;
        }
    }
}
