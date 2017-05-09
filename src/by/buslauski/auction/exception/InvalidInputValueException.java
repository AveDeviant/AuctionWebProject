package by.buslauski.auction.exception;

/**
 * Created by Acer on 06.05.2017.
 */
public class InvalidInputValueException extends Exception {

    public InvalidInputValueException() {
        super();
    }

    public InvalidInputValueException(String message, Throwable cause) {
        super(message, cause);
    }

    public InvalidInputValueException(Throwable cause) {
        super(cause);
    }

    public InvalidInputValueException(String message) {
        super(message);
    }
}
