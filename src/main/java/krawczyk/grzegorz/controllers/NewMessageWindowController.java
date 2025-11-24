package krawczyk.grzegorz.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.web.HTMLEditor;
import krawczyk.grzegorz.EmailManager;
import krawczyk.grzegorz.views.ViewFactory;

public class NewMessageWindowController extends BaseController {

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

    @FXML
    void sendButtonAction() {
        System.out.println("Send button clicked");
    }
}
