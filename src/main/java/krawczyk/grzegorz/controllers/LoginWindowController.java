package krawczyk.grzegorz.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import krawczyk.grzegorz.EmailManager;
import krawczyk.grzegorz.views.ViewFactory;

public class LoginWindowController extends BaseController {

    @FXML
    private TextField emailAddressField;

    @FXML
    private Label errorLabel;

    @FXML
    private PasswordField passwordField;

    public LoginWindowController(EmailManager emailManager, ViewFactory viewFactory, String fxmlName) {
        super(emailManager, viewFactory, fxmlName);
    }

    @FXML
    void loginButtonAction(ActionEvent event) {
        System.out.println("Clicked login button!");
        this.viewFactory.showMainWindow();

        // nie mamy dostępu do stage zainicjalizowanego w viewFactory.showLoginWindow().
        // ale mamy dostęp do każdego elementu interfejsu któremu daliśmy id.
        // z elementu możemy pobrać scenę, a ze sceny okno - a to rzutujemy na Stage.
        // brzydki sposób
        Stage stage = (Stage) this.errorLabel.getScene().getWindow();

        // zamykamy stare okno (bez tego wyświetlałyby się oba)
        this.viewFactory.closeStage(stage);
    }
}
