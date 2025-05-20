package io.github.imecuadorian.vitalmed.view.forms.auth;

import com.formdev.flatlaf.*;
import io.github.imecuadorian.vitalmed.controller.*;
import io.github.imecuadorian.vitalmed.factory.*;
import io.github.imecuadorian.vitalmed.i18n.I18n;
import io.github.imecuadorian.vitalmed.model.*;
import io.github.imecuadorian.vitalmed.util.*;
import io.github.imecuadorian.vitalmed.view.*;
import io.github.imecuadorian.vitalmed.view.menu.*;
import io.github.imecuadorian.vitalmed.view.modal.*;
import io.github.imecuadorian.vitalmed.view.system.*;
import net.miginfocom.swing.*;
import raven.modal.*;
import raven.modal.component.*;
import raven.modal.toast.option.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;

import static io.github.imecuadorian.vitalmed.util.Constants.getSelectedOption;
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
        Image scaled = logo.getImage().getScaledInstance(450, 120, Image.SCALE_SMOOTH);
        add(new JLabel(new ImageIcon(scaled, "Vitalmed")), "gapbottom 40");

        JLabel lbTitle = new JLabel(I18n.t("auth.formLogin.welcomeBack.jLabel"), CENTER);
        lbTitle.putClientProperty(FlatClientProperties.STYLE, "font:bold +15;");
        add(lbTitle, "gapy 8 8");

        add(new JLabel(I18n.t("auth.formLogin.logInToContinue"), CENTER), "gapy 10 5");

        JLabel lbEmail = new JLabel(I18n.t("auth.formLogin.email"));
        lbEmail.putClientProperty(FlatClientProperties.STYLE, "font:bold;");
        add(lbEmail, "gapy 10 5");

        txtEmail = new JTextField();
        txtEmail.putClientProperty(FlatClientProperties.STYLE, "iconTextGap:10;");
        txtEmail.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, I18n.t("auth.formLogin.placeholder.email"));
        add(txtEmail);

        JLabel lbPassword = new JLabel(I18n.t("auth.formLogin.password"));
        lbPassword.putClientProperty(FlatClientProperties.STYLE, "font:bold;");
        add(lbPassword, "gapy 10 5");

        txtPassword = new JPasswordField();
        txtPassword.putClientProperty(FlatClientProperties.STYLE, "iconTextGap:10;showRevealButton:true;");
        txtPassword.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, I18n.t("auth.formLogin.placeholder.password"));
        add(txtPassword);

        JButton cmdSignIn = new JButton(I18n.t("auth.formLogin.JButton.signIn.JButton")) {
            @Override
            public boolean isDefaultButton() {
                return true;
            }
        };
        cmdSignIn.putClientProperty(FlatClientProperties.STYLE, "foreground:#FFFFFF;iconTextGap:10;");
        cmdSignIn.setHorizontalTextPosition(JButton.LEADING);
        if (frame instanceof JFrame jFrame) {
            jFrame.getRootPane().setDefaultButton(cmdSignIn);
        }
        cmdSignIn.setDefaultCapable(true);
        add(cmdSignIn, "gapy 20 10");

        JLabel lbNoAccount = new JLabel(I18n.t("auth.formLogin.noAccount.jLabel"), CENTER);
        lbNoAccount.putClientProperty(FlatClientProperties.STYLE, "foreground:$Label.disabledForeground;");
        add(lbNoAccount, "split 2,gapx push n");

        JButton cmdCreateAccount = createNoBorderButton();
        cmdCreateAccount.addActionListener(this::createAccountActionPerformed);
        add(cmdCreateAccount, "gapx n push");

        cmdSignIn.addActionListener(this::signInActionPerformed);

        JButton cmdExit = new JButton(I18n.t("auth.formLogin.JButton.exit.JButton"));
        cmdExit.putClientProperty(FlatClientProperties.STYLE, "iconTextGap:10;");
        add(cmdExit, "gapy 100 10, align center");

        String message = I18n.t("auth.formLogin.exitMessage");
        cmdExit.addActionListener(e -> {
            ModalDialog.showModal(this, new SimpleMessageModal(SimpleMessageModal.Type.WARNING, message, I18n.t("auth.formLogin.typeWarning.messageExit"), SimpleModalBorder.YES_NO_OPTION, (controller, action) -> {
                if (action == SimpleModalBorder.YES_OPTION) {
                    System.exit(0);
                }
            }), getSelectedOption());
        });


        InputValidator.applyEmailValidation(txtEmail);
        InputValidator.applyPasswordValidation(txtPassword);

    }

    private void signInActionPerformed(ActionEvent e) {
        boolean valid = true;

        valid &= InputValidator.isNotEmpty(txtEmail, I18n.t("auth.formLogin.txtEmail.notEmpty"));
        valid &= InputValidator.isValidEmail(txtEmail, I18n.t("auth.formLogin.txtEmail.validEmail"));
        valid &= InputValidator.isNotEmpty(txtPassword, I18n.t("auth.formLogin.txtPassword.notEmpty"));
        valid &= InputValidator.isValidPassword(txtPassword, I18n.t("auth.formLogin.txtPassword.validPassword"));

        if (!valid) return;

        String email = txtEmail.getText();
        String password = new String(txtPassword.getPassword());

        User user = loginController.login(email, password);
        if (user == null) {
            Toast.show(this, Toast.Type.ERROR, I18n.t("auth.formLogin.typeError.credentialsIncorrect"), ToastLocation.TOP_TRAILING, Constants.getOption());
        } else {
            frame.dispose();
            new MainDashboard().setVisible(true);
            MyDrawerBuilder.getInstance().setUser(user);
            FormManager.login();
        }
    }

    private void createAccountActionPerformed(ActionEvent e) {
        ModalDialog.getDefaultOption().getBorderOption().setBorderWidth(2);
        ModalDialog.showModal(this, new SimpleModalBorder(new FormRegister(), I18n.t("auth.formLogin.formRegister.title"), SimpleModalBorder.DEFAULT_OPTION, (controller, action) -> {
        }));
    }

    private JButton createNoBorderButton() {
        JButton button = new JButton(I18n.t("auth.formLogin.JButton.register.JButton"));
        button.putClientProperty(FlatClientProperties.STYLE,
                "foreground:$Component.accentColor;margin:1,5,1,5;borderWidth:0;focusWidth:0;innerFocusWidth:0;background:null;");
        return button;
    }
}