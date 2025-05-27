package io.github.imecuadorian.vitalmed.view.forms.auth;

import com.formdev.flatlaf.FlatClientProperties;
import io.github.imecuadorian.vitalmed.controller.LoginController;
import io.github.imecuadorian.vitalmed.factory.ServiceFactory;
import io.github.imecuadorian.vitalmed.i18n.I18n;
import io.github.imecuadorian.vitalmed.model.User;
import io.github.imecuadorian.vitalmed.util.Constants;
import io.github.imecuadorian.vitalmed.util.InputValidator;
import io.github.imecuadorian.vitalmed.view.MainDashboard;
import io.github.imecuadorian.vitalmed.view.menu.MyDrawerBuilder;
import io.github.imecuadorian.vitalmed.view.modal.SimpleMessageModal;
import io.github.imecuadorian.vitalmed.view.system.FormManager;
import net.miginfocom.swing.MigLayout;
import raven.modal.*;
import raven.modal.component.SimpleModalBorder;
import raven.modal.toast.option.ToastLocation;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.Objects;
import java.util.ResourceBundle;

import static javax.swing.SwingConstants.CENTER;

public class FormLogin extends JPanel implements io.github.imecuadorian.vitalmed.i18n.LanguageChangeListener {

    private static final int LOGO_WIDTH = 450;
    private static final int LOGO_HEIGHT = 120;
    private static final String LOGO_PATH = "/io/github/imecuadorian/vitalmed/images/vitalmed-main-logo.png";

    private final Frame frame;
    private JTextField txtEmail;
    private JPasswordField txtPassword;
    private JButton cmdSignIn;
    private JButton cmdCreateAccount;
    private JButton cmdExit;

    private final transient LoginController loginController = new LoginController(
            ServiceFactory.getAdminService(),
            ServiceFactory.getDoctorAuth(),
            ServiceFactory.getPatientAuth()
    );

    public FormLogin(Frame frame) {
        this.frame = frame;
        initUI();
        initListeners();
        I18n.addListener(this);
    }

    private void initUI() {
        setLayout(new MigLayout("wrap,gapy 4", "[fill," + LOGO_WIDTH + "]"));

        addLogo();
        addTitle();
        addFields();
        addButtons();
    }

    private void addLogo() {
        ImageIcon logo = new ImageIcon(Objects.requireNonNull(getClass().getResource(LOGO_PATH)));
        Image scaled = logo.getImage().getScaledInstance(LOGO_WIDTH, LOGO_HEIGHT, Image.SCALE_SMOOTH);
        add(new JLabel(new ImageIcon(scaled, "Vitalmed")), "gapbottom 40");
    }

    private void addTitle() {
        JLabel lbTitle = new JLabel(I18n.t("auth.formLogin.welcomeBack.jLabel"), CENTER);
        lbTitle.putClientProperty(FlatClientProperties.STYLE, "font:bold +15;");
        add(lbTitle, "gapy 8 8");

        add(new JLabel(I18n.t("auth.formLogin.logInToContinue"), CENTER), "gapy 10 5");
    }

    private void addFields() {
        addLabel(I18n.t("auth.formLogin.email"));
        txtEmail = createTextField();
        add(txtEmail);

        addLabel(I18n.t("auth.formLogin.password"));
        txtPassword = createPasswordField();
        add(txtPassword);
    }

    private void addButtons() {
        cmdSignIn = new JButton(I18n.t("auth.formLogin.JButton.signIn.JButton"));
        cmdSignIn.putClientProperty(FlatClientProperties.STYLE, "foreground:#FFFFFF;iconTextGap:10;");
        cmdSignIn.setHorizontalTextPosition(SwingConstants.LEADING);
        setAsDefaultButton(cmdSignIn);
        add(cmdSignIn, "gapy 20 10");

        add(createAccountSection());
        cmdExit = new JButton(I18n.t("auth.formLogin.JButton.exit.JButton"));
        cmdExit.putClientProperty(FlatClientProperties.STYLE, "iconTextGap:10;");
        add(cmdExit, "gapy 100 10, align center");
    }

    private void initListeners() {
        cmdSignIn.addActionListener(this::signInActionPerformed);
        cmdCreateAccount.addActionListener(this::createAccountActionPerformed);
        cmdExit.addActionListener(e -> {
            String message = I18n.t("auth.formLogin.exitMessage");
            ModalDialog.showModal(this, new SimpleMessageModal(
                    SimpleMessageModal.Type.WARNING,
                    message,
                    I18n.t("auth.formLogin.typeWarning.messageExit"),
                    SimpleModalBorder.YES_NO_OPTION,
                    (controller, action) -> {
                        if (action == SimpleModalBorder.YES_OPTION) System.exit(0);
                    }), Constants.getSelectedOption());
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

        User user = loginController.login(txtEmail.getText(), new String(txtPassword.getPassword()));
        if (user == null) {
            Toast.show(this, Toast.Type.ERROR,
                    I18n.t("auth.formLogin.typeError.credentialsIncorrect"),
                    ToastLocation.TOP_TRAILING,
                    Constants.getOption());
        } else {
            frame.dispose();
            new MainDashboard().setVisible(true);
            MyDrawerBuilder.getInstance().setUser(user);
            FormManager.login();
        }
    }

    private void createAccountActionPerformed(ActionEvent e) {
        ModalDialog.showModal(this,
                new SimpleModalBorder(new FormRegister(),
                        I18n.t("auth.formLogin.formRegister.title"),
                        SimpleModalBorder.DEFAULT_OPTION,
                        (controller, action) -> {}));
    }

    private JPanel createAccountSection() {
        JPanel panel = new JPanel(new MigLayout("insets 0", "push[][]push"));
        JLabel lbNoAccount = new JLabel(I18n.t("auth.formLogin.noAccount.jLabel"));
        lbNoAccount.putClientProperty(FlatClientProperties.STYLE, "foreground:$Label.disabledForeground;");
        panel.add(lbNoAccount);

        cmdCreateAccount = new JButton(I18n.t("auth.formLogin.JButton.register.JButton"));
        cmdCreateAccount.putClientProperty(FlatClientProperties.STYLE,
                "foreground:$Component.accentColor;borderWidth:0;focusWidth:0;innerFocusWidth:0;background:null;");
        panel.add(cmdCreateAccount);
        return panel;
    }

    private void addLabel(String text) {
        JLabel label = new JLabel(text);
        label.putClientProperty(FlatClientProperties.STYLE, "font:bold;");
        add(label, "gapy 10 5");
    }

    private JTextField createTextField() {
        JTextField field = new JTextField();
        field.putClientProperty(FlatClientProperties.STYLE, "iconTextGap:10;");
        field.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, I18n.t("auth.formLogin.placeholder.email"));
        return field;
    }

    private JPasswordField createPasswordField() {
        JPasswordField field = new JPasswordField();
        field.putClientProperty(FlatClientProperties.STYLE, "iconTextGap:10;showRevealButton:true;");
        field.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, I18n.t("auth.formLogin.placeholder.password"));
        return field;
    }

    private void setAsDefaultButton(JButton button) {
        if (frame instanceof JFrame jFrame) {
            jFrame.getRootPane().setDefaultButton(button);
        }
        button.setDefaultCapable(true);
    }

    @Override
    public void onLanguageChanged(ResourceBundle bundle) {
    }
}
