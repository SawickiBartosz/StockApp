package pl.edu.pw.mini.zpoif.projekt.jung_sawicki.GUI;

import javafx.beans.property.SimpleFloatProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import pl.edu.pw.mini.zpoif.projekt.jung_sawicki.GUI.exceptions.JavaFXException;
import pl.edu.pw.mini.zpoif.projekt.jung_sawicki.notifications.Notification;
import pl.edu.pw.mini.zpoif.projekt.jung_sawicki.notifications.User;

import java.io.IOException;
import java.util.HashMap;

public class NotificationsController {

    @FXML
    private Button returnToPlotsButton;
    @FXML
    private RadioButton priceHigherRadio;
    @FXML
    private RadioButton priceLowerRadio;
    @FXML
    private TextField stockSymbolForNotification;
    @FXML
    private TextField notificationPrice;
    @FXML
    private Button submitNotificationButton;
    @FXML
    private TableColumn<NotificationRow, Button> removeButtonCol;
    @FXML
    private TableColumn<NotificationRow, String> stockSymbolCol;
    @FXML
    private TableColumn<NotificationRow, String> typeCol;
    @FXML
    private TableColumn<NotificationRow, Float> priceCol;
    @FXML
    private TableColumn<NotificationRow, String> statusCol;
    @FXML
    private TableView<NotificationRow> notificationsTable = new TableView<>();
    User user = StockApp.getCurrentUser();
    private HashMap<NotificationRow, Notification> notificationsMap = new HashMap<>();
    private PlotScene plotScene;
    private final ObservableList<NotificationRow> observableNotifications = FXCollections.observableArrayList();

    public void initialize() {
        User user = StockApp.getCurrentUser();
        notificationsTable.setEditable(true);
        plotScene = new PlotScene();
        notificationsTable.setItems(observableNotifications);
        stockSymbolCol.setCellValueFactory(new PropertyValueFactory<>("stockSymbolOfNotification"));
        typeCol.setCellValueFactory(new PropertyValueFactory<>("typeOfNotification"));
        priceCol.setCellValueFactory(new PropertyValueFactory<>("priceOfNotification"));
        statusCol.setCellValueFactory(new PropertyValueFactory<>("statusOfNotification"));
        removeButtonCol.setCellFactory(PlotController.ActionButtonTableCell.forTableColumn("Usuń", (NotificationRow n) ->
        {
            notificationsTable.getItems().remove(n);
            observableNotifications.remove(n);
            Notification notification = notificationsMap.remove(n);
            user.getNotifications().remove(notification);
            notification.endThread();
            return n;
        }));
        StockApp.getCurrentUser().getNotifications().forEach(n -> {
            NotificationRow notificationRow = new NotificationRow(n.getTriggeringPrice(),
                    n.getType().toString(), n.getObservedStock(), n.getStatus().toString());
            observableNotifications.add(notificationRow);
            notificationsMap.put(notificationRow, n);
        });
    }


    public void submitNotification(ActionEvent actionEvent) {
        String stockSymbolOfNotification = stockSymbolForNotification.getText();
        String typeOfNotification = priceHigherRadio.isSelected() ? ">" : "<";
        float priceOfNotification;
        try {
            priceOfNotification = Float.parseFloat(notificationPrice.getText());
        } catch (NumberFormatException e) {
            JavaFXException exception = new JavaFXException("Nieprawidłowa wartość w polu ceny");
            exception.showErrorDialog();
            return;
        }
        try {
            user.downloadData(stockSymbolOfNotification);
        } catch (JavaFXException e) {
            e.showErrorDialog();
        }
        Notification notification = null;
        if (priceHigherRadio.isSelected()) {
            notification = new Notification(stockSymbolOfNotification, priceOfNotification, Notification.Type.PRICE_HIGHER_THAN, user);
        }
        if (priceLowerRadio.isSelected()) {
            notification = new Notification(stockSymbolOfNotification, priceOfNotification, Notification.Type.PRICE_LOWER_THAN, user);
        }
        if (notification != null) {
            user.addNotification(notification);
            NotificationRow notificationRow = new NotificationRow(
                    notification.getTriggeringPrice(), notification.getType().toString(),
                    notification.getObservedStock(), notification.getStatus().toString());
            observableNotifications.add(notificationRow);
            notificationsMap.put(notificationRow, notification);
        }
    }

    public void priceHigherRadioClick(ActionEvent actionEvent) {
    }

    public void priceLowerRadioClick(ActionEvent actionEvent) {
    }

    public void returnToPlots(ActionEvent actionEvent) throws IOException {
        Stage stage = (Stage) returnToPlotsButton.getScene().getWindow();
        plotScene.start(stage);
    }

    public static class NotificationRow {
        public final SimpleFloatProperty priceOfNotification;
        public final SimpleStringProperty typeOfNotification;
        public final SimpleStringProperty stockSymbolOfNotification;
        public final SimpleStringProperty statusOfNotification;


        public NotificationRow(Float priceOfNotification, String typeOfNotification, String stockSymbolOfNotication, String statusOfNotification) {
            this.priceOfNotification = new SimpleFloatProperty(priceOfNotification);
            this.typeOfNotification = new SimpleStringProperty(typeOfNotification);
            this.stockSymbolOfNotification = new SimpleStringProperty(stockSymbolOfNotication);
            this.statusOfNotification = new SimpleStringProperty(statusOfNotification);
        }

        public Float getPriceOfNotification() {
            return priceOfNotification.get();
        }

        public String getTypeOfNotification() {
            return typeOfNotification.get();
        }

        public String getStockSymbolOfNotification() {
            return stockSymbolOfNotification.get();
        }

        public String getStatusOfNotification() {
            return statusOfNotification.get();
        }
    }
}
