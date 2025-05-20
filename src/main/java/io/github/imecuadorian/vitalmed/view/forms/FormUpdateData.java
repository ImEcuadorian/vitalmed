package io.github.imecuadorian.vitalmed.view.forms;

import com.formdev.flatlaf.*;
import io.github.imecuadorian.vitalmed.controller.*;
import io.github.imecuadorian.vitalmed.factory.*;
import io.github.imecuadorian.vitalmed.i18n.*;
import io.github.imecuadorian.vitalmed.model.*;
import io.github.imecuadorian.vitalmed.model.country.*;
import io.github.imecuadorian.vitalmed.util.*;
import io.github.imecuadorian.vitalmed.view.menu.*;
import io.github.imecuadorian.vitalmed.view.system.*;
import net.miginfocom.swing.*;
import raven.modal.*;
import raven.modal.component.*;
import raven.modal.toast.option.*;

import javax.swing.*;
import java.awt.*;
import java.util.*;

public class FormUpdateData extends Form implements LanguageChangeListener {

    private JLabel lblTitle;
    private JTextPane text;

    private final AdminDashboardController adminDashboardController = new AdminDashboardController(
            ServiceFactory.getAdminService()
    );

    public FormUpdateData() {
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

        JPanel contentPanel = new JPanel(new MigLayout("wrap 2, fillx, insets n 35 n 35, gapy 10", "[fill, 300]"));
        contentPanel.putClientProperty(FlatClientProperties.STYLE, "arc:20; background:$Table.background;");

        ImageIcon logo = new ImageIcon(Objects.requireNonNull(getClass().getResource("/io/github/imecuadorian/vitalmed/images/vitalmed-main-icon.png")));
        Image scaled = logo.getImage().getScaledInstance(30, 30, Image.SCALE_SMOOTH);

        JLabel lbContactDetail = new JLabel(I18n.t("form.form.formAddDoctor.newData"), new ImageIcon(scaled), JLabel.LEFT);
        lbContactDetail.putClientProperty(FlatClientProperties.STYLE, "font:bold +2;");
        lbContactDetail.setIconTextGap(10);
        contentPanel.add(lbContactDetail, "span 2, gapy 10 10, wrap");


        contentPanel.add(new JLabel(I18n.t("form.admin.form.formAddDoctor.email*")), "span 2");
        JTextField txtEmail = new JTextField();
        txtEmail.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, I18n.t("form.admin.form.formAddDoctor.email.placeHolder"));
        contentPanel.add(txtEmail, "span 2, growx");


        contentPanel.add(new JLabel(I18n.t("form.admin.form.formAddDoctor.cellphone*")));
        contentPanel.add(new JLabel(I18n.t("form.admin.form.formAddDoctor.phone*")));
        JTextField txtPhone = new JTextField();
        JTextField txtCellphone = new JTextField();
        txtPhone.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, I18n.t("form.admin.form.formAddDoctor.phone.placeHolder"));
        txtCellphone.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, I18n.t("form.admin.form.formAddDoctor.cellphone.placeHolder"));
        contentPanel.add(txtCellphone);
        contentPanel.add(txtPhone);

        contentPanel.add(new JLabel(I18n.t("form.admin.form.formAddDoctor.province*")), "split 2, span 2");
        contentPanel.add(new JLabel(I18n.t("form.admin.form.formAddDoctor.canton*")), "wrap");
        JComboBox<Province> cbProvince = new JComboBox<>(Province.values());
        JComboBox<String> cbCanton = new JComboBox<>();
        cbProvince.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, I18n.t("form.admin.form.formAddDoctor.province.placeHolder"));
        cbCanton.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, I18n.t("form.admin.form.formAddDoctor.canton.placeHolder"));
        contentPanel.add(cbProvince, "split 2, span 2, w ::300");
        contentPanel.add(cbCanton, "wrap");

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

        contentPanel.add(new JLabel(I18n.t("form.admin.form.formAddDoctor.address*")), "span 2");
        JTextField txtAddress = new JTextField();
        txtAddress.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, I18n.t("form.admin.form.formAddDoctor.address.placeHolder"));
        contentPanel.add(txtAddress, "span 2, growx, wrap");


        JLabel lbSpecialty = new JLabel(I18n.t("form.admin.form.formAddDoctor.specialty*"));
        lbSpecialty.putClientProperty(FlatClientProperties.STYLE, "font:bold +2;");
        lbSpecialty.setIconTextGap(10); // espacio entre ícono y texto
        contentPanel.add(lbSpecialty, "span 2, gapy 10 10, wrap");


        JComboBox<String> cbSpecialty = new JComboBox<>();
        cbSpecialty.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, I18n.t("form.admin.form.formAddDoctor.specialty.placeHolder"));
        cbSpecialty.addItem(I18n.t("form.admin.form.formAddDoctor.generalMedicine.selectedIterm"));
        cbSpecialty.addItem(I18n.t("form.admin.form.formAddDoctor.pediatrics.selectedIterm"));
        cbSpecialty.addItem(I18n.t("form.admin.form.formAddDoctor.gynecology.selectedIterm"));
        cbSpecialty.addItem(I18n.t("form.admin.form.formAddDoctor.dentistry.selectedIterm"));
        cbSpecialty.addItem(I18n.t("form.admin.form.formAddDoctor.dermatology.selectedIterm"));
        cbSpecialty.addItem(I18n.t("form.admin.form.formAddDoctor.laboratory.selectedIterm"));

        contentPanel.add(cbSpecialty, "span 2, growx, wrap");

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

        contentPanel.add(btnCancel, "grow 0");
        contentPanel.add(btnRegister, "grow 0, al trailing");

        InputValidator.applyEmailValidation(txtEmail);
        InputValidator.applyPhoneValidation(txtCellphone);
        InputValidator.applyPhoneValidation(txtPhone);


        btnRegister.addActionListener(actionEvent -> {
            boolean valid = true;

            valid &= InputValidator.isNotEmpty(txtEmail, I18n.t("form.admin.form.formAddDoctor.errorMessage.txtEmail.notEmpty"));
            valid &= InputValidator.isNotEmpty(txtCellphone, I18n.t("form.admin.form.formAddDoctor.errorMessage.txtCellphone.notEmpty"));

            valid &= InputValidator.isNotEmpty(txtAddress, I18n.t("form.admin.form.formAddDoctor.errorMessage.txtAddress.notEmpty"));

            valid &= InputValidator.isValidEmail(txtEmail, I18n.t("form.admin.form.formAddDoctor.errorMessage.txtEmail.validEmail"));
            valid &= InputValidator.isValidPhone(txtCellphone, I18n.t("form.admin.form.formAddDoctor.errorMessage.txtCellphone.validPhone"));

        });
        add(contentPanel, "grow");
    }

    @Override
    public void onLanguageChanged(ResourceBundle bundle) {
        lblTitle.setText("Actualizar Datos");
        text.setText("En esta sección puedes actualizar tus datos personales, como tu nombre, dirección y número de teléfono. Asegúrate de que la información sea precisa y esté actualizada.");
    }
}
