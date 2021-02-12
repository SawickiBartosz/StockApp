package pl.edu.pw.mini.zpoif.projekt.jung_sawicki.notifications;

import javafx.scene.chart.XYChart;
import pl.edu.pw.mini.zpoif.projekt.jung_sawicki.ApiOutput.Interday;
import pl.edu.pw.mini.zpoif.projekt.jung_sawicki.ApiOutput.Interval;
import pl.edu.pw.mini.zpoif.projekt.jung_sawicki.ApiOutput.Record;
import pl.edu.pw.mini.zpoif.projekt.jung_sawicki.Downloader;
import pl.edu.pw.mini.zpoif.projekt.jung_sawicki.GUI.exceptions.AppFilesCorruptedException;
import pl.edu.pw.mini.zpoif.projekt.jung_sawicki.GUI.exceptions.JavaFXException;
import pl.edu.pw.mini.zpoif.projekt.jung_sawicki.JsonParser;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class User implements Serializable {

    private String nick;
    private String email;
    private String apiKey;
    private Collection<Notification> notifications = new ArrayList<>();
    private Downloader downloader;
    private HashMap<String,ArrayList<Record>> cachedSeries = new HashMap<>();

    public User(String nick, String email, String apiKey) {
        this.nick = nick;
        this.email = email;
        this.apiKey = apiKey;
        this.downloader = new Downloader(apiKey);
    }

    public void saveUser(Path dirPath) {
        cachedSeries = new HashMap<>();
        checkIfSavingPathExistsAndCreateOne(dirPath);
        Path path = Paths.get(dirPath.toString() + "/" + this.hashCode() + ".ser");
        try {
            FileOutputStream fileOut = new FileOutputStream(path.toString());
            ObjectOutputStream out = new ObjectOutputStream(fileOut);
            out.writeObject(this);
            out.close();
            fileOut.close();
        } catch (
                IOException e) {
            e.printStackTrace();
        }
    }

    public void saveUser() {
        checkIfSavingPathExistsAndCreateOne();
        saveUser(Paths.get("./app_data/users"));
    }

    public static HashMap<String, User> uploadUsersFromFile() {
        checkIfSavingPathExistsAndCreateOne();
        Path dirPath = Paths.get("./app_data/users");
        return uploadUsersFromFile(dirPath);

    }

    public static HashMap<String, User> uploadUsersFromFile(Path dirPath) {
        HashMap<String, User> result = new HashMap<>();
        checkIfSavingPathExistsAndCreateOne(dirPath);
        ArrayList<Path> pathsList = new ArrayList<>();

        try (Stream<Path> paths = Files.walk(dirPath)) {
            pathsList = paths
                    .filter(Files::isRegularFile)
                    .collect(Collectors.toCollection(ArrayList::new));
        } catch (IOException e) {
            e.printStackTrace();
        }
        for (Path path : pathsList) {
            try {
                FileInputStream fileIn = new FileInputStream(String.valueOf(path));
                ObjectInputStream in = new ObjectInputStream(fileIn);
                User u = (User) in.readObject();
                in.close();
                fileIn.close();
                for (Notification notification : u.getNotifications()) {
                    notification.init();
                }
                result.put(u.nick, u);
            } catch (IOException | ClassNotFoundException i) {
                i.printStackTrace();
                JavaFXException exception = new AppFilesCorruptedException(i.getMessage());
                exception.showErrorDialog();
            }
        }
        return result;
    }

    private static void checkIfSavingPathExistsAndCreateOne() {
        Path dirPath = Paths.get("./app_data/users");
        checkIfSavingPathExistsAndCreateOne(dirPath);
    }

    public static void deleteUsersFiles(Path DirPath) {
        for (File file : DirPath.toFile().listFiles())
            if (!file.isDirectory())
                file.delete();
    }

    public static void deleteUsersFiles() {
        Path dirPath = Paths.get("./app_data/users");
        deleteUsersFiles(dirPath);
    }

    private static void checkIfSavingPathExistsAndCreateOne(Path dirPath) {
        File theDir = new File(String.valueOf(dirPath));
        if (!theDir.exists()) {
            theDir.mkdirs();
        }
    }


    public void addNotification(String stock, float trigerringPrice, Notification.Type type) {
        Notification notification = new Notification(stock, trigerringPrice, type, this);
        notifications.add(notification);
    }

    public void addNotification(Notification notification) {
        notifications.add(notification);
    }

    public String getEmail() {
        return email;
    }

    public String getNick() {
        return nick;
    }

    public Collection<Notification> getNotifications() {
        return notifications;
    }

    public void setNotifications(Collection<Notification> notifications) {
        this.notifications = notifications;
    }

    public XYChart.Series<Number, Number> downloadData(String symbol) throws JavaFXException {
        ArrayList<Record> records = JsonParser.parseJSONResponse(
                downloader.download(symbol, new Interday(downloader, symbol, Interval.FIVE)));
        if (records.size() == 0) {
            throw new JavaFXException("Nie udało się pobrać takiego instrumentu\n" +
                    "Nie istnieje lub wystąpił problem z pobieraniem");
        } else {
            XYChart.Series<Number, Number> series = generateSeries(records);
            series.setName(symbol);
            cachedSeries.put(symbol,records);
            return series;
        }
    }

    public XYChart.Series<Number, Number> restoreSeries(String symbol){
        XYChart.Series<Number, Number> series = generateSeries(cachedSeries.get(symbol));
        series.setName(symbol);
        return series;
    }

    private XYChart.Series<Number, Number> generateSeries(ArrayList<Record> records) {
        XYChart.Series<Number, Number> series = new XYChart.Series<>();
        records.sort(Comparator.comparing(Record::getDate).reversed());
        records.stream().forEach(record -> series.getData().add(
                new XYChart.Data<>(
                        records.indexOf(record) * (-5),
                        record.getClose())));
        return series;
    }

    public String getKey() {
        return apiKey;
    }
}
