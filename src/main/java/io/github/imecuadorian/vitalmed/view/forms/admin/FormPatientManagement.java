package io.github.imecuadorian.vitalmed.view.forms.admin;

import com.formdev.flatlaf.*;
import com.formdev.flatlaf.extras.*;
import io.github.imecuadorian.vitalmed.controller.*;
import io.github.imecuadorian.vitalmed.factory.*;
import io.github.imecuadorian.vitalmed.i18n.I18n;
import io.github.imecuadorian.vitalmed.model.*;
import io.github.imecuadorian.vitalmed.util.*;
import io.github.imecuadorian.vitalmed.view.component.table.*;
import io.github.imecuadorian.vitalmed.view.modal.*;
import io.github.imecuadorian.vitalmed.view.system.*;
import net.miginfocom.swing.*;
import raven.modal.*;
import raven.modal.component.*;
import raven.modal.option.*;
import raven.modal.toast.option.*;

import javax.swing.*;
import javax.swing.event.*;
import javax.swing.table.*;
import java.awt.*;
import java.util.*;
import java.util.List;

import static io.github.imecuadorian.vitalmed.util.Constants.*;

@SystemForm(name = "Gestión de Pacientes", description = "Gestión de pacientes", tags = {"pacientes", "gestión"})
public class FormPatientManagement extends Form {

    private final AdminDashboardController adminDashboardController = new AdminDashboardController(ServiceFactory.getAdminService());
    private DefaultTableModel tableModel;
    private TableRowSorter<DefaultTableModel> sorter;

    public FormPatientManagement() {
        init();
    }

    private void init() {
        setLayout(new MigLayout("fillx,wrap", "[fill]", "[][fill,grow]"));
        add(createInfo());
        add(createCustomTable(), "gapx 7 7");
    }


    private JPanel createInfo() {
        JPanel panel = new JPanel(new MigLayout("fillx,wrap", "[fill]"));
        JLabel title = new JLabel(I18n.t("form.formPatientManagement.patientManagement.title"));
        JTextPane text = new JTextPane();
        text.setText(I18n.t("form.formPatientManagement.description"));
        text.setEditable(false);
        text.setBorder(BorderFactory.createEmptyBorder());
        title.putClientProperty(FlatClientProperties.STYLE, "font:bold +3");

        panel.add(title);
        panel.add(text, "width 500");
        return panel;
    }

    private Component createCustomTable() {
        JPanel panel = new JPanel(new MigLayout("fillx,wrap,insets 10 0 10 0", "[fill]", "[][]0[fill,grow]"));

        Object[] columns = new Object[]{"SELECT", "#", I18n.t("form.formPatientManagement.id.table"), I18n.t("form.formPatientManagement.name.table"),
                I18n.t("form.formPatientManagement.mail.table"), I18n.t("form.formPatientManagement.address.table"), I18n.t("form.formPatientManagement.mobile.table")};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 0;
            }

            @Override
            public Class<?> getColumnClass(int columnIndex) {
                return columnIndex == 0 ? Boolean.class : super.getColumnClass(columnIndex);
            }
        };

        JTable table = new JTable(tableModel);
        sorter = new TableRowSorter<>(tableModel);
        table.setRowSorter(sorter);

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());

        table.getColumnModel().getColumn(0).setMaxWidth(50);
        table.getColumnModel().getColumn(1).setMaxWidth(50);
        table.getColumnModel().getColumn(2).setPreferredWidth(150);
        table.getColumnModel().getColumn(5).setPreferredWidth(300);
        table.getColumnModel().getColumn(6).setPreferredWidth(250);


        table.getColumnModel().getColumn(0).setHeaderRenderer(new CheckBoxTableHeaderRenderer(table, 0));

        table.getTableHeader().setDefaultRenderer(new TableHeaderAlignment(table) {
            @Override
            protected int getAlignment(int column) {
                if (column == 1) {
                    return SwingConstants.CENTER;
                }
                return SwingConstants.LEADING;
            }
        });

        panel.putClientProperty(FlatClientProperties.STYLE, "arc:20;" +
                                                            "background:$Table.background;");
        table.getTableHeader().putClientProperty(FlatClientProperties.STYLE, "" +
                                                                             "height:30;" +
                                                                             "hoverBackground:null;" +
                                                                             "pressedBackground:null;" +
                                                                             "separatorColor:$TableHeader.background;");
        table.putClientProperty(FlatClientProperties.STYLE, "rowHeight:70;" +
                                                            "showHorizontalLines:true;" +
                                                            "intercellSpacing:0,1;" +
                                                            "cellFocusColor:$TableHeader.hoverBackground;" +
                                                            "selectionBackground:$TableHeader.hoverBackground;" +
                                                            "selectionInactiveBackground:$TableHeader.hoverBackground;" +
                                                            "selectionForeground:$Table.foreground;");
        scrollPane.getVerticalScrollBar().putClientProperty(FlatClientProperties.STYLE, "" +
                                                                                        "trackArc:$ScrollBar.thumbArc;" +
                                                                                        "trackInsets:3,3,3,3;" +
                                                                                        "thumbInsets:3,3,3,3;" +
                                                                                        "background:$Table.background;");

        JLabel title = new JLabel(I18n.t("form.formPatientManagement.patientTable.title"));
        title.putClientProperty(FlatClientProperties.STYLE, "font:bold +2");
        panel.add(title, "gapx 20");

        panel.add(createHeaderAction());
        panel.add(scrollPane);

        for (Patient d : ServiceFactory.getAdminService().getAllPatients()) {
            tableModel.addRow(new Object[]{false, tableModel.getRowCount() + 1, d.getId(), d.getFullName(), d.getEmail(), d.getAddress(), d.getMobile()});
        }

        return panel;
    }


    private Component createHeaderAction() {
        JPanel panel = new JPanel(new MigLayout("insets 5 20 5 20", "[fill,230]push[][]"));

        JTextField txtSearch = new JTextField();
        txtSearch.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, I18n.t("form.formPatientManagement.search.placeholder"));
        txtSearch.putClientProperty(FlatClientProperties.TEXT_FIELD_LEADING_ICON, new FlatSVGIcon("io/github/imecuadorian/vitalmed/icon/search.svg", 0.4f));

        txtSearch.getDocument().addDocumentListener(new DocumentListener() {
            public void insertUpdate(DocumentEvent e) {
                search(txtSearch.getText());
            }

            public void removeUpdate(DocumentEvent e) {
                search(txtSearch.getText());
            }

            public void changedUpdate(DocumentEvent e) {
                search(txtSearch.getText());
            }

            private void search(String text) {
                if (text.trim().isEmpty()) {
                    sorter.setRowFilter(null);
                } else {
                    sorter.setRowFilter(RowFilter.regexFilter("(?i)" + text));
                }
            }
        });

        JButton cmdCreate = new JButton(I18n.t("form.formPatientManagement.resetPassword.button"));

        cmdCreate.addActionListener(e -> {
            List<String> selectedIds = new ArrayList<>();
            for (int i = 0; i < tableModel.getRowCount(); i++) {
                Boolean selected = (Boolean) tableModel.getValueAt(i, 0);
                if (selected != null && selected) {
                    selectedIds.add(tableModel.getValueAt(i, 2).toString());
                }
            }

            if (selectedIds.isEmpty()) {
                Toast.show(this, Toast.Type.ERROR, I18n.t("form.formPatientManagement.selectedPatient"), ToastLocation.TOP_TRAILING, Constants.getOption());
                return;
            }

            showModal(selectedIds);
        });
        ;


        panel.add(txtSearch);
        panel.add(cmdCreate);

        panel.putClientProperty(FlatClientProperties.STYLE, "background:null;");
        return panel;
    }

    private void showModal(List<String> selectedIds) {
        String message = I18n.t("form.formPatientManagement.resetPassword.message");
        Option option = ModalDialog.createOption();
        option.getLayoutOption().setSize(-1, 1f)
                .setLocation(Location.TRAILING, Location.TOP)
                .setAnimateDistance(0.7f, 0);
        ModalDialog.showModal(this, new SimpleMessageModal(SimpleMessageModal.Type.WARNING, message, I18n.t("form.formPatientManagement.resetPassword.typeWarning"), SimpleModalBorder.YES_NO_OPTION, (controller, action) -> {
            if (action == SimpleModalBorder.YES_OPTION) {
                for (String id : selectedIds) {
                    adminDashboardController.resetPassword(id, "Vm@" + id);
                }
                Toast.show(this, Toast.Type.SUCCESS, I18n.t("form.formPatientManagement.resetPassword.typeSuccess"), ToastLocation.TOP_TRAILING, Constants.getOption());
                for (int i = 0; i < tableModel.getRowCount(); i++) {
                    tableModel.setValueAt(false, i, 0);
                }
            }
        }), getSelectedOption());
    }

}
