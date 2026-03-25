package view;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import util.DBConnection;

public class CategoryForm extends JFrame {

    JTable tableCategories;
    DefaultTableModel model;
    JTextField txtCategory;
    JButton btnAdd, btnEdit, btnDelete, btnBack;
    int selectedCategoryId = -1;

    public CategoryForm(String username) {
        setTitle("Manage Categories");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        // --- Background Image ---
        JLabel background = new JLabel(new ImageIcon("Images/Login.png.jpeg"));
        background.setLayout(null);
        setContentPane(background);

        // --- Transparent panel (smaller) ---
        JPanel panel = new JPanel();
        panel.setBounds(100, 50, 600, 450); // smaller than full frame
        panel.setBackground(new Color(0, 0, 0, 120)); // semi-transparent black
        panel.setLayout(null);
        panel.setBorder(BorderFactory.createLineBorder(Color.WHITE, 2));
        background.add(panel);

        // --- Category label & text field ---
        JLabel lblCategory = new JLabel("Category Name:");
        lblCategory.setForeground(Color.WHITE);
        lblCategory.setFont(new Font("Arial", Font.BOLD, 18));
        lblCategory.setBounds(20, 20, 150, 30);
        panel.add(lblCategory);

        txtCategory = new JTextField();
        txtCategory.setFont(new Font("Arial", Font.PLAIN, 16));
        txtCategory.setBounds(180, 20, 200, 30);
        panel.add(txtCategory);

        btnAdd = new JButton("Add");
        btnAdd.setFont(new Font("Arial", Font.BOLD, 16));
        btnAdd.setBounds(400, 20, 100, 30);
        panel.add(btnAdd);

        // --- Table ---
        model = new DefaultTableModel(new String[]{"ID", "Category Name"}, 0);
        tableCategories = new JTable(model);
        tableCategories.getTableHeader().setFont(new Font("Arial", Font.BOLD, 18));
        tableCategories.setFont(new Font("Arial", Font.PLAIN, 14));// bigger font
        tableCategories.setRowHeight(28);
        JScrollPane scroll = new JScrollPane(tableCategories);
        scroll.setBounds(20, 70, 550, 280);
        panel.add(scroll);

        // --- Buttons panel ---
        btnBack = new JButton("← Back");
        btnBack.setFont(new Font("Arial", Font.BOLD, 16));
        btnBack.setBounds(20, 370, 120, 35);
        panel.add(btnBack);

        btnDelete = new JButton("Delete");
        btnDelete.setFont(new Font("Arial", Font.BOLD, 16));
        btnDelete.setBounds(340, 370, 120, 35);
        panel.add(btnDelete);

        // --- Load categories ---
        loadCategories();

        // --- Table selection ---
        tableCategories.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                int row = tableCategories.getSelectedRow();
                selectedCategoryId = (int) model.getValueAt(row, 0);
                txtCategory.setText(model.getValueAt(row, 1).toString());
            }
        });

        // --- Button actions ---
        btnAdd.addActionListener(e -> addCategory());
        btnDelete.addActionListener(e -> deleteCategory());
        btnBack.addActionListener(e -> {
            dispose();
            new Dashboard(username);
        });

        setVisible(true);
    }

    private void loadCategories() {
        try {
            model.setRowCount(0);
            Connection con = DBConnection.getConnection();
            ResultSet rs = con.prepareStatement("SELECT * FROM categories").executeQuery();
            while (rs.next()) {
                model.addRow(new Object[]{rs.getInt("category_id"), rs.getString("category_name")});
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void addCategory() {
        String category = txtCategory.getText().trim();
        if(category.isEmpty()) return;
        try {
            Connection con = DBConnection.getConnection();
            PreparedStatement pst = con.prepareStatement("INSERT INTO categories(category_name) VALUES(?)");
            pst.setString(1, category);
            pst.executeUpdate();
            txtCategory.setText("");
            loadCategories();
        } catch(Exception e){ e.printStackTrace(); }
    }


    private void deleteCategory() {
        if(selectedCategoryId == -1) return;
        try {
            Connection con = DBConnection.getConnection();
            PreparedStatement pst = con.prepareStatement("DELETE FROM categories WHERE category_id=?");
            pst.setInt(1, selectedCategoryId);
            pst.executeUpdate();
            txtCategory.setText("");
            selectedCategoryId = -1;
            loadCategories();
        } catch(Exception e){ e.printStackTrace(); }
    }
}