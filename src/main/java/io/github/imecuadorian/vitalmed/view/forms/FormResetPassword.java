package io.github.imecuadorian.vitalmed.view.forms;

import io.github.imecuadorian.vitalmed.controller.*;
import io.github.imecuadorian.vitalmed.factory.*;
import io.github.imecuadorian.vitalmed.i18n.I18n;
import io.github.imecuadorian.vitalmed.model.*;
import io.github.imecuadorian.vitalmed.util.*;
import io.github.imecuadorian.vitalmed.view.menu.*;
import io.github.imecuadorian.vitalmed.view.system.*;
import net.miginfocom.swing.*;

import javax.swing.*;
import java.awt.*;

@SystemForm(name = "Reset Password", description = "Formulario para resetear la contraseña", tags = {"reset password"})
public class FormResetPassword extends Form {

    private final AdminDashboardController adminDashboardController = new AdminDashboardController(
            ServiceFactory.getAdminService()
    );

    public FormResetPassword() {
        init();
    }

    private void init() {

        User user = MyMenuValidation.getUser();

        setLayout(new MigLayout("insets 0, fill", "[center]", "[center]"));

        JPanel contentPanel = new JPanel(new MigLayout("wrap 1, align center, gapy 15", "[300::300]", "[]"));

        JLabel title = new JLabel(I18n.t("form.formResetPassword.title"));
        title.setFont(title.getFont().deriveFont(Font.BOLD, 20f));
        contentPanel.add(title, "align center");

        JLabel passwordActualLabel = new JLabel(I18n.t("form.formResetPassword.passwordActual"));
        passwordActualLabel.setFont(passwordActualLabel.getFont().deriveFont(Font.PLAIN, 14f));
        JPasswordField passwordActualField = new JPasswordField(20);
        passwordActualField.setFont(passwordActualField.getFont().deriveFont(Font.PLAIN, 14f));
        passwordActualField.setToolTipText(I18n.t("form.formResetPassword.passwordActual.tooltip"));
        contentPanel.add(passwordActualLabel);
        contentPanel.add(passwordActualField, "w 100%");
        JLabel passwordNewLabel = new JLabel(I18n.t("form.formResetPassword.passwordNew"));
        passwordNewLabel.setFont(passwordNewLabel.getFont().deriveFont(Font.PLAIN, 14f));
        JPasswordField passwordNewField = new JPasswordField(20);
        passwordNewField.setFont(passwordNewField.getFont().deriveFont(Font.PLAIN, 14f));
        passwordNewField.setToolTipText(I18n.t("form.formResetPassword.passwordNew.tooltip"));
        contentPanel.add(passwordNewLabel);
        contentPanel.add(passwordNewField, "w 100%");
        JLabel passwordConfirmLabel = new JLabel(I18n.t("form.formResetPassword.passwordConfirm"));
        passwordConfirmLabel.setFont(passwordConfirmLabel.getFont().deriveFont(Font.PLAIN, 14f));
        JPasswordField passwordConfirmField = new JPasswordField(20);
        passwordConfirmField.setFont(passwordConfirmField.getFont().deriveFont(Font.PLAIN, 14f));
        passwordConfirmField.setToolTipText(I18n.t("form.formResetPassword.passwordConfirm.tooltip"));
        contentPanel.add(passwordConfirmLabel);
        contentPanel.add(passwordConfirmField, "w 100%");
        JButton resetButton = new JButton(I18n.t("form.formResetPassword.resetButton"));
        resetButton.setFont(resetButton.getFont().deriveFont(Font.PLAIN, 14f));
        resetButton.setToolTipText(I18n.t("form.formResetPassword.resetButton.tooltip"));
        add(resetButton, "w 100%, h 40px, growx, pushx");
        resetButton.addActionListener(e -> {
            String passwordActual = new String(passwordActualField.getPassword());
            String passwordNew = new String(passwordNewField.getPassword());
            String passwordConfirm = new String(passwordConfirmField.getPassword());

            if (passwordActual.isEmpty() || passwordNew.isEmpty() || passwordConfirm.isEmpty()) {
                JOptionPane.showMessageDialog(this, I18n.t("form.formResetPassword.showMessage.completeFields"), "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (!passwordNew.equals(passwordConfirm)) {
                JOptionPane.showMessageDialog(this, I18n.t("form.formResetPassword.showMessage.passwordNoMatch"), "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            if (passwordNew.length() < 8) {
                JOptionPane.showMessageDialog(this, I18n.t("form.formResetPassword.showMessage.passwordInformation"), "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            if (passwordNew.equals(passwordActual)) {
                JOptionPane.showMessageDialog(this, I18n.t("form.formResetPassword.showMessage.newPassword"), "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            adminDashboardController.resetPassword(user.getId(), passwordNew);
        });

        add(contentPanel, "w 100%, h 100%, grow");

        add(contentPanel);
    }
}
