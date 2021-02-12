package pl.edu.pw.mini.zpoif.projekt.jung_sawicki.GUI;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import pl.edu.pw.mini.zpoif.projekt.jung_sawicki.GUI.exceptions.AppFilesCorruptedException;
import pl.edu.pw.mini.zpoif.projekt.jung_sawicki.GUI.exceptions.JavaFXException;
import pl.edu.pw.mini.zpoif.projekt.jung_sawicki.notifications.Notification;
import pl.edu.pw.mini.zpoif.projekt.jung_sawicki.notifications.User;

import java.io.IOException;
import java.util.HashMap;

public class StockApp extends Application {


    private static HashMap<String, User> users;
    private static User currentUser;

    public static HashMap<String, User> getUsers() {
        return users;
    }

    public static User getCurrentUser() {
        return currentUser;
    }

    public static void setCurrentUser(User currentUser) {
        StockApp.currentUser = currentUser;
    }

    public static void main(String[] args) {
        loadLocalData();
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("StockApp");
        Parent root = null;
        try {
            root = FXMLLoader.load(getClass().getResource("FXML/LoginScene.fxml"));
        } catch (IOException e) {
            e.printStackTrace();
            JavaFXException exception = new AppFilesCorruptedException(e.getMessage());
            exception.showErrorDialog();

        }

        assert root != null;
        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    @Override
    public void stop() {
        System.out.println("Stage is closing");
        stopThreads();
        saveLocalFiles();
        System.out.println("Stage closed");
    }

    private void stopThreads() {
        users.values().forEach(user -> {
            user.getNotifications().forEach(Notification::endThread);
        });
    }

    private void saveLocalFiles() {
        User.deleteUsersFiles();
        users.values().forEach(User::saveUser);
    }

    private static void loadLocalData() {
        users = User.uploadUsersFromFile();
    }
}
