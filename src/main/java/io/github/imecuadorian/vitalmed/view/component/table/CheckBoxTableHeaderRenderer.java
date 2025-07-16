package io.github.imecuadorian.vitalmed.view.component.table;

import com.formdev.flatlaf.*;

import javax.swing.*;
import javax.swing.event.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;

public class CheckBoxTableHeaderRenderer extends JCheckBox implements TableCellRenderer {

    private final JTable table;
    private final int column;

    public CheckBoxTableHeaderRenderer(JTable table, int column) {
        this.table = table;
        this.column = column;
        setText(""); // 🔸 Important: avoid weird spacing
        setHorizontalAlignment(SwingConstants.CENTER);
        putClientProperty(FlatClientProperties.STYLE, "background:null;");
        putClientProperty(FlatClientProperties.STYLE_CLASS, "tableCheckBoxHeader");

        table.getTableHeader().addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent me) {
                if (SwingUtilities.isLeftMouseButton(me)) {
                    int col = table.columnAtPoint(me.getPoint());
                    if (col == column) {
                        putClientProperty(FlatClientProperties.SELECTED_STATE, null);
                        setSelected(!isSelected());
                        selectedTableRow(isSelected());
                        table.getTableHeader().repaint();
                    }
                }
            }
        });

        table.getModel().addTableModelListener(tme -> {
            if (tme.getColumn() == column || tme.getType() == TableModelEvent.DELETE) {
                SwingUtilities.invokeLater(this::checkRow);
            }
        });
    }

    private void checkRow() {
        if (table.getRowCount() == 0) {
            putClientProperty(FlatClientProperties.SELECTED_STATE, null);
            setSelected(false);
            table.getTableHeader().repaint();
            return;
        }

        boolean initValue = Boolean.TRUE.equals(table.getValueAt(0, column));
        for (int i = 1; i < table.getRowCount(); i++) {
            if (!Objects.equals(table.getValueAt(i, column), initValue)) {
                putClientProperty(FlatClientProperties.SELECTED_STATE, FlatClientProperties.SELECTED_STATE_INDETERMINATE);
                table.getTableHeader().repaint();
                return;
            }
        }

        putClientProperty(FlatClientProperties.SELECTED_STATE, null);
        setSelected(initValue);
        table.getTableHeader().repaint();
    }

    private void selectedTableRow(boolean selected) {
        for (int i = 0; i < table.getRowCount(); i++) {
            table.setValueAt(selected, i, column);
        }
    }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value,
                                                   boolean isSelected, boolean hasFocus, int row, int column) {
        TableCellRenderer defaultRenderer = table.getTableHeader().getDefaultRenderer();
        Component headerComp = defaultRenderer.getTableCellRendererComponent(
                table, value, isSelected, hasFocus, row, column);
        if (headerComp instanceof JComponent jc) {
            setBorder(jc.getBorder());
        }
        return this;
    }

    @Override
    protected void paintComponent(Graphics g) {
        if (getBorder() != null) {
            getBorder().paintBorder(this, g, 0, 0, getWidth(), getHeight());
        }
        super.paintComponent(g);
    }


}

