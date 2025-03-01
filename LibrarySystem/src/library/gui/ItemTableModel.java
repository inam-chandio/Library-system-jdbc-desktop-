// 10. Table Models
// 10.1 ItemTableModel.java
package library.gui;

import library.model.LibraryItem;
import library.model.Book;
import library.model.Magazine;
import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;
import java.util.List;

public class ItemTableModel extends AbstractTableModel {
    private final List<LibraryItem> items = new ArrayList<>();
    private final String[] columns = {"ID", "Type", "Title", "Year", "Author/Issue", "Genre", "Available"};

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
            case 1 -> item.getItemType();
            case 2 -> item.getTitle();
            case 3 -> item.getYear();
            case 4 -> item instanceof Book ? ((Book) item).getAuthor() :
                    item instanceof Magazine ? ((Magazine) item).getIssueNumber() : "";
            case 5 -> item instanceof Book ? ((Book) item).getGenre() : "";
            case 6 -> item.isAvailable();
            default -> null;
        };
    }
}