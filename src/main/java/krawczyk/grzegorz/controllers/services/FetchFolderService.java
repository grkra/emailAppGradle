package krawczyk.grzegorz.controllers.services;

import javafx.concurrent.Service;
import javafx.concurrent.Task;
import krawczyk.grzegorz.models.EmailTreeItem;
import krawczyk.grzegorz.views.IconResolver;

import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Store;
import javax.mail.event.MessageCountEvent;
import javax.mail.event.MessageCountListener;
import java.util.ArrayList;
import java.util.List;

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
    private IconResolver iconResolver = new IconResolver();

    /**
     * List of all folders in the application (objects of class Folder - folders from server side)
     */
    private List<Folder> folderList;

    /**
     * Constructor of FetchFolderService class.
     * <hr></hr>
     * It is created in EmailManager.addEmailAccount() method.
     * @param store       - object of class Store. It is field of EmailAccount class (object is created when login in)
     * @param foldersRoot - root folder to which folders are added by this class - email address folder.
     *                    It is object of class EmailTreeItem - folder from application end, folder in Email Tree View in main window of the application.
     * @param folderList - list of all folders in the application - objects of class Folder (folders from server end)
     */
    public FetchFolderService(Store store, EmailTreeItem<String> foldersRoot, List<Folder> folderList) {
        this.store = store;
        this.foldersRoot = foldersRoot;
        this.folderList = folderList;
    }

    // In EmailManager there is fetchFolderService.start() method called.
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
     *
     * @throws MessagingException
     */
    private void fetchFolders() throws MessagingException {

        // gets all folders from the store and add to array
        Folder[] folders = store.getDefaultFolder().list();
        handleFolders(folders, foldersRoot);
    }

    /**
     * Method creates EmailTreeItem from every folder passed in the array, and adds it to passed parent EmailTreeItem root folder as a child.
     * <hr></hr>
     * Method is used to populate root folder of Email Tree View in main window of the application.
     * Method is triggered in EmailManager class in addEmailAccount().
     * Then in MainWindowController root folder (containing all subfolders added here) is added to Email Tree View.
     *
     * @param folders     - array of objects of the class Folder - folders in Store to be added to the foldersRoot.
     * @param foldersRoot - object of the class EmailTreeItem - folder in TreeView menu in the application - to which it should add subfolders
     * @throws MessagingException
     */
    private void handleFolders(Folder[] folders, EmailTreeItem<String> foldersRoot) throws MessagingException {
        // For every folder in folders array:
        for (Folder folder : folders) {
            // it adds current folder to folderList - populate folderList
            this.folderList.add(folder);

            // It creates new EmailTreeItem from the folder
            EmailTreeItem<String> emailTreeItem = new EmailTreeItem<>(folder.getName());

            // It adds picture (icon) to the folder name
            emailTreeItem.setGraphic(iconResolver.getIconForFolder(folder.getName()));

            // It adds folder to the root folder (emailAddress folder)
            foldersRoot.getChildren().add(emailTreeItem);
            // EmailAddres TreeItem is set to be expanded.
            // If TreeView element has children (next subfolders) they will be visible by default.
            foldersRoot.setExpanded(true);

            fetchMessagesFromFolder(folder, emailTreeItem);

            addMessageListenerToFolder(folder, emailTreeItem);

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
     * Method adds emails from folder to emailTreeItem.
     * <hr></hr>
     * For each folder method creates new separate Service.
     *
     * @param folder        - object of the class Folder - folder in Store from which email messages are taken
     * @param emailTreeItem - object of the class EmailTreeItem - folder in TreeView menu in the application to which email messages are added
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
                                emailTreeItem.addEmail(folder.getMessage(i));
                            }
                        }
                        return null;
                    }
                };
            }
        };

        fetchMessagesService.start();
    }

    /**
     *
     * @param folder - object of class Folder - folder in Store from which email messages are taken
     * @param emailTreeItem - object of the class EmailTreeItem - folder in TreeView menu in the application to which email messages are added
     */
    private void addMessageListenerToFolder(Folder folder, EmailTreeItem<String> emailTreeItem) {

        // it adds listener to the folder (which is in Store)
        folder.addMessageCountListener(new MessageCountListener() {
            @Override
            public void messagesAdded(MessageCountEvent e) {
                for (int i = 0; i < e.getMessages().length; i++) {
                    try {
                        Message message = folder.getMessage(folder.getMessageCount()-i);
                        emailTreeItem.addEmailToTop(message);
                    } catch (MessagingException ex) {
                        throw new RuntimeException(ex);
                    }
                }
            }

            @Override
            public void messagesRemoved(MessageCountEvent e) {
                System.out.println("message deleted event: " + e);
            }
        });
    }
}
