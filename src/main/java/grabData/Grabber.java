package grabData;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import model.Stock;
import model.StockDay;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
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
                URL url = null;
                HttpURLConnection request;
                StringBuilder apirequest = new StringBuilder();
                apirequest.append("https://www.alphavantage.co/query?function=TIME_SERIES_DAILY&symbol=");
                apirequest.append(s.getSymbol().replace("\"", ""));
                apirequest.append("&apikey=");
                apirequest.append(apiKey);
                url = new URL(apirequest.toString());
                request = (HttpURLConnection) url.openConnection();
                request.setDoOutput(true);
                request.setRequestMethod("GET");
                System.out.println(url.toString());
                request.connect();
                //GSON
                String line;
                GsonBuilder builder = new GsonBuilder();
                Gson gson = builder.create();
                BufferedReader br = new BufferedReader(new InputStreamReader(request.getInputStream()));
                StringBuilder sb = new StringBuilder();
                while ((line = br.readLine()) != null) {
                    sb.append(line);
                }
                List<StockDay> days = new ArrayList<>();
                StockDay[] arrayDays = gson.fromJson(sb.toString(), StockDay[].class);

                System.out.println(arrayDays);
                for (StockDay sd : arrayDays) {
                    days.add(sd);
                }
                System.out.println(days);
                //GSON


            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch(Exception ex){
                ex.printStackTrace();
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

    public static List<StockDay> toGson(String symbol){
        boolean done = false;

        while(!done){

        }
        return null;
    }

}
