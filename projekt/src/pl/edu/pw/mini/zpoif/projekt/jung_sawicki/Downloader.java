package pl.edu.pw.mini.zpoif.projekt.jung_sawicki;

import pl.edu.pw.mini.zpoif.projekt.jung_sawicki.ApiOutput.StockTimeSeries;
import pl.edu.pw.mini.zpoif.projekt.jung_sawicki.GUI.exceptions.JavaFXException;

import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.net.URL;
import java.net.URLConnection;
import java.util.Scanner;

public class Downloader implements Serializable {
    String key;

    public Downloader(String key) {
        this.key = key;
    }
    public String download(String symbol, StockTimeSeries type){
        String urlString = buildRequest(type.getFunction(),symbol);
        try {
            URL url = new URL ( urlString );
            URLConnection connection = url.openConnection();
            InputStream inputStream = connection.getInputStream();
            Scanner in = new Scanner(inputStream);
            StringBuilder answer = new StringBuilder();
            while (in.hasNext()){
                answer.append(in.nextLine());
            }
            System.out.println("Downloaded " + symbol + " in Downloader");
            return answer.toString();
        } catch (IOException e) {
            JavaFXException exception = new JavaFXException(e.getMessage());
            exception.showErrorDialog();
            e.printStackTrace();
        }

        return null;
    }
    private String buildRequest(String function, String symbol){
        return "https://www.alphavantage.co/query?function=" +
                function +
                "&symbol=" +
                symbol +
                "&interval=5min&apikey=" +
                this.key;
    }
}

