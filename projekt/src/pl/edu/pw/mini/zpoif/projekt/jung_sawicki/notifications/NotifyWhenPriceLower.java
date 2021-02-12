package pl.edu.pw.mini.zpoif.projekt.jung_sawicki.notifications;

public class NotifyWhenPriceLower extends NotificationObserver {

    public NotifyWhenPriceLower(String observedStock, float triggeringPrice, String apiKey, Notification notification) {
        super(observedStock, triggeringPrice, apiKey, notification);
    }

    @Override
    protected boolean checkIfTrigger(float high, float low) {
        return low < super.getPrice();
    }

    @Override
    protected String getDetails() {
        return "W tych momentach cena była niższa od ustalonej ceny docelowej";

    }
}
