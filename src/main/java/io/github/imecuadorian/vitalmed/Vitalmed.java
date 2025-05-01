package io.github.imecuadorian.vitalmed;

import io.github.imecuadorian.vitalmed.controller.*;
import io.github.imecuadorian.vitalmed.factory.*;
import io.github.imecuadorian.vitalmed.view.*;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;
import java.util.*;
import java.util.logging.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class Vitalmed {

    private static final Logger LOGGER = Logger.getLogger("VitalmedLogger");
    private static final String DEFAULT_FONT = "Segoe UI";

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

    public Vitalmed() {
        initialize();
    }

    private void initialize() {
        JPasswordField txtPassword;

        LoginController loginController = new LoginController(
                ServiceFactory.getAdminService(),
                ServiceFactory.getDoctorAuth(),
                ServiceFactory.getPatientAuth()
        );

        frame = new JFrame();
        frame.setUndecorated(true);
        frame.setBackground(Color.WHITE);
        frame.setSize(760, 360);
        frame.setLocationRelativeTo(null);
        frame.setShape(new RoundRectangle2D.Double(0, 0, frame.getWidth(), frame.getHeight(), 25, 25));
        frame.setIconImage(Toolkit.getDefaultToolkit().getImage(getClass().getResource("/io/github/imecuadorian/images/vitalmed-main-icon.png")));
        assert frame != null;
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.getContentPane().setLayout(new BorderLayout(0, 0));

        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        splitPane.setDividerLocation(300);
        splitPane.setDividerSize(1);
        splitPane.setBorder(null);
        splitPane.setBackground(Color.WHITE);
        splitPane.setContinuousLayout(true);
        frame.getContentPane().add(splitPane, BorderLayout.CENTER);

        JPanel leftPanel = new JPanel(new GridBagLayout());
        leftPanel.setBackground(Color.WHITE);
        splitPane.setLeftComponent(leftPanel);

        ImageIcon icon = new ImageIcon(Objects.requireNonNull(getClass().getResource("/io/github/imecuadorian/images/vitalmed-main-logo.png")));
        Image scaledImage = icon.getImage().getScaledInstance(220, 220, Image.SCALE_SMOOTH);
        JLabel lblLogo = new JLabel(new ImageIcon(scaledImage));
        leftPanel.add(lblLogo);

        JPanel rightPanel = new JPanel();
        rightPanel.setBackground(Color.WHITE);
        rightPanel.setBorder(new EmptyBorder(10, 20, 10, 20));
        splitPane.setRightComponent(rightPanel);

        GridBagLayout gbl = new GridBagLayout();
        gbl.columnWidths = new int[]{10, 120, 220, 10};
        gbl.rowHeights = new int[] {30, 25, 20, 20, 20, 30, 30, 30, 25};
        gbl.columnWeights = new double[]{0.0, 0.0, 1.0, Double.MIN_VALUE};
        gbl.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
        rightPanel.setLayout(gbl);

        JLabel lblTitle = new JLabel("Bienvenido a Vitalmed");
        ImageIcon mainIconTitle = new ImageIcon(Objects.requireNonNull(getClass().getResource("/io/github/imecuadorian/images/vitalmed-main-icon.png")));
        Image scaledImageTitle = mainIconTitle.getImage().getScaledInstance(32, 32, Image.SCALE_SMOOTH);
        lblTitle.setIcon(new ImageIcon(scaledImageTitle));
        lblTitle.setFont(new Font(DEFAULT_FONT, Font.BOLD, 22));
        GridBagConstraints gbcLblTitle = new GridBagConstraints();
        gbcLblTitle.gridwidth = 2;
        gbcLblTitle.insets = new Insets(10, 0, 20, 0);
        gbcLblTitle.gridx = 1;
        gbcLblTitle.gridy = 1;
        rightPanel.add(lblTitle, gbcLblTitle);

        JLabel lblUser = new JLabel("Email:");
        lblUser.setIcon(new ImageIcon(Objects.requireNonNull(getClass().getResource("/io/github/imecuadorian/images/email-icon-16px.png"))));
        lblUser.setFont(new Font(DEFAULT_FONT, Font.PLAIN, 16));
        GridBagConstraints gbcLblUser = new GridBagConstraints();
        gbcLblUser.anchor = GridBagConstraints.EAST;
        gbcLblUser.insets = new Insets(5, 0, 5, 5);
        gbcLblUser.gridx = 1;
        gbcLblUser.gridy = 2;
        rightPanel.add(lblUser, gbcLblUser);

        JTextField txtUser = createStyledTextField();
        GridBagConstraints gbcTxtUser = new GridBagConstraints();
        gbcTxtUser.fill = GridBagConstraints.HORIZONTAL;
        gbcTxtUser.insets = new Insets(5, 0, 5, 0);
        gbcTxtUser.gridx = 2;
        gbcTxtUser.gridy = 2;
        rightPanel.add(txtUser, gbcTxtUser);

        JLabel lblPassword = new JLabel("Contraseña:");
        lblPassword.setIcon(new ImageIcon(Objects.requireNonNull(getClass().getResource("/io/github/imecuadorian/images/password-icon-16px.png"))));
        lblPassword.setFont(new Font(DEFAULT_FONT, Font.PLAIN, 16));
        GridBagConstraints gbcLblPassword = new GridBagConstraints();
        gbcLblPassword.anchor = GridBagConstraints.EAST;
        gbcLblPassword.insets = new Insets(5, 0, 5, 5);
        gbcLblPassword.gridx = 1;
        gbcLblPassword.gridy = 3;
        rightPanel.add(lblPassword, gbcLblPassword);

        txtPassword = new JPasswordField();
        txtPassword.setFont(new Font(DEFAULT_FONT, Font.PLAIN, 16));
        txtPassword.setToolTipText("Ingrese su contraseña");
        txtPassword.setBorder(BorderFactory.createCompoundBorder(new RoundedBorder(10), new EmptyBorder(8, 10, 8, 10)));
        txtPassword.setBackground(new Color(245, 245, 245));
        GridBagConstraints gbcTxtPassword = new GridBagConstraints();
        gbcTxtPassword.fill = GridBagConstraints.HORIZONTAL;
        gbcTxtPassword.insets = new Insets(5, 0, 5, 0);
        gbcTxtPassword.gridx = 2;
        gbcTxtPassword.gridy = 3;
        rightPanel.add(txtPassword, gbcTxtPassword);

        JCheckBox showPassword = new JCheckBox("Mostrar contraseña");
        showPassword.setFont(new Font(DEFAULT_FONT, Font.PLAIN, 12));
        showPassword.setBackground(Color.WHITE);
        showPassword.addActionListener(e -> txtPassword.setEchoChar(showPassword.isSelected() ? (char) 0 : '•'));
        GridBagConstraints gbcChkShow = new GridBagConstraints();
        gbcChkShow.anchor = GridBagConstraints.WEST;
        gbcChkShow.insets = new Insets(0, 0, 10, 0);
        gbcChkShow.gridx = 2;
        gbcChkShow.gridy = 4;
        rightPanel.add(showPassword, gbcChkShow);

        JButton btnLogin = createRoundedButton();
        GridBagConstraints gbcBtnLogin = new GridBagConstraints();
        gbcBtnLogin.insets = new Insets(5, 0, 10, 0);
        gbcBtnLogin.gridx = 2;
        gbcBtnLogin.gridy = 5;
        rightPanel.add(btnLogin, gbcBtnLogin);

        JLabel lblRegister = new JLabel("¿No tienes cuenta? Regístrate");
        lblRegister.addMouseListener(new MouseAdapter() {
        	@Override
        	public void mouseClicked(MouseEvent e) {
                new RegisterWindow().setVisible(true);
        	}
        });
        lblRegister.setFont(new Font(DEFAULT_FONT, Font.PLAIN, 13));
        lblRegister.setForeground(new Color(0, 102, 204));
        lblRegister.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        lblRegister.setToolTipText("Haz clic para registrarte si aún no tienes cuenta");
        GridBagConstraints gbcLblRegister = new GridBagConstraints();
        gbcLblRegister.insets = new Insets(5, 0, 5, 0);
        gbcLblRegister.gridx = 2;
        gbcLblRegister.gridy = 6;
        rightPanel.add(lblRegister, gbcLblRegister);
    }

    private JTextField createStyledTextField() {
        JTextField textField = new JTextField();
        textField.setFont(new Font(DEFAULT_FONT, Font.PLAIN, 16));
        textField.setToolTipText("Ingrese su correo electrónico");
        textField.setBorder(BorderFactory.createCompoundBorder(new RoundedBorder(10), new EmptyBorder(8, 10, 8, 10)));
        textField.setBackground(new Color(245, 245, 245));
        return textField;
    }

    private JButton createRoundedButton() {
        JButton button = new JButton("Iniciar Sesión") {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(getBackground());
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);
                super.paintComponent(g2);
                g2.dispose();
            }
            @Override
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
        return button;
    }

    static class RoundedBorder extends AbstractBorder {
        private final int radius;
        public RoundedBorder(int radius) {
            this.radius = radius;
        }
        @Override
        public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
            g.setColor(Color.LIGHT_GRAY);
            g.drawRoundRect(x, y, width - 1, height - 1, radius, radius);
        }
        @Override
        public Insets getBorderInsets(Component c) {
            return new Insets(radius + 2, radius + 2, radius + 2, radius + 2);
        }
        @Override
        public Insets getBorderInsets(Component c, Insets insets) {
            insets.set(radius + 2, radius + 2, radius + 2, radius + 2);
            return insets;
        }
    }
}