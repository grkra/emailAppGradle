package krawczyk.grzegorz;

import javafx.scene.control.TreeItem;
import krawczyk.grzegorz.controllers.services.FetchFolderService;
import krawczyk.grzegorz.controllers.services.FolderUpdaterService;
import krawczyk.grzegorz.models.EmailAccount;
import krawczyk.grzegorz.models.EmailTreeItem;

import javax.mail.Folder;
import java.util.ArrayList;
import java.util.List;

/**
 * Class holds information about application state and data.
 */
public class EmailManager {

    // Folder handling
    // Email Tree View (used in Main Window) consists of TreeItems.
    // This TreeItem is root for Email Tree View - starting point to which all other elements are added.
    // In MainWindowController it is set as root to Email Tree View (in setUpEmailsTreeView() method).
    // Root shouldn't be visible, it is only parent to next visible elements. It is set to be invisible in MainWindowController.
    private EmailTreeItem<String> foldersRoot = new EmailTreeItem<>("");

    /**
     * List of all folders in the application (object of class Folder - folders from server side)
     */
    private List<Folder> folderList = new ArrayList<>();
    private FolderUpdaterService folderUpdaterService;

    /**
     * Constructor of EmailManager class.
     * It initializes FolderUpdaterService service which will constantly check and update new and deleted emails on server.
     */
    public EmailManager() {
        // Create Service.
        // FolderUpdaterService will work in background thread all the time.
        this.folderUpdaterService = new FolderUpdaterService(this.folderList);

        // Start Service.
        // start() method from Service class does background task
        // (call and serach for new message added or message removed events on the connected servers every 5 seconds ) in separate thread.
        // start() method creates new thread, executes code, returns values - it is simpler than creating threads manually.
        folderUpdaterService.start();
    }

    /**
     * Method adds Email Account as child to Email Tree View root folder (foldersRoot property).
     * It is used to populate EmailTreeView in Main Window of the application.
     * Every Email Account added with addEmailAccount() method is displayed in EmailTreeView.
     * <hr></hr>
     * Method is called in login() method in LoginService class to fetch data to EmailTreeView.
     *
     * @param emailAccount - object of class EmailAccount
     */
    public void addEmailAccount(EmailAccount emailAccount) {
        // EmailTree Element with Email Account is created.
        EmailTreeItem<String> treeItem = new EmailTreeItem<>(emailAccount.getAddress());

        // Create Service.
        // Whole fetching folders is done in background thread.
        // treeItem (email account) is passed to fetchFolderService,
        // and then method in FetchFolderClass adds folders to that treeItem
        FetchFolderService fetchFolderService = new FetchFolderService(emailAccount.getStore(), treeItem, this.folderList);

        // Start Service.
        // start() method from Service class does background task (fetch folders) in separate thread.
        // start() method creates new thread, executes code, returns values - it is simpler than creating threads manually.
        fetchFolderService.start();

        // Email Addres EmailTreeItem is added to the foldersRoot - root of the TreeView.
        // It already contains all children folders fetch by fetchFolderService.
        foldersRoot.getChildren().add(treeItem);
    }

    /**
     * Method returns folder root of Email Tree List which is in main window of the application.
     * Folder root contains all logged-in email accounts, their folders (EmailTreeItems - folders in Email Tree View) and emails.
     *
     * @return
     */
    public EmailTreeItem<String> getFoldersRoot() {
        return foldersRoot;
    }

    /**
     * Method returns list of all folders (object of class Folder - folders from server side) in the application.
     *
     * @return List<Folder> - list of all folders in the logged in email accounts.
     */
    public List<Folder> getFolderList() {
        return this.folderList;
    }
}
