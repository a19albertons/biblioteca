package com.biblioteca.security;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class HashearContrasenaTest {

    @Test
    public void testHashGeneratesValidHash() {
        char[] passwordChars = "password123".toCharArray();
        String hash = HashearContrasena.hash(passwordChars);
        
        assertNotNull(hash);
        assertFalse(hash.isEmpty());
        // Verifica que el hash comienza con el prefijo de BCrypt
        assertTrue(hash.startsWith("$2a$") || hash.startsWith("$2b$") || hash.startsWith("$2y$"));
        
        // Verifica que la longitud del hash es mayor a 30 caracteres (longitud típica de un hash BCrypt)
        assertTrue(hash.length() > 30);
    }

    @Test
    public void testVerifyWithCorrectPassword() {
        char[] passwordChars = "correctPassword".toCharArray();
        String hash = HashearContrasena.hash(passwordChars);

        boolean isVerified = HashearContrasena.verify(passwordChars, hash);
        assertTrue(isVerified);
    }

    @Test
    public void testVerifyWithIncorrectPassword() {
        char[] passwordChars = "correctPassword".toCharArray();
        char[] wrongPasswordChars = "wrongPassword".toCharArray();
        String hash = HashearContrasena.hash(passwordChars);

        boolean isVerified = HashearContrasena.verify(wrongPasswordChars, hash);
        assertFalse(isVerified);
    }

    @Test
    public void testVerifyWithRandomHash() {
        char[] passwordChars = "somePassword".toCharArray();
        // Un hash totalmente random que no corresponde a la contraseña proporcionada
        String randomHash = "$2a$10$ABCDEFGHIJKLMNOPQRSTUVWXYZ.somehashstring.";
        
        boolean isVerified = HashearContrasena.verify(passwordChars, randomHash);
        assertFalse(isVerified);
    }
    
    @Test
    public void testHashEmptyPassword() {
        char[] passwordChars = new char[0];
        String hash = HashearContrasena.hash(passwordChars);
        assertNotNull(hash);
        assertTrue(hash.startsWith("$2a$"));
    }
}