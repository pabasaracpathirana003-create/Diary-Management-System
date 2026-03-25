package view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import controller.UserController;

public class LoginForm extends JFrame {

    JTextField txtUsername;
    JPasswordField txtPassword;
    JButton btnLogin;

    public LoginForm() {
        setTitle("Personal Diary Management - Login");
        setSize(1000, 790);
        setLayout(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // --- Background Image ---
        ImageIcon bgIcon = new ImageIcon("Images/Login.png.jpeg"); // use your image
        Image bgImg = bgIcon.getImage().getScaledInstance(1000, 790, Image.SCALE_SMOOTH);
        bgIcon = new ImageIcon(bgImg);
        JLabel background = new JLabel(bgIcon);
        background.setBounds(0, 0, 1000, 790);
        setContentPane(background);
        background.setLayout(null);

        // 🔹 Transparent Panel
        JPanel panel = new JPanel();
        panel.setBounds(300, 180, 400, 350);
        panel.setBackground(new Color(0, 0, 0, 150)); // transparent black
        panel.setLayout(null);
        panel.setBorder(BorderFactory.createLineBorder(Color.WHITE, 2));
        background.add(panel);

        // Fonts
        Font lblFont = new Font("Arial", Font.BOLD, 22);
        Font txtFont = new Font("Arial", Font.PLAIN, 20);
        Font btnFont = new Font("Arial", Font.BOLD, 22);

        int xLabel = 30;
        int xField = 150;
        int y = 40;
        int yGap = 80;

        // Username
        JLabel lblUser = new JLabel("Username:");
        lblUser.setFont(lblFont);
        lblUser.setForeground(Color.WHITE);
        lblUser.setBounds(xLabel, y, 150, 40);
        panel.add(lblUser);

        txtUsername = new JTextField();
        txtUsername.setFont(txtFont);
        txtUsername.setBounds(xField, y, 200, 40);
        panel.add(txtUsername);

        // Password
        y += yGap;
        JLabel lblPass = new JLabel("Password:");
        lblPass.setFont(lblFont);
        lblPass.setForeground(Color.WHITE);
        lblPass.setBounds(xLabel, y, 150, 40);
        panel.add(lblPass);

        txtPassword = new JPasswordField();
        txtPassword.setFont(txtFont);
        txtPassword.setBounds(xField, y, 200, 40);
        panel.add(txtPassword);

        // Login Button
        y += yGap;
        btnLogin = new JButton("Login");
        btnLogin.setFont(btnFont);
        btnLogin.setBounds(xField, y, 150, 40);
        panel.add(btnLogin);

        btnLogin.addActionListener(e -> login());

        // Register link
        y += yGap;
        JLabel lblRegister = new JLabel("Don't have an account? Register");
        lblRegister.setFont(new Font("Arial", Font.PLAIN, 16));
        lblRegister.setForeground(Color.LIGHT_GRAY);
        lblRegister.setBounds(xField - 20, y, 300, 30);
        lblRegister.setCursor(new Cursor(Cursor.HAND_CURSOR));
        panel.add(lblRegister);

        lblRegister.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                new RegisterForm();
                dispose();
            }

            public void mouseEntered(MouseEvent e) {
                lblRegister.setText("<html><u>Don't have an account? Register</u></html>");
            }

            public void mouseExited(MouseEvent e) {
                lblRegister.setText("Don't have an account? Register");
            }
        });

        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void login() {
        String username = txtUsername.getText().trim();
        String password = String.valueOf(txtPassword.getPassword()).trim();

        UserController controller = new UserController();
        String message = controller.loginUser(username, password);

        if(message.equals("Login Successful!")) {
            new Dashboard(username); // open Dashboard directly
            this.dispose();          // close login form
        }

    }

    public static void main(String[] args){

        new LoginForm();
    }
}