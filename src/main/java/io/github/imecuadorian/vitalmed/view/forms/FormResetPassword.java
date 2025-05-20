package io.github.imecuadorian.vitalmed.view.forms;

import com.formdev.flatlaf.*;
import io.github.imecuadorian.vitalmed.controller.*;
import io.github.imecuadorian.vitalmed.factory.*;
import io.github.imecuadorian.vitalmed.i18n.*;
import io.github.imecuadorian.vitalmed.model.*;
import io.github.imecuadorian.vitalmed.util.*;
import io.github.imecuadorian.vitalmed.view.menu.*;
import io.github.imecuadorian.vitalmed.view.system.*;
import lombok.extern.slf4j.*;
import net.miginfocom.swing.*;

import javax.swing.*;
import java.awt.*;
import java.util.*;

@Slf4j
@SystemForm(name = "Reset Password", description = "Formulario para resetear la contraseña", tags = {"reset password"})
public class FormResetPassword extends Form implements LanguageChangeListener {

    private JLabel lblTitle;
    private JTextPane text;
    private final AdminDashboardController adminDashboardController = new AdminDashboardController(
            ServiceFactory.getAdminService()
    );

    public FormResetPassword() {
        init();
        I18n.addListener(this);
    }

    private JPanel createInfo() {
        JPanel panel = new JPanel(new MigLayout("fillx, wrap", "[fill]"));
        lblTitle = new JLabel();
        text = new JTextPane();
        text.setEditable(false);
        text.setBorder(BorderFactory.createEmptyBorder());
        lblTitle.putClientProperty(FlatClientProperties.STYLE, "font:bold +3");
        panel.add(lblTitle);
        panel.add(text);
        return panel;
    }

    private void init() {
        User user = MyMenuValidation.getUser();

        setLayout(new MigLayout("fill, insets 10 10 10 10", "[fill]", "[][grow]"));

        add(createInfo(), "growx, wrap");

        JPanel contentPanel = new JPanel(new MigLayout("fill", "[grow]", "[center]"));
        contentPanel.putClientProperty(FlatClientProperties.STYLE, "arc:20; background:$Table.background;");

        JPanel formPanel = new JPanel(new MigLayout("wrap 1, align center, gapy 15", "[300::300]", ""));
        formPanel.setOpaque(false);
        JLabel title = new JLabel(I18n.t("form.formResetPassword.title"));
        title.setFont(title.getFont().deriveFont(Font.BOLD, 20f));
        formPanel.add(title, "align center");

        JLabel passwordActualLabel = new JLabel(I18n.t("form.formResetPassword.passwordActual"));
        passwordActualLabel.setFont(passwordActualLabel.getFont().deriveFont(Font.PLAIN, 14f));
        JPasswordField passwordActualField = new JPasswordField(20);
        passwordActualField.setFont(passwordActualField.getFont().deriveFont(Font.PLAIN, 14f));
        passwordActualField.setToolTipText(I18n.t("form.formResetPassword.passwordActual.tooltip"));
        formPanel.add(passwordActualLabel);
        formPanel.add(passwordActualField, "w 100%");

        JLabel passwordNewLabel = new JLabel(I18n.t("form.formResetPassword.passwordNew"));
        passwordNewLabel.setFont(passwordNewLabel.getFont().deriveFont(Font.PLAIN, 14f));
        JPasswordField passwordNewField = new JPasswordField(20);
        passwordNewField.setFont(passwordNewField.getFont().deriveFont(Font.PLAIN, 14f));
        passwordNewField.setToolTipText(I18n.t("form.formResetPassword.passwordNew.tooltip"));
        formPanel.add(passwordNewLabel);
        formPanel.add(passwordNewField, "w 100%");

        JLabel passwordConfirmLabel = new JLabel(I18n.t("form.formResetPassword.passwordConfirm"));
        passwordConfirmLabel.setFont(passwordConfirmLabel.getFont().deriveFont(Font.PLAIN, 14f));
        JPasswordField passwordConfirmField = new JPasswordField(20);
        passwordConfirmField.setFont(passwordConfirmField.getFont().deriveFont(Font.PLAIN, 14f));
        passwordConfirmField.setToolTipText(I18n.t("form.formResetPassword.passwordConfirm.tooltip"));
        formPanel.add(passwordConfirmLabel);
        formPanel.add(passwordConfirmField, "w 100%");

        JButton resetButton = new JButton(I18n.t("form.formResetPassword.resetButton"));
        resetButton.setFont(resetButton.getFont().deriveFont(Font.PLAIN, 14f));
        resetButton.setToolTipText(I18n.t("form.formResetPassword.resetButton.tooltip"));
        formPanel.add(resetButton, "w 100%, h 40px, align center");

        resetButton.addActionListener(e -> {
            String passwordActual = new String(passwordActualField.getPassword());
            String passwordNew = new String(passwordNewField.getPassword());
            String passwordConfirm = new String(passwordConfirmField.getPassword());
            adminDashboardController.resetPassword(user.getId(), passwordNew);
        });

        contentPanel.add(formPanel, "align center");

        add(contentPanel, "grow");
    }


    @Override
    public void onLanguageChanged(ResourceBundle bundle) {
        lblTitle.setText("Reinicio de contraseña");
        text.setText("En este apartado podras reiniciar tu contraseña, por favor ingresa tu nueva contraseña y confirmala.");
    }
}
