package pl.edu.pw.mini.zpoif.projekt.jung_sawicki.ApiOutput;

import java.time.LocalDateTime;

public class Record {
    private LocalDateTime date;
    private float open;
    private float high;
    private float low;
    private float close;
    private int volume;

    public Record(LocalDateTime date, float open, float high, float low, float close, int volume) {
        this.date = date;
        this.open = open;
        this.high = high;
        this.low = low;
        this.close = close;
        this.volume = volume;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public float getOpen() {
        return open;
    }

    public float getHigh() {
        return high;
    }

    public float getLow() {
        return low;
    }

    public float getClose() {
        return close;
    }

    public int getVolume() {
        return volume;
    }

    @Override
    public String toString() {
        return  "data = " + date +
                ", otwarcie = " + open +
                ", max = " + high +
                ", min = " + low +
                ", zamkniecie = " + close +
                ", wolumen = " + volume;
    }
}
