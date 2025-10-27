package krawczyk.grzegorz.controllers.services;

import javafx.concurrent.Service;
import javafx.concurrent.Task;
import krawczyk.grzegorz.models.EmailTreeItem;

import javax.mail.Folder;
import javax.mail.MessagingException;
import javax.mail.Store;

/**
 * Controller responsible for fetching folders from email account.
 * <hr></hr>
 * It extends Service class which is used to perform tasks on background Threads.
 * Service class is part of JavaFX and it makes easier to manage multithreading code.
 * <hr></hr>
 * Service won't return anything (it returns Void object).
 */
public class FetchFolderService extends Service<Void> {

    private Store store;
    private EmailTreeItem<String> foldersRoot;

    /**
     * Constructor of FetchFolderService class.
     * @param store - object of class Store
     * @param foldersRoot - root folder to which folders are added by this class.
     */
    public FetchFolderService(Store store, EmailTreeItem<String> foldersRoot) {
        this.store = store;
        this.foldersRoot = foldersRoot;
    }

    // In EmialManager there is fetchFolderService.start() method called.
    // start() method calls createTask() in which all background task code is.
    @Override
    protected Task<Void> createTask() {
        return new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                fetchFolders();
                return null;
            }
        };
    }

    /**
     * Method adds folders from store to the root folder.
     * @throws MessagingException
     */
    private void fetchFolders() throws MessagingException {

        // gets all folders from the store and add to array
        Folder[] folders = store.getDefaultFolder().list();
        handleFolders(folders, foldersRoot);
    }

    /**
     * Method creates EmailTreeItem from every folder passed in the array, and adds it to passed parent EmailTreeItem root folder as a child.
     * @param folders
     * @param foldersRoot
     * @throws MessagingException
     */
    private void handleFolders (Folder[] folders, EmailTreeItem<String> foldersRoot) throws MessagingException {
        // For every folder in folders array:
        for(Folder folder: folders) {
            // It creates new EmailTrreItem from the folder
            EmailTreeItem<String > emailTreeItem = new EmailTreeItem<>(folder.getName());

            // It adds folder to the root folder
            foldersRoot.getChildren().add(emailTreeItem);

            // EmailAddres TreeItem is set to be expanded.
            // If TreeView element has children (next subfolders) they will be visible by default.
            foldersRoot.setExpanded(true);

            // If current folder has subfolders:
            if (folder.getType() == Folder.HOLDS_FOLDERS || folder.getType() == Folder.HOLDS_FOLDERS + Folder.HOLDS_MESSAGES) {

                // It creates another array of these subfolders
                Folder[] subfolders = folder.list();


                // And calls again for the same method (recursion),
                // but this time passes array of subfolders and new emailTreeItem (folder) as root.
                handleFolders(subfolders, emailTreeItem);
            }
        }
    }
}
