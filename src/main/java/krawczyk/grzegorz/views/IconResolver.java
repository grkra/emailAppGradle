package krawczyk.grzegorz.views;

import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

/**
 * Class is used to upload image (icon).
 */
public class IconResolver {

    public Node getIconForFolder (String folderName) {
        String lowerCaseFolderName = folderName.toLowerCase();
        ImageView imageView;

        try {
            if (lowerCaseFolderName.contains("@")) {
//                imageView = new ImageView(new Image(getClass().getResourceAsStream("icons/email.png")));
                imageView = new ImageView(new Image(("icons/email.png")));
            } else if (lowerCaseFolderName.contains("inbox")){
                imageView = new ImageView(new Image(("icons/inbox.png")));
            } else if (lowerCaseFolderName.contains("sent")) {
                imageView = new ImageView(new Image(("icons/sent2.png")));
            } else if (lowerCaseFolderName.contains("spam")) {
                imageView = new ImageView(new Image(("icons/spam.png")));
            } else {
                imageView = new ImageView(new Image(("icons/folder.png")));
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

        imageView.setFitWidth(16);
        imageView.setFitHeight(16);
        return imageView;
    }
}
