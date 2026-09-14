package com.biblioteca.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.biblioteca.dto.UsuarioEstadoPorDNIOID;
import com.biblioteca.dto.UsuarioTipoDTO;
import com.biblioteca.modelo.TipoUsuario;
import com.biblioteca.modelo.Usuario;

/**
 * DAO para la tabla 'usuarios' contiene operaciones CRUD asociadas a los
 * usuarios de la biblioteca. Esta clase es responsable de interactuar con la
 * base de datos para realizar consultas, inserciones, actualizaciones y
 * eliminaciones de registros de usuarios.
 */
public class UsuarioDAO {
    /**
     * Conexión a la base de datos utilizada para las operaciones de acceso a datos
     * relacionadas con los usuarios. Esta conexión se inyecta en el constructor y
     * se utiliza en todos los métodos de la clase para ejecutar consultas SQL.
     */
    private final Connection conexion;

    /**
     * Constante SQL para consultar el inicio de sesión de un usuario.
     * Busca un usuario por su nombre de usuario.
     */
    private static final String SQL_CONSULTA_INICIO_SESION = "SELECT * FROM usuarios WHERE usuario = ?";
    /**
     * Constante SQL para consultar la recuperación de cuenta de un usuario.
     * Busca un usuario por su nombre de usuario o correo electrónico.
     */
    private static final String SQL_CONSULTA_RECUPERAR_CUENTA = "SELECT * FROM usuarios WHERE usuario = ? OR email = ?";
    /**
     * Constante SQL para contar el total de socios activos (estado = TRUE).
     * Devuelve el número total de usuarios activos en la base de datos.
     */
    private static final String SQL_TOTAL_SOCIOS_ACTIVOS = "SELECT COUNT(*) AS TOTAL FROM usuarios where estado = TRUE";
    /**
     * Constante SQL para listar usuarios con su estado de sanción.
     * Devuelve id, dni, nombre completo, estado de sanción (SANCIONADO/ACTIVO/BAJA)
     * y tipo de usuario.
     */
    private static final String SQL_LISTA_USUARIOS_ESTADO = "SELECT u.id, u.dni, CONCAT(u.nombre, ' ', u.apellido1, ' ', u.apellido2) AS nombre_completo, "
            + "CASE WHEN u.estado = FALSE THEN 'BAJA' "
            + "WHEN EXISTS (SELECT 1 FROM sanciones s WHERE s.id_usuario = u.id AND s.estado = TRUE) "
            + "THEN 'SANCIONADO' ELSE 'ACTIVO' END AS sancion_activa, "
            + "u.tipo "
            + "FROM usuarios u ORDER BY u.nombre ASC, u.apellido1 ASC, u.apellido2 ASC";
    /**
     * Constante SQL para insertar un nuevo usuario en la base de datos.
     * Los parámetros incluyen dni, nombre, apellidos, email, contraseña, tipo de
     * usuario, estado y nombre de usuario.
     */
    private static final String SQL_INSERT_USUARIO = "INSERT INTO usuarios (dni, nombre, apellido1, apellido2, email, contrasena, tipo, estado, usuario) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
    /**
     * Constante SQL para obtener los detalles de un usuario por su id.
     * Devuelve dni, nombre, apellidos, email, tipo y estado del usuario.
     */
    private static final String SQL_SELECT_USUARIO_POR_ID = "SELECT id, dni, nombre, apellido1, apellido2, email, tipo, estado FROM usuarios WHERE id = ?";
    /**
     * Constante SQL base para obtener id, dni, nombre completo, estado de sanción y
     * tipo de usuario.
     * Esta consulta se utiliza para obtener información de un usuario por su dni o
     * id.
     */
    private static final String SQL_USUARIO_Y_ESTADO_BASE = "SELECT u.id, u.dni, CONCAT(u.nombre, ' ', u.apellido1, ' ', u.apellido2) AS nombre_completo, "
            + "CASE WHEN u.estado = FALSE THEN 'BAJA' "
            + "WHEN EXISTS (SELECT 1 FROM sanciones s WHERE s.id_usuario = u.id AND s.estado = TRUE) "
            + "THEN 'SANCIONADO' ELSE 'ACTIVO' END AS sancion_activa, "
            + "u.tipo "
            + "FROM usuarios u";
    /**
     * Constante SQL para actualizar los datos de un usuario.
     * Actualiza dni, nombre, apellidos, email y tipo de usuario por su id.
     */
    private static final String SQL_UPDATE_USUARIO = "UPDATE usuarios SET dni = ?, nombre = ?, apellido1 = ?, apellido2 = ?, email = ?, tipo = ? WHERE id = ?";
    /**
     * Constante SQL para dar de baja a un usuario (estado = FALSE).
     * Marca un usuario como inactivo en la base de datos por su id.
     */
    private static final String SQL_BAJA_USUARIO = "UPDATE usuarios SET estado = FALSE WHERE id = ?";
    /**
     * Constante SQL para contar los préstamos activos de un usuario.
     * Devuelve el número de préstamos activos (estado = TRUE) asociados a un
     * usuario por su id.
     */
    private static final String SQL_CUENTA_PRESTAMOS_ACTIVOS_POR_USUARIO = "SELECT COUNT(*) AS total FROM prestamos WHERE id_usuario = ? AND estado = TRUE";
    /**
     * Constante SQL para obtener usuarios sancionables (estudiantes activos).
     * Devuelve id, dni, nombre completo y tipo de usuario para aquellos usuarios
     * que son estudiantes y están activos.
     */
    private static final String SQL_USUARIOS_SANCIONABLES = "SELECT u.id, u.dni, CONCAT(u.apellido1, ', ', u.nombre) AS nombre_completo, u.tipo "
            + "FROM usuarios u WHERE u.tipo = 'E' AND u.estado = TRUE ORDER BY u.apellido1, u.nombre";
    /**
     * Constante SQL para obtener el tipo de usuario como DTO.
     * Recupera el id y el tipo de usuario.
     */
    private static final String SQL_OBTENER_USUARIO_TIPO_DTO = "SELECT id, tipo FROM usuarios WHERE id = ?";
    /**
     * Constante SQL para consultar el número de usuarios por nombre de usuario.
     * Devuelve el conteo de usuarios cuyo nombre de usuario coincide con el patrón
     * proporcionado.
     */
    private static final String SQL_CONSULTA_NUMERO_USUARIOS_POR_USUARIO = "SELECT COUNT(*) AS total FROM usuarios WHERE usuario LIKE ?";

    /**
     * Constructor del DAO de usuarios.
     * 
     * @param conexion conexión a la base de datos (no puede ser null)
     */
    public UsuarioDAO(final Connection conexion) {
        if (conexion == null) {
            throw new IllegalArgumentException("Connection cannot be null");
        }
        this.conexion = conexion;
    }

    /**
     * Consulta las credenciales de inicio de sesión de un usuario.
     * Valida el nombre de usuario y la contraseña y devuelve el objeto de usuario
     * si
     * las credenciales son correctas.
     * 
     * @param usuario el nombre de usuario a autenticar
     * @return el objeto Usuario si las credenciales son correctas, null en caso
     *         contrario
     */
    public Usuario consultaInicioSesion(final String usuario) {
        Usuario devolver = null;
        try (
                PreparedStatement ps = conexion.prepareStatement(SQL_CONSULTA_INICIO_SESION)) {
            // establecer parámetros
            ps.setString(1, usuario);
            // ejecutar consulta
            // CPD-OFF
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
            } catch (SQLException e) {
                devolver = null;
                System.out.println(e.getMessage());
                System.out.println(e.getCause());
            }
        } catch (SQLException e) {
            devolver = null;
            System.out.println(e.getMessage());
            System.out.println(e.getCause());
        }
        return devolver;
        // CPD-ON
    }

    /**
     * Consulta la recuperación de cuenta de un usuario por nombre de usuario o
     * correo electrónico.
     * 
     * @param trim el nombre de usuario o correo electrónico a buscar
     * @return el objeto Usuario si se encuentra, null en caso contrario
     */
    public Usuario consultaRecuperarCuenta(final String trim) {
        Usuario devolver = null;
        try (
                PreparedStatement ps = conexion.prepareStatement(SQL_CONSULTA_RECUPERAR_CUENTA)) {
            // establecer parámetros
            ps.setString(1, trim);
            ps.setString(2, trim);
            // ejecutar consulta
            // CPD-OFF
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
            } catch (SQLException e) {
                devolver = null;
                System.out.println(e.getMessage());
                System.out.println(e.getCause());
            }
        } catch (SQLException e) {
            devolver = null;
            System.out.println(e.getMessage());
            System.out.println(e.getCause());
        }
        return devolver;
        //CPD-ON
    }

    /**
     * Obtiene el total de socios activos (estado = TRUE) en la base de datos.
     * 
     * @return el número total de socios activos como String, o "-1" en caso de
     *         error
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

        } catch (SQLException e) {
            // Manejo de excepciones. Se ve en consola
            System.out.println(e.getMessage());
            System.out.println(e.getCause());
            totalSocios = "-1";
        }
        return totalSocios;
    }

    /**
     * Obtiene un listado de usuario con una sancion activa.
     * 
     * @return un arreglo de arreglos de String con los datos de los usuarios y su
     *         estado de sanción, o un arreglo vacío si ocurre un error
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
        } catch (SQLException e) {
            // Manejo de excepciones. Se ve en consola
            devolver = new String[0][0];
            System.out.println(e.getMessage());
            System.out.println(e.getCause());
        }
        return devolver;
    }

    /**
     * Inserta un usuario en la base de datos utilizando una conexión existente.
     *
     * @param conexion   la conexión a la base de datos
     * @param dni        dni del usuario
     * @param nombre     nombre del usuario
     * @param apellido1  primer apellido del usuario
     * @param apellido2  segundo apellido del usuario
     * @param email      email del usuario
     * @param contrasena contraseña del usuario
     * @param tipo       tipo de usuario (E,P,A,C,L)
     * @param estado     estado del usuario (true = activo, false = inactivo)
     * @param usuario    nombre de usuario (login)
     * @return true si la inserción fue exitosa, false en caso contrario
     */
    public boolean insertarUsuario(final Connection conexion, final String dni, final String nombre,
            final String apellido1, final String apellido2,
            final String email, final String contrasena, final String tipo, final boolean estado,
            final String usuario) {
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
            ps.setString(9, usuario);
            // Ejecutar inserción
            int rows = ps.executeUpdate();
            return rows == 1;
        } catch (SQLException e) {
            // Manejo de excepciones. Se ve en consola
            System.out.println("Error insertando usuario: " + e.getMessage());
            System.out.println(e.getCause());
            return false;
        }
    }

    /**
     * Obtiene los detalles de un usuario por su id.
     * 
     * @param idUsuario el id del usuario a consultar
     * @return un arreglo de String con los detalles del usuario (dni, nombre,
     *         apellido1, apellido2, email, tipo, id) o null si no se encuentra
     */
    public String[] obtenerDetallesUsuario(final int idUsuario) {
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
        } catch (SQLException e) {
            // Manejo de excepciones. Se ve en consola
            System.out.println("Error obteniendo detalles de usuario: " + e.getMessage());
            System.out.println(e.getCause());
            devolver = null;
        }
        return devolver;
    }

    /**
     * Obtiene un usuario y su estado de sanción por dni o id.
     * 
     * @param dniOrId el dni o id del usuario a consultar
     * @return un objeto UsuarioEstadoPorDNIOID con los detalles del usuario y su
     *         estado de sanción, o null si no se encuentra
     */
    public UsuarioEstadoPorDNIOID obtenerUsuarioYEstadoPorDniOId(final String dniOrId) {
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
        } catch (NumberFormatException | SQLException e) {
            // Manejo de excepciones. Se ve en consola
            System.out.println("Error buscando usuario por DNI/ID: " + e.getMessage());
            System.out.println(e.getCause());
            devolver = null;
        }
        return devolver;
    }

    /**
     * Actualiza los datos de un usuario en la base de datos.
     * 
     * @param idUsuario el id del usuario a actualizar
     * @param dni       el nuevo dni del usuario
     * @param nombre    el nuevo nombre del usuario
     * @param apellido1 el nuevo primer apellido del usuario
     * @param apellido2 el nuevo segundo apellido del usuario
     * @param email     el nuevo email del usuario
     * @param tipo      el nuevo tipo de usuario (E,P,A,C,L)
     * @return true si la actualización fue exitosa, false en caso contrario
     */
    public boolean actualizarUsuario(final int idUsuario, final String dni, final String nombre, final String apellido1,
            final String apellido2,
            final String email, final String tipo) {
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
        } catch (SQLException e) {
            // Manejo de excepciones. Se ve en consola
            System.out.println("Error actualizando usuario: " + e.getMessage());
            System.out.println(e.getCause());
            return false;
        }
    }

    /**
     * Marca un usuario como inactivo (estado = FALSE) en la base de datos.
     * 
     * @param idUsuario el id del usuario a dar de baja
     * @return true si la operación fue exitosa, false en caso contrario
     */
    public boolean bajaUsuario(final int idUsuario) {
        // Consulta SQL
        final String sql = SQL_BAJA_USUARIO;
        try (
                PreparedStatement ps = conexion.prepareStatement(sql)) {
            // Asignar parámetro
            ps.setInt(1, idUsuario);
            // Ejecutar actualización
            int rows = ps.executeUpdate();
            return rows == 1;
        } catch (SQLException e) {
            // Manejo de excepciones. Se ve en consola
            System.out.println("Error dando de baja usuario: " + e.getMessage());
            System.out.println(e.getCause());
            return false;
        }
    }

    /**
     * Comprueba si un usuario tiene préstamos activos en la base de datos.
     * 
     * @param idUsuario el id del usuario a consultar
     * @return true si el usuario tiene préstamos activos, false en caso contrario
     */
    public boolean tienePrestamosActivosUsuario(final int idUsuario) {
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
        } catch (SQLException e) {
            // Manejo de excepciones. Se ve en consola
            System.out.println("Error comprobando prestamos activos usuario: " + e.getMessage());
            System.out.println(e.getCause());
        }
        return false;
    }

    /**
     * Obtiene un listado de usuarios sancionables (estudiantes activos).
     * 
     * @return un arreglo de arreglos de String con los datos de los usuarios
     *         sancionables, o un arreglo vacío si ocurre un error
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
        } catch (SQLException e) {
            System.out.println("Error obteniendo usuarios sancionables: " + e.getMessage());
            System.out.println(e.getCause());
        }
        return rows.toArray(new String[0][0]);
    }

    /**
     * Obtiene un UsuarioTipoDTO por id de usuario.
     * 
     * @param idUsuario el id del usuario
     * @return un UsuarioTipoDTO con idUsuario y tipoUsuario, o null si no se
     *         encuentra
     */
    public UsuarioTipoDTO obtenerUSuarioTipoDTO(final int idUsuario) {
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
            } catch (SQLException e) {
                // Manejo de excepciones. Se ve en consola
                System.out.println("Error obteniendo UsuarioTipoDTO: " + e.getMessage());
                System.out.println(e.getCause());
                usuarioTipoDTO = null;
            }
        } catch (SQLException e) {
            // Manejo de excepciones. Se ve en consola
            System.out.println("Ha surgido un error inesperado: " + e.getMessage());
            System.out.println(e.getCause());
            usuarioTipoDTO = null;
        }
        return usuarioTipoDTO;
    }

    /**
     * Consulta el número de usuarios cuyo nombre de usuario coincide con un patrón.
     * 
     * @param usuarioConsultar el patrón de nombre de usuario a buscar
     * @return el número de usuarios que coinciden con el patrón, o -1 en caso de
     *         error
     */
    public int consultaNumeroUsuariosPorUsuario(final String usuarioConsultar) {
        try (PreparedStatement ps = conexion.prepareStatement(SQL_CONSULTA_NUMERO_USUARIOS_POR_USUARIO)) {
            ps.setString(1, usuarioConsultar + "%"); // Usamos LIKE para buscar coincidencias
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("total");
                }
            }
        } catch (SQLException e) {
            System.out.println("Error consultando número de usuarios por usuario: " + e.getMessage());
            System.out.println(e.getCause());
        }
        return -1; // Retorna -1 en caso de error
    }
}
