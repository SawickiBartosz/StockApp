package pl.edu.pw.mini.zpoif.projekt.jung_sawicki.GUI;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class NewUserScene {
    public NewUserScene() {
    }

    public static void start(Stage stage) throws IOException {
        Parent root = FXMLLoader.load(NewUserScene.class.getResource("FXML/NewUserScene.fxml"));
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
}
