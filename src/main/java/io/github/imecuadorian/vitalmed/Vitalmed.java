package io.github.imecuadorian.vitalmed;

import io.github.imecuadorian.vitalmed.controller.*;
import io.github.imecuadorian.vitalmed.factory.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class Vitalmed {

    private JFrame frame;
    private JTextField txtEmail;
    private JPasswordField txtPassword;

    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            try {
                Vitalmed window = new Vitalmed();
                window.frame.setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    public Vitalmed() {
        initialize();
    }

    private void initialize() {
        LoginController loginController = new LoginController(
                ServiceFactory.getAdminService(),
                ServiceFactory.getDoctorAuth(),
                ServiceFactory.getPatientAuth()
        );

        frame = new JFrame();
        frame.setTitle("Clínica Vitalmed - Login");
        frame.setBounds(100, 100, 400, 300);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().setLayout(null);

        JLabel lblEmail = new JLabel("Email:");
        lblEmail.setBounds(50, 50, 100, 20);
        frame.getContentPane().add(lblEmail);

        txtEmail = new JTextField();
        txtEmail.setBounds(130, 50, 200, 25);
        frame.getContentPane().add(txtEmail);

        JLabel lblPassword = new JLabel("Contraseña:");
        lblPassword.setBounds(50, 90, 100, 20);
        frame.getContentPane().add(lblPassword);

        txtPassword = new JPasswordField();
        txtPassword.setBounds(130, 90, 200, 25);
        frame.getContentPane().add(txtPassword);

        JButton btnLogin = new JButton("Iniciar Sesión");
        btnLogin.setBounds(130, 140, 140, 30);
        frame.getContentPane().add(btnLogin);

        JLabel lblRegister = new JLabel("<HTML><U>¿No tienes cuenta? Regístrate</U></HTML>");
        lblRegister.setForeground(Color.BLUE.darker());
        lblRegister.setBounds(110, 190, 200, 20);
        lblRegister.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        frame.getContentPane().add(lblRegister);

        btnLogin.addActionListener(e -> {
            String email = txtEmail.getText();
            String password = new String(txtPassword.getPassword());
            loginController.login(email, password, frame);
        });

        lblRegister.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                frame.dispose();

            }
        });
    }
}
