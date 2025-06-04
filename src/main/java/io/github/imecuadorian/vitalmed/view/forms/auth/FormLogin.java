package io.github.imecuadorian.vitalmed.view.forms.auth;

import com.formdev.flatlaf.*;
import io.github.imecuadorian.vitalmed.config.*;
import io.github.imecuadorian.vitalmed.controller.*;
import io.github.imecuadorian.vitalmed.factory.*;
import io.github.imecuadorian.vitalmed.i18n.*;
import io.github.imecuadorian.vitalmed.model.*;
import io.github.imecuadorian.vitalmed.util.*;
import io.github.imecuadorian.vitalmed.view.*;
import io.github.imecuadorian.vitalmed.view.menu.*;
import io.github.imecuadorian.vitalmed.view.modal.*;
import io.github.imecuadorian.vitalmed.view.system.*;
import net.miginfocom.swing.*;
import org.jetbrains.annotations.*;
import org.slf4j.*;
import raven.modal.*;
import raven.modal.component.*;
import raven.modal.toast.option.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;

import static javax.swing.SwingConstants.*;

public class FormLogin extends JPanel implements LanguageChangeListener {

    private static final Logger LOGGER = LoggerFactory.getLogger(FormLogin.class);
    private static final int LOGO_WIDTH = 450;
    private static final int LOGO_HEIGHT = 120;
    private static final String CONSTRAINT_GAP = "gapy 10 5";
    private static final String LOGO_PATH = AppConfig.get("logo.path");
    private final Frame frame;
    private JTextField txtEmail;
    private JPasswordField txtPassword;
    private JLabel lbTitle;
    private JLabel lbSubTitle;
    private JLabel lbEmailLabel;
    private JLabel lbPasswordLabel;
    private JLabel lbNoAccount;

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
        LOGGER.info("Initializing FormLogin...");
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
        lbTitle = new JLabel();
        lbTitle.putClientProperty(FlatClientProperties.STYLE, "font:bold +15;");
        lbTitle.setHorizontalAlignment(CENTER);
        add(lbTitle, "gapy 8 8");

        lbSubTitle = new JLabel();
        lbSubTitle.setHorizontalAlignment(CENTER);
        add(lbSubTitle, CONSTRAINT_GAP);
    }

    private void addFields() {
        lbEmailLabel = new JLabel();
        lbEmailLabel.putClientProperty(FlatClientProperties.STYLE, "font:bold;");
        add(lbEmailLabel, CONSTRAINT_GAP);

        txtEmail = createTextField();
        add(txtEmail);

        lbPasswordLabel = new JLabel();
        lbPasswordLabel.putClientProperty(FlatClientProperties.STYLE, "font:bold;");
        add(lbPasswordLabel, CONSTRAINT_GAP);

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
                        if (action == SimpleModalBorder.YES_OPTION) {
                            System.exit(0);
                        }
                    }), Constants.getSelectedOption());
        });

        InputValidator.applyEmailValidation(txtEmail);
        InputValidator.applyPasswordValidation(txtPassword);
    }

    private void signInActionPerformed(ActionEvent e) {
        LOGGER.info("Attempting to sign in with email: {}", txtEmail.getText());
        boolean valid = true;

        valid &= InputValidator.isNotEmpty(txtEmail, I18n.t("auth.formLogin.txtEmail.notEmpty"));
        valid &= InputValidator.isValidEmail(txtEmail, I18n.t("auth.formLogin.txtEmail.validEmail"));
        valid &= InputValidator.isNotEmpty(txtPassword, I18n.t("auth.formLogin.txtPassword.notEmpty"));
        valid &= InputValidator.isValidPassword(txtPassword, I18n.t("auth.formLogin.txtPassword.validPassword"));

        if (!valid) {
            LOGGER.warn("Validation failed for a login form");
            return;
        }

        User user = loginController.login(txtEmail.getText(), new String(txtPassword.getPassword()));
        if (user == null) {
            LOGGER.warn("Login failed for user: {}", txtEmail.getText());
            Toast.show(this, Toast.Type.ERROR,
                    I18n.t("auth.formLogin.typeError.credentialsIncorrect"),
                    ToastLocation.TOP_TRAILING,
                    Constants.getOption());
        } else {
            LOGGER.info("Login is successful for user: {}", user.getEmail());
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
                        (controller, action) -> {
                        }));
    }

    private @NotNull JPanel createAccountSection() {
        JPanel panel = new JPanel(new MigLayout("insets 0", "push[][]push"));
        lbNoAccount = new JLabel();
        lbNoAccount.putClientProperty(FlatClientProperties.STYLE, "foreground:$Label.disabledForeground;");
        panel.add(lbNoAccount);

        cmdCreateAccount = new JButton();
        cmdCreateAccount.putClientProperty(FlatClientProperties.STYLE,
                "foreground:$Component.accentColor;borderWidth:0;focusWidth:0;innerFocusWidth:0;background:null;");
        panel.add(cmdCreateAccount);
        return panel;
    }
    private @NotNull JTextField createTextField() {
        JTextField field = new JTextField();
        field.putClientProperty(FlatClientProperties.STYLE, "iconTextGap:10;");
        field.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, I18n.t("auth.formLogin.placeholder.email"));
        return field;
    }

    private @NotNull JPasswordField createPasswordField() {
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
        LOGGER.info("Language changed to: {}", I18n.getLocale());
        lbTitle.setText(I18n.t("auth.formLogin.welcomeBack.jLabel"));
        lbSubTitle.setText(I18n.t("auth.formLogin.logInToContinue"));
        lbEmailLabel.setText(I18n.t("auth.formLogin.email"));
        lbPasswordLabel.setText(I18n.t("auth.formLogin.password"));

        cmdSignIn.setText(I18n.t("auth.formLogin.JButton.signIn.JButton"));
        cmdCreateAccount.setText(I18n.t("auth.formLogin.JButton.register.JButton"));
        cmdExit.setText(I18n.t("auth.formLogin.JButton.exit.JButton"));

        lbNoAccount.setText(I18n.t("auth.formLogin.noAccount.jLabel"));

        txtEmail.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, I18n.t("auth.formLogin.placeholder.email"));
        txtPassword.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, I18n.t("auth.formLogin.placeholder.password"));
    }

}
