package krawczyk.grzegorz.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TableView;
import javafx.scene.control.TreeView;
import javafx.scene.web.WebView;
import javafx.stage.Stage;
import krawczyk.grzegorz.EmailManager;
import krawczyk.grzegorz.views.ViewFactory;

/**
 * Controller of Main window of the app.
 */
public class MainWindowController extends BaseController {

    @FXML
    private WebView emailWebView;

    @FXML
    private TableView<?> emailsTableView;

    @FXML
    private TreeView<?> emailsTreeView;

    /**
     * MainWindowController constructor.
     * <hr></hr>
     * It calls BaseController constructor.
     * @param emailManager - an object of the class EmailManager.
     * @param viewFactory - an object of the class ViewFactory.
     * @param fxmlName - a String containing name of a fxml file with the extension.
     */
    public MainWindowController(EmailManager emailManager, ViewFactory viewFactory, String fxmlName) {
        super(emailManager, viewFactory, fxmlName);
    }

    /**
     * Event listener triggered when Options option is selected in Edit menu.
     */
    @FXML
    void optionsAction() {
        this.viewFactory.showOptionsWindow();
    }

    /**
     * Event listener triggered when Add account option is selected in File menu.
     */
    @FXML
    void addAccountAction() {
        this.viewFactory.showLoginWindow();
    }
}
