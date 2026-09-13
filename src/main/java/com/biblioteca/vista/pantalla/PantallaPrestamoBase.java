package com.biblioteca.vista.pantalla;

import java.awt.Color;
import java.awt.Dimension;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import com.biblioteca.controlador.Controlador;
import com.biblioteca.dto.UsuarioEstadoPorDNIOID;

/**
 * Clase base para las vistas de la aplicación
 */
public abstract class PantallaPrestamoBase {
    /**
     * Jlabel para el título de la vista
     */
    private final JLabel titulo = new JLabel();

    /**
     * JButton para el botón de formulario de devolución
     */
    private final JButton btnCambiarFormulario = new JButton();

    /** identificador del usuario seleccionado */
    private int[] usuarioSeleccionado = new int[] { -1 };

    /**
     * Controlador de la aplicación
     */
    private final Controlador controlador;

    /**
     * Constructor de la clase base PantallaPrestamoBase
     * 
     * @param controlador controlador principal
     * @param tituloTexto string para el título de la vista
     * @param botonTexto  string para el texto del botón de formulario
     */
    public PantallaPrestamoBase(final Controlador controlador, final String tituloTexto, final String botonTexto) {
        this.controlador = controlador;
        this.titulo.setText(tituloTexto);
        this.btnCambiarFormulario.setText(botonTexto);
    }

    /**
     * Getter para el usuario seleccionado
     * 
     * @return array con el ID del usuario seleccionado
     */
    public int[] getUsuarioSeleccionado() {
        return new int[] { usuarioSeleccionado[0] };
    }

    /**
     * Setter para el usuario seleccionado
     * 
     * @param usuarioSeleccionado establece el usuario seleccionado
     */
    public void setUsuarioSeleccionado(final int[] usuarioSeleccionado) {
        this.usuarioSeleccionado[0] = usuarioSeleccionado[0];
    }

    /**
     * Getter para el botón de cambiar formulario
     * 
     * @return JButton para cambiar formulario
     */
    public JButton getBtnCambiarFormulario() {
        return btnCambiarFormulario;
    }

    /**
     * Muestra la pantalla para gestionar devoluciones de préstamo
     *
     * @return JPanel con la vista de devolución de préstamos
     */
    public JPanel pantalla() {
        JPanel panel = new JPanel();
        panel.setPreferredSize(new Dimension(600, 600));
        panel.setBackground(Color.decode("#EDF3F6"));
        panel.setLayout(null);

        JPanel encabezado = crearEncabezado();
        JPanel contenido = crearContenido();

        panel.add(encabezado);
        panel.add(contenido);
        return panel;
    }

    /**
     * Crea el panel de encabezado con título
     *
     * @return JPanel con el encabezado
     */
    private JPanel crearEncabezado() {
        JPanel encabezado = new JPanel();
        encabezado.setSize(new Dimension(600, 60));
        encabezado.setBackground(Color.white);
        encabezado.setLayout(null);
        encabezado.setBounds(0, 0, 600, 60);

        // Titulo
        titulo.setFont(titulo.getFont().deriveFont(24f));
        titulo.setBounds(30, 10, 300, 40);
        encabezado.add(titulo);

        // Boton nueva publicacion
        btnCambiarFormulario.setBounds(430, 15, 150, 30);
        btnCambiarFormulario.setBackground(Color.white);
        btnCambiarFormulario.setForeground(Color.decode("#468DAE"));
        btnCambiarFormulario.setFocusPainted(false);
        btnCambiarFormulario.setBorder(null);
        encabezado.add(btnCambiarFormulario);

        return encabezado;
    }

    /**
     * Crea el panel de contenido
     *
     * @return el panel de contenido
     */
    private JPanel crearContenido() {
        JPanel contenido = new JPanel();
        contenido.setSize(540, 480);
        contenido.setLayout(null);
        contenido.setBackground(Color.white);
        contenido.setBounds(30, 80, 540, 480);

        JTextField txtIdEjemplar = new JTextField();
        txtIdEjemplar.setBounds(20, 280, 200, 35);
        contenido.add(txtIdEjemplar);

        configurarPasoSocio(contenido);
        configurarPasoEjemplar(contenido, txtIdEjemplar);
        configurarBotonesAccion(contenido, txtIdEjemplar);

        return contenido;
    }

    /**
     * Configura el paso 1: Identificar socio
     *
     * @param contenido el panel de contenido
     */
    private void configurarPasoSocio(final JPanel contenido) {
        JLabel paso1 = new JLabel("1. Identificar Socio (Usuario)");
        paso1.setBounds(20, 20, 350, 30);
        paso1.setForeground(Color.decode("#468DAE"));
        paso1.setFont(paso1.getFont().deriveFont(16f));
        contenido.add(paso1);

        JLabel dniID = new JLabel("DNI / ID:");
        dniID.setBounds(20, 70, 100, 25);
        contenido.add(dniID);

        JTextField txtDniID = new JTextField();
        txtDniID.setBounds(20, 100, 300, 35);
        contenido.add(txtDniID);

        JButton btnBuscarSocio = new JButton("Buscar");
        btnBuscarSocio.setBounds(330, 100, 90, 35);
        btnBuscarSocio.setBackground(Color.decode("#468DAE"));
        btnBuscarSocio.setForeground(Color.WHITE);
        btnBuscarSocio.setFocusPainted(false);
        btnBuscarSocio.setBorder(null);
        contenido.add(btnBuscarSocio);

        JLabel resultadoSocio = new JLabel("");
        resultadoSocio.setBounds(20, 150, 480, 40);
        resultadoSocio.setText(
                "<html>Usuario: (Estudiante) - <span style='color:#2BC187; font-weight:bold'>Sin Sanciones</span></html>");
        resultadoSocio.setBackground(Color.decode("#EDF3F6"));
        resultadoSocio.setOpaque(true);
        resultadoSocio.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 0));
        contenido.add(resultadoSocio);

        btnBuscarSocio.addActionListener(e -> {
            String input = txtDniID.getText().trim();
            if (input.isEmpty()) {
                resultadoSocio.setText("<html><span style='color:#F4791B'>Ingrese DNI o ID</span></html>");
                usuarioSeleccionado[0] = -1;
                return;
            }
            UsuarioEstadoPorDNIOID usuario = controlador.getControladorDevolverPrestamo()
                    .buscarUsuarioPorDniOId(input);
            if (usuario == null) {
                resultadoSocio.setText("<html><span style='color:#F4791B'>Usuario no encontrado</span></html>");
                usuarioSeleccionado[0] = -1;
                return;
            }
            usuarioSeleccionado[0] = usuario.getId();
            String estado = usuario.getSancionactiva();
            String tipoDesc = usuario.getTipoUsuario().getDescripcion();
            if ("SANCIONADO".equalsIgnoreCase(estado)) {
                resultadoSocio.setText("<html>Usuario: " + usuario.getNombreCompleto() + " (" + tipoDesc
                        + ") - <span style='color:#F4791B; font-weight:bold'>Tiene sanciones</span></html>");
            } else if ("BAJA".equalsIgnoreCase(estado)) {
                resultadoSocio.setText("<html>Usuario: " + usuario.getNombreCompleto()
                        + " - <span style='color:#F4791B; font-weight:bold'>Dado de baja</span></html>");
            } else {
                resultadoSocio.setText("<html>Usuario: " + usuario.getNombreCompleto() + " (" + tipoDesc
                        + ") - <span style='color:#2BC187; font-weight:bold'>Sin sanciones</span></html>");
            }
        });
    }

    protected abstract void configurarPasoEjemplar(JPanel contenido, JTextField txtIdEjemplar);

    protected abstract void configurarBotonesAccion(JPanel contenido, JTextField txtIdEjemplar);

    /**
     * Valida usuario y ejemplar
     * 
     * @param usuarioSeleccionado array holder con el ID de usuario seleccionado
     * @param txtIdEjemplar       JTextField con el ID del ejemplar
     * @return true si validación exitosa, false si hay error
     */
    protected boolean validarUsuarioYEjemplar(final int[] usuarioSeleccionado, final JTextField txtIdEjemplar) {
        // Validar usuario seleccionado
        if (usuarioSeleccionado[0] == -1) {
            JOptionPane.showMessageDialog(null, "Seleccione primero un usuario válido", "Error",
                    JOptionPane.ERROR_MESSAGE);
            return false;
        }
        // Validar ID de ejemplar
        String idEjStr = txtIdEjemplar.getText().trim();
        if (idEjStr.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Introduzca el ID del ejemplar", "Error",
                    JOptionPane.ERROR_MESSAGE);
            return false;
        }
        // Validar que sea entero

        try {
            Integer.parseInt(idEjStr);
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(null, "ID de ejemplar inválido", "Error",
                    JOptionPane.ERROR_MESSAGE);
            return false;
        }
        return true;
    }
}
