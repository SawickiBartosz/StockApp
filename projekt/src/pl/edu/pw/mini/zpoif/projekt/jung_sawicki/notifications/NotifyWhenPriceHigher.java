package pl.edu.pw.mini.zpoif.projekt.jung_sawicki.notifications;

public class NotifyWhenPriceHigher extends NotificationObserver{
    public NotifyWhenPriceHigher(String observedStock, float triggeringPrice, String apiKey, Notification notification) {
        super(observedStock, triggeringPrice, apiKey, notification);
    }

    @Override
    protected boolean checkIfTrigger(float high, float low) {
        return high > super.getPrice();
    }

    @Override
    protected String getDetails() {
        return "W tych momentach cena była wyższa od ustalonej ceny docelowej";
    }


}
