package com.example.projectx;

import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

public class allOrders extends AppCompatActivity {

    ListView lv;
    menu_orders orders;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_orders);

        orders = new menu_orders(this);
        lv = findViewById(R.id.allOrderList);

        ArrayList<String> orderList = new ArrayList<>();
        Cursor cursor = orders.getAllOrderData();
        ArrayAdapter adapter;

        while(cursor.moveToNext())
        {
            orderList.add("order ID : "+cursor.getString(0)+"\n Item : "+cursor.getString(1)+"\nQuantity : "+cursor.getString(2)+"\nStatus : "+cursor.getString(3));
        }
        adapter = new ArrayAdapter(allOrders.this,android.R.layout.simple_list_item_1, orderList);
        lv.setAdapter(adapter);

    }
}
