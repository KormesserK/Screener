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
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalUnit;
import java.util.ArrayList;
import java.util.Date;
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
        String stockname= "";

        File dir = new File(dirPath);
        File[] directoryListing = dir.listFiles();
        if (directoryListing != null) {
            for (File child : directoryListing) {
                try (BufferedReader br = new BufferedReader(new FileReader(child))) {
                    br.readLine();
                    String line;

                    while((line = br.readLine())!=null){
                        try {
                            String[] splitLine = line.split(",");
                            StockDay sd = new StockDay();
                            sd.setStock(child.getName());
                            stockname = child.getName();
                            String[] dateSplit = splitLine[0].split("-");
                            LocalDate ld = LocalDate.of(Integer.parseInt(dateSplit[0]), Integer.parseInt(dateSplit[1]), Integer.parseInt(dateSplit[2]));
                            sd.setDay(ld);
                            sd.setOpen(Double.parseDouble(splitLine[1]));
                            sd.setHigh(Double.parseDouble(splitLine[2]));
                            sd.setLow(Double.parseDouble(splitLine[3]));
                            sd.setClose(Double.parseDouble(splitLine[4]));
                            sd.setVolume(Long.parseLong(splitLine[6]));
                            priceList.add(sd);
                        }catch(Exception ex){
                            //ex.printStackTrace();
                        }
                    }

                    if(isDarvas(priceList)){
                        Stock darvas = new Stock(stockname);
                        System.out.println(darvas.getSymbol());
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
        if(low.getClose()*1.70>data.get(data.size()-1).getClose()){
            return false;
        }
        if(!hasHigh(data)){
            return false;
        }

        return true;

    }

    public static StockDay getLowWeeks(List<StockDay> data){
        LocalDate day = LocalDate.now().minus(52, ChronoUnit.WEEKS);
        StockDay low = new StockDay();

        for (StockDay sd: data ) {
            if(day.equals(sd.getDay())){
                low = sd;
            }
        }

        for (StockDay sd : data){
            if(low.getClose()>sd.getLow() && sd.getDay().isAfter(day)){
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
