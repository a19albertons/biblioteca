package com.biblioteca.utilities;

import java.net.URL;

/**
 * Clase que contiene las rutas de los recursos de la aplicación
 * AppResources
 */
public final class AppResources {
    /**
     * Ruta al logo de la aplicación
     */
    public static final String LOGO_PATH = "/imagenes/logo.png";

    /**
     * Ruta al icono de editar
     */
    public static final String EDITAR_PATH = "/imagenes/editar.png";

    /**
     * Ruta al icono de eliminar
     */
    public static final String ELIMINAR_PATH = "/imagenes/eliminar.png";

    private AppResources() {
        // Evitar instanciación
    }

    /**
     * Devuelve la URL del logo de la aplicación
     * @return URL del logo de la aplicación
     */
    public static URL logoPath() {
        return AppResources.class.getResource(LOGO_PATH);
    }

    /**
     * Devuelve la URL del icono de editar
     * @return URL del icono de editar
     */
    public static URL editarPath() {
        return AppResources.class.getResource(EDITAR_PATH);
    }

    /**
     * Devuelve la URL del icono de eliminar
     * @return URL del icono de eliminar
     */
    public static URL eliminarPath() {
        return AppResources.class.getResource(ELIMINAR_PATH);
    }
}
