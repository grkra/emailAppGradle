package krawczyk.grzegorz.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import krawczyk.grzegorz.EmailManager;
import krawczyk.grzegorz.controllers.services.LoginService;
import krawczyk.grzegorz.models.EmailAccount;
import krawczyk.grzegorz.views.ViewFactory;

/**
 * Controller of Login window of the app.
 */
public class LoginWindowController extends BaseController {

    @FXML
    private TextField emailAddressField;

    @FXML
    private Label errorLabel;

    @FXML
    private PasswordField passwordField;

    /**
     * LoginWindowController constructor.
     * <hr></hr>
     * It calls BaseController constructor.
     *
     * @param emailManager - an object of the class EmailManager.
     * @param viewFactory  - an object of the class ViewFactory.
     * @param fxmlName     - a String containing name of a fxml file with the extension.
     */
    public LoginWindowController(EmailManager emailManager, ViewFactory viewFactory, String fxmlName) {
        super(emailManager, viewFactory, fxmlName);
    }

    /**
     * Event listener triggered when Login button is clicked.
     * Method validates email address and password, and trays to log in to email account.
     *
     * @param event - click on the Login button.
     */
    @FXML
    void loginButtonAction(ActionEvent event) {
        System.out.println("Clicked login button!");

        if (validateFields()) {
            EmailAccount emailAccount = new EmailAccount(emailAddressField.getText(), passwordField.getText());

            LoginService loginService = new LoginService(emailAccount, emailManager);

            EmailLoginResult emailLoginResult = loginService.login();

            switch (emailLoginResult) {
                case SUCCESS:
                    System.out.println("login successful: " + emailAccount);

                    this.viewFactory.showMainWindow();

                    // From the class there is no access to the Stage initialized in viewFacotry.showLoginWindow(),
                    // but it has access to eny interface element with id.
                    // From any element it is possible to get Scene (which contains that element), and from Scene, Stage.
                    // Ugly method.
                    Stage stage = (Stage) this.errorLabel.getScene().getWindow();

                    // Closing old window (Stage) - without closing window, application would display multiple windows.
                    this.viewFactory.closeStage(stage);

                    return;
            }
        }
    }

    private boolean validateFields() {
        if (emailAddressField.getText().isEmpty()) {
            errorLabel.setText("Please fill email address");
            return false;
        }
        if (passwordField.getText().isEmpty()) {
            errorLabel.setText("Please fill password");
            return false;
        }

        return true;
    }
}
