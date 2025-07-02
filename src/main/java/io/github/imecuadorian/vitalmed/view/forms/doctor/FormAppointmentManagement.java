package io.github.imecuadorian.vitalmed.view.forms.doctor;

import com.formdev.flatlaf.*;
import io.github.imecuadorian.vitalmed.controller.*;
import io.github.imecuadorian.vitalmed.factory.*;
import io.github.imecuadorian.vitalmed.i18n.*;
import io.github.imecuadorian.vitalmed.util.*;
import io.github.imecuadorian.vitalmed.view.system.*;
import net.miginfocom.swing.*;

import javax.swing.*;
import java.util.*;

@SystemForm(name = "Gestión de Citas", description = "Gestión de citas médicas", tags = {"citas", "gestión"})
public class FormAppointmentManagement extends Form implements LanguageChangeListener {

    private JLabel lblTitle;
    private JTextPane text;

    public FormAppointmentManagement() {
        init();
        I18n.addListener(this);
    }

    private void init() {
        setLayout(new MigLayout("fill, insets 10 10 10 10", "[fill]", "[][grow]"));
        add(createInfo(), "growx, wrap");

        JPanel contentPanel = new JPanel(new MigLayout("wrap 2, fill, insets n 35 n 35, gapy 10", "[fill]", "[fill]"));
        contentPanel.putClientProperty(FlatClientProperties.STYLE, "arc:20; background:$Table.background;");
        add(contentPanel, "grow, push");

        contentPanel.add(new JLabel("Gestión de citas médicas aquí...")); // Aquí se agregará tabla, filtro, botones, etc.
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

    @Override
    public void onLanguageChanged(ResourceBundle bundle) {
        lblTitle.setText("Gestión de Citas Médicas");
        text.setText("En esta sección puedes gestionar todas las citas médicas registradas en el sistema. Puedes filtrarlas por estado, doctor o especialidad, y actualizarlas si es necesario.");
    }
}