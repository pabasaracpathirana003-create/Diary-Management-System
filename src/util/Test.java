package util;

public class Test {
    public static void main(String args[]) {
        if (DBConnection.getConnection() != null) {
            System.out.println("Connected!");
        } else {
            System.out.println("Failed!");
        }
    }
}
