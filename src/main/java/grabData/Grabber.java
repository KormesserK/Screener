package grabData;

import jdk.nashorn.api.scripting.URLReader;
import model.Stock;
import model.StockDay;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.LocalDate;
import java.time.Period;
import java.time.temporal.TemporalAmount;
import java.time.temporal.TemporalUnit;
import java.util.ArrayList;
import java.util.List;

public class Grabber {

    public static void main(String[] args) {
        List<Stock> resultData = new ArrayList<>();
        List<Stock> nasdaqStocks = new ArrayList<>();
        String apiKey = "4MM13XOJEQTR9QD5";


        //Grabbing all Stocks of the Nasdaq
        try {
            BufferedReader br = new BufferedReader(new FileReader(new File("src/companylist.csv")));
            String readin;
            while((readin = br.readLine())!=null){
                nasdaqStocks.add(new Stock(readin.split(",")[0]));
            }
        }catch(IOException ioex){
            System.err.println("IOException" + ioex.getMessage());
        }
        System.out.println(nasdaqStocks);
        System.out.println(nasdaqStocks.size());
        List<StockDay> priceList = new ArrayList<>();
        List<Stock> darwasList = new ArrayList<>();


        //https://www.alphavantage.co/query?function=TIME_SERIES_DAILY&symbol=pih&apikey=4MM13XOJEQTR9QD5
        for (Stock s : nasdaqStocks) {
            try {
                HttpURLConnection request = null;
                StringBuilder apirequest = new StringBuilder();
                apirequest.append("https://www.alphavantage.co/query?function=TIME_SERIES_DAILY&symbol=");
                apirequest.append(s.getSymbol().replace("\"", ""));
                apirequest.append("&apikey=");
                apirequest.append(apiKey);
                apirequest.append("&datatype=csv");

                BufferedReader br = new BufferedReader(new URLReader(new URL(apirequest.toString())));
                br.readLine();

                priceList = new ArrayList<>();

                String line;
                System.out.println(s.getSymbol());
                while((line=br.readLine())!=null){
                    //System.out.println(line);
                    String[] splitted = line.split(",");
                    Stock stock = new Stock(s.getSymbol());
                    String[] datums = splitted[0].split("-");
                    LocalDate date =  LocalDate.of(Integer.parseInt(datums[0]), Integer.parseInt(datums[1]), Integer.parseInt(datums[2]));
                    priceList.add(new StockDay(stock, date, Double.parseDouble(splitted[1]), Double.parseDouble(splitted[2]), Double.parseDouble(splitted[3]), Double.parseDouble(splitted[4]), Long.parseLong(splitted[5])));
                }

                if(isDarwas(priceList, s.getSymbol())){
                    darwasList.add(new Stock(s.getSymbol()));
                    System.out.println(darwasList);
                }


                //GSON



            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch(Exception e){
                e.printStackTrace();
            }
        }

        /*
        //https://www.alphavantage.co/query?function=TIME_SERIES_DAILY&symbol=MSFT&outputsize=full&apikey=demo
        //String json = "{\"brand\":\"Jeep\", \"doors\": 3}";
        String stock = nasdaqStocks.get(0).toString();
        String jsonString = "https://www.alphavantage.co/query?function=TIME_SERIES_DAILY&symbol"+stock.toString()+"&outputsize=full&apikey="+apiKey;
        GsonBuilder gb = new GsonBuilder();
        Gson gson = gb.create();
        gson.toJson(jsonString, StockDay.class);
        String fjsonString = "{\"
        gson.fromJson()
        */
       /* for (Stock stock : nasdaqStocks){





        }*/


    }

    private static boolean isDarwas(List<StockDay> priceList, String symbol) {
        if(priceList.size()<1){
            return false;
        }
        StockDay high = getHigh(priceList, symbol);
        StockDay low = getLow(priceList, symbol);
        StockDay akt = priceList.get(0);
        StockDay low52 = priceList.get(0);

        for (int i = 0; priceList.get(i).getDay().isAfter(priceList.get(0).getDay().minus(Period.ofWeeks(52))) ; i++) {
            if(priceList.get(i).getHigh()<low52.getHigh()){
                low52 = priceList.get(i);
            }
        }

        if(high.getDay().isBefore(LocalDate.now().minus(Period.ofDays(28)))&&priceList.get(0).getHigh()*1.7>low52.getHigh()){
            return true;

        }

        return false;
    }

    private static StockDay getLow(List<StockDay> priceList, String symbol){
        StockDay low = priceList.get(0);
        for (StockDay sd : priceList){
            if(sd.getHigh()<low.getHigh()){
                low = sd;
            }
        }
        return low;
    }

    private static StockDay getHigh(List<StockDay> priceList, String symbol) {
        StockDay high = priceList.get(0);
        for(StockDay sd: priceList){
            if(sd.getHigh()>high.getHigh()){
                high = sd;
            }
        }
        return high;
    }


}
