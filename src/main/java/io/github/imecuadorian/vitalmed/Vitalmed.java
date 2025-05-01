
package io.github.imecuadorian.vitalmed;

import io.github.imecuadorian.vitalmed.controller.LoginController;
import io.github.imecuadorian.vitalmed.factory.ServiceFactory;
import io.github.imecuadorian.vitalmed.util.RegexValidator;
import io.github.imecuadorian.vitalmed.util.TextPrompt;
import io.github.imecuadorian.vitalmed.util.TextPrompt.Show;
import io.github.imecuadorian.vitalmed.view.*;

import javax.swing.*;
import javax.swing.border.AbstractBorder;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.RoundRectangle2D;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Vitalmed {

    private static final Logger LOGGER = Logger.getLogger("VitalmedLogger");
    private static final String DEFAULT_FONT = "Segoe UI";

    private JTextField txtUser;
    private JPasswordField txtPassword;
    private JFrame frame;

    public static void main(String[] args) {
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

    LoginController loginController = new LoginController(
            ServiceFactory.getAdminService(),
            ServiceFactory.getDoctorAuth(),
            ServiceFactory.getPatientAuth()
    );

    public Vitalmed() {
        initialize();
    }

    private void initialize() {
        frame = new JFrame();
        frame.setUndecorated(true);
        frame.setBackground(Color.WHITE);
        frame.setSize(900, 360);
        frame.setLocationRelativeTo(null);
        frame.setShape(new RoundRectangle2D.Double(0, 0, frame.getWidth(), frame.getHeight(), 25, 25));
        frame.setIconImage(Toolkit.getDefaultToolkit().getImage(getClass().getResource("/io/github/imecuadorian/images/vitalmed-main-icon.png")));
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.getContentPane().setLayout(new BorderLayout());

        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        splitPane.setDividerLocation(300);
        splitPane.setDividerSize(1);
        splitPane.setBorder(null);
        splitPane.setBackground(Color.WHITE);
        frame.getContentPane().add(splitPane, BorderLayout.CENTER);

        JPanel leftPanel = new JPanel(new GridBagLayout());
        leftPanel.setBackground(Color.WHITE);
        splitPane.setLeftComponent(leftPanel);

        ImageIcon icon = new ImageIcon(Objects.requireNonNull(getClass().getResource("/io/github/imecuadorian/images/vitalmed-main-logo.png")));
        Image scaledImage = icon.getImage().getScaledInstance(220, 220, Image.SCALE_SMOOTH);
        JLabel lblLogo = new JLabel(new ImageIcon(scaledImage));
        leftPanel.add(lblLogo);

        JPanel rightPanel = new JPanel(new GridBagLayout());
        rightPanel.setBackground(Color.WHITE);
        rightPanel.setBorder(new EmptyBorder(10, 20, 10, 20));
        splitPane.setRightComponent(rightPanel);

        GridBagConstraints gbc;

        JLabel lblTitle = new JLabel("Bienvenido a Vitalmed");
        ImageIcon mainIcon = new ImageIcon(getClass().getResource("/io/github/imecuadorian/images/vitalmed-main-icon.png"));
        lblTitle.setIcon(new ImageIcon(mainIcon.getImage().getScaledInstance(32, 32, Image.SCALE_SMOOTH)));
        lblTitle.setFont(new Font(DEFAULT_FONT, Font.BOLD, 22));
        gbc = new GridBagConstraints();
        gbc.gridwidth = 2;
        gbc.insets = new Insets(10, 0, 20, 0);
        gbc.gridx = 1;
        gbc.gridy = 1;
        rightPanel.add(lblTitle, gbc);

        JLabel lblUser = new JLabel("Email:");
        lblUser.setIcon(new ImageIcon(getClass().getResource("/io/github/imecuadorian/images/email-icon-16px.png")));
        lblUser.setFont(new Font(DEFAULT_FONT, Font.PLAIN, 16));
        gbc = new GridBagConstraints();
        gbc.anchor = GridBagConstraints.EAST;
        gbc.insets = new Insets(5, 0, 5, 5);
        gbc.gridx = 1;
        gbc.gridy = 2;
        rightPanel.add(lblUser, gbc);

        txtUser = createStyledTextField();
        new TextPrompt("ejemplo@correo.com", txtUser, Show.FOCUS_LOST);
        gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 0, 5, 0);
        gbc.gridx = 2;
        gbc.gridy = 2;
        rightPanel.add(txtUser, gbc);

        JLabel lblPassword = new JLabel("Contraseña:");
        lblPassword.setIcon(new ImageIcon(getClass().getResource("/io/github/imecuadorian/images/password-icon-16px.png")));
        lblPassword.setFont(new Font(DEFAULT_FONT, Font.PLAIN, 16));
        gbc = new GridBagConstraints();
        gbc.anchor = GridBagConstraints.EAST;
        gbc.insets = new Insets(5, 0, 5, 5);
        gbc.gridx = 1;
        gbc.gridy = 3;
        rightPanel.add(lblPassword, gbc);

        txtPassword = new JPasswordField();
        txtPassword.setFont(new Font(DEFAULT_FONT, Font.PLAIN, 16));
        txtPassword.setBorder(BorderFactory.createCompoundBorder(
                new RoundedBorder(10),
                new EmptyBorder(4, 20, 4, 20)  // mismo estilo
        ));
        txtPassword.setColumns(20);
        txtPassword.setPreferredSize(null);
        new TextPrompt("********", txtPassword, Show.FOCUS_LOST);
        gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 0, 5, 0);
        gbc.gridx = 2;
        gbc.gridy = 3;
        rightPanel.add(txtPassword, gbc);

        JCheckBox showPassword = new JCheckBox("Mostrar contraseña");
        showPassword.setFont(new Font(DEFAULT_FONT, Font.PLAIN, 12));
        showPassword.setBackground(Color.WHITE);
        showPassword.addActionListener(e -> txtPassword.setEchoChar(showPassword.isSelected() ? (char) 0 : '•'));
        gbc = new GridBagConstraints();
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(0, 0, 10, 0);
        gbc.gridx = 2;
        gbc.gridy = 4;
        rightPanel.add(showPassword, gbc);

        JButton btnLogin = createRoundedButton();
        gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 0, 10, 0);
        gbc.gridx = 2;
        gbc.gridy = 5;
        rightPanel.add(btnLogin, gbc);

        JLabel lblRegister = new JLabel("¿No tienes cuenta? Regístrate");
        lblRegister.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                new RegisterWindow().setVisible(true);
            }
        });
        lblRegister.setFont(new Font(DEFAULT_FONT, Font.PLAIN, 13));
        lblRegister.setForeground(new Color(0, 102, 204));
        lblRegister.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 0, 5, 0);
        gbc.gridx = 2;
        gbc.gridy = 6;
        rightPanel.add(lblRegister, gbc);
    }

    private JTextField createStyledTextField() {
        JTextField textField = new JTextField();
        textField.setFont(new Font(DEFAULT_FONT, Font.PLAIN, 16));
        textField.setBorder(BorderFactory.createCompoundBorder(
                new RoundedBorder(10),
                new EmptyBorder(6, 20, 6, 20)
        ));
        textField.setBackground(new Color(245, 245, 245));
        textField.setColumns(20);
        textField.setPreferredSize(null);
        return textField;
    }

    private JButton createRoundedButton() {
        JButton button = new JButton("Iniciar Sesión") {
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(getBackground());
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);
                super.paintComponent(g2);
                g2.dispose();
            }

            protected void paintBorder(Graphics g) {}
        };
        button.setFont(new Font(DEFAULT_FONT, Font.BOLD, 16));
        button.setBackground(new Color(33, 150, 243));
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setContentAreaFilled(false);
        button.setOpaque(false);
        button.setPreferredSize(new Dimension(180, 40));
        button.setToolTipText("Presione para acceder al sistema");
        button.addActionListener(e -> {
            String email = txtUser.getText();
            String password = new String(txtPassword.getPassword());

            if (!RegexValidator.isValidEmail(email)) {
                JOptionPane.showMessageDialog(frame, "Email no válido", "Error", JOptionPane.ERROR_MESSAGE);
            } else if (password.isEmpty()) {
                JOptionPane.showMessageDialog(frame, "Ingrese la contraseña", "Error", JOptionPane.ERROR_MESSAGE);
            } else {
                loginController.login(email, password, frame);
            }
        });
        return button;
    }

    static class RoundedBorder extends AbstractBorder {
        private final int radius;

        public RoundedBorder(int radius) {
            this.radius = radius;
        }

        public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
            g.setColor(Color.LIGHT_GRAY);
            g.drawRoundRect(x, y, width - 1, height - 1, radius, radius);
        }

        public Insets getBorderInsets(Component c) {
            return new Insets(radius + 2, radius + 2, radius + 2, radius + 2);
        }

        public Insets getBorderInsets(Component c, Insets insets) {
            insets.set(radius + 2, radius + 2, radius + 2, radius + 2);
            return insets;
        }
    }
}
