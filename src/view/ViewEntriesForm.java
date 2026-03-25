package view;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.util.ArrayList;
import util.DBConnection;

public class ViewEntriesForm extends JFrame {

    JTable tableEntries;
    DefaultTableModel model;
    JTextField txtSearch;
    JButton btnSearch, btnEdit, btnDelete;
    String username;
    int selectedEntryId = -1;

    // Temporary in-memory entries (unsaved)
    ArrayList<Object[]> tempEntries = new ArrayList<>();
    int tempIdCounter = -1; // negative IDs for temp entries

    public ViewEntriesForm(String username) {
        this.username = username;

        setTitle("View Diary Entries");
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
        panel.setBounds(50, 50, 700, 500);
        panel.setBackground(new Color(0, 0, 0, 100)); // semi-transparent
        panel.setLayout(null);
        panel.setBorder(BorderFactory.createLineBorder(Color.WHITE, 2));
        background.add(panel);

        // --- Search components ---
        txtSearch = new JTextField();
        txtSearch.setBounds(30, 20, 200, 25);
        panel.add(txtSearch);

        btnSearch = new JButton("Search");
        btnSearch.setBounds(250, 20, 100, 25);
        panel.add(btnSearch);

        // --- Table setup ---
        model = new DefaultTableModel(new String[]{"ID","Title","Date","Mood","Category"},0);
        tableEntries = new JTable(model);

        tableEntries.getTableHeader().setFont(new Font("Arial", Font.BOLD, 18));
        tableEntries.setFont(new Font("Arial", Font.PLAIN, 16));
        tableEntries.setRowHeight(28);
        tableEntries.setFillsViewportHeight(true);
        tableEntries.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);

        JScrollPane scroll = new JScrollPane(tableEntries);
        scroll.setBounds(30, 60, 640, 350);
        scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        panel.add(scroll);

        // --- Buttons ---
        btnEdit = new JButton("Edit");
        btnEdit.setBounds(150, 430, 100, 30);
        panel.add(btnEdit);

        btnDelete = new JButton("Delete");
        btnDelete.setBounds(350, 430, 100, 30);
        panel.add(btnDelete);

        JButton btnGoBack = new JButton("Go Back");
        btnGoBack.setBounds(550, 430, 100, 30);
        panel.add(btnGoBack);

        loadEntries();

        // --- Table click ---
        tableEntries.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                try {
                    int row = tableEntries.getSelectedRow();
                    selectedEntryId = (int) model.getValueAt(row, 0);
                } catch(Exception ex){
                    JOptionPane.showMessageDialog(null, "Select a valid entry!");
                }
            }
        });

        // --- Button actions ---
        btnSearch.addActionListener(e -> searchEntries(txtSearch.getText()));

        btnEdit.addActionListener(e -> {
            try {
                if(selectedEntryId != -1) {
                    if(selectedEntryId < 0) { // temporary entry
                        JOptionPane.showMessageDialog(null, "Editing unsaved entry not implemented yet.");
                    } else {
                        new EditEntryForm(selectedEntryId);
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "Select an entry to edit!");
                }
            } catch(Exception ex){
                JOptionPane.showMessageDialog(null, "Cannot open edit form!");
            }
        });

        btnDelete.addActionListener(e -> {
            if(selectedEntryId != -1) {
                if(selectedEntryId < 0) { // temporary entry
                    tempEntries.removeIf(row -> (int)row[0] == selectedEntryId);
                    loadEntries();
                } else {
                    deleteEntry(selectedEntryId);
                }
            } else {
                JOptionPane.showMessageDialog(null, "Select an entry to delete!");
            }
        });

        btnGoBack.addActionListener(e -> {
            new Dashboard(username);
            dispose();
        });

        setLocationRelativeTo(null);
        setVisible(true);
    }

    // --- Add temporary unsaved entry ---
    public void addTemporaryEntry(String title, String date, String mood, String category) {
        Object[] row = new Object[]{tempIdCounter--, title, date, mood, category};
        tempEntries.add(row);
        model.addRow(row);
    }

    // --- Database methods ---
    private void loadEntries() {
        model.setRowCount(0); // clear table

        ArrayList<Object[]> allEntries = new ArrayList<>();

        // 1. Load DB entries
        try {
            Connection con = DBConnection.getConnection();
            int userId = getUserId(con);
            if(userId != 0) {
                PreparedStatement pst = con.prepareStatement(
                        "SELECT e.entry_id, e.title, e.entry_date, e.mood, c.category_name " +
                                "FROM diary_entries e JOIN categories c ON e.category_id=c.category_id " +
                                "WHERE e.user_id=?"
                );
                pst.setInt(1, userId);
                ResultSet rs = pst.executeQuery();
                while(rs.next()) {
                    allEntries.add(new Object[]{
                            rs.getInt("entry_id"),
                            rs.getString("title"),
                            rs.getString("entry_date"),
                            rs.getString("mood"),
                            rs.getString("category_name")
                    });
                }
            }
        } catch(Exception e){
            JOptionPane.showMessageDialog(null, "Error loading entries!");
            e.printStackTrace();
        }

        // 2. Add temporary unsaved entries
        allEntries.addAll(tempEntries);

        // 3. Sort by ID ascending
        allEntries.sort((a, b) -> Integer.compare((int)a[0], (int)b[0]));

        // 4. Add to table
        for(Object[] row : allEntries) {
            model.addRow(row);
        }
    }
    private void searchEntries(String keyword) {
        model.setRowCount(0);
        try {
            Connection con = DBConnection.getConnection();
            int userId = getUserId(con);
            if(userId != 0) {
                PreparedStatement pst = con.prepareStatement(
                        "SELECT e.entry_id, e.title, e.entry_date, e.mood, c.category_name " +
                                "FROM diary_entries e JOIN categories c ON e.category_id=c.category_id " +
                                "WHERE e.user_id=? AND (e.title LIKE ? OR e.entry_date LIKE ?)"
                );
                pst.setInt(1, userId);
                pst.setString(2, "%" + keyword + "%");
                pst.setString(3, "%" + keyword + "%");
                ResultSet rs = pst.executeQuery();
                while(rs.next()) {
                    model.addRow(new Object[]{
                            rs.getInt("entry_id"),
                            rs.getString("title"),
                            rs.getString("entry_date"),
                            rs.getString("mood"),
                            rs.getString("category_name")
                    });
                }
            }
        } catch(Exception e){
            JOptionPane.showMessageDialog(null, "Error searching entries!");
            e.printStackTrace();
        }

        // Also include temporary entries in search
        for(Object[] row : tempEntries) {
            String title = (String) row[1];
            String date = (String) row[2];
            if(title.contains(keyword) || date.contains(keyword)) {
                model.addRow(row);
            }
        }
    }

    private void deleteEntry(int entryId) {
        try {
            int confirm = JOptionPane.showConfirmDialog(this, "Delete this entry?", "Delete", JOptionPane.YES_NO_OPTION);
            if(confirm == JOptionPane.YES_OPTION) {
                Connection con = DBConnection.getConnection();
                PreparedStatement pst = con.prepareStatement("DELETE FROM diary_entries WHERE entry_id=?");
                pst.setInt(1, entryId);
                pst.executeUpdate();
                JOptionPane.showMessageDialog(this, "Entry deleted!");
                loadEntries();
            }
        } catch(Exception e){
            JOptionPane.showMessageDialog(null, "Error deleting entry!");
            e.printStackTrace();
        }
    }

    private int getUserId(Connection con) {
        try {
            PreparedStatement pst = con.prepareStatement("SELECT user_id FROM users WHERE username=?");
            pst.setString(1, username);
            ResultSet rs = pst.executeQuery();
            if(rs.next()) return rs.getInt("user_id");
        } catch(Exception e){
            JOptionPane.showMessageDialog(null, "Error getting user!");
            e.printStackTrace();
        }
        return 0;
    }
}