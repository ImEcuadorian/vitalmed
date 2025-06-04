package io.github.imecuadorian.vitalmed.view.forms.auth;

import com.formdev.flatlaf.*;
import io.github.imecuadorian.vitalmed.controller.*;
import io.github.imecuadorian.vitalmed.factory.*;
import io.github.imecuadorian.vitalmed.i18n.*;
import io.github.imecuadorian.vitalmed.model.*;
import io.github.imecuadorian.vitalmed.model.country.*;
import io.github.imecuadorian.vitalmed.util.*;
import net.miginfocom.swing.*;
import org.slf4j.*;
import raven.modal.*;
import raven.modal.component.*;
import raven.modal.toast.option.*;

import javax.swing.*;
import java.util.*;

public class FormRegister extends JPanel implements io.github.imecuadorian.vitalmed.i18n.LanguageChangeListener {

    private static final Logger LOGGER = LoggerFactory.getLogger(FormRegister.class);
    private final transient RegistrationController registrationController = new RegistrationController(ServiceFactory.getPatientService());
    private JLabel lblId;
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
    private JTextField txtId;
    private JTextField txtName;
    private JTextField txtSurname;
    private JTextField txtEmail;
    private JTextField txtPhone;
    private JTextField txtCellphone;
    private JTextField txtAddress;
    private JPasswordField txtPassword;
    private JPasswordField txtConfirmPassword;

    private JComboBox<Province> cbProvince;
    private JComboBox<String> cbCanton;
    private JTextArea textAreaTerms;

    private JButton btnCancel;
    private JButton btnRegister;


    public FormRegister() {
        LOGGER.info("Initializing FormRegister UI");
        initComponents();
        initLayout();
        initListeners();
        applyValidation();
        I18n.addListener(this);
        updateTexts();
    }

    private void initComponents() {
        lblId = new JLabel();
        lbContactDetail = new JLabel();
        lblNames = new JLabel();
        lblSurnames = new JLabel();
        lblEmail = new JLabel();
        lblPassword = new JLabel();
        lblConfirmPassword = new JLabel();
        lblCellphone = new JLabel();
        lblPhone = new JLabel();
        lblProvince = new JLabel();
        lblCanton = new JLabel();
        lblAddress = new JLabel();

        txtId = new JTextField();
        txtName = new JTextField();
        txtSurname = new JTextField();
        txtEmail = new JTextField();
        txtPhone = new JTextField();
        txtCellphone = new JTextField();
        txtAddress = new JTextField();

        txtPassword = new JPasswordField();
        txtConfirmPassword = new JPasswordField();

        cbProvince = new JComboBox<>(Province.values());
        cbCanton = new JComboBox<>();

        textAreaTerms = new JTextArea();
        btnCancel = new JButton();
        btnRegister = new JButton() {
            @Override
            public boolean isDefaultButton() {
                return true;
            }
        };
    }

    private void initLayout() {
        setLayout(new MigLayout("wrap 2, fillx, insets n 35 n 35, gapy 10", "[fill, 300]"));
        add(lbContactDetail, "span 2, gapy 10 10, wrap");
        add(lblId, "span 2");
        add(txtId, "span 2, growx");
        add(lblNames);
        add(lblSurnames);
        add(txtName);
        add(txtSurname);
        add(lblEmail, "span 2");
        add(txtEmail, "span 2, growx");
        add(lblPassword);
        add(lblConfirmPassword);
        add(txtPassword);
        add(txtConfirmPassword);
        add(lblCellphone);
        add(lblPhone);
        add(txtCellphone);
        add(txtPhone);
        add(lblProvince, "split 2, span 2");
        add(lblCanton, "wrap");
        add(cbProvince, "split 2, span 2, w ::300");
        add(cbCanton, "wrap");
        add(lblAddress, "span 2");
        add(txtAddress, "span 2, growx");
        add(textAreaTerms, "gapy 10 10,span 2");
        add(btnCancel, "grow 0");
        add(btnRegister, "grow 0, al trailing");
    }

    private void initListeners() {
        cbProvince.addActionListener(e -> {
            Province selected = (Province) cbProvince.getSelectedItem();
            cbCanton.removeAllItems();
            if (selected != null) {
                selected.getListCantons().forEach(cbCanton::addItem);
                cbCanton.setSelectedItem(selected == Province.PICHINCHA ? "Distrito Metropolitano de Quito" : selected.getListCantons().getFirst());
            }
        });

        cbProvince.setSelectedItem(Province.PICHINCHA);

        btnCancel.addActionListener(actionEvent -> {
            LOGGER.info("Register cancelled by user");
            ModalBorderAction.getModalBorderAction(this).doAction(SimpleModalBorder.CANCEL_OPTION);
        });

        btnRegister.addActionListener(actionEvent -> {
            LOGGER.info("Attempting to register a new patient");
            if (!validateForm()) {
                LOGGER.warn("Form validation failed");
                return;
            }
            Patient patient = new Patient(
                    txtId.getText(),
                    txtName.getText() + " " + txtSurname.getText(),
                    txtEmail.getText(),
                    new String(txtPassword.getPassword()),
                    txtPhone.getText().isBlank() ? "0000000000" : txtPhone.getText(),
                    txtCellphone.getText(),
                    cbProvince.getSelectedItem() + " - " + cbCanton.getSelectedItem() + " - " + txtAddress.getText()
            );
            boolean success = registrationController.register(patient);
            if (success) {
                LOGGER.info("Patient registered successfully");
                ModalBorderAction.getModalBorderAction(this).doAction(SimpleModalBorder.OK_OPTION);
                Toast.show(this, Toast.Type.SUCCESS,
                        I18n.t("auth.formRegister.typeSuccess.registerPatient"), ToastLocation.TOP_TRAILING, Constants.getOption());
            } else {
                LOGGER.error("Patient registration failed");
                Toast.show(this, Toast.Type.ERROR,
                        I18n.t("auth.formRegister.typeError.registerPatient"), ToastLocation.TOP_TRAILING, Constants.getOption());
            }
        });
    }

    private boolean validateForm() {
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
            Toast.show(txtConfirmPassword, Toast.Type.ERROR,
                    I18n.t("auth.formRegister.typeError.txtConfirmPassword"), ToastLocation.TOP_TRAILING, Constants.getOption());
        }

        return valid;
    }

    private void applyValidation() {
        InputValidator.applyCedulaValidation(txtId);
        InputValidator.applyNameValidation(txtName);
        InputValidator.applyNameValidation(txtSurname);
        InputValidator.applyEmailValidation(txtEmail);
        InputValidator.applyPhoneValidation(txtCellphone);
        InputValidator.applyPhoneValidation(txtPhone);
    }

    @Override
    public void onLanguageChanged(ResourceBundle bundle) {
        LOGGER.debug("Language changed, updating UI texts in FormRegister");
        updateTexts();
    }

    private void updateTexts() {
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
