package library.gui;

import library.model.*;
import library.service.LibraryService;
import library.exception.LibraryException;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class AddItemDialog extends JDialog {
    private final LibraryService libraryService;
    private boolean itemAdded = false;

    private JComboBox<String> typeCombo;
    private JTextField idField;
    private JTextField titleField;
    private JTextField yearField;
    private JTextField authorField;
    private JTextField genreField;
    private JTextField issueNumberField;

    public AddItemDialog(Frame owner, LibraryService libraryService) {
        super(owner, "Add New Item", true);
        this.libraryService = libraryService;
        initializeUI();
    }

    private void initializeUI() {
        setLayout(new BorderLayout());
        setSize(400, 300);
        setLocationRelativeTo(getOwner());

        // Create form panel
        JPanel formPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;

        // Add form fields
        gbc.gridx = 0; gbc.gridy = 0;
        formPanel.add(new JLabel("Type:"), gbc);
        typeCombo = new JComboBox<>(new String[]{"Book", "Magazine"});
        gbc.gridx = 1;
        formPanel.add(typeCombo, gbc);

        gbc.gridx = 0; gbc.gridy++;
        formPanel.add(new JLabel("ID:"), gbc);
        idField = new JTextField(20);
        gbc.gridx = 1;
        formPanel.add(idField, gbc);

        gbc.gridx = 0; gbc.gridy++;
        formPanel.add(new JLabel("Title:"), gbc);
        titleField = new JTextField(20);
        gbc.gridx = 1;
        formPanel.add(titleField, gbc);

        gbc.gridx = 0; gbc.gridy++;
        formPanel.add(new JLabel("Year:"), gbc);
        yearField = new JTextField(20);
        gbc.gridx = 1;
        formPanel.add(yearField, gbc);

        gbc.gridx = 0; gbc.gridy++;
        formPanel.add(new JLabel("Author:"), gbc);
        authorField = new JTextField(20);
        gbc.gridx = 1;
        formPanel.add(authorField, gbc);

        gbc.gridx = 0; gbc.gridy++;
        formPanel.add(new JLabel("Genre:"), gbc);
        genreField = new JTextField(20);
        gbc.gridx = 1;
        formPanel.add(genreField, gbc);

        gbc.gridx = 0; gbc.gridy++;
        formPanel.add(new JLabel("Issue Number:"), gbc);
        issueNumberField = new JTextField(20);
        gbc.gridx = 1;
        formPanel.add(issueNumberField, gbc);

        // Add type change listener
        typeCombo.addActionListener(this::handleTypeChange);
        handleTypeChange(null); // Initial setup

        // Add buttons
        JPanel buttonPanel = new JPanel();
        JButton saveButton = new JButton("Save");
        JButton cancelButton = new JButton("Cancel");
        buttonPanel.add(saveButton);
        buttonPanel.add(cancelButton);

        // Add action listeners
        saveButton.addActionListener(e -> saveItem());
        cancelButton.addActionListener(e -> dispose());

        // Add panels to dialog
        add(formPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    private void handleTypeChange(ActionEvent e) {
        boolean isBook = "Book".equals(typeCombo.getSelectedItem());
        authorField.setEnabled(isBook);
        genreField.setEnabled(isBook);
        issueNumberField.setEnabled(!isBook);
    }

    private void saveItem() {
        try {
            String id = idField.getText().trim();
            String title = titleField.getText().trim();
            int year = Integer.parseInt(yearField.getText().trim());

            LibraryItem item;
            if ("Book".equals(typeCombo.getSelectedItem())) {
                item = new Book(id, title, year,
                        authorField.getText().trim(),
                        genreField.getText().trim());
            } else {
                item = new Magazine(id, title, year,
                        Integer.parseInt(issueNumberField.getText().trim()));
            }

            libraryService.addItem(item);
            itemAdded = true;
            dispose();

            JOptionPane.showMessageDialog(this,
                    "Item added successfully",
                    "Success",
                    JOptionPane.INFORMATION_MESSAGE);
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this,
                    "Please enter valid numeric values",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        } catch (LibraryException ex) {
            JOptionPane.showMessageDialog(this,
                    "Error adding item: " + ex.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    public boolean isItemAdded() {
        return itemAdded;
    }
}