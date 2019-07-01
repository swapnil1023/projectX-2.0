package com.example.projectx;

import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class businessReport extends AppCompatActivity {

    menu_orders orders;
    TextView totalSell;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_business_report);

        orders = new menu_orders(this);
        totalSell = findViewById(R.id.totalSell);

        Cursor cursor = orders.getAllOrderData();
        int total = 0;
        while(cursor.moveToNext())
        {
            total+= Integer.parseInt(cursor.getString(4)) * Integer.parseInt(cursor.getString(2));
        }

        totalSell.setText(String.valueOf(total));

    }
}
