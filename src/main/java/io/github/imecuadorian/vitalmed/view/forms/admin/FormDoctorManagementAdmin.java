package io.github.imecuadorian.vitalmed.view.forms.admin;

import com.formdev.flatlaf.*;
import com.formdev.flatlaf.extras.*;
import io.github.imecuadorian.vitalmed.controller.*;
import io.github.imecuadorian.vitalmed.factory.*;
import io.github.imecuadorian.vitalmed.i18n.*;
import io.github.imecuadorian.vitalmed.model.*;
import io.github.imecuadorian.vitalmed.util.*;
import io.github.imecuadorian.vitalmed.view.component.table.*;
import io.github.imecuadorian.vitalmed.view.forms.admin.form.*;
import io.github.imecuadorian.vitalmed.view.system.*;
import net.miginfocom.swing.*;
import raven.modal.*;
import raven.modal.component.*;
import raven.modal.option.*;

import javax.swing.*;
import javax.swing.event.*;
import javax.swing.table.*;
import java.awt.*;
import java.util.*;

@SystemForm(name = "Registro de Doctor", description = "Registro de doctor", tags = {"doctor", "registro"})
public class FormDoctorManagementAdmin extends Form implements LanguageChangeListener {

    private transient TableRowSorter<DefaultTableModel> sorter;
    private static final String FILL = "[fill]";
    private DefaultTableModel tableModel;

    private JLabel lblTitle;
    private JTextPane text;
    private JLabel lblTitleTable;
    private JTextField txtSearch;
    private JButton btnCreate;
    private JButton btnDelete;
    private JButton btnEdit;
    private transient Object[] columns;

    public FormDoctorManagementAdmin() {
        setupLayout();
    }

    @Override
    public void formInit() {
    }

    @Override
    public void formRefresh() {
        reloadTable();
    }

    private void setupLayout() {
        setLayout(new MigLayout("fillx, wrap", FILL, "[][fill, grow]"));
        add(createInfo());
        add(createCustomTable(), "gapx 7 7");
        I18n.addListener(this);
    }

    private JPanel createInfo() {
        JPanel panel = new JPanel(new MigLayout("fillx, wrap", FILL));
        lblTitle = new JLabel();
        text = new JTextPane();
        text.setEditable(false);
        text.setBorder(BorderFactory.createEmptyBorder());
        lblTitle.putClientProperty(FlatClientProperties.STYLE, "font:bold +3");
        panel.add(lblTitle);
        panel.add(text, "width 500");
        return panel;
    }

    private Component createCustomTable() {
        JPanel panel = new JPanel(new MigLayout("fillx,wrap,insets 10 0 10 0", FILL, "[][]0[fill,grow]"));

        this.columns = new Object[]{
                I18n.t("form.formDoctorManagementAdmin.table.col.select"),
                I18n.t("form.formDoctorManagementAdmin.table.col.number"),
                I18n.t("form.formDoctorManagementAdmin.table.col.id"),
                I18n.t("form.formDoctorManagementAdmin.table.col.name"),
                I18n.t("form.formDoctorManagementAdmin.table.col.email"),
                I18n.t("form.formDoctorManagementAdmin.table.col.address"),
                I18n.t("form.formDoctorManagementAdmin.table.col.phone"),
                I18n.t("form.formDoctorManagementAdmin.table.col.specialty")
        };
        tableModel = new DefaultTableModel(this.columns, 0) {
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
        table.getColumnModel().getColumn(3).setPreferredWidth(200);
        table.getColumnModel().getColumn(4).setPreferredWidth(200);
        table.getColumnModel().getColumn(5).setPreferredWidth(300);
        table.getColumnModel().getColumn(6).setPreferredWidth(100);
        table.getColumnModel().getColumn(7).setPreferredWidth(100);

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

        panel.putClientProperty(FlatClientProperties.STYLE, """
                                                    arc:20;
                                                    background:$Table.background;
                """
        );
        table.getTableHeader().putClientProperty(FlatClientProperties.STYLE, """
                                                                     height:30;
                                                                     hoverBackground:null;
                                                                     pressedBackground:null;
                                                                     separatorColor:$TableHeader.background;
                """
        );
        table.putClientProperty(FlatClientProperties.STYLE, """
                rowHeight:70;
                showHorizontalLines:true;
                intercellSpacing:0,1;
                cellFocusColor:$TableHeader.hoverBackground;
                selectionBackground:$TableHeader.hoverBackground;
                selectionInactiveBackground:$TableHeader.hoverBackground;
                selectionForeground:$Table.foreground;
                """
        );
        scrollPane.getVerticalScrollBar().putClientProperty(FlatClientProperties.STYLE, """
                trackArc:$ScrollBar.thumbArc;
                trackInsets:3,3,3,3;
                thumbInsets:3,3,3,3;
                background:$Table.background;
                """);

        lblTitleTable = new JLabel();
        lblTitleTable.putClientProperty(FlatClientProperties.STYLE, "font:bold +2");
        panel.add(lblTitleTable, "gapx 20");
        panel.add(createHeaderAction(), "gapbottom 20");
        panel.add(scrollPane);


        return panel;
    }
    private void updateTableHeaders() {
        columns = new Object[]{
                I18n.t("form.formDoctorManagementAdmin.table.col.select"),
                I18n.t("form.formDoctorManagementAdmin.table.col.number"),
                I18n.t("form.formDoctorManagementAdmin.table.col.id"),
                I18n.t("form.formDoctorManagementAdmin.table.col.name"),
                I18n.t("form.formDoctorManagementAdmin.table.col.email"),
                I18n.t("form.formDoctorManagementAdmin.table.col.address"),
                I18n.t("form.formDoctorManagementAdmin.table.col.phone"),
                I18n.t("form.formDoctorManagementAdmin.table.col.specialty")
        };
        tableModel.setColumnIdentifiers(columns);
    }

    private Component createHeaderAction() {
        JPanel panel = new JPanel(new MigLayout("insets 5 20 5 20", "[fill,230]push[][]"));

        txtSearch = new JTextField();

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

        btnCreate = new JButton() {
            @Override
            public boolean isDefaultButton() {
                return true;
            }
        };

        btnDelete = new JButton();
        btnEdit = new JButton();


        btnDelete.putClientProperty(FlatClientProperties.STYLE_CLASS, "deleteButton");
        btnEdit.putClientProperty(FlatClientProperties.STYLE_CLASS, "editButton");
        panel.putClientProperty(FlatClientProperties.STYLE, "background:null;");


        panel.add(txtSearch);
        panel.add(btnCreate);
        panel.add(btnEdit);
        panel.add(btnDelete);

        btnCreate.addActionListener(e -> showModal());
        return panel;
    }

    private void showModal() {
        Option option = ModalDialog.createOption();
        option.getLayoutOption().setSize(-1, 1f)
                .setLocation(Location.TRAILING, Location.TOP)
                .setAnimateDistance(0.7f, 0);
        option.setAnimationEnabled(true)
                .setCloseOnPressedEscape(false)
                .setOpacity(0.5f);
        option.getBorderOption()
                .setBorderWidth(1)
                .setShadow(BorderOption.Shadow.EXTRA_LARGE);
        option.getLayoutOption().setAnimateDistance(0, 0)
                .setAnimateScale(0.1f);

        FormAddDoctor formAddDoctor = new FormAddDoctor(this::reloadTable);
        ModalDialog.showModal(this, new SimpleModalBorder(formAddDoctor, I18n.t("form.formDoctorManagementAdmin.doctorRegister.formAddDoctor"), SimpleModalBorder.DEFAULT_OPTION, (controller, action) -> {
        }), option);

    }

    private void reloadTable() {
        tableModel.setRowCount(0);

    }

    @Override
    public void onLanguageChanged(ResourceBundle bundle) {
        lblTitle.setText(I18n.t("form.formDoctorManagementAdmin.doctorRegistration.lblTitle"));
        text.setText(I18n.t("form.formDoctorManagementAdmin.description"));
        lblTitleTable.setText(I18n.t("form.formDoctorManagementAdmin.tableOfDoctors.title"));
        txtSearch.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, I18n.t("form.formDoctorManagementAdmin.search.placeholder"));
        btnCreate.setText(I18n.t("form.formDoctorManagementAdmin.createDoctor.btnCreate"));
        btnDelete.setText(I18n.t("form.formDoctorManagementAdmin.deleteDoctor.btnDelete"));
        btnEdit.setText(I18n.t("form.formDoctorManagementAdmin.editDoctor.btnEdit"));

        updateTableHeaders();
    }
}
