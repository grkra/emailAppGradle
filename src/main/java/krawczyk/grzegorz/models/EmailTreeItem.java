package krawczyk.grzegorz.models;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TreeItem;

import javax.mail.Flags;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

/**
 * Class represents email address (main folder) to be displayed in TreeView in Main Window of the application.
 * <hr></hr>
 * Class extends TreeItem class.
 * @param <String>
 */
public class EmailTreeItem<String> extends TreeItem<String> {
    private String name;

    /**
     * List of email messages inside a folder
     */
    private ObservableList<EmailMessage> emailMessages;

    private int unreadMessagesCount;

    /**
     * Constructor of the class EmailTreeItem.
     * @param name - String containing email address to be displayed in TreeView menu in Main Window of the application
     */
    public EmailTreeItem(String name) {
        super(name);
        this.name = name;
        this.emailMessages = FXCollections.observableArrayList();
    }

    /**
     * Method creates email message in the app and adds it to the list of messages in current folder.
     * <hr></hr>
     * This method gets and object of the class Email (which is an email message on the email server provider side)
     * and transforms it to object of the class EmailMessage (which is and email message inside the app)
     * @param message - object of the class Message (email on email account provider side)
     * @throws MessagingException
     */
    public void addEmail(Message message) throws MessagingException {
        boolean messageWasRead = message.getFlags().contains(Flags.Flag.SEEN);
        EmailMessage emailMessage = new EmailMessage(
                message.getSubject(),
                message.getFrom()[0].toString(),
                message.getRecipients(MimeMessage.RecipientType.TO)[0].toString(),
                message.getSize(),
                message.getSentDate(),
                messageWasRead,
                message
        );

        emailMessages.add(emailMessage);
        if (!messageWasRead) {
            incrementMessageCount();
        }
        System.out.println("Added to " + name + " " + message.getSubject());
    }

    public void incrementMessageCount() {
        unreadMessagesCount++;
        updateName();
    }

    private void updateName () {
        if (unreadMessagesCount > 0) {
            this.setValue((String) (this.name + "(" +unreadMessagesCount + ")"));
        } else {
            this.setValue(name);
        }
    }
}
