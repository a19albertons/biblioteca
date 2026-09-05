package com.biblioteca.conexiones;


import java.sql.Connection;

/*
 * Interfaz que unifica la forma de obtener las conexiones
 */

public interface DBConnection {

    /**
     * Obtiene la conexión a la base de datos.
     *
     * @return la conexión a la base de datos
     */
    Connection getConnection();

} 
