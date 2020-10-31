package com.example.myfinalproject.DB;

public class StockItem {
    public int id;
    public String stockname, stockcode, stockprice;

    public StockItem() {
        super();
        stockname = "";
        stockcode = "";
        stockprice = "";
    }

    public StockItem(String stockname, String code, String stockprice) {
        super();
        this.stockname = stockname;
        this.stockcode = code;
        this.stockprice = stockprice;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getStockname() {
        return stockname;
    }

    public void setStockname(String stockname) {
        this.stockname = stockname;
    }

    public String getStockcode() {
        return stockcode;
    }

    public void setStockcode(String stockcode) {
        this.stockcode = stockcode;
    }

    public String getStockprice() {
        return stockprice;
    }

    public void setStockprice(String stockprice) {
        this.stockprice = stockprice;
    }
}
