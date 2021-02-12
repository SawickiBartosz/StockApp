package pl.edu.pw.mini.zpoif.projekt.jung_sawicki.ApiOutput;

import pl.edu.pw.mini.zpoif.projekt.jung_sawicki.Downloader;

public class Interday extends StockTimeSeries{
    private Interval interval;

    public Interday(Downloader downloader,String symbol, Interval interval) {
        super(downloader,symbol,"TIME_SERIES_INTRADAY");
        this.interval = interval;
    }
}
