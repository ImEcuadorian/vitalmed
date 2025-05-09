package io.github.imecuadorian.vitalmed.view;

import com.formdev.flatlaf.*;
import io.github.imecuadorian.vitalmed.util.*;
import io.github.imecuadorian.vitalmed.view.menu.*;
import io.github.imecuadorian.vitalmed.view.system.*;
import raven.modal.*;

import javax.swing.*;

public class MainDashboard extends JFrame {

    public MainDashboard() {
        init();
    }

    private void init() {
        AppPreferences.init();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        getRootPane().putClientProperty(FlatClientProperties.FULL_WINDOW_CONTENT, true);
        Drawer.installDrawer(this, MyDrawerBuilder.getInstance());
        FormManager.install(this);
        AppPreferences.setupLaf();

        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setVisible(true);
    }
}
