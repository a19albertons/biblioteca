package com.biblioteca.conexiones;


import java.sql.Connection;

/*
 * Interfaz que unifica la forma de obtener las conexiones
 */

public interface DBConnection {

    Connection getConnection();

} 
