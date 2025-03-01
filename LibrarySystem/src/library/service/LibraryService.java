package library.service;

import library.dao.*;
import library.model.*;
import library.exception.*;
import java.sql.SQLException;
import java.util.List;
import java.time.LocalDateTime;

public class LibraryService {
    private final LibraryItemDAO itemDAO;
    private final UserDAO userDAO;
    private final BorrowingDAO borrowingDAO;
    private static final int MAX_BORROW_LIMIT = 5;
    private static final int BORROW_PERIOD_DAYS = 14;

    public LibraryService() {
        this.itemDAO = new LibraryItemDAO();
        this.userDAO = new UserDAO();
        this.borrowingDAO = new BorrowingDAO();
    }

    // Add a new library item
    public void addItem(LibraryItem item) throws LibraryException {
        validateItem(item);
        try {
            itemDAO.addItem(item);
        } catch (SQLException e) {
            throw new LibraryException("Error adding item: " + e.getMessage(), e);
        }
    }

    // Register a new user
    public void registerUser(User user) throws LibraryException {
        validateUser(user);
        try {
            if (userDAO.findById(user.getId()) != null) {
                throw new LibraryException("User with ID " + user.getId() + " already exists");
            }
            userDAO.addUser(user);
        } catch (SQLException e) {
            throw new LibraryException("Error registering user: " + e.getMessage(), e);
        }
    }

    // Borrow an item
    public void borrowItem(String userId, String itemId) throws LibraryException {
        try {
            // Validate user
            User user = userDAO.findById(userId);
            if (user == null) {
                throw new LibraryException("User not found with ID: " + userId);
            }

            // Validate item
            LibraryItem item = itemDAO.findById(itemId)
                    .orElseThrow(() -> new LibraryException("Item not found with ID: " + itemId));

            // Check if item is available
            if (!item.isAvailable()) {
                throw new ItemNotAvailableException("Item is not available for borrowing: " + itemId);
            }

            // Check borrow limit
            List<LibraryItem> currentBorrowedItems = borrowingDAO.getBorrowedItems(userId);
            if (currentBorrowedItems.size() >= MAX_BORROW_LIMIT) {
                throw new MaxBorrowLimitException("User has reached maximum borrow limit of " + MAX_BORROW_LIMIT + " items");
            }

            // Check if user already has overdue items
            if (hasOverdueItems(userId)) {
                throw new LibraryException("Cannot borrow new items while having overdue items");
            }

            // Process the borrow
            borrowingDAO.borrowItem(userId, itemId);
            item.setLastBorrowDate(LocalDateTime.now());
            item.setAvailable(false);
            itemDAO.updateItem(item);

        } catch (SQLException e) {
            throw new LibraryException("Error processing borrow request: " + e.getMessage(), e);
        }
    }

    // Return an item
    public void returnItem(String userId, String itemId) throws LibraryException {
        try {
            // Validate user and item
            User user = userDAO.findById(userId);
            if (user == null) {
                throw new LibraryException("User not found with ID: " + userId);
            }

            LibraryItem item = itemDAO.findById(itemId)
                    .orElseThrow(() -> new LibraryException("Item not found with ID: " + itemId));

            // Check if user actually borrowed this item
            List<LibraryItem> borrowedItems = borrowingDAO.getBorrowedItems(userId);
            if (borrowedItems.stream().noneMatch(i -> i.getId().equals(itemId))) {
                throw new LibraryException("Item was not borrowed by this user");
            }

            // Process the return
            borrowingDAO.returnItem(userId, itemId);
            item.setAvailable(true);
            item.setLastBorrowDate(null);
            itemDAO.updateItem(item);

        } catch (SQLException e) {
            throw new LibraryException("Error processing return request: " + e.getMessage(), e);
        }
    }

    // Get all library items
    public List<LibraryItem> getAllItems() throws LibraryException {
        try {
            return itemDAO.getAllItems();
        } catch (SQLException e) {
            throw new LibraryException("Error retrieving items: " + e.getMessage(), e);
        }
    }

    // Get borrowed items for a user
    public List<LibraryItem> getBorrowedItems(String userId) throws LibraryException {
        try {
            if (userDAO.findById(userId) == null) {
                throw new LibraryException("User not found with ID: " + userId);
            }
            return borrowingDAO.getBorrowedItems(userId);
        } catch (SQLException e) {
            throw new LibraryException("Error retrieving borrowed items: " + e.getMessage(), e);
        }
    }

    // Get available items only
    public List<LibraryItem> getAvailableItems() throws LibraryException {
        try {
            return itemDAO.getAllItems().stream()
                    .filter(LibraryItem::isAvailable)
                    .toList();
        } catch (SQLException e) {
            throw new LibraryException("Error retrieving available items: " + e.getMessage(), e);
        }
    }

    // Search items by title or author
    public List<LibraryItem> searchItems(String query) throws LibraryException {
        try {
            return itemDAO.searchItems(query);
        } catch (SQLException e) {
            throw new LibraryException("Error searching items: " + e.getMessage(), e);
        }
    }



    // Check for overdue items
    public boolean hasOverdueItems(String userId) throws SQLException {
        List<LibraryItem> borrowedItems = borrowingDAO.getBorrowedItems(userId);
        LocalDateTime now = LocalDateTime.now();

        return borrowedItems.stream()
                .anyMatch(item -> {
                    LocalDateTime borrowDate = item.getLastBorrowDate();
                    return borrowDate != null &&
                            borrowDate.plusDays(BORROW_PERIOD_DAYS).isBefore(now);
                });
    }

    // Get overdue items for a user
    public List<LibraryItem> getOverdueItems(String userId) throws LibraryException {
        try {
            List<LibraryItem> borrowedItems = borrowingDAO.getBorrowedItems(userId);
            LocalDateTime now = LocalDateTime.now();

            return borrowedItems.stream()
                    .filter(item -> {
                        LocalDateTime borrowDate = item.getLastBorrowDate();
                        return borrowDate != null &&
                                borrowDate.plusDays(BORROW_PERIOD_DAYS).isBefore(now);
                    })
                    .toList();
        } catch (SQLException e) {
            throw new LibraryException("Error retrieving overdue items: " + e.getMessage(), e);
        }
    }

    // Validate library item
    private void validateItem(LibraryItem item) throws LibraryException {
        if (item == null) {
            throw new LibraryException("Item cannot be null");
        }
        if (item.getId() == null || item.getId().trim().isEmpty()) {
            throw new LibraryException("Item ID cannot be empty");
        }
        if (item.getTitle() == null || item.getTitle().trim().isEmpty()) {
            throw new LibraryException("Item title cannot be empty");
        }
        if (item.getYear() < 1000 || item.getYear() > LocalDateTime.now().getYear()) {
            throw new LibraryException("Invalid year");
        }

        // Validate book-specific fields
        if (item instanceof Book) {
            Book book = (Book) item;
            if (book.getAuthor() == null || book.getAuthor().trim().isEmpty()) {
                throw new LibraryException("Book author cannot be empty");
            }
            if (book.getGenre() == null || book.getGenre().trim().isEmpty()) {
                throw new LibraryException("Book genre cannot be empty");
            }
        }

        // Validate magazine-specific fields
        if (item instanceof Magazine) {
            Magazine magazine = (Magazine) item;
            if (magazine.getIssueNumber() <= 0) {
                throw new LibraryException("Magazine issue number must be positive");
            }
        }
    }

    // Validate user
    private void validateUser(User user) throws LibraryException {
        if (user == null) {
            throw new LibraryException("User cannot be null");
        }
        if (user.getId() == null || user.getId().trim().isEmpty()) {
            throw new LibraryException("User ID cannot be empty");
        }
        if (user.getName() == null || user.getName().trim().isEmpty()) {
            throw new LibraryException("User name cannot be empty");
        }
        if (user.getEmail() != null && !user.getEmail().matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
            throw new LibraryException("Invalid email format");
        }
    }
}