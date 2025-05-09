package io.github.imecuadorian.vitalmed.view.system;

import com.formdev.flatlaf.*;
import com.formdev.flatlaf.extras.*;
import io.github.imecuadorian.vitalmed.i18n.*;
import io.github.imecuadorian.vitalmed.view.component.*;
import io.github.imecuadorian.vitalmed.view.forms.search.*;
import net.miginfocom.swing.*;
import raven.modal.*;

import javax.swing.*;
import java.awt.*;
import java.util.*;

public class MainForm extends JPanel {

    public MainForm() {
        init();
    }

    private void init() {
        setLayout(new MigLayout("fillx,wrap,insets 0,gap 0", "[fill]", "[][][fill,grow][]"));
        add(createHeader());
        add(createRefreshLine(), "height 3!");
        add(createMain());
        add(new JSeparator(), "height 2!");
        add(createFooter());
    }

    private JPanel createHeader() {
        JPanel panel = new JPanel(new MigLayout("insets 3", "[]push[]push", "[fill]"));
        JToolBar toolBar = new JToolBar();
        JButton buttonDrawer = new JButton(new FlatSVGIcon("io/github/imecuadorian/vitalmed/icon/menu.svg", 0.5f));
        buttonUndo = new JButton(new FlatSVGIcon("io/github/imecuadorian/vitalmed/icon/undo.svg", 0.5f));
        buttonRedo = new JButton(new FlatSVGIcon("io/github/imecuadorian/vitalmed/icon/redo.svg", 0.5f));
        buttonRefresh = new JButton(new FlatSVGIcon("io/github/imecuadorian/vitalmed/icon/refresh.svg", 0.5f));

        buttonDrawer.putClientProperty(FlatClientProperties.STYLE, "" +
                                                                   "arc:10;");
        buttonUndo.putClientProperty(FlatClientProperties.STYLE, "" +
                                                                 "arc:10;");
        buttonRedo.putClientProperty(FlatClientProperties.STYLE, "" +
                                                                 "arc:10;");
        buttonRefresh.putClientProperty(FlatClientProperties.STYLE, "" +
                                                                    "arc:10;");

        buttonDrawer.addActionListener(e -> {
            if (Drawer.isOpen()) {
                Drawer.showDrawer();
            } else {
                Drawer.toggleMenuOpenMode();
            }
        });
        buttonUndo.addActionListener(e -> FormManager.undo());
        buttonRedo.addActionListener(e -> FormManager.redo());
        buttonRefresh.addActionListener(e -> FormManager.refresh());

        toolBar.add(buttonDrawer);
        toolBar.add(buttonUndo);
        toolBar.add(buttonRedo);
        toolBar.add(buttonRefresh);
        panel.add(toolBar);
        panel.add(createSearchBox(), "gapx n 135");
        return panel;
    }

    private JPanel createFooter() {
        JPanel panel = new JPanel(new MigLayout("insets 1 n 1 n,al trailing center,gapx 10,height 30!", "[]push[][]", "fill"));
        panel.putClientProperty(FlatClientProperties.STYLE, "" +
                                                            "[light]background:tint($Panel.background,20%);" +
                                                            "[dark]background:tint($Panel.background,5%);");

        JButton btnEs = new JButton("ES");
        btnEs.setFocusable(false);
        btnEs.setToolTipText("Español");
        btnEs.addActionListener(e -> {
            I18n.setLocale(new Locale("es"));
            SwingUtilities.updateComponentTreeUI(SwingUtilities.getWindowAncestor(panel));
        });

        JButton btnEn = new JButton("EN");
        btnEn.setFocusable(false);
        btnEn.setToolTipText("English");
        btnEn.addActionListener(e -> {
            I18n.setLocale(new Locale("en"));
            SwingUtilities.updateComponentTreeUI(SwingUtilities.getWindowAncestor(panel));
        });

        panel.add(btnEs);
        panel.add(btnEn);

        return panel;
    }


    private JPanel createSearchBox() {
        JPanel panel = new JPanel(new MigLayout("fill", "[fill,center,200:250:]", "[fill]"));
        FormSearchButton button = new FormSearchButton();
        button.addActionListener(e -> FormSearch.getInstance().showSearch());
        panel.add(button);
        return panel;
    }

    private JPanel createRefreshLine() {
        refreshLine = new RefreshLine();
        return refreshLine;
    }

    private Component createMain() {
        mainPanel = new JPanel(new BorderLayout());
        return mainPanel;
    }

    public void setForm(Form form) {
        mainPanel.removeAll();
        mainPanel.add(form);
        mainPanel.repaint();
        mainPanel.revalidate();

        buttonUndo.setEnabled(FormManager.FORMS.isUndoAble());
        buttonRedo.setEnabled(FormManager.FORMS.isRedoAble());
        if (mainPanel.getComponentOrientation().isLeftToRight() != form.getComponentOrientation().isLeftToRight()) {
            applyComponentOrientation(mainPanel.getComponentOrientation());
        }
    }

    public void refresh() {
        refreshLine.refresh();
    }

    private JPanel mainPanel;
    private RefreshLine refreshLine;

    private JButton buttonUndo;
    private JButton buttonRedo;
    private JButton buttonRefresh;
}
