package io.github.imecuadorian.vitalmed.view.forms.admin.form;

import javax.swing.*;

public class FormResetPassword extends JPanel {

    public FormResetPassword() {
        init();
    }

    private void init() {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JLabel title = new JLabel("Restablecer Contraseña");
        title.setFont(title.getFont().deriveFont(20f));
        add(title);

        JTextField txtEmail = new JTextField();
        txtEmail.putClientProperty("JTextField.placeholderText", "Ingrese su correo electrónico");
        add(txtEmail);
    }


}
