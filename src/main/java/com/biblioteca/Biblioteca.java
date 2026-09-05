package com.biblioteca;

import com.biblioteca.conexiones.DBConnection;
import com.biblioteca.conexiones.MySQLConnection;
import com.biblioteca.controlador.Controlador;
import com.biblioteca.utilities.Fonts;

/**
 * Hello world!
 *
 */
public final class Biblioteca {
    private Biblioteca() {
    }

    /**
     * Metodo que inicia la aplicacion de la biblioteca.
     * 
     * @param args command line arguments
     */
    public static void main(final String[] args) {

        // Aplicar Open Sans y estilo normal por defecto a etiquetas y botones
        Fonts.applyDefaultOpenSans();

        DBConnection dbConnection = new MySQLConnection();
        Controlador controlador = new Controlador(dbConnection);
        controlador.iniciarAplicacion();
    }
}
