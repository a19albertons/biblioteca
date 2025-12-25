package com.example.utilities;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Clase que carga las propiedades de conexión del fichero
 * /resources/application.properties
 * lo que nos permite cambiar cualquier dato de consifuración sin tener que
 * recompilar el proyecto.
 */
public class ConfigLoader {
    /**
     * Propiedades cargadas desde el fichero de configuración
     */
    private static final Properties properties = new Properties();

    // Carga las propiedades al inicializar la clase
    /**
     * Bloque estático para cargar las propiedades desde el fichero al inicializar
     * la clase
     */
    static {
        try (InputStream input = ConfigLoader.class.getClassLoader().getResourceAsStream("application.properties")) {
            if (input == null) {
                throw new RuntimeException("No se encontró el fichero application.properties");
            }
            properties.load(input);
        } catch (IOException ex) {
            throw new RuntimeException("ERROR cargando el fichero de configuración: " + ex.getMessage());
        }
    }

    /**
     * Obtiene el valor de una propiedad por su clave
     * 
     * @param key clave de la propiedad
     * @return valor de la propiedad
     */
    public static String get(String key) {
        return properties.getProperty(key);
    }
}
