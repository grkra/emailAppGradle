package krawczyk.grzegorz.controllers.services;

import javafx.concurrent.Service;
import javafx.concurrent.Task;
import krawczyk.grzegorz.controllers.EmailSendingResult;
import krawczyk.grzegorz.models.EmailAccount;

import javax.mail.*;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

public class EmailSenderService extends Service<EmailSendingResult> {
    private EmailAccount emailAccount;
    private String subject;
    private String recipient;
    private String content;

    /**
     * Constructor of the class EmailSenderService.
     *
     * @param emailAccount
     * @param subject
     * @param recipient
     * @param content
     */
    public EmailSenderService(EmailAccount emailAccount, String subject, String recipient, String content) {
        this.emailAccount = emailAccount;
        this.subject = subject;
        this.recipient = recipient;
        this.content = content;
    }

    // In NewMessageWindowController there is emailSendService.start() method called (in sendButtonAction()).
    // start() method calls createTask() in which all background task code is.
    @Override
    protected Task<EmailSendingResult> createTask() {
        return new Task<EmailSendingResult>() {
            @Override
            protected EmailSendingResult call() {
                try {
                    //Create message to send:
                    MimeMessage mimeMessage = new MimeMessage(emailAccount.getSession());
                    mimeMessage.setFrom(emailAccount.getAddress());
                    mimeMessage.setRecipients(Message.RecipientType.TO, recipient);
                    mimeMessage.setSubject(subject);

                    // Set content of the message:
                    Multipart multipart = new MimeMultipart();
                    BodyPart messageBodyPart = new MimeBodyPart();
                    messageBodyPart.setContent(content, "text/html");
                    multipart.addBodyPart(messageBodyPart);
                    mimeMessage.setContent(multipart);

                    // Send the message
                    Transport transport = emailAccount.getSession().getTransport();
                    transport.connect(
                            emailAccount.getProperties().getProperty("outgoingHost"),
                            emailAccount.getAddress(),
                            emailAccount.getPassword()
                    );
                    transport.sendMessage(mimeMessage,mimeMessage.getAllRecipients());
                    transport.close();

                    return EmailSendingResult.SUCCESS;
                } catch (MessagingException e) {
                    e.printStackTrace();
                    return EmailSendingResult.FAILED_BY_PROVIDER;
                } catch (Exception e) {
                    return EmailSendingResult.FAILED_BY_UNEXPECTED_ERROR;
                }
            }
        };
    }
}
