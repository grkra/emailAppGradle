package krawczyk.grzegorz.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.web.HTMLEditor;
import javafx.stage.Stage;
import krawczyk.grzegorz.EmailManager;
import krawczyk.grzegorz.controllers.services.EmailSenderService;
import krawczyk.grzegorz.models.EmailAccount;
import krawczyk.grzegorz.views.ViewFactory;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Controller of new message window of the app.
 */
public class NewMessageWindowController extends BaseController implements Initializable {

    @FXML
    private ChoiceBox<EmailAccount> emailAccountChoice;

    @FXML
    private Label errorLabel;

    @FXML
    private HTMLEditor htmlEditor;

    @FXML
    private TextField recipientTextField;

    @FXML
    private Button sendButton;

    @FXML
    private TextField subjectTextField;

    /**
     * NewMessage constructor.
     * <hr></hr>
     * Saves passed values to properties.
     *
     * @param emailManager - an object of the class EmailManager.
     * @param viewFactory  - an object of the class ViewFactory.
     * @param fxmlName     - a String containing name of a fxml file with the extension.
     */
    public NewMessageWindowController(EmailManager emailManager, ViewFactory viewFactory, String fxmlName) {
        super(emailManager, viewFactory, fxmlName);
    }

    /**
     * Event listener triggered when Send button is clicked.
     * It is responsible for sending a message.
     * In case of error - it shows error label.
     * In case of success - it closes the window.
     */
    @FXML
    void sendButtonAction() {

        // Create Service.
        // Whole sending email process is done in background thread to not freeze UI.
        EmailSenderService emailSenderService = new EmailSenderService(
                emailAccountChoice.getValue(),
                subjectTextField.getText(),
                recipientTextField.getText(),
                htmlEditor.getHtmlText()
        );

        // Start Service.
        // start() method from Service class does background task (login process) in separate thread.
        // start() method creates new thread, executes code, returns values - it is simpler than creating threads manually.
        emailSenderService.start();

        // Service ended task.
        // setOnSucceeded() event listener is triggered only if task state is SUCCEEDED
        // (it works only in case of success of background task in Service was ended, and it was success).
        emailSenderService.setOnSucceeded(event -> {
            EmailSendingResult emailSendingResult = emailSenderService.getValue();

            switch (emailSendingResult) {
                case SUCCESS:
                    // From the class there is no access to the Stage initialized in viewFacotry.showLoginWindow(),
                    // but it has access to eny interface element with id.
                    // From any element it is possible to get Scene (which contains that element), and from Scene, Stage.
                    // Ugly method.
                    Stage stage = (Stage) this.recipientTextField.getScene().getWindow();
                    // Closing old window (Stage) - without closing window, application would display multiple windows.
                    this.viewFactory.closeStage(stage);
                    break;
                case FAILED_BY_PROVIDER:
                    this.errorLabel.setText("Provider error.");
                    break;
                case FAILED_BY_UNEXPECTED_ERROR:
                    this.errorLabel.setText("Unexpected error.");
                    break;
            }
        });
    }

    // Method implemented from Initializable interface.
    // It lets to initialize values of the fields exactly after initialization of an object,
    // so they are already initialized when window is displayed.
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        // it sets options:
        emailAccountChoice.setItems(emailManager.getEmailAccounts());

        // it set default value
        emailAccountChoice.setValue(emailManager.getEmailAccounts().getFirst());
    }
}
