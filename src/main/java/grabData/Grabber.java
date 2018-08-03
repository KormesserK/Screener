package grabData;

import jdk.nashorn.api.scripting.URLReader;
import model.Stock;
import model.StockDay;

import java.awt.*;
import java.io.*;
import java.net.*;
import java.time.LocalDate;
import java.time.Period;
import java.time.temporal.TemporalAmount;
import java.time.temporal.TemporalUnit;
import java.util.ArrayList;
import java.util.List;

public class Grabber {

    public static void main(String[] args) {
        List<Stock> resultData = new ArrayList<>();
        List<Stock> nasdaqStocks = allNasdaqStocks();


        //Grabbing all Stocks of the Nasdaq

        System.out.println(nasdaqStocks);
        System.out.println(nasdaqStocks.size());
        List<StockDay> priceList = new ArrayList<>();
        List<Stock> darwasList = new ArrayList<>();
        try{

            downloadStocks(nasdaqStocks);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }

    }

    public static List<Stock> allNasdaqStocks() {
        List<Stock> retList = new ArrayList<>();
        try {
            BufferedReader br = new BufferedReader(new FileReader(new File("src/companylist.csv")));
            String readin;
            while((readin = br.readLine())!=null){
                retList.add(new Stock(readin.split(",")[0]));
            }
        }catch(IOException ioex){
            System.err.println("IOException" + ioex.getMessage());
        }
        return retList;
    }

    private static void downloadStocks(List<Stock> nasdaqStocks) throws IOException, URISyntaxException {
        int i = 0;
        for (Stock s : nasdaqStocks){
            //https://query1.finance.yahoo.com/v7/finance/download/AAPL?period1=1375480800&period2=1533247200&interval=1d&events=history&crumb=I/QgbXUC23S
            String url = "https://query1.finance.yahoo.com/v7/finance/download/"+s.getSymbol().replace("\"", "") + "?period1=1375480800&period2=1533247200&interval=1d&events=history&crumb=I/QgbXUC23S".replace(" ", "");

            try {
                Desktop desktop = java.awt.Desktop.getDesktop();

                URI uri = new URI(url);

                desktop.browse(uri);

                i++;
                if(i%100==0){
                    Thread.sleep(10000);
                    System.out.println(i);
                }

            }catch (URISyntaxException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }
    }



    private static String urlToString(InputStream inputStream) throws IOException
    {
        try (BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8")))
        {
            String inputLine;
            StringBuilder stringBuilder = new StringBuilder();
            while ((inputLine = bufferedReader.readLine()) != null)
            {
                stringBuilder.append(inputLine);
            }

            return stringBuilder.toString();
        }
    }



    private static boolean isDarwas(List<StockDay> priceList, String symbol) {
        if(priceList.size()<1){
            return false;
        }
        System.err.println("ISDarwas");
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
