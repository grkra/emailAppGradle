package krawczyk.grzegorz.views;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import krawczyk.grzegorz.EmailManager;
import krawczyk.grzegorz.controllers.BaseController;
import krawczyk.grzegorz.controllers.LoginWindowController;
import krawczyk.grzegorz.controllers.MainWindowController;
import krawczyk.grzegorz.controllers.OptionsWindowController;

import java.io.IOException;

public class ViewFactory {

    private EmailManager emailManager;

    public ViewFactory(EmailManager emailManager) {
        this.emailManager = emailManager;
    }

    public void showLoginWindow() {
        System.out.println("Showing login window.");

        BaseController controller = new LoginWindowController(emailManager, this, "LoginWindow.fxml");
        initializeStage(controller);
    }

    public void showMainWindow () {
        System.out.println("Showing main window.");

        BaseController controller = new MainWindowController(emailManager, this, "MainWindow.fxml");
        initializeStage(controller);
    }

    public void showOptionsWindow () {
        System.out.println("Showing options window.");

        BaseController controller = new OptionsWindowController(emailManager, this, "OptionsWindow.fxml");
        initializeStage(controller);
    }

    public void closeStage (Stage stageToClose) {
        stageToClose.close();
    }

    private void initializeStage (BaseController controller) {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml_files/" + controller.getFxmlName()));
        fxmlLoader.setController(controller);

        Parent parent;
        try {
            parent = fxmlLoader.load();
        } catch (IOException e) {
            return;
        }

        Scene scene = new Scene(parent);
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.show();
    }
}
