package pl.edu.pw.mini.zpoif.projekt.jung_sawicki.notifications;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;


class NotificationTest {

    @Test
    public void simpleTest() throws InterruptedException {
        /* Add your email and apiKey before launching*/
        User user = new User("user", "*", "*");
        user.addNotification("IBM", 130, Notification.Type.PRICE_HIGHER_THAN);
        Thread.sleep(1000*10);
    }

    @Test
    public void simpleSavingTest() throws InterruptedException {
        /* Add your email and apiKey before launching*/
        User user = new User("user", "*", "*");
        user.addNotification("IBM", 130, Notification.Type.PRICE_HIGHER_THAN);
        user.getNotifications().forEach(Notification::saveNotification);
        user.getNotifications().clear();
        user.setNotifications(Notification.readAllNotifications());
        assertEquals(1, user.getNotifications().size());
        assertEquals("IBM", user.getNotifications().stream().findFirst().orElseThrow().getObservedStock());

        Thread.sleep(1000*10);

    }

}