package com.example.projectx;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class menu_orders extends SQLiteOpenHelper {

    public static final String dbName = "menuOrders.db";
    public static final String tableName = "menu";

    public menu_orders(Context context)
    {
        super(context, dbName, null, 5);
    }

    @Override
    public void onCreate(SQLiteDatabase db)
    {
        db.execSQL("CREATE TABLE menu (ID INTEGER PRIMARY KEY AUTOINCREMENT, Name TEXT, Price TEXT)");
        db.execSQL("CREATE TABLE currentOrder (ID INTEGER PRIMARY KEY AUTOINCREMENT, item_name TEXT , quantity INTEGER )");
        db.execSQL("CREATE TABLE allOrders (ID INTEGER PRIMARY KEY AUTOINCREMENT, item_id INTEGER, quantity INTEGER, status TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        db.execSQL("DROP TABLE IF EXISTS menu");
        db.execSQL("DROP TABLE IF EXISTS currentOrder");
        db.execSQL("DROP TABLE IF EXISTS allOrders");
        onCreate(db);
    }

    public boolean insertData(String name, String price)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("Name",name);
        values.put("Price",price);
        long result = db.insert("menu",null,values);
        if(result == -1)
        {
            return false;
        }
        else
            return true;
    }

    public boolean insertAllOrder(int id, int quantity)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("item_id",id);
        values.put("quantity",quantity);
        values.put("status","processing");
        long result = db.insert("allOrders",null,values);
        if(result == -1)
        {
            return false;
        }
        else
            return true;
    }

    public boolean insertCurrent(String name , int quantity)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("item_name",name);
        values.put("quantity",quantity);
        long result = db.insert("currentOrder",null,values);
        if(result == -1)
        {
            return false;
        }
        else
            return true;
    }

    public Cursor getAllOrderData()
    {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("select allOrders.ID, menu.Name,allOrders.quantity,status,menu.Price,menu.ID  from allOrders JOIN menu WHERE allOrders.ID = menu.ID"/*todo yahi kuch jhol hai ye name eqaul nahi kar sakte*/,null);
        return res;
    }

    public Cursor getData()
    {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("select * from menu",null);
        return res;
    }

    public Cursor getCurrentData()
    {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("select * from currentOrder",null);
        return res;
    }

    public int delete(String id)
    {
        SQLiteDatabase db =  this.getWritableDatabase();
        return db.delete("menu","ID = ?",new String[]{id});
    }

    public String getId (String item)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT ID FROM menu WHERE Name = ?",new String[]{item});
        cursor.moveToFirst();
        return cursor.getString(0);
    }

    public String getName(String id)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT Name FROM menu WHERE ID = ?", new String[]{id});
        cursor.moveToFirst();
        return cursor.getString(0);
    }

    public String getPrice(String id)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT Price FROM menu WHERE ID = ?", new String[]{id});
        cursor.moveToFirst();
        return cursor.getString(0);
    }

    public void clearOrder()
    {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM currentOrder");
    }
}
