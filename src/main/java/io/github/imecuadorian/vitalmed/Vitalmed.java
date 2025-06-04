package io.github.imecuadorian.vitalmed;

import com.formdev.flatlaf.*;
import com.formdev.flatlaf.themes.*;
import io.github.imecuadorian.vitalmed.util.*;
import io.github.imecuadorian.vitalmed.view.forms.auth.*;
import net.miginfocom.swing.*;
import org.slf4j.*;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.*;

public class Vitalmed extends JFrame {

    private static final Logger LOGGER = LoggerFactory.getLogger(Vitalmed.class);


    public static void main(String[] args) {
        LOGGER.info("Starting Vitalmed application...");
        FlatLaf.registerCustomDefaultsSource("io.github.imecuadorian.vitalmed.ui");
        FlatMacDarkLaf.setup();
        AppPreferences.init();
        EventQueue.invokeLater(() -> {
            try {
                Vitalmed window = new Vitalmed();
                window.setVisible(true);
            } catch (Exception e) {
                LOGGER.error("Error initializing the application", e);
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
        add(new FormLogin(this));
        setUndecorated(true);
        setShape(new RoundRectangle2D.Double(0, 0, width, height, 100, 100));
        setIconImage(Toolkit.getDefaultToolkit().getImage(
                getClass().getResource("/io/github/imecuadorian/vitalmed/images/vitalmed-main-icon.png")));
    }

}

