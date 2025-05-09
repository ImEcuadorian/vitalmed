package io.github.imecuadorian.vitalmed.view.forms.auth;

import com.formdev.flatlaf.*;
import io.github.imecuadorian.vitalmed.controller.*;
import io.github.imecuadorian.vitalmed.factory.*;
import io.github.imecuadorian.vitalmed.model.*;
import io.github.imecuadorian.vitalmed.util.*;
import io.github.imecuadorian.vitalmed.view.*;
import io.github.imecuadorian.vitalmed.view.menu.*;
import io.github.imecuadorian.vitalmed.view.system.*;
import net.miginfocom.swing.*;
import raven.modal.*;
import raven.modal.component.*;
import raven.modal.toast.option.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;

import static javax.swing.SwingConstants.*;

public class FormLogin extends JPanel {

    private final Frame frame;
    private JTextField txtEmail;
    private JPasswordField txtPassword;
    private final LoginController loginController = new LoginController(
            ServiceFactory.getAdminService(),
            ServiceFactory.getDoctorAuth(),
            ServiceFactory.getPatientAuth()
    );

    public FormLogin(Frame frame) {
        this.frame = frame;
        init();
    }

    private void init() {
        setLayout(new MigLayout("wrap,gapy 4", "[fill,450]"));

        ImageIcon logo = new ImageIcon(Objects.requireNonNull(getClass().getResource("/io/github/imecuadorian/vitalmed/images/vitalmed-main-logo.png")));
        Image scaled = logo.getImage().getScaledInstance(400, 120, Image.SCALE_SMOOTH);
        add(new JLabel(new ImageIcon(scaled, "Vitalmed")), "gapbottom 40");

        JLabel lbTitle = new JLabel("Bienvenido de Vuelta", CENTER);
        lbTitle.putClientProperty(FlatClientProperties.STYLE, "font:bold +15;");
        add(lbTitle, "gapy 8 8");

        add(new JLabel("Inicia sesión para continuar", CENTER), "gapy 10 5");

        JLabel lbEmail = new JLabel("Email");
        lbEmail.putClientProperty(FlatClientProperties.STYLE, "font:bold;");
        add(lbEmail, "gapy 10 5");

        txtEmail = new JTextField();
        txtEmail.putClientProperty(FlatClientProperties.STYLE, "iconTextGap:10;");
        txtEmail.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Enter your email");
        add(txtEmail);

        JLabel lbPassword = new JLabel("Contraseña");
        lbPassword.putClientProperty(FlatClientProperties.STYLE, "font:bold;");
        add(lbPassword, "gapy 10 5");

        txtPassword = new JPasswordField();
        txtPassword.putClientProperty(FlatClientProperties.STYLE, "iconTextGap:10;showRevealButton:true;");
        txtPassword.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Enter your password");
        add(txtPassword);

        JButton cmdSignIn = new JButton("Iniciar sesión") {
            @Override
            public boolean isDefaultButton() {
                return true;
            }
        };
        cmdSignIn.putClientProperty(FlatClientProperties.STYLE, "foreground:#FFFFFF;iconTextGap:10;");
        cmdSignIn.setHorizontalTextPosition(JButton.LEADING);
        add(cmdSignIn, "gapy 20 10");

        JLabel lbNoAccount = new JLabel("Don't have an account?");
        lbNoAccount.putClientProperty(FlatClientProperties.STYLE, "foreground:$Label.disabledForeground;");
        add(lbNoAccount, "split 2,gapx push n");

        JButton cmdCreateAccount = createNoBorderButton();
        cmdCreateAccount.addActionListener(this::createAccountActionPerformed);
        add(cmdCreateAccount, "gapx n push");

        cmdSignIn.addActionListener(this::signInActionPerformed);

        // Validation hooks
        InputValidator.applyEmailValidation(txtEmail);
        InputValidator.applyPasswordValidation(txtPassword);
    }

    private void signInActionPerformed(ActionEvent e) {
        boolean valid = true;

        valid &= InputValidator.isNotEmpty(txtEmail, "El campo email no puede estar vacío");
        valid &= InputValidator.isValidEmail(txtEmail, "El email no es válido");
        valid &= InputValidator.isNotEmpty(txtPassword, "El campo contraseña no puede estar vacío");
        valid &= InputValidator.isValidPassword(txtPassword, "El campo contraseña no es válido");

        if (!valid) return;

        String email = txtEmail.getText();
        String password = new String(txtPassword.getPassword());

        User user = loginController.login(email, password);
        if (user == null) {
            Toast.show(this, Toast.Type.ERROR, "Las credenciales son incorrectas", ToastLocation.TOP_TRAILING, Constants.getOption());
        } else {
            frame.dispose();
            new MainDashboard().setVisible(true);
            MyDrawerBuilder.getInstance().setUser(user);
            FormManager.login();
        }
    }

    private void createAccountActionPerformed(ActionEvent e) {
        ModalDialog.getDefaultOption().getBorderOption().setBorderWidth(2);
        ModalDialog.showModal(this, new SimpleModalBorder(new FormRegister(), "Register", SimpleModalBorder.DEFAULT_OPTION, (controller, action) -> {
        }));
    }

    private JButton createNoBorderButton() {
        JButton button = new JButton("Register");
        button.putClientProperty(FlatClientProperties.STYLE,
                "foreground:$Component.accentColor;margin:1,5,1,5;borderWidth:0;focusWidth:0;innerFocusWidth:0;background:null;");
        return button;
    }
}