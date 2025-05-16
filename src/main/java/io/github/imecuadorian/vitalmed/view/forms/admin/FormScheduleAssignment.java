package io.github.imecuadorian.vitalmed.view.forms.admin;

import com.formdev.flatlaf.FlatClientProperties;
import io.github.imecuadorian.vitalmed.controller.AdminDashboardController;
import io.github.imecuadorian.vitalmed.factory.ServiceFactory;
import io.github.imecuadorian.vitalmed.i18n.I18n;
import io.github.imecuadorian.vitalmed.model.*;
import io.github.imecuadorian.vitalmed.util.*;
import io.github.imecuadorian.vitalmed.view.system.Form;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@SystemForm(name = "Asignar turno", description = "Formulario para asignar turnos a doctores", tags = {"turnos", "horario", "doctor"})
public class FormScheduleAssignment extends Form {

    private final AdminDashboardController controller = new AdminDashboardController(ServiceFactory.getAdminService());
    private JComboBox<Doctor> cmbDoctor;
    private JComboBox<Room> cmbRoom;
    private final String[] days = {I18n.t("form.formScheduleAssignment.monday.days"), I18n.t("form.formScheduleAssignment.tuesday.days"),
            I18n.t("form.formScheduleAssignment.wednesday.days"), I18n.t("form.formScheduleAssignment.thursday.days"),
            I18n.t("form.formScheduleAssignment.friday.days")};
    private final JTable[] tables = new JTable[5];

    public FormScheduleAssignment() {
        init();
    }

    private void init() {
        setLayout(new MigLayout("fillx,wrap", "[fill]", "[][grow][]"));

        add(createHeaderPanel());
        add(createSchedulePanel(), "grow");
        add(createSaveButton(), "right");
    }

    private JPanel createHeaderPanel() {
        JPanel panel = new JPanel(new MigLayout("fillx,wrap", "[][grow]"));

        JLabel title = new JLabel(I18n.t("form.formScheduleAssignment.turnAssignment.title"));
        title.putClientProperty(FlatClientProperties.STYLE, "font:bold +3");

        JTextPane text = new JTextPane();
        text.setText(I18n.t("form.formScheduleAssignment.description"));//Ver fuente :)
        text.setEditable(false);
        text.setOpaque(false);
        text.setBorder(BorderFactory.createEmptyBorder());

        panel.add(title, "span 2");
        panel.add(text, "span 2, width 600");

        JLabel lblDoctor = new JLabel(I18n.t("form.formScheduleAssignment.doctor.label"));
        cmbDoctor = new JComboBox<>(controller.getDoctors().toArray(new Doctor[0]));

        JLabel lblRoom = new JLabel(I18n.t("form.formScheduleAssignment.room.label"));
        cmbRoom = new JComboBox<>(controller.getRooms().toArray(new Room[0]));

        panel.add(lblDoctor);
        panel.add(cmbDoctor, "growx");
        panel.add(lblRoom);
        panel.add(cmbRoom, "growx");

        return panel;
    }

    private JPanel createSchedulePanel() {
        JPanel daysPanel = new JPanel(new MigLayout("wrap 1, fillx", "[grow]"));
        for (int i = 0; i < days.length; i++) {
            DefaultTableModel model = new DefaultTableModel(new Object[]{I18n.t("form.formScheduleAssigment.startTime.tableModel"),
                    I18n.t("form.formScheduleAssigment.endTime.tableModel")}, 4);
            JTable table = new JTable(model);
            tables[i] = table;

            JScrollPane scroll = new JScrollPane(table);
            scroll.setBorder(BorderFactory.createEmptyBorder());
            scroll.getVerticalScrollBar().putClientProperty(FlatClientProperties.STYLE, "trackArc:$ScrollBar.thumbArc;trackInsets:3,3,3,3;thumbInsets:3,3,3,3;");

            JPanel dayPanel = new JPanel(new MigLayout("fill"));
            dayPanel.setBorder(BorderFactory.createTitledBorder(days[i]));
            dayPanel.add(scroll, "grow, h 100::150");

            daysPanel.add(dayPanel);
        }
        return daysPanel;
    }

    private JButton createSaveButton() {
        JButton button = new JButton(I18n.t("form.formScheduleAssignment.saveShifts.button"));
        button.addActionListener(e -> saveSchedules());
        return button;
    }

    private void saveSchedules() {
        Doctor doctor = (Doctor) cmbDoctor.getSelectedItem();
        Room room = (Room) cmbRoom.getSelectedItem();

        if (doctor == null || room == null) {
            JOptionPane.showMessageDialog(this, I18n.t("form.formScheduleAssignment.error.selectDoctorAndRoom"));
            return;
        }

        List<Schedule> schedules = new ArrayList<>();

        for (int i = 0; i < days.length; i++) {
            JTable table = tables[i];
            for (int j = 0; j < 4; j++) {
                String startStr = (String) table.getValueAt(j, 0);
                String endStr = (String) table.getValueAt(j, 1);
                if (startStr != null && endStr != null && !startStr.isEmpty() && !endStr.isEmpty()) {
                    try {
                        LocalTime start = LocalTime.parse(startStr);
                        LocalTime end = LocalTime.parse(endStr);
                        schedules.add(new Schedule(DayOfWeek.of(i + 1), start, end, room));
                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(this, I18n.t("form.formScheduleAssignment.errorMessage.errorIn") + days[i] + ", fila " + (j + 1)
                                + I18n.t("form.formScheduleAssignment.errorMessage.invalidTime"));
                        return;
                    }
                }
            }
        }

        boolean success = controller.assignSchedules(doctor.getId(), schedules);
        if (success) {
            JOptionPane.showMessageDialog(this, I18n.t("form.formScheduleAssignment.success.shiftsSaved"));
        } else {
            JOptionPane.showMessageDialog(this, I18n.t("form.formScheduleAssignment.error.shiftsNotSaved"));
        }
    }
}
