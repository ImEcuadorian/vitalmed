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

@SystemForm(name = "Historia Medica", description = "Historia Medica del Doctor", tags = {"historia medica"})
public class FormMedicalHistory extends Form implements LanguageChangeListener {

    private JLabel lblTitle;
    private JTextPane text;

    private JTextField txtCedula, txtEdad, txtSexo, txtAlergias, txtEnfermedades, txtMedicamentos, txtOperaciones;
    private JTextField txtEspecialidad, txtTratamiento, txtMedicamentoAsignado;
    private JButton btnBuscar, btnGuardar;
    private JPanel contentPanel;

    public FormMedicalHistory() {
        I18n.addListener(this);
    }

    @Override
    public void formInit() {
        setLayout(new MigLayout("fill, insets 10 10 10 10", "[fill]", "[][grow]"));
        add(createInfo(), "growx, wrap");

        contentPanel = new JPanel(new MigLayout("wrap 2, fill, insets n 35 n 35, gapy 10", "[fill,150]", "[fill]"));
        contentPanel.putClientProperty(FlatClientProperties.STYLE, "arc:20; background:$Table.background;");

        // Fila 1: Buscar cédula
        txtCedula = new JTextField();
        btnBuscar = new JButton("Buscar");
        contentPanel.add(new JLabel("Cédula del Paciente:"));
        contentPanel.add(txtCedula, "growx");
        contentPanel.add(new JLabel(""));
        contentPanel.add(btnBuscar, "wrap");

        // Datos básicos
        txtEdad = new JTextField();
        txtSexo = new JTextField();
        txtAlergias = new JTextField();
        txtEnfermedades = new JTextField();
        txtMedicamentos = new JTextField();
        txtOperaciones = new JTextField();
        txtEspecialidad = new JTextField();
        txtTratamiento = new JTextField();
        txtMedicamentoAsignado = new JTextField();

        addField("Edad:", txtEdad);
        addField("Sexo:", txtSexo);
        addField("Alergias:", txtAlergias);
        addField("Enfermedades preexistentes:", txtEnfermedades);
        addField("Medicamentos ingeridos:", txtMedicamentos);
        addField("Operaciones realizadas:", txtOperaciones);
        addField("Especialidad:", txtEspecialidad);
        addField("Tratamiento:", txtTratamiento);
        addField("Medicamento asignado:", txtMedicamentoAsignado);

        btnGuardar = new JButton("Guardar");
        contentPanel.add(btnGuardar, "span, center");

        add(contentPanel, "grow, push");

        // Eventos
        btnBuscar.addActionListener(e -> buscarPaciente());
        btnGuardar.addActionListener(e -> guardarHistoriaClinica());
    }

    private void addField(String label, JTextField field) {
        contentPanel.add(new JLabel(label));
        contentPanel.add(field, "growx, wrap");
    }

    private void buscarPaciente() {
        String cedula = txtCedula.getText().trim();
        if (cedula.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Ingrese la cédula del paciente");
            return;
        }
        // Simula carga de datos — Aquí conectas a DB o servicio
        txtEdad.setText("32");
        txtSexo.setText("Masculino");
        txtAlergias.setText("Ninguna");
        txtEnfermedades.setText("Asma");
        txtMedicamentos.setText("Ibuprofeno");
        txtOperaciones.setText("Apendicectomía");
        // Puedes añadir lógica real aquí usando tu controlador
    }

    private void guardarHistoriaClinica() {
        // Recoger valores
        String cedula = txtCedula.getText();
        String edad = txtEdad.getText();
        String sexo = txtSexo.getText();
        String alergias = txtAlergias.getText();
        String enfermedades = txtEnfermedades.getText();
        String medicamentos = txtMedicamentos.getText();
        String operaciones = txtOperaciones.getText();
        String especialidad = txtEspecialidad.getText();
        String tratamiento = txtTratamiento.getText();
        String medicamentoAsignado = txtMedicamentoAsignado.getText();

        System.out.println("Guardando historia clínica de: " + cedula);
        JOptionPane.showMessageDialog(this, "Historia clínica guardada correctamente");
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
        lblTitle.setText("Admnistrar Historia Medica");
        text.setText("En esta sección puedes gestionar los doctores registrados en el sistema. Puedes ver su información, editarla o eliminarla si es necesario.");
    }
}
