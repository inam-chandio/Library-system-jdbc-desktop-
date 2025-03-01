
// 11.3 UserPanel.java
package library.gui;

import library.model.User;
import library.service.LibraryService;
import library.exception.LibraryException;

import javax.swing.*;
import java.awt.*;

public class UserPanel extends JPanel {
    private final LibraryService libraryService;
    private JTextField idField;
    private JTextField nameField;
    private JTextField emailField;
    private JTextField phoneField;

    public UserPanel(LibraryService libraryService) {
        this.libraryService = libraryService;
        initializeUI();
    }

    private void initializeUI() {
        setLayout(new BorderLayout());

        // Create form panel
        JPanel formPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;

        // Add form fields
        gbc.gridx = 0; gbc.gridy = 0;
        formPanel.add(new JLabel("User ID:"), gbc);
        idField = new JTextField(20);
        gbc.gridx = 1;
        formPanel.add(idField, gbc);

        gbc.gridx = 0; gbc.gridy++;
        formPanel.add(new JLabel("Name:"), gbc);
        nameField = new JTextField(20);
        gbc.gridx = 1;
        formPanel.add(nameField, gbc);

        gbc.gridx = 0; gbc.gridy++;
        formPanel.add(new JLabel("Email:"), gbc);
        emailField = new JTextField(20);
        gbc.gridx = 1;
        formPanel.add(emailField, gbc);

        gbc.gridx = 0; gbc.gridy++;
        formPanel.add(new JLabel("Phone:"), gbc);
        phoneField = new JTextField(20);
        gbc.gridx = 1;
        formPanel.add(phoneField, gbc);

        // Add buttons
        JPanel buttonPanel = new JPanel();
        JButton registerButton = new JButton("Register User");
        JButton clearButton = new JButton("Clear");
        buttonPanel.add(registerButton);
        buttonPanel.add(clearButton);

        // Add action listeners
        registerButton.addActionListener(e -> registerUser());
        clearButton.addActionListener(e -> clearFields());

        // Add panels to main panel
        add(formPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    private void registerUser() {
        try {
            User user = new User(
                    idField.getText().trim(),
                    nameField.getText().trim(),
                    emailField.getText().trim()
            );
            user.setPhone(phoneField.getText().trim());

            libraryService.registerUser(user);
            JOptionPane.showMessageDialog(this,
                    "User registered successfully",
                    "Success",
                    JOptionPane.INFORMATION_MESSAGE);
            clearFields();
        } catch (IllegalArgumentException | LibraryException e) {
            JOptionPane.showMessageDialog(this,
                    "Error registering user: " + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void clearFields() {
        idField.setText("");
        nameField.setText("");
        emailField.setText("");
        phoneField.setText("");
    }
}