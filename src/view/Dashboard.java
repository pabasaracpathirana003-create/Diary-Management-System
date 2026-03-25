package view;

import javax.swing.*;
import java.awt.*;
import java.io.FileWriter;
import java.sql.*;
import util.DBConnection;

public class Dashboard extends JFrame {

    JButton btnAddEntry, btnViewEntries, btnManageCategories, btnGenerateReport;
    JLabel lblWelcome;
    String username;

    public Dashboard(String username) {
        this.username = username;

        setTitle("Personal Diary Management - Dashboard");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(null);

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
        panel.setBounds(100, 80, 600, 400);
        panel.setBackground(new Color(0,0,0,150)); // semi-transparent
        panel.setLayout(null);
        panel.setBorder(BorderFactory.createLineBorder(Color.WHITE, 2));
        background.add(panel);

        Font lblFont = new Font("Arial", Font.BOLD, 24);
        Font btnFont = new Font("Arial", Font.BOLD, 20);

        // --- Welcome Label ---
        lblWelcome = new JLabel("Welcome, " + username + "!");
        lblWelcome.setFont(lblFont);
        lblWelcome.setForeground(Color.WHITE);
        lblWelcome.setBounds(50, 20, 500, 40);
        panel.add(lblWelcome);

        // --- Buttons ---
        btnAddEntry = new JButton("Add Diary Entry");
        btnAddEntry.setFont(btnFont);
        btnAddEntry.setBounds(50, 90, 220, 50);
        panel.add(btnAddEntry);

        btnViewEntries = new JButton("View Entries");
        btnViewEntries.setFont(btnFont);
        btnViewEntries.setBounds(320, 90, 220, 50);
        panel.add(btnViewEntries);

        btnManageCategories = new JButton("Manage Categories");
        btnManageCategories.setFont(btnFont);
        btnManageCategories.setBounds(50, 170, 220, 50);
        panel.add(btnManageCategories);

        btnGenerateReport = new JButton("Generate Report");
        btnGenerateReport.setFont(btnFont);
        btnGenerateReport.setBounds(320, 170, 220, 50);
        panel.add(btnGenerateReport);

        // --- Button Actions ---
        btnAddEntry.addActionListener(e -> {
            new AddEntryForm(username);
            this.dispose();
        });

        btnViewEntries.addActionListener(e -> {
            new ViewEntriesForm(username);
            this.dispose();
        });

        btnManageCategories.addActionListener(e -> {
            new CategoryForm(username);
            this.dispose();
        });

        btnGenerateReport.addActionListener(e -> {
            new ReportForm(username);
            this.dispose();
        });

        setLocationRelativeTo(null);
        setVisible(true);
    }




    public static void main(String[] args) {
        new Dashboard("User"); // for testing
    }
}