package io.github.imecuadorian.vitalmed.view;

import com.formdev.flatlaf.*;
import io.github.imecuadorian.vitalmed.util.*;
import io.github.imecuadorian.vitalmed.view.menu.*;
import io.github.imecuadorian.vitalmed.view.system.*;
import raven.modal.*;

import javax.swing.*;
import java.awt.*;

public class MainDashboard extends JFrame {

    public MainDashboard() {
        init();
    }

    private void init() {
        setTitle("VitalMed " + AppPreferences.VERSION);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        getRootPane().putClientProperty(FlatClientProperties.FULL_WINDOW_CONTENT, true);
        setExtendedState(MAXIMIZED_BOTH);
        setMinimumSize(new Dimension(1024, 720));
        setVisible(true);
        FormManager.install(this);
        Drawer.installDrawer(this, MyDrawerBuilder.getInstance());
        AppPreferences.setupLaf();
        setIconImage(Toolkit.getDefaultToolkit().getImage(getClass().getResource("/io/github/imecuadorian/vitalmed/images/vitalmed-main-icon.png")));
    }
}
