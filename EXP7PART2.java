import java.sql.*;
import java.util.Scanner;

public class ProductCRUD {
    static final String URL = "jdbc:mysql://localhost:3306/your_database";
    static final String USER = "root";
    static final String PASSWORD = "password";

    public static void main(String[] args) {
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             Scanner scanner = new Scanner(System.in)) {

            while (true) {
                System.out.println("\nMenu:");
                System.out.println("1. Add Product");
                System.out.println("2. View Products");
                System.out.println("3. Update Product");
                System.out.println("4. Delete Product");
                System.out.println("5. Exit");
                System.out.print("Choose an option: ");
                int choice = scanner.nextInt();

                switch (choice) {
                    case 1 -> addProduct(conn, scanner);
                    case 2 -> viewProducts(conn);
                    case 3 -> updateProduct(conn, scanner);
                    case 4 -> deleteProduct(conn, scanner);
                    case 5 -> System.exit(0);
                    default -> System.out.println("Invalid choice. Try again.");
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    static void addProduct(Connection conn, Scanner scanner) throws SQLException {
        System.out.print("Enter Product Name: ");
        String name = scanner.next();
        System.out.print("Enter Price: ");
        double price = scanner.nextDouble();
        System.out.print("Enter Quantity: ");
        int qty = scanner.nextInt();

        String sql = "INSERT INTO Product (ProductName, Price, Quantity) VALUES (?, ?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, name);
            stmt.setDouble(2, price);
            stmt.setInt(3, qty);
            stmt.executeUpdate();
            System.out.println("Product added successfully!");
        }
    }

    static void viewProducts(Connection conn) throws SQLException {
        String sql = "SELECT * FROM Product";
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            System.out.println("ProductID\tProductName\tPrice\tQuantity");
            while (rs.next()) {
                System.out.println(rs.getInt("ProductID") + "\t" + rs.getString("ProductName") + "\t" +
                        rs.getDouble("Price") + "\t" + rs.getInt("Quantity"));
            }
        }
    }

    static void updateProduct(Connection conn, Scanner scanner) throws SQLException {
        System.out.print("Enter Product ID to update: ");
        int id = scanner.nextInt();
        System.out.print("Enter new Price: ");
        double price = scanner.nextDouble();

        String sql = "UPDATE Product SET Price = ? WHERE ProductID = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setDouble(1, price);
            stmt.setInt(2, id);
            int rows = stmt.executeUpdate();
            System.out.println(rows > 0 ? "Product updated!" : "Product not found.");
        }
    }

    static void deleteProduct(Connection conn, Scanner scanner) throws SQLException {
        System.out.print("Enter Product ID to delete: ");
        int id = scanner.nextInt();

        String sql = "DELETE FROM Product WHERE ProductID = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            int rows = stmt.executeUpdate();
            System.out.println(rows > 0 ? "Product deleted!" : "Product not found.");
        }
    }
}
