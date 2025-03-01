
// 4. Model Classes
// 4.1 LibraryItem.java (Base class)
package library.model;

import java.io.Serializable;
import java.time.LocalDateTime;

public abstract class LibraryItem implements Serializable {
    private String id;
    private String title;
    private int year;
    private boolean available;
    private LocalDateTime lastBorrowDate;

    public LibraryItem(String id, String title, int year) {
        this.id = id;
        this.title = title;
        this.year = year;
        this.available = true;
    }

    // Getters and setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public int getYear() { return year; }
    public void setYear(int year) { this.year = year; }
    public boolean isAvailable() { return available; }
    public void setAvailable(boolean available) { this.available = available; }
    public LocalDateTime getLastBorrowDate() { return lastBorrowDate; }
    public void setLastBorrowDate(LocalDateTime lastBorrowDate) {
        this.lastBorrowDate = lastBorrowDate;
    }

    public abstract String getItemType();

    @Override
    public String toString() {
        return String.format("ID: %s, Title: %s, Year: %d, Available: %s",
                id, title, year, available);
    }
}
