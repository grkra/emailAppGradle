package krawczyk.grzegorz.controllers;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.TreeView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.web.WebView;
import javafx.util.Callback;
import krawczyk.grzegorz.EmailManager;
import krawczyk.grzegorz.models.EmailMessage;
import krawczyk.grzegorz.models.EmailTreeItem;
import krawczyk.grzegorz.models.SizeInteger;
import krawczyk.grzegorz.views.ViewFactory;

import java.net.URL;
import java.util.Date;
import java.util.ResourceBundle;

/**
 * Controller of Main window of the app.
 */
public class MainWindowController extends BaseController implements Initializable {

    @FXML
    private WebView emailWebView;

    @FXML
    private TableView<EmailMessage> emailsTableView;

    @FXML
    private TreeView<String> emailsTreeView;

    @FXML
    private TableColumn<EmailMessage, String> senderCol;

    @FXML
    private TableColumn<EmailMessage, String> subjectCol;

    @FXML
    private TableColumn<EmailMessage, String> recipientCal;

    @FXML
    private TableColumn<EmailMessage, SizeInteger> sizeCal;

    @FXML
    private TableColumn<EmailMessage, Date> dateCol;

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
        this.setUpEmailsTreeView();
        this.setUpEmailsTableView();
        this.setUpFolderSelection();
        this.setUpBoldRows();
    }

    /**
     * Method sets (initializes) Emails Tree View when displaying the window.
     * Method adds Root to the Email Tree View.
     * <hr></hr>
     * Root is populated with email addresses and their folders in EmailManager class.
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

    /**
     * Method sets (initializes) Emails Table View when displaying the window.
     */
    private void setUpEmailsTableView() {
        senderCol.setCellValueFactory(new PropertyValueFactory<EmailMessage, String>("sender"));
        subjectCol.setCellValueFactory(new PropertyValueFactory<EmailMessage, String>("subject"));
        recipientCal.setCellValueFactory(new PropertyValueFactory<EmailMessage, String>("recipient"));
        sizeCal.setCellValueFactory(new PropertyValueFactory<EmailMessage, SizeInteger>("size"));
        dateCol.setCellValueFactory(new PropertyValueFactory<EmailMessage, Date>("date"));
    }

    /**
     * Method populates Emails Table View with email messages from folder which is selected in Emails Tree View.
     * Method contains event listener which reacts on selection of folder in Emails Tree View.
     */
    private void setUpFolderSelection() {
        emailsTreeView.setOnMouseClicked(event->{
            EmailTreeItem<String> item = (EmailTreeItem<String>) emailsTreeView.getSelectionModel().getSelectedItem();

            if (item != null) {
                emailsTableView.setItems(item.getEmailMessages());
            }
        });
    }

    /**
     * Method initializes that rows Emails Table View containing emails which were not yet red are bold.
     */
    private void setUpBoldRows() {

        // Method requires callback
        emailsTableView.setRowFactory(new Callback<TableView<EmailMessage>, TableRow<EmailMessage>>() {
            @Override
            public TableRow<EmailMessage> call(TableView<EmailMessage> emailMessageTableView) {
                return new TableRow<EmailMessage>() {
                    @Override
                    protected void updateItem(EmailMessage item, boolean empty) {
                        super.updateItem(item, empty);

                        if(item != null) {
                            if (item.getWasRead()) {
                                setStyle("");
                            } else {
                                setStyle("-fx-font-weight: bold");
                            }
                        }
                    }
                };
            }
        });
    }
}
