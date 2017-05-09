package by.buslauski.auction.mail;


import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

/**
 * Created by Acer on 08.05.2017.
 */
public class MailSender {
    private static final String AUCTION_MAIL = "auctionhouse.webproject@gmail.com";
    private static final String PASSWORD = "AuctionHouse95";
    private static final Properties MAIL_PROPERTIES;

    static {
        MAIL_PROPERTIES = new Properties();
        MAIL_PROPERTIES.put("mail.smtp.host", "smtp.gmail.com");
        MAIL_PROPERTIES.put("mail.smtp.socketFactory.port", "465");
        MAIL_PROPERTIES.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        MAIL_PROPERTIES.put("mail.smtp.auth", "true");
        MAIL_PROPERTIES.put("mail.smtp.port", "465");
    }

    public static void sendMessage(String subject, String content, String recipientEmail) {
        Session session = Session.getInstance(MAIL_PROPERTIES, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(AUCTION_MAIL, PASSWORD);
            }
        });
        try {
            Message message = new MimeMessage(session);
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(recipientEmail));
            message.setSubject(subject);
            message.setText(content);
            Transport.send(message);
        } catch (MessagingException e) {
            System.out.println("Lol");
            e.printStackTrace();
        }
    }

}
