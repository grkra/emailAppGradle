package krawczyk.grzegorz.models;

import com.sun.glass.ui.Size;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Class represents email message in the app.
 * <hr></hr>
 * Email message fetched from server is different class - Message.
 */
public class EmailMessage {

    // TableView class in JavaFX doesn't support String. Instead, it supports SimpleProperty classes.
    private SimpleStringProperty subject;
    private SimpleStringProperty sender;
    private SimpleStringProperty recipient;
    private SimpleObjectProperty<SizeInteger> size;
    private SimpleObjectProperty<Date> date;
    private boolean wasRead;
    // Message class represent actual email message from provider. This is pointer to email message from provider point of view.
    private Message message;

    // List of attachments in the message:
    private List<MimeBodyPart> attachmentsList = new ArrayList<>();
    private boolean hasAttachemnts = false;

    /**
     * Constructor of the class EmailMessage. It creates email message in the app.
     *
     * @param subject   - (String) subject of the message
     * @param sender    - (String) sender of the message (email address)
     * @param recipient - (String) recipient of the message (email address)
     * @param size      - (int) size of the message (bytes)
     * @param date      - (Date) send date of the message
     * @param wasRead   - (boolean) information if the message was already read
     * @param message   (Message) email message from server
     */
    public EmailMessage(String subject, String sender, String recipient, int size, Date date, boolean wasRead, Message message) {
        this.subject = new SimpleStringProperty(subject);
        this.sender = new SimpleStringProperty(sender);
        this.recipient = new SimpleStringProperty(recipient);
        this.size = new SimpleObjectProperty<SizeInteger>(new SizeInteger(size));
        this.date = new SimpleObjectProperty<>(date);

        this.wasRead = wasRead;
        this.message = message;
    }

    /**
     * Method returns subject of the message
     *
     * @return String - subject of the message
     */
    public String getSubject() {
        return subject.get();
    }

    /**
     * Method returns sender (email address) of the message
     *
     * @return String - sender of the message
     */
    public String getSender() {
        return sender.get();
    }

    /**
     * Method returns recipient (of the message) of the message
     *
     * @return String - recipient of the message
     */
    public String getRecipient() {
        return recipient.get();
    }

    /**
     * Method returns size (in bytes) of the message
     *
     * @return SizeInteger - size of the message
     */
    public SizeInteger getSize() {
        return size.get();
    }

    /**
     * Method returns send date of the message
     *
     * @return Date - send date of the message
     */
    public Date getDate() {
        return date.get();
    }

    /**
     * Method returns if the message was read already
     *
     * @return Boolean - true if the message was read, false if it wasn't
     */
    public boolean getWasRead() {
        return wasRead;
    }

    /**
     * Method sets if the message was read
     *
     * @param wasRead Boolean - true if the message was read, false if it wasn't
     */
    public void setWasRead(boolean wasRead) {
        this.wasRead = wasRead;
    }

    /**
     * Method returns email message (object representing email message on a server)
     *
     * @return Message - email message (object representing email message on a server)
     */
    public Message getMessage() {
        return message;
    }

    /**
     * Method adds attachment (mimeBodyPart) from the message (Message) to the list of attachments in the message (EmailMessage)
     * and sets information, that message has attachments.
     *
     * @param mimeBodyPart MimeBodyPart - attachment to the message
     */
    public void addAttachment(MimeBodyPart mimeBodyPart) {
        this.hasAttachemnts = true;
        this.attachmentsList.add(mimeBodyPart);
    }

    /**
     * Method returns information if the email has any attachments
     *
     * @return Boolean - true if the message has attachments, false if hasn't
     */
    public boolean getHasAttachments() {
        return this.hasAttachemnts;
    }

    /**
     * Method returns list of attachments of the email
     * @return List<MimeBodyPart> - list of all attachments
     */
    public List<MimeBodyPart> getAttachmentsList() {
        return attachmentsList;
    }
}
