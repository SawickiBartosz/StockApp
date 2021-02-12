package pl.edu.pw.mini.zpoif.projekt.jung_sawicki.notifications;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;


public class MailSender {

    Session session;

    public MailSender() {
        final String username = "private@mail.adress.com";
        final String password = "password";

        Properties props = new Properties();
        updateProperties(props);

        this.session = Session.getInstance(props,
                new javax.mail.Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(username, password);
                    }
                });
    }

    public static void main(String[] args) {

        final String username = "private@mail.adress.com";
        final String password = "password";

        String addresslist = "recipient_email@domain.com";
        String text = "some_text";
        String subject = "Test2";


        Properties props = new Properties();
        updateProperties(props);

        Session session = Session.getInstance(props,
                new javax.mail.Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(username, password);
                    }
                });

        try {

            sendMail(addresslist, text, subject, session);

        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }

    public void sendMail(String addresslist, String text, String subject) throws MessagingException {
        sendMail(addresslist, text, subject, this.session);
    }

    private static void sendMail(String addresslist, String text, String subject, Session session) throws MessagingException {
    
        final String username = "private@mail.adress.com";
        Message message = new MimeMessage(session);
        message.setFrom(new InternetAddress(username));
        message.setRecipients(Message.RecipientType.TO,
                InternetAddress.parse(addresslist));
        message.setSubject(subject);
        message.setText(text);

        Transport.send(message);
        System.out.println("Done");
    }

    private static void updateProperties(Properties props) {
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");
    }
}
