package pl.edu.pw.mini.zpoif.projekt.jung_sawicki.GUI;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import pl.edu.pw.mini.zpoif.projekt.jung_sawicki.GUI.exceptions.AppFilesCorruptedException;
import pl.edu.pw.mini.zpoif.projekt.jung_sawicki.GUI.exceptions.JavaFXException;
import pl.edu.pw.mini.zpoif.projekt.jung_sawicki.GUI.exceptions.UserNickExistsException;
import pl.edu.pw.mini.zpoif.projekt.jung_sawicki.notifications.User;

import java.io.IOException;

public class NewUserController {
    @FXML
    private Button createNewUserButton;
    @FXML
    private TextField email;
    @FXML
    private TextField nick;
    @FXML
    private TextField key;
    @FXML
    private Button backToLoginButton;

    private LoginScene loginScene;
    private PlotScene plotScene;

    public void initialize() {
        this.loginScene = new LoginScene();
        this.plotScene = new PlotScene();
    }

    @FXML
    public void createNewUSer(ActionEvent actionEvent) {
        User user = new User(nick.getText(), email.getText(), key.getText());

        if(StockApp.getUsers().containsKey(user.getNick())){
            JavaFXException exception = new UserNickExistsException("Użytkownik o podanej nazwie już istnieje");
            exception.showErrorDialog();
            return;
        }
        StockApp.getUsers().put(user.getNick(),user);
        System.out.println("Liczba uzytkownikow: " + StockApp.getUsers().size());
        StockApp.setCurrentUser(user);
        Stage stage = (Stage) createNewUserButton.getScene().getWindow();
        try {
            plotScene.start(stage);
        }catch (IOException e){
            JavaFXException exception = new AppFilesCorruptedException(e.getMessage());
            exception.showErrorDialog();
            e.printStackTrace();
        }

    }


    public void backToLogin(ActionEvent actionEvent) {
        Stage stage = (Stage) backToLoginButton.getScene().getWindow();
        try {
            LoginScene.start(stage);
        } catch (IOException e) {
            e.printStackTrace();
            JavaFXException exception = new AppFilesCorruptedException(e.getMessage());
            exception.showErrorDialog();
        }
    }
}
