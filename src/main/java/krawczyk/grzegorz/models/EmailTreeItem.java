package krawczyk.grzegorz.models;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TreeItem;

import javax.mail.Flags;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

/**
 * Class represents element to be displayed in Email Tree View in Main Window of the application.
 * It can be email account (address) as main folder or subfolder in this email account.
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
     * Method creates email message in the application and adds it to the TOP of the list of messages in current folder.
     * <hr></hr>
     * This method gets and object of the class Message (which is an email message on the email server provider side)
     * and transforms it to object of the class EmailMessage (which is and email message inside the app)
     * @param message - object of the class Message (email on email account provider side)
     * @throws MessagingException
     */
    public void addEmail(Message message) throws MessagingException {
        EmailMessage emailMessage = fetchMessage(message);
        this.emailMessages.add(emailMessage);
    }

    /**
     * Method creates email message in the application and adds it to the END of the list of messages in current folder.
     * <hr></hr>
     * This method gets and object of the class Message (which is an email message on the email server provider side)
     * and transforms it to object of the class EmailMessage (which is and email message inside the app)
     * @param message - object of the class Message (email on email account provider side)
     * @throws MessagingException
     */
    public void addEmailToTop(Message message) throws MessagingException {
        EmailMessage emailMessage = fetchMessage(message);
        this.emailMessages.add(0,emailMessage);
    }

    /**
     * Method creates new EmailMessage object (which is and email message inside the app)
     * from Message object (which is an email message on the email server provider side)
     * @param message - object of the class Message (email on email account provider side)
     * @throws MessagingException
     */
    private EmailMessage fetchMessage(Message message) throws MessagingException {
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

        if (!messageWasRead) {
            incrementMessageCount();
        }

        return emailMessage;
    }

    /**
     * Method increases number of unread messages in the folder in Email Tree View and updates it in View.
     */
    public void incrementMessageCount() {
        unreadMessagesCount++;
        updateName();
    }

    /**
     * Method decreases number of unread messages in the folder in Email Tree View and updates it in View.
     */
    public void decrementMessageCount() {
        unreadMessagesCount--;
        updateName();
    }

    /**
     * Method updates name of the folder displayed in the Email Tree View based on number of unread messages it hat folder.
     */
    private void updateName () {
        if (unreadMessagesCount > 0) {
            this.setValue((String) (this.name + "(" +unreadMessagesCount + ")"));
        } else {
            this.setValue(name);
        }
    }

    /**
     * Method returns list of email messages inside a folder.
     * @return ObservableList<EmailMessage> - list of email messages in the folder.
     */
    public ObservableList<EmailMessage> getEmailMessages() {
        return emailMessages;
    }
}
