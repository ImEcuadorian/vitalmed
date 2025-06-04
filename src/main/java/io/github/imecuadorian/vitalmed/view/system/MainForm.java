package io.github.imecuadorian.vitalmed.view.system;

import com.formdev.flatlaf.*;
import com.formdev.flatlaf.extras.*;
import io.github.imecuadorian.vitalmed.i18n.*;
import io.github.imecuadorian.vitalmed.view.component.*;
import io.github.imecuadorian.vitalmed.view.forms.search.*;
import net.miginfocom.swing.*;
import org.jetbrains.annotations.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import raven.modal.*;

import javax.swing.*;
import java.awt.*;
import java.util.*;

public class MainForm extends JPanel {

    private static final Logger LOGGER = LoggerFactory.getLogger(MainForm.class);
    private static final String STYLE_ARC = "arc:10;";
    private static final String STYLE_FILL = "[fill]";

    private JPanel mainPanel;
    private RefreshLine refreshLine;
    private JButton buttonUndo;
    private JButton buttonRedo;

    public MainForm() {
        LOGGER.info("Initializing MainForm panel");
        init();
    }

    private void init() {
        LOGGER.debug("Setting up layout and components");
        setLayout(new MigLayout("fillx,wrap,insets 0,gap 0", STYLE_FILL, "[][][fill,grow][]"));
        add(createHeader());
        add(createRefreshLine(), "height 3!");
        add(createMain());
        add(new JSeparator(), "height 2!");
        add(createFooter());
    }

    private @NotNull JPanel createHeader() {
        LOGGER.debug("Creating a header with toolbar buttons");
        JButton buttonRefresh;
        JPanel panel = new JPanel(new MigLayout("insets 3", "[]push[]push", STYLE_FILL));
        JToolBar toolBar = new JToolBar();
        JButton buttonDrawer = new JButton(new FlatSVGIcon("io/github/imecuadorian/vitalmed/icon/menu.svg", 0.5f));
        buttonUndo = new JButton(new FlatSVGIcon("io/github/imecuadorian/vitalmed/icon/undo.svg", 0.5f));
        buttonRedo = new JButton(new FlatSVGIcon("io/github/imecuadorian/vitalmed/icon/redo.svg", 0.5f));
        buttonRefresh = new JButton(new FlatSVGIcon("io/github/imecuadorian/vitalmed/icon/refresh.svg", 0.5f));

        buttonDrawer.putClientProperty(FlatClientProperties.STYLE, STYLE_ARC);
        buttonUndo.putClientProperty(FlatClientProperties.STYLE, STYLE_ARC);
        buttonRedo.putClientProperty(FlatClientProperties.STYLE, STYLE_ARC);
        buttonRefresh.putClientProperty(FlatClientProperties.STYLE, STYLE_ARC);

        buttonDrawer.addActionListener(e -> {
            LOGGER.info("Toggle drawer button pressed");
            if (Drawer.isOpen()) {
                Drawer.showDrawer();
            } else {
                Drawer.toggleMenuOpenMode();
            }
        });
        buttonUndo.addActionListener(e -> {
            LOGGER.info("Undo button pressed");
            FormManager.undo();
        });
        buttonRedo.addActionListener(e -> {
            LOGGER.info("Redo button pressed");
            FormManager.redo();
        });
        buttonRefresh.addActionListener(e -> {
            LOGGER.info("Refresh button pressed");
            FormManager.refresh();
        });

        toolBar.add(buttonDrawer);
        toolBar.add(buttonUndo);
        toolBar.add(buttonRedo);
        toolBar.add(buttonRefresh);
        panel.add(toolBar);
        panel.add(createSearchBox(), "gapx n 135");
        return panel;
    }

    private @NotNull JPanel createFooter() {
        LOGGER.debug("Creating footer with language toggle");
        JPanel panel = new JPanel(new MigLayout("insets 0 10 0 10, fillx, aligny center", "push[]", "center"));
        panel.putClientProperty(FlatClientProperties.STYLE,
                "[light]background:tint($Panel.background,20%);[dark]background:tint($Panel.background,5%);");

        JButton btnLang = new JButton(I18n.getLocale().getLanguage().equals("es") ? "EN" : "ES");
        btnLang.setFocusable(false);
        btnLang.setPreferredSize(new Dimension(50, 26));
        btnLang.putClientProperty(FlatClientProperties.STYLE, STYLE_ARC);
        btnLang.addActionListener(e -> {
            Locale current = I18n.getLocale();
            Locale next = current.getLanguage().equals("es") ? Locale.ENGLISH : Locale.of("es", "EC");
            LOGGER.info("Language change requested: {} -> {}", current, next);
            I18n.setLocale(next);
            SwingUtilities.updateComponentTreeUI(SwingUtilities.getWindowAncestor(panel));
            btnLang.setText(next.getLanguage().equals("es") ? "EN" : "ES");
        });

        panel.add(btnLang);
        return panel;
    }

    private @NotNull JPanel createSearchBox() {
        LOGGER.debug("Creating a search box");
        JPanel panel = new JPanel(new MigLayout("fill", "[fill,center,200:250:]", STYLE_FILL));
        FormSearchButton button = new FormSearchButton();
        button.addActionListener(e -> {
            LOGGER.info("Search button clicked");
            FormSearch.INSTANCE.showSearch();
        });
        panel.add(button);
        return panel;
    }

    private JPanel createRefreshLine() {
        LOGGER.debug("Creating a refresh line");
        refreshLine = new RefreshLine();
        return refreshLine;
    }

    private Component createMain() {
        LOGGER.debug("Creating the main content area");
        mainPanel = new JPanel(new BorderLayout());
        return mainPanel;
    }

    public void setForm(@NotNull Form form) {
        LOGGER.info("Setting a new form in the main panel: {}", form.getClass().getSimpleName());
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
        LOGGER.debug("Refreshing refresh line");
        refreshLine.refresh();
    }
}
