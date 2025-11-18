package krawczyk.grzegorz.models;

import com.sun.glass.ui.Size;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;

import javax.mail.Message;
import java.util.Date;

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

    public EmailMessage(String subject, String sender, String recipient, int size, Date date, boolean wasRead, Message message) {
        this.subject = new SimpleStringProperty(subject);
        this.sender = new SimpleStringProperty(sender);
        this.recipient = new SimpleStringProperty(recipient);
        this.size = new SimpleObjectProperty<SizeInteger>(new SizeInteger(size));
        this.date = new SimpleObjectProperty<>(date);

        this.wasRead = wasRead;
        this.message = message;
    }

    public String getSubject() {
        return subject.get();
    }

    public String getSender() {
        return sender.get();
    }

    public String getRecipient() {
        return recipient.get();
    }

    public SizeInteger getSize() {
        return size.get();
    }


    public Date getDate() {
        return date.get();
    }


    public boolean getWasRead() {
        return wasRead;
    }
    public void setWasRead (boolean wasRead) {
        this.wasRead = wasRead;
    }

    public Message getMessage() {
        return message;
    }
}
