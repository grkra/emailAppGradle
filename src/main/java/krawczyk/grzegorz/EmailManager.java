package krawczyk.grzegorz;

import javafx.scene.control.TreeItem;
import krawczyk.grzegorz.controllers.services.FetchFolderService;
import krawczyk.grzegorz.models.EmailAccount;
import krawczyk.grzegorz.models.EmailTreeItem;

/**
 * Class holds information about application state and data.
 */
public class EmailManager {

    // Folder handling
    // TreeView element (used in Main Window) consists of TreeItems.
    // This TreeItem is root for TreeView, starting point to which all other elements are added.
    // In MainWindowController it is set as root to TreeView.
    // Root shouldn't be visible, it is only parent to next visible elements. It is set to be invisible in MainWindowCOntroller.
    private EmailTreeItem<String> foldersRoot = new EmailTreeItem<>("");

    /**
     * Method adds Email Account as child to EmailTreeView root.
     * It is used to populate EmailTreeView in Main Window of the application.
     * Every Email Account added with addEmailAccount() method is displayed in EmailTreeView.
     * <hr></hr>
     * Method is called in login() method in LoginService class to fetch data to EmailTreeView.
     * @param emailAccount - object of class EmailAccount
     */
    public void addEmailAccount(EmailAccount emailAccount) {
        // EmailTree Element with Email Account is created.
        EmailTreeItem<String> treeItem = new EmailTreeItem<>(emailAccount.getAddress());

        // Create Service.
        // Whole fetching folders is done in background thread.
        // treeItem (email account) is passed to fetchFolderService,
        // and then method in FetchFolderClass adds folders to that treeItem
        FetchFolderService fetchFolderService = new FetchFolderService(emailAccount.getStore(), treeItem);

        // Start Service.
        // start() method from Service class does background task (fetch folders) in separate thread.
        // start() method creates new thread, executes code, returns values - it is simpler than creating threads manually.
        fetchFolderService.start();

        // Email Addres EmailTreeItem is added to the foldersRoot - root of the TreeView.
        // It already contains all children folders fetch by fetchFolderService.
        foldersRoot.getChildren().add(treeItem);
    }

    public EmailTreeItem<String> getFoldersRoot() {
        return foldersRoot;
    }
}
