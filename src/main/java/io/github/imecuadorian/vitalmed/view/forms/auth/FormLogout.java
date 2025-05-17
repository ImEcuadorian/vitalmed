package io.github.imecuadorian.vitalmed.view.forms.auth;

import io.github.imecuadorian.vitalmed.*;
import io.github.imecuadorian.vitalmed.i18n.I18n;
import io.github.imecuadorian.vitalmed.util.*;
import io.github.imecuadorian.vitalmed.view.menu.*;
import io.github.imecuadorian.vitalmed.view.system.*;
import net.miginfocom.swing.*;

import javax.swing.*;
import java.awt.*;

@SystemForm(name = "Cerrar Sesión", description = "Cerrar sesión del sistema", tags = {"cerrar sesión"})
public class FormLogout extends Form {

    public FormLogout() {
        init();
    }

    private void init() {
        setLayout(new MigLayout("insets 0, fill", "[center]", "[center]"));

        JPanel contentPanel = new JPanel(new MigLayout("wrap 1, align center, gapy 15", "[300::300]", "[]"));

        JLabel title = new JLabel(I18n.t("form.formLogout.title"));
        title.setFont(title.getFont().deriveFont(Font.BOLD, 20f));
        contentPanel.add(title, "align center");

        JLabel message = new JLabel(I18n.t("form.formLogout.message"));
        contentPanel.add(message, "align center");

        JButton btnLogout = new JButton(I18n.t("form.formLogout.btnLogout"));
        contentPanel.add(btnLogout, "align center");

        btnLogout.addActionListener(e -> {
            JFrame dashboardFrame = (JFrame) SwingUtilities.getWindowAncestor(btnLogout);
            dashboardFrame.dispose();

            EventQueue.invokeLater(()->{
                MyMenuValidation.setUser(null);
                new Vitalmed().setVisible(true);
            });
        });

        add(contentPanel);
    }
}
