package by.buslauski.auction.validator.exception;

/**
 * @author Mikita Buslauski
 */
public class InvalidDateValueException extends Exception {

    public InvalidDateValueException() {
        super();
    }

    public InvalidDateValueException(String message, Throwable cause) {
        super(message, cause);
    }

    public InvalidDateValueException(Throwable cause) {
        super(cause);
    }

    public InvalidDateValueException(String message) {
        super(message);
    }
}
