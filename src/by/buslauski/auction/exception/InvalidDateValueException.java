package by.buslauski.auction.exception;

/**
 * Created by Acer on 06.05.2017.
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
