package pl.edu.pw.mini.zpoif.projekt.jung_sawicki;

import java.util.ArrayList;
import java.util.LinkedList;

public class MovingAvg {
    public static ArrayList<Number> createAvgList(ArrayList<Number> Records, int N){
        ArrayList<Number> AvgList = new ArrayList<>();
        LinkedList<Number> NRecords = new LinkedList<>();
        for (int i=0; i < N-1; i++){
            NRecords.add(Records.get(i));
        }
        for (int i=N-1; i < Records.size(); i++) {
            NRecords.add(Records.get(i));
            double RecordsMean = (double)0;
            for (int j = 0; j < N; j++) {
                RecordsMean += (NRecords.get(j)).doubleValue();
            }
            RecordsMean/=N;
            NRecords.poll();
            AvgList.add(RecordsMean);
        }
        return AvgList;
    }
}
