package library.gui;

import library.service.LibraryService;
import javax.swing.*;
import java.awt.*;

public class ItemsPanel extends JPanel {
    private final LibraryService libraryService;
    private final JFrame parentFrame;
    private JTable itemsTable;
    private ItemTableModel tableModel;

    public ItemsPanel(JFrame parentFrame, LibraryService libraryService) {
        this.parentFrame = parentFrame;
        this.libraryService = libraryService;
        initializeUI();
        loadItems();
    }

    private void initializeUI() {
        setLayout(new BorderLayout());

        // Create table
        tableModel = new ItemTableModel();
        itemsTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(itemsTable);
        add(scrollPane, BorderLayout.CENTER);

        // Create buttons panel
        JPanel buttonPanel = new JPanel();
        JButton addButton = new JButton("Add Item");
        JButton refreshButton = new JButton("Refresh");
        buttonPanel.add(addButton);
        buttonPanel.add(refreshButton);
        add(buttonPanel, BorderLayout.SOUTH);

        // Add action listeners
        addButton.addActionListener(e -> showAddItemDialog());
        refreshButton.addActionListener(e -> loadItems());
    }

    private void showAddItemDialog() {
        AddItemDialog dialog = new AddItemDialog(parentFrame, libraryService);
        dialog.setVisible(true);
        if (dialog.isItemAdded()) {
            loadItems();
        }
    }

    private void loadItems() {
        try {
            tableModel.setItems(libraryService.getAllItems());
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "Error loading items: " + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }
}