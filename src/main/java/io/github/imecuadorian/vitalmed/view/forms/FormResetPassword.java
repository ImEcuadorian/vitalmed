package io.github.imecuadorian.vitalmed.view.forms;

import io.github.imecuadorian.vitalmed.*;
import io.github.imecuadorian.vitalmed.util.*;
import io.github.imecuadorian.vitalmed.view.system.*;
import net.miginfocom.swing.*;

import javax.swing.*;
import java.awt.*;

@SystemForm(name = "Reset Password", description = "Formulario para resetear la contraseña", tags = {"reset password"})
public class FormResetPassword extends Form {



    private void init() {
        setLayout(new MigLayout("insets 0, fill", "[center]", "[center]"));

        JPanel contentPanel = new JPanel(new MigLayout("wrap 1, align center, gapy 15", "[300::300]", "[]"));

        JLabel title = new JLabel("Cerrar Sesión");
        title.setFont(title.getFont().deriveFont(Font.BOLD, 20f));
        contentPanel.add(title, "align center");

        JLabel message = new JLabel("¿Está seguro de que desea cerrar sesión?");
        contentPanel.add(message, "align center");

        JButton btnLogout = new JButton("Cerrar Sesión");
        contentPanel.add(btnLogout, "align center");

        btnLogout.addActionListener(e -> {
            JFrame dashboardFrame = (JFrame) SwingUtilities.getWindowAncestor(btnLogout);
            dashboardFrame.dispose();

            EventQueue.invokeLater(()->{
                new Vitalmed().setVisible(true);
            });
        });

        add(contentPanel);  // lo agregas centrado al contenedor principal
    }
}
