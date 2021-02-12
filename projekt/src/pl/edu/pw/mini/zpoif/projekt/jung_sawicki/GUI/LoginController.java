package pl.edu.pw.mini.zpoif.projekt.jung_sawicki.GUI;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import pl.edu.pw.mini.zpoif.projekt.jung_sawicki.GUI.exceptions.AppFilesCorruptedException;
import pl.edu.pw.mini.zpoif.projekt.jung_sawicki.GUI.exceptions.JavaFXException;
import pl.edu.pw.mini.zpoif.projekt.jung_sawicki.GUI.exceptions.NoSuchNickException;
import pl.edu.pw.mini.zpoif.projekt.jung_sawicki.notifications.User;

import java.io.IOException;
import java.util.HashMap;

public class LoginController {


    @FXML
    private TextField userName;
    @FXML
    private Button newUserButton;
    @FXML
    private Button submitButton;

    private NewUserScene newUserScene;
    private PlotScene plotScene;

    public void initialize() {
        newUserScene = new NewUserScene();
        plotScene = new PlotScene();
    }

    @FXML
    public void submit(ActionEvent event) {
        String user_str = userName.getText();
        HashMap<String, User> users = StockApp.getUsers();

        if (!users.containsKey(user_str)) {
            try {
                throw new NoSuchNickException("Nie ma takiej nazwy użytkownika w bazie danych");
            } catch (NoSuchNickException e) {
                e.showErrorDialog();
            }
            return;
        }
        User user = users.get(user_str);
        StockApp.setCurrentUser(user);
        Stage stage = (Stage) submitButton.getScene().getWindow();
        try {
            plotScene.start(stage);
        } catch (IOException e) {
            e.printStackTrace();
            JavaFXException exception = new AppFilesCorruptedException(e.getMessage());
            exception.showErrorDialog();
        }
    }

    @FXML
    public void newUser(ActionEvent actionEvent) {
        Stage stage = (Stage) newUserButton.getScene().getWindow();
        try {
            NewUserScene.start(stage);
        } catch (IOException e) {
            e.printStackTrace();
            JavaFXException exception = new AppFilesCorruptedException(e.getMessage());
            exception.showErrorDialog();
        }

    }
}