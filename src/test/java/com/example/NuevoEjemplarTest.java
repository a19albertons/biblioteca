package com.example;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.time.LocalDate;

import com.example.controlador.Controlador;
import org.junit.Test;

/**
 * Test para creación de nuevo ejemplar
 */
public class NuevoEjemplarTest {

    @Test
    public void crearEjemplarPublicacionSinEjemplares() {
        Controlador c = new Controlador();
        int idPublicacion = 13; // en inicializacion.sql publicaion 13 no tiene ejemplares
        boolean ok = c.getControladorNuevoEjemplarDialog().crearEjemplar(idPublicacion, LocalDate.now());
        assertTrue(ok);

        String[][] ejemplares = c.getControladorEjemplares().obtenerEjemplaresPorPublicacion(idPublicacion);
        assertNotNull(ejemplares);
        assertTrue(ejemplares.length >= 1);
    }
}
