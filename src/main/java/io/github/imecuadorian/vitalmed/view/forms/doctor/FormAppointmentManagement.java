package io.github.imecuadorian.vitalmed.view.forms.doctor;

import com.formdev.flatlaf.FlatClientProperties;
import io.github.imecuadorian.vitalmed.controller.AppointmentController;
import io.github.imecuadorian.vitalmed.factory.ServiceFactory;
import io.github.imecuadorian.vitalmed.i18n.*;
import io.github.imecuadorian.vitalmed.model.Appointment;
import io.github.imecuadorian.vitalmed.model.Doctor;
import io.github.imecuadorian.vitalmed.util.*;
import io.github.imecuadorian.vitalmed.view.menu.*;
import io.github.imecuadorian.vitalmed.view.system.Form;
import net.miginfocom.swing.MigLayout;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDate;
import java.util.List;
import java.util.ResourceBundle;

@SystemForm(
        name = "Gestión de Citas",
        description = "Gestión de citas médicas",
        tags = {"citas","gestión"}
)
public class FormAppointmentManagement extends Form implements LanguageChangeListener {
    private JLabel lblTitle;
    private JTextPane text;
    private JComboBox<String> cmbFilter;
    private JTable tblAppointments;
    private DefaultTableModel model;
    private JButton btnMarkAttended, btnRefresh;

    // Controlador de cita
    private final AppointmentController controller =
            new AppointmentController(ServiceFactory.getAPPOINTMENT_SERVICE());

    private Doctor currentDoctor;

    @Override
    public void formInit() {
        setLayout(new MigLayout("fill, insets 10","[grow]","[][grow][]"));
        add(createInfo(), "growx, wrap");

        JPanel top = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 4));
        top.add(new JLabel(I18n.t("form.appMgmt.filter.label")));
        cmbFilter = new JComboBox<>(new String[]{
                I18n.t("form.appMgmt.filter.weekly"),
                I18n.t("form.appMgmt.filter.attended")
        });
        top.add(cmbFilter);
        btnRefresh = new JButton(I18n.t("form.appMgmt.button.refresh"));
        top.add(btnRefresh);
        add(top, "growx, wrap");

        // Tabla
        model = new DefaultTableModel(
                new String[]{
                        I18n.t("form.appMgmt.col.date"),
                        I18n.t("form.appMgmt.col.time"),
                        I18n.t("form.appMgmt.col.patient"),
                        I18n.t("form.appMgmt.col.specialty"),
                        I18n.t("form.appMgmt.col.room"),
                        I18n.t("form.appMgmt.col.status")
                }, 0
        ) {
            @Override public boolean isCellEditable(int r,int c){return false;}
        };
        tblAppointments = new JTable(model);
        add(new JScrollPane(tblAppointments), "grow, push, wrap");

        JPanel bot = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        btnMarkAttended = new JButton(I18n.t("form.appMgmt.button.markAttended"));
        bot.add(btnMarkAttended);
        add(bot, "growx");

        // Listeners
        cmbFilter.addActionListener(e -> loadAppointments());
        btnRefresh.addActionListener(e -> loadAppointments());
        btnMarkAttended.addActionListener(e -> markSelectedAttended());

        // carga inicial
        loadAppointments();
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

    private void loadAppointments() {
        model.setRowCount(0);
        boolean onlyThisWeek = cmbFilter.getSelectedIndex()==0;
        controller.getByDoctor(currentDoctor.getId(), onlyThisWeek)
                .thenAccept(list -> SwingUtilities.invokeLater(() -> {
                    for (Appointment a : list) {
                        model.addRow(new Object[]{
                                a.getDate(), a.getTime(), a.getPatient().getFullName(),
                                a.getSpecialty(), a.getRoom().getName(), a.getStatus()
                        });
                    }
                }))
                .exceptionally(ex -> {
                    JOptionPane.showMessageDialog(this, I18n.t("form.appMgmt.error.load"));
                    return null;
                });
    }

    private void markSelectedAttended() {
        int sel = tblAppointments.getSelectedRow();
        if (sel<0) {
            JOptionPane.showMessageDialog(this, I18n.t("form.appMgmt.error.selectRow"));
            return;
        }
        LocalDate date = (LocalDate) model.getValueAt(sel,0);
        controller.markAttended(currentDoctor.getId(), date)
                .thenRun(() -> SwingUtilities.invokeLater(() -> {
                    JOptionPane.showMessageDialog(this, I18n.t("form.appMgmt.success.marked"));
                    loadAppointments();
                }))
                .exceptionally(ex -> {
                    JOptionPane.showMessageDialog(this, I18n.t("form.appMgmt.error.mark"));
                    return null;
                });
    }

    @Override
    public void onLanguageChanged(ResourceBundle bundle) {
        lblTitle.setText(bundle.getString("form.appMgmt.title"));
        text.setText(bundle.getString("form.appMgmt.description"));
    }
}
