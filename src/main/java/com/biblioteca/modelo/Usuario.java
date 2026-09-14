package com.biblioteca.modelo;

import java.time.LocalDate;

import javax.annotation.Nonnull;

/**
 * Modelo de datos para la tabla usuarios
 */
public class Usuario {
    // Atributos de la tabla usuarios
    /**
     * id del usuario
     */
    private int id;
    /**
     * DNI del usuario
     */
    private String dni;
    /**
     * Nombre del usuario
     */
    private String nombre;
    /**
     * Primer apellido del usuario
     */
    private String apellido1;
    /**
     * Segundo apellido del usuario
     */
    private String apellido2;
    /**
     * Nombre de usuario
     */
    private String usuario;
    /**
     * Email del usuario
     */
    private String email;
    /**
     * Contraseña del usuario
     */
    private String contrasena;
    /**
     * Tipo de usuario
     */
    private TipoUsuario tipo;
    /**
     * Estado del usuario (activo/inactivo)
     */
    private boolean estado;

    // Getters y Setters
    /**
     * Obtiene el id del usuario
     * 
     * @return el id del usuario
     */
    public int getId() {
        return id;
    }

    /**
     * Permite recuperar el id del usuario de la bd y establecerlo en el objeto
     * 
     * @param id el id del usuario
     */
    public void setId(final int id) {
        this.id = id;
    }

    /**
     * Obtiene el DNI del usuario
     * 
     * @return el DNI del usuario
     */
    public String getDni() {
        return dni;
    }

    /**
     * Establece el DNI del usuario
     * 
     * @param dni
     */
    public void setDni(final String dni) {
        this.dni = dni;
    }

    /**
     * Obtiene el nombre del usuario
     * 
     * @return el nombre del usuario
     */
    public String getNombre() {
        return nombre;
    }

    /**
     * Establece el nombre del usuario
     * 
     * @param nombre
     */
    public void setNombre(final String nombre) {
        this.nombre = nombre;
    }

    /**
     * Obtiene el primer apellido del usuario
     * 
     * @return el primer apellido del usuario
     */
    public String getApellido1() {
        return apellido1;
    }

    /**
     * Establece el primer apellido del usuario
     * 
     * @param apellido1
     */
    public void setApellido1(final String apellido1) {
        this.apellido1 = apellido1;
    }

    /**
     * Obtiene el segundo apellido del usuario
     * 
     * @return el segundo apellido del usuario
     */
    public String getApellido2() {
        return apellido2;
    }

    /**
     * Establece el segundo apellido del usuario
     * 
     * @param apellido2
     */
    public void setApellido2(final String apellido2) {
        this.apellido2 = apellido2;
        setUsuario();
    }

    /**
     * Genera el nombre de usuario según los calculos de año, nombre y apellidos
     * 
     * @return el nombre de usuario
     */
    public String getUsuario() {
        return usuario;
    }

    /**
     * Obtiene el email del usuario
     * 
     * @return el email del usuario
     */
    public String getEmail() {
        return email;
    }

    /**
     * Establece el email del usuario (puede ser nulo)
     * 
     * @param email
     */
    public void setEmail(final String email) {
        this.email = email;
    }

    /**
     * Calcula el nombre de usuario: 'A' + YY + nombre + inicial apellido1 + inicial
     * apellido2
     */
    private void setUsuario() {
        String yearSuffix = String.format("%02d", LocalDate.now().getYear() % 100);
        StringBuilder sb = new StringBuilder();
        sb.append('A').append(yearSuffix);
        if (nombre != null) {
            sb.append(nombre);
        }
        if (!apellido1.isEmpty()) {
            sb.append(apellido1.charAt(0));
        }
        if (apellido2 != null && !apellido2.isEmpty()) {
            sb.append(apellido2.charAt(0));
        }
        this.usuario = sb.toString();
    }

    /**
     * Obtiene la contraseña del usuario
     * 
     * @return la contraseña del usuario
     */
    public String getContrasena() {
        return contrasena;
    }

    /**
     * Establece la contraseña del usuario
     * 
     * @param contrasena
     */
    public void setContrasena(final String contrasena) {
        this.contrasena = contrasena;
    }

    /**
     * Obtiene el tipo de usuario
     * 
     * @return el tipo de usuario
     */
    public TipoUsuario getTipo() {
        return tipo;
    }

    /**
     * Establece el tipo de usuario
     * 
     * @param tipo
     */
    public void setTipo(final TipoUsuario tipo) {
        this.tipo = tipo;
    }

    /**
     * Obtiene el estado del usuario
     * 
     * @return el estado del usuario
     */
    public boolean getEstado() {
        return estado;
    }

    /**
     * Establece el estado del usuario
     * 
     * @param estado
     */
    public void setEstado(final boolean estado) {
        this.estado = estado;
    }

    /**
     * Crear nuevo objeto Usuarios
     * 
     * @param dni        el DNI del usuario
     * @param nombre     el nombre del usuario
     * @param apellido1  el primer apellido del usuario
     * @param apellido2  el segundo apellido del usuario
     * @param email      el email del usuario
     * @param contrasena la contraseña del usuario
     * @param tipo       el tipo de usuario
     * @param estado     el estado del usuario
     */
    public Usuario(@Nonnull final String dni, @Nonnull final String nombre, @Nonnull final String apellido1,
            final String apellido2,
            final String email, @Nonnull final String contrasena,
            final TipoUsuario tipo, final boolean estado) {
        setDni(dni);
        setNombre(nombre);
        setApellido1(apellido1);
        setApellido2(apellido2);
        setUsuario();
        setEmail(email);
        setContrasena(contrasena);
        setTipo(tipo);
        setEstado(estado);
    }

    /**
     * Recuperar el usuario de la bd
     * 
     * @param id         el id del usuario
     * @param dni        el DNI del usuario
     * @param nombre     el nombre del usuario
     * @param apellido1  el primer apellido del usuario
     * @param apellido2  el segundo apellido del usuario
     * @param usuario    el nombre de usuario
     * @param email      el email del usuario
     * @param contrasena la contraseña del usuario
     * @param tipo       el tipo de usuario
     * @param estado     el estado del usuario
     */
    public Usuario(final int id, @Nonnull final String dni, @Nonnull final String nombre,
            @Nonnull final String apellido1, final String apellido2,
            final String usuario, final String email,
            @Nonnull final String contrasena, final TipoUsuario tipo, final boolean estado) {
        this.id = id;
        this.dni = dni;
        this.nombre = nombre;
        this.apellido1 = apellido1;
        this.apellido2 = apellido2;
        this.usuario = usuario;
        this.email = email;
        this.contrasena = contrasena;
        this.tipo = tipo;
        this.estado = estado;
    }

}
