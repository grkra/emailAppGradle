package krawczyk.grzegorz.controllers;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import krawczyk.grzegorz.EmailManager;
import krawczyk.grzegorz.controllers.services.LoginService;
import krawczyk.grzegorz.models.EmailAccount;
import krawczyk.grzegorz.views.ViewFactory;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Controller of Login window of the app.
 */
public class LoginWindowController extends BaseController implements Initializable {

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
     * It is responsible for trying to log in to account.
     * In case of error - it shows error label.
     * In case of success - it displays main window and closes login window.
     * Method validates email address and password, and trays to log in to email account.
     */
    @FXML
    void loginButtonAction() {
        System.out.println("Clicked login button!");

        if (validateFields()) {
            EmailAccount emailAccount = new EmailAccount(emailAddressField.getText(), passwordField.getText());

            // Create Service.
            // Whole login process is done in background thread to not freeze UI.
            LoginService loginService = new LoginService(emailAccount, emailManager);

            // Start Service.
            // start() method from Service class does background task (login process) in separate thread.
            // start() method creates new thread, executes code, returns values - it is simpler than creating threads manually.
            loginService.start();

            // Service ended task.
            // setOnSucceeded() event listener is triggered only if task state is SUCCEEDED
            // (it works only in case of success of background task in Service was ended, and it was success).
            loginService.setOnSucceeded(event -> {

                // getValue() method returns current value of the Worker.
                // In this case createTask() method in LoginService class returns EmailLoginResult.
                EmailLoginResult emailLoginResult = loginService.getValue();

                switch (emailLoginResult) {
                    case SUCCESS:
                        System.out.println("login successful: " + emailAccount);
                        if (!viewFactory.isMainViewInitialized()) {
                            this.viewFactory.showMainWindow();
                        }

                        // From the class there is no access to the Stage initialized in viewFacotry.showLoginWindow(),
                        // but it has access to eny interface element with id.
                        // From any element it is possible to get Scene (which contains that element), and from Scene, Stage.
                        // Ugly method.
                        Stage stage = (Stage) this.errorLabel.getScene().getWindow();

                        // Closing old window (Stage) - without closing window, application would display multiple windows.
                        this.viewFactory.closeStage(stage);
                        return;
                    case FAILED_BY_CREDENTIALS:
                        errorLabel.setText("Email address or password are invalid.");
                        return;
                    case FAILED_BY_UNEXPECTED_ERROR:
                        errorLabel.setText("There was unexpected error.");
                        return;
                    default:
                        return;
                }
            });
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

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
    }
}
