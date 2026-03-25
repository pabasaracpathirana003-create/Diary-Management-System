package view;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;
import util.DBConnection;

public class ReportForm extends JFrame {

    JTable tableReport;
    DefaultTableModel model;
    String username;

    public ReportForm(String username) {
        this.username = username;

        setTitle("Diary Report");
        setSize(800, 600);
        setLayout(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        // --- Background Image ---
        ImageIcon bgIcon = new ImageIcon("Images/Login.png.jpeg");
        Image bgImg = bgIcon.getImage().getScaledInstance(800, 600, Image.SCALE_SMOOTH);
        bgIcon = new ImageIcon(bgImg);
        JLabel background = new JLabel(bgIcon);
        background.setBounds(0, 0, 800, 600);
        setContentPane(background);
        background.setLayout(null);

        // --- Transparent Panel ---
        JPanel panel = new JPanel();
        panel.setBounds(80, 60, 640, 450);
        panel.setBackground(new Color(0, 0, 0, 150)); // transparent black
        panel.setLayout(new BorderLayout());
        panel.setBorder(BorderFactory.createLineBorder(Color.WHITE, 2));
        background.add(panel);

        // --- Title Label ---
        JLabel lblTitle = new JLabel("Diary Report", JLabel.CENTER);
        lblTitle.setForeground(Color.WHITE);
        lblTitle.setFont(new Font("Arial", Font.BOLD, 22));
        panel.add(lblTitle, BorderLayout.NORTH);

        // --- Table ---
        model = new DefaultTableModel(new String[]{"Title", "Date", "Mood", "Category"}, 0);
        tableReport = new JTable(model);

       //table text size
        tableReport.setFont(new Font("Arial", Font.PLAIN, 18));
        tableReport.setRowHeight(30);

      //column headers bold
        tableReport.getTableHeader().setFont(new Font("Arial", Font.BOLD, 18));

        JScrollPane scroll = new JScrollPane(tableReport);
        panel.add(scroll, BorderLayout.CENTER);

        // --- Back Button ---
        JButton btnBack = new JButton("← Back");
        panel.add(btnBack, BorderLayout.SOUTH);

        btnBack.addActionListener(e -> {
            dispose();
            new Dashboard(username);
        });

        loadReport();

        setLocationRelativeTo(null);
        setVisible(true);
    }

    // --- Load data ---
    private void loadReport() {
        try (Connection con = DBConnection.getConnection()) {
            String sql = "SELECT e.title, e.entry_date, e.mood, c.category_name " +
                    "FROM diary_entries e JOIN categories c ON e.category_id=c.category_id " +
                    "WHERE e.user_id = (SELECT user_id FROM users WHERE username=?)";

            PreparedStatement pst = con.prepareStatement(sql);
            pst.setString(1, username);
            ResultSet rs = pst.executeQuery();

            model.setRowCount(0);

            while (rs.next()) {
                model.addRow(new Object[]{
                        rs.getString("title"),
                        rs.getString("entry_date"),
                        rs.getString("mood"),
                        rs.getString("category_name")
                });
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error loading report!");
            e.printStackTrace();
        }
    }
}