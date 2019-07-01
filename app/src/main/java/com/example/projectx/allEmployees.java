package com.example.projectx;

import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

public class allEmployees extends AppCompatActivity {

    ListView lv;
    EmpData empDB;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_employees);

        empDB = new EmpData(this);

        lv = findViewById(R.id.lvMenu);
        Cursor cursor = empDB.getData();

        ArrayList<String> name = new ArrayList();
        while(cursor.moveToNext())
        {
            name.add("ID:  "+cursor.getString(0)+"\nname:  "+cursor.getString(1)+ "\nType:  "+ cursor.getString(2)+ "\nPassword:  "+ cursor.getString(3));
            ArrayAdapter arrayAdapter = new ArrayAdapter(this,android.R.layout.simple_list_item_1, name);
            lv.setAdapter(arrayAdapter);
        }
    }
}
