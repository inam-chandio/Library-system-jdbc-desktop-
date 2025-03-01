package library.gui;

import javax.swing.*;
import java.awt.*;
import library.service.LibraryService;

public class MainFrame extends JFrame {
    private final LibraryService libraryService;
    private JTabbedPane tabbedPane;

    public MainFrame() {
        libraryService = new LibraryService();
        initializeUI();
    }

    private void initializeUI() {
        setTitle("Library Management System");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);
        setLocationRelativeTo(null);

        tabbedPane = new JTabbedPane();
        tabbedPane.addTab("Items", new ItemsPanel(this, libraryService));
        tabbedPane.addTab("Borrowing", new BorrowingPanel(this, libraryService));
        tabbedPane.addTab("Users", new UserPanel(libraryService));

        add(tabbedPane);
    }
}