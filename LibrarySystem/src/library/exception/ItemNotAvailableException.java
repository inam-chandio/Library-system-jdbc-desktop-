// 1. First, let's add ItemNotAvailableException.java
package library.exception;

public class ItemNotAvailableException extends LibraryException {
    public ItemNotAvailableException(String message) {
        super(message);
    }
}