package darvasChecker;

/*
- Die Aktie hat in den letzten 4 Wochen ein neues 52-Wochenhoch markiert.
- Die Aktie hat mindestens 70 % Performance seit dem 52-Wochentief erzielt.
- Die Marktampel gibt grünes Licht. Der S&P 500 notiert über dem GD 200
 */

import grabData.Grabber;
import model.Stock;
import model.StockDay;

import java.io.*;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalUnit;
import java.util.ArrayList;
import java.util.List;

public class DarvasChecker {

    public static void main(String[] args) {
        List<Stock> resultData = new ArrayList<>();
        List<Stock> nasdaqStocks = Grabber.allNasdaqStocks();
        System.out.println(nasdaqStocks);
        System.out.println(nasdaqStocks.size());
        List<StockDay> priceList = new ArrayList<>();
        List<Stock> darwasList = new ArrayList<>();

        String dirPath = "src/CSVs";

        File dir = new File(dirPath);
        File[] directoryListing = dir.listFiles();
        if (directoryListing != null) {
            for (File child : directoryListing) {
                try (BufferedReader br = new BufferedReader(new FileReader(child))) {
                    br.readLine();
                    String line;

                    while((line = br.readLine())!=null){
                        String[] splitLine = line.split(",");
                        StockDay sd = new StockDay();
                        sd.setStock(child.getName());
                        sd.setDay();
                    }



                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            }


    }

    public static Boolean isDarvas(List<StockDay> data){
        StockDay low = getLowWeeks(data);
        if(low.getClose()*1.70>data.get(0).getClose()){
            return false;
        }
        if(!hasHigh(data)){
            return false;
        }

        return true;

    }

    public static StockDay getLowWeeks(List<StockDay> data){
        LocalDateTime day = LocalDateTime.now().minus(52, ChronoUnit.WEEKS);
        StockDay low = new StockDay();

        for (StockDay sd: data ) {
            if(day.equals(sd.getDay())){
                low = sd;
            }
        }

        for (StockDay sd : data){
            if(low.getClose()<sd.getLow()){
                low = sd;
            }
        }
        return low;
    }

    public static boolean hasHigh(List<StockDay> data){
        StockDay akt = data.get(0);
        for (StockDay sd : data) {
            if(!sd.equals(data.get(0))){
                if(akt.getClose()<sd.getClose()){
                    return false;
                }
            }
        }
        return true;
    }

}
