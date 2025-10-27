package krawczyk.grzegorz.controllers;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableView;
import javafx.scene.control.TreeView;
import javafx.scene.web.WebView;
import krawczyk.grzegorz.EmailManager;
import krawczyk.grzegorz.views.ViewFactory;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Controller of Main window of the app.
 */
public class MainWindowController extends BaseController implements Initializable {

    @FXML
    private WebView emailWebView;

    @FXML
    private TableView<?> emailsTableView;

    @FXML
    private TreeView<String> emailsTreeView;

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

    // Method implemented from Initializable interface.
    // It lets to initialize values of the fields exactly after initialization of an object,
    // so they are already initialized when window is dislayed.
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setUpEmailsTreeView();
    }

    /**
     * Method sets (initializes) Emails Tree View element when displaying the window.
     */
    private void setUpEmailsTreeView() {
        // It sets EmailTreeItem foldersRoot from EmailManager class as root inside TreeView.
        // Everything inside the root (all children) are automatically added with the root to the TreeView.
        this.emailsTreeView.setRoot(emailManager.getFoldersRoot());

        // Root is normal TreeItem - just sent as root. It has no name, so it wouldn't be visible.
        // Only its children (nest TreeItems) should be visible.
        // So TreeView is set to not display root element.
        this.emailsTreeView.setShowRoot(false);
    }
}
