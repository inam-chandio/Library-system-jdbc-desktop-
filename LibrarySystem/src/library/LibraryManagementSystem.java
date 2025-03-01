
// 5. Main Application Class
package library;

import library.dao.*;
import library.model.*;
import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;

public class LibraryManagementSystem {
    private static final LibraryItemDAO itemDAO = new LibraryItemDAO();
    private static final UserDAO userDAO = new UserDAO();
    private static final Scanner scanner = new Scanner(System.in);
    private static User currentUser;

    public static void main(String[] args) {
        try {
            showLoginMenu();
            while (true) {
                showMainMenu();
                int choice = scanner.nextInt();
                scanner.nextLine(); // consume newline

                switch (choice) {
                    case 1 -> addItem();
                    case 2 -> viewAllItems();
                    case 3 -> borrowItem();
                    case 4 -> returnItem();
                    case 5 -> System.exit(0);
                    default -> System.out.println("Invalid choice!");
                }
            }
        } catch (SQLException e) {
            System.out.println("Database error: " + e.getMessage());
        }
    }

    private static void showLoginMenu() throws SQLException {
        System.out.print("Enter user ID: ");
        String userId = scanner.nextLine();

        currentUser = userDAO.findById(userId);
        if (currentUser == null) {
            System.out.print("New user. Enter name: ");
            String name = scanner.nextLine();
            System.out.print("Enter email: ");
            String email = scanner.nextLine();

            currentUser = new User(userId, name, email);
            userDAO.addUser(currentUser);
        }
        System.out.println("Welcome, " + currentUser.getName() + "!");
    }

    private static void showMainMenu() {
        System.out.println("\nLibrary Management System");
        System.out.println("1. Add New Item");
        System.out.println("2. View All Items");
        System.out.println("3. Borrow Item");
        System.out.println("4. Return Item");
        System.out.println("5. Exit");
        System.out.print("Enter your choice: ");
    }

    private static void addItem() throws SQLException {
        System.out.println("\nAdd New Item");
        System.out.print("Enter item type (1 for Book, 2 for Magazine): ");
        int type = scanner.nextInt();
        scanner.nextLine(); // consume newline

        System.out.print("Enter ID: ");
        String id = scanner.nextLine();
        System.out.print("Enter title: ");
        String title = scanner.nextLine();
        System.out.print("Enter year: ");
        int year = scanner.nextInt();
        scanner.nextLine(); // consume newline

        if (type == 1) {
            System.out.print("Enter author: ");
            String author = scanner.nextLine();
            System.out.print("Enter genre: ");
            String genre = scanner.nextLine();

            itemDAO.addItem(new Book(id, title, year, author, genre));
        } else {
            System.out.print("Enter issue number: ");
            int issueNumber = scanner.nextInt();

            itemDAO.addItem(new Magazine(id, title, year, issueNumber));
        }
        System.out.println("Item added successfully!");
    }

    private static void viewAllItems() throws SQLException {
        List<LibraryItem> items = itemDAO.getAllItems();
        System.out.println("\nAll Library Items:");
        for (LibraryItem item : items) {
            System.out.println(item);
        }
    }

    private static void borrowItem() throws SQLException {
        System.out.print("Enter item ID to borrow: ");
        String itemId = scanner.nextLine();

        List<LibraryItem> items = itemDAO.getAllItems();
        LibraryItem itemToBorrow = items.stream()
                .filter(item -> item.getId().equals(itemId) && item.isAvailable())
                .findFirst()
                .orElse(null);

        if (itemToBorrow != null) {
            itemDAO.updateAvailability(itemId, false);
            System.out.println("Item borrowed successfully!");
        } else {
            System.out.println("Item not found or not available!");
        }
    }

    private static void returnItem() throws SQLException {
        System.out.print("Enter item ID to return: ");
        String itemId = scanner.nextLine();

        itemDAO.updateAvailability(itemId, true);
        System.out.println("Item returned successfully!");
    }
}