
// 11.2 BorrowDialog.java
package library.gui;

import library.model.LibraryItem;
import library.service.LibraryService;
import library.exception.LibraryException;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class BorrowDialog extends JDialog {
    private final LibraryService libraryService;
    private final String userId;
    private boolean itemBorrowed = false;
    private JTable availableItemsTable;
    private ItemTableModel tableModel;

    public BorrowDialog(JComponent parent, LibraryService libraryService, String userId) {
        super((Frame) SwingUtilities.getWindowAncestor(parent), "Borrow Item", true);
        this.libraryService = libraryService;
        this.userId = userId;
        initializeUI();
        loadAvailableItems();
    }

    private void initializeUI() {
        setLayout(new BorderLayout());
        setSize(600, 400);
        setLocationRelativeTo(getOwner());

        // Create table
        tableModel = new ItemTableModel();
        availableItemsTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(availableItemsTable);
        add(scrollPane, BorderLayout.CENTER);

        // Create buttons
        JPanel buttonPanel = new JPanel();
        JButton borrowButton = new JButton("Borrow Selected");
        JButton cancelButton = new JButton("Cancel");
        buttonPanel.add(borrowButton);
        buttonPanel.add(cancelButton);
        add(buttonPanel, BorderLayout.SOUTH);

        // Add action listeners
        borrowButton.addActionListener(e -> borrowSelectedItem());
        cancelButton.addActionListener(e -> dispose());
    }

    private void loadAvailableItems() {
        try {
            List<LibraryItem> items = libraryService.getAllItems().stream()
                    .filter(LibraryItem::isAvailable)
                    .toList();
            tableModel.setItems(items);
        } catch (LibraryException e) {
            JOptionPane.showMessageDialog(this,
                    "Error loading available items: " + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void borrowSelectedItem() {
        int selectedRow = availableItemsTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this,
                    "Please select an item to borrow",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        LibraryItem item = tableModel.getItemAt(selectedRow);
        try {
            libraryService.borrowItem(userId, item.getId());
            itemBorrowed = true;
            dispose();
            JOptionPane.showMessageDialog(this,
                    "Item borrowed successfully",
                    "Success",
                    JOptionPane.INFORMATION_MESSAGE);
        } catch (LibraryException e) {
            JOptionPane.showMessageDialog(this,
                    "Error borrowing item: " + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    public boolean isItemBorrowed() {
        return itemBorrowed;
    }
}
