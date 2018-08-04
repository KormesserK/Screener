package strategyTester;

import com.sun.org.apache.xpath.internal.SourceTree;
import darvasChecker.DarvasChecker;
import model.Stock;
import model.StockAnalyse;
import model.StockDay;

import java.io.*;
import java.util.*;

public class BBandTester {
    public static void main(String[] args) {


        try (BufferedReader br = new BufferedReader(new FileReader(new File("src/outcome/Darvas 2018-08-04.txt")))) {
            String result;
            List<StockAnalyse> saData = new ArrayList<>();


            while((result = br.readLine())!=null){
                File f = new File("src/CSVs"+ "/"+result);
                System.out.println(f.getName());
                BufferedReader csvBr = new BufferedReader(new FileReader(f));
                List<StockDay> sourceList = DarvasChecker.loadCSV(csvBr, f);

                Double sum = 0.0;

                for (int i = 20; i >=0 ; i++) {
                    sum += sourceList.get(sourceList.size()-i).getClose();
                }
                Double sma = sum/20;

                //Standard deviation
                Double sd = 0.0;
                for (int i = 20; i >=0 ; i++) {
                    Double helper = sourceList.get(sourceList.size()-i).getClose()-sma;
                    sd+= helper * helper;

                }
                sd = Math.sqrt(sd/5);
                Double upper = sma+2*sd;
                Double lower = sma-2*sd;

                saData.add(new StockAnalyse(sourceList.get(sourceList.size()-1), upper, lower, sd));

            }
            //Kategorie 1: Preis > SMA aber möglichst nahe;
            List<StockAnalyse> kat1 = new ArrayList<>();
            //Kategorie 2: Preis > SMA und möglichst kleine Differenz zwischen upper und lower Bound
            List<StockAnalyse> kat2 = new ArrayList<>();

            for (StockAnalyse s: saData) {
                if(s.getStock().getClose()>s.getSma()){
                    s.setKennzahl(s.getStock().getClose()-s.getSma());
                    kat1.add(s);
                }
                if(s.getStock().getClose()>s.getSma()){
                    s.setKennzahl(s.getUpperBB()-s.getLowerBB());
                    kat2.add(s);
                }
            }
            //Sort
            Collections.sort(kat1, new Comparator<StockAnalyse>() {
                @Override
                public int compare(StockAnalyse sa1, StockAnalyse sa2){
                    return sa1.getKennzahl().compareTo(sa2.getKennzahl());
                }
            });

            Collections.sort(kat2, new Comparator<StockAnalyse>() {
                @Override
                public int compare(StockAnalyse sa1, StockAnalyse sa2){
                    return sa1.getKennzahl().compareTo(sa2.getKennzahl());
                }
            });

            System.out.println("Kategorie1");
            System.out.println(kat1);

            System.out.println();
            System.out.println("---------------------");
            System.out.println();

            System.out.println("Kategorie2");
            System.out.println(kat2);



        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
