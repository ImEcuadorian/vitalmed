package io.github.imecuadorian.vitalmed.view.forms.doctor;

import com.formdev.flatlaf.FlatClientProperties;
import io.github.imecuadorian.vitalmed.controller.HistoryController;
import io.github.imecuadorian.vitalmed.factory.ServiceFactory;
import io.github.imecuadorian.vitalmed.i18n.*;
import io.github.imecuadorian.vitalmed.model.MedicalHistory;
import io.github.imecuadorian.vitalmed.model.Patient;
import io.github.imecuadorian.vitalmed.util.*;
import io.github.imecuadorian.vitalmed.view.system.Form;
import net.miginfocom.swing.MigLayout;
import javax.swing.*;
import java.util.ResourceBundle;

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
            new HistoryController(ServiceFactory.getHISTORY_SERVICE());

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
        btnBuscar  = new JButton(I18n.t("form.medHistory.button.search"));
        content.add(new JLabel(I18n.t("form.medHistory.label.cedula")));
        content.add(txtCedula,"growx, split 2");
        content.add(btnBuscar,"wrap");

        txtEdad = new JTextField();   txtSexo = new JTextField();
        txtAlergias = new JTextField(); txtEnfermedades = new JTextField();
        txtMedicamentos = new JTextField(); txtOperaciones = new JTextField();
        txtEspecialidad = new JTextField(); txtTratamiento = new JTextField();
        txtMedicamentoAsignado = new JTextField();

        addField(content,"form.medHistory.label.age", txtEdad);
        addField(content,"form.medHistory.label.sex", txtSexo);
        addField(content,"form.medHistory.label.allergies", txtAlergias);
        addField(content,"form.medHistory.label.preexisting", txtEnfermedades);
        addField(content,"form.medHistory.label.meds", txtMedicamentos);
        addField(content,"form.medHistory.label.ops", txtOperaciones);
        addField(content,"form.medHistory.label.specialty", txtEspecialidad);
        addField(content,"form.medHistory.label.treatment", txtTratamiento);
        addField(content,"form.medHistory.label.assignedMed", txtMedicamentoAsignado);

        btnGuardar = new JButton(I18n.t("form.medHistory.button.save"));
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
        p.add(new JLabel(I18n.t(key)));
        p.add(f,"growx, wrap");
    }

    private void onSearch() {
        String ced = txtCedula.getText().trim();
        if (ced.isEmpty()) {
            JOptionPane.showMessageDialog(this, I18n.t("form.medHistory.error.noCedula"));
            return;
        }
        controller.fetchByCedula(ced)
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
                });
    }

    private void onSave() {
        MedicalHistory mh = new MedicalHistory();
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
                });
    }

    @Override
    public void onLanguageChanged(ResourceBundle bundle) {
        lblTitle.setText(bundle.getString("form.medHistory.title"));
        text.setText(bundle.getString("form.medHistory.description"));
    }
}
