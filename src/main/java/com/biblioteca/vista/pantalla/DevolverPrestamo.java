package com.biblioteca.vista.pantalla;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.event.DocumentListener;

import com.biblioteca.controlador.Controlador;
import com.biblioteca.dto.EjemplarConTituloDTO;
import com.biblioteca.dto.UsuarioEstadoPorDNIOID;

import javax.swing.event.DocumentEvent;
import javax.swing.BorderFactory;

/**
 * Clase para la vista Devolver préstamo
 */
public class DevolverPrestamo {

    /**
     * Controlador de la aplicación
     */
    private final Controlador controlador;

    /**
     * Instancia de la clase base para validaciones y utilidades
     */
    private final VistaBase vistaBase;

    /**
     * Constructor de la vista DevolverPrestamo
     *
     * @param controlador controlador principal
     */
    public DevolverPrestamo(final Controlador controlador) {
        this.controlador = controlador;
        this.vistaBase = new VistaBase();
    }

    /**
     * Almacena el ID del usuario seleccionado.
     */
    private int[] usuarioSeleccionado = new int[] { -1 };

    /**
     * Getter para el controlador
     *
     * @return el controlador de la aplicación
     */
    public Controlador getControlador() {
        return controlador;
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
     * Crea el panel de encabezado
     *
     * @return el panel de encabezado
     */
    private JPanel crearEncabezado() {
        JPanel encabezado = new JPanel();
        encabezado.setSize(new Dimension(600, 60));
        encabezado.setBackground(Color.white);
        encabezado.setLayout(null);
        encabezado.setBounds(0, 0, 600, 60);

        JLabel titulo = new JLabel("Devolución Préstamo");
        titulo.setFont(titulo.getFont().deriveFont(24f));
        titulo.setBounds(30, 10, 300, 40);
        encabezado.add(titulo);

        JButton btnFormularioRegistrar = new JButton("Formulario registrar prestamo");
        btnFormularioRegistrar.setBounds(360, 15, 220, 30);
        btnFormularioRegistrar.setBackground(Color.white);
        btnFormularioRegistrar.setForeground(Color.decode("#468DAE"));
        btnFormularioRegistrar.setFocusPainted(false);
        btnFormularioRegistrar.setBorder(null);
        encabezado.add(btnFormularioRegistrar);

        // Eventos botones
        btnFormularioRegistrar.addActionListener(e -> {
            controlador.getControladorNavegacion().cambiarPantallaHijo("concederPrestamo");
        });

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
            UsuarioEstadoPorDNIOID usuario = getControlador().getControladorDevolverPrestamo()
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

    /**
     * Configura el paso 2: Identificar ejemplar
     *
     * @param contenido     el panel de contenido
     * @param txtIdEjemplar campo de texto para el ID del ejemplar
     */
    private void configurarPasoEjemplar(final JPanel contenido, final JTextField txtIdEjemplar) {
        JLabel paso2 = new JLabel("2. Identificar Ejemplar");
        paso2.setBounds(20, 200, 300, 30);
        paso2.setForeground(Color.decode("#468DAE"));
        paso2.setFont(paso2.getFont().deriveFont(16f));
        contenido.add(paso2);

        JLabel idEjemplar = new JLabel("ID Ejemplar:");
        idEjemplar.setBounds(20, 250, 100, 25);
        contenido.add(idEjemplar);

        JLabel publicacion = new JLabel("Publicación");
        publicacion.setBounds(230, 250, 300, 35);
        contenido.add(publicacion);

        JLabel txtPublicacion = new JLabel();
        txtPublicacion.setBounds(230, 280, 260, 35);
        txtPublicacion.setBorder(BorderFactory.createMatteBorder(0, 5, 0, 0, Color.white));
        contenido.add(txtPublicacion);

        configurarDetectorEjemplar(txtIdEjemplar, txtPublicacion);
    }

    /**
     * Configura el detector de ejemplar mediante DocumentListener
     *
     * @param txtIdEjemplar  campo de texto para el ID del ejemplar
     * @param txtPublicacion etiqueta para mostrar la publicación detectada
     */
    private void configurarDetectorEjemplar(final JTextField txtIdEjemplar, final JLabel txtPublicacion) {
        txtIdEjemplar.getDocument().addDocumentListener(new DocumentListener() {
            private void doDetect() {
                String idEjStr = txtIdEjemplar.getText().trim();
                if (idEjStr.isEmpty()) {
                    txtPublicacion.setText("");
                    return;
                }
                int idEj;
                try {
                    idEj = Integer.parseInt(idEjStr);
                } catch (NumberFormatException ex) {
                    txtPublicacion.setText("");
                    return;
                }
                EjemplarConTituloDTO detectado = getControlador().getControladorDevolverPrestamo()
                        .detectarEjemplar(idEj);
                if (detectado == null) {
                    txtPublicacion.setText("");
                    return;
                }
                String pubTitulo = detectado.getTitulo();
                String numEd = String.valueOf(detectado.getNumEdicion());
                String tipoPub = detectado.getTipoPublicacion().toString();
                if ("L".equalsIgnoreCase(tipoPub)) {
                    String texto = "Detectado: " + pubTitulo
                            + (numEd != null && !numEd.isEmpty() ? " (Ed. " + numEd + ")" : "");
                    txtPublicacion.setText(texto);
                } else if ("R".equalsIgnoreCase(tipoPub)) {
                    String texto = "Detectado: " + pubTitulo + " (Revista)";
                    txtPublicacion.setText(texto);
                } else {
                    txtPublicacion.setText("Detectado: " + pubTitulo);
                }
            }

            @Override
            public void insertUpdate(final DocumentEvent e) {
                doDetect();
            }

            @Override
            public void removeUpdate(final DocumentEvent e) {
                doDetect();
            }

            @Override
            public void changedUpdate(final DocumentEvent e) {
                doDetect();
            }
        });
    }

    /**
     * Configura los botones de acción
     *
     * @param contenido           el panel de contenido
     * @param txtIdEjemplar       campo de texto para el ID del ejemplar
     */
    private void configurarBotonesAccion(final JPanel contenido, final JTextField txtIdEjemplar) {
        JButton btnCancelar = new JButton("Cancelar");
        btnCancelar.setBounds(250, 420, 100, 35);
        btnCancelar.setBackground(Color.white);
        btnCancelar.setForeground(Color.BLACK);
        btnCancelar.setFocusPainted(false);
        btnCancelar.setBorder(null);
        contenido.add(btnCancelar);

        JButton btnDevolverPrestamo = new JButton("DEVOLVER PRESTAMO");
        btnDevolverPrestamo.setBounds(370, 420, 120, 35);
        btnDevolverPrestamo.setBackground(Color.decode("#F4791B"));
        btnDevolverPrestamo.setForeground(Color.WHITE);
        btnDevolverPrestamo.setFocusPainted(false);
        btnDevolverPrestamo.setBorder(null);
        btnDevolverPrestamo.setFont(new Font("Open Sans", Font.PLAIN, 11));
        contenido.add(btnDevolverPrestamo);

        btnCancelar.addActionListener(e -> {
            getControlador().getControladorNavegacion().cambiarPantallaHijo("panelControl");
        });

        btnDevolverPrestamo.addActionListener(e -> {
            if (!vistaBase.validarUsuarioYEjemplar(usuarioSeleccionado, txtIdEjemplar)) {
                return;
            }
            String err = getControlador().getControladorDevolverPrestamo()
                    .devolverPrestamo(usuarioSeleccionado[0], Integer.parseInt(txtIdEjemplar.getText()));
            if (err == null) {
                JOptionPane.showMessageDialog(null, "Devolución registrada correctamente", "Éxito",
                        JOptionPane.INFORMATION_MESSAGE);
                String notif = getControlador().getControladorDevolverPrestamo()
                        .obtenerYLimpiarUltimaNotificacionSancion();
                if (notif != null) {
                    JOptionPane.showMessageDialog(null, notif, "Información", JOptionPane.INFORMATION_MESSAGE);
                }
                getControlador().getControladorNavegacion().refrescarPublicaciones();
                getControlador().getControladorNavegacion().refrescarPanelControl();
                getControlador().getControladorNavegacion().marcarPantallaActiva("panelControl");
                getControlador().getControladorNavegacion().cambiarPantallaHijo("panelControl");
            } else {
                JOptionPane.showMessageDialog(null, err, "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
    }

}
