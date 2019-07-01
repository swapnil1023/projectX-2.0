package com.example.projectx;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
public class EmpData extends SQLiteOpenHelper {

    public static final String dbName = "EmployeeData.db";
    public static final String TableName = "empTable";
    public static final String col1 = "ID";
    public static final String col2 = "Name";
    public static final String col3 = "Type";
    public static final String col4 = "Password";

    public static final String TablePass = "AdminPassword";

    public EmpData( Context context)
    {
        super(context, dbName , null, 5);
    }

    @Override
    public void onCreate(SQLiteDatabase db)
    {
        db.execSQL("CREATE TABLE "+ TableName +" (ID INTEGER PRIMARY KEY AUTOINCREMENT, Name TEXT, Type Integer, Password Text)");
        db.execSQL("CREATE TABLE "+ TablePass +" (Password text PRIMARY KEY)");
        ContentValues values = new ContentValues();
        values.put("Password","123");
        db.insert(TablePass,null,values);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        db.execSQL("DROP TABLE IF EXISTS "+ TableName);
        db.execSQL("DROP TABLE IF EXISTS "+ TablePass);
        onCreate(db);
    }


    public boolean insertData(String name, int type)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(col2,name);
        values.put(col3,type);
        values.put(col4,"0000");
        long result = db.insert(TableName,null,values);
        if(result == -1)
        {
            return false;
        }
        else
            return true;
    }


    public Cursor getData()
    {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("select * from empTable",null);
        return res;
    }


    public int delete(String id)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete("empTable","ID = ?", new String[] {id});
    }


    public String getName(String id)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT Name FROM empTable WHERE ID = ?", new String[]{id});
        cursor.moveToFirst();
        return cursor.getString(0);
    }


    public String getId(String id)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor;
        cursor = db.rawQuery("SELECT ID FROM empTable WHERE ID = ?",new String[]{id});
        cursor.moveToFirst();
        return cursor.getString(0);
    }


    public String getEmpPass(String id)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor;
        cursor = db.rawQuery("SELECT Password FROM empTable WHERE ID = ?",new String[]{id});
        cursor.moveToFirst();
        return cursor.getString(0);
    }

    public String getType(String id)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT Type FROM empTable WHERE ID = ?", new String[]{id});
        cursor.moveToFirst();
        return cursor.getString(0);
    }

    public boolean changeEmpPass(String newPass,String id)
    {
        SQLiteDatabase db = this.getWritableDatabase();

        String strSQL = "UPDATE empTable SET Password = \'"+ newPass +"\' WHERE ID = " + id;
        db.execSQL(strSQL);
        if(this.getEmpPass(id).equals(newPass))
            return true;
        else
          return false;
    }


    public boolean changePass(String pass)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM AdminPassword");

        ContentValues values = new ContentValues();
        values.put("Password",pass);
        long result = db.insert(TablePass,null,values);
        if(result == -1)
        {
            return false;
        }
        else
            return true;
    }

    public String getPass()
    {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("select Password from AdminPassword",null);
        cursor.moveToFirst();
        String name = cursor.getString(0);
        return name;
    }
}
