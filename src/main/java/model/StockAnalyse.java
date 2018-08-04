package model;

public class StockAnalyse {
    private StockDay stock;
    private Double upperBB;
    private Double lowerBB;
    private Double sma;
    private Double kennzahl;

    public StockAnalyse() {
    }

    public StockAnalyse(StockDay stock, Double upperBB, Double lowerBB, Double sma) {
        this.stock = stock;
        this.upperBB = upperBB;
        this.lowerBB = lowerBB;
        this.sma = sma;
    }



    public StockDay getStock() {
        return stock;
    }

    public void setStock(StockDay stock) {
        this.stock = stock;
    }

    public Double getUpperBB() {
        return upperBB;
    }

    public void setUpperBB(Double upperBB) {
        this.upperBB = upperBB;
    }

    public Double getLowerBB() {
        return lowerBB;
    }

    public void setLowerBB(Double lowerBB) {
        this.lowerBB = lowerBB;
    }

    public Double getSma() {
        return sma;
    }

    public void setSma(Double sma) {
        this.sma = sma;
    }

    public Double getKennzahl() {
        return kennzahl;
    }

    public void setKennzahl(Double kennzahl) {
        this.kennzahl = kennzahl;
    }
}
