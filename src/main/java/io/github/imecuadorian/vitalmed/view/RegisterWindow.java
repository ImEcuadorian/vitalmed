
package io.github.imecuadorian.vitalmed.view;

import io.github.imecuadorian.vitalmed.controller.*;
import io.github.imecuadorian.vitalmed.factory.*;
import io.github.imecuadorian.vitalmed.model.*;
import io.github.imecuadorian.vitalmed.util.*;
import io.github.imecuadorian.vitalmed.util.TextPrompt.*;
import org.jetbrains.annotations.*;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import java.util.*;

public class RegisterWindow extends JFrame {

    private static final String DEFAULT_FONT = "Segoe UI";
    private final JTextField[] fields;
    private JPasswordField passwordField;
    private JPasswordField confirmPasswordField;
    private final JCheckBox showPasswordCheck;
    private final JPanel formPanel;
    private final String[] labels = {
            "Cédula", "Nombre completo", "Email", "Contraseña",
            "Confirmar contraseña", "Teléfono(Opcional)", "Celular", "Dirección"
    };

    private final RegistrationController registrationController = new RegistrationController(
            ServiceFactory.getPatientService()
    );

    public RegisterWindow() {
        setTitle("Registro de Usuario");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(1000, 800);
        setLocationRelativeTo(null);
        setUndecorated(true);
        setBackground(Color.WHITE);
        setShape(new RoundRectangle2D.Double(0, 0, getWidth(), getHeight(), 25, 25));
        getContentPane().setLayout(new BorderLayout());

        fields = new JTextField[labels.length];
        formPanel = new JPanel(new GridBagLayout());
        showPasswordCheck = new JCheckBox("Mostrar contraseñas");

        setupLayout();
        setupFields();
        setupButtons();

        setVisible(true);
    }

    private void setupLayout() {
        JSplitPane splitPane = new JSplitPane();
        splitPane.setDividerLocation(600);
        splitPane.setDividerSize(1);
        getContentPane().add(splitPane, BorderLayout.CENTER);

        formPanel.setBorder(new EmptyBorder(20, 40, 20, 40));
        formPanel.setBackground(Color.WHITE);
        splitPane.setLeftComponent(formPanel);

        JLabel lblTitle = new JLabel("Registro de Usuario");
        lblTitle.setFont(new Font(DEFAULT_FONT, Font.BOLD, 20));
        GridBagConstraints gbcTitle = new GridBagConstraints();
        gbcTitle.gridwidth = 2;
        gbcTitle.insets = new Insets(0, 0, 10, 0);
        gbcTitle.gridx = 0;
        gbcTitle.gridy = 0;
        formPanel.add(lblTitle, gbcTitle);

        JSeparator separator = new JSeparator();
        GridBagConstraints gbcSeparator = new GridBagConstraints();
        gbcSeparator.gridwidth = 2;
        gbcSeparator.gridx = 0;
        gbcSeparator.gridy = 1;
        gbcSeparator.insets = new Insets(5, 0, 20, 0);
        gbcSeparator.fill = GridBagConstraints.HORIZONTAL;
        formPanel.add(separator, gbcSeparator);

        JPanel imagePanel = new JPanel(new GridBagLayout());
        imagePanel.setBackground(Color.WHITE);
        ImageIcon logo = new ImageIcon(Objects.requireNonNull(getClass().getResource("/io/github/imecuadorian/images/vitalmed-main-logo.png")));
        Image scaled = logo.getImage().getScaledInstance(280, 280, Image.SCALE_SMOOTH);
        imagePanel.add(new JLabel(new ImageIcon(scaled)));
        splitPane.setRightComponent(imagePanel);
    }

    private void setupFields() {
        int row = 2;
        for (int i = 0; i < labels.length; i++) {
            JLabel lbl = new JLabel(labels[i] + ":");
            lbl.setFont(new Font(DEFAULT_FONT, Font.PLAIN, 14));
            lbl.setIcon(getFieldIcon(labels[i]));
            lbl.setIconTextGap(10);

            GridBagConstraints gbcLbl = new GridBagConstraints();
            gbcLbl.anchor = GridBagConstraints.EAST;
            gbcLbl.insets = new Insets(5, 0, 5, 10);
            gbcLbl.gridx = 0;
            gbcLbl.gridy = row;
            formPanel.add(lbl, gbcLbl);

            fields[i] = createField(labels[i]);
            GridBagConstraints gbcTxt = new GridBagConstraints();
            gbcTxt.fill = GridBagConstraints.HORIZONTAL;
            gbcTxt.gridx = 1;
            gbcTxt.gridy = row++;
            gbcTxt.insets = new Insets(5, 0, 5, 0);
            gbcTxt.weightx = 1.0;
            formPanel.add(fields[i], gbcTxt);

            if (labels[i].equals("Contraseña")) {
                GridBagConstraints hintGbc = new GridBagConstraints();
                hintGbc.gridwidth = 2;
                hintGbc.gridx = 0;
                hintGbc.gridy = row++;
                hintGbc.insets = new Insets(0, 0, 5, 0);
                JLabel hint = new JLabel("Debe tener al menos 8 caracteres, 1 mayúscula y 1 símbolo (@-/)");
                hint.setFont(new Font(DEFAULT_FONT, Font.ITALIC, 11));
                hint.setForeground(Color.GRAY);
                formPanel.add(hint, hintGbc);
            }
        }
    }

    private Icon getFieldIcon(String label) {
        String filename = switch (label) {
            case "Cédula" -> "id-icon-16px.png";
            case "Nombre completo" -> "user-icon-16px.png";
            case "Email" -> "email-icon-16px.png";
            case "Contraseña", "Confirmar contraseña" -> "password-icon-16px.png";
            case "Teléfono(Opcional)", "Celular" -> "phone-icon-16px.png";
            case "Dirección" -> "address-icon-16px.png";
            default -> null;
        };
        if (filename == null) return null;
        return new ImageIcon(Objects.requireNonNull(getClass().getResource("/io/github/imecuadorian/images/" + filename)));
    }

    private JTextField createField(String label) {
        JTextField field;
        if (label.equals("Contraseña")) {
            passwordField = new JPasswordField();
            field = passwordField;
        } else if (label.equals("Confirmar contraseña")) {
            confirmPasswordField = new JPasswordField();
            field = confirmPasswordField;
        } else {
            field = new JTextField();
        }

        field.setFont(new Font(DEFAULT_FONT, Font.PLAIN, 14));
        field.setBorder(BorderFactory.createCompoundBorder(new RoundedBorder(12), new EmptyBorder(5, 14, 5, 14)));
        field.setBackground(new Color(245, 245, 245));
        field.setToolTipText("Ingrese su " + label.toLowerCase());
        new TextPrompt("Ingrese " + label.toLowerCase(), field, Show.FOCUS_LOST);

        if (label.equals("Cédula") || label.equals("Teléfono") || label.equals("Celular")) {
            final int max = 10;
            field.addKeyListener(new KeyAdapter() {
                public void keyTyped(KeyEvent e) {
                    char c = e.getKeyChar();
                    if (!Character.isDigit(c) || field.getText().length() >= max) {
                        e.consume();
                    }
                }
            });
        }

        return field;
    }

    private void setupButtons() {
        int row = labels.length + 3;

        GridBagConstraints gbcCheck = new GridBagConstraints();
        gbcCheck.anchor = GridBagConstraints.WEST;
        gbcCheck.gridwidth = 2;
        gbcCheck.gridx = 0;
        gbcCheck.gridy = row++;
        gbcCheck.insets = new Insets(5, 0, 5, 0);
        showPasswordCheck.setBackground(Color.WHITE);
        showPasswordCheck.setFont(new Font(DEFAULT_FONT, Font.PLAIN, 12));
        showPasswordCheck.addActionListener(e -> {
            char echo = showPasswordCheck.isSelected() ? (char) 0 : '•';
            passwordField.setEchoChar(echo);
            confirmPasswordField.setEchoChar(echo);
        });
        formPanel.add(showPasswordCheck, gbcCheck);

        JButton registerBtn = getButton();

        GridBagConstraints gbcBtn = new GridBagConstraints();
        gbcBtn.gridwidth = 2;
        gbcBtn.gridx = 0;
        gbcBtn.gridy = row++;
        gbcBtn.insets = new Insets(5, 0, 10, 0);
        formPanel.add(registerBtn, gbcBtn);
        registerBtn.addActionListener(e -> handleRegistration());

        JButton backBtn = new JButton("← Volver al login");
        backBtn.setFont(new Font(DEFAULT_FONT, Font.PLAIN, 12));
        backBtn.setForeground(Color.BLUE);
        backBtn.setBorderPainted(false);
        backBtn.setContentAreaFilled(false);
        backBtn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        backBtn.addActionListener(e -> dispose());

        GridBagConstraints gbcBack = new GridBagConstraints();
        gbcBack.gridwidth = 2;
        gbcBack.gridx = 0;
        gbcBack.gridy = row;
        formPanel.add(backBtn, gbcBack);
    }

    private @NotNull JButton getButton() {
        JButton registerBtn = new JButton("Registrar") {
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(getBackground());
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 18, 18);
                super.paintComponent(g2);
                g2.dispose();
            }

            protected void paintBorder(Graphics g) {
            }
        };
        registerBtn.setFont(new Font(DEFAULT_FONT, Font.BOLD, 14));
        registerBtn.setBackground(new Color(33, 150, 243));
        registerBtn.setForeground(Color.WHITE);
        registerBtn.setFocusPainted(false);
        registerBtn.setContentAreaFilled(false);
        registerBtn.setOpaque(false);
        return registerBtn;
    }

    private void handleRegistration() {
        for (int i = 0; i < fields.length; i++) {
            if (i == 5) {
                continue;
            }
            if (fields[i].getText().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Por favor complete todos los campos.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
        }
        String cedula = fields[0].getText();
        String name = fields[1].getText();
        String email = fields[2].getText();
        String password = new String(passwordField.getPassword());
        String confirm = new String(confirmPasswordField.getPassword());

        if (!RegexValidator.isValidCedula(cedula)) {
            JOptionPane.showMessageDialog(this, "Cédula no válida", "Error", JOptionPane.ERROR_MESSAGE);
        } else if (!RegexValidator.isValidEmail(email)) {
            JOptionPane.showMessageDialog(this, "Email no válido", "Error", JOptionPane.ERROR_MESSAGE);
        } else if (!RegexValidator.isValidPassword(password)) {
            JOptionPane.showMessageDialog(this, "Contraseña inválida. Debe tener al menos 8 caracteres, 1 mayúscula y 1 símbolo.", "Error", JOptionPane.ERROR_MESSAGE);
        } else if (!password.equals(confirm)) {
            JOptionPane.showMessageDialog(this, "Las contraseñas no coinciden.", "Error", JOptionPane.ERROR_MESSAGE);
        } else if (!RegexValidator.isValidNamesOrSurnames(name)) {
            JOptionPane.showMessageDialog(this, "Nombre completo no válido", "Error", JOptionPane.ERROR_MESSAGE);
        } else {
            Patient patient;
            if (fields[5].getText().isEmpty()) {
                patient = new Patient(
                        cedula,
                        name,
                        email,
                        password,
                        "000000000",
                        fields[6].getText(),
                        fields[7].getText()
                );
            } else {
                patient = new Patient(
                        cedula,
                        name,
                        email,
                        password,
                        fields[5].getText(),
                        fields[6].getText(),
                        fields[7].getText()
                );
            }
            if (registrationController.register(patient)) {
                JOptionPane.showMessageDialog(this, "Registro exitoso. Ahora puede iniciar sesión.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Error al registrar el usuario. Intente nuevamente.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
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

        public Insets getBorderInsets(Component c) {
            return new Insets(radius + 2, radius + 2, radius + 2, radius + 2);
        }

        public Insets getBorderInsets(Component c, Insets insets) {
            insets.set(radius + 2, radius + 2, radius + 2, radius + 2);
            return insets;
        }
    }
}
