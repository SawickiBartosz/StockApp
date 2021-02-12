package pl.edu.pw.mini.zpoif.projekt.jung_sawicki.notifications;

import pl.edu.pw.mini.zpoif.projekt.jung_sawicki.ApiOutput.Record;

import java.time.LocalDateTime;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Notification implements Serializable {


    private Notification.Status status;
    private final String observedStock;
    private final float triggeringPrice;
    private final NotificationObserver observer;
    private final User user;
    private final LocalDateTime startDate;
    private final Type type;
    private ScheduledThreadPoolExecutor scheduler;


    public void init() {
        scheduler = new ScheduledThreadPoolExecutor(1);
        scheduler.scheduleAtFixedRate(observer, 0,5, TimeUnit.MINUTES);
        scheduler.setRemoveOnCancelPolicy(true);
        scheduler.setExecuteExistingDelayedTasksAfterShutdownPolicy(false);
    }

    public void endThread(){
        if(scheduler != null && !scheduler.isTerminated()) scheduler.shutdown();
        scheduler = null;
    }

    public float getTriggeringPrice() {
        return triggeringPrice;
    }

    public Type getType() {
        return type;
    }

    public Status getStatus() {
        return status;
    }

    public enum Type {
        PRICE_LOWER_THAN, PRICE_HIGHER_THAN;

        @Override
        public String toString() {
            return super.toString().replace('_',' ' );
        }
    }

    public enum Status {
        ACTIVE, TRIGGERED, REALIZED;


        @Override
        public String toString() {
            return super.toString();
        }
    }

    public Notification(String observedStock, float triggeringPrice, Type type, User user) {
        this.observedStock = observedStock;
        this.triggeringPrice = triggeringPrice;
        this.user = user;
        this.status = Notification.Status.ACTIVE;

        this.observer = type == Type.PRICE_HIGHER_THAN ?
                new NotifyWhenPriceHigher(observedStock, triggeringPrice, user.getKey(), this):
                new NotifyWhenPriceLower(observedStock, triggeringPrice, user.getKey(), this);
        this.scheduler = new ScheduledThreadPoolExecutor(1);
        scheduler.scheduleAtFixedRate(observer, 0,5, TimeUnit.MINUTES);
        startDate = LocalDateTime.now();
        this.type = type;
    }

    public LocalDateTime getStartDate() {
        return startDate;
    }

    public void trigger(ArrayList<Record> records){
        if(this.status != Status.ACTIVE) return;
        if(records.size() == 0) return;
        status = Status.TRIGGERED;
        MailSender mailSender = new MailSender();
        String text = buildMessage(records);
        String subject = "Powiadomienie StockApp";
        try {
            mailSender.sendMail(user.getEmail(), text, subject);
            status = Status.REALIZED;
            System.out.println("Email sent");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String buildMessage(ArrayList<Record> records) {
        StringBuilder builder = new StringBuilder();
        builder.append(user.getNick());
        builder.append(",\n");
        builder.append("Oto Twoje powiadomienie z aplikacji StockApp dotyczące papieru ");
        builder.append(observedStock);
        builder.append(":\n");
        builder.append(observer.getDetails());
        builder.append(":\n");
        records.forEach(r -> {
            builder.append(r.toString());
            builder.append("\n");
        });
        builder.append("\n\nŻyczymy powodzenia na giełdzie!\nZespół StockApp");
        return builder.toString();
    }

    public void saveNotification() {
        checkIfSavingPathExistsAndCreateOne();
        Path path = Paths.get("./app_data/notifications/" + this.hashCode() + ".ser");
        try {
            FileOutputStream fileOut = new FileOutputStream(path.toString());
            ObjectOutputStream out = new ObjectOutputStream(fileOut);
            out.writeObject(this);
            out.close();
            fileOut.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void checkIfSavingPathExistsAndCreateOne() {
        Path dirPath = Paths.get("./app_data/notifications");
        File theDir = new File(String.valueOf(dirPath));
        if (!theDir.exists()) {
            theDir.mkdirs();
        }
    }

    public static ArrayList<Notification> readAllNotifications() {
        ArrayList<Notification> result = new ArrayList<>();
        checkIfSavingPathExistsAndCreateOne();
        Path dirPath = Paths.get("./app_data/notifications");
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
                Notification n = (Notification) in.readObject();
                in.close();
                fileIn.close();
                result.add(n);
            } catch (IOException | ClassNotFoundException i) {
                i.printStackTrace();
            }
        }
        return result;
    }

    public String getObservedStock() {
        return observedStock;
    }
}
