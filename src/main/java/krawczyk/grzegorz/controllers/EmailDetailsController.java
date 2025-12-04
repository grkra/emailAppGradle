package krawczyk.grzegorz.controllers;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.web.WebView;
import krawczyk.grzegorz.EmailManager;
import krawczyk.grzegorz.controllers.services.MessageRendererService;
import krawczyk.grzegorz.models.EmailMessage;
import krawczyk.grzegorz.views.ViewFactory;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Controller of Main window of the app.
 */
public class EmailDetailsController extends BaseController implements Initializable {

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
}

