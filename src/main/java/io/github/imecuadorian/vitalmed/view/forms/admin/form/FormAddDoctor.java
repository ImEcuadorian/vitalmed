package io.github.imecuadorian.vitalmed.view.forms.admin.form;

import com.formdev.flatlaf.*;
import io.github.imecuadorian.vitalmed.controller.*;
import io.github.imecuadorian.vitalmed.factory.*;
import io.github.imecuadorian.vitalmed.i18n.*;
import io.github.imecuadorian.vitalmed.model.*;
import io.github.imecuadorian.vitalmed.model.country.*;
import io.github.imecuadorian.vitalmed.repository.impl.*;
import io.github.imecuadorian.vitalmed.repository.interfaces.*;
import io.github.imecuadorian.vitalmed.util.*;
import net.miginfocom.swing.*;
import raven.modal.*;
import raven.modal.component.*;
import raven.modal.toast.option.*;

import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.util.List;

public class FormAddDoctor extends JPanel implements LanguageChangeListener {


    private static final AdminDashboardController ADMIN_DASHBOARD_CONTROLLER = new AdminDashboardController(ServiceFactory.getADMIN_SERVICE(),
            ServiceFactory.getUSER_SERVICE());
    private final Runnable success;

    public FormAddDoctor(Runnable success) {
        this.success = success;
        init();
        I18n.addListener(this);
    }

    private void init() {
        setLayout(new MigLayout("wrap 2, fillx, insets n 35 n 35, gapy 10", "[fill, 300]"));

        ImageIcon logo = new ImageIcon(Objects.requireNonNull(getClass().getResource("/io/github/imecuadorian/vitalmed/images/vitalmed-main-icon.png")));
        Image scaled = logo.getImage().getScaledInstance(30, 30, Image.SCALE_SMOOTH);

        JLabel lbContactDetail = new JLabel(I18n.t("form.form.formAddDoctor.newData"), new ImageIcon(scaled), JLabel.LEFT);
        lbContactDetail.putClientProperty(FlatClientProperties.STYLE, "font:bold +2;");
        lbContactDetail.setIconTextGap(10);
        add(lbContactDetail, "span 2, gapy 10 10, wrap");


        add(new JLabel(I18n.t("form.admin.form.formAddDoctor.id*")), "span 2");
        JTextField txtId = new JTextField();
        txtId.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, I18n.t("form.admin.form.formAddDoctor.id.placeHolder"));
        add(txtId, "span 2, growx");

        add(new JLabel(I18n.t("form.admin.form.formAddDoctor.name*")));
        add(new JLabel(I18n.t("form.admin.form.formAddDoctor.surname*")));

        JTextField txtName = new JTextField();
        JTextField txtSurname = new JTextField();
        txtName.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, I18n.t("form.admin.form.formAddDoctor.name.placeHolder"));
        txtSurname.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, I18n.t("form.admin.form.formAddDoctor.surname.placeHolder"));
        add(txtName);
        add(txtSurname);


        add(new JLabel(I18n.t("form.admin.form.formAddDoctor.email*")), "span 2");
        JTextField txtEmail = new JTextField();
        txtEmail.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, I18n.t("form.admin.form.formAddDoctor.email.placeHolder"));
        add(txtEmail, "span 2, growx");

        add(new JLabel(I18n.t("form.admin.form.formAddDoctor.password*")));
        add(new JLabel(I18n.t("form.admin.form.formAddDoctor.confirmPassword*")));
        String toolTipPassword = I18n.t("form.admin.form.formAddDoctor.password.toolTip");

        JPasswordField txtPassword = new JPasswordField();
        JPasswordField txtConfirmPassword = new JPasswordField();
        txtPassword.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, I18n.t("form.admin.form.formAddDoctor.password.placeHolder"));
        txtConfirmPassword.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, I18n.t("form.admin.form.formAddDoctor.confirmPassword.placeHolder"));
        txtPassword.putClientProperty(FlatClientProperties.STYLE, "iconTextGap:10;" +
                                                                  "showRevealButton:true;");
        txtPassword.setToolTipText(toolTipPassword);
        txtConfirmPassword.putClientProperty(FlatClientProperties.STYLE, "iconTextGap:10;" +
                                                                         "showRevealButton:true;");
        add(txtPassword);
        add(txtConfirmPassword);

        add(new JLabel(I18n.t("form.admin.form.formAddDoctor.cellphone*")));
        add(new JLabel(I18n.t("form.admin.form.formAddDoctor.phone*")));
        JTextField txtPhone = new JTextField();
        JTextField txtCellphone = new JTextField();
        txtPhone.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, I18n.t("form.admin.form.formAddDoctor.phone.placeHolder"));
        txtCellphone.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, I18n.t("form.admin.form.formAddDoctor.cellphone.placeHolder"));
        add(txtCellphone);
        add(txtPhone);

        add(new JLabel(I18n.t("form.admin.form.formAddDoctor.province*")), "split 2, span 2");
        add(new JLabel(I18n.t("form.admin.form.formAddDoctor.canton*")), "wrap");
        JComboBox<Province> cbProvince = new JComboBox<>(Province.values());
        JComboBox<String> cbCanton = new JComboBox<>();
        cbProvince.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, I18n.t("form.admin.form.formAddDoctor.province.placeHolder"));
        cbCanton.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, I18n.t("form.admin.form.formAddDoctor.canton.placeHolder"));
        add(cbProvince, "split 2, span 2, w ::300");
        add(cbCanton, "wrap");

        cbProvince.addActionListener(e -> {
            Province selected = (Province) cbProvince.getSelectedItem();
            if (selected != null) {
                cbCanton.removeAllItems();
                selected.getListCantons().forEach(cbCanton::addItem);

                if (selected == Province.PICHINCHA) {
                    cbCanton.setSelectedItem(I18n.t("form.admin.form.formAddDoctor.districtQuito.selectedIterm"));
                } else {
                    cbCanton.setSelectedIndex(0);
                }
            }
        });

        cbProvince.setSelectedItem(Province.PICHINCHA);

        add(new JLabel(I18n.t("form.admin.form.formAddDoctor.address*")), "span 2");
        JTextField txtAddress = new JTextField();
        txtAddress.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, I18n.t("form.admin.form.formAddDoctor.address.placeHolder"));
        add(txtAddress, "span 2, growx, wrap");

        JLabel lbSpecialty = new JLabel(I18n.t("form.admin.form.formAddDoctor.specialty*"));
        lbSpecialty.putClientProperty(FlatClientProperties.STYLE, "font:bold +2;");
        lbSpecialty.setIconTextGap(10); // espacio entre ícono y texto
        add(lbSpecialty, "span 2, gapy 10 10, wrap");

        JComboBox<Specialty> cbSpecialty = new JComboBox<>();
        cbSpecialty.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, I18n.t("form.admin.form.formAddDoctor.specialty.placeHolder"));
        cbSpecialty.setEnabled(false);
        cbSpecialty.addItem(new Specialty(-1, "Cargando especialidades..."));

        ADMIN_DASHBOARD_CONTROLLER.getSpecialties().thenAccept(specialties -> {
            SwingUtilities.invokeLater(() -> {
                cbSpecialty.removeAllItems();
                List<Specialty> sortedSpecialties = specialties.stream()
                        .sorted(Comparator.comparing(Specialty::name, String.CASE_INSENSITIVE_ORDER))
                        .toList();
                for (Specialty s : sortedSpecialties) {
                    cbSpecialty.addItem(s);
                }
                cbSpecialty.setEnabled(true);
            });
        }).exceptionally(ex -> {
            SwingUtilities.invokeLater(() -> {
                cbSpecialty.removeAllItems();
                cbSpecialty.addItem(new Specialty(-1, I18n.t("error.loading.specialties")));
                cbSpecialty.setEnabled(false);
            });
            ex.printStackTrace();
            return null;
        });

        add(cbSpecialty, "span 2, growx, wrap");

        JTextArea textArea = new JTextArea();
        textArea.setEnabled(false);
        textArea.setText(I18n.t("form.admin.form.formAddDoctor.textTermsAndConditions"));
        textArea.putClientProperty(FlatClientProperties.STYLE, "border:0,0,0,0;" +
                                                               "font:-1;" +
                                                               "background:null;");
        add(textArea, "gapy 10 10,span 2, wrap");

        JButton btnCancel = new JButton(I18n.t("form.admin.form.formAddDoctor.btnCancel"));
        JButton btnRegister = new JButton(I18n.t("form.admin.form.formAddDoctor.btnRegister")) {
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

            valid &= InputValidator.isNotEmpty(txtId, I18n.t("form.admin.form.formAddDoctor.errorMessage.txtId.notEmpty"));
            valid &= InputValidator.isNotEmpty(txtName, I18n.t("form.admin.form.formAddDoctor.errorMessage.txtName.notEmpty"));
            valid &= InputValidator.isNotEmpty(txtSurname, I18n.t("form.admin.form.formAddDoctor.errorMessage.txtSurname.notEmpty"));
            valid &= InputValidator.isNotEmpty(txtEmail, I18n.t("form.admin.form.formAddDoctor.errorMessage.txtEmail.notEmpty"));
            valid &= InputValidator.isNotEmpty(txtPassword, I18n.t("form.admin.form.formAddDoctor.errorMessage.txtPassword.notEmpty"));
            valid &= InputValidator.isNotEmpty(txtConfirmPassword, I18n.t("form.admin.form.formAddDoctor.errorMessage.txtConfirmPassword.notEmpty"));
            valid &= InputValidator.isNotEmpty(txtCellphone, I18n.t("form.admin.form.formAddDoctor.errorMessage.txtCellphone.notEmpty"));

            valid &= InputValidator.isValidPassword(txtPassword, I18n.t("form.admin.form.formAddDoctor.errorMessage.txtPassword.validPassword"));
            valid &= InputValidator.isValidPassword(txtConfirmPassword, I18n.t("form.admin.form.formAddDoctor.errorMessage.txtConfirmPassword.validPassword"));
            valid &= InputValidator.isNotEmpty(txtAddress, I18n.t("form.admin.form.formAddDoctor.errorMessage.txtAddress.notEmpty"));

            valid &= InputValidator.isValidName(txtName, I18n.t("form.admin.form.formAddDoctor.errorMessage.txtName.validName"));
            valid &= InputValidator.isValidName(txtSurname, I18n.t("form.admin.form.formAddDoctor.errorMessage.txtSurname.validName"));
            valid &= InputValidator.isValidCedula(txtId);
            valid &= InputValidator.isValidEmail(txtEmail, I18n.t("form.admin.form.formAddDoctor.errorMessage.txtEmail.validEmail"));
            valid &= InputValidator.isValidPhone(txtCellphone, I18n.t("form.admin.form.formAddDoctor.errorMessage.txtCellphone.validPhone"));

            String pass = new String(txtPassword.getPassword());
            String confirm = new String(txtConfirmPassword.getPassword());

            if (!pass.equals(confirm)) {
                valid = false;
                InputValidator.markAsError(txtConfirmPassword);
                Toast.show(txtConfirmPassword, Toast.Type.ERROR, I18n.t("form.admin.form.formAddDoctor.typeError.passwords"), ToastLocation.TOP_TRAILING, Constants.getOption());
            }

            if (!valid) return;

            String address = cbProvince.getSelectedItem() + " - " + cbCanton.getSelectedItem() + " - " + txtAddress.getText();

            User user = User.builder()
                    .cedula(txtId.getText())
                    .fullName(txtName.getText() + " " + txtSurname.getText())
                    .email(txtEmail.getText())
                    .passwordHash(pass)
                    .phone(txtPhone.getText().isBlank() ? "0000000000" : txtPhone.getText())
                    .cell(txtCellphone.getText())
                    .address(address)
                    .role(Role.DOCTOR)
                    .build();

            Specialty selectedSpecialty = (Specialty) cbSpecialty.getSelectedItem();
            if (selectedSpecialty == null || selectedSpecialty.id() == -1) {
                Toast.show(this, Toast.Type.ERROR, "Debe seleccionar una especialidad", ToastLocation.TOP_TRAILING, Constants.getOption());
                return;
            }

            Doctor doctor = Doctor.builder()
                    .user(user)
                    .specialty(selectedSpecialty)
                    .build();


            ADMIN_DASHBOARD_CONTROLLER.createDoctor(doctor).thenAccept(result -> {
                if (result != null) {
                    SwingUtilities.invokeLater(() -> {
                        Toast.show(this, Toast.Type.SUCCESS, "Doctor registrado correctamente", ToastLocation.TOP_TRAILING, Constants.getOption());
                        success.run(); // ← actualiza tabla del formulario padre
                        ModalBorderAction.getModalBorderAction(this).doAction(SimpleModalBorder.CLOSE_OPTION);
                    });
                } else {
                    SwingUtilities.invokeLater(() -> {
                        Toast.show(this, Toast.Type.ERROR, "El doctor ya existe", ToastLocation.TOP_TRAILING, Constants.getOption());
                    });
                }
            }).exceptionally(ex -> {
                ex.printStackTrace();
                SwingUtilities.invokeLater(() -> {
                    Toast.show(this, Toast.Type.ERROR, "Error al registrar doctor", ToastLocation.TOP_TRAILING, Constants.getOption());
                });
                return null;
            });

        });

    }

    @Override
    public void onLanguageChanged(ResourceBundle bundle) {

    }
}
