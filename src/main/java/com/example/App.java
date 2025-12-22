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

        Controlador controlador = new Controlador();
        controlador.iniciarAplicacion();
    }
}
