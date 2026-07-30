package com.example.vista.pantalla;

import com.example.controlador.Controlador;

import java.awt.Color;
import java.awt.Dimension;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;

/**
 * Clase para la vista Sanción manual
 */
public class SancionManual {

    /**
     * Controlador de la aplicación
     */
    Controlador controlador;

    /**
     * Constructor de la vista SancionManual
     *
     * @param controlador controlador principal
     */
    public SancionManual(Controlador controlador) {
        this.controlador = controlador;
    }

    /**
     * Muestra la pantalla para crear sanciones manuales
     *
     * @return JPanel con la interfaz de sanciones
     */
    public JPanel pantalla() {
        JPanel panel = new JPanel();
        panel.setPreferredSize(new Dimension(600, 600));
        panel.setBackground(Color.decode("#EDF3F6"));
        panel.setLayout(null);

        // Panel de encabezado con título
        JPanel encabezado = new JPanel();
        encabezado.setSize(new Dimension(600, 60));
        encabezado.setBackground(Color.white);
        encabezado.setLayout(null);
        encabezado.setBounds(0, 0, 600, 60);

        // Titulo
        JLabel titulo = new JLabel("Panel de Control");
        titulo.setFont(titulo.getFont().deriveFont(24f));
        titulo.setBounds(10, 10, 200, 40);
        encabezado.add(titulo);

        // Formulario sancion manual
        JPanel formularioSancion = new JPanel();
        formularioSancion.setSize(540, 450);
        formularioSancion.setBounds(30, 90, 540, 450);
        formularioSancion.setBackground(Color.white);
        formularioSancion.setLayout(null);

        // Paso 1: Usuario afectado
        JLabel paso1 = new JLabel("1. Usuario afectado:");
        paso1.setFont(paso1.getFont().deriveFont(16f));
        paso1.setBounds(20, 10, 300, 30);
        paso1.setForeground(Color.decode("#468DAE"));
        formularioSancion.add(paso1);

        // Resultado elegir socio
        JLabel resultadoSocio = new JLabel("");
        resultadoSocio.setBounds(20, 50, 500, 40);
        resultadoSocio.setText(
                "<html>Usuario: Estudiante) : <span style='color:#2BC187; font-weight:bold'>Sin sanciones</span></html>");
        resultadoSocio.setBackground(Color.decode("#EDF3F6"));
        resultadoSocio.setOpaque(true);
        // Pequeño margen izquierdo para separar el texto del borde (10px)
        resultadoSocio.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 0));
        formularioSancion.add(resultadoSocio);

        // mutable holder para el id de usuario seleccionado (para usar desde lambdas)
        final int[] usuarioSeleccionado = new int[] { -1 };

        JButton btnCambiarUsuario = new JButton("Cambiar");
        btnCambiarUsuario.setBounds(390, 55, 100, 30);
        btnCambiarUsuario.setBackground(Color.decode("#468DAE"));
        btnCambiarUsuario.setForeground(Color.WHITE);
        btnCambiarUsuario.setFocusPainted(false);
        btnCambiarUsuario.setBorder(null);
        // Para que el botón quede encima del JLabel
        formularioSancion.add(btnCambiarUsuario, 0);

        // Acción del botón Cambiar -> abrir modal con usuarios sancionables
        btnCambiarUsuario.addActionListener(e -> {
            // obtener usuarios sancionables desde el controlador
            String[][] usuarios = controlador.getControladorGestionUsuarios().obtenerUsuariosSancionables();
            if (usuarios == null || usuarios.length == 0) {
                JOptionPane.showMessageDialog(null, "No hay usuarios sancionables", "Información",
                        JOptionPane.INFORMATION_MESSAGE);
                return;
            }
            // construir lista de strings y mapping a ids
            java.util.Map<String, Integer> mapa = new java.util.LinkedHashMap<>();
            DefaultListModel<String> listModel = new DefaultListModel<>();
            for (String[] u : usuarios) {
                String item = u[2] + " | DNI: " + u[1] + " | Estudiante"; // Apellido, Nombre
                listModel.addElement(item);
                mapa.put(item, Integer.parseInt(u[0]));
            }

            // mostrar diálogo con lista
            JList<String> jlist = new JList<>(listModel);
            jlist.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
            JScrollPane scroll = new JScrollPane(jlist);
            scroll.setPreferredSize(new java.awt.Dimension(400, 200));
            int option = JOptionPane.showConfirmDialog(null, scroll, "Seleccione usuario",
                    JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

            // si se seleccionó un usuario, actualizar resultadoSocio y usuarioSeleccionado
            if (option == JOptionPane.OK_OPTION) {
                String sel = jlist.getSelectedValue();
                if (sel != null) {
                    usuarioSeleccionado[0] = mapa.get(sel);
                    // mostrar en resultadoSocio: Apellido, Nombre | DNI | Estudiante
                    resultadoSocio.setText("<html><b>" + sel + "</b></html>");
                }
            }
        });

        // Paso 2: Detalles de la sancion y ejemplar afectado
        JLabel paso2 = new JLabel("2. Detalles de la sanción y ejemplar afectado:");
        paso2.setFont(paso2.getFont().deriveFont(16f));
        paso2.setBounds(20, 110, 400, 30);
        paso2.setForeground(Color.decode("#468DAE"));
        formularioSancion.add(paso2);

        // Motivo de la sancion
        JLabel lblMotivo = new JLabel("Motivo de la sanción:");
        lblMotivo.setBounds(20, 150, 200, 25);
        formularioSancion.add(lblMotivo);

        // JcomboBox motivo (opciones predefinidas)
        JComboBox<String> comboMotivo = new JComboBox<>();
        comboMotivo.setBounds(20, 180, 230, 30);
        comboMotivo.addItem("Daño de material");
        comboMotivo.addItem("Pérdida del material");
        comboMotivo.addItem("Comportamiento inapropiado");
        formularioSancion.add(comboMotivo);

        // Ejemplar afectado (alineado a la derecha del motivo)
        JLabel lblEjemplar = new JLabel("Ejemplar:");
        lblEjemplar.setBounds(270, 150, 200, 25);
        formularioSancion.add(lblEjemplar);

        JTextField txtEjemplar = new JTextField();
        txtEjemplar.setBounds(270, 180, 230, 30);
        formularioSancion.add(txtEjemplar);

        // Descripción de la sanción
        JLabel lblDescripcion = new JLabel("Descripción / Observaciones:");
        lblDescripcion.setBounds(20, 220, 200, 25);
        formularioSancion.add(lblDescripcion);

        JTextField txtDescripcion = new JTextField();
        txtDescripcion.setBounds(20, 250, 480, 80);
        formularioSancion.add(txtDescripcion);

        // Fecha de inicio de la sanción
        JLabel lblFechaInicio = new JLabel("Fecha de inicio de la sanción:");
        lblFechaInicio.setBounds(20, 340, 200, 25);
        formularioSancion.add(lblFechaInicio);

        JTextField txtFechaInicio = new JTextField();
        txtFechaInicio.setBounds(20, 370, 200, 30);
        // fijar fecha inicio a hoy y no editable
        String hoyStr = LocalDate.now().toString();
        txtFechaInicio.setText(hoyStr);
        txtFechaInicio.setEditable(false);
        formularioSancion.add(txtFechaInicio);

        // Fecha de fin de la sanción
        JLabel lblFechaFin = new JLabel("Fecha de fin de la sanción:");
        lblFechaFin.setBounds(270, 340, 200, 25);
        formularioSancion.add(lblFechaFin);

        JTextField txtFechaFin = new JTextField();
        txtFechaFin.setBounds(270, 370, 200, 30);
        formularioSancion.add(txtFechaFin);

        // Limpiar Formulario
        JButton btnLimpiar = new JButton("Limpiar Formulario");
        btnLimpiar.setBounds(150, 410, 150, 30);
        btnLimpiar.setBackground(Color.white);
        btnLimpiar.setForeground(Color.BLACK);
        btnLimpiar.setFocusPainted(false);
        btnLimpiar.setBorder(null);
        formularioSancion.add(btnLimpiar);

        // Aplicar Sanción
        JButton btnAplicarSancion = new JButton("Aplicar Sanción");
        btnAplicarSancion.setBounds(320, 410, 150, 30);
        btnAplicarSancion.setBackground(Color.decode("#F4791B"));
        btnAplicarSancion.setForeground(Color.WHITE);
        btnAplicarSancion.setFocusPainted(false);
        btnAplicarSancion.setBorder(null);
        formularioSancion.add(btnAplicarSancion);

        // Acciones botones
        btnLimpiar.addActionListener(e -> {
            // limpiar formulario
            usuarioSeleccionado[0] = -1;
            resultadoSocio.setText(
                    "<html>Usuario: Estudiante) : <span style='color:#2BC187; font-weight:bold'>Sin sanciones</span></html>");
            txtEjemplar.setText("");
            txtDescripcion.setText("");
            txtFechaFin.setText("");
            comboMotivo.setSelectedIndex(0);
        });

        btnAplicarSancion.addActionListener(e -> {
            // Validar usuario seleccionado
            if (usuarioSeleccionado[0] == -1) {
                JOptionPane.showMessageDialog(null, "Seleccione primero un usuario", "Error",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }
            // validar ejemplar
            String idEjStr = txtEjemplar.getText().trim();
            if (idEjStr.isEmpty()) {
                JOptionPane.showMessageDialog(null, "Introduzca el ID del ejemplar", "Error",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }
            int idEj;
            try {
                idEj = Integer.parseInt(idEjStr);
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(null, "ID de ejemplar inválido", "Error",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }
            // comprobar que el usuario fue el ultimo en tener el ejemplar
            String[] ultimo = controlador.getControladorSancionManual().obtenerUltimoPrestamoPorEjemplar(idEj);
            if (ultimo == null) {
                JOptionPane.showMessageDialog(null,
                        "No se encontró historial de préstamos para este ejemplar", "Error",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }
            int idUsuarioUlt = Integer.parseInt(ultimo[1]);
            if (idUsuarioUlt != usuarioSeleccionado[0]) {
                JOptionPane.showMessageDialog(null,
                        "El usuario seleccionado no fue el último en tener el ejemplar", "Error",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }
            int idPrestamo = Integer.parseInt(ultimo[0]);
            // validar fecha fin
            String finStr = txtFechaFin.getText().trim();
            LocalDate hoy = LocalDate.parse(txtFechaInicio.getText().trim());
            LocalDate fin;
            try {
                fin = LocalDate.parse(finStr);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(null, "Fecha fin inválida (formato YYYY-MM-DD)", "Error",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }
            if (fin.isBefore(hoy)) {
                JOptionPane.showMessageDialog(null,
                        "La fecha fin no puede ser anterior a la fecha de inicio", "Error",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }
            // preparar descripcion
            String motivo = (String) comboMotivo.getSelectedItem();
            String descAd = txtDescripcion.getText().trim();
            String descripcionBase = motivo + " - " + (descAd.isEmpty() ? "" : descAd);
            // comprobar sancion activa y acumulacion (usar controlador)
            String[] sancionActiva = controlador.getControladorSancionManual().obtenerSancionActivaPorUsuario(usuarioSeleccionado[0]);
            String descripcion = descripcionBase;
            if (sancionActiva != null && sancionActiva[1] != null && !sancionActiva[1].isEmpty()) {
                try {
                    LocalDate finAct = LocalDate.parse(sancionActiva[1]);
                    // días restantes de la sanción activa desde hoy (si es negativa, 0)
                    long diasRestantes = ChronoUnit.DAYS.between(hoy, finAct);
                    if (diasRestantes > 0) {
                        // extender la fecha fin propuesta sumando los días restantes
                        LocalDate finExtendida = fin.plusDays(diasRestantes);
                        descripcion = descripcionBase + " (Acumulativa: sanción activa hasta " + finAct + "; se añaden "
                                + diasRestantes + " días)";
                        // usar la fin extendida como fecha final real
                        fin = finExtendida;
                    } else {
                        descripcion = descripcionBase + " (Acumulativa: sanción activa hasta " + finAct
                                + "; no se añade plazo adicional)";
                    }
                } catch (Exception ex) {
                    // registrar el error al parsear la fecha anterior
                    System.out.println("Error leyendo sanción previa para usuario " + usuarioSeleccionado[0] + ": "
                            + ex.getMessage());
                    ex.printStackTrace();
                    descripcion = descripcionBase + " (Acumulativa: fallo leyendo sanción previa)";
                }
            }
            // preparar notificación y desactivar sanción previa si existe
            String notificacion = "Sanción aplicada: fin " + fin.toString();
            boolean previaDesactivada = false;
            // notificar acumulación si aplica
            if (sancionActiva != null && sancionActiva[1] != null && !sancionActiva[1].isEmpty()) {
                try {
                    // analizar sanción activa previa para el mensaje
                    LocalDate finAct = LocalDate.parse(sancionActiva[1]);
                    long diasRestantes = ChronoUnit.DAYS.between(hoy, finAct);
                    // construir mensaje adecuado
                    if (diasRestantes > 0) {
                        LocalDate finExtendida = fin;
                        notificacion = "Sanción acumulativa: anterior fin " + finAct + ", nuevo fin " + finExtendida
                                + ", se añadieron " + diasRestantes + " días.";
                    } else {
                        notificacion = "Sanción acumulativa: anterior fin " + finAct
                                + ", no se añadió plazo adicional.";
                    }
                    // intentar desactivar la sanción previa
                    try {
                        int idPrev = Integer.parseInt(sancionActiva[0]);
                        previaDesactivada = controlador.getControladorSancionManual().desactivarSancionPorId(idPrev);
                        if (previaDesactivada) {
                            notificacion += " La sanción previa ha sido desactivada.";
                        }
                    } catch (Exception ex2) {
                        // registrar el error al intentar desactivar la sanción previa
                        System.out.println(
                                "Error desactivando sanción previa (id=" + sancionActiva[0] + "): " + ex2.getMessage());
                        ex2.printStackTrace();
                    }
                } catch (Exception ex) {
                    System.out.println("Error preparando notificación de sanción manual: " + ex.getMessage());
                    ex.printStackTrace();
                    notificacion = "Sanción aplicada.";
                }
            }

            // insertar sancion (usar idPrestamo obtenido del historial)
            boolean ins = controlador.getControladorSancionManual().insertarSancion(usuarioSeleccionado[0], idPrestamo,
                    java.sql.Date.valueOf(hoy), java.sql.Date.valueOf(fin), descripcion);
            if (!ins) {
                JOptionPane.showMessageDialog(null, "Error aplicando la sanción", "Error",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }
            JOptionPane.showMessageDialog(null, "Sanción aplicada correctamente", "Éxito",
                    JOptionPane.INFORMATION_MESSAGE);
            // notificar acumulación si aplica
            if (notificacion != null && !notificacion.isEmpty()) {
                JOptionPane.showMessageDialog(null, notificacion, "Información",
                        JOptionPane.INFORMATION_MESSAGE);
            }
            // limpiar
            btnLimpiar.doClick();
        });

        // Agregar paneles al panel principal
        panel.add(encabezado);
        panel.add(formularioSancion);

        return panel;
    }

}
