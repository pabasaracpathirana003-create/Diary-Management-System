package controller;

import model.User;
import dao.UserDAO;

public class UserController {

    public String registerUser(String username, String password, String email) {

        if (username.isEmpty() || password.isEmpty() || email.isEmpty()) {
            return "Please fill all fields!";
        }

        try {
            User user = new User(username, password, email);
            UserDAO dao = new UserDAO();

            boolean success = dao.registerUser(user);

            if (success) {
                return "Registration Successful!";
            } else {
                return "Registration Failed!";
            }

        } catch (Exception e) {

            // MySQL duplicate error (username unique)
            if (e.getMessage() != null && e.getMessage().contains("Duplicate")) {
                return "Username already exists!";
            }

            e.printStackTrace();
            return "Error occurred!";
        }
    }

    public String loginUser(String username, String password) {

        if (username.isEmpty() || password.isEmpty()) {
            return "Please fill all fields!";
        }

        try {
            UserDAO dao = new UserDAO();
            boolean success = dao.loginUser(username, password);

            if (success) {
                return "Login Successful!";
            } else {
                return "Invalid username/password!";
            }

        } catch (Exception e) {
            e.printStackTrace();
            return "Error occurred!";
        }
    }
}