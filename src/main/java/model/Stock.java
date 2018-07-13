package model;

import java.time.LocalDateTime;

public class Stock {
    private String symbol;
    private LocalDateTime dateAdded;

    public Stock(String symbol){
        this.symbol = symbol;
        dateAdded = LocalDateTime.now();
    }

    @Override
    public String toString() {
        return symbol;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public LocalDateTime getDateAdded() {
        return dateAdded;
    }

    public void setDateAdded(LocalDateTime dateAdded) {
        this.dateAdded = dateAdded;
    }
}
