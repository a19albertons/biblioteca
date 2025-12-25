package com.example;

import com.example.controlador.Controlador;
import com.example.utilities.Fonts;
import com.example.conexiones.DBConnection;
import com.example.conexiones.MySQLConnection;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
        // Aplicar Open Sans y estilo normal por defecto a etiquetas y botones
        Fonts.applyDefaultOpenSans();

        DBConnection dbConnection = new MySQLConnection();
        Controlador controlador = new Controlador(dbConnection);
        controlador.iniciarAplicacion();
    }
}
