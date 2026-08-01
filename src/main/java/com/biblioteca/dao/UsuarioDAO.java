package com.biblioteca.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import com.biblioteca.dto.UsuarioEstadoPorDNIOID;
import com.biblioteca.dto.UsuarioTipoDTO;
import com.biblioteca.modelo.TipoUsuario;
import com.biblioteca.modelo.Usuario;

/**
 * Clase para el acceso a datos de Usuario
 */
public class UsuarioDAO {
    /**
     * Conexión para la base de datos
     */
    private final Connection conexion;

    // SQL constants 🔧
    private static final String SQL_CONSULTA_INICIO_SESION = "SELECT * FROM usuarios WHERE usuario = ? AND contrasena = ?";
    private static final String SQL_CONSULTA_RECUPERAR_CUENTA = "SELECT * FROM usuarios WHERE usuario = ? OR email = ?";
    private static final String SQL_TOTAL_SOCIOS_ACTIVOS = "SELECT COUNT(*) AS TOTAL FROM usuarios where estado = TRUE";
    private static final String SQL_LISTA_USUARIOS_ESTADO = "SELECT u.id, u.dni, CONCAT(u.nombre, ' ', u.apellido1, ' ', u.apellido2) AS nombre_completo, "
            + "CASE WHEN u.estado = FALSE THEN 'BAJA' "
            + "WHEN EXISTS (SELECT 1 FROM sanciones s WHERE s.id_usuario = u.id AND s.estado = TRUE) "
            + "THEN 'SANCIONADO' ELSE 'ACTIVO' END AS sancion_activa, "
            + "u.tipo "
            + "FROM usuarios u ORDER BY u.nombre ASC, u.apellido1 ASC, u.apellido2 ASC";
    private static final String SQL_INSERT_USUARIO = "INSERT INTO usuarios (dni, nombre, apellido1, apellido2, email, contrasena, tipo, estado) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
    private static final String SQL_SELECT_USUARIO_POR_ID = "SELECT id, dni, nombre, apellido1, apellido2, email, tipo, estado FROM usuarios WHERE id = ?";
    private static final String SQL_USUARIO_Y_ESTADO_BASE = "SELECT u.id, u.dni, CONCAT(u.nombre, ' ', u.apellido1, ' ', u.apellido2) AS nombre_completo, "
            + "CASE WHEN u.estado = FALSE THEN 'BAJA' "
            + "WHEN EXISTS (SELECT 1 FROM sanciones s WHERE s.id_usuario = u.id AND s.estado = TRUE) "
            + "THEN 'SANCIONADO' ELSE 'ACTIVO' END AS sancion_activa, "
            + "u.tipo "
            + "FROM usuarios u";
    private static final String SQL_UPDATE_USUARIO = "UPDATE usuarios SET dni = ?, nombre = ?, apellido1 = ?, apellido2 = ?, email = ?, tipo = ? WHERE id = ?";
    private static final String SQL_BAJA_USUARIO = "UPDATE usuarios SET estado = FALSE WHERE id = ?";
    private static final String SQL_CUENTA_PRESTAMOS_ACTIVOS_POR_USUARIO = "SELECT COUNT(*) AS total FROM prestamos WHERE id_usuario = ? AND estado = TRUE";
    private static final String SQL_USUARIOS_SANCIONABLES = "SELECT u.id, u.dni, CONCAT(u.apellido1, ', ', u.nombre) AS nombre_completo, u.tipo "
            + "FROM usuarios u WHERE u.tipo = 'E' AND u.estado = TRUE ORDER BY u.apellido1, u.nombre";
    private static final String SQL_OBTENER_USUARIO_TIPO_DTO = "SELECT id, tipo FROM usuarios WHERE id = ?";

    /**
     * Constructor de UsuarioDAO
     * 
     * @param conexion
     */
    public UsuarioDAO(Connection conexion) {
        if (conexion == null) {
            throw new IllegalArgumentException("Connection cannot be null");
        }
        this.conexion = conexion;
    }

    /**
     * Consulta el inicio de sesión de un usuario
     * 
     * @param usuario    Nombre de usuario
     * @param contrasena Contraseña del usuario
     * @return Usuario si las credenciales son correctas, null en caso contrario
     */
    public Usuario consultaInicioSesion(String usuario, String contrasena) {
        Usuario devolver = null;
        try (
                PreparedStatement ps = conexion.prepareStatement(SQL_CONSULTA_INICIO_SESION)) {
            // establecer parámetros
            ps.setString(1, usuario);
            ps.setString(2, contrasena);
            // ejecutar consulta
            try (ResultSet resultado = ps.executeQuery()) {
                if (resultado.next()) {
                    // crear objeto Usuario con los datos obtenidos
                    devolver = new Usuario(
                            resultado.getInt("id"),
                            resultado.getString("dni"),
                            resultado.getString("nombre"),
                            resultado.getString("apellido1"),
                            resultado.getString("apellido2"),
                            resultado.getString("usuario"),
                            resultado.getString("email"),
                            resultado.getString("contrasena"),
                            TipoUsuario.valueOf(resultado.getString("tipo")),
                            resultado.getBoolean("estado"));
                }
                // Si no hay resultados, devolver sigue siendo null. Si saltase excepción
                // tendrías que abrir esto desde una linea de comandos para ver errores critico
                // o todos los detalles
            } catch (Exception e) {
                devolver = null;
                System.out.println(e.getMessage());
                System.out.println(e.getCause());
            }
            // Si no hay resultados, devolver sigue siendo null. Si saltase excepción
            // tendrías que abrir esto desde una linea de comandos para ver errores critico
            // o todos los detalles
        } catch (Exception e) {
            devolver = null;
            System.out.println(e.getMessage());
            System.out.println(e.getCause());
        }
        return devolver;
    }

    /**
     * Consulta para recuperar la cuenta de un usuario
     * 
     * @param trim
     * @return
     */
    public Usuario consultaRecuperarCuenta(String trim) {
        Usuario devolver = null;
        try (
                PreparedStatement ps = conexion.prepareStatement(SQL_CONSULTA_RECUPERAR_CUENTA)) {
            // establecer parámetros
            ps.setString(1, trim);
            ps.setString(2, trim);
            // ejecutar consulta
            try (ResultSet resultado = ps.executeQuery()) {
                if (resultado.next()) {
                    // crear objeto Usuario con los datos obtenidos
                    devolver = new Usuario(
                            resultado.getInt("id"),
                            resultado.getString("dni"),
                            resultado.getString("nombre"),
                            resultado.getString("apellido1"),
                            resultado.getString("apellido2"),
                            resultado.getString("usuario"),
                            resultado.getString("email"),
                            resultado.getString("contrasena"),
                            TipoUsuario.valueOf(resultado.getString("tipo")),
                            resultado.getBoolean("estado"));
                }
                // Si no hay resultados, devolver sigue siendo null. Si saltase excepción
                // tendrías que abrir esto desde una linea de comandos para ver errores critico
                // o todos los detalles
            } catch (Exception e) {
                devolver = null;
                System.out.println(e.getMessage());
                System.out.println(e.getCause());
            }
            // Si no hay resultados, devolver sigue siendo null. Si saltase excepción
            // tendrías que abrir esto desde una linea de comandos para ver errores critico
            // o todos los detalles
        } catch (Exception e) {
            devolver = null;
            System.out.println(e.getMessage());
            System.out.println(e.getCause());
        }
        return devolver;
    }

    /**
     * Obtiene el número total de socios activos
     * 
     * @return
     */
    public String totalSociosActivos() {
        String totalSocios = "-1";
        // Consulta SQL para contar los socios activos
        try (
                PreparedStatement ps = conexion.prepareStatement(SQL_TOTAL_SOCIOS_ACTIVOS)) {
            // ejecutar consulta
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    totalSocios = rs.getString("TOTAL");
                }
            }

        } catch (Exception e) {
            // Manejo de excepciones. Se ve en consola
            System.out.println(e.getMessage());
            System.out.println(e.getCause());
            totalSocios = "-1";
        }
        return totalSocios;
    }

    /**
     * Obtiene el id, dni, nombre + apellidos, tipo (version larga) y estado
     * (activo/sancionado)
     * 
     * @return String[][] con columnas: id, dni, nombre_completo, sancion_activa,
     *         tipo
     */
    public String[][] listaUsuariosYEstadoSancionActiva() {
        // Listado de usuarios
        String[][] devolver = new String[0][0];
        try (
                // Consulta SQL
                PreparedStatement ps = conexion.prepareStatement(SQL_LISTA_USUARIOS_ESTADO);) { // Ejecutar consulta
            try (ResultSet rs = ps.executeQuery();) {
                // Collect rows into a list (works with forward-only ResultSet)
                java.util.List<String[]> rows = new java.util.ArrayList<>();
                while (rs.next()) {
                    String[] fila = new String[5];
                    fila[0] = rs.getString("id");
                    fila[1] = rs.getString("dni");
                    fila[2] = rs.getString("nombre_completo");
                    fila[3] = rs.getString("sancion_activa");
                    // Map single-letter code to descriptive name using TipoUsuario enum
                    String tipoCode = rs.getString("tipo");
                    String tipoDesc = "";
                    // Comprueba si el tipo esta declarado y despues llama a la descripcion
                    if (tipoCode != null) {
                        try {
                            tipoDesc = TipoUsuario.valueOf(tipoCode).getDescripcion();
                        } catch (IllegalArgumentException e) {
                            tipoDesc = tipoCode; // fallback to raw code if unknown
                        }
                    }
                    fila[4] = tipoDesc;
                    rows.add(fila);
                }
                // Convert list to array for return
                devolver = rows.toArray(new String[0][0]);
            }
        } catch (Exception e) {
            // Manejo de excepciones. Se ve en consola
            devolver = new String[0][0];
            System.out.println(e.getMessage());
            System.out.println(e.getCause());
        }
        return devolver;
    }

    /**
     * Inserta un nuevo usuario usando la conexión proporcionada. NO cierra la
     * conexión (permite uso transaccional).
     *
     * @param conexion
     * @param dni
     * @param nombre
     * @param apellido1
     * @param apellido2
     * @param email
     * @param contrasena
     * @param tipo       código (E,P,A,C,L)
     * @param estado     true = activo
     * @return true si la inserción fue exitosa
     */
    public boolean insertarUsuario(Connection conexion, String dni, String nombre, String apellido1, String apellido2,
            String email, String contrasena, String tipo, boolean estado) {
        // Consulta SQL
        final String sql = SQL_INSERT_USUARIO;
        try (PreparedStatement ps = conexion.prepareStatement(sql)) {
            // Asignar parámetros
            ps.setString(1, dni);
            ps.setString(2, nombre);
            ps.setString(3, apellido1);
            ps.setString(4, apellido2);
            ps.setString(5, email);
            ps.setString(6, contrasena);
            ps.setString(7, tipo);
            ps.setBoolean(8, estado);
            // Ejecutar inserción
            int rows = ps.executeUpdate();
            return rows == 1;
        } catch (Exception e) {
            // Manejo de excepciones. Se ve en consola
            System.out.println("Error insertando usuario: " + e.getMessage());
            System.out.println(e.getCause());
            return false;
        }
    }

    /**
     * Obtiene los detalles de un usuario por su id. Devuelve array: dni, nombre,
     * apellido1, apellido2, email, tipo, id
     * 
     * @param idUsuario id del usuario
     * @return String[] con los detalles o null si error
     */
    public String[] obtenerDetallesUsuario(int idUsuario) {
        String[] devolver = null;
        try (
                // Consulta SQL
                PreparedStatement ps = conexion.prepareStatement(SQL_SELECT_USUARIO_POR_ID)) {
            // Asignar parámetro
            ps.setInt(1, idUsuario);
            // Ejecutar consulta
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    // Rellenar array de devolución
                    devolver = new String[7];
                    devolver[0] = rs.getString("dni");
                    devolver[1] = rs.getString("nombre");
                    devolver[2] = rs.getString("apellido1");
                    devolver[3] = rs.getString("apellido2");
                    devolver[4] = rs.getString("email");
                    devolver[5] = rs.getString("tipo");
                    devolver[6] = rs.getString("id");
                }
            }
        } catch (Exception e) {
            // Manejo de excepciones. Se ve en consola
            System.out.println("Error obteniendo detalles de usuario: " + e.getMessage());
            System.out.println(e.getCause());
            devolver = null;
        }
        return devolver;
    }

    /**
     * Obtiene los datos de un usuario por DNI o por ID (cadena). Devuelve
     * arreglo: id, dni, nombre_completo, sancion_activa (SANCIONADO/ACTIVO/BAJA),
     * tipo_desc
     *
     * @param dniOrId cadena que contiene DNI o ID
     * @return String[] con los datos o null si no existe
     */
    public UsuarioEstadoPorDNIOID obtenerUsuarioYEstadoPorDniOId(String dniOrId) {
        // Listado de usuarios
        UsuarioEstadoPorDNIOID devolver = null;
        try {
            // Consulta SQL dependiendo si es número (id) o texto (dni)
            boolean esNumero = dniOrId != null && dniOrId.matches("^\\d+$");
            final String sql = SQL_USUARIO_Y_ESTADO_BASE + (esNumero ? " WHERE u.id = ?" : " WHERE u.dni = ?");
            try (PreparedStatement ps = conexion.prepareStatement(sql)) {
                // Asignar parámetro
                if (esNumero) {
                    ps.setInt(1, Integer.parseInt(dniOrId));
                } else {
                    ps.setString(1, dniOrId);
                }
                try (ResultSet rs = ps.executeQuery()) {
                    // Ejecutar consulta
                    if (rs.next()) {
                        devolver = new UsuarioEstadoPorDNIOID(
                                rs.getInt("id"),
                                rs.getString("dni"),
                                rs.getString("nombre_completo"),
                                rs.getString("sancion_activa"),
                                TipoUsuario.valueOf(rs.getString("tipo")));
                    }
                }
            }
        } catch (Exception e) {
            // Manejo de excepciones. Se ve en consola
            System.out.println("Error buscando usuario por DNI/ID: " + e.getMessage());
            System.out.println(e.getCause());
            devolver = null;
        }
        return devolver;
    }

    /**
     * Actualiza los datos de un usuario (dni, nombre, apellidos, email, tipo)
     * 
     * @param idUsuario id del usuario a actualizar
     * @param dni
     * @param nombre
     * @param apellido1
     * @param apellido2
     * @param email
     * @param tipo
     * @return true si la actualización fue exitosa
     */
    public boolean actualizarUsuario(int idUsuario, String dni, String nombre, String apellido1, String apellido2,
            String email, String tipo) {
        // Consulta SQL
        final String sql = SQL_UPDATE_USUARIO;
        try (
                PreparedStatement ps = conexion.prepareStatement(sql)) {
            // Asignar parámetros
            ps.setString(1, dni);
            ps.setString(2, nombre);
            ps.setString(3, apellido1);
            ps.setString(4, apellido2);
            ps.setString(5, email);
            ps.setString(6, tipo);
            ps.setInt(7, idUsuario);
            int rows = ps.executeUpdate();
            return rows == 1;
        } catch (Exception e) {
            // Manejo de excepciones. Se ve en consola
            System.out.println("Error actualizando usuario: " + e.getMessage());
            System.out.println(e.getCause());
            return false;
        }
    }

    /**
     * Da de baja (marca estado = false) a un usuario por su id
     * 
     * @param idUsuario id del usuario
     * @return true si la operación fue exitosa
     */
    public boolean bajaUsuario(int idUsuario) {
        // Consulta SQL
        final String sql = SQL_BAJA_USUARIO;
        try (
                PreparedStatement ps = conexion.prepareStatement(sql)) {
            // Asignar parámetro
            ps.setInt(1, idUsuario);
            // Ejecutar actualización
            int rows = ps.executeUpdate();
            return rows == 1;
        } catch (Exception e) {
            // Manejo de excepciones. Se ve en consola
            System.out.println("Error dando de baja usuario: " + e.getMessage());
            System.out.println(e.getCause());
            return false;
        }
    }

    /**
     * Comprueba si el usuario tiene préstamos activos (estado = TRUE)
     * 
     * @param idUsuario id del usuario
     * @return true si tiene préstamos activos
     */
    public boolean tienePrestamosActivosUsuario(int idUsuario) {
        // Consulta SQL
        final String sql = SQL_CUENTA_PRESTAMOS_ACTIVOS_POR_USUARIO;

        try (
                PreparedStatement ps = conexion.prepareStatement(sql)) {
            // Asignar parámetro
            ps.setInt(1, idUsuario);
            try (ResultSet rs = ps.executeQuery()) {
                // Ejecutar consulta
                if (rs.next()) {
                    return rs.getInt("total") > 0;
                }
            }
        } catch (Exception e) {
            // Manejo de excepciones. Se ve en consola
            System.out.println("Error comprobando prestamos activos usuario: " + e.getMessage());
            System.out.println(e.getCause());
        }
        return false;
    }

    /**
     * Obtiene los usuarios sancionables (estudiantes activos)
     * Devuelve filas: id, dni, nombre_completo, tipo
     */
    public String[][] obtenerUsuariosSancionables() {
        // Consulta SQL
        final String sql = SQL_USUARIOS_SANCIONABLES;
        java.util.List<String[]> rows = new java.util.ArrayList<>();
        try (
                PreparedStatement ps = conexion.prepareStatement(sql);
                // Ejecutar consulta
                ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                // Rellenar fila
                String[] fila = new String[4];
                fila[0] = rs.getString("id");
                fila[1] = rs.getString("dni");
                fila[2] = rs.getString("nombre_completo");
                fila[3] = rs.getString("tipo");
                rows.add(fila);
            }
        } catch (Exception e) {
            System.out.println("Error obteniendo usuarios sancionables: " + e.getMessage());
            System.out.println(e.getCause());
        }
        return rows.toArray(new String[0][0]);
    }

    /**
     * Obtiene un UsuarioTipoDTO por id de usuario
     * 
     * @param idUsuario id del usuario
     * @return UsuarioTipoDTO con idUsuario y tipoUsuario, o null si no encontrado
     */
    public UsuarioTipoDTO obtenerUSuarioTipoDTO(int idUsuario) {
        UsuarioTipoDTO usuarioTipoDTO = null;
        try (
                PreparedStatement consulta = conexion.prepareStatement(SQL_OBTENER_USUARIO_TIPO_DTO)) {
            consulta.setInt(1, idUsuario);
            // Ejecutamos la consulta
            try (ResultSet resultado = consulta.executeQuery()) {
                if (resultado.next()) {
                    usuarioTipoDTO = new UsuarioTipoDTO(
                            resultado.getInt("id"),
                            TipoUsuario.valueOf(resultado.getString("tipo")));
                }
            } catch (Exception e) {
                // Manejo de excepciones. Se ve en consola
                System.out.println("Error obteniendo UsuarioTipoDTO: " + e.getMessage());
                System.out.println(e.getCause());
                usuarioTipoDTO = null;
            }
        } catch (Exception e) {
            // Manejo de excepciones. Se ve en consola
            System.out.println("Ha surgido un error inesperado: " + e.getMessage());
            System.out.println(e.getCause());
            usuarioTipoDTO = null;
        }
        return usuarioTipoDTO;
    }
}
