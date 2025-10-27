package krawczyk.grzegorz.models;

import javafx.scene.control.TreeItem;

/**
 * Class represents email address (main folder) to be displayed in TreeView in Main Window of the application.
 * <hr></hr>
 * Class extends TreeItem class.
 * @param <String>
 */
public class EmailTreeItem<String> extends TreeItem<String> {
    private String name;

    /**
     * Constructor of the class EmailTreeItem.
     * @param name - String containing email address to be displayed in TreeView menu in Main Window of the application
     */
    public EmailTreeItem(String name) {
        super(name);
        this.name = name;
    }
}
