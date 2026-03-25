package view;

import javax.swing.*;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.sql.*;
import util.DBConnection;

public class AddEntryForm extends JFrame {

    JTextField txtTitle;
    JTextArea txtDescription;
    JComboBox<String> comboMood, comboCategory;
    JFormattedTextField txtDate;
    JButton btnSave;
    String username;

    public AddEntryForm(String username) {
        this.username = username;

        setTitle("Add Diary Entry");
        setSize(800, 600);
        setLayout(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        // Background Image
        ImageIcon bgIcon = new ImageIcon("Images/Login.png.jpeg");
        Image bgImg = bgIcon.getImage().getScaledInstance(800, 600, Image.SCALE_SMOOTH);
        bgIcon = new ImageIcon(bgImg);
        JLabel background = new JLabel(bgIcon);
        background.setBounds(0, 0, 800, 600);
        setContentPane(background);
        background.setLayout(null);

        // Transparent Panel
        JPanel panel = new JPanel();
        panel.setBounds(80, 60, 640, 460); // bigger panel
        panel.setBackground(new Color(0,0,0,150));
        panel.setLayout(null);
        panel.setBorder(BorderFactory.createLineBorder(Color.WHITE, 2));
        background.add(panel);

        Font lblFont = new Font("Arial", Font.BOLD, 18);
        Font txtFont = new Font("Arial", Font.PLAIN, 16);

        int xLabel = 60;
        int xField = 200;
        int y = 30;
        int gap = 55;

        // Title
        JLabel lblTitle = new JLabel("Title:");
        lblTitle.setForeground(Color.WHITE);
        lblTitle.setFont(lblFont);
        lblTitle.setBounds(xLabel, y, 120, 30);
        panel.add(lblTitle);

        txtTitle = new JTextField();
        txtTitle.setFont(txtFont);
        txtTitle.setBounds(xField, y, 300, 30);
        panel.add(txtTitle);

        // Description (BIG)
        y += gap;

        JLabel lblDesc = new JLabel("Description:");
        lblDesc.setForeground(Color.WHITE);
        lblDesc.setFont(lblFont);
        lblDesc.setBounds(xLabel, y, 120, 30);
        panel.add(lblDesc);

        txtDescription = new JTextArea();
        txtDescription.setLineWrap(true);
        txtDescription.setWrapStyleWord(true);
        txtDescription.setFont(txtFont);

        JScrollPane scrollDesc = new JScrollPane(txtDescription);
        scrollDesc.setBounds(xField, y, 350, 140); // bigger box
        panel.add(scrollDesc);

        // Date
        y += 160;

        JLabel lblDate = new JLabel("Date:");
        lblDate.setForeground(Color.WHITE);
        lblDate.setFont(lblFont);
        lblDate.setBounds(xLabel, y, 120, 30);
        panel.add(lblDate);

        txtDate = new JFormattedTextField(new SimpleDateFormat("yyyy-MM-dd"));
        txtDate.setFont(txtFont);
        txtDate.setBounds(xField, y, 200, 30);
        panel.add(txtDate);

        // Mood
        y += gap;

        JLabel lblMood = new JLabel("Mood:");
        lblMood.setForeground(Color.WHITE);
        lblMood.setFont(lblFont);
        lblMood.setBounds(xLabel, y, 120, 30);
        panel.add(lblMood);

        String[] moods = {"Happy", "Sad", "Excited", "Angry", "Neutral"};
        comboMood = new JComboBox<>(moods);
        comboMood.setBounds(xField, y, 200, 30);
        panel.add(comboMood);

        // Category
        y += gap;

        JLabel lblCategory = new JLabel("Category:");
        lblCategory.setForeground(Color.WHITE);
        lblCategory.setFont(lblFont);
        lblCategory.setBounds(xLabel, y, 120, 30);
        panel.add(lblCategory);

        comboCategory = new JComboBox<>();
        comboCategory.setBounds(xField, y, 200, 30);
        panel.add(comboCategory);

        loadCategories();

        // Save Button
        y += 40;

        btnSave = new JButton("Save Entry");
        btnSave.setBounds(450, y, 150, 40);
        panel.add(btnSave);

        btnSave.addActionListener(e -> saveEntry());

        // --- Back Button ---
        JButton btnBack = new JButton("← Back"); // you can also use an icon here
        btnBack.setBounds(30, 400, 80, 35);  // top-left corner
        panel.add(btnBack);

        btnBack.addActionListener(e -> {
            this.dispose();              // close AddEntryForm
            new Dashboard(username);     // open Dashboard again
        });

        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void loadCategories() {
        try {
            Connection con = DBConnection.getConnection();
            String sql = "SELECT category_name FROM categories";
            PreparedStatement pst = con.prepareStatement(sql);
            ResultSet rs = pst.executeQuery();
            comboCategory.removeAllItems();
            while (rs.next()) {
                comboCategory.addItem(rs.getString("category_name"));
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error loading categories! Make sure DB exists.");
            e.printStackTrace();
        }
    }

    private void saveEntry() {
        String title = txtTitle.getText().trim();
        String desc = txtDescription.getText().trim();
        String date = txtDate.getText().trim();
        String mood = (String) comboMood.getSelectedItem();
        String category = (String) comboCategory.getSelectedItem();

        if(title.isEmpty() || desc.isEmpty() || date.isEmpty() || category==null) {
            JOptionPane.showMessageDialog(this, "Please fill all fields!");
            return;
        }

        try {
            Connection con = DBConnection.getConnection();

            // Get user_id
            PreparedStatement pstUser = con.prepareStatement("SELECT user_id FROM users WHERE username=?");
            pstUser.setString(1, username);
            ResultSet rsUser = pstUser.executeQuery();
            int userId = 0;
            if(rsUser.next()) userId = rsUser.getInt("user_id");

            // Get category_id
            PreparedStatement pstCat = con.prepareStatement("SELECT category_id FROM categories WHERE category_name=?");
            pstCat.setString(1, category);
            ResultSet rsCat = pstCat.executeQuery();
            int catId = 0;
            if(rsCat.next()) catId = rsCat.getInt("category_id");

            String sql = "INSERT INTO diary_entries(title, description, entry_date, mood, user_id, category_id) VALUES(?,?,?,?,?,?)";
            PreparedStatement pst = con.prepareStatement(sql);
            pst.setString(1, title);
            pst.setString(2, desc);
            pst.setString(3, date);
            pst.setString(4, mood);
            pst.setInt(5, userId);
            pst.setInt(6, catId);

            int row = pst.executeUpdate();
            if(row > 0){
                JOptionPane.showMessageDialog(this, "Diary entry saved successfully!");
                this.dispose();
                new Dashboard(username);
            } else {
                JOptionPane.showMessageDialog(this, "Failed to save entry!");
            }

        } catch(Exception e){
            JOptionPane.showMessageDialog(this, "Error saving entry! Check DB connection.");
            e.printStackTrace();
        }
    }
}