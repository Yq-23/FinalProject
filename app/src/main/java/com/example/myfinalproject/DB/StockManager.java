package com.example.myfinalproject.DB;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

public class StockManager {

    private DBhelper dbhelper;
    private String tbname;

    public StockManager(Context context){
        dbhelper = new DBhelper(context);
        tbname = DBhelper.table_name;
    }

    public void add(StockItem item){
        SQLiteDatabase db = dbhelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("stockname",item.getStockname());
        values.put("stockcode",item.getStockcode());
        values.put("stockprice",item.getStockprice());
        db.insert(tbname, null, values);
        db.close();
    }

    public void addAll(List<StockItem> list){
        SQLiteDatabase db = dbhelper.getWritableDatabase();
        for(StockItem item : list){
            ContentValues values = new ContentValues();
            values.put("stockname",item.getStockname());
            values.put("stockcode",item.getStockcode());
            values.put("stockprice",item.getStockprice());
            db.insert(tbname, null, values);
        }
        db.close();
    }

    public void deleteAll(){
        SQLiteDatabase db = dbhelper.getWritableDatabase();
        db.delete(tbname,null,null);
        db.close();
    }

    public void deleteId(int id){
        SQLiteDatabase db = dbhelper.getWritableDatabase();
        db.delete(tbname, "ID=?", new String[]{String.valueOf(id)});
        db.close();
    }

    public void deleteCode(String stockcode){
        SQLiteDatabase db = dbhelper.getWritableDatabase();
        db.delete(tbname, "STOCKCODE=?", new String[]{stockcode});
        db.close();
    }

    public void deleteName(String stockname){
        SQLiteDatabase db = dbhelper.getWritableDatabase();
        db.delete(tbname, "STOCKNAME=?", new String[]{stockname});
        db.close();
    }

    public void updatePrice(StockItem item){
        SQLiteDatabase db = dbhelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("stockname",item.getStockname());
        values.put("stockcode",item.getStockcode());
        values.put("stockprice",item.getStockprice());
        db.update(tbname, values, "STOCKCODE=?", new String[]{String.valueOf(item.getStockcode())});
        db.close();
    }

    public List<StockItem> listAll(){
        List<StockItem> stockList = null;
        SQLiteDatabase db = dbhelper.getReadableDatabase();
        Cursor cursor = db.query(tbname, null, null, null, null, null, null);
        StockItem item;
        if(cursor!=null){
            stockList = new ArrayList<StockItem>();
            while(cursor.moveToNext()){
                item = new StockItem();
                item.setId(cursor.getInt(cursor.getColumnIndex("ID")));
                item.setStockname(cursor.getString(cursor.getColumnIndex("STOCKNAME")));
                item.setStockcode(cursor.getString(cursor.getColumnIndex("STOCKCODE")));
                item.setStockprice(cursor.getString(cursor.getColumnIndex("STOCKPRICE")));
                stockList.add(item);
            }
            cursor.close();
        }
        db.close();
        return stockList;
    }

    public StockItem findByCode(String stockcode){
        SQLiteDatabase db = dbhelper.getWritableDatabase();
        Cursor cursor = db.query(tbname, null, "STOCKCODE=?", new String[]{stockcode},null, null,null);
        StockItem item = null;
        if(cursor!=null && cursor.moveToFirst()){
            item = new StockItem();
            item.setId(cursor.getInt(cursor.getColumnIndex("ID")));
            item.setStockname(cursor.getString(cursor.getColumnIndex("STOCKNAME")));
            item.setStockcode(cursor.getString(cursor.getColumnIndex("STOCKCODE")));
            item.setStockprice(cursor.getString(cursor.getColumnIndex("STOCKPRICE")));
            cursor.close();
        }else{
            item = new StockItem();
            item.setStockcode("0");
        }
        db.close();
        return item;
    }

    public StockItem findByName(String stockname){
        SQLiteDatabase db = dbhelper.getWritableDatabase();
        Cursor cursor = db.query(tbname, null, "STOCKNAME=?", new String[]{stockname},null, null,null);
        StockItem item = null;
        if(cursor!=null && cursor.moveToFirst()){
            item = new StockItem();
            item.setId(cursor.getInt(cursor.getColumnIndex("ID")));
            item.setStockname(cursor.getString(cursor.getColumnIndex("STOCKNAME")));
            item.setStockcode(cursor.getString(cursor.getColumnIndex("STOCKCODE")));
            item.setStockprice(cursor.getString(cursor.getColumnIndex("STOCKPRICE")));
            cursor.close();
        }
        db.close();
        return item;
    }

    public StockItem findById(int id){
        SQLiteDatabase db = dbhelper.getWritableDatabase();
        Cursor cursor = db.query(tbname, null, "ID=?", new String[]{String.valueOf(id)},
                null, null,null);

        StockItem item = null;
        if(cursor!=null && cursor.moveToFirst()){
            item = new StockItem();
            item.setId(cursor.getInt(cursor.getColumnIndex("ID")));
            item.setStockname(cursor.getString(cursor.getColumnIndex("STOCKNAME")));
            item.setStockcode(cursor.getString(cursor.getColumnIndex("STOCKCODE")));
            item.setStockprice(cursor.getString(cursor.getColumnIndex("STOCKPRICE")));
            cursor.close();
        }
        db.close();
        return item;
    }


}
