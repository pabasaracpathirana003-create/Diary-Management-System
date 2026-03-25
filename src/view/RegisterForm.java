package view;

import javax.swing.*;
import java.awt.*;
import controller.UserController;

public class RegisterForm extends JFrame {

    private JLabel lblUsername, lblPassword, lblEmail;
    private JTextField txtUsername, txtEmail;
    private JPasswordField txtPassword;
    private JButton btnRegister;

    public RegisterForm() {
        setTitle("Register Form");
        setSize(1000, 790);
        setLayout(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // --- Background Image ---
        ImageIcon bgIcon = new ImageIcon("Images/Login.png.jpeg");
        Image bgImg = bgIcon.getImage().getScaledInstance(1000, 790, Image.SCALE_SMOOTH);
        bgIcon = new ImageIcon(bgImg);
        JLabel background = new JLabel(bgIcon);
        background.setBounds(0, 0, 1000, 790);
        setContentPane(background);
        background.setLayout(null);

        //  Transparent Panel
        JPanel panel = new JPanel();
        panel.setBounds(250, 150, 500, 450);
        panel.setBackground(new Color(0, 0, 0, 150)); // transparent black
        panel.setLayout(null);
        panel.setBorder(BorderFactory.createLineBorder(Color.WHITE, 2));
        background.add(panel);

        // Fonts
        Font lblFont = new Font("Arial", Font.BOLD, 22);
        Font txtFont = new Font("Arial", Font.PLAIN, 20);
        Font btnFont = new Font("Arial", Font.BOLD, 22);

        int xLabel = 30;
        int xField = 180;
        int y = 40;
        int yGap = 70;

        // Username
        lblUsername = new JLabel("Username:");
        lblUsername.setFont(lblFont);
        lblUsername.setForeground(Color.WHITE);
        lblUsername.setBounds(xLabel, y, 150, 40);
        panel.add(lblUsername);

        txtUsername = new JTextField();
        txtUsername.setFont(txtFont);
        txtUsername.setBounds(xField, y, 250, 40);
        panel.add(txtUsername);

        // Password
        y += yGap;
        lblPassword = new JLabel("Password:");
        lblPassword.setFont(lblFont);
        lblPassword.setForeground(Color.WHITE);
        lblPassword.setBounds(xLabel, y, 150, 40);
        panel.add(lblPassword);

        txtPassword = new JPasswordField();
        txtPassword.setFont(txtFont);
        txtPassword.setBounds(xField, y, 250, 40);
        panel.add(txtPassword);

        // Email
        y += yGap;
        lblEmail = new JLabel("Email:");
        lblEmail.setFont(lblFont);
        lblEmail.setForeground(Color.WHITE);
        lblEmail.setBounds(xLabel, y, 150, 40);
        panel.add(lblEmail);

        txtEmail = new JTextField();
        txtEmail.setFont(txtFont);
        txtEmail.setBounds(xField, y, 250, 40);
        panel.add(txtEmail);

        // Register Button
        y += yGap;
        btnRegister = new JButton("Register");
        btnRegister.setFont(btnFont);
        btnRegister.setBounds(xField, y, 180, 40);
        panel.add(btnRegister);

        btnRegister.addActionListener(e -> registerUser());

        // Login link
        y += yGap;
        JLabel lblLogin = new JLabel("Already registered? Login here");
        lblLogin.setFont(new Font("Arial", Font.PLAIN, 16));
        lblLogin.setForeground(Color.GRAY);
        lblLogin.setBounds(xField, y, 300, 30);
        lblLogin.setCursor(new Cursor(Cursor.HAND_CURSOR));
        panel.add(lblLogin);

        lblLogin.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                new LoginForm();
                dispose();
            }

            public void mouseEntered(java.awt.event.MouseEvent evt) {
                lblLogin.setText("<html><u>Already registered? Login here</u></html>");
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                lblLogin.setText("Already registered? Login here");
            }
        });

        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void registerUser() {

        String username = txtUsername.getText().trim();
        String password = new String(txtPassword.getPassword()).trim();
        String email = txtEmail.getText().trim();

        UserController controller = new UserController();
        String message = controller.registerUser(username, password, email);

        JOptionPane.showMessageDialog(this, message);

        if (message.equals("Registration Successful!")) {
            this.dispose();
            new LoginForm();
        }
    }

    public static void main(String[] args)
    {

        SwingUtilities.invokeLater(RegisterForm::new);
    }
}