package io.github.imecuadorian.vitalmed.view.forms.admin;

import com.formdev.flatlaf.*;
import com.formdev.flatlaf.extras.*;
import io.github.imecuadorian.vitalmed.controller.*;
import io.github.imecuadorian.vitalmed.factory.*;
import io.github.imecuadorian.vitalmed.i18n.I18n;
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

@SystemForm(name = "Registro de Doctor", description = "Registro de doctor", tags = {"doctor", "registro", "doctor", "register"})
public class FormRegisterDoctor extends Form {

    private final AdminDashboardController adminDashboardController = new AdminDashboardController(ServiceFactory.getAdminService());
    private DefaultTableModel tableModel;
    private TableRowSorter<DefaultTableModel> sorter;

    public FormRegisterDoctor() {
        init();
    }

    public void init() {
        setLayout(new MigLayout("fillx,wrap", "[fill]", "[][fill,grow]"));
        add(createInfo());
        add(createCustomTable(), "gapx 7 7");
    }

    private JPanel createInfo() {
        JPanel panel = new JPanel(new MigLayout("fillx,wrap", "[fill]"));
        JLabel title = new JLabel(I18n.t("form.formRegisterDoctor.doctorRegistration.title"));
        JTextPane text = new JTextPane();
        text.setText(I18n.t("form.formRegisterDoctor.description"));
        text.setEditable(false);
        text.setBorder(BorderFactory.createEmptyBorder());
        title.putClientProperty(FlatClientProperties.STYLE, "font:bold +3");

        panel.add(title);
        panel.add(text, "width 500");
        return panel;
    }

    private Component createCustomTable() {
        JPanel panel = new JPanel(new MigLayout("fillx,wrap,insets 10 0 10 0", "[fill]", "[][]0[fill,grow]"));

        Object[] columns = new Object[]{"#", I18n.t("form.formRegisterDoctor.id.table"), I18n.t("form.formRegisterDoctor.name.table"),
                I18n.t("form.formRegisterDoctor.mail.table"), I18n.t("form.formRegisterDoctor.address.table"),
                I18n.t("form.formRegisterDoctor.mobile.table"), I18n.t("form.formRegisterDoctor.speciality.table")};
        tableModel = new DefaultTableModel(columns, 0);

        JTable table = new JTable(tableModel);
        sorter = new TableRowSorter<>(tableModel);
        table.setRowSorter(sorter);

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());

        table.getColumnModel().getColumn(0).setMaxWidth(50);
        table.getColumnModel().getColumn(1).setPreferredWidth(150);
        table.getColumnModel().getColumn(2).setPreferredWidth(200);
        table.getColumnModel().getColumn(3).setPreferredWidth(200);
        table.getColumnModel().getColumn(4).setPreferredWidth(300);
        table.getColumnModel().getColumn(5).setPreferredWidth(100);
        table.getColumnModel().getColumn(6).setPreferredWidth(100);


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
        table.getTableHeader().putClientProperty(FlatClientProperties.STYLE, "height:30;" +
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
        scrollPane.getVerticalScrollBar().putClientProperty(FlatClientProperties.STYLE, "trackArc:$ScrollBar.thumbArc;" +
                                                                                        "trackInsets:3,3,3,3;" +
                                                                                        "thumbInsets:3,3,3,3;" +
                                                                                        "background:$Table.background;");

        JLabel title = new JLabel(I18n.t("form.formRegisterDoctor.doctorTable.title"));
        title.putClientProperty(FlatClientProperties.STYLE, "font:bold +2");
        panel.add(title, "gapx 20");

        panel.add(createHeaderAction());
        panel.add(scrollPane);

        for (Doctor d : adminDashboardController.getDoctors()) {
            tableModel.addRow(new Object[]{tableModel.getRowCount() + 1, d.getId(), d.getFullName(), d.getEmail(), d.getAddress(), d.getMobile(), d.getSpeciality()});
        }

        return panel;
    }


    private Component createHeaderAction() {
        JPanel panel = new JPanel(new MigLayout("insets 5 20 5 20", "[fill,230]push[][]"));

        JTextField txtSearch = new JTextField();
        txtSearch.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, I18n.t("form.formRegisterDoctor.search.placeholder"));
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

        JButton cmdCreate = new JButton(I18n.t("form.formRegisterDoctor.createDoctor.button")) {
            ;

            @Override
            public boolean isDefaultButton() {
                return true;
            }
        };

        cmdCreate.addActionListener(e -> {
            showModal();
        });


        panel.add(txtSearch);
        panel.add(cmdCreate);

        panel.putClientProperty(FlatClientProperties.STYLE, "background:null;");
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

        final FormAddDoctor formAddDoctor = new FormAddDoctor(this::reloadTable);
        ModalDialog.showModal(this, new SimpleModalBorder(formAddDoctor, I18n.t("form.formRegisterDoctor.registerDoctor.button"), SimpleModalBorder.DEFAULT_OPTION, (controller, action) ->  { }), option);

    }

    private void reloadTable() {
        tableModel.setRowCount(0);

        for (Doctor d : adminDashboardController.getDoctors()) {
            tableModel.addRow(new Object[]{
                    tableModel.getRowCount() + 1,
                    d.getId(),
                    d.getFullName(),
                    d.getEmail(),
                    d.getAddress(),
                    d.getMobile(),
                    d.getSpeciality()
            });
        }
    }
}
