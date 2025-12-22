package com.example.vista;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagLayout;
import java.net.URL;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import com.example.controlador.Controlador;

/**
 * Clase para la vista de inicio de sesión
 */
public class InicioSesion {
    /**
     * Controlador de la aplicación
     */
    Controlador controlador;

    /**
     * Constructor de la clase InicioSesion
     * 
     * @param controlador
     */
    public InicioSesion(Controlador controlador) {
        this.controlador = controlador;
    }

    /**
     * Muestra la pantalla de inicio de sesión
     * 
     * @return JPanel con la pantalla de inicio de sesión
     */
    public JPanel pantalla() {
        // Crear el panel principal
        JPanel panel = new JPanel();
        panel.setSize(800, 600);
        panel.setLayout(new GridBagLayout());
        panel.setBackground(Color.decode("#EDF3F6"));

        // La tarteta de inicio de sesión
        JPanel tarjeta = new JPanel();
        tarjeta.setPreferredSize(new Dimension(300, 360));
        tarjeta.setBackground(Color.decode("#EDF3F6"));
        // Para que los componentes se apilen verticalmente
        tarjeta.setLayout(new BoxLayout(tarjeta, BoxLayout.Y_AXIS));

        // Encabezado de la tarjeta (logo centrado 1:1)
        JPanel encabezado = new JPanel();
        encabezado.setLayout(new BorderLayout());
        encabezado.setPreferredSize(new Dimension(300, 70));
        encabezado.setBackground(Color.decode("#468DAE"));
        URL imgUrl = getClass().getResource("/logo.png");
        if (imgUrl != null) {
            ImageIcon icon = new ImageIcon(imgUrl); // escala 1:1
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

        // Panel de login
        JPanel login = new JPanel();
        login.setPreferredSize(new Dimension(300, 290));
        login.setBackground(Color.white);
        login.setLayout(null);
        tarjeta.add(login);

        // Campos de usuario y contraseña
        JLabel usuarioLabel = new JLabel("Usuario");
        usuarioLabel.setBounds(30, 30, 240, 20);
        // Asegurar estilo de fuente normal (no negrita)
        usuarioLabel.setFont(usuarioLabel.getFont().deriveFont(Font.PLAIN));
        login.add(usuarioLabel);

        JTextField usuarioField = new JTextField();
        usuarioField.setBounds(30, 70, 240, 35);
        login.add(usuarioField);

        JLabel contrasenaLabel = new JLabel("Contraseña");
        contrasenaLabel.setBounds(30, 110, 240, 20);
        // Asegurar estilo de fuente normal (no negrita)
        contrasenaLabel.setFont(contrasenaLabel.getFont().deriveFont(Font.PLAIN));
        login.add(contrasenaLabel);

        JTextField contrasenaField = new JTextField();
        contrasenaField.setBounds(30, 150, 240, 35);
        login.add(contrasenaField);

        // Boton de acceder
        JButton btnAcceder = new JButton("Acceder");
        btnAcceder.setBounds(30, 200, 240, 35);
        btnAcceder.setBorder(null);
        btnAcceder.setBackground(Color.decode("#F4791B"));
        btnAcceder.setForeground(Color.white);
        login.add(btnAcceder);

        // Texto de recuperar cuenta (usar JLabel con HTML para el enlace)
        JLabel recuperarCuenta = new JLabel("<html>Si has olvidado tu cuenta, Haz click <span style='color:#468DAE; font-weight:bold'>aquí</span></html>");
        recuperarCuenta.setBounds(30, 240, 240, 20);
        // Asegurar fuente normal en la etiqueta principal (el <span> puede seguir en negrita)
        recuperarCuenta.setFont(recuperarCuenta.getFont().deriveFont(Font.PLAIN));
        recuperarCuenta.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        recuperarCuenta.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                controlador.getControladorNavegacion().cambiarPantallaPadre("recuperarCuenta");
            }
        });
        login.add(recuperarCuenta);

        JLabel version = new JLabel("Sistema de Gestion Academica v1.0");
        version.setBounds(40, 260, 240, 20);
        version.setFont(version.getFont().deriveFont(Font.PLAIN));
        login.add(version);

        // Agregar la tarjeta al panel principal
        panel.add(tarjeta);

        // Eventos
        btnAcceder.addActionListener(e -> {
            // temporal no hay logica de backend aun
            controlador.getControladorNavegacion().cambiarPantallaPadre("entrarSistema");
        });
        return panel;
    }

}
