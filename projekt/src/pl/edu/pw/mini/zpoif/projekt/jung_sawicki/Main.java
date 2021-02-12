package pl.edu.pw.mini.zpoif.projekt.jung_sawicki;

import pl.edu.pw.mini.zpoif.projekt.jung_sawicki.notifications.User;

import java.util.HashMap;

public class Main {
    private static HashMap<String, User> users;
    public static void main(String[] args) {
        users = User.uploadUsersFromFile();
    }
}
