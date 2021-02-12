package pl.edu.pw.mini.zpoif.projekt.jung_sawicki.GUI.exceptions;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;

public class JavaFXException extends Exception{
    public JavaFXException(String message) {
        super(message);
    }

    public void showErrorDialog() {
        String errorMsg = super.getMessage();
        Stage dialog = new Stage();
        dialog.initModality(Modality.APPLICATION_MODAL);
        FXMLLoader loader = new FXMLLoader(ErrorController.class.getResource("../FXML/Error.fxml"));
        try {
            Parent root = loader.load();
            dialog.setScene(new Scene(root));
            dialog.show();
            ((ErrorController)loader.getController()).setErrorText(errorMsg);
        } catch (IOException exc) {
            exc.printStackTrace();
        }
    }
}
