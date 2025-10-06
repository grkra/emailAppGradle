package krawczyk.grzegorz;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public class Launcher extends Application {

    public static void main (String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
//        Button parent = new Button("AAA");
        Parent parent = FXMLLoader.load(getClass().getResource("/fxml_files/LoginWindow.fxml"));

        Scene scene = new Scene(parent);

        stage.setScene(scene);
        stage.show();
    }
}
