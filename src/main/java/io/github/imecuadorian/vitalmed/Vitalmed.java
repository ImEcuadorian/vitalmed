package io.github.imecuadorian.vitalmed;

import com.formdev.flatlaf.*;
import com.formdev.flatlaf.themes.*;
import io.github.imecuadorian.vitalmed.view.forms.auth.*;
import net.miginfocom.swing.*;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.*;
import java.util.logging.*;

public class Vitalmed {

    private static final Logger LOGGER = Logger.getLogger("VitalmedLogger");
    private JFrame frame;


    public static void main(String[] args) {
        FlatLaf.registerCustomDefaultsSource("io.github.imecuadorian.vitalmed.ui");
        FlatMacDarkLaf.setup();
        EventQueue.invokeLater(() -> {
            try {

                Vitalmed window = new Vitalmed();
                window.frame.setVisible(true);
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
        frame = new JFrame("Login - Vitalmed");
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int width = (int) (screenSize.width * 0.8);
        int height = (int) (screenSize.height * 0.8);
        frame.setSize(new Dimension(width, height));
        frame.setLocationRelativeTo(null);
        frame.setLayout(new MigLayout("al center center"));
        frame.add(new FormLogin(frame));
        frame.setUndecorated(true);
        frame.setShape(new RoundRectangle2D.Double(0, 0, frame.getWidth(), frame.getHeight(), 40, 40));
        frame.setIconImage(Toolkit.getDefaultToolkit().getImage(getClass().getResource("/io/github/imecuadorian/vitalmed/images/vitalmed-main-icon.png")));
    }

}

