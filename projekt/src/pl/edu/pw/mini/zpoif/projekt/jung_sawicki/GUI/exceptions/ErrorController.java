package pl.edu.pw.mini.zpoif.projekt.jung_sawicki.GUI.exceptions;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.stage.Stage;

public class ErrorController {
    @FXML
    private Label errorMessage;

    public void setErrorText(String text) {
        errorMessage.setText(text);
        ((Stage) errorMessage.getScene().getWindow()).setTitle("ERROR");
    }

    @FXML
    private void close() {
        errorMessage.getScene().getWindow().hide();
    }
}
