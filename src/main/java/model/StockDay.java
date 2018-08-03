package model;

import java.time.LocalDate;

public class StockDay {
    private String stock;
    private LocalDate day;
    private Double open;
    private Double high;
    private Double low;
    private Double close;
    private Long volume;



    public StockDay(String stock, LocalDate day, Double open, Double high, Double low, Double close, Long volume) {
        this.stock = stock;
        this.day = day;
        this.open = open;
        this.high = high;
        this.low = low;
        this.close = close;
        this.volume = volume;
    }

    public StockDay() {
    }

    //Getter und Setter


    public String getStock() {
        return stock;
    }

    public void setStock(String stock) {
        this.stock = stock;
    }

    public LocalDate getDay() {
        return day;
    }

    public void setDay(LocalDate day) {
        this.day = day;
    }

    public Double getOpen() {
        return open;
    }

    public void setOpen(Double open) {
        this.open = open;
    }

    public Double getClose() {
        return close;
    }

    public void setClose(Double close) {
        this.close = close;
    }

    public Double getLow() {
        return low;
    }

    public void setLow(Double low) {
        this.low = low;
    }

    public Double getHigh() {
        return high;
    }

    public void setHigh(Double high) {
        this.high = high;
    }

    public Long getVolume() {
        return volume;
    }

    public void setVolume(Long volume) {
        this.volume = volume;
    }

}


