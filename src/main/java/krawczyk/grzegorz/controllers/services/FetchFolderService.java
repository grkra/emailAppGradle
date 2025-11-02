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
     * @param folders - array of objects of the class Folder - subfolders to be added to the foldersRoot.
     * @param foldersRoot - object of the class EmailTreeItem - folder in TreeView menu in the application - to which it should add subfolders
     * @throws MessagingException
     */
    private void handleFolders (Folder[] folders, EmailTreeItem<String> foldersRoot) throws MessagingException {
        // For every folder in folders array:
        for(Folder folder: folders) {
            // It creates new EmailTrreItem from the folder
            EmailTreeItem<String > emailTreeItem = new EmailTreeItem<>(folder.getName());
            // It adds folder to the root folder (emailAddress folder)
            foldersRoot.getChildren().add(emailTreeItem);
            // EmailAddres TreeItem is set to be expanded.
            // If TreeView element has children (next subfolders) they will be visible by default.
            foldersRoot.setExpanded(true);

            fetchMessagesFromFolder(folder, emailTreeItem);

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

    /**
     * Method adds emails from folder to emailTrrItem.
     * <hr></hr>
     * For each folder method creates new separate Service.
     * @param folder - object of the class Folder - folder fetch from email account (current folder)
     * @param emailTreeItem - object of the class EmailTreeItem - folder in TreeView menu in the application
     */
    private void fetchMessagesFromFolder(Folder folder, EmailTreeItem<String> emailTreeItem) {

        // Below fetchMessagesService object definition there in fetchMessagesService.start().
        // start() method calls createTask() in which all background task code is.
        Service fetchMessagesService = new Service() {

            @Override
            protected Task createTask() {
                return new Task() {
                    @Override
                    protected Object call() throws Exception {
                        // If current folder has no subfolders:
                        if (folder.getType() != Folder.HOLDS_FOLDERS) {
                            folder.open(Folder.READ_WRITE);
                            int folderSize = folder.getMessageCount();
                            for (int i = folderSize; i > 0; i--) {
                                System.out.println(folder.getMessage(i).getSubject());
                            }
                        }
                        return null;
                    }
                };
            }
        };

        fetchMessagesService.start();
    }
}
