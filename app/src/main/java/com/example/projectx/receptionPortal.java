package com.example.projectx;

import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class receptionPortal extends AppCompatActivity {

    AutoCompleteTextView itemName;
    TextView quantity;
    TextView total;
    Button add;
    Button clear;
    menu_orders menu;
    ListView current;
    Button clearOrder;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reception_portal);

        menu = new menu_orders(this);

        itemName = findViewById(R.id.nameRecep);
        quantity = findViewById(R.id.quantityRecep);
        total = findViewById(R.id.totalRecep);
        add = findViewById(R.id.addRecep);
        clear = findViewById(R.id.clearRecep);
        current = findViewById(R.id.listCurrent);
        clearOrder = findViewById(R.id.clearOrder);


        ArrayList<String> orders = new ArrayList();
        Cursor cursor1 = menu.getCurrentData();
        ArrayAdapter arrayAdapterList;
        while(cursor1.moveToNext())
        {
            orders.add("Item: "+cursor1.getString(1) + "\nQuantity: "+cursor1.getString(2));
        }
        arrayAdapterList = new ArrayAdapter(receptionPortal.this,android.R.layout.simple_list_item_1, orders);
        current.setAdapter(arrayAdapterList);

        Cursor cursor = menu.getData();
        ArrayList<String> name = new ArrayList<>();
        while (cursor.moveToNext())
        {
            name.add(cursor.getString(1));
            ArrayAdapter Adapter = new ArrayAdapter(this,android.R.layout.simple_list_item_1, name);
            itemName.setAdapter(Adapter);
        }

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                if(itemName.getText().toString().equals("")  || quantity.getText().toString().equals(""))
                {
                    Toast.makeText(receptionPortal.this,"Please fill all the fields",Toast.LENGTH_SHORT).show();
                }

                else
                {
                    String id = menu.getId(itemName.getText().toString());
                    int price = Integer.parseInt(menu.getPrice(id));
                    int quan = Integer.parseInt(quantity.getText().toString());
                    boolean isIns = menu.insertCurrent(Integer.parseInt(id),quan);
                    if (isIns)
                        Toast.makeText(receptionPortal.this, "Item Added", Toast.LENGTH_SHORT).show();
                    else
                        Toast.makeText(receptionPortal.this, "Item Addition Failed", Toast.LENGTH_SHORT).show();

                    int orderTotal = Integer.parseInt(total.getText().toString());
                    orderTotal+= price*quan;
                    total.setText(String.valueOf(orderTotal));
                    itemName.setText("");
                    quantity.setText("");

                    ArrayList<String> orders = new ArrayList();
                    Cursor cursor1 = menu.getCurrentData();
                    ArrayAdapter arrayAdapterList;
                    while(cursor1.moveToNext())
                    {
                        orders.add("Item: "+cursor1.getString(1) + "\nQuantity: "+cursor1.getString(2));
                    }
                    arrayAdapterList = new ArrayAdapter(receptionPortal.this,android.R.layout.simple_list_item_1, orders);
                    current.setAdapter(arrayAdapterList);
                }
            }
        });
        clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                itemName.setText("");
                quantity.setText("");
            }
        });

        clearOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                menu.clearOrder();
                itemName.setText("");
                quantity.setText("");
                total.setText("0");
                ArrayList<String> orders = new ArrayList();
                final ArrayAdapter arrayAdapterList;
                arrayAdapterList = new ArrayAdapter(receptionPortal.this,android.R.layout.simple_list_item_1, orders);
                current.setAdapter(arrayAdapterList);
                Toast.makeText(receptionPortal.this,"Order cleared",Toast.LENGTH_SHORT).show();
            }
        });
    }
}
