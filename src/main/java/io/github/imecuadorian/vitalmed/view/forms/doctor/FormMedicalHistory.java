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

@SystemForm(
        name = "Historia Médica",
        description = "Historia Clínica del Paciente",
        tags = {"historia","médica"}
)
public class FormMedicalHistory extends Form implements LanguageChangeListener {
    private JLabel lblTitle;
    private JTextPane text;
    private JTextField txtCedula, txtEdad, txtSexo, txtAlergias,
            txtEnfermedades, txtMedicamentos, txtOperaciones,
            txtEspecialidad, txtTratamiento, txtMedicamentoAsignado;
    private JButton btnBuscar, btnGuardar;

    private final HistoryController controller =
            new HistoryController(ServiceFactory.getJDBC_HISTORY_SERVICE());

    @Override
    public void formInit() {
        setLayout(new MigLayout("fill, insets 10","[fill]","[][grow]"));
        add(createInfo(), "growx, wrap");

        JPanel content = new JPanel(new MigLayout("wrap 2, gap 10","[right][grow]"));
        content.putClientProperty(
                FlatClientProperties.STYLE,
                "arc:20; background:$Table.background;"
        );

        txtCedula = new JTextField();
        btnBuscar  = new JButton("Buscar");
        content.add(new JLabel("Cedula:"));
        content.add(txtCedula,"growx, split 2");
        content.add(btnBuscar,"wrap");

        txtEdad = new JTextField();   txtSexo = new JTextField();
        txtAlergias = new JTextField(); txtEnfermedades = new JTextField();
        txtMedicamentos = new JTextField(); txtOperaciones = new JTextField();
        txtEspecialidad = new JTextField(); txtTratamiento = new JTextField();
        txtMedicamentoAsignado = new JTextField();

        addField(content,"Edad", txtEdad);
        addField(content,"Sexo", txtSexo);
        addField(content,"Alergias", txtAlergias);
        addField(content,"Enfermedades", txtEnfermedades);
        addField(content,"Medicamentos", txtMedicamentos);
        addField(content,"Espcialidad", txtEspecialidad);
        addField(content,"Tratamiento", txtTratamiento);
        addField(content,"Medicamento", txtMedicamentoAsignado);

        btnGuardar = new JButton("Guardar");
        content.add(btnGuardar, "span, center");

        add(content, "grow, push");

        // Listeners
        btnBuscar.addActionListener(e -> onSearch());
        btnGuardar.addActionListener(e -> onSave());
    }

    private JPanel createInfo() {
        JPanel p = new JPanel(new MigLayout("fillx, wrap","[grow]"));
        lblTitle = new JLabel(); text = new JTextPane();
        lblTitle.putClientProperty(FlatClientProperties.STYLE,"font:bold +3");
        text.setEditable(false); text.setOpaque(false); text.setBorder(null);
        p.add(lblTitle);
        p.add(text);
        return p;
    }

    private void addField(JPanel p, String key, JTextField f) {
        p.add(new JLabel(key));
        p.add(f,"growx, wrap");
    }

    private void onSearch() {
        String ced = txtCedula.getText().trim();
        if (ced.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No se ha ingresado una cédula válida.");
            return;
        }
        /*controller.fetchByCedula(ced)
                .thenAccept(hist -> SwingUtilities.invokeLater(() -> {
                    Patient pat = hist.getPatient();
                    txtEdad.setText(String.valueOf(pat.getAge()));
                    txtSexo.setText(pat.getSex());
                    txtAlergias.setText(hist.getAllergies());
                    txtEnfermedades.setText(hist.getPreexisting());
                    txtMedicamentos.setText(hist.getMedications());
                    txtOperaciones.setText(hist.getOperations());
                    txtEspecialidad.setText(hist.getSpecialty());
                    txtTratamiento.setText(hist.getTreatment());
                    txtMedicamentoAsignado.setText(hist.getAssignedMedication());
                }))
                .exceptionally(ex -> {
                    JOptionPane.showMessageDialog(this, I18n.t("form.medHistory.error.load"));
                    return null;
                });*/
    }

    private void onSave() {
       /* MedicalHistory mh = new MedicalHistory();
        mh.setCedula(txtCedula.getText().trim());
        mh.setAllergies(txtAlergias.getText());
        mh.setPreexisting(txtEnfermedades.getText());
        mh.setMedications(txtMedicamentos.getText());
        mh.setOperations(txtOperaciones.getText());
        mh.setSpecialty(txtEspecialidad.getText());
        mh.setTreatment(txtTratamiento.getText());
        mh.setAssignedMedication(txtMedicamentoAsignado.getText());

        controller.save(mh)
                .thenRun(() -> SwingUtilities.invokeLater(() ->
                        JOptionPane.showMessageDialog(this, I18n.t("form.medHistory.success.save"))
                ))
                .exceptionally(ex -> {
                    JOptionPane.showMessageDialog(this, I18n.t("form.medHistory.error.save"));
                    return null;
                });*/
    }

    @Override
    public void onLanguageChanged(ResourceBundle bundle) {
        lblTitle.setText("Medical History");
        text.setText("Enter patient details to view or edit their medical history.");
    }
}
