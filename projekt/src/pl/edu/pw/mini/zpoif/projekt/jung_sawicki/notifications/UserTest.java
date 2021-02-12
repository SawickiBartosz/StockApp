package pl.edu.pw.mini.zpoif.projekt.jung_sawicki.notifications;

import org.junit.jupiter.api.Test;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.assertEquals;

class UserTest {

    @Test
    void saveUser() {
        Path testDirPath = Paths.get("./test_data/users");
        User user = new User("user", "*", "*");
        user.saveUser(testDirPath);
        user = User.uploadUsersFromFile(testDirPath).get("user");
        assertEquals("*", user.getEmail());
        for (File file : testDirPath.toFile().listFiles())
            if (!file.isDirectory())
                file.delete();
    }
}