package library.gui;

import library.model.LibraryItem;
import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;
import java.util.List;

public class BorrowedItemsTableModel extends AbstractTableModel {
    private final List<LibraryItem> items = new ArrayList<>();
    private final String[] columns = {"ID", "Title", "Type", "Due Date"};

    public void setItems(List<LibraryItem> items) {
        this.items.clear();
        this.items.addAll(items);
        fireTableDataChanged();
    }

    public LibraryItem getItemAt(int row) {
        return items.get(row);
    }

    @Override
    public int getRowCount() {
        return items.size();
    }

    @Override
    public int getColumnCount() {
        return columns.length;
    }

    @Override
    public String getColumnName(int column) {
        return columns[column];
    }

    @Override
    public Object getValueAt(int row, int column) {
        LibraryItem item = items.get(row);
        return switch (column) {
            case 0 -> item.getId();
            case 1 -> item.getTitle();
            case 2 -> item.getItemType();
            case 3 -> item.getLastBorrowDate() != null ?
                    item.getLastBorrowDate().plusDays(14).toString() : "N/A";
            default -> null;
        };
    }
}