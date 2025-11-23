package krawczyk.grzegorz.controllers;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.web.WebView;
import javafx.util.Callback;
import krawczyk.grzegorz.EmailManager;
import krawczyk.grzegorz.controllers.services.MessageRendererService;
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

    // Options under right-click to mark selected message as unread and to delete selected message.
    // They have to be added to Email Table View (they are to be used there) - in setUpEmailsTableView() method.
    // Actions under this options are initialized in initialize() method.
    private MenuItem markMessageUnreadMenuItem = new MenuItem("Mark as unread");
    private MenuItem deleteMessageMenuItem = new MenuItem("Delete the message");

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

    private MessageRendererService messageRendererService;

    /**
     * MainWindowController constructor.
     * <hr></hr>
     * It calls BaseController constructor.
     *
     * @param emailManager - an object of the class EmailManager.
     * @param viewFactory  - an object of the class ViewFactory.
     * @param fxmlName     - a String containing name of a fxml file with the extension.
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
        this.setUpMessageRendererService();
        this.setUpMessageSelection();
        this.setUpContextMenus();
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
     * Method sets (initializes) columns of Emails Table View when displaying the window.
     */
    private void setUpEmailsTableView() {
        senderCol.setCellValueFactory(new PropertyValueFactory<EmailMessage, String>("sender"));
        subjectCol.setCellValueFactory(new PropertyValueFactory<EmailMessage, String>("subject"));
        recipientCal.setCellValueFactory(new PropertyValueFactory<EmailMessage, String>("recipient"));
        sizeCal.setCellValueFactory(new PropertyValueFactory<EmailMessage, SizeInteger>("size"));
        dateCol.setCellValueFactory(new PropertyValueFactory<EmailMessage, Date>("date"));

        // it adds context menu items under right click (properties created on top) to Email Table View:
        emailsTableView.setContextMenu(new ContextMenu(markMessageUnreadMenuItem, deleteMessageMenuItem));
    }

    /**
     * Method initializes event listener listening for mouse click on any element of Email Tree View.
     * When user clicks Email Tree View item populates Emails Table View with email messages from selected folder.
     */
    private void setUpFolderSelection() {
        emailsTreeView.setOnMouseClicked(event -> {
            EmailTreeItem<String> item = (EmailTreeItem<String>) emailsTreeView.getSelectionModel().getSelectedItem();

            if (item != null) {
                this.emailManager.setSelecedFolder(item);
                emailsTableView.setItems(item.getEmailMessages());
            }
        });
    }

    /**
     * Method initializes that rows of Emails Table View containing emails which were not yet red are bold.
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

                        if (item != null) {
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

    /**
     * Method creates new MessageRendererService class object.
     * <hr></hr>
     * It passes webEngine of Email Web View to the created object.
     * This way messageRendererService contains WebEngine object of Email Web View window and uses it to display messages.
     * So displaying messages is controlled in MessageRendererService.
     */
    private void setUpMessageRendererService() {
        this.messageRendererService = new MessageRendererService(this.emailWebView.getEngine());
    }

    /**
     * Method initializes event listener listening for mouse click on any Email Table View row.
     * Email Table View contains EmialMessage object (every row is 1 object).
     * When user clicks Email Table View item:
     * <ol>
     *     <li>
     *         It sets selected EmailMessage object in messageRendererService and restarts it.
     *         So this way every time user clicks Email Table View it starts new background thread which renders email message (content of the message).
     *         Every rendering is new thread.</li>
     *     <li>
     *         It sets this message read.
     *     </li>
     * </ol>
     */
    private void setUpMessageSelection() {
        emailsTableView.setOnMouseClicked(mouseEvent -> {
            EmailMessage emailMessage = emailsTableView.getSelectionModel().getSelectedItem();
            if (emailMessage != null) {
                this.emailManager.setSelectedMessage(emailMessage);
                if (!emailMessage.getWasRead()) {
                    emailManager.setWasRead();
                }
                messageRendererService.setEmailMessage(emailMessage);
                messageRendererService.restart();
            }
        });
    }

    /**
     * Method initializes events triggered by options selected in context menu (under right click).
     * Options for context menus are added as properties on top of the class
     * and then initialized in EmailTableView in setUpEmailsTableView()
     */
    private void setUpContextMenus() {
        markMessageUnreadMenuItem.setOnAction(event -> {
            emailManager.setWasNotRead();
        });

        deleteMessageMenuItem.setOnAction(event -> {
            emailManager.deleteSelectedMessage();
            // it clears Email Web View:
            emailWebView.getEngine().loadContent("");
        });
    }
}
