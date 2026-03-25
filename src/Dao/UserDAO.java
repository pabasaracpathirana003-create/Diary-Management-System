package dao;

import model.User;
import java.sql.*;

public class UserDAO {

    public boolean registerUser(User user) throws SQLException {

        Connection con = util.DBConnection.getConnection();

        PreparedStatement pst = con.prepareStatement(
                "INSERT INTO users(username, password, email) VALUES(?, ?, ?)"
        );

        pst.setString(1, user.getUsername());
        pst.setString(2, user.getPassword());
        pst.setString(3, user.getEmail());

        int row = pst.executeUpdate();

        return row > 0;
    }

    public boolean loginUser(String username, String password) throws SQLException {

        Connection con = util.DBConnection.getConnection();

        String sql = "SELECT * FROM users WHERE username=? AND password=?";
        PreparedStatement pst = con.prepareStatement(sql);

        pst.setString(1, username);
        pst.setString(2, password);

        ResultSet rs = pst.executeQuery();

        return rs.next(); // true if user exists
    }
}