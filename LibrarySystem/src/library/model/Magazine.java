

// 4.3 Magazine.java
package library.model;

public class Magazine extends LibraryItem {
    private int issueNumber;
    private String publisher;

    public Magazine(String id, String title, int year, int issueNumber) {
        super(id, title, year);
        this.issueNumber = issueNumber;
    }

    // Getters and setters
    public int getIssueNumber() { return issueNumber; }
    public void setIssueNumber(int issueNumber) { this.issueNumber = issueNumber; }
    public String getPublisher() { return publisher; }
    public void setPublisher(String publisher) { this.publisher = publisher; }

    @Override
    public String getItemType() {
        return "MAGAZINE";
    }

    @Override
    public String toString() {
        return super.toString() + String.format(", Issue Number: %d", issueNumber);
    }
}
