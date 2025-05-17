package io.github.imecuadorian.vitalmed.view.forms;

import com.formdev.flatlaf.*;
import com.formdev.flatlaf.util.*;
import io.github.imecuadorian.vitalmed.util.*;
import io.github.imecuadorian.vitalmed.view.system.*;
import net.miginfocom.swing.*;

import javax.swing.*;
import java.awt.*;

@SystemForm(name = "Dashboard", description = "Panel para mostrar el estado de la aplicacion")
public class FormDashboard extends Form {
    private JPanel panelLayout;
    public FormDashboard() {
        init();
    }

    private void init() {
        setLayout(new MigLayout("wrap,fill", "[fill]", "[grow 0][fill]"));
        createTitle();
        createPanelLayout();
    }

    @Override
    public void formInit() {

    }

    @Override
    public void formRefresh() {

    }


    private void createTitle() {
        JPanel panel = new JPanel(new MigLayout("fillx", "[]push[][]"));
        JLabel title = new JLabel("Dashboard");

        title.putClientProperty(FlatClientProperties.STYLE, "" +
                                                            "font:bold +3");

        add(panel);
    }

    private void createPanelLayout() {
        panelLayout = new JPanel(new DashboardLayout());
        JScrollPane scrollPane = new JScrollPane(panelLayout);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.getVerticalScrollBar().setUnitIncrement(10);
        scrollPane.getVerticalScrollBar().putClientProperty(FlatClientProperties.STYLE, "width:5;" +
                                                                                        "trackArc:$ScrollBar.thumbArc;" +
                                                                                        "trackInsets:0,0,0,0;" +
                                                                                        "thumbInsets:0,0,0,0;");
        add(scrollPane);
    }


}

class DashboardLayout implements LayoutManager {

    private int gap = 0;

    @Override
    public void addLayoutComponent(String name, Component comp) {
    }

    @Override
    public void removeLayoutComponent(Component comp) {
    }

    @Override
    public Dimension preferredLayoutSize(Container parent) {
        synchronized (parent.getTreeLock()) {
            Insets insets = parent.getInsets();
            int width = (insets.left + insets.right);
            int height = insets.top + insets.bottom;
            int g = UIScale.scale(gap);
            int count = parent.getComponentCount();
            for (int i = 0; i < count; i++) {
                Component com = parent.getComponent(i);
                Dimension size = com.getPreferredSize();
                height += size.height;
            }
            if (count > 1) {
                height += (count - 1) * g;
            }
            return new Dimension(width, height);
        }
    }

    @Override
    public Dimension minimumLayoutSize(Container parent) {
        synchronized (parent.getTreeLock()) {
            return new Dimension(10, 10);
        }
    }

    @Override
    public void layoutContainer(Container parent) {
        synchronized (parent.getTreeLock()) {
            Insets insets = parent.getInsets();
            int x = insets.left;
            int y = insets.top;
            int width = parent.getWidth() - (insets.left + insets.right);
            int g = UIScale.scale(gap);
            int count = parent.getComponentCount();
            for (int i = 0; i < count; i++) {
                Component com = parent.getComponent(i);
                Dimension size = com.getPreferredSize();
                com.setBounds(x, y, width, size.height);
                y += size.height + g;
            }
        }
    }
}
