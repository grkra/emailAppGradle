package krawczyk.grzegorz.controllers.services;

import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.scene.web.WebEngine;
import krawczyk.grzegorz.models.EmailMessage;

import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import java.io.IOException;

/**
 * Controller responsible for displaying email message - content of the message.
 * <hr></hr>
 * It extends Service class which is used to perform tasks on background Threads.
 * Service class is part of JavaFX and it makes easier to manage multithreading code.
 * <hr></hr>
 * Service won't return anything (it returns Void object).
 */
public class MessageRendererService extends Service {

    /**
     * EmailMessage set by MainWindowController.
     */
    private EmailMessage emailMessage;

    /**
     * WebEngine is used to render email message. It is managed by WebView node in main wind of the application.
     */
    private WebEngine webEngine;

    /**
     * stringBuffer is used to temporarily store email message content.
     */
    private StringBuffer stringBuffer;

    /**
     * Constructor of MessageRendererService class.
     * @param webEngine - Object of class WebEngine from WebView object.
     *                  WebEngine is used to render (display) email message set int this class. So it is important
     *                  to pass WebEngine object managed (contained) by WebView node in main window of the application.
     */
    public MessageRendererService(WebEngine webEngine) {
        this.webEngine = webEngine;
        this.stringBuffer = new StringBuffer();

        // setOnSucceeded() event handler is triggered only if task state is SUCCEEDED
        // (it works only in case of success of background task in Service was ended, and it was success).
        // So if code from createTask() method is ended successfully it will trigger this event listener which will display message.
        this.setOnSucceeded(event -> {
            displayMessage();
        });
    }

    @Override
    protected Task createTask() {
        return new Task() {
            @Override
            protected Object call() throws Exception {
                try {
                    loadMessage();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return null;
            }
        };
    }

    /**
     * Method loads email message content (from emailMessage property) to stringBuffer.
     * <hr></hr>
     * Method clears stringBuffer property. Then it adds parts of the message to stringBuffer.
     * @throws MessagingException
     * @throws IOException
     */
    private void loadMessage() throws MessagingException, IOException {
        stringBuffer.setLength(0);
        Message message = emailMessage.getMessage();
        // contentType can be simple of multipart
        String contentType = message.getContentType();

        if (isSimpleType(contentType)){
            stringBuffer.append(message.getContent().toString());
        } else if (isMultipartType(contentType)) {
            Multipart multipart = (Multipart) message.getContent();
            loadMultipart(multipart, stringBuffer);
        }
    }

    /**
     * Method recursively loads nested Multipart subcomponents of the passed Multipart to stringBuffer.
     * @param multipart - object of class Multipart (component of the message or subcomponent of previous Multipart component)
     * @param stringBuffer - StringBuffer used to load the message before displaying (load parts of the message)
     * @throws MessagingException
     * @throws IOException
     */
    private void loadMultipart (Multipart multipart, StringBuffer stringBuffer) throws MessagingException, IOException {
        for (int i = multipart.getCount() - 1; i >= 0; i--) {
            BodyPart bodyPart = multipart.getBodyPart(i);
            String bodyPartContentType = bodyPart.getContentType();

            if (isSimpleType(bodyPartContentType)) {
                stringBuffer.append(bodyPart.getContent().toString());
            } else if (isMultipartType(bodyPartContentType)) {
                Multipart multipartSubComponent = (Multipart) bodyPart.getContent();
                loadMultipart(multipartSubComponent, stringBuffer);
            }
        }
    }

    /**
     * Method returns true if passed content type matches simple type of content of a message.
     * @param contentType - Sting containing content type of message.
     * @return boolean - true if content type is simple.
     */
    private boolean isSimpleType(String contentType) {
        if (contentType.contains("TEXT/HTML") ||
                contentType.contains("mixed") ||
                contentType.contains("text")) {
            return true;
        }

        return false;
    }

    /**
     * Method returns true if passed content type matches multipart type of content of a message.
     * @param contentType - Sting containing content type of message.
     * @return boolean - true if content type is multipart.
     */
    private boolean isMultipartType (String contentType) {
        if (contentType.contains("multipart")) {
            return true;
        }

        return false;
    }

    /**
     * Method displays email message content in Web View in main window of the application.
     */
    private void displayMessage() {

        // Email message content was added (part by part) to stringBuffer.
        // Now it is used to display of the message.
        webEngine.loadContent(stringBuffer.toString());
    }

    /**
     * Method sets passed EmailMessage object as property of MessageRendererService.
     * It can be rendered by background thread.
     * @param emailMessage - object of class EmailMessage. Email message to be displayed.
     */
    public void setEmailMessage(EmailMessage emailMessage) {
        this.emailMessage = emailMessage;
    }
}
