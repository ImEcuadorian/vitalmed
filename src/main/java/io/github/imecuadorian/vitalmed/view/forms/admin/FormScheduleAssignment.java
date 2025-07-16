package io.github.imecuadorian.vitalmed.view.forms.admin;

import com.formdev.flatlaf.FlatClientProperties;
import io.github.imecuadorian.vitalmed.controller.AdminDashboardController;
import io.github.imecuadorian.vitalmed.factory.ServiceFactory;
import io.github.imecuadorian.vitalmed.i18n.*;
import io.github.imecuadorian.vitalmed.model.*;
import io.github.imecuadorian.vitalmed.util.*;
import io.github.imecuadorian.vitalmed.view.system.Form;
import net.miginfocom.swing.MigLayout;
import org.slf4j.*;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.*;
import java.util.List;

@SystemForm(name = "Asignar turno", description = "Formulario para asignar turnos a doctores", tags = {"turnos", "horario", "doctor"})
public class FormScheduleAssignment extends Form implements LanguageChangeListener {

    private static final Logger LOGGER = LoggerFactory.getLogger(FormScheduleAssignment.class);
    private static final AdminDashboardController adminDashboardController = new AdminDashboardController(
            ServiceFactory.getADMIN_SERVICE()
    );
    private JComboBox<Doctor> cmbDoctor;
    private JComboBox<Room> cmbRoom;
    private final String[] days = {I18n.t("form.formScheduleAssignment.monday.days"), I18n.t("form.formScheduleAssignment.tuesday.days"),
            I18n.t("form.formScheduleAssignment.wednesday.days"), I18n.t("form.formScheduleAssignment.thursday.days"),
            I18n.t("form.formScheduleAssignment.friday.days")};
    private final JTable[] tables = new JTable[5];

    public FormScheduleAssignment() {
        I18n.addListener(this);
    }

    @Override
    public void formInit() {
        setLayout(new MigLayout("fillx,wrap", "[fill]", "[][grow][]"));
        add(createHeaderPanel());
        add(createSchedulePanel(), "grow");
        add(createSaveButton(), "right");
    }

    private JPanel createHeaderPanel() {
        JPanel panel = new JPanel(new MigLayout("fillx,wrap", "[grow]"));

        JLabel title = new JLabel(I18n.t("form.formScheduleAssignment.turnAssignment.title"));
        title.putClientProperty(FlatClientProperties.STYLE, "font:bold +3");

        JTextPane text = new JTextPane();
        text.setText(I18n.t("form.formScheduleAssignment.description"));
        text.setEditable(false);
        text.setOpaque(false);
        text.setBorder(BorderFactory.createEmptyBorder());

        panel.add(title, "span 2");
        panel.add(text);

        JLabel lblDoctor = new JLabel(I18n.t("form.formScheduleAssignment.doctor.label"));
        cmbDoctor = new JComboBox<>();

        JLabel lblRoom = new JLabel(I18n.t("form.formScheduleAssignment.room.label"));
        cmbRoom = new JComboBox<>();

        adminDashboardController.getDoctors().thenAccept(doctors -> {
            cmbDoctor.setModel(new DefaultComboBoxModel<>(doctors.toArray(new Doctor[0])));
        }).exceptionally(ex -> {
            LOGGER.error("Error loading doctors", ex);
            JOptionPane.showMessageDialog(this, I18n.t("form.formScheduleAssignment.error.loadDoctors"));
            return null;
        });

        adminDashboardController.getRooms().thenAccept(rooms -> {
            cmbRoom.setModel(new DefaultComboBoxModel<>(rooms.toArray(new Room[0])));
        }).exceptionally(ex -> {
            LOGGER.error("Error loading rooms", ex);
            JOptionPane.showMessageDialog(this, I18n.t("form.formScheduleAssignment.error.loadRooms"));
            return null;
        });


        panel.add(lblDoctor);
        panel.add(cmbDoctor, "growx");
        panel.add(lblRoom);
        panel.add(cmbRoom, "growx");

        return panel;
    }

    private JTabbedPane createSchedulePanel() {
        JTabbedPane tabs = new JTabbedPane();
        for (String day : days) {
            JPanel dayPanel = new JPanel();
            dayPanel.setLayout(new BoxLayout(dayPanel, BoxLayout.Y_AXIS));
            dayPanel.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));

            JPanel shiftsContainer = new JPanel();
            shiftsContainer.setLayout(new BoxLayout(shiftsContainer, BoxLayout.Y_AXIS));

            JButton addBtn = new JButton("Añadir turno");
            addBtn.putClientProperty( FlatClientProperties.STYLE, "icon:"  );
            addBtn.addActionListener(e -> {
                JPanel row = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 4));

                JButton remove = new JButton();
                remove.putClientProperty( FlatClientProperties.STYLE, "icon:"  );
                remove.addActionListener(ev -> {
                    shiftsContainer.remove(row);
                    shiftsContainer.revalidate();
                    shiftsContainer.repaint();
                });

                row.add(new JLabel("De"));
                row.add(new JLabel("a"));
                row.add(remove);

                shiftsContainer.add(row);
                shiftsContainer.revalidate();
                shiftsContainer.repaint();
            });

            dayPanel.add(addBtn);
            dayPanel.add(Box.createRigidArea(new Dimension(0,8)));
            dayPanel.add(shiftsContainer);

            tabs.addTab(day, dayPanel);
        }
        return tabs;
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
                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(this, I18n.t("form.formScheduleAssignment.errorMessage.errorIn") + days[i] + ", fila " + (j + 1)
                                + I18n.t("form.formScheduleAssignment.errorMessage.invalidTime"));
                        return;
                    }
                }
            }
        }

        if (true) {
            JOptionPane.showMessageDialog(this, I18n.t("form.formScheduleAssignment.success.shiftsSaved"));
        } else {
            JOptionPane.showMessageDialog(this, I18n.t("form.formScheduleAssignment.error.shiftsNotSaved"));
        }
    }

    @Override
    public void onLanguageChanged(ResourceBundle bundle) {

    }
}
