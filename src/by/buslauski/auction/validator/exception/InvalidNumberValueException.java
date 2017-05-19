package by.buslauski.auction.validator.exception;

/**
 * @author Mikita Buslauski
 */
public class InvalidNumberValueException extends Exception {

    public InvalidNumberValueException() {
        super();
    }

    public InvalidNumberValueException(String message) {
        super(message);
    }

    public InvalidNumberValueException(String message, Throwable cause) {
        super(message, cause);
    }

    public InvalidNumberValueException(Throwable cause) {
        super(cause);
    }

}