package io.github.imecuadorian.vitalmed.view.component.table;

import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;

public class TableHeaderAlignment implements TableCellRenderer {

    private final TableCellRenderer headerDelegate;
    private final TableCellRenderer cellDelegate;

    public TableHeaderAlignment(JTable table) {
        this.headerDelegate = table.getTableHeader().getDefaultRenderer();
        this.cellDelegate = table.getDefaultRenderer(Object.class);
        table.setDefaultRenderer(Object.class, (jtable, o, bln, bln1, row, column) -> {
            JLabel label = (JLabel) cellDelegate.getTableCellRendererComponent(jtable, o, bln, bln1, row, column);
            label.setHorizontalAlignment(getAlignment(column));
            return label;
        });
    }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        JLabel label = (JLabel) headerDelegate.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
        label.setHorizontalAlignment(getAlignment(column));
        return label;
    }

    protected int getAlignment(int column) {
        return SwingConstants.CENTER;
    }
}
