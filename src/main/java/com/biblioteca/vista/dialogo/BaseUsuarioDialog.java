package com.biblioteca.vista.dialogo;

import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JTextField;

import com.biblioteca.modelo.TipoUsuario;

/**
 * Clase base para diálogos de usuario (nuevo, editar).
 * BaseUsuarioDialog
 */
public abstract class BaseUsuarioDialog extends OverlayDialog {
    /**
     * Constructor
     * 
     * @param parent El JFrame padre del diálogo
     * @param titulo El título del diálogo
     */
    public BaseUsuarioDialog(final JFrame parent, final String titulo) {
        super(parent, titulo);
    }

    /**
     * Campos de texto del formulario
     */
    private JTextField dniField = new JTextField(20);
    /**
     * Otros campos del formulario
     */
    private JTextField nombreField = new JTextField(20);
    /**
     * Otros campos del formulario
     */
    private JTextField apellidosField = new JTextField(20);
    /**
     * Otros campos del formulario
     */
    private JTextField emailField = new JTextField(20);
    /**
     * Otros campos del formulario
     */
    private JComboBox<String> tipoBox = new JComboBox<>();

    /**
     * Obtiene el campo de texto del DNI.
     * 
     * @return El JTextField correspondiente al DNI.
     */
    public JTextField getDniField() {
        return dniField;
    }

    /**
     * Obtiene el campo de texto del nombre.
     * 
     * @return El JTextField correspondiente al nombre.
     */
    public JTextField getNombreField() {
        return nombreField;
    }

    /**
     * Obtiene el campo de texto de los apellidos.
     * 
     * @return El JTextField correspondiente a los apellidos.
     */
    public JTextField getApellidosField() {
        return apellidosField;
    }

    /**
     * Obtiene el campo de texto del email.
     * 
     * @return El JTextField correspondiente al email.
     */
    public JTextField getEmailField() {
        return emailField;
    }

    /**
     * Obtiene el JComboBox del tipo de usuario.
     * 
     * @return El JComboBox correspondiente al tipo de usuario.
     */
    public JComboBox<String> getTipoBox() {
        return tipoBox;
    }

    /**
     * Obtiene el valor del campo de texto del DNI.
     * 
     * @return El valor del campo de texto del DNI.
     */
    public String getDni() {
        return dniField.getText().trim();
    }

    /**
     * Obtiene el valor del campo de texto del nombre.
     * @return El valor del campo de texto del nombre.
     */
    public String getNombre() {
        return nombreField.getText().trim();
    }

    /**
     * Obtiene el valor del campo de texto de los apellidos.
     * @return El valor del campo de texto de los apellidos.
     */
    public String getApellidos() {
        return apellidosField.getText().trim();
    }

    /**
     * Obtiene el valor del campo de texto del email.
     * @return El valor del campo de texto del email.
     */
    public String getEmail() {
        return emailField.getText().trim();
    }

    /**
     * Obtiene el código del tipo de usuario seleccionado en el JComboBox.
     * @return El código del tipo de usuario seleccionado.
     */
    public String getCode() {
        return TipoUsuario.values()[getTipoBox().getSelectedIndex()].name();
    }
}
