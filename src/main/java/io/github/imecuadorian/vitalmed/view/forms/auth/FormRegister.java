package io.github.imecuadorian.vitalmed.view.forms.auth;

import com.formdev.flatlaf.*;
import io.github.imecuadorian.vitalmed.controller.*;
import io.github.imecuadorian.vitalmed.factory.*;
import io.github.imecuadorian.vitalmed.i18n.*;
import io.github.imecuadorian.vitalmed.model.*;
import io.github.imecuadorian.vitalmed.model.country.*;
import io.github.imecuadorian.vitalmed.util.*;
import net.miginfocom.swing.*;
import raven.modal.*;
import raven.modal.component.*;
import raven.modal.toast.option.*;

import javax.swing.*;
import java.awt.*;
import java.time.*;
import java.util.*;
import java.util.concurrent.*;

public class FormRegister extends JPanel implements LanguageChangeListener {

    private final transient RegistrationController registrationController = new RegistrationController(
            ServiceFactory.getUSER_SERVICE()
    );

    private JLabel lblId = new JLabel();
    private JLabel lbContactDetail;
    private JLabel lblNames;
    private JLabel lblSurnames;
    private JLabel lblEmail;
    private JLabel lblPassword;
    private JLabel lblConfirmPassword;
    private JLabel lblCellphone;
    private JLabel lblPhone;
    private JLabel lblProvince;
    private JLabel lblCanton;
    private JLabel lblAddress;

    // Inputs
    private JTextField txtId = new JTextField();
    private JTextField txtName = new JTextField();
    private JTextField txtSurname = new JTextField();
    private JTextField txtEmail = new JTextField();
    private JPasswordField txtPassword = new JPasswordField();
    private JPasswordField txtConfirmPassword = new JPasswordField();
    private JTextField txtPhone = new JTextField();
    private JTextField txtCellphone = new JTextField();
    private JComboBox<Province> cbProvince = new JComboBox<>(Province.values());
    private JComboBox<String> cbCanton = new JComboBox<>();
    private JTextField txtAddress = new JTextField();
    private JTextArea textAreaTerms = new JTextArea();

    private JButton btnCancel;
    private JButton btnRegister;

    public FormRegister() {
        init();
        I18n.addListener(this);
    }

    private void init() {
        setLayout(new MigLayout("wrap 2, fillx, insets n 35 n 35, gapy 10", "[fill, 300]"));

        ImageIcon logo = new ImageIcon(Objects.requireNonNull(getClass().getResource("/io/github/imecuadorian/vitalmed/images/vitalmed-main-icon.png")));
        Image scaled = logo.getImage().getScaledInstance(30, 30, Image.SCALE_SMOOTH);

        lbContactDetail = new JLabel(I18n.t("auth.formRegister.patientsMedicalHistoryData"), new ImageIcon(scaled), JLabel.LEFT);
        lbContactDetail.putClientProperty(FlatClientProperties.STYLE, "font:bold +2;");
        lbContactDetail.setIconTextGap(10);
        add(lbContactDetail, "span 2, gapy 10 10, wrap");

        lblId.setText(I18n.t("auth.formRegister.id*"));
        add(lblId, "span 2");

        txtId.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, I18n.t("auth.formRegister.placeholder.id"));
        txtId.setToolTipText(I18n.t("auth.formRegister.idTooltip"));
        add(txtId, "span 2, growx");

        lblNames = new JLabel(I18n.t("auth.formRegister.names*"));
        lblNames.setToolTipText(I18n.t("auth.formRegister.nameTooltip"));
        lblSurnames = new JLabel(I18n.t("auth.formRegister.surnames*"));
        lblSurnames.setToolTipText(I18n.t("auth.formRegister.surnameTooltip"));
        add(lblNames);
        add(lblSurnames);

        txtName.setToolTipText(lblNames.getToolTipText());
        txtSurname.setToolTipText(lblSurnames.getToolTipText());
        txtName.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, I18n.t("auth.formRegister.placeholder.name"));
        txtSurname.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, I18n.t("auth.formRegister.placeholder.surname"));
        add(txtName);
        add(txtSurname);

        lblEmail = new JLabel(I18n.t("auth.formRegister.email"));
        add(lblEmail, "span 2");

        txtEmail.setToolTipText(I18n.t("auth.formRegister.emailTooltip"));
        txtEmail.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, I18n.t("auth.formRegister.placeholder.email"));
        add(txtEmail, "span 2, growx");

        lblPassword = new JLabel(I18n.t("auth.formRegister.password*"));
        lblConfirmPassword = new JLabel(I18n.t("auth.formRegister.confirmPassword*"));
        add(lblPassword);
        add(lblConfirmPassword);

        txtPassword.setToolTipText(I18n.t("auth.formRegister.passwordTooltip"));
        txtPassword.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, I18n.t("auth.formRegister.placeholder.password"));
        txtPassword.putClientProperty(FlatClientProperties.STYLE, "iconTextGap:10;showRevealButton:true;");

        txtConfirmPassword.setToolTipText(I18n.t("auth.formRegister.confirmPasswordTooltip"));
        txtConfirmPassword.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, I18n.t("auth.formRegister.placeholder.confirmPassword"));
        txtConfirmPassword.putClientProperty(FlatClientProperties.STYLE, "iconTextGap:10;showRevealButton:true;");
        add(txtPassword);
        add(txtConfirmPassword);

        lblCellphone = new JLabel(I18n.t("auth.formRegister.CellphoneNumber"));
        lblPhone = new JLabel(I18n.t("auth.formRegister.PhoneNumber"));
        add(lblCellphone);
        add(lblPhone);

        txtPhone.setToolTipText(I18n.t("auth.formRegister.phoneTooltip"));
        txtPhone.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, I18n.t("auth.formRegister.placeholder.phoneNumber"));
        txtCellphone.setToolTipText(I18n.t("auth.formRegister.cellphoneTooltip"));
        txtCellphone.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, I18n.t("auth.formRegister.placeholder.cellphoneNumber"));
        add(txtCellphone);
        add(txtPhone);

        lblProvince = new JLabel(I18n.t("auth.formRegister.province*"));
        lblCanton = new JLabel(I18n.t("auth.formRegister.canton*"));
        add(lblProvince, "split 2, span 2");
        add(lblCanton, "wrap");

        cbProvince.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, I18n.t("auth.formRegister.placeholder.province"));
        cbCanton.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, I18n.t("auth.formRegister.placeholder.canton"));
        add(cbProvince, "split 2, span 2, w ::300");
        add(cbCanton, "wrap");

        cbProvince.addActionListener(e -> {
            Province selected = (Province) cbProvince.getSelectedItem();
            if (selected != null) {
                cbCanton.removeAllItems();
                selected.getListCantons().forEach(cbCanton::addItem);
                if (selected == Province.PICHINCHA) {
                    cbCanton.setSelectedItem("Distrito Metropolitano de Quito");
                } else {
                    cbCanton.setSelectedIndex(0);
                }
            }
        });
        cbProvince.setSelectedItem(Province.PICHINCHA);

        lblAddress = new JLabel(I18n.t("auth.formRegister.address*"));
        add(lblAddress, "span 2");

        txtAddress.setToolTipText(I18n.t("auth.formRegister.addressTooltip"));
        txtAddress.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, I18n.t("auth.formRegister.placeholder.address"));
        add(txtAddress, "span 2, growx");

        textAreaTerms.setEnabled(false);
        textAreaTerms.setText(I18n.t("aut.formRegister.textTermsAndConditions"));
        textAreaTerms.putClientProperty(FlatClientProperties.STYLE, "border:0,0,0,0;font:-1;background:null;");
        add(textAreaTerms, "gapy 10 10,span 2");

        btnCancel = new JButton(I18n.t("auth.formRegister.JButton.cancel"));
        btnRegister = new JButton(I18n.t("auth.formRegister.JButton.register")) {
            @Override
            public boolean isDefaultButton() {
                return true;
            }
        };

        btnCancel.addActionListener(actionEvent -> {
            ModalBorderAction.getModalBorderAction(this).doAction(SimpleModalBorder.CANCEL_OPTION);
        });

        add(btnCancel, "grow 0");
        add(btnRegister, "grow 0, al trailing");

        InputValidator.applyCedulaValidation(txtId);
        InputValidator.applyNameValidation(txtName);
        InputValidator.applyNameValidation(txtSurname);
        InputValidator.applyEmailValidation(txtEmail);
        InputValidator.applyPhoneValidation(txtCellphone);
        InputValidator.applyPhoneValidation(txtPhone);

        btnRegister.addActionListener(_ -> {
            boolean valid = true;

            valid &= InputValidator.isNotEmpty(txtId, I18n.t("auth.formRegister.errorMessage.txtId.notEmpty"));
            valid &= InputValidator.isNotEmpty(txtName, I18n.t("auth.formRegister.errorMessage.txtName.notEmpty"));
            valid &= InputValidator.isNotEmpty(txtSurname, I18n.t("auth.formRegister.errorMessage.txtSurname.notEmpty"));
            valid &= InputValidator.isNotEmpty(txtEmail, I18n.t("auth.formRegister.errorMessage.txtEmail.notEmpty"));
            valid &= InputValidator.isNotEmpty(txtPassword, I18n.t("auth.formRegister.errorMessage.txtPassword.notEmpty"));
            valid &= InputValidator.isNotEmpty(txtConfirmPassword, I18n.t("auth.formRegister.errorMessage.txtConfirmPassword.notEmpty"));
            valid &= InputValidator.isNotEmpty(txtCellphone, I18n.t("auth.formRegister.errorMessage.txtCellphone.notEmpty"));

            valid &= InputValidator.isValidPassword(txtPassword, I18n.t("auth.formRegister.errorMessage.txtPassword.validPassword"));
            valid &= InputValidator.isValidPassword(txtConfirmPassword, I18n.t("auth.formRegister.errorMessage.txtConfirmPassword.validPassword"));
            valid &= InputValidator.isNotEmpty(txtAddress, I18n.t("auth.formRegister.errorMessage.txtAddress.notEmpty"));

            valid &= InputValidator.isValidName(txtName, I18n.t("auth.formRegister.errorMessage.txtName.validName"));
            valid &= InputValidator.isValidName(txtSurname, I18n.t("auth.formRegister.errorMessage.txtSurname.validName"));
            valid &= InputValidator.isValidCedula(txtId);
            valid &= InputValidator.isValidEmail(txtEmail, I18n.t("auth.formRegister.errorMessage.txtEmail.validEmail"));
            valid &= InputValidator.isValidPhone(txtCellphone, I18n.t("auth.formRegister.errorMessage.txtCellphone.validPhone"));

            String pass = new String(txtPassword.getPassword());
            String confirm = new String(txtConfirmPassword.getPassword());

            if (!pass.equals(confirm)) {
                valid = false;
                InputValidator.markAsError(txtConfirmPassword);
                Toast.show(
                        txtConfirmPassword,
                        Toast.Type.ERROR,
                        I18n.t("auth.formRegister.typeError.txtConfirmPassword"),
                        ToastLocation.TOP_TRAILING,
                        Constants.getOption()
                );
            }

            if (!valid) return;

            User patient = new User(
                    null,
                    txtId.getText(),
                    txtName.getText() + " " + txtSurname.getText(),
                    txtEmail.getText(),
                    txtPhone.getText().isBlank() ? "0000000000" : txtPhone.getText(),
                    txtCellphone.getText(),
                    cbProvince.getSelectedItem() + " - " +
                    cbCanton.getSelectedItem() + " - " +
                    txtAddress.getText(),
                    pass,
                    Role.PATIENT,
                    Instant.now(),
                    Instant.now()
            );

            registrationController.registerUser(patient)
                    .handle((_, throwable) -> {
                        if (throwable != null) {
                            Throwable cause = (throwable instanceof CompletionException)
                                    ? throwable.getCause()
                                    : throwable;
                            SwingUtilities.invokeLater(() ->
                                    Toast.show(
                                            this,
                                            Toast.Type.ERROR,
                                            cause.getMessage(),
                                            ToastLocation.TOP_TRAILING,
                                            Constants.getOption()
                                    )
                            );
                        } else {
                            SwingUtilities.invokeLater(() -> {
                                ModalBorderAction
                                        .getModalBorderAction(this)
                                        .doAction(SimpleModalBorder.OK_OPTION);
                                Toast.show(
                                        this,
                                        Toast.Type.SUCCESS,
                                        I18n.t("auth.formRegister.typeSuccess.registerPatient"),
                                        ToastLocation.TOP_TRAILING,
                                        Constants.getOption()
                                );
                            });
                        }
                        return null;
                    });
        });
    }

    @Override
    public void onLanguageChanged(ResourceBundle bundle) {
        lbContactDetail.setText(I18n.t("auth.formRegister.patientsMedicalHistoryData"));
        lblId.setText(I18n.t("auth.formRegister.id*"));
        lblNames.setText(I18n.t("auth.formRegister.names*"));
        lblSurnames.setText(I18n.t("auth.formRegister.surnames*"));
        lblEmail.setText(I18n.t("auth.formRegister.email"));
        lblPassword.setText(I18n.t("auth.formRegister.password*"));
        lblConfirmPassword.setText(I18n.t("auth.formRegister.confirmPassword*"));
        lblCellphone.setText(I18n.t("auth.formRegister.CellphoneNumber"));
        lblPhone.setText(I18n.t("auth.formRegister.PhoneNumber"));
        lblProvince.setText(I18n.t("auth.formRegister.province*"));
        lblCanton.setText(I18n.t("auth.formRegister.canton*"));
        lblAddress.setText(I18n.t("auth.formRegister.address*"));

        txtId.setToolTipText(I18n.t("auth.formRegister.idTooltip"));
        txtId.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, I18n.t("auth.formRegister.placeholder.id"));
        txtName.setToolTipText(I18n.t("auth.formRegister.nameTooltip"));
        txtName.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, I18n.t("auth.formRegister.placeholder.name"));
        txtSurname.setToolTipText(I18n.t("auth.formRegister.surnameTooltip"));
        txtSurname.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, I18n.t("auth.formRegister.placeholder.surname"));
        txtEmail.setToolTipText(I18n.t("auth.formRegister.emailTooltip"));
        txtEmail.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, I18n.t("auth.formRegister.placeholder.email"));
        txtPassword.setToolTipText(I18n.t("auth.formRegister.passwordTooltip"));
        txtPassword.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, I18n.t("auth.formRegister.placeholder.password"));
        txtConfirmPassword.setToolTipText(I18n.t("auth.formRegister.confirmPasswordTooltip"));
        txtConfirmPassword.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, I18n.t("auth.formRegister.placeholder.confirmPassword"));
        txtPhone.setToolTipText(I18n.t("auth.formRegister.phoneTooltip"));
        txtPhone.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, I18n.t("auth.formRegister.placeholder.phoneNumber"));
        txtCellphone.setToolTipText(I18n.t("auth.formRegister.cellphoneTooltip"));
        txtCellphone.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, I18n.t("auth.formRegister.placeholder.cellphoneNumber"));
        txtAddress.setToolTipText(I18n.t("auth.formRegister.addressTooltip"));
        txtAddress.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, I18n.t("auth.formRegister.placeholder.address"));

        cbProvince.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, I18n.t("auth.formRegister.placeholder.province"));
        cbCanton.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, I18n.t("auth.formRegister.placeholder.canton"));

        textAreaTerms.setText(I18n.t("aut.formRegister.textTermsAndConditions"));

        btnCancel.setText(I18n.t("auth.formRegister.JButton.cancel"));
        btnRegister.setText(I18n.t("auth.formRegister.JButton.register"));
    }

}
