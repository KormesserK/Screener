package darvasChecker;

/*
- Die Aktie hat in den letzten 4 Wochen ein neues 52-Wochenhoch markiert.
- Die Aktie hat mindestens 70 % Performance seit dem 52-Wochentief erzielt.
- Die Marktampel gibt grünes Licht. Der S&P 500 notiert über dem GD 200
 */

import model.StockDay;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalUnit;
import java.util.List;

public class DarvasChecker {


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
