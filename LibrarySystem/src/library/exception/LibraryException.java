
// 7. Custom Exceptions
// 7.1 LibraryException.java
package library.exception;

public class LibraryException extends Exception {
    public LibraryException(String message) {
        super(message);
    }

    public LibraryException(String message, Throwable cause) {
        super(message, cause);
    }
}
