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
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        getRootPane().putClientProperty(FlatClientProperties.FULL_WINDOW_CONTENT, true);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setVisible(true);
        FormManager.install(this);
        Drawer.installDrawer(this, MyDrawerBuilder.getInstance());
        AppPreferences.setupLaf();
        setIconImage(Toolkit.getDefaultToolkit().getImage(getClass().getResource("/io/github/imecuadorian/vitalmed/images/vitalmed-main-icon.png")));
    }
}
