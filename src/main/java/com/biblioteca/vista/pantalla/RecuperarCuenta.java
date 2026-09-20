package com.biblioteca.vista.pantalla;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.net.URL;

import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import com.biblioteca.controlador.Controlador;
import com.biblioteca.modelo.Usuario;
import com.biblioteca.utilities.AppResources;
import com.biblioteca.utilities.Fonts;

/**
 * Clase para la vista Recuperar cuenta
 */
public class RecuperarCuenta {

    /**
     * Controlador de la aplicación
     */
    private final Controlador controlador;

    /**
     * Constructor de la vista RecuperarCuenta
     *
     * @param controlador controlador principal
     */
    public RecuperarCuenta(final Controlador controlador) {
        this.controlador = controlador;
    }

    /**
     * Obtiene el controlador de la aplicación
     *
     * @return Controlador
     */
    public Controlador getControlador() {
        return controlador;
    }

    /**
     * Etiqueta para volver al inicio
     */
    private JLabel volver;

    /**
     * Botón enviar
     */
    private JButton btnEnviar;

    /**
     * Campo de texto para ingresar usuario o correo
     */
    private JTextField campo;
    
    /**
     * Etiqueta de versión
     */
    private JLabel version;

    /**
     * Muestra la pantalla de recuperación de cuenta
     *
     * @return JPanel con el formulario de recuperación
     */
    public JPanel pantalla() {
        // CPD-OFF
        // Panel principal
        JPanel panel = new JPanel();
        panel.setSize(800, 600);
        panel.setLayout(new GridBagLayout());
        panel.setBackground(Color.decode("#EDF3F6"));

        // Tarjeta central
        JPanel tarjeta = new JPanel();
        tarjeta.setPreferredSize(new Dimension(300, 360));
        tarjeta.setBackground(Color.decode("#EDF3F6"));
        tarjeta.setLayout(new BoxLayout(tarjeta, BoxLayout.Y_AXIS));

        // Encabezado con logo
        JPanel encabezado = new JPanel();
        encabezado.setLayout(new BorderLayout());
        encabezado.setPreferredSize(new Dimension(300, 70));
        encabezado.setBackground(Color.decode("#468DAE"));
        URL imgUrl = AppResources.logoPath();
        if (imgUrl != null) {
            ImageIcon icon = new ImageIcon(imgUrl);
            JLabel lblLogo = new JLabel(icon);
            lblLogo.setHorizontalAlignment(JLabel.CENTER);
            lblLogo.setVerticalAlignment(JLabel.CENTER);
            encabezado.add(lblLogo, BorderLayout.CENTER);
        } else {
            JLabel lblAlt = new JLabel("Logo no encontrado");
            lblAlt.setHorizontalAlignment(JLabel.CENTER);
            encabezado.add(lblAlt, BorderLayout.CENTER);
        }
        tarjeta.add(encabezado);

        // Panel blanco con campos
        JPanel login = new JPanel();
        login.setPreferredSize(new Dimension(300, 290));
        login.setBackground(Color.white);
        login.setLayout(null);
        tarjeta.add(login);

        // CPD-ON

        // Etiqueta
        JLabel etiqueta = new JLabel("Correo Electrónico o Usuario");
        etiqueta.setBounds(30, 30, 240, 20);
        // Asegurar estilo de fuente normal (no negrita)
        etiqueta.setFont(etiqueta.getFont().deriveFont(Font.PLAIN));
        login.add(etiqueta);

        // Campo de entrada
        campo = new JTextField();
        campo.setBounds(30, 70, 240, 35);
        login.add(campo);

        // Botón ENVIAR
        btnEnviar = new JButton("ENVIAR");
        btnEnviar.setBounds(30, 120, 240, 40);
        btnEnviar.setBorder(null);
        btnEnviar.setBackground(Color.decode("#F4791B"));
        btnEnviar.setForeground(Color.white);
        login.add(btnEnviar);

        // Texto con enlace a volver al inicio
        volver = new JLabel(
                "<html>¿Recordaste tu contraseña? <span style='color:#468DAE; font-weight:bold'>Volver al inicio</span></html>");
        volver.setBounds(30, 170, 240, 22);
        // Usar Open Sans en estilo normal
        volver.setFont(Fonts.openSans(11f));

        login.add(volver);

        // Versión (bajada un poco para evitar solapamiento)
        version = new JLabel("Sistema de Gestión Académica v1.0.1");
        version.setBounds(40, 195, 240, 20);
        version.setFont(version.getFont().deriveFont(Font.PLAIN));
        login.add(version);

        panel.add(tarjeta);

        configurarEventos();
        return panel;
    }

    private void configurarEventos() {
        // Cambiar cursor y manejar click
        volver.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(final MouseEvent e) {
                controlador.getControladorNavegacion().cambiarPantallaPadre("inicioSesion");
            }

            @Override
            public void mouseEntered(final MouseEvent e) {
                volver.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            }

            @Override
            public void mouseExited(final MouseEvent e) {
                volver.setCursor(Cursor.getDefaultCursor());
            }
        });
        // Eventos (sin lógica de backend)
        btnEnviar.addActionListener(e -> {
            // Logica de backend para iniciar sesion
            // Se comprueban el campo de formulario
            if (!campo.getText().trim().isEmpty()) {
                // Invocamos al usuario desde el controlador
                Usuario usuario = controlador.getControladorRecuperarCuenta().recuperarCuenta(campo.getText().trim());
                // Comprobar si el usuario es null (credenciales incorrectas)
                if (controlador.getControladorLogin().usuarioNoValido(usuario)) {
                    JOptionPane.showMessageDialog(version, "Ingrese un usuario o correo electrónico válido.",
                            "Error de inicio de sesión", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                // Si da true, la cuenta está desactivada. Mantener signo de exclamación
                if (controlador.getControladorLogin().cuentaDesactivada(usuario)) {
                    JOptionPane.showMessageDialog(version, "La cuenta está desactivada. Contacte con el administrador.",
                            "Error de inicio de sesión", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                // Solo los conserjes pueden recuperar la contraseña
                if (controlador.getControladorLogin().usuarioNoConserje(usuario)) {
                    JOptionPane.showMessageDialog(version, "Actualmente esto no le afecta.",
                            "Error de inicio de sesión", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                // Simular envío de correo y volver al inicio de sesión
                // Aquí se agregaría la lógica real de envío de correo electrónico (en este caso
                // queda como placeholder)
                JOptionPane.showMessageDialog(version,
                        "Debería haber recibido un correo electronico con su contraseña.",
                        "Recuperar cuenta", JOptionPane.INFORMATION_MESSAGE);
                controlador.getControladorNavegacion().cambiarPantallaPadre("inicioSesion");
            } else {
                JOptionPane.showMessageDialog(version, "Por favor, ingrese un usuario o correo electrónico.",
                        "Error de inicio de sesión", JOptionPane.ERROR_MESSAGE);
            }

        });

        volver.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(final MouseEvent e) {
                controlador.getControladorNavegacion().cambiarPantallaPadre("inicioSesion");
            }

            @Override
            public void mouseEntered(final MouseEvent e) {
                volver.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            }

            @Override
            public void mouseExited(final MouseEvent e) {
                volver.setCursor(Cursor.getDefaultCursor());
            }
        });
    }

}
