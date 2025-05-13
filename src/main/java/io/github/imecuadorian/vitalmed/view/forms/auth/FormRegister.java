package io.github.imecuadorian.vitalmed.view.forms.auth;

import com.formdev.flatlaf.*;
import io.github.imecuadorian.vitalmed.controller.*;
import io.github.imecuadorian.vitalmed.factory.*;
import io.github.imecuadorian.vitalmed.i18n.I18n;
import io.github.imecuadorian.vitalmed.model.*;
import io.github.imecuadorian.vitalmed.model.country.*;
import io.github.imecuadorian.vitalmed.util.*;
import net.miginfocom.swing.*;
import raven.modal.*;
import raven.modal.component.*;
import raven.modal.toast.option.*;

import javax.swing.*;
import java.awt.*;
import java.util.*;

public class FormRegister extends JPanel {

    private final RegistrationController registrationController = new RegistrationController(
            ServiceFactory.getPatientService()
    );
    public FormRegister() {
        init();
    }

    private void init() {
        setLayout(new MigLayout("wrap 2, fillx, insets n 35 n 35, gapy 10", "[fill, 300]"));

        ImageIcon logo = new ImageIcon(Objects.requireNonNull(getClass().getResource("/io/github/imecuadorian/vitalmed/images/vitalmed-main-icon.png")));
        Image scaled = logo.getImage().getScaledInstance(30, 30, Image.SCALE_SMOOTH);

        JLabel lbContactDetail = new JLabel(I18n.t("auth.formRegister.patientsMedicalHistoryData"), new ImageIcon(scaled), JLabel.LEFT);
        lbContactDetail.putClientProperty(FlatClientProperties.STYLE, "font:bold +2;");
        lbContactDetail.setIconTextGap(10); // espacio entre ícono y texto
        add(lbContactDetail, "span 2, gapy 10 10, wrap");


        add(new JLabel(I18n.t("auth.formRegister.id*")), "span 2");
        JTextField txtId = new JTextField();
        txtId.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, I18n.t("auth.formRegister.placeholder.id"));
        String toolTipId =
                I18n.t("auth.formRegister.idTooltip"); //Key for the tooltip text
        txtId.setToolTipText(toolTipId); // Set the tooltip text for the ID field
        add(txtId, "span 2, growx");

        add(new JLabel(I18n.t("auth.formRegister.names*")));
        String toolTipName =
                I18n.t("auth.formRegister.nameTooltip"); //Key for the tooltip text
        add(new JLabel(I18n.t("auth.formRegister.surnames*")));
        String toolTipSurname =
                I18n.t("auth.formRegister.surnameTooltip"); //Key for the tooltip text

        JTextField txtName = new JTextField();
        txtName.setToolTipText(toolTipName); // Set the tooltip text for the name field
        JTextField txtSurname = new JTextField();
        txtSurname.setToolTipText(toolTipSurname); // Set the tooltip text for the surname field
        txtName.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, I18n.t("auth.formRegister.placeholder.name"));
        txtSurname.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, I18n.t("auth.formRegister.placeholder.surname"));
        add(txtName);
        add(txtSurname);


        add(new JLabel(I18n.t("auth.formRegister.email")),"span 2");
        String toolTipEmail =
                I18n.t("auth.formRegister.emailTooltip"); //Key for the tooltip text
        JTextField txtEmail = new JTextField();
        txtEmail.setToolTipText(toolTipEmail); // Set the tooltip text for the email field
        txtEmail.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, I18n.t("auth.formRegister.placeholder.email"));
        add(txtEmail, "span 2, growx");

        add(new JLabel(I18n.t("auth.formRegister.password*")));
        add(new JLabel(I18n.t("auth.formRegister.confirmPassword*")));
        String toolTipPassword =
                I18n.t("auth.formRegister.passwordTooltip");
        String toolTipConfirmPassword =
                I18n.t("auth.formRegister.confirmPasswordTooltip");

        JPasswordField txtPassword = new JPasswordField();
        JPasswordField txtConfirmPassword = new JPasswordField();
        txtPassword.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, I18n.t("auth.formRegister.placeholder.password"));
        txtConfirmPassword.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, I18n.t("auth.formRegister.placeholder.confirmPassword"));
        txtPassword.putClientProperty(FlatClientProperties.STYLE, "iconTextGap:10;" +
                "showRevealButton:true;");
        txtPassword.setToolTipText(toolTipPassword);
        txtConfirmPassword.setToolTipText(toolTipConfirmPassword); // Set the tooltip text for the confirm password field
        txtConfirmPassword.putClientProperty(FlatClientProperties.STYLE, "iconTextGap:10;" +
                "showRevealButton:true;");
        add(txtPassword);
        add(txtConfirmPassword);

        add(new JLabel(I18n.t("auth.formRegister.CellphoneNumber")));
        String toolTipCellphone =
                I18n.t("auth.formRegister.cellphoneTooltip"); //Key for the tooltip text
        add(new JLabel(I18n.t("auth.formRegister.PhoneNumber")));
        String toolTipPhone =
                I18n.t("auth.formRegister.phoneTooltip"); //Key for the tooltip text
        JTextField txtPhone = new JTextField();
        txtPhone.setToolTipText(toolTipPhone); // Set the tooltip text for the phone field
        JTextField txtCellphone = new JTextField();
        txtCellphone.setToolTipText(toolTipCellphone); // Set the tooltip text for the cellphone field
        txtPhone.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, I18n.t("auth.formRegister.placeholder.phoneNumber"));
        txtCellphone.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, I18n.t("auth.formRegister.placeholder.cellphoneNumber"));
        add(txtCellphone);
        add(txtPhone);

        add(new JLabel(I18n.t("auth.formRegister.province*")), "split 2, span 2");
        add(new JLabel(I18n.t("auth.formRegister.canton*")), "wrap");
        JComboBox<Province> cbProvince = new JComboBox<>(Province.values());
        JComboBox<String> cbCanton = new JComboBox<>();
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

        add(new JLabel(I18n.t("auth.formRegister.address*")), "span 2");
        String toolTipAddress =
                I18n.t("auth.formRegister.addressTooltip"); //Key for the tooltip text
        JTextField txtAddress = new JTextField();
        txtAddress.setToolTipText(toolTipAddress); // Set the tooltip text for the address field
        txtAddress.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, I18n.t("auth.formRegister.placeholder.address"));
        add(txtAddress, "span 2, growx");

        JTextArea textArea = new JTextArea();
        textArea.setEnabled(false);
        textArea.setText("Al registrarte, aceptas los términos y condiciones de uso y la política de privacidad de Vitalmed.");
        textArea.putClientProperty(FlatClientProperties.STYLE, "border:0,0,0,0;" +
                                                               "font:-1;" +
                                                               "background:null;");
        add(textArea, "gapy 10 10,span 2");

        JButton btnCancel = new JButton("Cancelar");
        JButton btnRegister = new JButton("Registrar") {
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


        btnRegister.addActionListener(actionEvent -> {
            boolean valid = true;

            valid &= InputValidator.isNotEmpty(txtId, I18n.t("auth.formRegister.message.txtIdNotEmpty"));
            valid &= InputValidator.isNotEmpty(txtName, I18n.t("auth.formRegister.message.txtNameNotEmpty"));
            valid &= InputValidator.isNotEmpty(txtSurname, I18n.t("auth.formRegister.message.txtSurnameNotEmpty"));
            valid &= InputValidator.isNotEmpty(txtEmail, I18n.t("auth.formRegister.message.txtEmailNotEmpty"));
            valid &= InputValidator.isNotEmpty(txtPassword, I18n.t("auth.formRegister.message.txtPasswordNotEmpty"));
            valid &= InputValidator.isNotEmpty(txtConfirmPassword, I18n.t("auth.formRegister.message.txtConfirmPasswordNotEmpty"));
            valid &= InputValidator.isNotEmpty(txtCellphone, I18n.t("auth.formRegister.message.txtCellphoneNotEmpty"));

            valid &= InputValidator.isValidPassword(txtPassword, I18n.t("auth.formRegister.message.txtPasswordValid"));
            valid &= InputValidator.isValidPassword(txtConfirmPassword, "La confirmación no cumple los requisitos");
            valid &= InputValidator.isNotEmpty(txtAddress, "La dirección no puede estar vacía");

            valid &= InputValidator.isValidName(txtName, "Nombre inválido (máx. 2 palabras, iniciales con mayúscula)");
            valid &= InputValidator.isValidName(txtSurname, "Apellido inválido (máx. 2 palabras, iniciales con mayúscula)");
            valid &= InputValidator.isValidCedula(txtId);
            valid &= InputValidator.isValidEmail(txtEmail, "Correo inválido");
            valid &= InputValidator.isValidPhone(txtCellphone, "Número de celular inválido");

            String pass = new String(txtPassword.getPassword());
            String confirm = new String(txtConfirmPassword.getPassword());

            if (!pass.equals(confirm)) {
                valid = false;
                InputValidator.markAsError(txtConfirmPassword);
                Toast.show(txtConfirmPassword, Toast.Type.ERROR, "Las contraseñas no coinciden", ToastLocation.TOP_TRAILING, Constants.getOption());
            }

            if (valid) {
                Patient patient = new Patient(
                        txtId.getText(),
                        txtName.getText() + " " + txtSurname.getText(),
                        txtEmail.getText(),
                        pass,
                        txtPhone.getText().isBlank() ? "0000000000" : txtPhone.getText(),
                        txtCellphone.getText(),
                        cbProvince.getSelectedItem() + " - " + cbCanton.getSelectedItem() + " - " + txtAddress.getText()
                );
                if (registrationController.register(patient)) {
                    ModalBorderAction.getModalBorderAction(this).doAction(SimpleModalBorder.OK_OPTION);
                    Toast.show(this, Toast.Type.SUCCESS, "Registro exitoso", ToastLocation.TOP_TRAILING, Constants.getOption());
                } else {
                    Toast.show(this, Toast.Type.ERROR, "Error al registrar el paciente", ToastLocation.TOP_TRAILING, Constants.getOption());
                }
            }
        });



    }
}
