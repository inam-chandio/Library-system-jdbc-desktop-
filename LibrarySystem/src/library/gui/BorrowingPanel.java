
// 8.3 BorrowingPanel.java
package library.gui;

import javax.swing.*;
import java.awt.*;
import java.util.List;

import library.service.LibraryService;
import library.model.LibraryItem;
import library.exception.LibraryException;

public class BorrowingPanel extends JPanel {
    private final LibraryService libraryService;
    private JTextField userIdField;
    private JTable borrowedItemsTable;
    private BorrowedItemsTableModel tableModel;

    public BorrowingPanel(MainFrame mainFrame, LibraryService libraryService) {
        this.libraryService = libraryService;
        initializeUI();
    }

    private void initializeUI() {
        setLayout(new BorderLayout());

        // User ID panel
        JPanel userPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        userPanel.add(new JLabel("User ID:"));
        userIdField = new JTextField(20);
        JButton loadButton = new JButton("Load Borrowed Items");
        userPanel.add(userIdField);
        userPanel.add(loadButton);
        add(userPanel, BorderLayout.NORTH);

        // Borrowed items table
        tableModel = new BorrowedItemsTableModel();
        borrowedItemsTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(borrowedItemsTable);
        add(scrollPane, BorderLayout.CENTER);

        // Buttons panel
        JPanel buttonPanel = new JPanel();
        JButton borrowButton = new JButton("Borrow New Item");
        JButton returnButton = new JButton("Return Item");
        buttonPanel.add(borrowButton);
        buttonPanel.add(returnButton);
        add(buttonPanel, BorderLayout.SOUTH);

        // Add action listeners
        loadButton.addActionListener(e -> loadBorrowedItems());
        borrowButton.addActionListener(e -> showBorrowDialog());
        returnButton.addActionListener(e -> returnSelectedItem());
    }

    private void loadBorrowedItems() {
        String userId = userIdField.getText().trim();
        if (userId.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Please enter a user ID",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            List<LibraryItem> items = libraryService.getBorrowedItems(userId);
            tableModel.setItems(items);
        } catch (LibraryException e) {
            JOptionPane.showMessageDialog(this,
                    "Error loading borrowed items: " + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void showBorrowDialog() {
        String userId = userIdField.getText().trim();
        if (userId.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Please enter a user ID",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        BorrowDialog dialog = new BorrowDialog(this, libraryService, userId);
        dialog.setVisible(true);
        if (dialog.isItemBorrowed()) {
            loadBorrowedItems();
        }
    }

    private void returnSelectedItem() {
        int selectedRow = borrowedItemsTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this,
                    "Please select an item to return",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        String userId = userIdField.getText().trim();
        LibraryItem item = tableModel.getItemAt(selectedRow);

        try {
            libraryService.returnItem(userId, item.getId());
            loadBorrowedItems();
            JOptionPane.showMessageDialog(this,
                    "Item returned successfully",
                    "Success",
                    JOptionPane.INFORMATION_MESSAGE);
        } catch (LibraryException e) {
            JOptionPane.showMessageDialog(this,
                    "Error returning item: " + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }
}