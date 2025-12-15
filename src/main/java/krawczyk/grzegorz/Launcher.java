package krawczyk.grzegorz;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import krawczyk.grzegorz.controllers.persistence.PersistenceAccess;
import krawczyk.grzegorz.controllers.persistence.ValidAccount;
import krawczyk.grzegorz.controllers.services.LoginService;
import krawczyk.grzegorz.models.EmailAccount;
import krawczyk.grzegorz.views.ViewFactory;

import java.util.ArrayList;
import java.util.List;

public class Launcher extends Application {

    public static void main(String[] args) {
        // Method launch() is inherited form the class Application.
        // It calls start() method.
        launch(args);
    }

    private PersistenceAccess persistenceAccess = new PersistenceAccess();
    private EmailManager emailManager = new EmailManager();

    /* Method start() is called by launch() method in main().
    start() is responsible for displaying window on screen.
    It is inherited from the class Application.
    It is used to get valid accounts from local file and based on that display login window or already main window
    (if there are valid accounts persisted in local file)
    */
    @Override
    public void start(Stage stage) throws Exception {
        ViewFactory viewFactory = new ViewFactory(emailManager);
        List<ValidAccount> validAccountsList = this.persistenceAccess.loadFromPersistence();
        if (validAccountsList.size() > 0) {
            viewFactory.showMainWindow();
            for (ValidAccount validAccount: validAccountsList) {
                EmailAccount emailAccount = new EmailAccount(validAccount.getEmailAddress(), validAccount.getPassword());
                LoginService loginService = new LoginService(emailAccount, this.emailManager);
                loginService.start();
            }
        } else {
            viewFactory.showLoginWindow();
        }
    }

    /* Method stop() is called when application (window) is closed.
    It is used to save email accounts to which user logged in in the application to the local file for persistance.
     */
    @Override
    public void stop() throws Exception {
        List<ValidAccount> validAccountsList = new ArrayList<>();
        for (EmailAccount emailAccount: this.emailManager.getEmailAccounts()) {
            validAccountsList.add(new ValidAccount(emailAccount.getAddress(), emailAccount.getPassword()));
        }
        this.persistenceAccess.saveToPersistence(validAccountsList);
    }
}
