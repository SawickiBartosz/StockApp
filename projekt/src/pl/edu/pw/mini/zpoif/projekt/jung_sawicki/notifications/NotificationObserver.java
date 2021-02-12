package pl.edu.pw.mini.zpoif.projekt.jung_sawicki.notifications;

import pl.edu.pw.mini.zpoif.projekt.jung_sawicki.ApiOutput.*;
import pl.edu.pw.mini.zpoif.projekt.jung_sawicki.Downloader;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.stream.Collectors;

public abstract class NotificationObserver implements Runnable, Serializable {
    private String stock;
    private float price;
    private Downloader downloader;
    private Notification notificacion;

    public NotificationObserver(String observedStock, float triggeringPrice, String apiKey, Notification notification) {
        this.stock = observedStock;
        this.price = triggeringPrice;
        this.downloader = new Downloader(apiKey);
        this.notificacion = notification;
    }

    abstract protected boolean checkIfTrigger(float high, float low);
    abstract protected String getDetails();

    @Override
    public void run() {
        StockTimeSeries interday = new Interday(downloader, stock, Interval.FIVE );
        ArrayList<Record> records = interday.downloadData();
        System.out.println("pobrano");
        ArrayList<Record> recordList = records.stream().filter(r -> checkIfTrigger(r.getHigh(), r.getLow()))
                .filter(r -> r.getDate().isAfter(notificacion.getStartDate()))
                .collect(Collectors.toCollection(ArrayList::new));
        if(recordList.size() == 0){
            notificacion.trigger(
                    records.stream().filter(r -> checkIfTrigger(r.getHigh(), r.getLow()))
                            .sorted(Comparator.comparing(Record::getDate))
                            .limit(1)
                            .collect(Collectors.toCollection(ArrayList::new))
            );
        }else notificacion.trigger(
               recordList
        );
    }

    protected float getPrice() {
        return price;
    }
}
