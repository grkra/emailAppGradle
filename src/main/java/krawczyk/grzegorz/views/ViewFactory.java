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
import java.util.ArrayList;
import java.util.List;

public class ViewFactory {

    private EmailManager emailManager;

    /**
     * List contains active stages of the application (open windows of the application).
     * When new window is opened - it is added to the list.
     * When window is closed - it is removed from the list.
     * <hr></hr>
     * List is used so when style of graphical interface is changed (in Options Window)
     * change can be applied to all stages saved in the list (all open windows). It is used in updateStyles() method.
     */
    private ArrayList<Stage> activeStages;

    // View options
    /**
     * Holds color theme of the graphical interface. It can be equal to values specified in Enum.
     * <hr></hr>
     * Color theme can be changed in Options Window of the application,
     * and then it is applied to all open windows (Stages saved to activeStages list).
     */
    private ColorTheme colorTheme = ColorTheme.DEFAULT;

    /**
     * Holds font size to be used in the graphical interface. It can be equal to values specified in Enum.
     * <hr></hr>
     * Font size can be changed in Options Window of the application,
     * and then it is applied to all open windows (Stages saved to activeStages list).
     */
    private FontSize fontSize = FontSize.MEDIUM;

    public ViewFactory(EmailManager emailManager) {
        this.emailManager = emailManager;
        this.activeStages = new ArrayList<>();
    }

    /**
     * Method displays login window of the application.
     */
    public void showLoginWindow() {
        System.out.println("Showing login window.");

        BaseController controller = new LoginWindowController(emailManager, this, "LoginWindow.fxml");
        initializeStage(controller);
    }

    /**
     * Method displays main window of the application.
     */
    public void showMainWindow() {
        System.out.println("Showing main window.");

        BaseController controller = new MainWindowController(emailManager, this, "MainWindow.fxml");
        initializeStage(controller);
    }

    /**
     * Method displays options window of the application.
     */
    public void showOptionsWindow() {
        System.out.println("Showing options window.");

        BaseController controller = new OptionsWindowController(emailManager, this, "OptionsWindow.fxml");
        initializeStage(controller);
    }

    /**
     * Method update CSS stylesheet used to display graphical interface of the application.
     * <hr></hr>
     * Method iterates through all opened windows of the application (all Stages saved in updateStyles list),
     * and updates css stylesheets used in them (clears old ones, and adds new ones).
     */
    public void updateStyles() {
        // In JavaFX CSS files are applied to Scenes.
        // Change is done for all windows opened at the time (all Stages saved in activeStages list):
        for (Stage stage : activeStages) {

            // Scene is got from a Stage:
            Scene scene = stage.getScene();

            // getStylesheets() method gets list of URLs linking to stylesheets,
            // and then clear() method deletes these URLs - clears list:
            scene.getStylesheets().clear();

            // Based on value saved in colorTheme css styleSheet is added to the scene
            scene.getStylesheets().add(getClass().getResource(ColorTheme.getCssPath(colorTheme)).toExternalForm());

            // Based on value saved in fontSize css styleSheet is added to the scene
            scene.getStylesheets().add(getClass().getResource(FontSize.getCssPath(fontSize)).toExternalForm());
        }
    }

    /**
     * Method closes passed window of the application.
     * <hr></hr>
     * Method closes the window and removes the Stage (window) from list of active windows.
     * @param stageToClose - Stage (window) to be closed.
     */
    public void closeStage(Stage stageToClose) {
        stageToClose.close();

        // it removes the Stage from the list of activeStages
        activeStages.remove(stageToClose);
    }

    /**
     * Method initializes and opens window (Stage) of the graphical interface of the application.
     * Which window is being initialized is set based on passed Controller.
     * <hr></hr>
     * Method loads right fxml file to be displayed to a Parent class object. Path to the fxml file depends on passed controller.
     * Then method creates Scene with loaded Parent, sets Scene and displays Stage (window).
     * At the end method adds Stage to activeStages list (list of opened windows of the application).
     * @param controller - object of a class extending BaseController.
     */
    private void initializeStage(BaseController controller) {
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

        // Stage (window) is added to list of active stages (open windows)
        activeStages.add(stage);
    }

    public ColorTheme getColorTheme() {
        return colorTheme;
    }

    public void setColorTheme(ColorTheme colorTheme) {
        this.colorTheme = colorTheme;
    }

    public FontSize getFontSize() {
        return fontSize;
    }

    public void setFontSize(FontSize fontSize) {
        this.fontSize = fontSize;
    }
}
