
// 4.2 Book.java
package library.model;

public class Book extends LibraryItem {
    private String author;
    private String genre;
    private String isbn;

    public Book(String id, String title, int year, String author, String genre) {
        super(id, title, year);
        this.author = author;
        this.genre = genre;
    }

    // Getters and setters
    public String getAuthor() { return author; }
    public void setAuthor(String author) { this.author = author; }
    public String getGenre() { return genre; }
    public void setGenre(String genre) { this.genre = genre; }
    public String getIsbn() { return isbn; }
    public void setIsbn(String isbn) { this.isbn = isbn; }

    @Override
    public String getItemType() {
        return "BOOK";
    }

    @Override
    public String toString() {
        return super.toString() + String.format(", Author: %s, Genre: %s", author, genre);
    }
}
