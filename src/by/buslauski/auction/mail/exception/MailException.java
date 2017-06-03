package by.buslauski.auction.mail.exception;


/**
 * @author Mikita Buslauski
 */
public class MailException extends Exception {
    public MailException() {
        super();
    }

    public MailException(Throwable cause) {
        super(cause);
    }

    public MailException(String message) {
        super(message);
    }

    public MailException(String message, Throwable cause) {
        super(message, cause);
    }
}
