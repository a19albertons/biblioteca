package com.example;

import com.example.controlador.Controlador;
import com.example.utilities.Fonts;

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

        com.example.conexiones.DBConnection dbConnection = new com.example.conexiones.MySQLConnection();
        Controlador controlador = new Controlador(dbConnection);
        controlador.iniciarAplicacion();
    }
}
