package by.buslauski.auction.mail.exception;

import java.security.PublicKey;

/**
 * Created by Acer on 08.05.2017.
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
