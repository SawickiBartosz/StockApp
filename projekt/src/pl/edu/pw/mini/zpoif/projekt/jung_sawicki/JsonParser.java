package pl.edu.pw.mini.zpoif.projekt.jung_sawicki;


import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.ObjectMapper;
import pl.edu.pw.mini.zpoif.projekt.jung_sawicki.ApiOutput.Record;
import pl.edu.pw.mini.zpoif.projekt.jung_sawicki.GUI.exceptions.ParsingException;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class JsonParser {
    public static ArrayList<Record> parseJSONResponse(String response) {
        return processAll(response);
    }

    /**
     * Process the JSON response and return the
     * results as List of Records.
     *
     * @param response a String from API containing JSON response plain text
     * @return a list of Records with stock information.
     */
    private static ArrayList<Record> processAll(String response) {

        ArrayList<Record> list = new ArrayList<>();

        try {
            ObjectMapper mapper = new ObjectMapper();
            JsonNode rootNode = mapper.readValue(response, JsonNode.class);
            Map<String, Object> mapRootNode = mapper.convertValue(rootNode, Map.class);

            for (String key : mapRootNode.keySet()) {
                if (key.toLowerCase().contains("time")) {
                    Map<String, Object> timeseriesRoot = (Map<String, Object>) mapRootNode.get(key);

                    for (String timestamp_recorded : timeseriesRoot.keySet()) {
                        Map<String, String> recorded_values = (Map<String, String>) timeseriesRoot.get(timestamp_recorded);
                        HashMap<String, String> single_row = buildRow(recorded_values, timestamp_recorded);

                        Record record = new Record(parseDate(single_row),
                                Float.parseFloat(single_row.get("open")),
                                Float.parseFloat(single_row.get("high")),
                                Float.parseFloat(single_row.get("low")),
                                Float.parseFloat(single_row.get("close")),
                                Integer.parseInt(single_row.get("volume")));

                        list.add(record);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            ParsingException exception = new ParsingException("Błąd podczas parsowania danych");
            exception.showErrorDialog();
        }

        return list;
    }

    private static LocalDateTime parseDate(HashMap<String, String> single_row) {

        String date_string = single_row.get("Timestamp_Recorded");
        LocalDateTime date;

        if (date_string.length() == 10) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            date = LocalDate.parse(date_string, formatter).atStartOfDay();
        } else {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            date = LocalDateTime.parse(date_string, formatter);
        }

        return date;
    }

    private static HashMap<String, String> buildRow(Map<String, String> recorded_values, String timestamp_recorded) {
        HashMap<String, String> single_row = new HashMap<>();

        single_row.put("Timestamp_Recorded", timestamp_recorded);
        single_row.put("open", recorded_values.get("1. open"));
        single_row.put("high", recorded_values.get("2. high"));
        single_row.put("low", recorded_values.get("3. low"));
        single_row.put("close", recorded_values.get("4. close"));
        single_row.put("volume", recorded_values.get("5. volume"));

        return single_row;
    }
}
