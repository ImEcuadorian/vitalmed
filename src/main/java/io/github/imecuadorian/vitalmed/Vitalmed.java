package io.github.imecuadorian.vitalmed;

import com.formdev.flatlaf.*;
import com.formdev.flatlaf.themes.*;
import io.github.imecuadorian.vitalmed.util.*;
import io.github.imecuadorian.vitalmed.view.forms.auth.*;
import io.github.imecuadorian.vitalmed.view.menu.*;
import io.github.imecuadorian.vitalmed.view.system.*;
import net.miginfocom.swing.*;
import raven.modal.*;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.*;
import java.util.logging.*;

public class Vitalmed extends JFrame {

    private static final Logger LOGGER = Logger.getLogger("VitalmedLogger");


    public static void main(String[] args) {
        FlatLaf.registerCustomDefaultsSource("io.github.imecuadorian.vitalmed.ui");
        FlatMacDarkLaf.setup();
        EventQueue.invokeLater(() -> {
            try {

                Vitalmed window = new Vitalmed();
                window.setVisible(true);
            } catch (Exception e) {
                LOGGER.log(Level.SEVERE, "Error initializing the application", e);
                JOptionPane.showMessageDialog(null, "Error initializing the application: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
    }

    public Vitalmed() {
        init();
    }

    private void init() {
        setTitle("Vitalmed");
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int width = (int) (screenSize.width * 0.8);
        int height = (int) (screenSize.height * 0.8);
        setSize(new Dimension(width, height));
        setLocationRelativeTo(null);
        setLayout(new MigLayout("al center center"));
        add(new FormLogin(this));  // esto es correcto
        setUndecorated(true);
        setShape(new RoundRectangle2D.Double(0, 0, width, height, 100, 100));
        setIconImage(Toolkit.getDefaultToolkit().getImage(
                getClass().getResource("/io/github/imecuadorian/vitalmed/images/vitalmed-main-icon.png")));
    }

}

