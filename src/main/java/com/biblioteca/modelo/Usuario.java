package com.biblioteca.modelo;

import java.time.LocalDate;

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
     * @return
     */
    public int getId() {
        return id;
    }

    /**
     * Permite recuperar el id del usuario de la bd y establecerlo en el objeto
     * 
     * @return
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Obtiene el DNI del usuario
     * 
     * @return
     */
    public String getDni() {
        return dni;
    }

    /**
     * Establece el DNI del usuario
     * 
     * @param dni
     */
    public void setDni(String dni) {
        if (dni != null) {
            this.dni = dni;
        } else {
            throw new IllegalArgumentException("El DNI no puede ser nulo");
        }
    }

    /**
     * Obtiene el nombre del usuario
     * 
     * @return
     */
    public String getNombre() {
        return nombre;
    }

    /**
     * Establece el nombre del usuario
     * 
     * @param nombre
     */
    public void setNombre(String nombre) {
        if (nombre != null) {
            this.nombre = nombre;
            setUsuario();
        } else {
            throw new IllegalArgumentException("El nombre no puede ser nulo");
        }
    }

    /**
     * Obtiene el primer apellido del usuario
     * 
     * @return
     */
    public String getApellido1() {
        return apellido1;
    }

    /**
     * Establece el primer apellido del usuario
     * 
     * @param apellido1
     */
    public void setApellido1(String apellido1) {
        if (apellido1 != null && !apellido1.isEmpty()) {
            this.apellido1 = apellido1;
            setUsuario();
        } else {
            throw new IllegalArgumentException("El primer apellido no puede ser nulo o vacío");
        }
    }

    /**
     * Obtiene el segundo apellido del usuario
     * 
     * @return
     */
    public String getApellido2() {
        return apellido2;
    }

    /**
     * Establece el segundo apellido del usuario
     * 
     * @param apellido2
     */
    public void setApellido2(String apellido2) {
        this.apellido2 = apellido2;
        setUsuario();
    }

    /**
     * Genera el nombre de usuario según los calculos de año, nombre y apellidos
     * 
     * @return
     */
    public String getUsuario() {
        return usuario;
    }

    /**
     * Obtiene el email del usuario
     * 
     * @return
     */
    public String getEmail() {
        return email;
    }

    /**
     * Establece el email del usuario (puede ser nulo)
     * 
     * @param email
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Calcula el nombre de usuario: 'A' + YY + nombre + inicial apellido1 + inicial apellido2
     */
    private void setUsuario() {
        String yearSuffix = String.format("%02d", LocalDate.now().getYear() % 100);
        StringBuilder sb = new StringBuilder();
        sb.append('A').append(yearSuffix);
        if (nombre != null) sb.append(nombre);
        if (apellido1 != null && !apellido1.isEmpty()) sb.append(apellido1.charAt(0));
        if (apellido2 != null && !apellido2.isEmpty()) sb.append(apellido2.charAt(0));
        this.usuario = sb.toString();
    }

    /**
     * Obtiene la contraseña del usuario
     * 
     * @return
     */
    public String getContrasena() {
        return contrasena;
    }

    /**
     * Establece la contraseña del usuario
     * 
     * @param contrasena
     */
    public void setContrasena(String contrasena) {
        if (contrasena != null) {
            this.contrasena = contrasena;
        } else {
            throw new IllegalArgumentException("La contraseña no puede ser nula");
        }
    }

    /**
     * Obtiene el tipo de usuario
     * 
     * @return
     */
    public TipoUsuario getTipo() {
        return tipo;
    }

    /**
     * Establece el tipo de usuario
     * 
     * @param tipo
     */
    public void setTipo(TipoUsuario tipo) {
        this.tipo = tipo;
    }

    /**
     * Obtiene el estado del usuario
     * 
     * @return
     */
    public boolean getEstado() {
        return estado;
    }

    /**
     * Establece el estado del usuario
     * 
     * @param estado
     */
    public void setEstado(boolean estado) {
        this.estado = estado;
    }

    /**
     * Crear nuevo objeto Usuarios
     * 
     * @param dni
     * @param nombre
     * @param apellido1
     * @param apellido2
     * @param contrasena
     * @param tipo
     * @param estado
     */
    public Usuario(String dni, String nombre, String apellido1, String apellido2, String email, String contrasena,
            TipoUsuario tipo, boolean estado) {
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
     * @param id
     * @param dni
     * @param nombre
     * @param apellido1
     * @param apellido2
     * @param usuario
     * @param contrasena
     * @param tipo
     * @param estado
     */
    public Usuario(int id, String dni, String nombre, String apellido1, String apellido2, String usuario, String email,
            String contrasena, TipoUsuario tipo, boolean estado) {
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
