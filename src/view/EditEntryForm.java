package view;

import javax.swing.*;
import java.awt.*;
import java.sql.*;
import java.text.SimpleDateFormat;
import util.DBConnection;

public class EditEntryForm extends JFrame {

    JTextField txtTitle;
    JTextArea txtDescription;
    JComboBox<String> comboMood, comboCategory;
    JFormattedTextField txtDate;
    JButton btnUpdate;

    int entryId;

    public EditEntryForm(int entryId) {
        this.entryId = entryId;

        // --- Basic JFrame setup ---
        setTitle("Edit Diary Entry");
        setSize(800, 600);
        setLayout(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        // --- Background image ---
        ImageIcon bgIcon = new ImageIcon("Images/Login.png.jpeg"); // change path
        Image bgImg = bgIcon.getImage().getScaledInstance(800, 600, Image.SCALE_SMOOTH);
        bgIcon = new ImageIcon(bgImg);
        JLabel background = new JLabel(bgIcon);
        background.setBounds(0, 0, 800, 600);
        setContentPane(background);
        background.setLayout(null);

        // --- Transparent panel for form ---
        JPanel panel = new JPanel();
        panel.setBounds(80, 60, 600, 400); // panel size and position
        panel.setBackground(new Color(0, 0, 0, 100)); // semi-transparent
        panel.setLayout(null);
        panel.setBorder(BorderFactory.createLineBorder(Color.WHITE, 2));
        background.add(panel);

// Padding variables for easier alignment
        int labelX = 40;          // X position for all labels
        int fieldX = 150;         // X position for all input fields
        int startY = 30;          // starting Y position
        int gapY = 50;            // vertical gap between rows

// --- Title ---
        JLabel lblTitle = new JLabel("Title:");
        lblTitle.setForeground(Color.WHITE);
        lblTitle.setBounds(labelX, startY, 100, 25);
        panel.add(lblTitle);

        txtTitle = new JTextField();
        txtTitle.setBounds(fieldX, startY, 300, 25);
        panel.add(txtTitle);

// --- Date ---
        JLabel lblDate = new JLabel("Date:");
        lblDate.setForeground(Color.WHITE);
        lblDate.setBounds(labelX, startY + gapY, 100, 25);
        panel.add(lblDate);

        txtDate = new JFormattedTextField(new SimpleDateFormat("yyyy-MM-dd"));
        txtDate.setBounds(fieldX, startY + gapY, 300, 25);
        panel.add(txtDate);

// --- Mood ---
        JLabel lblMood = new JLabel("Mood:");
        lblMood.setForeground(Color.WHITE);
        lblMood.setBounds(labelX, startY + 2 * gapY, 100, 25);
        panel.add(lblMood);

        String[] moods = {"Happy", "Sad", "Excited", "Angry", "Neutral"};
        comboMood = new JComboBox<>(moods);
        comboMood.setBounds(fieldX, startY + 2 * gapY, 300, 25);
        panel.add(comboMood);

// --- Category ---
        JLabel lblCategory = new JLabel("Category:");
        lblCategory.setForeground(Color.WHITE);
        lblCategory.setBounds(labelX, startY + 3 * gapY, 100, 25);
        panel.add(lblCategory);

        comboCategory = new JComboBox<>();
        comboCategory.setBounds(fieldX, startY + 3 * gapY, 300, 25);
        panel.add(comboCategory);

        int posY = 210; // your own vertical position tracker, safe name

// Add description
        txtDescription = new JTextArea();
        txtDescription.setLineWrap(true);
        txtDescription.setWrapStyleWord(true);

        JScrollPane scrollDesc = new JScrollPane(txtDescription,
                JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollDesc.setBounds(150, posY, 300, 120);
        panel.add(scrollDesc);

// Move posY down for next component
        posY += 130;

// Add button
        btnUpdate = new JButton("Update Entry");
        btnUpdate.setBounds(150, posY, 150, 40);
        panel.add(btnUpdate);
        // --- Load categories from DB ---
        loadCategories();

        // --- Load entry details ---
        loadEntryDetails();

        // --- Button action ---
        btnUpdate.addActionListener(e -> {
            try {
                updateEntry();
            } catch(Exception ex) {
                JOptionPane.showMessageDialog(this, "Error updating entry: " + ex.getMessage());
            }
        });

        setLocationRelativeTo(null);
        setVisible(true);
    }

    // --- Load categories ---
    private void loadCategories() {
        try (Connection con = DBConnection.getConnection();
             PreparedStatement pst = con.prepareStatement("SELECT category_name FROM categories");
             ResultSet rs = pst.executeQuery()) {

            comboCategory.removeAllItems();
            while(rs.next()){
                comboCategory.addItem(rs.getString("category_name"));
            }

        } catch(Exception e) {
            JOptionPane.showMessageDialog(this, "Error loading categories");
        }
    }

    // --- Load entry data ---
    private void loadEntryDetails() {
        try (Connection con = DBConnection.getConnection();
             PreparedStatement pst = con.prepareStatement(
                     "SELECT e.title, e.description, e.entry_date, e.mood, c.category_name " +
                             "FROM diary_entries e JOIN categories c ON e.category_id=c.category_id " +
                             "WHERE e.entry_id=?")) {

            pst.setInt(1, entryId);
            try (ResultSet rs = pst.executeQuery()) {
                if(rs.next()){
                    txtTitle.setText(rs.getString("title"));
                    txtDescription.setText(rs.getString("description"));
                    txtDate.setText(rs.getString("entry_date"));
                    comboMood.setSelectedItem(rs.getString("mood"));
                    comboCategory.setSelectedItem(rs.getString("category_name"));
                } else {
                    JOptionPane.showMessageDialog(this, "Entry not found!");
                    dispose();
                }
            }
        } catch(Exception e){
            JOptionPane.showMessageDialog(this, "Error loading entry details");
        }
    }

    // --- Update entry in DB ---
    private void updateEntry() {
        String title = txtTitle.getText().trim();
        String desc = txtDescription.getText().trim();
        String date = txtDate.getText().trim();
        String mood = (String) comboMood.getSelectedItem();
        String category = (String) comboCategory.getSelectedItem();

        if(title.isEmpty() || desc.isEmpty() || date.isEmpty() || category == null){
            JOptionPane.showMessageDialog(this, "Fill all fields!");
            return;
        }

        try (Connection con = DBConnection.getConnection()) {

            // Get category ID
            int catId = 0;
            try (PreparedStatement pstCat = con.prepareStatement("SELECT category_id FROM categories WHERE category_name=?")) {
                pstCat.setString(1, category);
                try (ResultSet rs = pstCat.executeQuery()) {
                    if(rs.next()) catId = rs.getInt("category_id");
                }
            }

            // Update entry
            try (PreparedStatement pst = con.prepareStatement(
                    "UPDATE diary_entries SET title=?, description=?, entry_date=?, mood=?, category_id=? WHERE entry_id=?")) {

                pst.setString(1, title);
                pst.setString(2, desc);
                pst.setString(3, date);
                pst.setString(4, mood);
                pst.setInt(5, catId);
                pst.setInt(6, entryId);

                int rows = pst.executeUpdate();
                if(rows > 0){
                    JOptionPane.showMessageDialog(this, "Entry updated!");
                    dispose();
                } else {
                    JOptionPane.showMessageDialog(this, "Update failed!");
                }
            }

        } catch(Exception e){
            JOptionPane.showMessageDialog(this, "Error updating entry");
        }
    }
}