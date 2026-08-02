package com.biblioteca.security;

import at.favre.lib.crypto.bcrypt.BCrypt;

/**
 * Clase utilitaria para hashear y verificar contraseñas usando BCrypt.
 */
public class HashearContrasena {
    private static final int COST = 12;

    /**
     * Hashea la contraseña proporcionada usando BCrypt.
     * @param password la contraseña como un char array
     * @return el hash de la contraseña
     */
    public static String hash(char[] password) {
        return BCrypt.withDefaults().hashToString(COST, password);
    }

    /**
     * Verifica si la contraseña proporcionada coincide con el hash almacenado.
     * @param password la contraseña como un char array
     * @param storedHash el hash almacenado
     * @return true si la contraseña es correcta, false en caso contrario
     */
    public static boolean verify(char[] password, String storedHash) {
        return BCrypt.verifyer()
                .verify(password, storedHash).verified;
    }
}
