package pl.edu.pw.mini.zpoif.projekt.jung_sawicki.ApiOutput;

import pl.edu.pw.mini.zpoif.projekt.jung_sawicki.Downloader;
import pl.edu.pw.mini.zpoif.projekt.jung_sawicki.JsonParser;

import java.util.ArrayList;

public abstract class StockTimeSeries {
    private String symbol;
    private final String function;
    private Downloader downloader;
    protected ArrayList<Record> records;
    public String getFunction(){
        return function;
    }

    public StockTimeSeries(Downloader downloader,String symbol,String function) {
        this.symbol = symbol;
        this.downloader = downloader;
        this.function = function;
    }

    public ArrayList<Record> downloadData(){
        return JsonParser.parseJSONResponse(downloader.download(symbol,this));
    }

}
