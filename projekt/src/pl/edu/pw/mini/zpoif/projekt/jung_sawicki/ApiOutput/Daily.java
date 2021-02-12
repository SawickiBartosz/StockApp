package pl.edu.pw.mini.zpoif.projekt.jung_sawicki.ApiOutput;

import pl.edu.pw.mini.zpoif.projekt.jung_sawicki.Downloader;

public class Daily extends StockTimeSeries{

    public Daily(Downloader downloader, String symbol) {
        super(downloader, symbol, "TIME_SERIES_DAILY");
    }
}
