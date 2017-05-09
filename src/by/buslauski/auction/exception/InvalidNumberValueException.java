package by.buslauski.auction.exception;

/**
 * Created by Acer on 06.05.2017.
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