package krawczyk.grzegorz.controllers.services;

import javafx.concurrent.Service;
import javafx.concurrent.Task;

import javax.mail.Folder;
import java.util.List;

/**
 * Controller responsible for connecting with email server and updating data about new and deleted emails.
 * <hr></hr>
 * It is meant to start when the application starts and work all the time.
 * It will always call and serach for new message added or message removed events on the connected servers.
 */
public class FolderUpdaterService extends Service {

    /**
     * List of all Folders in the application.
     * Folders are folders in email Store (from server side)
     */
    private List<Folder> folderList;

    /**
     * constructor of class FolderUpdaterService.
     * <hr></hr>
     * It is created in EmailManager constructor.
     *
     * @param folderList - list of all active folders in the application.
     *                    Folders are folders in email Store (from provider side).
     */
    public FolderUpdaterService(List<Folder> folderList) {
        this.folderList = folderList;
    }

    @Override
    protected Task createTask() {
        return new Task() {
            @Override
            protected Object call() throws Exception {
                // forever (in infinite loop) - this thread works all the time, as long as the application works.
                for (; ; ) {
                    try {
                        // it sleeps 5 seconds
                        Thread.sleep(5000);

                        // every 5 seconds for every folder in the application
                        for (Folder folder : folderList) {
                            if (folder.getType() != Folder.HOLDS_FOLDERS && folder.isOpen()) {
                                // it checks number of messages in folder on server
                                folder.getMessageCount();
                            }
                        }
                    } catch (Exception e) {
                        System.out.println(e.getStackTrace());
                    }
                }
            }
        };
    }
}
