package com.example.conexiones;


import java.sql.Connection;

/*
 * Interfaz que unifica la forma de obtener las conexiones
 */

public interface DBConnection {

    Connection getConnection();

} 
