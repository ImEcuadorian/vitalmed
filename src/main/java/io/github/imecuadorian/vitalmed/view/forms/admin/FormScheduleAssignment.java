package io.github.imecuadorian.vitalmed.view.forms.admin;

import com.formdev.flatlaf.FlatClientProperties;
import io.github.imecuadorian.vitalmed.controller.AdminDashboardController;
import io.github.imecuadorian.vitalmed.factory.ServiceFactory;
import io.github.imecuadorian.vitalmed.i18n.*;
import io.github.imecuadorian.vitalmed.model.Room;
import io.github.imecuadorian.vitalmed.model.Schedule;
import io.github.imecuadorian.vitalmed.model.Doctor;
import io.github.imecuadorian.vitalmed.util.*;
import io.github.imecuadorian.vitalmed.view.system.Form;
import net.miginfocom.swing.MigLayout;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.text.MaskFormatter;
import java.awt.*;
import java.text.ParseException;
import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.List;
import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.regex.Pattern;

@SystemForm(
        name        = "Asignar turno",
        description = "Formulario para asignar turnos a doctores",
        tags        = {"turnos","horario","doctor"}
)
public class FormScheduleAssignment extends Form implements LanguageChangeListener {
    private static final Logger LOGGER = LoggerFactory.getLogger(FormScheduleAssignment.class);
    private static final AdminDashboardController adminDashboardController =
            new AdminDashboardController(
                    ServiceFactory.getADMIN_SERVICE(),
                    ServiceFactory.getUSER_SERVICE()
            );

    private JComboBox<Doctor> cmbDoctor;
    private JComboBox<Room>  cmbRoom;
    private final Map<DayOfWeek, JTable> tableMap = new EnumMap<>(DayOfWeek.class);
    private static final Pattern TIME_PATTERN = Pattern.compile("([01]\\d|2[0-3]):[0-5]\\d");

    @Override
    public void formInit() {
        setLayout(new MigLayout("fillx, insets 10","[grow]","[][grow][]"));
        add(createHeaderPanel(), "growx, wrap");
        add(createSchedulePanel(), "grow, wrap");
        add(createSaveButton(), "tag right");
        loadDoctorsAndRooms();
    }

    private JPanel createHeaderPanel() {
        JPanel p = new JPanel(new MigLayout("fillx, wrap","[grow][grow]"));
        JLabel title = new JLabel(I18n.t("form.formScheduleAssignment.turnAssignment.title"));
        title.putClientProperty(FlatClientProperties.STYLE, "font:bold +3");
        JTextPane desc = new JTextPane();
        desc.setText("Designe los turnos semanales para los doctores y las salas disponibles.");
        desc.setEditable(false);
        desc.setOpaque(false);
        desc.setBorder(null);

        p.add(title, "span 2, center");
        p.add(desc,  "span 2, growx, wrap");

        p.add(new JLabel("Doctor:"));
        cmbDoctor = new JComboBox<>();
        p.add(cmbDoctor, "growx, split 2");

        p.add(new JLabel("Salas:"));
        cmbRoom = new JComboBox<>();
        p.add(cmbRoom, "growx");


        return p;
    }

    private JPanel createSchedulePanel() {
        JPanel p = new JPanel(new GridLayout(5,1,5,5));
        DayOfWeek[] days = {
                DayOfWeek.MONDAY, DayOfWeek.TUESDAY,
                DayOfWeek.WEDNESDAY, DayOfWeek.THURSDAY, DayOfWeek.FRIDAY
        };

        for (DayOfWeek dow : days) {
            String title = dow.getDisplayName(
                    java.time.format.TextStyle.FULL, Locale.getDefault()
            );
            JPanel dayPanel = new JPanel(new BorderLayout());
            dayPanel.setBorder(BorderFactory.createTitledBorder(title));

            // Modelo con 4 filas y 2 columnas: Inicio / Fin
            DefaultTableModel model = new DefaultTableModel(
                    new String[]{ "Iniciar", "Termina" },
                    4
            ) {
                @Override public boolean isCellEditable(int r,int c){ return true; }
            };

            JTable table = new JTable(model);
            // Use MaskFormatter para HH:mm
            try {
                MaskFormatter mf = new MaskFormatter("##:##");
                mf.setPlaceholderCharacter('0');
                DefaultCellEditor editor = new DefaultCellEditor(
                        new JFormattedTextField(mf)
                );
                table.getColumnModel().getColumn(0).setCellEditor(editor);
                table.getColumnModel().getColumn(1).setCellEditor(editor);
            } catch (ParseException ex) {
                LOGGER.warn("No se pudo aplicar MaskFormatter", ex);
            }

            tableMap.put(dow, table);
            dayPanel.add(new JScrollPane(table), BorderLayout.CENTER);
            p.add(dayPanel);
        }

        return p;
    }

    private JButton createSaveButton() {
        JButton btn = new JButton("Guardar Turnos");
        btn.addActionListener(e -> saveSchedules());
        return btn;
    }

    private void loadDoctorsAndRooms() {
        adminDashboardController.getDoctors()
                .thenAccept(list -> SwingUtilities.invokeLater(() ->
                        cmbDoctor.setModel(
                                new DefaultComboBoxModel<>(list.toArray(new Doctor[0]))
                        )
                ))
                .exceptionally(ex -> {
                    LOGGER.error("Error cargando doctores", ex);
                    JOptionPane.showMessageDialog(this,
                            "Carga de doctores fallida: " + ex.getMessage());
                    return null;
                });

        adminDashboardController.getRooms()
                .thenAccept(list -> SwingUtilities.invokeLater(() ->
                        cmbRoom.setModel(
                                new DefaultComboBoxModel<>(list.toArray(new Room[0]))
                        )
                ))
                .exceptionally(ex -> {
                    LOGGER.error("Error cargando salas", ex);
                    JOptionPane.showMessageDialog(this,
                            "Carga de salas fallida: " + ex.getMessage());
                    return null;
                });
    }

    /*private void loadExistingSchedules() {
        Doctor d = (Doctor)cmbDoctor.getSelectedItem();
        Room r   = (Room)cmbRoom.getSelectedItem();
        if (d == null || r == null) return;

        new SwingWorker<List<Schedule>,Void>() {
            @Override
            protected List<Schedule> doInBackground() {
                return adminDashboardController
                        .getWeeklySchedules(d, r)
                        .join();
            }
            @Override
            protected void done() {
                try {
                    List<Schedule> list = get();
                    // limpio cada tabla
                    tableMap.forEach((dow, tbl) ->
                            ((DefaultTableModel)tbl.getModel()).setRowCount(4)
                    );
                    for (Schedule s : list) {
                        DefaultTableModel m = (DefaultTableModel)
                                tableMap.get(s.getDayOfWeek()).getModel();
                        for (int row = 0; row < m.getRowCount(); row++) {
                            if (m.getValueAt(row,0)==null) {
                                m.setValueAt(s.getStart().toString(), row, 0);
                                m.setValueAt(s.getEnd().toString(),   row, 1);
                                break;
                            }
                        }
                    }
                } catch (InterruptedException|ExecutionException ex) {
                    LOGGER.error("Error al poblar horarios", ex);
                    JOptionPane.showMessageDialog(FormScheduleAssignment.this,
                            I18n.t("form.formScheduleAssignment.error.loadSchedules"));
                }
            }
        }.execute();
    }*/

    private void saveSchedules() {
        Doctor d = (Doctor)cmbDoctor.getSelectedItem();
        Room r   = (Room)cmbRoom.getSelectedItem();
        if (d == null || r == null) {
            JOptionPane.showMessageDialog(this,
                    "Debe seleccionar un doctor y una sala.");
            return;
        }

        List<Schedule> toSave = new ArrayList<>();
        for (Map.Entry<DayOfWeek, JTable> e : tableMap.entrySet()) {
            DayOfWeek dow = e.getKey();
            JTable tbl = e.getValue();
            DefaultTableModel m = (DefaultTableModel)tbl.getModel();

            for (int row = 0; row < m.getRowCount(); row++) {
                String start = Objects.toString(m.getValueAt(row,0),"").trim();
                String end   = Objects.toString(m.getValueAt(row,1),"").trim();
                if (start.isEmpty() && end.isEmpty()) continue;

                if (!TIME_PATTERN.matcher(start).matches() ||
                    !TIME_PATTERN.matcher(end).matches()) {
                    JOptionPane.showMessageDialog(this,
                            "Formato de hora inválido para "
                            + " " + dow + ", fila " + (row+1)
                            + ". Debe ser HH:mm.");
                    return;
                }

                LocalTime s = LocalTime.parse(start);
                LocalTime t = LocalTime.parse(end);
                //toSave.add(new Schedule(d, r, dow, s, t));
            }
        }

        /*new SwingWorker<Void,Void>() {
            @Override
            protected Void doInBackground() {
                adminDashboardController
                        .saveWeeklySchedules(d, r, toSave)
                        .join();
                return null;
            }
            @Override
            protected void done() {
                JOptionPane.showMessageDialog(FormScheduleAssignment.this,
                        I18n.t("form.formScheduleAssignment.success.shiftsSaved"));
            }
        }.execute();*/
    }

    @Override
    public void onLanguageChanged(ResourceBundle bundle) {
        // aquí recargas todos los textos de labels y títulos usando bundle.getString(...)
    }
}
