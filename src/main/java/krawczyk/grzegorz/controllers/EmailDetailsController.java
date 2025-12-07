package krawczyk.grzegorz.controllers;

import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.web.WebView;
import krawczyk.grzegorz.EmailManager;
import krawczyk.grzegorz.controllers.services.MessageRendererService;
import krawczyk.grzegorz.models.EmailMessage;
import krawczyk.grzegorz.views.ViewFactory;

import javax.mail.MessagingException;
import javax.mail.internet.MimeBodyPart;
import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * Controller of Main window of the app.
 */
public class EmailDetailsController extends BaseController implements Initializable {

    private final String LOCATION_OF_DOWNLOADS = System.getProperty("user.home") + "/downloads/";

    @FXML
    private Label attachmentLabel;

    @FXML
    private HBox hBoxDownloads;

    @FXML
    private Label senderLabel;

    @FXML
    private Label subjectLabel;

    @FXML
    private WebView webView;

    /**
     * EmailDetailsController constructor.
     * <hr></hr>
     * It calls BaseController constructor.
     *
     * @param emailManager - an object of the class EmailManager.
     * @param viewFactory  - an object of the class ViewFactory.
     * @param fxmlName     - a String containing name of a fxml file with the extension.
     */
    public EmailDetailsController(EmailManager emailManager, ViewFactory viewFactory, String fxmlName) {
        super(emailManager, viewFactory, fxmlName);
    }

    // Method implemented from Initializable interface.
    // It lets to initialize values of the fields exactly after initialization of an object,
    // so they are already initialized when window is displayed.
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        EmailMessage emailMessage = this.emailManager.getSelectedMessage();
        this.subjectLabel.setText(emailMessage.getSubject());
        this.senderLabel.setText(emailMessage.getSender());

        loadAttachments(emailMessage);

        // It creates new MessageRendererService class object.
        // It passes webEngine of Email Web View to the created object.
        // This way messageRendererService contains WebEngine object of Email Web View window and uses it to display messages.
        // So displaying messages is controlled in MessageRendererService.
        MessageRendererService messageRendererService = new MessageRendererService(this.webView.getEngine());

        // It sets selected EmailMessage object in messageRendererService and restarts it.
        // So this way every time user opens EmailDetailsWindow it starts new background thread which renders email message (content of the message).
        // Every rendering is new thread.
        messageRendererService.setEmailMessage(emailMessage);
        messageRendererService.restart();
    }

    /**
     * Method checks if the message has any attachments and:
     * - if yes it adds button to every attachment
     * - if no it deletes attachment label
     * @param emailMessage - email message which is opened in Email Details window.
     */
    private void loadAttachments(EmailMessage emailMessage) {
        if (emailMessage.getHasAttachments()) {
            for (MimeBodyPart mimeBodyPart: emailMessage.getAttachmentsList()) {

                try {
                    Button button = new AttachmentButton(mimeBodyPart);
                    this.hBoxDownloads.getChildren().add(button);
                } catch (MessagingException e) {
                    throw new RuntimeException(e);
                }
            }
        } else {
            // if there are no attachments, attachmentLabel has no text (instead of hiding it - it isn't possible)
            this.attachmentLabel.setText("");
        }
    }

    /**
     * Class represents button which contains attachment file and is used to download and open attachment.
     */
    private class AttachmentButton extends Button {

        private MimeBodyPart mimeBodyPart;
        // path to downloaded file (to open it)
        private String downloadedFilePath;

        public AttachmentButton(MimeBodyPart mimeBodyPart) throws MessagingException {
            this.mimeBodyPart = mimeBodyPart;
            this.setText(mimeBodyPart.getFileName());
            this.downloadedFilePath = LOCATION_OF_DOWNLOADS + mimeBodyPart.getFileName();

            // it adds event listener which is triggered by click on the button
            this.setOnAction(event -> downloadAttachment());
        }

        private void downloadAttachment() {
            // when clicked it makes button blue:
            colorButtonBlue();

            // downloading file can take some time.
            // so new Servcie is created to do that on different thread
            Service service = new Service() {
                @Override
                protected Task createTask() {
                    return new Task() {
                        @Override
                        protected Object call() throws Exception {
                            // it downloads attachment (mimeBodyPart) to passed directory
                            mimeBodyPart.saveFile(downloadedFilePath);
                            return null;
                        }
                    };
                }
            };

            service.start();

            // Service ended task.
            // setOnSucceeded() event listener is triggered only if task state is SUCCEEDED
            // (it works only in case of success of background task in Service was ended, and it was success).
            service.setOnSucceeded(e -> {

                // when service is finished (file is downloaded) it makes button green
                colorButtonGreen();

                // after download event listener is changed so it triggers different method when clicked again
                // instead of downloading a file, it opens already downloaded file
                this.setOnAction(event -> {
                    File file = new File(downloadedFilePath);

                    // Desktop is Singleton
                    Desktop desktop = Desktop.getDesktop();
                    // Check if file exists
                    if (file.exists()) {
                        try {
                            // Open the file:
                            desktop.open(file);
                        } catch (IOException ex) {
                            ex.printStackTrace();
                        }
                    }
                });
            });
        }

        private void colorButtonBlue() {
            this.setStyle("-fx-background-color: Blue");
        }

        private void colorButtonGreen() {
            this.setStyle("-fx-background-color: Green");
        }
    }
}

