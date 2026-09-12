package com.biblioteca.vista.dialogo;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.time.LocalDate;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import com.biblioteca.controlador.Controlador;
import com.biblioteca.utilities.Fonts;

/**
 * Modal para crear una nueva publicación.
 * Implementa un pequeño wizard: paso 1 (datos comunes) → paso 2 (datos por
 * tipo)
 */
public class NuevaPublicacionDialog extends OverlayDialog {
    /**
     * Controlador principal de la aplicación
     */
    private Controlador controlador;
    /**
     * Panel de tarjetas para el wizard
     */
    private JPanel cardPanel;
    /**
     * Layout de tarjetas
     */
    private CardLayout cardLayout;

    // Step 1 fields
    /**
     * Campos paso 1 (isbn)
     */
    private JTextField isbnField;
    /**
     * Campos paso 1 (titulo)
     */
    private JTextField tituloField;
    /**
     * Campos paso 1 (idioma)
     */
    private JTextField idiomaField;
    /**
     * Campos paso 1 (temas)
     */
    private JTextField temasField;
    /**
     * Campos paso 1 (modulos)
     */
    private JTextField modulosField;
    /**
     * Campos paso 1 (ciclos)
     */
    private JTextField ciclosField;
    /**
     * Campos paso 1 (editorial)
     */
    private JTextField editorialField;
    /**
     * Campos paso 1 (tipo)
     */
    private JComboBox<String> tipoCombo;

    // Step Libro
    /**
     * Campos paso Libro (número edición)
     */
    private JTextField numeroEdicionField;
    /**
     * Campos paso Libro (fecha publicación)
     */
    private JTextField fechaPublicacionField;
    /**
     * Campos paso Libro (autores)
     */
    private JTextField autoresField;

    // Step Revista
    /**
     * Campos paso Revista (periodicidad)
     */
    private JTextField periodicidadField;

    /**
     * Constructor
     * 
     * @param parent
     * @param controlador
     */
    public NuevaPublicacionDialog(final JFrame parent, final Controlador controlador) {
        super(parent, "Nueva Publicación");
        this.controlador = controlador;
        initUI();
        setSize(new Dimension(340, 500));
        setLocationRelativeTo(parent);

        // Asegurar que si el diálogo se cierra por otros medios, el overlay se restaura
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed(final WindowEvent e) {
                removeOverlay();
            }

            @Override
            public void windowClosing(final WindowEvent e) {
                removeOverlay();
            }
        });
    }

    /**
     * Inicializa la interfaz de usuario
     */
    private void initUI() {
        cardLayout = new java.awt.CardLayout();
        cardPanel = new JPanel(cardLayout);

        // Paso 1: datos comunes
        JPanel paso1 = crearPaso1();

        // Paso Libro
        JPanel pasoLibro = crearPasoLibro();

        // Paso Revista
        JPanel pasoRevista = crearPasoRevista();

        cardPanel.add(paso1, "paso1");
        cardPanel.add(pasoLibro, "libro");
        cardPanel.add(pasoRevista, "revista");

        getContentPane().setLayout(new BorderLayout());
        getContentPane().add(cardPanel, BorderLayout.CENTER);
    }

    /**
     * Crea el panel del paso 1 (datos comunes)
     * 
     * @return el panel del paso 1
     */
    private JPanel crearPaso1() {
        // Definir panel
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(Color.white);

        // Definir constraints
        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(8, 12, 8, 12);
        c.fill = GridBagConstraints.HORIZONTAL;
        c.anchor = GridBagConstraints.WEST;

        // Titulo
        JLabel title = new JLabel("Nueva Publicación");
        title.setFont(title.getFont().deriveFont(Font.BOLD, 16f));
        c.gridx = 0;
        c.gridy = 0;
        c.gridwidth = 2;
        panel.add(title, c);

        // Campos
        // ISBN
        c.gridwidth = 1;
        c.gridy++;
        panel.add(new JLabel("ISBN"), c);
        isbnField = new JTextField();
        configurarCampo(isbnField);
        // Formatos aceptados: ISBN clásico o códigos de revista (ej. 978-1-23456-789-0
        // o RV-2024-001)
        isbnField.setToolTipText("Formato ISBN o código de revista. Ej: 978-1-23456-789-0 o RV-2024-001");
        c.gridx = 1;
        panel.add(isbnField, c);

        // Titulo
        c.gridx = 0;
        c.gridy++;
        panel.add(new JLabel("Titulo"), c);
        tituloField = new JTextField();
        configurarCampo(tituloField);
        tituloField.setToolTipText("Título completo de la publicación (obligatorio)");
        c.gridx = 1;
        panel.add(tituloField, c);

        // Idioma
        c.gridx = 0;
        c.gridy++;
        panel.add(new JLabel("Idioma"), c);
        idiomaField = new JTextField();
        configurarCampo(idiomaField);
        idiomaField.setToolTipText("Idioma de la publicación (ej: Español, Inglés)");
        c.gridx = 1;
        panel.add(idiomaField, c);

        // Temas
        c.gridx = 0;
        c.gridy++;
        panel.add(new JLabel("Temas"), c);
        temasField = new JTextField();
        configurarCampo(temasField);
        temasField.setToolTipText("Lista de temas separados por comas (ej: Programación, Java)");
        c.gridx = 1;
        panel.add(temasField, c);

        // Modulos
        c.gridx = 0;
        c.gridy++;
        panel.add(new JLabel("Modulos"), c);
        modulosField = new JTextField();
        configurarCampo(modulosField);
        modulosField.setToolTipText("Lista de módulos separados por comas (ej: Programación, Bases de Datos)");
        c.gridx = 1;
        panel.add(modulosField, c);

        // Ciclos
        c.gridx = 0;
        c.gridy++;
        panel.add(new JLabel("Ciclos"), c);
        ciclosField = new JTextField();
        configurarCampo(ciclosField);
        ciclosField.setToolTipText("Lista de ciclos separados por comas (ej: DAM, DAW)");
        c.gridx = 1;
        panel.add(ciclosField, c);

        // Editorial
        c.gridx = 0;
        c.gridy++;
        panel.add(new JLabel("Editorial"), c);
        editorialField = new JTextField();
        configurarCampo(editorialField);
        editorialField.setToolTipText("Editorial de la publicación (obligatorio)");
        c.gridx = 1;
        panel.add(editorialField, c);

        // Tipo
        c.gridx = 0;
        c.gridy++;
        panel.add(new JLabel("Tipo"), c);
        tipoCombo = new JComboBox<>();
        tipoCombo.addItem("Libro");
        tipoCombo.addItem("Revista");
        tipoCombo.setToolTipText("Seleccione el tipo de publicación (Libro o Revista)");
        c.gridx = 1;
        panel.add(tipoCombo, c);

        // Botón siguiente
        JButton siguiente = new JButton("Siguiente");
        siguiente.setBackground(Color.decode("#F4791B"));
        siguiente.setForeground(Color.white);
        siguiente.setBorder(null);
        c.gridx = 1;
        c.gridy++;
        c.anchor = GridBagConstraints.EAST;
        panel.add(siguiente, c);

        // Acción botón siguiente
        siguiente.addActionListener(e -> {
            String tipo = (String) tipoCombo.getSelectedItem();
            if ("Libro".equals(tipo)) {
                cardLayout.show(cardPanel, "libro");
            } else {
                cardLayout.show(cardPanel, "revista");
            }
        });

        return panel;
    }

    /**
     * Crea el panel del paso Libro
     * 
     * @return el panel del paso Libro
     */
    private JPanel crearPasoLibro() {
        // Definir panel
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(Color.white);
        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(8, 12, 8, 12);
        c.fill = GridBagConstraints.HORIZONTAL;

        // Titulo
        JLabel title = new JLabel("Nueva Publicación (Libro)");
        title.setFont(title.getFont().deriveFont(Font.BOLD, 16f));
        c.gridx = 0;
        c.gridy = 0;
        c.gridwidth = 2;
        panel.add(title, c);

        // Campos específicos
        // Número edición
        c.gridwidth = 1;
        c.gridy++;
        panel.add(new JLabel("Numero edición"), c);
        numeroEdicionField = new JTextField();
        configurarCampo(numeroEdicionField);
        numeroEdicionField.setToolTipText("Número entero mayor que 0");
        c.gridx = 1;
        panel.add(numeroEdicionField, c);

        // Fecha publicación
        c.gridx = 0;
        c.gridy++;
        panel.add(new JLabel("Fecha publicacion"), c);
        fechaPublicacionField = new JTextField();
        configurarCampo(fechaPublicacionField);
        fechaPublicacionField.setToolTipText("Formato YYYY-MM-DD (ej: 2023-10-05)");
        c.gridx = 1;
        panel.add(fechaPublicacionField, c);

        // Autores
        c.gridx = 0;
        c.gridy++;
        panel.add(new JLabel("Autores"), c);
        autoresField = new JTextField();
        configurarCampo(autoresField);
        autoresField.setToolTipText("Lista de autores separados por comas (ej: Ana García, Carlos Pérez)");
        c.gridx = 1;
        panel.add(autoresField, c);

        // Botón añadir
        JButton anadir = new JButton("Añadir");
        anadir.setBackground(Color.decode("#F4791B"));
        anadir.setForeground(Color.white);
        anadir.setBorder(null);
        c.gridx = 1;
        c.gridy++;
        c.anchor = GridBagConstraints.EAST;
        panel.add(anadir, c);

        // Acción botón añadir
        anadir.addActionListener(e -> {
            // Validar campos
            if (!validarPasoLibro()) {
                return;
            }
            // Intentar crear la publicación
            boolean ok = controlador.getControladorNuevaPublicacionDialog().crearPublicacionLibro(
                    isbnField.getText().trim(),
                    tituloField.getText().trim(),
                    idiomaField.getText().trim(),
                    temasField.getText().trim(),
                    modulosField.getText().trim(),
                    ciclosField.getText().trim(),
                    editorialField.getText().trim(),
                    Integer.parseInt(numeroEdicionField.getText().trim()),
                    LocalDate.parse(fechaPublicacionField.getText().trim()),
                    autoresField.getText().trim());
            // Mostrar resultado
            if (ok) {
                JOptionPane.showMessageDialog(this, "Publicación tipo Libro añadida", "Éxito",
                        JOptionPane.INFORMATION_MESSAGE);
                // Refrescar vista de publicaciones y panel de control
                controlador.getControladorNavegacion().refrescarPublicaciones();
                controlador.getControladorNavegacion().refrescarPanelControl();
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Error añadiendo la publicación en la base de datos", "Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        });

        // Botón volver
        JButton volver = new JButton("Volver");
        volver.setBackground(Color.white);
        volver.setForeground(Color.decode("#000000"));
        c.gridx = 0;
        panel.add(volver, c);
        volver.addActionListener(e -> cardLayout.show(cardPanel, "paso1"));

        return panel;
    }

    /**
     * Crea el panel del paso Revista
     * 
     * @return el panel del paso Revista
     */
    private JPanel crearPasoRevista() {
        // Definir panel
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(Color.white);
        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(8, 12, 8, 12);
        c.fill = GridBagConstraints.HORIZONTAL;

        // Titulo
        JLabel title = new JLabel("Nueva Publicación (Revista)");
        title.setFont(title.getFont().deriveFont(Font.BOLD, 16f));
        c.gridx = 0;
        c.gridy = 0;
        c.gridwidth = 2;
        panel.add(title, c);

        // Campos específicos
        // Periodicidad
        c.gridwidth = 1;
        c.gridy++;
        panel.add(new JLabel("Periodicidad"), c);
        periodicidadField = new JTextField();
        configurarCampo(periodicidadField);
        periodicidadField.setToolTipText("Ej: Mensual, Trimestral, Semestral");
        c.gridx = 1;
        panel.add(periodicidadField, c);

        // Botón añadir
        JButton anadir = new JButton("Añadir");
        anadir.setBackground(Color.decode("#F4791B"));
        anadir.setForeground(Color.white);
        anadir.setBorder(null);
        c.gridx = 1;
        c.gridy++;
        c.anchor = GridBagConstraints.EAST;
        panel.add(anadir, c);

        // Acción botón añadir
        anadir.addActionListener(e -> {
            // Validar campos
            if (!validarPasoRevista()) {
                return;
            }
            // Intentar crear la publicación
            boolean ok = controlador.getControladorNuevaPublicacionDialog().crearPublicacionRevista(
                    isbnField.getText().trim(),
                    tituloField.getText().trim(),
                    idiomaField.getText().trim(),
                    temasField.getText().trim(),
                    modulosField.getText().trim(),
                    ciclosField.getText().trim(),
                    editorialField.getText().trim(),
                    periodicidadField.getText().trim());
            // Mostrar resultado
            if (ok) {
                JOptionPane.showMessageDialog(this, "Publicación tipo Revista añadida", "Éxito",
                        JOptionPane.INFORMATION_MESSAGE);
                // Refrescar vista de publicaciones y panel de control
                controlador.getControladorNavegacion().refrescarPublicaciones();
                controlador.getControladorNavegacion().refrescarPanelControl();
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Error añadiendo la publicación en la base de datos", "Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        });

        // Botón volver
        JButton volver = new JButton("Volver");
        volver.setBackground(Color.white);
        volver.setForeground(Color.decode("#000000"));
        c.gridx = 0;
        panel.add(volver, c);
        volver.addActionListener(e -> cardLayout.show(cardPanel, "paso1"));

        return panel;
    }

    /**
     * Valida los campos comunes del paso 1
     * 
     * @return true si los campos son válidos, false si hay errores
     */
    private boolean validarPasoComun() {
        // Campos obligatorios en tabla `publicaciones`: titulo, editorial, codigo_isbn,
        // idioma, tipo
        if (tituloField.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "El título es requerido", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        if (isbnField.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "El ISBN es requerido", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        if (editorialField.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "La editorial es requerida", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        if (idiomaField.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "El idioma es requerido", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        if (tipoCombo.getSelectedItem() == null) {
            JOptionPane.showMessageDialog(this, "Seleccione el tipo de publicación", "Error",
                    JOptionPane.ERROR_MESSAGE);
            return false;
        }
        return true;
    }

    /**
     * Valida los campos del paso Libro
     * 
     * @return true si los campos son válidos, false si hay errores
     */
    private boolean validarPasoLibro() {
        if (!validarPasoComun()) {
            return false;
        }
        // Campos obligatorios en tabla `libros`: num_edicion, fecha_publicacion
        String numEd = numeroEdicionField.getText().trim();
        if (numEd.isEmpty()) {
            JOptionPane.showMessageDialog(this, "El número de edición es requerido", "Error",
                    JOptionPane.ERROR_MESSAGE);
            return false;
        }
        try {
            int n = Integer.parseInt(numEd);
            if (n <= 0) {
                JOptionPane.showMessageDialog(this, "El número de edición debe ser mayor que 0", "Error",
                        JOptionPane.ERROR_MESSAGE);
                return false;
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Número de edición inválido", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        String fecha = fechaPublicacionField.getText().trim();
        if (fecha.isEmpty()) {
            JOptionPane.showMessageDialog(this, "La fecha de publicación es requerida (YYYY-MM-DD)", "Error",
                    JOptionPane.ERROR_MESSAGE);
            return false;
        }
        try {
            // Intentar parsear la fecha
            LocalDate.parse(fecha);
        } catch (java.time.format.DateTimeParseException ex) {
            JOptionPane.showMessageDialog(this, "Formato de fecha inválido. Use YYYY-MM-DD", "Error",
                    JOptionPane.ERROR_MESSAGE);
            return false;
        }
        return true;
    }

    /**
     * Valida los campos del paso Revista
     * 
     * @return true si los campos son válidos, false si hay errores
     */
    private boolean validarPasoRevista() {
        if (!validarPasoComun()) {
            return false;
        }
        // Campos obligatorios en tabla `revistas`: periodicidad, num_revista
        // (num_revista can be derived or optional here, but periodicidad is NOT NULL)
        String per = periodicidadField.getText().trim();
        if (per.isEmpty()) {
            JOptionPane.showMessageDialog(this, "La periodicidad es requerida", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        return true;
    }

    /**
     * Configura el estilo de un campo de texto
     * 
     * @param field
     */
    private void configurarCampo(final JTextField field) {
        field.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(Color.decode("#CCCCCC")),
                BorderFactory.createEmptyBorder(6, 8, 6, 8)));
        field.setFont(Fonts.openSans(12f));
    }

}
